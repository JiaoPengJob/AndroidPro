package com.tch.youmuwa.ui.activity.mine;

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
import com.tch.youmuwa.bean.parameters.DealdetailEmployer;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.DealdetailEmployerResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.view.RecyclerViewDecoration;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 雇主/交易明细
 */
public class TransactionDetailsActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvTDAll)
    TextView tvTDAll;
    @BindView(R.id.tvTDWeek)
    TextView tvTDWeek;
    @BindView(R.id.tvTDMonth)
    TextView tvTDMonth;
    @BindView(R.id.refreshTransactionDetails)
    SmartRefreshLayout refreshTransactionDetails;
    @BindView(R.id.rvTransactionDetails)
    RecyclerView rvTransactionDetails;
    @BindView(R.id.llTdNet)
    LinearLayout llTdNet;

    private PresenterImpl<Object> presenter;
    private int index = 0;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private CommonAdapter adapter;
    private String date = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
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
        refreshTransactionDetails.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDealdetailEmployer();
            }
        });
        netShow();
    }

    private void netShow() {
        if (HelperUtil.isNetworkConnected(TransactionDetailsActivity.this)) {
            llTdNet.setVisibility(View.GONE);
            refreshTransactionDetails.setVisibility(View.VISIBLE);
            getDealdetailEmployer();
        } else {
            llTdNet.setVisibility(View.VISIBLE);
            refreshTransactionDetails.setVisibility(View.GONE);
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
     * 获取雇主交易明细列表信息
     */
    private void getDealdetailEmployer() {
        mSVProgressHUD.showWithStatus("加载中...");
        DealdetailEmployer dealdetailEmployer = new DealdetailEmployer(
                date,
                index
        );
        presenter = new PresenterImpl<Object>(TransactionDetailsActivity.this);
        presenter.onCreate();
        presenter.getdealdetailemployer(dealdetailEmployer);
        presenter.attachView(dealdetailEmployerView);
    }

    private ClientBaseView<Object> dealdetailEmployerView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshTransactionDetails.isRefreshing()) {
                refreshTransactionDetails.finishRefresh();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(TransactionDetailsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                DealdetailEmployerResult dealdetailEmployer = (DealdetailEmployerResult) GsonUtil.parseJson(baseBean.getData(), DealdetailEmployerResult.class);
                initListData(dealdetailEmployer.getMsg_list());
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshTransactionDetails.isRefreshing()) {
                refreshTransactionDetails.finishRefresh();
            }
            Log.e("Error", "dealdetailEmployerView" + result);
        }
    };

    /**
     * 加载列表
     */
    private void initListData(List<DealdetailEmployerResult.MsgListBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTransactionDetails.setLayoutManager(layoutManager);
        rvTransactionDetails.addItemDecoration(new RecyclerViewDecoration(this, "#F0F0F0", 1, false));
        rvTransactionDetails.setAdapter(adapter = new CommonAdapter<DealdetailEmployerResult.MsgListBean>(this, R.layout.item_transaction_details, list) {
            @Override
            protected void convert(ViewHolder holder, DealdetailEmployerResult.MsgListBean item, int position) {
                holder.setText(R.id.tvTDRemark, item.getType());
                holder.setText(R.id.tvTDDetailDate, HelperUtil.StringDateToSingle(item.getComplete_date() + " "));
                if (item.getIsin() == 0) {
                    holder.setTextColor(R.id.tvTDDetailPrice, R.color.red_msg);
                    holder.setText(R.id.tvTDDetailPrice, "-" + String.valueOf(item.getMoney()));
                } else {
                    holder.setTextColor(R.id.tvTDDetailPrice, R.color.green_31d09a);
                    holder.setText(R.id.tvTDDetailPrice, "+" + String.valueOf(item.getMoney()));
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * 全部
     */
    @OnClick(R.id.tvTDAll)
    public void tvTDAll() {
        date = "all";
        netShow();
        tvTDAll.setBackgroundResource(R.drawable.oval_guide_button);
        tvTDAll.setTextColor(Color.parseColor("#FFFFFF"));
        tvTDWeek.setBackgroundResource(R.drawable.oval_frame_trans);
        tvTDWeek.setTextColor(Color.parseColor("#444444"));
        tvTDMonth.setBackgroundResource(R.drawable.oval_frame_trans);
        tvTDMonth.setTextColor(Color.parseColor("#444444"));
    }

    /**
     * 近一周
     */
    @OnClick(R.id.tvTDWeek)
    public void tvTDWeek() {
        date = "week";
        netShow();
        tvTDAll.setBackgroundResource(R.drawable.oval_frame_trans);
        tvTDAll.setTextColor(Color.parseColor("#444444"));
        tvTDWeek.setBackgroundResource(R.drawable.oval_guide_button);
        tvTDWeek.setTextColor(Color.parseColor("#FFFFFF"));
        tvTDMonth.setBackgroundResource(R.drawable.oval_frame_trans);
        tvTDMonth.setTextColor(Color.parseColor("#444444"));
    }

    /**
     * 近一月
     */
    @OnClick(R.id.tvTDMonth)
    public void tvTDMonth() {
        date = "month";
        netShow();
        tvTDAll.setBackgroundResource(R.drawable.oval_frame_trans);
        tvTDAll.setTextColor(Color.parseColor("#444444"));
        tvTDWeek.setBackgroundResource(R.drawable.oval_frame_trans);
        tvTDWeek.setTextColor(Color.parseColor("#444444"));
        tvTDMonth.setBackgroundResource(R.drawable.oval_guide_button);
        tvTDMonth.setTextColor(Color.parseColor("#FFFFFF"));
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatTransactionDetails() {
        TransactionDetailsActivity.this.finish();
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
                TransactionDetailsActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
