package com.tch.youmuwa.ui.activity.worker;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.tch.youmuwa.bean.parameters.RefuseOrderParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.HelperUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工人/拒单原因
 */
public class RejectSingleReasonActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etRejectSingleReasonInput)
    EditText etRejectSingleReasonInput;
    @BindView(R.id.tvRejectSingleReasonNumber)
    TextView tvRejectSingleReasonNumber;

    private Dialog dialog;
    private int id;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_single_reason);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("拒单原因");
        id = getIntent().getIntExtra("id", 0);
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(RejectSingleReasonActivity.this);
        etRejectSingleReasonInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etRejectSingleReasonInput.length() <= 500) {
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
                tvRejectSingleReasonNumber.setText(String.valueOf(etRejectSingleReasonInput.length()));
            }
        }
    };

    /**
     * 提交
     */
    @OnClick(R.id.btRejectSingleReasonSubmit)
    public void rejectSingleReasonSubmit() {
        mSVProgressHUD.showWithStatus("加载中...");
        RefuseOrderParam refuseOrderParam = new RefuseOrderParam(
                id,
                etRejectSingleReasonInput.getText().toString()
        );
        presenter = new PresenterImpl<Object>(RejectSingleReasonActivity.this);
        presenter.onCreate();
        presenter.refuseorder(refuseOrderParam);
        presenter.attachView(refuseOrderView);
    }

    private ClientBaseView<Object> refuseOrderView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(RejectSingleReasonActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                rejectSingleSuccess();
                RejectSingleReasonActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "refuseOrderView--" + result);
        }
    };

    /**
     * 显示拒单成功
     */
    private void rejectSingleSuccess() {
        dialog = new Dialog(RejectSingleReasonActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_reject_single, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(RejectSingleReasonActivity.this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatFeedBack() {
        RejectSingleReasonActivity.this.finish();
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
                RejectSingleReasonActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
