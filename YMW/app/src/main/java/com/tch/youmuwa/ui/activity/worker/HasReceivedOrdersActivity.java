package com.tch.youmuwa.ui.activity.worker;

import android.os.Bundle;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 已接单/弃用
 */
public class HasReceivedOrdersActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_received_orders);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("已接单");
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatHasReceivedOrders() {
        HasReceivedOrdersActivity.this.finish();
    }
}
