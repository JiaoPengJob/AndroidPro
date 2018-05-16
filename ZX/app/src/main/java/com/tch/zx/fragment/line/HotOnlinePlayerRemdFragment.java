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
import com.tch.zx.activity.line.online.OnLinePlayerItemMainActivity;
import com.tch.zx.adapter.line.HotOnlinePlayerRemdAdapter;
import com.tch.zx.http.bean.result.LiveListResultBean;
import com.tch.zx.http.presenter.LiveListPresenter;
import com.tch.zx.http.view.LiveListView;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门直播/推荐
 */

public class HotOnlinePlayerRemdFragment extends Fragment {

    @BindView(R.id.rv_fgt_purchassed)
    RecyclerView rv_fgt_purchassed;

    @BindView(R.id.refreshPurchased)
    SmartRefreshLayout refreshPurchased;

    private String typeId = "";

    /**
     * Fragment父布局
     */
    private View viewRoot;

    /**
     * 列表适配器
     */
    private HotOnlinePlayerRemdAdapter hotOnlinePlayerRemdAdapter;

    private LiveListPresenter liveListPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_purchased, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        typeId = getArguments().getString("typeId");
        liveList();

        refreshPurchased.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                liveList();
            }
        });
    }

    /**
     * 加载数据
     */
    private void setRvData(List<LiveListResultBean.ResultBean.ResponseObjectBean> list) {
        hotOnlinePlayerRemdAdapter = new HotOnlinePlayerRemdAdapter(getContext(), list);
        rv_fgt_purchassed.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_fgt_purchassed.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 10, false));
        rv_fgt_purchassed.setAdapter(hotOnlinePlayerRemdAdapter);
        hotOnlinePlayerRemdAdapter.setOnItemClickListener(new HotOnlinePlayerRemdAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                hotOnlinePlayerRemdAdapter.setSelectedPosition(position);
                Intent intent = new Intent(getContext(), OnLinePlayerItemMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void liveList() {
        liveListPresenter = new LiveListPresenter(getContext());
        liveListPresenter.onCreate();
        liveListPresenter.attachView(liveListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeId);
        map.put("pageSize", "1");
        map.put("pageNow", "10");

        String data = HelperUtil.getParameter(map);
        liveListPresenter.liveList(data);

    }

    private LiveListView liveListView = new LiveListView() {
        @Override
        public void onSuccess(LiveListResultBean liveListResultBean) {
            if (refreshPurchased.isRefreshing()) {
                refreshPurchased.finishRefresh();
            }
            if (liveListResultBean.getResult().getResponseObject() != null
                    && liveListResultBean.getResult().getResponseObject().size() > 0) {
                setRvData(liveListResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            if (refreshPurchased.isRefreshing()) {
                refreshPurchased.finishRefresh();
            }
            Log.e("Error", "liveListView:==" + result);
        }
    };

}
