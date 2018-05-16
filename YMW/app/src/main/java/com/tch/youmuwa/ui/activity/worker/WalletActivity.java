package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.JpushParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.IsBindBankResult;
import com.tch.youmuwa.bean.result.IsSetWalletPwdResult;
import com.tch.youmuwa.bean.result.WalletResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.GsonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvWorkerWalletPrice)
    TextView tvWorkerWalletPrice;

    private Intent intent;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private JpushParam jp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("钱包");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getSerializableExtra("jp") != null) {
            jp = (JpushParam) getIntent().getSerializableExtra("jp");
        }
        getWalletPrice();
    }

    /**
     * 修改提现密码
     */
    @OnClick(R.id.rlUpdateWithdrawPwd)
    public void updateWithdrawPwd() {
        isUpdateWithdrawPwd();
    }

    /**
     * 验证提现密码是否设置
     */
    private void isUpdateWithdrawPwd() {
        presenter = new PresenterImpl<Object>(WalletActivity.this);
        presenter.onCreate();
        presenter.issetwithdrawpass();
        presenter.attachView(isUpdateWithdrawPwdView);
    }

    private ClientBaseView<Object> isUpdateWithdrawPwdView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(WalletActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                IsSetWalletPwdResult isSetWalletPwdResult = (IsSetWalletPwdResult) GsonUtil.parseJson(baseBean.getData(), IsSetWalletPwdResult.class);
                if (isSetWalletPwdResult.getIsset() == 0) {
                    intent = new Intent(WalletActivity.this, UpdateWithdrawPwdActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WalletActivity.this, UpWithdrawPwdActivity.class);
                    intent.putExtra("activity", "WalletActivity");
                    startActivity(intent);
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "isUpdateWithdrawPwdView==" + result);
        }
    };

    /**
     * 提现
     */
    @OnClick(R.id.rlWithdraw)
    public void withdraw() {
        intent = new Intent(WalletActivity.this, WithdrawActivity.class);
        intent.putExtra("priceCount", tvWorkerWalletPrice.getText().toString());
        startActivity(intent);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatWallet() {
        if (jp != null) {
            intent = new Intent(WalletActivity.this, WorkerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            WalletActivity.this.finish();
        }
    }

    /**
     * 获取钱包金额
     */
    private void getWalletPrice() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(WalletActivity.this);
        presenter.onCreate();
        presenter.workerwallet();
        presenter.attachView(walletView);
    }

    private ClientBaseView<Object> walletView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(WalletActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                WalletResult walletResult = (WalletResult) GsonUtil.parseJson(baseBean.getData(), WalletResult.class);
                tvWorkerWalletPrice.setText(walletResult.getRemain_money());
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "walletView==" + result);
        }
    };

    /**
     * 绑定银行卡
     */
    @OnClick(R.id.rlBindBankCard)
    public void bindBankCard() {
        isBandBank();
    }

    /**
     * 验证是否绑定银行卡
     */
    private void isBandBank() {
        presenter = new PresenterImpl<Object>(WalletActivity.this);
        presenter.onCreate();
        presenter.isbandbank();
        presenter.attachView(isBindBankView);
    }

    private ClientBaseView<Object> isBindBankView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                IsBindBankResult isBindBankResult = (IsBindBankResult) GsonUtil.parseJson(baseBean.getData(), IsBindBankResult.class);
                intent = new Intent(WalletActivity.this, BindBankCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("isBindBankResult", isBindBankResult);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(WalletActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "isBindBankView==" + result);
        }
    };

    /**
     * 收益明细
     */
    @OnClick(R.id.rlIncomeDetails)
    public void incomeDetails() {
        intent = new Intent(WalletActivity.this, WorkerTDActivity.class);
        startActivity(intent);
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
                if (jp != null) {
                    intent = new Intent(WalletActivity.this, WorkerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    WalletActivity.this.finish();
                }
                bl = true;
            }
        }
        return bl;
    }
}
