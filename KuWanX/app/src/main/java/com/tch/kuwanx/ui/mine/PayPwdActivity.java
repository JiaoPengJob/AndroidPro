package com.tch.kuwanx.ui.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.UpdateUserPassword;
import com.tch.kuwanx.result.VerifyCodeResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
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
 * 设置支付密码
 */
public class PayPwdActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etPayPwdPhone)
    EditText etPayPwdPhone;
    @BindView(R.id.etPayPwdCode)
    EditText etPayPwdCode;
    @BindView(R.id.btPayPwdCode)
    Button btPayPwdCode;
    @BindView(R.id.etPayPwd)
    EditText etPayPwd;
    @BindView(R.id.etPayPwdConfirm)
    EditText etPayPwdConfirm;
    @BindView(R.id.llPayPwdWarnInfo)
    LinearLayout llPayPwdWarnInfo;
    @BindView(R.id.tvPayPwdWarnInfo)
    TextView tvPayPwdWarnInfo;

    private int codeIndex = 60;
    private Timer timer;
    private TimerTask task;
    private String payPwdType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pwd);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() throws NullPointerException {
        if (getIntent().getStringExtra("payPwdType") != null) {
            payPwdType = getIntent().getStringExtra("payPwdType");
            switch (payPwdType) {
                case "已设置":
                    tvTitleContent.setText("修改支付密码");
                    break;
                case "未设置":
                    tvTitleContent.setText("设置支付密码");
                    break;
            }
        }
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.btPayPwdCode)
    public void payPwdCode() {
        if (!TextUtils.isEmpty(etPayPwdPhone.getText().toString())
                && Utils.isPhone(etPayPwdPhone.getText().toString())) {
            if (btPayPwdCode.getText().toString().equals("发送验证码")) {
                //访问接口,如果成功，调用下面的读秒方法
                codeHttp(etPayPwdPhone.getText().toString());
            }
        } else {
            Toasty.warning(PayPwdActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 读秒方法
     */
    private void getCodeChange() {
        btPayPwdCode.setClickable(false);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                codeIndex--;
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    btPayPwdCode.setText(codeIndex + "秒");
                    if (codeIndex < 0) {
                        codeIndex = 60;
                        btPayPwdCode.setText("发送验证码");
                        btPayPwdCode.setClickable(true);
                        timer.cancel();
                        task.cancel();
                        handler.removeMessages(0);
                    }
                    break;
            }
        }
    };

    /**
     * 提交
     */
    @OnClick(R.id.btSubmitPayPwd)
    public void submitPayPwd() {
        if (TextUtils.isEmpty(etPayPwdPhone.getText().toString())) {
            llPayPwdWarnInfo.setVisibility(View.VISIBLE);
            tvPayPwdWarnInfo.setText("手机号不能为空！");
        } else if (!Utils.isPhone(etPayPwdPhone.getText().toString())) {
            llPayPwdWarnInfo.setVisibility(View.VISIBLE);
            tvPayPwdWarnInfo.setText("请输入正确的手机号！");
        } else if (TextUtils.isEmpty(etPayPwdCode.getText().toString())) {
            llPayPwdWarnInfo.setVisibility(View.VISIBLE);
            tvPayPwdWarnInfo.setText("验证码不能为空！");
        } else if (TextUtils.isEmpty(etPayPwd.getText().toString())) {
            llPayPwdWarnInfo.setVisibility(View.VISIBLE);
            tvPayPwdWarnInfo.setText("支付密码不能为空！");
        } else if (TextUtils.isEmpty(etPayPwdConfirm.getText().toString())) {
            llPayPwdWarnInfo.setVisibility(View.VISIBLE);
            tvPayPwdWarnInfo.setText("支付密码不能为空！");
        } else if (!etPayPwd.getText().toString().equals(etPayPwdConfirm.getText().toString())) {
            llPayPwdWarnInfo.setVisibility(View.VISIBLE);
            tvPayPwdWarnInfo.setText("两次密码输入不一致！");
        } else {
            //全部通过，成功，调用接口
            llPayPwdWarnInfo.setVisibility(View.INVISIBLE);
            updateUserPasswordHttp();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void payPwdBack() {
        PayPwdActivity.this.finish();
    }

    /**
     * 注册：获取验证码
     */
    private void codeHttp(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("type", "4");
        map.put("userId", DaoUtils.getUserId(PayPwdActivity.this));
        String params = EncryptionUtil.getParameter(PayPwdActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/getVerifyCode.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName() + "_payPwd")
                .cacheTime(2)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(PayPwdActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        VerifyCodeResult verifyCodeResult = (VerifyCodeResult) GsonUtil.json2Object(response, VerifyCodeResult.class);
                        if (verifyCodeResult != null
                                && verifyCodeResult.getRet().equals("1")) {
                            getCodeChange();
                        } else {
                            Toasty.warning(PayPwdActivity.this, verifyCodeResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 设置支付密码
     */
    private void updateUserPasswordHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(PayPwdActivity.this));
        map.put("phone", etPayPwdPhone.getText().toString());
        map.put("vcode", etPayPwdCode.getText().toString());
        map.put("password", etPayPwd.getText().toString());
        String params = EncryptionUtil.getParameter(PayPwdActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/updateUserPassword.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_updateUserPassword")
                .cacheTime(2)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(PayPwdActivity.this, "设置支付密码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        UpdateUserPassword updateUserPassword =
                                (UpdateUserPassword) GsonUtil.json2Object(response, UpdateUserPassword.class);
                        if (updateUserPassword != null
                                && updateUserPassword.getRet().equals("1")) {
                            PayPwdActivity.this.finish();
                        } else {
                            Toasty.warning(PayPwdActivity.this, "设置支付密码失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
