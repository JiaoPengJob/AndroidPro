package com.tch.youmuwa.ui.activity.employer;

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
import com.tch.youmuwa.bean.parameters.OrderDismissParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.fragment.OrderFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 辞退原因
 */
public class DismissReasonActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvItem1)
    TextView tvItem1;
    @BindView(R.id.tvItem2)
    TextView tvItem2;
    @BindView(R.id.tvItem3)
    TextView tvItem3;
    @BindView(R.id.tvItem4)
    TextView tvItem4;
    @BindView(R.id.tvItem5)
    TextView tvItem5;
    @BindView(R.id.tvInputNumbers)
    TextView tvInputNumbers;
    @BindView(R.id.etReasonInput)
    EditText etReasonInput;

    private String orderNum;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss_reason);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("辞退原因");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getStringExtra("orderNum") != null) {
            orderNum = getIntent().getStringExtra("orderNum");
        }

        etReasonInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etReasonInput.length() <= 500) {
                    handler.sendEmptyMessage(0);
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
                tvInputNumbers.setText(String.valueOf(etReasonInput.length()));
            }
        }
    };

    /**
     * 提交按钮
     */
    @OnClick(R.id.btDisReasonSubmit)
    public void disReasonSubmit() {
        mSVProgressHUD.showWithStatus("加载中...");
        orderDismiss();
    }

    /**
     * 辞退
     */
    private void orderDismiss() {
        OrderDismissParam orderDismissParam = new OrderDismissParam(
                orderNum,
                etReasonInput.getText().toString()
        );
        presenter = new PresenterImpl<Object>(DismissReasonActivity.this);
        presenter.onCreate();
        presenter.orderdismiss(orderDismissParam);
        presenter.attachView(orderDismissView);
    }

    private ClientBaseView<Object> orderDismissView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() == 1) {
//                Intent intent = new Intent(DismissReasonActivity.this, EmployerActivity.class);
//                intent.putExtra("aid", 1);
//                startActivity(intent);
                DismissReasonActivity.this.finish();
            } else {
                Toast.makeText(DismissReasonActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "orderDismissView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatDismissReason() {
        DismissReasonActivity.this.finish();
    }

    @OnClick(R.id.tvItem1)
    public void itemSelect1() {
        String str = etReasonInput.getText().toString();
        if (TextUtils.isEmpty(str)) {
            etReasonInput.setText(str + tvItem1.getText().toString() + ",");
        } else {
            if ((str.substring(str.length() - 1)).equals(",")) {
                etReasonInput.setText(str + tvItem1.getText().toString() + ",");
            } else {
                etReasonInput.setText(str + "," + tvItem1.getText().toString() + ",");
            }
        }
        if (etReasonInput.length() <= 500) {
            handler.sendEmptyMessage(0);
        }
    }

    @OnClick(R.id.tvItem2)
    public void itemSelect2() {
        String str = etReasonInput.getText().toString();
        if (TextUtils.isEmpty(str)) {
            etReasonInput.setText(str + tvItem2.getText().toString() + ",");
        } else {
            if ((str.substring(str.length() - 1)).equals(",")) {
                etReasonInput.setText(str + tvItem2.getText().toString() + ",");
            } else {
                etReasonInput.setText(str + "," + tvItem2.getText().toString() + ",");
            }
        }
        if (etReasonInput.length() <= 500) {
            handler.sendEmptyMessage(0);
        }
    }

    @OnClick(R.id.tvItem3)
    public void itemSelect3() {
        String str = etReasonInput.getText().toString();
        if (TextUtils.isEmpty(str)) {
            etReasonInput.setText(str + tvItem3.getText().toString() + ",");
        } else {
            if ((str.substring(str.length() - 1)).equals(",")) {
                etReasonInput.setText(str + tvItem3.getText().toString() + ",");
            } else {
                etReasonInput.setText(str + "," + tvItem3.getText().toString() + ",");
            }
        }
        if (etReasonInput.length() <= 500) {
            handler.sendEmptyMessage(0);
        }
    }

    @OnClick(R.id.tvItem4)
    public void itemSelect4() {
        String str = etReasonInput.getText().toString();
        if (TextUtils.isEmpty(str)) {
            etReasonInput.setText(str + tvItem4.getText().toString() + ",");
        } else {
            if ((str.substring(str.length() - 1)).equals(",")) {
                etReasonInput.setText(str + tvItem4.getText().toString() + ",");
            } else {
                etReasonInput.setText(str + "," + tvItem4.getText().toString() + ",");
            }
        }
        if (etReasonInput.length() <= 500) {
            handler.sendEmptyMessage(0);
        }
    }

    @OnClick(R.id.tvItem5)
    public void itemSelect5() {
        String str = etReasonInput.getText().toString();
        if (TextUtils.isEmpty(str)) {
            etReasonInput.setText(str + tvItem5.getText().toString() + ",");
        } else {
            if ((str.substring(str.length() - 1)).equals(",")) {
                etReasonInput.setText(str + tvItem5.getText().toString() + ",");
            } else {
                etReasonInput.setText(str + "," + tvItem5.getText().toString() + ",");
            }
        }
        if (etReasonInput.length() <= 500) {
            handler.sendEmptyMessage(0);
        }
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
                DismissReasonActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
