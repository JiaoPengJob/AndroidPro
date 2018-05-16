package com.tch.kuwanx.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.ForgetPwdResult;
import com.tch.kuwanx.result.VerifyCodeResult;
import com.tch.kuwanx.ui.BaseActivity;
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
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etForgetPwdPhone)
    EditText etForgetPwdPhone;
    @BindView(R.id.etForgetPwdCode)
    EditText etForgetPwdCode;
    @BindView(R.id.btForgetPwdCode)
    Button btForgetPwdCode;
    @BindView(R.id.etForgetPwdPwd)
    EditText etForgetPwdPwd;
    @BindView(R.id.etForgetPwdConfirmPwd)
    EditText etForgetPwdConfirmPwd;
    @BindView(R.id.tvForgetPwdWarnContent)
    TextView tvForgetPwdWarnContent;
    @BindView(R.id.llForgetPwdWarn)
    LinearLayout llForgetPwdWarn;
    @BindView(R.id.llForgetPwd)
    LinearLayout llForgetPwd;

    private Timer timer;
    private TimerTask timerTask;
    private int timeNum = 60;

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
        tvTitleContent.setText("忘记密码");
    }

    /**
     * 提交
     */
    @OnClick(R.id.btForgetPwdSubmit)
    public void forgetPwdSubmit() {
        if (!TextUtils.isEmpty(etForgetPwdPhone.getText().toString())
                && !TextUtils.isEmpty(etForgetPwdCode.getText().toString())
                && !TextUtils.isEmpty(etForgetPwdPwd.getText().toString())
                && !TextUtils.isEmpty(etForgetPwdConfirmPwd.getText().toString())) {
            llForgetPwdWarn.setVisibility(View.GONE);
            if (Utils.isPhone(etForgetPwdPhone.getText().toString())) {
                llForgetPwdWarn.setVisibility(View.GONE);
                if (etForgetPwdPwd.getText().toString().equals(etForgetPwdConfirmPwd.getText().toString())) {
                    llForgetPwdWarn.setVisibility(View.GONE);
                    //访问忘记密码接口
                    forgetPwdHttp(etForgetPwdPhone.getText().toString(),
                            etForgetPwdPwd.getText().toString(),
                            etForgetPwdCode.getText().toString());
                } else {
                    llForgetPwdWarn.setVisibility(View.VISIBLE);
                    tvForgetPwdWarnContent.setText("两次密码不一致，请重新输入！！");
                }
            } else {
                llForgetPwdWarn.setVisibility(View.VISIBLE);
                tvForgetPwdWarnContent.setText("手机号格式不正确！");
            }
            llForgetPwdWarn.setVisibility(View.GONE);
        } else {
            llForgetPwdWarn.setVisibility(View.VISIBLE);
            tvForgetPwdWarnContent.setText("填写信息不完整！");
        }
    }

    private CustomPopWindow successPop;

    /**
     * 修改成功
     */
    private void updatePwdSuccess() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_success, null);
        successPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(Utils.getScreenWidth(this) - 120, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.5f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        ForgetPwdActivity.this.finish();
                    }
                })
                .create()
                .showAtLocation(llForgetPwd, Gravity.CENTER, 0, 0);
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.btForgetPwdCode)
    public void forgetPwdCode() {
        if (!TextUtils.isEmpty(etForgetPwdPhone.getText().toString())) {
            if (Utils.isPhone(etForgetPwdPhone.getText().toString())) {
                if (btForgetPwdCode.getText().toString().equals("发送验证码")) {
                    //访问接口
                    codeHttp(etForgetPwdPhone.getText().toString());
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
        etForgetPwdCode.setFocusable(true);
        etForgetPwdCode.setFocusableInTouchMode(true);
        etForgetPwdCode.requestFocus();
        btForgetPwdCode.setTextColor(Color.parseColor("#333333"));
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
                    btForgetPwdCode.setText(timeNum + "秒");
                    if (timeNum < 0) {
                        timeNum = 60;
                        btForgetPwdCode.setTextColor(Color.parseColor("#FFFFFF"));
                        btForgetPwdCode.setText("发送验证码");
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
     * 获取验证码
     */
    private void codeHttp(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("type", "2");
        String params = EncryptionUtil.getParameter(ForgetPwdActivity.this, map);
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
                        Toasty.warning(ForgetPwdActivity.this, "发送失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        VerifyCodeResult verifyCodeResult = (VerifyCodeResult) GsonUtil.json2Object(response, VerifyCodeResult.class);
                        if (verifyCodeResult != null
                                && verifyCodeResult.getRet().equals("1")) {
                            getCode();
                        } else {
                            Toasty.warning(ForgetPwdActivity.this, verifyCodeResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 忘记密码
     */
    private void forgetPwdHttp(String phone, String pwd, String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", pwd);
        map.put("vcode", code);
        String params = EncryptionUtil.getParameter(ForgetPwdActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/forgetPassword.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ForgetPwdActivity.this, "修改中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ForgetPwdActivity.this, "修改密码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        ForgetPwdResult forgetPwdResult = (ForgetPwdResult) GsonUtil.json2Object(response, ForgetPwdResult.class);
                        if (forgetPwdResult != null
                                && forgetPwdResult.getRet().equals("1")) {
                            updatePwdSuccess();
                        } else {
                            Toasty.warning(ForgetPwdActivity.this, forgetPwdResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 标题栏返回
     */
    @OnClick(R.id.ibTitleBack)
    public void titleBack() {
        ForgetPwdActivity.this.finish();
    }

}
