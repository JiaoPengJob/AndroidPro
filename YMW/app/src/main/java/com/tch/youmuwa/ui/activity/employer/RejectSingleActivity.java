package com.tch.youmuwa.ui.activity.employer;

import android.os.Bundle;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 需求中心/拒单
 */
public class RejectSingleActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_single);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("需求中心");
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatRejectSingle() {
        RejectSingleActivity.this.finish();
    }
}
