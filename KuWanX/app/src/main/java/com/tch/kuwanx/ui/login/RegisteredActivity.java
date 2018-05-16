package com.tch.kuwanx.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.RegisteredResult;
import com.tch.kuwanx.result.VerifyCodeResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.ProtocolActivity;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 注册
 */
public class RegisteredActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etRegisteredPhone)
    EditText etRegisteredPhone;
    @BindView(R.id.etRegisteredCode)
    EditText etRegisteredCode;
    @BindView(R.id.etRegisteredPwd)
    EditText etRegisteredPwd;
    @BindView(R.id.etRegisteredConfirmPwd)
    EditText etRegisteredConfirmPwd;
    @BindView(R.id.btRegisteredCode)
    Button btRegisteredCode;
    @BindView(R.id.ivRegisteredTerms)
    ImageView ivRegisteredTerms;

    private boolean isTerm = false;
    private Timer timer;
    private TimerTask timerTask;
    private int timeNum = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tvTitleContent.setText("注册");
    }

    /**
     * 是否同意服务条款
     */
    @OnClick(R.id.ivRegisteredTerms)
    public void registeredTerms() {
        if (isTerm) {
            ivRegisteredTerms.setImageResource(R.mipmap.terms_no_select);
            isTerm = false;
        } else {
            ivRegisteredTerms.setImageResource(R.mipmap.terms_select);
            isTerm = true;
        }
    }

    /**
     * 提交
     */
    @OnClick(R.id.btRegisteredSubmit)
    public void registeredSubmit() {
        if (!TextUtils.isEmpty(etRegisteredPhone.getText().toString())
                && !TextUtils.isEmpty(etRegisteredCode.getText().toString())
                && !TextUtils.isEmpty(etRegisteredPwd.getText().toString())
                && !TextUtils.isEmpty(etRegisteredConfirmPwd.getText().toString())) {
            if (Utils.isPhone(etRegisteredPhone.getText().toString())) {
                if (etRegisteredPwd.getText().toString().equals(etRegisteredConfirmPwd.getText().toString())) {
                    if (isTerm) {
                        //访问注册接口
                        registeredHttp(etRegisteredPhone.getText().toString()
                                , etRegisteredPwd.getText().toString()
                                , etRegisteredCode.getText().toString());
                    } else {
                        Toasty.warning(this, "您未同意条款！", Toast.LENGTH_SHORT, false).show();
                    }
                } else {
                    Toasty.warning(this, "两次密码不一致，请重新输入！", Toast.LENGTH_SHORT, false).show();
                }
            } else {
                Toasty.warning(this, "手机号格式不正确！", Toast.LENGTH_SHORT, false).show();
            }
        } else {
            Toasty.warning(this, "填写信息不完整！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.btRegisteredCode)
    public void registeredCode() {
        if (!TextUtils.isEmpty(etRegisteredPhone.getText().toString())) {
            if (Utils.isPhone(etRegisteredPhone.getText().toString())) {
                if (btRegisteredCode.getText().toString().equals("发送验证码")) {
                    codeHttp(etRegisteredPhone.getText().toString());
                }
            } else {
                Toasty.warning(this, "手机号格式不正确！", Toast.LENGTH_SHORT, false).show();
            }
        } else {
            Toasty.warning(this, "手机号不能为空！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 发送验证码
     */
    private void getCode() {
        //如果访问接口成功，则调用
        etRegisteredCode.setFocusable(true);
        etRegisteredCode.setFocusableInTouchMode(true);
        etRegisteredCode.requestFocus();
        btRegisteredCode.setTextColor(Color.parseColor("#333333"));
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
                    btRegisteredCode.setText(timeNum + "秒");
                    if (timeNum < 0) {
                        timeNum = 60;
                        btRegisteredCode.setTextColor(Color.parseColor("#FFFFFF"));
                        btRegisteredCode.setText("发送验证码");
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
     * 注册：获取验证码
     */
    private void codeHttp(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("type", "1");
        String params = EncryptionUtil.getParameter(RegisteredActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/getVerifyCode.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(RegisteredActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        VerifyCodeResult verifyCodeResult = (VerifyCodeResult) GsonUtil.json2Object(response, VerifyCodeResult.class);
                        if (verifyCodeResult != null
                                && verifyCodeResult.getRet().equals("1")) {
                            getCode();
                        } else {
                            Toasty.warning(RegisteredActivity.this, verifyCodeResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 注册
     */
    private void registeredHttp(String phone, String pwd, String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", pwd);
        map.put("vcode", code);
        String params = EncryptionUtil.getParameter(RegisteredActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/register.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        RegisteredActivity.this, "注册中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(RegisteredActivity.this, "注册失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        RegisteredResult registeredResult = (RegisteredResult) GsonUtil.json2Object(response, RegisteredResult.class);
                        if (registeredResult != null
                                && registeredResult.getRet().equals("1")) {
                            RegisteredActivity.this.finish();
                        } else {
                            Toasty.warning(RegisteredActivity.this, registeredResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 我同意
     */
    @OnClick(R.id.llIAgreeProtocol)
    public void iAgreeProtocol() {
        Intent intent = new Intent(RegisteredActivity.this, ProtocolActivity.class);
        startActivity(intent);
    }

    /**
     * 标题栏返回
     */
    @OnClick(R.id.ibTitleBack)
    public void titleBack() {
        this.finish();
    }
}
