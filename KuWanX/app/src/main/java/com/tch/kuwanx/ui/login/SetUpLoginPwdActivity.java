package com.tch.kuwanx.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.application.BaseApplication;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.https.MyApiResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheResult;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 设置登录密码（第三方登录时跳转）
 */
public class SetUpLoginPwdActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etSetUpPhone)
    EditText etSetUpPhone;
    @BindView(R.id.etSetUpCode)
    EditText etSetUpCode;
    @BindView(R.id.btSetUpCode)
    Button btSetUpCode;
    @BindView(R.id.etSetUpPwd)
    EditText etSetUpPwd;
    @BindView(R.id.etSetUpConfirmPwd)
    EditText etSetUpConfirmPwd;
    @BindView(R.id.tvSetUpWarnContent)
    TextView tvSetUpWarnContent;
    @BindView(R.id.llSetUpWarn)
    LinearLayout llSetUpWarn;

    private Timer timer;
    private TimerTask timerTask;
    private int timeNum = 60;
    private String openId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_login_pwd);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        if (getIntent().getStringExtra("openId") != null) {
            openId = getIntent().getStringExtra("openId");
        }
        tvTitleContent.setText("设置登录密码");
    }

    /**
     * 提交
     */
    @OnClick(R.id.btSetUpSubmit)
    public void setUpSubmit() {
        if (!TextUtils.isEmpty(etSetUpPhone.getText().toString())
                && !TextUtils.isEmpty(etSetUpCode.getText().toString())
                && !TextUtils.isEmpty(etSetUpPwd.getText().toString())
                && !TextUtils.isEmpty(etSetUpConfirmPwd.getText().toString())) {
            llSetUpWarn.setVisibility(View.GONE);
            if (Utils.isPhone(etSetUpPhone.getText().toString())) {
                llSetUpWarn.setVisibility(View.GONE);
                if (etSetUpPwd.getText().toString().equals(etSetUpConfirmPwd.getText().toString())) {
                    llSetUpWarn.setVisibility(View.GONE);
                    //访问接口
//                    threeLoginHttp();
                } else {
                    llSetUpWarn.setVisibility(View.VISIBLE);
                    tvSetUpWarnContent.setText("两次密码不一致，请重新输入！！");
                }
            } else {
                llSetUpWarn.setVisibility(View.VISIBLE);
                tvSetUpWarnContent.setText("手机号格式不正确！");
            }
            llSetUpWarn.setVisibility(View.GONE);
        } else {
            llSetUpWarn.setVisibility(View.VISIBLE);
            tvSetUpWarnContent.setText("填写信息不完整！");
        }
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.btSetUpCode)
    public void setUpCode() {
        if (!TextUtils.isEmpty(etSetUpPhone.getText().toString())) {
            if (Utils.isPhone(etSetUpPhone.getText().toString())) {
                if (btSetUpCode.getText().toString().equals("发送验证码")) {
//                    codeHttp();
                }
            } else {
                Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发送验证码
     */
    private void getCode() {
        //如果访问接口成功，则调用
        etSetUpCode.setFocusable(true);
        etSetUpCode.setFocusableInTouchMode(true);
        etSetUpCode.requestFocus();
        btSetUpCode.setTextColor(Color.parseColor("#333333"));
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timeNum--;
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    btSetUpCode.setText(timeNum + "秒");
                    if (timeNum < 0) {
                        timeNum = 60;
                        btSetUpCode.setTextColor(Color.parseColor("#FFFFFF"));
                        btSetUpCode.setText("发送验证码");
                        cancelTimer();
                    }
                    break;
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

    /**
     * 标题栏返回
     */
    @OnClick(R.id.ibTitleBack)
    public void titleBack() {
        SetUpLoginPwdActivity.this.finish();
    }

    /**
     * 获取验证码
     */
    private void codeHttp() {
        EasyHttp.post("")
                .params("", "")
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.wtf("获取验证码请求错误：" + e.getMessage());
                        Toasty.warning(SetUpLoginPwdActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        Logger.wtf("获取验证码请求：成功");
                        Toasty.warning(SetUpLoginPwdActivity.this, "获取验证码成功！", Toast.LENGTH_SHORT, false).show();
                        getCode();
                    }
                });
    }

    /**
     * 第三方登录
     */
    private void threeLoginHttp() {
        EasyHttp.post("")
                .params("", "")
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new CallBackProxy<MyApiResult<CacheResult<String>>, CacheResult<String>>
                        (new ProgressDialogCallBack<CacheResult<String>>(
                                HttpUtils.getIProgressDialog(SetUpLoginPwdActivity.this, "正在提交..."),
                                true, true) {
                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                Logger.wtf("第三方请求错误：" + e.getMessage());
                                Toasty.warning(SetUpLoginPwdActivity.this, "登录失败！", Toast.LENGTH_SHORT, false).show();
                            }

                            @Override
                            public void onSuccess(CacheResult<String> response) {
                                //请求成功
                                if (response.isFromCache) {
                                    Logger.wtf("第三方请求：来自缓存");
                                } else {
                                    Logger.wtf("第三方请求：来自网络");
                                }
                            }
                        }) {
                });
    }

}
