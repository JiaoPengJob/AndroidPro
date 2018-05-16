package com.tch.kuwanx.ui.mine.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.login.ForgetPwdActivity;
import com.tch.kuwanx.ui.login.LoginActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.SharedPrefsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("设置");
    }

    /**
     * 关于我们
     */
    @OnClick(R.id.rlAboutUs)
    public void aboutUs() {
        intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    /**
     * 系统消息
     */
    @OnClick(R.id.rlSystemMsg)
    public void systemMsg() {
        intent = new Intent(SettingsActivity.this, SystemMsgActivity.class);
        startActivity(intent);
    }

    /**
     * 意见反馈
     */
    @OnClick(R.id.rlOpinion)
    public void opinion() {
        intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
        startActivity(intent);
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.rlUpdatePwd)
    public void updatePwd() {
        intent = new Intent(SettingsActivity.this, ForgetPwdActivity.class);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btSignOut)
    public void signOut() {
        SharedPrefsUtil.putValue(SettingsActivity.this, "hasLogin", false);
        DaoUtils.clearDao(SettingsActivity.this);
        intent = new Intent(SettingsActivity.this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void settingsBack() {
        SettingsActivity.this.finish();
    }
}
