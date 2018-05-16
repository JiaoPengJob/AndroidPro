package com.tch.youmuwa.ui.activity.login;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.ForgetParam;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GetCodeResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivtiy {

    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvGetForgetPwdVerificationCode)
    TextView tvGetForgetPwdVerificationCode;
    @BindView(R.id.etForgetPhone)
    EditText etForgetPhone;
    @BindView(R.id.etForgetCode)
    EditText etForgetCode;
    @BindView(R.id.etForgetNewPwd)
    EditText etForgetNewPwd;
    @BindView(R.id.ivForgetPwdEye)
    ImageView ivForgetPwdEye;
    @BindView(R.id.etReForgetNewPwd)
    EditText etReForgetNewPwd;
    @BindView(R.id.ivReForgetPwdEye)
    ImageView ivReForgetPwdEye;
    @BindView(R.id.llParentForget)
    LinearLayout llParentForget;
    /**
     * 设置的参数
     */
    private Timer timer;
    private TimerTask timerTask;
    private int index = 60;//计时器
    private Intent intent;//跳转
    private PresenterImpl<Object> presenter;//接口
    private boolean isPwdClose = true;//密码是否可见
    private boolean isRePwdClose = true;//确认密码是否可见
    private Dialog dialog;//弹出框
    private boolean isEmployer;//是否为雇主
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        isEmployer = getIntent().getBooleanExtra("isEmployer", true);
        title.setText("忘记密码");
        if (isEmployer) {
            tvGetForgetPwdVerificationCode.setTextColor(Color.parseColor("#31D09A"));
        } else {
            tvGetForgetPwdVerificationCode.setTextColor(Color.parseColor("#FBC83F"));
        }
    }

    @OnClick(R.id.llParentForget)
    public void hideInput() {
        HelperUtil.hideInput(ForgetPwdActivity.this, llParentForget);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvGetForgetPwdVerificationCode)
    public void getForgetPwdVerificationCode() {
        if (!TextUtils.isEmpty(etForgetPhone.getText().toString())) {
            if (HelperUtil.isMobileNO(etForgetPhone.getText().toString())) {
                if (tvGetForgetPwdVerificationCode.getText().toString().equals("获取验证码")) {
                    getCode();
                }
            } else {
                Toast.makeText(ForgetPwdActivity.this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ForgetPwdActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvGetForgetPwdVerificationCode.setText(index + "秒");
                if (index < 0) {
                    index = 60;
                    tvGetForgetPwdVerificationCode.setText("获取验证码");
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
     * 密码是否可见
     */
    @OnClick(R.id.ivForgetPwdEye)
    public void forgetPwdEye() {
        if (isPwdClose) {
            isPwdClose = false;
            ivForgetPwdEye.setImageResource(R.mipmap.pwd_open);
            etForgetNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isPwdClose = true;
            ivForgetPwdEye.setImageResource(R.mipmap.pwd_close);
            etForgetNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 确认密码是否可见
     */
    @OnClick(R.id.ivReForgetPwdEye)
    public void reForgetPwdEye() {
        if (isRePwdClose) {
            isRePwdClose = false;
            ivReForgetPwdEye.setImageResource(R.mipmap.pwd_open);
            etReForgetNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isRePwdClose = true;
            ivReForgetPwdEye.setImageResource(R.mipmap.pwd_close);
            etReForgetNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.btUpdatePwd)
    public void updatePwd() {
        forgetClient();
    }

    /**
     * 忘记密码
     */
    private void forgetClient() {
        mSVProgressHUD.showWithStatus("加载中...");
        ForgetParam forgetParam = new ForgetParam(
                etForgetPhone.getText().toString(),
                etForgetCode.getText().toString(),
                etForgetNewPwd.getText().toString(),
                etReForgetNewPwd.getText().toString()
        );
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.attachView(forgetView);
        presenter.forget(forgetParam);
    }

    private ClientBaseView<Object> forgetView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(ForgetPwdActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
                //跳转进入登录页面，将数据传递过去并展示
                intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                intent.putExtra("phone", etForgetPhone.getText().toString());
                intent.putExtra("pwd", etForgetNewPwd.getText().toString());
                startActivity(intent);
                dialog.cancel();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "forgetView:--" + result);
        }
    };

    /**
     * 展示弹出框
     */
    private void showDialog() {
        dialog = new Dialog(this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_pwd_update_success, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(this) * 0.8);
        p.height = HelperUtil.getScreenHeight(this) / 3;
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void forgetPwdBack() {
        ForgetPwdActivity.this.finish();
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        CacheDataManager.clearAllCache(ForgetPwdActivity.this);
        GetCodeParam getCodeParam = new GetCodeParam(etForgetPhone.getText().toString(), 2);
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.attachView(getCodeView);
        presenter.getsms(getCodeParam);
    }

    private ClientBaseView<Object> getCodeView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(ForgetPwdActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                etForgetCode.setFocusable(true);
                etForgetCode.setFocusableInTouchMode(true);
                etForgetCode.requestFocus();
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
            Log.e("Error", "getCodeView:--" + result);
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
                ForgetPwdActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
