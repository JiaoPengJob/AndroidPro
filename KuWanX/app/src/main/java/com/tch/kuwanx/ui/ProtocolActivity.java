package com.tch.kuwanx.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.tch.kuwanx.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 协议通用页面
 */
public class ProtocolActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("协议");
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void protocolBack() {
        ProtocolActivity.this.finish();
    }
}
