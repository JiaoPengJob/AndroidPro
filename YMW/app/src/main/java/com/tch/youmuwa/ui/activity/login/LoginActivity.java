package com.tch.youmuwa.ui.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.LoginParam;
import com.tch.youmuwa.bean.parameters.ThirdLoginParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.ThirdLoginResult;
import com.tch.youmuwa.bean.result.WorkerLoginResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerMainActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.UMAPIShareHandler;
import com.umeng.weixin.handler.UmengWXHandler;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivtiy {

    /**
     * 加载组件
     */
    @BindView(R.id.ivEmployer)
    ImageView ivEmployer;/*雇主图标*/
    @BindView(R.id.ivWorker)
    ImageView ivWorker;/*工人图标*/
    @BindView(R.id.etLoginPhone)
    EditText etLoginPhone;/*手机号*/
    @BindView(R.id.etLoginPwd)
    EditText etLoginPwd;/*密码*/
    @BindView(R.id.ivPwdEye)
    ImageView ivPwdEye;/*密码是否可见*/
    @BindView(R.id.tvFreeRegister)
    TextView tvFreeRegister;
    @BindView(R.id.ivLoginTypeInfo)
    ImageView ivLoginTypeInfo;
    @BindView(R.id.llOtherLoginType)
    LinearLayout llOtherLoginType;
    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.llParentLogin)
    LinearLayout llParentLogin;
    /**
     * 设置的参数
     */
    private boolean isEmployer = true;//是否是雇主登录
    private boolean isPwdClose = true;//是否显示密码
    private Intent intent;//跳转
    private PresenterImpl<Object> presenter;//接口
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库
    private String phone = "", pwd = "";//手机号和密码
    private UMShareAPI mShareAPI;//友盟api
    private int type = 1;//登录类型:1,雇主;2,工人
    private SVProgressHUD mSVProgressHUD;//加载显示
    private int wq = 0;
    private boolean ifPwdLogin = true;

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
        //初始化友盟
        mShareAPI = UMShareAPI.get(LoginActivity.this);
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
    }

    @OnClick(R.id.llParentLogin)
    public void hideInput() {
        HelperUtil.hideInput(LoginActivity.this, llParentLogin);
    }

    /**
     * 雇主图标选择
     */
    @OnClick(R.id.ivEmployer)
    public void selectEmplyer() {
        isEmployer = true;
        ivEmployer.setImageResource(R.mipmap.user_type_select);
        ivWorker.setImageResource(R.mipmap.user_type_not_select);
        tvFreeRegister.setTextColor(Color.parseColor("#31D09A"));
        ivLoginTypeInfo.setVisibility(View.VISIBLE);
        llOtherLoginType.setVisibility(View.VISIBLE);
        btLogin.setBackgroundResource(R.drawable.employer_button_sel);
    }

    /**
     * 工人图标选择
     */
    @OnClick(R.id.ivWorker)
    public void selectWorker() {
        isEmployer = false;
        ivEmployer.setImageResource(R.mipmap.user_type_not_select);
        ivWorker.setImageResource(R.mipmap.user_work_select);
        tvFreeRegister.setTextColor(Color.parseColor("#FBC83F"));
        ivLoginTypeInfo.setVisibility(View.GONE);
        llOtherLoginType.setVisibility(View.GONE);
        btLogin.setBackgroundResource(R.drawable.worker_button_sel);
    }

    /**
     * 密码是否可见
     */
    @OnClick(R.id.ivPwdEye)
    public void pwdEye() {
        if (isPwdClose) {
            isPwdClose = false;
            ivPwdEye.setImageResource(R.mipmap.pwd_open);
            etLoginPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isPwdClose = true;
            ivPwdEye.setImageResource(R.mipmap.pwd_close);
            etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 免费注册
     */
    @OnClick(R.id.tvFreeRegister)
    public void freeRegister() {
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("isEmployer", isEmployer);
        startActivity(intent);
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.tvForgetPwd)
    public void forgetPwd() {
        intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
        intent.putExtra("isEmployer", isEmployer);
        startActivity(intent);
    }

    /**
     * 立即登录
     */
    @OnClick(R.id.btLogin)
    public void login() {
        if (!TextUtils.isEmpty(etLoginPhone.getText().toString()) && !TextUtils.isEmpty(etLoginPwd.getText().toString())) {
            //判断手机号格式是否正确
            if (HelperUtil.isMobileNO(etLoginPhone.getText().toString())) {
                if (isEmployer) {
                    type = 1;
                    loginClient();
                } else {
                    type = 2;
                    loginClient();
                }
            }
        } else {
            Toast.makeText(LoginActivity.this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 微信登录
     */
    @OnClick({R.id.llWechatLogin, R.id.ivWechatLogin})
    public void wechatLogin() {
        wq = 1;
        UmengWXHandler wxHandler = new UmengWXHandler();
        wxHandler.release();
        mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
    }

    /**
     * QQ登录
     */
    @OnClick({R.id.llQQLogin, R.id.ivQQLogin})
    public void qqLogin() {
        wq = 2;
        if (isEmployer) {
            mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
        }
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
            thirdlogin(wq, data.get("openid"));
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(LoginActivity.this, "登录失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 第三方登录
     *
     * @param thirdtype
     * @param openId
     */
    private void thirdlogin(int thirdtype, String openId) {
        mSVProgressHUD.showWithStatus("加载中...");
        ThirdLoginParam thirdLoginParam = new ThirdLoginParam(thirdtype, openId);
        presenter = new PresenterImpl<Object>(LoginActivity.this);
        presenter.onCreate();
        presenter.thirdlogin(thirdLoginParam);
        presenter.attachView(thirdLoginView);
    }

    private ClientBaseView<Object> thirdLoginView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() == 1) {
                ThirdLoginResult tl = (ThirdLoginResult) GsonUtil.parseJson(baseBean.getData(), ThirdLoginResult.class);
                SharedPrefsUtil.putValue(LoginActivity.this, "isEmployer", true);
                SharedPrefsUtil.putValue(LoginActivity.this, "ifPwdLogin", false);
                setUserDaoInfo(tl.getMobile(), "未设置", 0, 0);
                intent = new Intent(LoginActivity.this, EmployerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (baseBean.getState() == 2) {
                //未绑定手机号
                intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("TAG", "Error:--" + result);
        }
    };

    /**
     * 登录链接
     */
    private void loginClient() {
        mSVProgressHUD.showWithStatus("加载中...");
        LoginParam loginParam = new LoginParam(
                type,
                etLoginPhone.getText().toString(),
                etLoginPwd.getText().toString()
        );
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.attachView(loginView);
        presenter.login(loginParam);
    }

    private ClientBaseView<Object> loginView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_LONG).show();
            } else {
                if (baseBean.getData() != null && !baseBean.getData().toString().equals("")) {
                    SharedPrefsUtil.putValue(LoginActivity.this, "ifPwdLogin", true);
                    if (isEmployer) {
                        SharedPrefsUtil.putValue(LoginActivity.this, "isEmployer", true);
                        WorkerLoginResult loginResult = (WorkerLoginResult) GsonUtil.parseJson(baseBean.getData(), WorkerLoginResult.class);
                        setUserDaoInfo(loginResult.getMobile(), etLoginPwd.getText().toString(), type, loginResult.getResult());
                        intent = new Intent(LoginActivity.this, EmployerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        SharedPrefsUtil.putValue(LoginActivity.this, "isEmployer", false);
                        WorkerLoginResult workerLoginResult = (WorkerLoginResult) GsonUtil.parseJson(baseBean.getData(), WorkerLoginResult.class);
                        if (workerLoginResult.getResult() == 0) {
                            intent = new Intent(LoginActivity.this, WorkerPerfectDataActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("workerLogin", workerLoginResult);
                            bundle.putString("pwd", etLoginPwd.getText().toString());
                            bundle.putInt("type", type);
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            setUserDaoInfo(workerLoginResult.getMobile(), etLoginPwd.getText().toString(), type, workerLoginResult.getResult());
                            intent = new Intent(LoginActivity.this, WorkerMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "loginView:--" + result);
        }
    };

    /**
     * 保存用户信息
     */
    private void setUserDaoInfo(String phone, String pwd, int type, int result) {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.deleteAll();
        UserInfo userinfo = new UserInfo();
        userinfo.setPhone(phone);
        userinfo.setPwd(pwd);
        userinfo.setType(type);
        userinfo.setResult(result);
        userInfoDao.insert(userinfo);
    }

    /**
     * 监听后退物理按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bl = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
                if (presenter != null) {
                    presenter.onStop();
                }
                bl = false;
            } else {
                System.exit(0);
                bl = true;
            }
        }
        return bl;
    }
}
