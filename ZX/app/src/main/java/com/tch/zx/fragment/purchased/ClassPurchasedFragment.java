package com.tch.zx.fragment.purchased;

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
import com.tch.zx.adapter.purchased.ClassPurchasedAdapter;
import com.tch.zx.adapter.purchased.PurchasedAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.TestBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.OrderListResultBean;
import com.tch.zx.http.presenter.OrderListPresenter;
import com.tch.zx.http.view.OrderListView;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tch.zx.application.RxRetrofitApp.getApplication;

/**
 * 已购/小课
 */

public class ClassPurchasedFragment extends Fragment {

    @BindView(R.id.rv_fgt_purchassed)
    RecyclerView rv_fgt_purchassed;
    @BindView(R.id.refreshPurchased)
    SmartRefreshLayout refreshPurchased;
    public View viewRoot;

    /**
     * 接口
     */
    private OrderListPresenter orderListPresenter;
    /**
     * 参数
     */
    private Map<String, Object> map;
    private String data;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_purchased, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        initView();
        refreshPurchased.setOnRefreshListener(new OnRefreshListener() {
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
        orderListPresenter = new OrderListPresenter(getContext());
        orderListPresenter.onCreate();
        orderListPresenter.attachView(orderListView);
        List<UserBean> list = userBeanDao.loadAll();
        if (list != null && list.size() != 0) {
            map = new HashMap<String, Object>();
            map.put("pageNow", "1");
            map.put("pageSize", "10");
            map.put("appUserId", list.get(0).getAppUserId());
            map.put("orderClassType", "2");
        }
        data = HelperUtil.getParameter(map);
        orderListPresenter.orderList(data);
    }

    public void setRvData(List<OrderListResultBean.ResultBean.ResponseObjectBean> list) {
        //已购选项卡显示适配器
        ClassPurchasedAdapter purchasedAdapter = new ClassPurchasedAdapter(getContext(), list);
        rv_fgt_purchassed.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_fgt_purchassed.addItemDecoration(new RecyclerViewDecoration(getContext(), "#949494", 1, true));
        rv_fgt_purchassed.setAdapter(purchasedAdapter);
        purchasedAdapter.setOnItemClickListener(new ClassPurchasedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), GreatClassItemPlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 接口回调
     */
    private OrderListView orderListView = new OrderListView() {
        @Override
        public void onSuccess(OrderListResultBean orderListResultBean) {
            if (refreshPurchased.isRefreshing()) {
                refreshPurchased.finishRefresh();
            }
            if (orderListResultBean.getResult().getResponseObject() != null
                    && orderListResultBean.getResult().getResponseObject().size() != 0) {
                setRvData(orderListResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            if (refreshPurchased.isRefreshing()) {
                refreshPurchased.finishRefresh();
            }
            Log.e("Error", "全部-orderListView:==" + result);
        }
    };
}
