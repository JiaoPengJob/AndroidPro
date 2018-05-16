package com.tch.zx.fragment.contacts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.activity.contacts.AddLabelActivity;
import com.tch.zx.activity.message.FriendInfoActivity;
import com.tch.zx.adapter.contacts.IndustryAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.UserFollowIndustryResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.FlowTagGroup;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 人脉/合作/行业
 */

public class IndustryFragment extends Fragment {

    /**
     * 列表
     */
    @BindView(R.id.rvCooperationList)
    RecyclerView rvCooperationList;
    /**
     * 默认布局内容
     */
    @BindView(R.id.info_default_industry)
    RelativeLayout info_default_industry;
    @BindView(R.id.refreshFIndustry)
    SmartRefreshLayout refreshFIndustry;

    private View viewRoot;
    /**
     * 适配器
     */
    private IndustryAdapter industryAdapter;

    private BasePresenter<Object> presenter;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private List<UserFollowIndustryResult.ResponseObjectBean> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_industry, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    /**
     * 初始化
     */
    private void initView() {
        daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        refreshFIndustry.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                userFollowIndustry();
            }
        });
        userFollowIndustry();
    }

    private void userFollowIndustry() {
        presenter = new BasePresenter<Object>(getActivity());
        presenter.onCreate();
        presenter.attachView(userFollowIndustryView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        String data = HelperUtil.getParameter(map);
        presenter.userFollowIndustry(data);
    }

    private BaseView<Object> userFollowIndustryView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshFIndustry.isRefreshing()) {
                refreshFIndustry.finishRefresh();
            }
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                UserFollowIndustryResult userFollowIndustryResult = (UserFollowIndustryResult) GsonUtil.parseJson(baseResultBean.getResult(), UserFollowIndustryResult.class);
                list = userFollowIndustryResult.getResponseObject();
                setRvData();
            }
        }

        @Override
        public void onError(String result) {
            if (refreshFIndustry.isRefreshing()) {
                refreshFIndustry.finishRefresh();
            }
            Log.e("ZX", "userFollowIndustryView接口错误" + result);
        }
    };


    /**
     * 加载数据
     */
    private void setRvData() {
        industryAdapter = new IndustryAdapter(getContext(), list);
        rvCooperationList.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rvCooperationList.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 10, false));
        rvCooperationList.setAdapter(industryAdapter);
        industryAdapter.setOnItemClickListener(new IndustryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                intent.putExtra("activity", "Other");
                intent.putExtra("userId", list.get(position).getAppUserId());
                startActivity(intent);
            }
        });
    }

}
