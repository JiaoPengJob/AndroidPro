package com.tch.kuwanx.ui.mine.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.InsertIdeaListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etFeedBack)
    EditText etFeedBack;
    @BindView(R.id.tvEditTextNumber)
    TextView tvEditTextNumber;
    @BindView(R.id.etFeedBackPhone)
    EditText etFeedBackPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("意见反馈");
        etFeedBack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etFeedBack.length() <= 140) {
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
                tvEditTextNumber.setText(String.valueOf(140 - etFeedBack.length()));
            }
        }
    };

    /**
     * 提交
     */
    @OnClick(R.id.btFeedBackSubmit)
    public void btFeedBackSubmit() {
        if (TextUtils.isEmpty(etFeedBackPhone.getText().toString())) {
            Toasty.warning(FeedbackActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT, false).show();
        } else if (TextUtils.isEmpty(etFeedBack.getText().toString())) {
            Toasty.warning(FeedbackActivity.this, "反馈内容不能为空！", Toast.LENGTH_SHORT, false).show();
        } else {
            insertIdeaListHttp();
        }
    }

    /**
     * 意见反馈
     */
    private void insertIdeaListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("fm_appuserid", DaoUtils.getUserId(FeedbackActivity.this));
        map.put("fm_content", etFeedBack.getText().toString());
        map.put("fm_phone", etFeedBackPhone.getText().toString());
        String params = EncryptionUtil.getParameter(FeedbackActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "message/insertIdeaList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_insertIdeaList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        FeedbackActivity.this, "反馈中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(FeedbackActivity.this, "反馈失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        InsertIdeaListResult insertIdeaListResult =
                                (InsertIdeaListResult) GsonUtil.json2Object(response, InsertIdeaListResult.class);
                        if (insertIdeaListResult != null
                                && insertIdeaListResult.getRet().equals("1")) {
                            Toasty.warning(FeedbackActivity.this, "反馈成功！", Toast.LENGTH_SHORT, false).show();
                        } else {
                            Toasty.warning(FeedbackActivity.this, "反馈失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void feedBack() {
        FeedbackActivity.this.finish();
    }
}
