package com.tch.zx.fragment.line;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.line.greatclass.GreatClassItemPlayerActivity;
import com.tch.zx.activity.line.greatclass.GreatClassPlayerActivity;
import com.tch.zx.adapter.line.ClassAdapter;
import com.tch.zx.adapter.line.HotOnlinePlayerRemdAdapter;
import com.tch.zx.bean.SmallListBean;
import com.tch.zx.http.presenter.SmallListPresenter;
import com.tch.zx.http.view.SmallListView;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门小课/推荐
 */

public class ClassFragment extends Fragment {
    /**
     * 列表
     */
    @BindView(R.id.rv_fgt_purchassed)
    RecyclerView rv_fgt_purchassed;
    @BindView(R.id.refreshPurchased)
    SmartRefreshLayout refreshClass;
    /**
     * Fragment父布局
     */
    private View viewRoot;

    /**
     * 列表适配器
     */
    private ClassAdapter classAdapter;
    /**
     * 跳转
     */
    private Intent intent;
    /**
     * 接口
     */
    private SmallListPresenter smallListPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_purchased, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        initView();
        refreshClass.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initView();
            }
        });
        return viewRoot;
    }

    /**
     * 初始化
     */
    private void initView() {
        smallListPresenter = new SmallListPresenter(getContext());
        smallListPresenter.onCreate();
        smallListPresenter.attachView(smallListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", "0");
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        String data = HelperUtil.getParameter(map);
        smallListPresenter.smallList(data);
    }

    /**
     * 加载数据
     */
    private void setRvData(final List<SmallListBean.ResponseObject> list) {
        classAdapter = new ClassAdapter(getContext(), list);
        rv_fgt_purchassed.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_fgt_purchassed.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 10, false));
        rv_fgt_purchassed.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(getContext(), GreatClassPlayerActivity.class);
                intent.putExtra("intentType", "class");
                intent.putExtra("appUserId", list.get(position).getAppUserId());
                intent.putExtra("smallClassId", String.valueOf(list.get(position).getSmallClassId()));
                intent.putExtra("videoPath", list.get(position).getVideoPath());
                intent.putExtra("viewNum", list.get(position).getViewNum());
                intent.putExtra("viewId", list.get(position).getVideoId());
                startActivity(intent);
            }
        });
    }

    /**
     * 接口回调
     */
    SmallListView smallListView = new SmallListView() {
        @Override
        public void onSuccess(SmallListBean smallListBean) {
            if (refreshClass.isRefreshing()) {
                refreshClass.finishRefresh();
            }
            if (smallListBean.getResult().getResponseObject() != null && smallListBean.getResult().getResponseObject().size() != 0) {
                setRvData(smallListBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            if (refreshClass.isRefreshing()) {
                refreshClass.finishRefresh();
            }
            Log.e("Error", "smallListView:==" + result);
        }
    };

}
