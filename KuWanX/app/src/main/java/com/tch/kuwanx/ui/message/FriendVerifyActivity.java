package com.tch.kuwanx.ui.message;

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
import com.tch.kuwanx.result.AddFriendResult;
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
 * 好友验证信息
 */
public class FriendVerifyActivity extends BaseActivity {

    @BindView(R.id.etFriendVerify)
    EditText etFriendVerify;
    @BindView(R.id.tvFriendVerifyNumber)
    TextView tvFriendVerifyNumber;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_verify);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        }

        etFriendVerify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etFriendVerify.length() <= 20) {
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
                tvFriendVerifyNumber.setText(String.valueOf(20 - etFriendVerify.length()));
            }
        }
    };

    /**
     * 提交
     */
    @OnClick(R.id.btFriendVerifyTitleFeatures)
    void btFriendVerifyTitleFeatures() {
        if (!TextUtils.isEmpty(etFriendVerify.getText().toString())) {
            addFriendHttp();
        } else {
            Toasty.warning(FriendVerifyActivity.this, "请输入验证信息！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 好友申请
     */
    private void addFriendHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(FriendVerifyActivity.this));
        map.put("apply_app_user_id", id);
        map.put("verification_message", etFriendVerify.getText().toString());
        String params = EncryptionUtil.getParameter(FriendVerifyActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/addFriend.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        FriendVerifyActivity.this, "申请添加中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(FriendVerifyActivity.this, "申请添加失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddFriendResult addFriendResult =
                                (AddFriendResult) GsonUtil.json2Object(response, AddFriendResult.class);
                        if (addFriendResult != null
                                && addFriendResult.getRet().equals("1")) {
                            Toasty.warning(FriendVerifyActivity.this, "申请添加成功！", Toast.LENGTH_SHORT, false).show();
                            FriendVerifyActivity.this.finish();
                        } else {
                            Toasty.warning(FriendVerifyActivity.this, "申请添加失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibFriendVerifyTitleBack)
    void ibFriendVerifyTitleBack() {
        FriendVerifyActivity.this.finish();
    }
}
