package com.tch.zx.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.mine.PurchaseHistoryAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.PayRecordResult;
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
import butterknife.OnClick;

/**
 * 购买记录
 */
public class PurchaseHistoryActivity extends BaseActivity {

    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 数据列表
     */
    @BindView(R.id.rv_purchase_history_mine_main)
    RecyclerView rv_purchase_history_mine_main;
    @BindView(R.id.refreshPurchaseHistory)
    SmartRefreshLayout refreshPurchaseHistory;

    private BasePresenter<Object> presenter;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private List<PayRecordResult.ResponseObjectBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_purchase_history);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("购买记录");
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        payRecord();
        refreshPurchaseHistory.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                payRecord();
            }
        });
    }

    private void payRecord() {
        presenter = new BasePresenter<Object>(PurchaseHistoryActivity.this);
        presenter.onCreate();
        presenter.attachView(payRecordView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.payRecord(data);
    }

    private BaseView<Object> payRecordView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshPurchaseHistory.isRefreshing()) {
                refreshPurchaseHistory.finishRefresh();
            }
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                PayRecordResult payRecordResult = (PayRecordResult) GsonUtil.parseJson(baseResultBean.getResult(), PayRecordResult.class);
                list = payRecordResult.getResponseObject();
                setRecyclerViewData();
            }
        }

        @Override
        public void onError(String result) {
            if (refreshPurchaseHistory.isRefreshing()) {
                refreshPurchaseHistory.finishRefresh();
            }
            Log.e("ZX", "payRecordView接口错误" + result);
        }
    };


    /**
     * 加载购买记录列表数据
     */
    private void setRecyclerViewData() {
        PurchaseHistoryAdapter purchaseHistoryAdapter = new PurchaseHistoryAdapter(this, list);
        rv_purchase_history_mine_main.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_purchase_history_mine_main.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 1, false));
        rv_purchase_history_mine_main.setAdapter(purchaseHistoryAdapter);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnPurchaseHistory() {
        this.finish();
    }
}
