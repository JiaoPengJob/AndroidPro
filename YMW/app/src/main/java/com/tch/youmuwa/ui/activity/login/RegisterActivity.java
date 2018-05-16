package com.tch.youmuwa.ui.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.tch.youmuwa.bean.parameters.RegisterParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.HelperUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivtiy {

    /**
     * 加载组件
     */
    @BindView(R.id.tvRegisterType)
    TextView tvRegisterType;
    @BindView(R.id.ivRegisterType)
    ImageView ivRegisterType;
    @BindView(R.id.tvGetVerificationCode)
    TextView tvGetVerificationCode;
    @BindView(R.id.btRegister)
    Button btRegister;
    @BindView(R.id.llTermsInfo)
    LinearLayout llTermsInfo;
    @BindView(R.id.ivTermsSelect)
    ImageView ivTermsSelect;
    @BindView(R.id.etRegisterPhone)
    EditText etRegisterPhone;
    @BindView(R.id.etRegisterCode)
    EditText etRegisterCode;
    @BindView(R.id.etRegisterPwd)
    EditText etRegisterPwd;
    @BindView(R.id.etRegisterRePwd)
    EditText etRegisterRePwd;
    @BindView(R.id.ivRegisterPwdEye)
    ImageView ivRegisterPwdEye;
    @BindView(R.id.ivReRegisterPwdEye)
    ImageView ivReRegisterPwdEye;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.llParentRegister)
    LinearLayout llParentRegister;
    /**
     * 设置的参数
     */
    private boolean isEmployer;//获取注册类型
    private Timer timer;
    private TimerTask timerTask;//计时器
    private int index = 60;//时间显示
    private boolean isTermsSelect = false;//是否同意用户协议
    private int termsSelect = 0;//用户协议状态:1,同意;2,不同意(必须为1)
    private Intent intent;//跳转
    private PresenterImpl<Object> presenter;//接口
    private boolean isPwdClose = true;//密码是否隐藏
    private boolean isRePwdClose = true;//确认密码是否隐藏
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("注册");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        isEmployer = getIntent().getBooleanExtra("isEmployer", true);
        if (isEmployer) {
            tvRegisterType.setText("雇主");
            ivRegisterType.setImageResource(R.mipmap.user_type_select);
            tvGetVerificationCode.setTextColor(Color.parseColor("#31D09A"));
            btRegister.setText("立即注册");
            btRegister.setBackgroundResource(R.drawable.employer_button_sel);
            llTermsInfo.setVisibility(View.VISIBLE);
        } else {
            tvRegisterType.setText("工人");
            ivRegisterType.setImageResource(R.mipmap.user_work_select);
            tvGetVerificationCode.setTextColor(Color.parseColor("#FBC83F"));
            btRegister.setText("下一步");
            btRegister.setBackgroundResource(R.drawable.worker_button_sel);
            llTermsInfo.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.llParentRegister)
    public void hideInput() {
        HelperUtil.hideInput(RegisterActivity.this, llParentRegister);
    }

    /**
     * 密码是否可见
     */
    @OnClick(R.id.ivRegisterPwdEye)
    public void reRegisterPwdEye() {
        if (isPwdClose) {
            isPwdClose = false;
            ivRegisterPwdEye.setImageResource(R.mipmap.pwd_open);
            etRegisterPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isPwdClose = true;
            ivRegisterPwdEye.setImageResource(R.mipmap.pwd_close);
            etRegisterPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 确认密码是否可见
     */
    @OnClick(R.id.ivReRegisterPwdEye)
    public void registerPwdEye() {
        if (isRePwdClose) {
            isRePwdClose = false;
            ivReRegisterPwdEye.setImageResource(R.mipmap.pwd_open);
            etRegisterRePwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isRePwdClose = true;
            ivReRegisterPwdEye.setImageResource(R.mipmap.pwd_close);
            etRegisterRePwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 是否同意服务条款
     */
    @OnClick(R.id.ivTermsSelect)
    public void termsSelect() {
        if (isTermsSelect) {
            isTermsSelect = false;
            termsSelect = 0;
            ivTermsSelect.setImageResource(R.mipmap.terms_service_not_select);
        } else {
            isTermsSelect = true;
            termsSelect = 1;
            ivTermsSelect.setImageResource(R.mipmap.terms_service_select);
        }
    }

    /**
     * 立即注册
     */
    @OnClick(R.id.btRegister)
    public void registerInto() {
        if (isEmployer) {
            if (!TextUtils.isEmpty(etRegisterCode.getText().toString())) {
                register(1);
            }
        } else {
            termsSelect = 1;
            if (!TextUtils.isEmpty(etRegisterCode.getText().toString())) {
                register(2);
            }
        }
    }

    /**
     * 服务条款
     */
    @OnClick(R.id.tvTermsServiceContent)
    public void termsServiceContent() {
        intent = new Intent(RegisterActivity.this, TermsServiceContentActivity.class);
        intent.putExtra("activity", "RegisterActivity");
        startActivity(intent);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvGetVerificationCode)
    public void getVerificationCode() {
        if (!TextUtils.isEmpty(etRegisterPhone.getText().toString())) {
            if (HelperUtil.isMobileNO(etRegisterPhone.getText().toString())) {
                if (tvGetVerificationCode.getText().toString().equals("获取验证码")) {
                    getCode();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvGetVerificationCode.setText(index + "秒");
                if (index < 0) {
                    index = 60;
                    tvGetVerificationCode.setText("获取验证码");
                    cancelTimer();
                }
            }
        }
    };

    /**
     * 清空计时器
     */
    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        handler.removeMessages(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    /**
     * 注册
     */
    private void register(int type) {
        mSVProgressHUD.showWithStatus("加载中...");
        RegisterParam registerParam = new RegisterParam(etRegisterPhone.getText().toString()
                , Integer.parseInt(etRegisterCode.getText().toString())
                , etRegisterPwd.getText().toString()
                , etRegisterRePwd.getText().toString()
                , type
                , termsSelect);
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.register(registerParam);
        presenter.attachView(registerView);
    }

    private ClientBaseView<Object> registerView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(RegisterActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                if (isEmployer) {
                    //跳转进入登录页面，将数据传递过去并展示
                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                } else {
                    intent = new Intent(RegisterActivity.this, WorkerPerfectDataActivity.class);
                    intent.putExtra("phone", etRegisterPhone.getText().toString());
                    intent.putExtra("pwd", etRegisterPwd.getText().toString());
                    startActivity(intent);
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "registerView--" + result);
        }
    };

    /**
     * 获取验证码
     */
    private void getCode() {
        CacheDataManager.clearAllCache(RegisterActivity.this);
        GetCodeParam getCodeParam = new GetCodeParam(etRegisterPhone.getText().toString(), 1);
        PresenterImpl<Object> presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.attachView(getCodeView);
        presenter.getsms(getCodeParam);
    }

    private ClientBaseView<Object> getCodeView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (presenter != null) {
                presenter.onStop();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(RegisterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                etRegisterCode.setFocusable(true);
                etRegisterCode.setFocusableInTouchMode(true);
                etRegisterCode.requestFocus();
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        index--;
                        handler.sendEmptyMessage(0);
                    }
                };
                timer.schedule(timerTask, 0, 1000);
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "getCodeView--Error:--" + result);
        }
    };

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
                RegisterActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatBack() {
        RegisterActivity.this.finish();
    }
}
