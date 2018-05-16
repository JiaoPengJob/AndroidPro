package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.BindBankCardParam;
import com.tch.youmuwa.bean.result.IsBindBankResult;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认是否绑定银行卡
 */
public class BindBankCardActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.llHasBindBank)
    LinearLayout llHasBindBank;
    @BindView(R.id.llHasNoBindBank)
    LinearLayout llHasNoBindBank;
    @BindView(R.id.tvBankCardNumber)
    TextView tvBankCardNumber;
    @BindView(R.id.btBindBandCard)
    Button btBindBandCard;

    private IsBindBankResult isBindBankResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("绑定银行卡");
        if (getIntent().getSerializableExtra("isBindBankResult") != null) {
            isBindBankResult = (IsBindBankResult) getIntent().getSerializableExtra("isBindBankResult");
            if (isBindBankResult.getBankcard() == 0) {
                llHasBindBank.setVisibility(View.GONE);
                llHasNoBindBank.setVisibility(View.VISIBLE);
                btBindBandCard.setText("立即绑定");
            } else {
                llHasBindBank.setVisibility(View.VISIBLE);
                llHasNoBindBank.setVisibility(View.GONE);
                tvBankCardNumber.setText(isBindBankResult.getBankcard_id());
                btBindBandCard.setText("换绑");
            }
        }
    }

    /**
     * 换绑
     */
    @OnClick(R.id.btBindBandCard)
    public void bindBandCard() {
        Intent intent = new Intent(BindBankCardActivity.this, BindTheBankCardActivity.class);
        startActivity(intent);
        BindBankCardActivity.this.finish();
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatBindBankCard() {
        BindBankCardActivity.this.finish();
    }
}
