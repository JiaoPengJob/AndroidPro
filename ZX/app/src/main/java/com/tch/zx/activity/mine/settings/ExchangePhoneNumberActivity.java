package com.tch.zx.activity.mine.settings;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.login_register.RegisterActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
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

/**
 * 更换手机号第二步
 */
public class ExchangePhoneNumberActivity extends BaseActivity {
    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 获取验证码
     */
    @BindView(R.id.tv_getcode_exphone)
    TextView tv_getcode_exphone;

    @BindView(R.id.etExPhone)
    EditText etExPhone;
    @BindView(R.id.etExPhoneCode)
    EditText etExPhoneCode;

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
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_exchange_phone_number);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("更换手机号");
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
    }

    /**
     * 确定
     */
    @OnClick(R.id.bt_exchange_phone_done)
    public void donePhoneNum() {
        if (TextUtils.isEmpty(etExPhone.getText().toString())) {
            Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
        } else {
            if (!HelperUtil.isMobileNO(etExPhone.getText().toString())) {
                Toast.makeText(this, "手机号码格式不正确!", Toast.LENGTH_SHORT).show();
            } else {
                if (etExPhoneCode.getText().toString().equals(smsResult.getCode())) {
                    updatePhone();
                } else {
                    Toast.makeText(this, "验证码错误!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updatePhone() {
        presenter = new BasePresenter<Object>(this);
        presenter.onCreate();
        presenter.attachView(updatePhoneView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("userPhone", etExPhone.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.updatePhone(data);
    }

    private BaseView<Object> updatePhoneView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                ExchangePhoneNumberActivity.this.finish();
            } else {
                Toast.makeText(ExchangePhoneNumberActivity.this, "验证码错误!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "updatePhoneView接口错误" + result);
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnExPhoneNext() {
        this.finish();
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_getcode_exphone)
    public void getCodeExPhone() {
        if (!TextUtils.isEmpty(etExPhone.getText().toString())) {
            if (HelperUtil.isMobileNO(etExPhone.getText().toString())) {
                getSms();
            } else {
                Toast.makeText(this, "手机号码格式不正确!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取验证码
     */
    private void getSms() {
        presenter = new BasePresenter<Object>(ExchangePhoneNumberActivity.this);
        presenter.onCreate();
        presenter.attachView(smsView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", etExPhone.getText().toString());

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
     * handler线程
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //计时器运行
                case ConstantData.HANDLER_MESSAGE_CODE_REGISTER:
                    tv_getcode_exphone.setText(minute + "秒");
                    tv_getcode_exphone.setEnabled(false);
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
        tv_getcode_exphone.setText("获取验证码");
        tv_getcode_exphone.setEnabled(true);
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
