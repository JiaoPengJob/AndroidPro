package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退押金
 */
public class RefundDepositActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_deposit);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("押金");
        btTitleFeatures.setText("押金明细");
        btTitleFeatures.setVisibility(View.VISIBLE);
    }

    /**
     * 押金明细
     */
    @OnClick(R.id.btTitleFeatures)
    public void depositDetails() {
        Intent intent = new Intent(RefundDepositActivity.this, DepositDetailsActivity.class);
        startActivity(intent);
    }

    /**
     * 申请退款
     */
    @OnClick(R.id.btRequestRefund)
    public void requestRefund() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void refundDepositBack() {
        RefundDepositActivity.this.finish();
    }
}
