package com.tch.youmuwa.ui.activity.worker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.DealdeTailResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.view.RecyclerViewDecoration;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工人/交易明细
 */
public class WorkerTDActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvWTDAll)
    TextView tvWTDAll;
    @BindView(R.id.tvWTDWeek)
    TextView tvWTDWeek;
    @BindView(R.id.tvWTDMonth)
    TextView tvWTDMonth;
    @BindView(R.id.refreshWTransactionDetails)
    SmartRefreshLayout refreshWTransactionDetails;
    @BindView(R.id.rvWTransactionDetails)
    RecyclerView rvWTransactionDetails;
    @BindView(R.id.llWorkerTDNet)
    LinearLayout llWorkerTDNet;

    private PresenterImpl<Object> presenter;
    private CommonAdapter adapter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private String type = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_td);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("交易明细");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        refreshWTransactionDetails.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                netShow();
            }
        });
        netShow();
    }

    private void netShow() {
        if (HelperUtil.isNetworkConnected(WorkerTDActivity.this)) {
            llWorkerTDNet.setVisibility(View.GONE);
            refreshWTransactionDetails.setVisibility(View.VISIBLE);
            getDealdeTail();
        } else {
            llWorkerTDNet.setVisibility(View.VISIBLE);
            refreshWTransactionDetails.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新
     */
    @OnClick(R.id.btRefreshNet)
    public void refreshNet() {
        netShow();
    }


    /**
     * 获取工人端交易明细
     * all:所有，week：最近一周，month：最近一月
     */
    private void getDealdeTail() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(WorkerTDActivity.this);
        presenter.onCreate();
        presenter.getdealdetail(type);
        presenter.attachView(dealdeTailView);
    }

    private ClientBaseView<Object> dealdeTailView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshWTransactionDetails.isRefreshing()) {
                refreshWTransactionDetails.finishRefresh();
            }

            if (baseBean.getState() == 1) {
                DealdeTailResult dealdeTail = (DealdeTailResult) GsonUtil.parseJson(baseBean.getData(), DealdeTailResult.class);
                initListData(dealdeTail.getList());
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(WorkerTDActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshWTransactionDetails.isRefreshing()) {
                refreshWTransactionDetails.finishRefresh();
            }

            Log.e("Error", "dealdeTailView==" + result);
        }
    };

    /**
     * 加载列表
     */
    private void initListData(List<DealdeTailResult.ListBean> dealdeTails) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(WorkerTDActivity.this);
        rvWTransactionDetails.setLayoutManager(layoutManager);
        rvWTransactionDetails.addItemDecoration(new RecyclerViewDecoration(this, "#F0F0F0", 1, false));
        adapter = new CommonAdapter<DealdeTailResult.ListBean>(WorkerTDActivity.this, R.layout.item_worker_transaction_details, dealdeTails) {
            @Override
            protected void convert(ViewHolder holder, DealdeTailResult.ListBean item, int position) {
                holder.setText(R.id.tvWorkerOrderNumber, item.getOrder_sn());
                holder.setText(R.id.tvWorkerRemark, item.getRemark());
                holder.setText(R.id.tvWorkerDetailDate, HelperUtil.StringDateToSingle(item.getCreate_date()));
                if (item.getBehavior().equals("1")) {
                    holder.setTextColor(R.id.tvWorkerDetailPrice, R.color.green_31d09a);
                    holder.setText(R.id.tvWorkerDetailPrice, "+" + item.getMoney());
                } else {
                    holder.setTextColor(R.id.tvWorkerDetailPrice, R.color.red_msg);
                    holder.setText(R.id.tvWorkerDetailPrice, "-" + item.getMoney());
                }
            }
        };
        if (adapter != null) {
            rvWTransactionDetails.setAdapter(adapter);
        }
    }

    /**
     * 全部
     */
    @OnClick(R.id.tvWTDAll)
    public void wTDAll() {
        type = "all";
        netShow();
        tvWTDAll.setBackgroundResource(R.drawable.oval_worker_type_select);
        tvWTDAll.setTextColor(Color.parseColor("#FFFFFF"));
        tvWTDWeek.setBackgroundResource(R.drawable.oval_frame_trans);
        tvWTDWeek.setTextColor(Color.parseColor("#444444"));
        tvWTDMonth.setBackgroundResource(R.drawable.oval_frame_trans);
        tvWTDMonth.setTextColor(Color.parseColor("#444444"));
    }

    /**
     * 近一周
     */
    @OnClick(R.id.tvWTDWeek)
    public void wTDWeek() {
        type = "week";
        netShow();
        tvWTDAll.setBackgroundResource(R.drawable.oval_frame_trans);
        tvWTDAll.setTextColor(Color.parseColor("#444444"));
        tvWTDWeek.setBackgroundResource(R.drawable.oval_worker_type_select);
        tvWTDWeek.setTextColor(Color.parseColor("#FFFFFF"));
        tvWTDMonth.setBackgroundResource(R.drawable.oval_frame_trans);
        tvWTDMonth.setTextColor(Color.parseColor("#444444"));
    }

    /**
     * 近一月
     */
    @OnClick(R.id.tvWTDMonth)
    public void wTDMonth() {
        type = "month";
        netShow();
        tvWTDAll.setBackgroundResource(R.drawable.oval_frame_trans);
        tvWTDAll.setTextColor(Color.parseColor("#444444"));
        tvWTDWeek.setBackgroundResource(R.drawable.oval_frame_trans);
        tvWTDWeek.setTextColor(Color.parseColor("#444444"));
        tvWTDMonth.setBackgroundResource(R.drawable.oval_worker_type_select);
        tvWTDMonth.setTextColor(Color.parseColor("#FFFFFF"));
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatWTransactionDetails() {
        WorkerTDActivity.this.finish();
    }

    /**
     * 监听后退物理按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bl = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
                if (presenter != null) {
                    presenter.onStop();
                }
                bl = false;
            } else {
                WorkerTDActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
