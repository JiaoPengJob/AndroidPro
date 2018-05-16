package com.tch.youmuwa.ui.activity.mine;

import android.content.Intent;
import android.graphics.Color;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.bean.parameters.RePasswordParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GetCodeResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.login.LoginActivity;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置密码
 */
public class SettingPwdActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvGetSPwdVerificationCode)
    TextView tvGetSPwdVerificationCode;
    @BindView(R.id.ivPwdEye)
    ImageView ivPwdEye;
    @BindView(R.id.ivRePwdEye)
    ImageView ivRePwdEye;
    @BindView(R.id.etSettingPwd)
    EditText etSettingPwd;
    @BindView(R.id.etSettingRePwd)
    EditText etSettingRePwd;
    @BindView(R.id.etSettingPwdCode)
    EditText etSettingPwdCode;
    @BindView(R.id.btDeterminePwd)
    Button btDeterminePwd;
    @BindView(R.id.llParentPwd)
    RelativeLayout llParentPwd;
    /**
     * 设置的参数
     */
    private Timer timer;
    private TimerTask timerTask;
    private int index = 60;//计时器
    private String activity;//页面标识
    private boolean isPwdClose = true;//密码是否可见
    private boolean isRePwdClose = true;//确认密码是否可见
    private PresenterImpl<Object> presenter;//接口
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库
    private int type = 3;//获取验证码标识
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("设置密码");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            if (activity.equals("WorkerMineDataActivity")) {
                tvGetSPwdVerificationCode.setTextColor(Color.parseColor("#FBC83F"));
                btDeterminePwd.setBackgroundResource(R.drawable.worker_button_sel);
            } else if (activity.equals("WithdrawActivity")) {
                tvGetSPwdVerificationCode.setTextColor(Color.parseColor("#FBC83F"));
                btDeterminePwd.setBackgroundResource(R.drawable.worker_button_sel);
                type = 4;
            }
        }
    }

    @OnClick(R.id.llParentPwd)
    public void hideInput() {
        HelperUtil.hideInput(SettingPwdActivity.this, llParentPwd);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvGetSPwdVerificationCode)
    public void getSPwdVerificationCode() {
        if (tvGetSPwdVerificationCode.getText().toString().equals("获取验证码")) {
            getCode();
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        CacheDataManager.clearAllCache(SettingPwdActivity.this);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        UserInfo userInfo = (UserInfo) userInfoDao.loadAll().get(0);
        if (userInfo != null) {
            GetCodeParam getCodeParam = new GetCodeParam(userInfo.getPhone(), type);
            presenter = new PresenterImpl<Object>(this);
            presenter.onCreate();
            presenter.attachView(getCodeView);
            presenter.getsms(getCodeParam);
        }
    }

    private ClientBaseView<Object> getCodeView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(SettingPwdActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                etSettingPwdCode.setFocusable(true);
                etSettingPwdCode.setFocusableInTouchMode(true);
                etSettingPwdCode.requestFocus();
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvGetSPwdVerificationCode.setText(index + "秒");
                if (index < 0) {
                    index = 60;
                    tvGetSPwdVerificationCode.setText("获取验证码");
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
     * 确定
     */
    @OnClick(R.id.btDeterminePwd)
    public void determinePwd() {
        clientRePassword();
    }

    /**
     * 重置密码
     */
    private void clientRePassword() {
        mSVProgressHUD.showWithStatus("加载中...");
        RePasswordParam rePasswordParam = new RePasswordParam(
                Integer.parseInt(etSettingPwdCode.getText().toString()),
                etSettingPwd.getText().toString(),
                etSettingRePwd.getText().toString()
        );
        presenter = new PresenterImpl<Object>(SettingPwdActivity.this);
        presenter.onCreate();
        presenter.repassword(rePasswordParam);
        presenter.attachView(rePwdView);
    }

    private ClientBaseView<Object> rePwdView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(SettingPwdActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                Intent intent = new Intent(SettingPwdActivity.this, LoginActivity.class);
                startActivity(intent);
                SettingPwdActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "rePwdView==" + result);
        }
    };

    /**
     * 密码
     */
    @OnClick(R.id.ivPwdEye)
    public void pwdEye() {
        if (isPwdClose) {
            isPwdClose = false;
            ivPwdEye.setImageResource(R.mipmap.pwd_open);
            etSettingPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isPwdClose = true;
            ivPwdEye.setImageResource(R.mipmap.pwd_close);
            etSettingPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 确认密码
     */
    @OnClick(R.id.ivRePwdEye)
    public void rePwdEye() {
        if (isRePwdClose) {
            isRePwdClose = false;
            ivRePwdEye.setImageResource(R.mipmap.pwd_open);
            etSettingRePwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isRePwdClose = true;
            ivRePwdEye.setImageResource(R.mipmap.pwd_close);
            etSettingRePwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatSettingPwd() {
        SettingPwdActivity.this.finish();
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
                SettingPwdActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
