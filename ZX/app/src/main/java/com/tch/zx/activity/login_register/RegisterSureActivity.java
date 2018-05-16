package com.tch.zx.activity.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.LoginResultBean;
import com.tch.zx.http.bean.result.SIndustryListResultBean;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.presenter.LoginPresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.http.view.FIndustryListResultBean;
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
 * 注册第二步
 */
public class RegisterSureActivity extends BaseActivity {

    //姓名输入框
    @BindView(R.id.et_user_name_register)
    EditText et_user_name_register;

    //所选择的行业
    @BindView(R.id.tv_text_trade)
    TextView tv_text_trade;

    private String phone, pwd, type, wxId;
    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_register_sure);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra("phone") != null
                && getIntent().getStringExtra("type") != null) {
            phone = getIntent().getStringExtra("phone");
            type = getIntent().getStringExtra("type");
            if (type.equals("wx")) {
                wxId = getIntent().getStringExtra("wx");
            } else {
                pwd = getIntent().getStringExtra("pwd");
            }
        }
    }

    /**
     * 头标题返回点击事件
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBackOnClick() {
        this.finish();
    }

    /**
     * 选择行业
     */
    @OnClick(R.id.ll_select_trade)
    public void selectTradeOnClick() {
        Intent intent = new Intent(this, TradeActivity.class);
        startActivityForResult(intent, 15);
    }

    //完成注册
    @OnClick(R.id.bt_done_register_sure)
    public void doneRegisterSureOnClick() {
        //判断姓名是否为空
        if (TextUtils.isEmpty(et_user_name_register.getText().toString())) {
            Toast.makeText(this, "姓名不能为空!", Toast.LENGTH_SHORT).show();
        }
        //判断行业内容
        else if (tv_text_trade.getText().toString().equals("请选择您的行业")) {
            Toast.makeText(this, "请重新选择您的行业!", Toast.LENGTH_SHORT).show();
        }
        //条件都符合,则跳转
        else {
            if (type.equals("register")) {
                phoneRegistered();
            } else if (type.equals("wx")) {
                weixinRegistered();
            }
        }
    }

    private FIndustryListResultBean.ResultBean.ResponseObjectBean robL;
    private SIndustryListResultBean.ResultBean.ResponseObjectBean robR;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) throws NullPointerException {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 15) {
            if (resultCode == RESULT_OK) {
                robL = (FIndustryListResultBean.ResultBean.ResponseObjectBean) data.getSerializableExtra("lRob");
                robR = (SIndustryListResultBean.ResultBean.ResponseObjectBean) data.getSerializableExtra("rRob");
                tv_text_trade.setText(robL.getIndustryFTypeName() + "  " + robR.getIndustrySTypeName());
            }
        }
    }

    private void phoneRegistered() {
        presenter = new BasePresenter<Object>(RegisterSureActivity.this);
        presenter.onCreate();
        presenter.attachView(phoneRegisteredView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", phone);
        map.put("userPassword", pwd);
        map.put("companyIndustryFType", robL.getIndustryFTypeid());
        map.put("companyIndustrySType", robR.getIndustrySTypeid());
        map.put("appUserName", et_user_name_register.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.phoneRegistered(data);
    }

    private BaseView<Object> phoneRegisteredView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                phoneLogin();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "phoneRegisteredView接口错误" + result);
        }
    };

    private LoginPresenter loginPresenter;

    /**
     * 手机号登录
     */
    private void phoneLogin() {
        loginPresenter = new LoginPresenter(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", phone);
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
                        SharedPrefsUtil.getValue(RegisterSureActivity.this, "loginType", ConstantData.LOGIN_TYPE_LOGINED);
                        initCacheDao(baseResultBean.getResult().getResponseObject());
                        connect(baseResultBean.getResult().getResponseObject().getYunToken());
                        Intent intent = new Intent(RegisterSureActivity.this, ChiefActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.e("TAG", "登录失败!");
//                    Toast.makeText(RegisterSureActivity.this, "登录失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "LoginView:==" + result);
        }
    };

    private UserBeanDao userBeanDao;
    private DaoSession daoSession;

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

    private void weixinRegistered() {
        presenter = new BasePresenter<Object>(this);
        presenter.onCreate();
        presenter.attachView(weixinRegisteredView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", phone);
        map.put("weixinId", wxId);
        map.put("userPassword", "");
        map.put("companyIndustryFType", robL.getIndustryFTypeid());
        map.put("companyIndustrySType", robR.getIndustrySTypeid());
        map.put("appUserName", et_user_name_register.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.weixinRegistered(data);
    }

    private BaseView<Object> weixinRegisteredView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {

            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "weixinRegisteredView接口错误" + result);
        }
    };

}
