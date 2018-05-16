package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.PointFinishedParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.HelperUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工资结算/点工
 */
public class WageSettlementActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etWageWorkDays)
    EditText etWageWorkDays;
    @BindView(R.id.tvDayWages)
    TextView tvDayWages;
    @BindView(R.id.tvDayWagesCount)
    TextView tvDayWagesCount;

    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private int id;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_settlement);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("结算工资");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(WageSettlementActivity.this);
        id = getIntent().getIntExtra("id", 0);
        price = getIntent().getDoubleExtra("price", 0);
        tvDayWages.setText(String.valueOf(price));
        etWageWorkDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (HelperUtil.isNumeric(charSequence.toString())) {
                    Message message = new Message();
                    message.obj = charSequence.toString();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (msg.obj.toString() != null && !msg.obj.toString().equals("")) {
                    double num = Double.parseDouble(msg.obj.toString());
                    tvDayWagesCount.setText(String.valueOf(price * num));
                }
            }
        }
    };

    /**
     * 提交
     */
    @OnClick(R.id.btWageSettlementSubmit)
    public void btWageSettlementSubmit() {
        if (!TextUtils.isEmpty(etWageWorkDays.getText().toString())) {
            achievePoint();
        }
    }

    /**
     * 点工完工
     */
    private void achievePoint() {
        mSVProgressHUD.showWithStatus("提交中...");
        PointFinishedParam pointFinished = new PointFinishedParam(
                id,
                Integer.parseInt(etWageWorkDays.getText().toString())
        );
        presenter = new PresenterImpl<Object>(WageSettlementActivity.this);
        presenter.onCreate();
        presenter.achieve1(pointFinished);
        presenter.attachView(achievePointView);
    }

    private ClientBaseView<Object> achievePointView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                Intent intent = new Intent(WageSettlementActivity.this, WorkerMainActivity.class);
                intent.putExtra("aid", 1);
                startActivity(intent);
                WageSettlementActivity.this.finish();
            } else {
                Toast.makeText(WageSettlementActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "achievePointView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)

    public void retreatWageSettlement() {
        WageSettlementActivity.this.finish();
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
                WageSettlementActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
