package com.tch.zx.activity.mine.settings;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.login_register.FindPasswordBackActivity;
import com.tch.zx.view.BindWeChatPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账号设置
 */
public class AccountSettingActivity extends BaseActivity {

    /**
     * 父布局
     */
    @BindView(R.id.ll_parent_view_account_setting)
    LinearLayout ll_parent_view_account_setting;
    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    /**
     * 跳转页面
     */
    private Intent intent;
    /**
     * 设置popupwindow的布局参数
     */
    private WindowManager.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("设置");
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.rl_account_update_pwd)
    public void intentUpdatePwd() {
        intent = new Intent(this, FindPasswordBackActivity.class);
        startActivity(intent);
    }


    /**
     * 更改手机号
     */
    @OnClick(R.id.rl_account_update_phone_num)
    public void intentUpdatePhoneNumber() {
        intent = new Intent(this, ChangePhoneNumberActivity.class);
        startActivity(intent);
    }

    /**
     * 是否绑定微信
     */
    @OnClick(R.id.rl_is_bind_wechat)
    public void isBindWechat() {
        showPopup();
    }

    /**
     * 是否解除绑定
     */
    private void showPopup() {
        BindWeChatPopupWindow rewardPopupWindow = new BindWeChatPopupWindow(this);
        //设置Popupwindow显示位置（从底部弹出）
        rewardPopupWindow.showAtLocation(ll_parent_view_account_setting, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        rewardPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnAccountSetting() {
        this.finish();
    }
}
