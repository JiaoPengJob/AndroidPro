package com.tch.kuwanx.ui.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 测试用页面
 */
public class TestActivity extends BaseActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    /**
     * 已提交
     */
    @OnClick(R.id.btSendSubmit)
    public void btSendSubmit() {
        intent = new Intent(this, SubmitActivity.class);
        startActivity(intent);
    }

    /**
     * 已确认
     */
    @OnClick(R.id.btSendConfirm)
    public void btSendConfirm() {
        intent = new Intent(this, ConfirmActivity.class);
        startActivity(intent);
    }

    /**
     * 已付款
     */
    @OnClick(R.id.btSendPay)
    public void btSendPay() {
        intent = new Intent(this, PayActivity.class);
        startActivity(intent);
    }

    /**
     * 已发货
     */
    @OnClick(R.id.btSendSend)
    public void btSendSend() {
        intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }

    /**
     * 已收货
     */
    @OnClick(R.id.btSendReceived)
    public void btSendReceived() {
        intent = new Intent(this, ReceivedActivity.class);
        startActivity(intent);
    }

    /**
     * 已还货
     */
    @OnClick(R.id.btSendRepay)
    public void btSendRepay() {
        intent = new Intent(this, RepayActivity.class);
        startActivity(intent);
    }

    /**
     * 已确认收货
     */
    @OnClick(R.id.btSendConfirmReceived)
    public void btSendConfirmReceived() {
        intent = new Intent(this, ConfirmReceivedActivity.class);
        startActivity(intent);
    }

    /**
     * 已评价
     */
    @OnClick(R.id.btSendEvaluation)
    public void btSendEvaluation() {
        intent = new Intent(this, EvaluationActivity.class);
        startActivity(intent);
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * 已提交
     */
    @OnClick(R.id.btOtherSubmit)
    public void btOtherSubmit() {
        intent = new Intent(this, OtherSubmitActivity.class);
        startActivity(intent);
    }

    /**
     * 已确认并付款
     */
    @OnClick(R.id.btOtherConfirm)
    public void btOtherConfirm() {
        intent = new Intent(this, PayActivity.class);
        startActivity(intent);
    }

    /**
     * 已发货
     */
    @OnClick(R.id.btOtherSend)
    public void btOtherSend() {
        intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }

    /**
     * 已收货
     */
    @OnClick(R.id.btOtherReceived)
    public void btOtherReceived() {
        intent = new Intent(this, ReceivedActivity.class);
        startActivity(intent);
    }

    /**
     * 已还货
     */
    @OnClick(R.id.btOtherRepay)
    public void btOtherRepay() {
        intent = new Intent(this, RepayActivity.class);
        startActivity(intent);
    }

    /**
     * 已确认收货
     */
    @OnClick(R.id.btOtherConfirmReceived)
    public void btOtherConfirmReceived() {
        intent = new Intent(this, ConfirmReceivedActivity.class);
        startActivity(intent);
    }

    /**
     * 已评价
     */
    @OnClick(R.id.btOtherEvaluation)
    public void btOtherEvaluation() {
        intent = new Intent(this, EvaluationActivity.class);
        startActivity(intent);
    }
}
