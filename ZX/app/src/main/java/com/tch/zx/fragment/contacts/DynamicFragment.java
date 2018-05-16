package com.tch.zx.fragment.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.adapter.contacts.DynamicAdapter;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.GetDynamicListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.id;

/**
 * 动态
 */

public class DynamicFragment extends Fragment {

    private View viewRoot;

    /**
     * 列表
     */
    @BindView(R.id.rv_dynamic_main)
    RecyclerView rv_dynamic_main;
    @BindView(R.id.refreshFDynamic)
    SmartRefreshLayout refreshFDynamic;

    /**
     * 适配器
     */
    private DynamicAdapter dynamicAdapter;
    private BasePresenter<Object> presenter;
    private GetDynamicListResult getDynamicListResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_dynamic, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);

        getDynamicList();
        refreshFDynamic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDynamicList();
            }
        });
        return viewRoot;
    }

    private void getDynamicList() {
        presenter = new BasePresenter<Object>(getActivity());
        presenter.onCreate();
        presenter.attachView(getDynamicListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNow", "1");
        map.put("pageSize", "10");


        String data = HelperUtil.getParameter(map);
        presenter.getDynamicList(data);
    }

    private BaseView<Object> getDynamicListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getRet() != null) {
                if (refreshFDynamic.isRefreshing()) {
                    refreshFDynamic.finishRefresh();
                }
                if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                    getDynamicListResult = (GetDynamicListResult) GsonUtil.parseJson(baseResultBean.getResult(), GetDynamicListResult.class);
                    setRvData();
                }
            }
        }

        @Override
        public void onError(String result) {
            if (refreshFDynamic.isRefreshing()) {
                refreshFDynamic.finishRefresh();
            }
            Log.e("ZX", "getDynamicListView接口错误" + result);
        }
    };

    /**
     * 加载数据
     */
    private void setRvData() {
        dynamicAdapter = new DynamicAdapter(getContext(), getDynamicListResult.getResponseObject());
        rv_dynamic_main.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_dynamic_main.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 10, false));
        rv_dynamic_main.setAdapter(dynamicAdapter);
    }
}
