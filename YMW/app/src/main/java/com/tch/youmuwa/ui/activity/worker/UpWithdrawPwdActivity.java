package com.tch.youmuwa.ui.activity.worker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
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
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.EditWithdrawPassParam;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GetCodeResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改提现密码
 */
public class UpWithdrawPwdActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etWpForgetCode)
    EditText etWpForgetCode;
    @BindView(R.id.tvWpGetForgetPwdVerificationCode)
    TextView tvWpGetForgetPwdVerificationCode;
    @BindView(R.id.etWpForgetNewPwd)
    EditText etWpForgetNewPwd;
    @BindView(R.id.ivWpForgetPwdEye)
    ImageView ivWpForgetPwdEye;
    @BindView(R.id.etReWpForgetNewPwd)
    EditText etReWpForgetNewPwd;
    @BindView(R.id.ivReWpForgetPwdEye)
    ImageView ivReWpForgetPwdEye;
    @BindView(R.id.llParentWith)
    LinearLayout llParentWith;
    /**
     * 设置的参数
     */
    private String activity;//页面标识
    private Timer timer;
    private TimerTask timerTask;
    private int index = 60;//计时器
    private boolean isPwdClose = true;
    private boolean isRePwdClose = true;//密码是否可见
    private PresenterImpl<Object> presenter;//接口
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库
    private SVProgressHUD mSVProgressHUD;//加载显示
    private Dialog dialog;//提示框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_withdraw_pwd);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            if (activity.equals("WalletActivity")) {
                title.setText("设置提现密码");
            } else {
                title.setText("修改提现密码");
            }
        }
    }

    @OnClick(R.id.llParentWith)
    public void hideInput() {
        HelperUtil.hideInput(UpWithdrawPwdActivity.this, llParentWith);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvWpGetForgetPwdVerificationCode)
    public void getWpForgetPwdVerificationCode() {
        if (tvWpGetForgetPwdVerificationCode.getText().toString().equals("获取验证码")) {
            getCode();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvWpGetForgetPwdVerificationCode.setText(index + "秒");
                if (index < 0) {
                    index = 60;
                    tvWpGetForgetPwdVerificationCode.setText("获取验证码");
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
     * 获取验证码
     */
    private void getCode() {
        CacheDataManager.clearAllCache(UpWithdrawPwdActivity.this);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        if (userInfoDao.loadAll().size() > 0) {
            UserInfo userInfo = (UserInfo) userInfoDao.loadAll().get(0);
            GetCodeParam getCodeParam = new GetCodeParam(userInfo.getPhone(), 4);
            presenter = new PresenterImpl<Object>(UpWithdrawPwdActivity.this);
            presenter.onCreate();
            presenter.getsms(getCodeParam);
            presenter.attachView(getCodeView);
        }
    }

    private ClientBaseView<Object> getCodeView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(UpWithdrawPwdActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                etWpForgetCode.setFocusable(true);
                etWpForgetCode.setFocusableInTouchMode(true);
                etWpForgetCode.requestFocus();
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

    /**
     * 密码是否可见
     */
    @OnClick(R.id.ivWpForgetPwdEye)
    public void forgetPwdEye() {
        if (isPwdClose) {
            isPwdClose = false;
            ivWpForgetPwdEye.setImageResource(R.mipmap.pwd_open);
            etWpForgetNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isPwdClose = true;
            ivWpForgetPwdEye.setImageResource(R.mipmap.pwd_close);
            etWpForgetNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 确认密码是否可见
     */
    @OnClick(R.id.ivReWpForgetPwdEye)
    public void reForgetPwdEye() {
        if (isRePwdClose) {
            isRePwdClose = false;
            ivReWpForgetPwdEye.setImageResource(R.mipmap.pwd_open);
            etReWpForgetNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isRePwdClose = true;
            ivReWpForgetPwdEye.setImageResource(R.mipmap.pwd_close);
            etReWpForgetNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 设置完成
     */
    @OnClick(R.id.btWpSettingDone)
    public void wpSettingDone() {
        mSVProgressHUD.showWithStatus("加载中...");
        EditWithdrawPassParam editWithdrawPassParam = new EditWithdrawPassParam(
                Integer.parseInt(etWpForgetCode.getText().toString()),
                etWpForgetNewPwd.getText().toString(),
                etReWpForgetNewPwd.getText().toString()
        );
        presenter = new PresenterImpl<Object>(UpWithdrawPwdActivity.this);
        presenter.onCreate();
        presenter.editwithdrawpass(editWithdrawPassParam);
        presenter.attachView(editWithdrawPassView);
    }

    private ClientBaseView<Object> editWithdrawPassView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(UpWithdrawPwdActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                showSuccessDialog();
                if (activity.equals("WalletActivity")) {
                    Intent intent = new Intent(UpWithdrawPwdActivity.this, WalletActivity.class);
                    startActivity(intent);
                } else {
                    UpWithdrawPwdActivity.this.finish();
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "editWithdrawPassView:--" + result);
        }
    };

    /**
     * 显示成功提示框
     */
    private void showSuccessDialog() {
        dialog = new Dialog(this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_withdraw_pwd, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatUpWithdrawPwd() {
        UpWithdrawPwdActivity.this.finish();
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
                UpWithdrawPwdActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
