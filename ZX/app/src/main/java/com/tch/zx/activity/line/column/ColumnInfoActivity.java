package com.tch.zx.activity.line.column;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.http.bean.result.SpecialSubscribeResultBean;
import com.tch.zx.http.presenter.SpecialSubscribePresenter;
import com.tch.zx.http.view.SpecialSubscribeView;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 专栏信息简介页面
 */
public class ColumnInfoActivity extends BaseActivity {

    /**
     * 弹出框取消
     */
    private TextView tv_exit_dialog;
    /**
     * 弹出框确定
     */
    private TextView tv_sure_dialog;
    @BindView(R.id.tvSpecialColumnName)
    TextView tvSpecialColumnName;
    @BindView(R.id.tvSpecialColumnByName)
    TextView tvSpecialColumnByName;
    @BindView(R.id.tvSubscriptionNumber)
    TextView tvSubscriptionNumber;
    @BindView(R.id.tvSpecialColumnIntroduce)
    TextView tvSpecialColumnIntroduce;
    @BindView(R.id.tvSubscriptionNotes)
    TextView tvSubscriptionNotes;
    @BindView(R.id.tv_column_subscription)
    TextView tv_column_subscription;
    @BindView(R.id.refreshLayoutColumnInfo)
    SmartRefreshLayout refreshLayoutColumnInfo;

    private String specialColumnId = "";
    private SpecialSubscribeResultBean.ResultBean.ResponseObjectBean subscribeResultBean;
    private SpecialSubscribePresenter subscribePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_column_info);
        //集成使用Butterknife
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        specialColumnId = getIntent().getStringExtra("specialColumnId");
        subscribePresenter = new SpecialSubscribePresenter(this);
        subscribePresenter.onCreate();
        subscribePresenter.attachView(subscribeView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("specialColumnId", specialColumnId);

        String data = HelperUtil.getParameter(map);
        subscribePresenter.specialSubscribe(data);

        refreshLayoutColumnInfo.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                subscribePresenter.attachView(subscribeView);
            }
        });
    }

    /**
     * 加载数据
     */
    private void initData(SpecialSubscribeResultBean.ResultBean.ResponseObjectBean subscribeResultBean) {
        tvSpecialColumnName.setText(subscribeResultBean.getSpecialColumnName());
        tvSpecialColumnByName.setText(subscribeResultBean.getSpecialColumnByName());
        tvSubscriptionNumber.setText(subscribeResultBean.getSubscriptionNumber() + "人订阅");
        tvSpecialColumnIntroduce.setText(subscribeResultBean.getSpecialColumnIntroduce());
        tvSubscriptionNotes.setText(subscribeResultBean.getSubscriptionNotes());
        tv_column_subscription.setText("订阅" + subscribeResultBean.getSpecialColumnPrice() + "/年");
    }

    /**
     * 后退
     */
    @OnClick(R.id.iv_return_column_info)
    public void returnBack() {
        ColumnInfoActivity.this.finish();
    }

    /**
     * 免费体验
     */
    @OnClick(R.id.tv_column_free_experience)
    public void freeExperience() {
        Intent intent = new Intent(ColumnInfoActivity.this, FreeExperienceActivity.class);
        intent.putExtra("specialColumnId", specialColumnId);
        startActivity(intent);
    }

    /**
     * 订阅
     */
    @OnClick(R.id.tv_column_subscription)
    public void tv_column_subscription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ColumnInfoActivity.this);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_pay_column, null);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        tv_exit_dialog = (TextView) layout.findViewById(R.id.tv_exit_dialog);
        tv_sure_dialog = (TextView) layout.findViewById(R.id.tv_sure_dialog);
        tv_exit_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        tv_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ColumnInfoActivity.this, BuySubscriptionActivity.class);
                intent.putExtra("subscribeResultBean", subscribeResultBean);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * 接口回调
     */
    private SpecialSubscribeView subscribeView = new SpecialSubscribeView() {
        @Override
        public void onSuccess(SpecialSubscribeResultBean specialSubscribeResultBean) {
            if (refreshLayoutColumnInfo.isRefreshing()) {
                refreshLayoutColumnInfo.finishRefresh();
            }
            if (specialSubscribeResultBean.getResult().getResponseObject() != null) {
                subscribeResultBean = specialSubscribeResultBean.getResult().getResponseObject();
                initData(specialSubscribeResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            if (refreshLayoutColumnInfo.isRefreshing()) {
                refreshLayoutColumnInfo.finishRefresh();
            }
            Log.e("Error", "SpecialSubscribeView:==" + result);
        }
    };
}
