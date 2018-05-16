package com.tch.zx.activity.login_register;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.SmsResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.ConstantData;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.message;

/**
 * 注册第一步
 */
public class RegisterActivity extends BaseActivity {

    /**
     * 手机号输入框
     */
    @BindView(R.id.et_phone_num_register)
    EditText et_phone_num_register;

    /**
     * 验证码输入框
     */
    @BindView(R.id.et_code_register)
    EditText et_code_register;

    /**
     * 获取验证码
     */
    @BindView(R.id.tv_get_code_register)
    TextView tv_get_code_register;

    /**
     * 密码输入框
     */
    @BindView(R.id.et_pwd_register)
    EditText et_pwd_register;

    /**
     * 是否可见状态图标
     */
    @BindView(R.id.iv_eye_register)
    ImageView iv_eye_register;

    /**
     * 密码是否可见,默认为false不可见
     */
    private boolean isVisiblePwd = false;

    /**
     * 获取验证码的时间间隔
     */
    private int minute = 60;

    /**
     * 计时器
     */
    private Timer timer;
    private TimerTask timerTask;
    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    /**
     * 登录点击事件
     */
    @OnClick(R.id.bt_next_register)
    public void loginOnclick() {
        //判断手机号是否为空
        if (TextUtils.isEmpty(et_phone_num_register.getText().toString())) {
            Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
        }
        //判断手机号格式是否正确
        else if (!HelperUtil.isMobileNO(et_phone_num_register.getText().toString())) {
            Toast.makeText(this, "手机号码格式不正确!", Toast.LENGTH_SHORT).show();
        }
        //判断验证码是否为空
        else if (TextUtils.isEmpty(et_code_register.getText().toString())) {
            Toast.makeText(this, "验证码不能为空!", Toast.LENGTH_SHORT).show();
        }
        //判断密码是否为空
        else if (TextUtils.isEmpty(et_pwd_register.getText().toString())) {
            Toast.makeText(this, "密码不能为空!", Toast.LENGTH_SHORT).show();
        } else {
            if (et_code_register.getText().toString().equals(smsResult.getCode())) {
                //如果都符合要求,则跳转
                Intent intent = new Intent(this, RegisterSureActivity.class);
                intent.putExtra("phone", et_phone_num_register.getText().toString());
                intent.putExtra("pwd", et_pwd_register.getText().toString());
                intent.putExtra("type", "register");
                startActivity(intent);
            } else {
                Toast.makeText(this, "验证码不正确!", Toast.LENGTH_SHORT).show();
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
     * 密码是否可见
     */
    @OnClick(R.id.iv_eye_register)
    public void isVisibleRegisterPwdOnClick() {
        if (isVisiblePwd) {
            //不可见图标设置为可见图标
            iv_eye_register.setImageDrawable(getResources().getDrawable(R.mipmap.eye_open));
            //密码设置为不可见状态
            et_pwd_register.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //转换是否可见的状态
            isVisiblePwd = false;
        } else {
            //可见图标设置为不可见图标
            iv_eye_register.setImageDrawable(getResources().getDrawable(R.mipmap.eye_close));
            //密码设置为可见状态
            et_pwd_register.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //转换是否可见的状态
            isVisiblePwd = true;
        }
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_get_code_register)
    public void getCodeRegisterOnClick() {
        if (!TextUtils.isEmpty(et_phone_num_register.getText().toString())) {
            if (HelperUtil.isMobileNO(et_phone_num_register.getText().toString())) {
                getSms();
            } else {
                Toast.makeText(this, "手机号码格式不正确!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * handler线程
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //计时器运行
                case ConstantData.HANDLER_MESSAGE_CODE_REGISTER:
                    tv_get_code_register.setText(minute + "秒");
                    tv_get_code_register.setEnabled(false);
                    break;
                //计时器停止
                case ConstantData.HANDLER_MESSAGE_CODE_REGISTER_STOP:
                    clearTimer();
                    break;
            }
        }
    };

    /**
     * 清除计时器状态
     */
    private void clearTimer() {
        tv_get_code_register.setText("获取验证码");
        tv_get_code_register.setEnabled(true);
        minute = 60;

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (handler != null) {
            handler.removeMessages(ConstantData.HANDLER_MESSAGE_CODE_REGISTER);
            handler = null;
        }
    }

    /**
     * 获取验证码
     */
    private void getSms() {
        presenter = new BasePresenter<Object>(RegisterActivity.this);
        presenter.onCreate();
        presenter.attachView(smsView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", et_phone_num_register.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.smsUtils(data);
    }

    private SmsResult smsResult;

    private BaseView<Object> smsView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                smsResult = (SmsResult) GsonUtil.parseJson(baseResultBean.getResult(), SmsResult.class);
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (minute > 1) {
                            //发送消息给handler
                            handler.sendEmptyMessage(ConstantData.HANDLER_MESSAGE_CODE_REGISTER);
                            minute--;
                        } else {
                            handler.sendEmptyMessage(ConstantData.HANDLER_MESSAGE_CODE_REGISTER_STOP);
                        }
                    }
                };
                timer.schedule(timerTask, 0, 1000);
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "smsView接口错误" + result);
        }
    };

}
