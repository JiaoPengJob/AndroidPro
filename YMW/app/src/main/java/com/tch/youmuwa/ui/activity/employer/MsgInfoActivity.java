package com.tch.youmuwa.ui.activity.employer;

import android.os.Bundle;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息详情
 */
public class MsgInfoActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvMsgTime)
    TextView tvMsgTime;
    @BindView(R.id.tvMsgContent)
    TextView tvMsgContent;

    private MsgResult.MsgListBean msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_info);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("系统通知");
        if (getIntent().getSerializableExtra("msg") != null) {
            msg = (MsgResult.MsgListBean) getIntent().getSerializableExtra("msg");
            tvMsgTime.setText(msg.getCreate_date());
            tvMsgContent.setText(msg.getContent());
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibRetreat)
    public void retreatMsg() {
        MsgInfoActivity.this.finish();
    }
}
