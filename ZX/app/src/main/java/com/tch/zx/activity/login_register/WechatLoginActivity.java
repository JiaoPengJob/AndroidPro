package com.tch.zx.activity.login_register;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
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
import com.tch.zx.util.WXUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 微信登录页面
 */
public class WechatLoginActivity extends BaseActivity {
    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    /**
     * 手机号输入框
     */
    @BindView(R.id.et_phone_num_wechat_login)
    EditText et_phone_num_wechat_login;

    /**
     * 验证码输入框
     */
    @BindView(R.id.et_code_wechat_login)
    EditText et_code_wechat_login;

    /**
     * 获取验证码
     */
    @BindView(R.id.tv_getCode_wechat_login)
    TextView tv_getCode_wechat_login;

    /**
     * 计时器
     */
    private Timer timer;
    private TimerTask timerTask;
    /**
     * 获取验证码的时间间隔
     */
    private int minute = 60;
    /**
     * 跳转
     */
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_wechat_login);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化页面信息
     */
    private void initView() {
        tv_title_top_all.setText("登录");
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_getCode_wechat_login)
    public void getCodeWechatLogin() {
        if (!TextUtils.isEmpty(et_phone_num_wechat_login.getText().toString())) {
            if (HelperUtil.isMobileNO(et_phone_num_wechat_login.getText().toString())) {
                getSms();
            } else {
                Toast.makeText(this, "手机号码格式不正确!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    private BasePresenter presenter;

    /**
     * 获取验证码
     */
    private void getSms() {
        presenter = new BasePresenter<Object>(WechatLoginActivity.this);
        presenter.onCreate();
        presenter.attachView(smsView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", et_phone_num_wechat_login.getText().toString());

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

    /**
     * 下一步
     */
    @OnClick(R.id.bt_next_wechat_login)
    public void nextLogin() {
        if (TextUtils.isEmpty(et_phone_num_wechat_login.getText().toString())) {
            Toast.makeText(this, "手机号不能为空!", Toast.LENGTH_SHORT).show();
        } else if (!HelperUtil.isMobileNO(et_phone_num_wechat_login.getText().toString())) {
            Toast.makeText(this, "手机号格式不正确!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_code_wechat_login.getText().toString())) {
            Toast.makeText(this, "验证码不能为空!", Toast.LENGTH_SHORT).show();
        } else {
            if (et_code_wechat_login.getText().toString().equals(smsResult.getCode())) {
                //如果都符合要求,则跳转
                Intent intent = new Intent(this, RegisterSureActivity.class);
                intent.putExtra("phone", et_phone_num_wechat_login.getText().toString());
                intent.putExtra("wxId", "100");
                intent.putExtra("type", "wx");
                startActivity(intent);
            } else {
                Toast.makeText(this, "验证码不正确!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBackLast() {
        this.finish();
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
                    tv_getCode_wechat_login.setText(minute + "秒");
                    tv_getCode_wechat_login.setEnabled(false);
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
        tv_getCode_wechat_login.setText("获取验证码");
        tv_getCode_wechat_login.setEnabled(true);
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

}
