package com.tch.kuwanx.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.LoginResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.HomeActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.SharedPrefsUtil;
import com.tch.kuwanx.utils.Utils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.UMWXHandler;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.etLoginPhone)
    EditText etLoginPhone;
    @BindView(R.id.etLoginPwd)
    EditText etLoginPwd;

    private Intent intent;
    private UMShareAPI mShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        mShareAPI = UMShareAPI.get(this);
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.llLoginForgetPwd)
    public void loginForgetPwd() {
        intent = new Intent(this, ForgetPwdActivity.class);
        startActivity(intent);
    }

    /**
     * 登录
     */
    @OnClick(R.id.btLogin)
    public void login() {
        if (TextUtils.isEmpty(etLoginPhone.getText().toString())) {
            Toasty.warning(LoginActivity.this, "请输入手机号！", Toast.LENGTH_SHORT, false).show();
        } else if (TextUtils.isEmpty(etLoginPwd.getText().toString())) {
            Toasty.warning(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT, false).show();
        } else {
            if (Utils.isPhone(etLoginPhone.getText().toString())) {
                //成功，调用接口
                loginHttp(etLoginPhone.getText().toString(), etLoginPwd.getText().toString());
            } else {
                Toasty.warning(LoginActivity.this, "手机号格式不正确！", Toast.LENGTH_SHORT, false).show();
            }
        }
    }

    /**
     * 注册
     */
    @OnClick(R.id.btLoginRegistered)
    public void loginRegistered() {
        intent = new Intent(this, RegisteredActivity.class);
        startActivity(intent);
    }

    /**
     * QQ登录
     */
    @OnClick(R.id.ibLoginQQ)
    public void loginQQ() {
        mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
    }

    /**
     * 微信登录
     */
    @OnClick(R.id.ibLoginWeChat)
    public void loginWeChat() {
        UMWXHandler wxHandler = new UMWXHandler();
        wxHandler.release();
        mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
    }

    private UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            intent = new Intent(LoginActivity.this, SetUpLoginPwdActivity.class);
            intent.putExtra("openId", data.get("openid"));
            startActivity(intent);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toasty.warning(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT, false).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toasty.warning(LoginActivity.this, "取消登录！", Toast.LENGTH_SHORT, false).show();
        }
    };

    /**
     * 页面回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 手机号登录
     */
    private void loginHttp(String phone, String pwd) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        map.put("password", pwd);
        String params = EncryptionUtil.getParameter(LoginActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/userLogin.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        LoginActivity.this, "登录中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        LoginResult loginResult = (LoginResult) GsonUtil.json2Object(response, LoginResult.class);
                        if (loginResult != null && loginResult.getRet().equals("1")) {
                            SharedPrefsUtil.putValue(LoginActivity.this, "hasLogin", true);
                            DaoUtils.saveUserId(LoginActivity.this, loginResult, etLoginPhone.getText().toString());
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            Toasty.warning(LoginActivity.this, loginResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
