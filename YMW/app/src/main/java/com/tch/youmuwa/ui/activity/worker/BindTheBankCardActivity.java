package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.Gson;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.BindBankCardParam;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GetCodeResult;
import com.tch.youmuwa.bean.result.IsBindBankResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.login.BindPhoneActivity;
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
 * 绑定银行卡
 */
public class BindTheBankCardActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvGetBankCardCode)
    TextView tvGetBankCardCode;
    @BindView(R.id.tvBankCardAreaShow)
    TextView tvBankCardAreaShow;
    @BindView(R.id.etBankPhone)
    EditText etBankPhone;
    @BindView(R.id.etBankCardCode)
    EditText etBankCardCode;
    @BindView(R.id.llParentBank)
    LinearLayout llParentBank;
    @BindView(R.id.etBankCard)
    EditText etBankCard;

    private Timer timer;
    private TimerTask timerTask;
    private int index = 60;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_the_bank_card);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("绑定银行卡");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
    }

    @OnClick(R.id.llParentBank)
    public void hideInput() {
        HelperUtil.hideInput(BindTheBankCardActivity.this, llParentBank);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvGetBankCardCode)
    public void getBankCardCode() {
        if (tvGetBankCardCode.getText().toString().equals("获取验证码")) {
            if (!TextUtils.isEmpty(etBankPhone.getText().toString())) {
                getCode();
            }
        }
    }

    /**
     * 获取验证码链接
     */
    private void getCode() {
        CacheDataManager.clearAllCache(BindTheBankCardActivity.this);
        GetCodeParam getCodeParam = new GetCodeParam(etBankPhone.getText().toString(), 5);
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.attachView(getCodeView);
        presenter.getsms(getCodeParam);
    }

    private ClientBaseView<Object> getCodeView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(BindTheBankCardActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                etBankCardCode.setFocusable(true);
                etBankCardCode.setFocusableInTouchMode(true);
                etBankCardCode.requestFocus();
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
            Log.e("TAG", "getCodeView--Error:--" + result);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvGetBankCardCode.setText(index + "秒");
                if (index < 0) {
                    index = 60;
                    tvGetBankCardCode.setText("获取验证码");
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

    /**
     * 立即绑定
     */
    @OnClick(R.id.btBindBankCard)
    public void bindBankCard() {
        if (!TextUtils.isEmpty(etBankCard.getText().toString())
                && !TextUtils.isEmpty(etBankPhone.getText().toString())
                && !TextUtils.isEmpty(etBankCardCode.getText().toString())) {
            mSVProgressHUD.showWithStatus("绑定中...");
            BindBankCardParam bindBankCardParam = new BindBankCardParam(
                    etBankCard.getText().toString(),
                    etBankPhone.getText().toString(),
                    etBankCardCode.getText().toString()
            );
            presenter = new PresenterImpl<Object>(BindTheBankCardActivity.this);
            presenter.onCreate();
            presenter.bandbankcard(bindBankCardParam);
            presenter.attachView(bindBankCardView);
        }
    }

    private ClientBaseView<Object> bindBankCardView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                IsBindBankResult isBindBankResult = (IsBindBankResult) GsonUtil.parseJson(baseBean.getData(), IsBindBankResult.class);
                Intent intent = new Intent(BindTheBankCardActivity.this, BindBankCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("isBindBankResult", isBindBankResult);
                intent.putExtras(bundle);
                startActivity(intent);
                BindTheBankCardActivity.this.finish();
            } else {
                Toast.makeText(BindTheBankCardActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "bindBankCardView--" + result);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatBindTheBankCard() {
        BindTheBankCardActivity.this.finish();
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
                BindTheBankCardActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
