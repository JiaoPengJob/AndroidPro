package com.tch.youmuwa.ui.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.FeedBackParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etFeedBackInput)
    EditText etFeedBackInput;
    @BindView(R.id.tvFeedBackInputNumber)
    TextView tvFeedBackInputNumber;
    @BindView(R.id.btFeedBackSubmit)
    Button btFeedBackSubmit;

    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private String fragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("意见反馈");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);

        if (getIntent().getStringExtra("fragmentType") != null) {
            fragmentType = getIntent().getStringExtra("fragmentType");
            if (fragmentType.equals("employer")) {
                btFeedBackSubmit.setBackgroundResource(R.drawable.employer_button_sel);
            } else {
                btFeedBackSubmit.setBackgroundResource(R.drawable.worker_button_sel);
            }
        }

        etFeedBackInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etFeedBackInput.length() <= 500) {
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
                tvFeedBackInputNumber.setText(String.valueOf(etFeedBackInput.length()));
            }
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatFeedBack() {
        FeedbackActivity.this.finish();
    }

    /**
     * 提交
     */
    @OnClick(R.id.btFeedBackSubmit)
    public void feedBackSubmit() {
        feedback();
    }

    /**
     * 意见反馈
     */
    private void feedback() {
        mSVProgressHUD.showWithStatus("加载中...");
        FeedBackParam param = new FeedBackParam(etFeedBackInput.getText().toString());
        presenter = new PresenterImpl<Object>(FeedbackActivity.this);
        presenter.onCreate();
        presenter.feedback(param);
        presenter.attachView(feedBackView);
    }

    private ClientBaseView<Object> feedBackView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                FeedbackActivity.this.finish();
            } else {
                Toast.makeText(FeedbackActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "feedBackView--" + result);
        }
    };
}
