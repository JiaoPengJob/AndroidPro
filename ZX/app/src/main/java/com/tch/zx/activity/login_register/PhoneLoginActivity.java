package com.tch.zx.activity.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.LoginResultBean;
import com.tch.zx.http.presenter.LoginPresenter;
import com.tch.zx.http.view.LoginView;
import com.tch.zx.util.ConstantData;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.util.SharedPrefsUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 手机登录页面
 */
public class PhoneLoginActivity extends BaseActivity {

    /**
     * 手机号输入框
     */
    @BindView(R.id.et_phone_num)
    EditText et_phone_num;

    /**
     * 密码输入框
     */
    @BindView(R.id.et_pwd_phone)
    EditText et_pwd_phone;

    /**
     * 密码是否可见图标
     */
    @BindView(R.id.iv_is_visible_pwd)
    ImageView iv_is_visible_pwd;

    /**
     * 头标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    /**
     * 密码是否可见,默认为false不可见
     */
    private boolean isVisiblePwd = false;
    /**
     * 登录接口
     */
    private LoginPresenter loginPresenter;
    /**
     * 跳转
     */
    private Intent intent;
    /**
     * 用户数据dao
     */
    private UserBeanDao userBeanDao;
    /**
     * 数据库session
     */
    private DaoSession daoSession;

//    17319311613    a123456789
//    15275519710    123456789

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 加载组件信息
     */
    public void initView() {
        tv_title_top_all.setText("登录");
        loginPresenter = new LoginPresenter(this);
    }

    /**
     * 密码是否可见
     */
    @OnClick(R.id.iv_is_visible_pwd)
    public void isVisibleOnClick() {
        if (isVisiblePwd) {
            //不可见图标设置为可见图标
            iv_is_visible_pwd.setImageDrawable(getResources().getDrawable(R.mipmap.eye_open));
            //密码设置为不可见状态
            et_pwd_phone.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //转换是否可见的状态
            isVisiblePwd = false;
        } else {
            //可见图标设置为不可见图标
            iv_is_visible_pwd.setImageDrawable(getResources().getDrawable(R.mipmap.eye_close));
            //密码设置为可见状态
            et_pwd_phone.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //转换是否可见的状态
            isVisiblePwd = true;
        }
    }

    /**
     * 登录点击事件
     */
    @OnClick(R.id.bt_login_phone)
    public void loginOnclick() {
        //判断手机号是否为空
        if (TextUtils.isEmpty(et_phone_num.getText().toString())) {
            Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
        }
        //判断密码是否为空
        else if (TextUtils.isEmpty(et_pwd_phone.getText().toString())) {
            Toast.makeText(this, "密码不能为空!", Toast.LENGTH_SHORT).show();
        } else {
            //判断手机号格式是否正确
            if (!HelperUtil.isMobileNO(et_phone_num.getText().toString())) {
                Toast.makeText(this, "手机号码格式不正确!", Toast.LENGTH_SHORT).show();
            } else {
                //进行登录访问
                phoneLogin(et_phone_num.getText().toString(), et_pwd_phone.getText().toString());
            }
        }
//        Toast.makeText(PhoneLoginActivity.this, "点击登录", Toast.LENGTH_SHORT).show();
//        phoneLogin(et_phone_num.getText().toString(), et_pwd_phone.getText().toString());
    }

    /**
     * 头标题返回点击事件
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBackOnClick() {
        intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 找回密码点击事件
     */
    @OnClick(R.id.tv_find_pwd)
    public void findPwdOnClick() {
        intent = new Intent(this, FindPasswordBackActivity.class);
        startActivity(intent);
    }

    /**
     * 手机号登录
     */
    private void phoneLogin(String name, String pwd) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", name);
        map.put("userPassword", pwd);

        String data = HelperUtil.getParameter(map);

        loginPresenter.onCreate();
        loginPresenter.login(data);
        loginPresenter.attachView(loginView);

    }

    private LoginView loginView = new LoginView() {
        @Override
        public void onSuccess(LoginResultBean baseResultBean) {
            if (baseResultBean != null) {
                if (baseResultBean.getRet().equals("1")) {
                    if (baseResultBean.getResult().getResponseObject() != null) {
                        SharedPrefsUtil.getValue(PhoneLoginActivity.this, "loginType", ConstantData.LOGIN_TYPE_LOGINED);
                        initCacheDao(baseResultBean.getResult().getResponseObject());
                        connect(baseResultBean.getResult().getResponseObject().getYunToken());
                        intent = new Intent(PhoneLoginActivity.this, ChiefActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(PhoneLoginActivity.this, "登录失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "LoginView:==" + result);
        }
    };

    /**
     * 对于用户数据进行长缓存,判断是否直接进入主页
     *
     * @param loginBean
     */
    private void initCacheDao(LoginResultBean.ResultBean.ResponseObjectBean loginBean) {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        userBeanDao.deleteAll();
        UserBean userBean = HelperUtil.loginBeanToUserBean(1, loginBean);
        userBeanDao.insert(userBean);
    }

    /**
     * 连接服务器，在整个应用程序全局，只需要调用一次
     * 如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     *              //     * @param callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.e("Main", "--onIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.e("Main", "--onSuccess========userid==" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("Main", "--onError" + errorCode);
            }
        });
    }

}
