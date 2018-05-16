package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开户行/弃用
 */
public class OpenBankActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ivCcbSel)
    ImageView ivCcbSel;
    @BindView(R.id.ivIcbcSel)
    ImageView ivIcbcSel;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_bank);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("开户行");
    }

    /**
     * 建设银行
     */
    @OnClick(R.id.rlCCB)
    public void ccb() {
        ivCcbSel.setVisibility(View.VISIBLE);
        ivIcbcSel.setVisibility(View.GONE);
        intent = new Intent();
        intent.putExtra("openBank", "");
        setResult(RESULT_OK, intent);
        OpenBankActivity.this.finish();
    }

    /**
     * 工商银行
     */
    @OnClick(R.id.rlICBC)
    public void icbc() {
        ivCcbSel.setVisibility(View.GONE);
        ivIcbcSel.setVisibility(View.VISIBLE);
        intent = new Intent();
        intent.putExtra("openBank", "");
        setResult(RESULT_OK, intent);
        OpenBankActivity.this.finish();
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void ibRetreatOpenBank() {
        OpenBankActivity.this.finish();
    }
}
