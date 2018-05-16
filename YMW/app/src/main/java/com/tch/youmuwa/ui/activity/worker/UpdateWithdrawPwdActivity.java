package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改提现密码提示页面
 */
public class UpdateWithdrawPwdActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_withdraw_pwd);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("修改提现密码");
    }

    /**
     * 立即设置
     */
    @OnClick(R.id.btSettingWithdrawPwd)
    public void settingWithdrawPwd() {
        Intent intent = new Intent(UpdateWithdrawPwdActivity.this, UpWithdrawPwdActivity.class);
        intent.putExtra("activity", "UpdateWithdrawPwdActivity");
        startActivity(intent);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatUpdateWithdrawPwd() {
        UpdateWithdrawPwdActivity.this.finish();
    }

}
