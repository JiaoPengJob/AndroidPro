package com.tch.zx.activity.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.util.ConstantData;
import com.tch.zx.util.SharedPrefsUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.yancy.gallerypick.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity {

    //随便看看
    @BindView(R.id.ll_casual_see)
    LinearLayout ll_casual_see;

    //注册按钮
    @BindView(R.id.bt_register_guide)
    Button bt_register_guide;

    //微信登录
    @BindView(R.id.ll_wechat_login)
    LinearLayout ll_wechat_login;

    //手机登录
    @BindView(R.id.ll_phone_login)
    LinearLayout ll_phone_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

    }

    /**
     * 随便看看点击事件
     */
    @OnClick(R.id.ll_casual_see)
    public void casualSeeOnClick() {
        //记录随便看看的状态,也就是未登录状态
        SharedPrefsUtil.putValue(this, "loginType", ConstantData.LOGIN_TYPE_CASUALSEE);
        Intent intent = new Intent(this, ChiefActivity.class);
        startActivity(intent);
    }

    /**
     * 注册点击事件
     */
    @OnClick(R.id.bt_register_guide)
    public void registerGuideOnClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 微信登录点击事件
     */
    @OnClick(R.id.ll_wechat_login)
    public void wechatLoginOnClick() {
//        Intent intent = new Intent(this, WechatLoginActivity.class);
//        startActivity(intent);
        weChatLogin();
    }

    private void weChatLogin() {
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            Toast.makeText(GuideActivity.this, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MyApplication.mWxApi.sendReq(req);
    }

    /**
     * 手机登录点击事件
     */
    @OnClick(R.id.ll_phone_login)
    public void phoneLoginOnClick() {
        Intent intent = new Intent(this, PhoneLoginActivity.class);
        startActivity(intent);
    }

    /**
     * 判断网络状态
     *
     * @param netMobile
     */
    @Override
    public void onNetChange(int netMobile) {
        //根据需要做处理
    }

}
