package com.tch.youmuwa.ui.activity.worker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.WithdrawalParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.WithdrawalResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.mine.SettingPwdActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现
 */
public class WithdrawActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etWithdrawalPrice)
    EditText etWithdrawalPrice;
    @BindView(R.id.tvWithdrawalInfo)
    TextView tvWithdrawalInfo;

    private Dialog dialog;
    private TextView tvWithdrawSettingPwd, tvWithdrawDetermine;
    private Intent intent;
    private double priceCount;
    private double editPrice;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private EditText etWithdrawPwdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("提现");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getStringExtra("priceCount") != null) {
            priceCount = Double.parseDouble(getIntent().getStringExtra("priceCount"));
            tvWithdrawalInfo.setText("当前最高可提现" + getIntent().getStringExtra("priceCount") + "元");
        }
        etWithdrawalPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null && !charSequence.toString().equals("")) {
                    editPrice = Double.parseDouble(charSequence.toString());
                    if (editPrice > priceCount) {
                        tvWithdrawalInfo.setText("输入金额超过当前最高提现金额");
                    }
                } else {
                    tvWithdrawalInfo.setText("当前最高可提现" + getIntent().getStringExtra("priceCount") + "元");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                if (temp.length() == 2) {
                    if (temp.indexOf("0") == 0) {
                        if (!temp.substring(1).equals(".")) {
                            etWithdrawalPrice.setText("0");
                            etWithdrawalPrice.setSelection(etWithdrawalPrice.length());
                        }
                    }
                }
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    editable.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    /**
     * 立即提现
     */
    @OnClick(R.id.btNowWithdraw)
    public void nowWithdraw() {
        if (editPrice > priceCount) {
            Toast.makeText(WithdrawActivity.this, "输入金额超过当前最高提现金额", Toast.LENGTH_SHORT).show();
        } else {
            dialog = new Dialog(this, R.style.dialog);
            //获取自定义布局
            View layout = getLayoutInflater().inflate(R.layout.dialog_withdraw, null);
            tvWithdrawSettingPwd = (TextView) layout.findViewById(R.id.tvWithdrawSettingPwd);
            tvWithdrawDetermine = (TextView) layout.findViewById(R.id.tvWithdrawDetermine);
            etWithdrawPwdInput = (EditText) layout.findViewById(R.id.etWithdrawPwdInput);
            dialog.setContentView(layout);
            dialog.show();
            android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
            p.width = (int) (HelperUtil.getScreenWidth(this) * 0.7);
            p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(p);
            //设置密码
            tvWithdrawSettingPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    intent = new Intent(WithdrawActivity.this, SettingPwdActivity.class);
                    intent.putExtra("activity", "WithdrawActivity");
                    startActivity(intent);
                }
            });
            //确认
            tvWithdrawDetermine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    withdrawal();
                    dialog.cancel();
                }
            });
        }
    }

    /**
     * 提现申请
     */
    private void withdrawal() {
        mSVProgressHUD.showWithStatus("加载中...");
        WithdrawalParam withdrawalParam = new WithdrawalParam(
                etWithdrawalPrice.getText().toString(),
                etWithdrawPwdInput.getText().toString()
        );
        presenter = new PresenterImpl<Object>(WithdrawActivity.this);
        presenter.onCreate();
        presenter.withdrawal(withdrawalParam);
        presenter.attachView(withdrawalView);
    }

    private ClientBaseView<Object> withdrawalView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                WithdrawalResult result = (WithdrawalResult) GsonUtil.parseJson(baseBean.getData(), WithdrawalResult.class);
                if (result.getBankcard() == 0) {
                    Toast.makeText(WithdrawActivity.this, "当前未绑定银行卡，请先绑定银行卡...", Toast.LENGTH_SHORT).show();
                } else {
                    showSuccessDialog();
                }
            }else{
                Toast.makeText(WithdrawActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "withdrawalView==" + result);
        }
    };

    /**
     * 成功提示
     */
    private void showSuccessDialog() {
        dialog = new Dialog(this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_withdraw_success, null);
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
    public void retreatWithdraw() {
        WithdrawActivity.this.finish();
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
                WithdrawActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
