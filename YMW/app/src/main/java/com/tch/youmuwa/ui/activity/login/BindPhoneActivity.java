package com.tch.youmuwa.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.BindMobileParam;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.ui.activity.worker.BindTheBankCardActivity;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定手机号
 */
public class BindPhoneActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvGetPhoneCode)
    TextView tvGetPhoneCode;
    @BindView(R.id.etBindPhone)
    EditText etBindPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.llParentBindPhone)
    LinearLayout llParentBindPhone;
    /**
     * 设置的参数
     */
    private Timer timer;
    private TimerTask timerTask;
    private int index = 60;
    private PresenterImpl<Object> presenter;//接口
    private SVProgressHUD mSVProgressHUD;//加载显示
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("手机号绑定");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
    }

    @OnClick(R.id.llParentBindPhone)
    public void hideInput() {
        HelperUtil.hideInput(BindPhoneActivity.this, llParentBindPhone);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvGetPhoneCode)
    public void getPhoneVerificationCode() {
        if (tvGetPhoneCode.getText().toString().equals("获取验证码")) {
            getCode();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvGetPhoneCode.setText(index + "秒");
                if (index < 0) {
                    index = 60;
                    tvGetPhoneCode.setText("获取验证码");
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
     * 完成
     */
    @OnClick(R.id.btBindPhoneDone)
    public void bindPhoneDone() {
        bindPhone();
    }

    /**
     * 绑定手机号
     */
    private void bindPhone() {
        mSVProgressHUD.showWithStatus("绑定中...");
        BindMobileParam bm = new BindMobileParam(
                etBindPhone.getText().toString(),
                etCode.getText().toString()
        );
        presenter = new PresenterImpl<Object>(BindPhoneActivity.this);
        presenter.onCreate();
        presenter.bindmobile(bm);
        presenter.attachView(bmView);
    }

    private ClientBaseView<Object> bmView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        SharedPrefsUtil.putValue(BindPhoneActivity.this, "isEmployer", true);
                        SharedPrefsUtil.putValue(BindPhoneActivity.this, "ifPwdLogin", false);
                        daoSession = ((MyApplication) getApplication()).getDaoSession();
                        userInfoDao = daoSession.getUserInfoDao();
                        UserInfo userinfo = new UserInfo();
                        userinfo.setPhone(etBindPhone.getText().toString());
                        userInfoDao.insert(userinfo);
                        Intent intent = new Intent(BindPhoneActivity.this, EmployerActivity.class);
                        startActivity(intent);
                    }
                }, 3000);
            } else {
                Toast.makeText(BindPhoneActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "bmView--" + result);
        }
    };

    /**
     * 获取验证码
     */
    private void getCode() {
        CacheDataManager.clearAllCache(BindPhoneActivity.this);
        GetCodeParam getCodeParam = new GetCodeParam(etBindPhone.getText().toString(), 6);
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.attachView(getCodeView);
        presenter.getsms(getCodeParam);
    }

    private ClientBaseView<Object> getCodeView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(BindPhoneActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                etCode.setFocusable(true);
                etCode.setFocusableInTouchMode(true);
                etCode.requestFocus();
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
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void bindPhoneBack() {
        this.finish();
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
                BindPhoneActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }

}
