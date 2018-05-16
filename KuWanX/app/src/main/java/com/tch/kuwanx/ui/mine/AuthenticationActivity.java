package com.tch.kuwanx.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.RealAuthResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 身份认证
 */
public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etCode)
    EditText etCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("身份认证");
    }

    /**
     * 提交
     */
    @OnClick(R.id.btSubmitPersonalCode)
    public void submitPersonalCode() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            Toasty.warning(AuthenticationActivity.this, "请输入正确的姓名！", Toast.LENGTH_SHORT, false).show();
        } else if (TextUtils.isEmpty(etCode.getText().toString())) {
            Toasty.warning(AuthenticationActivity.this, "请输入正确的身份证号！", Toast.LENGTH_SHORT, false).show();
        } else if (!Utils.isRealIDCard(etCode.getText().toString())) {
            Toasty.warning(AuthenticationActivity.this, "请输入正确的身份证号！", Toast.LENGTH_SHORT, false).show();
        } else {
            //全部正确进行下一步
            bindNewPhoneHttp();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void authenticationBack() {
        AuthenticationActivity.this.finish();
    }

    /**
     * 绑定新手机号
     */
    private void bindNewPhoneHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(AuthenticationActivity.this));
        map.put("user_realname", etName.getText().toString());
        map.put("user_idcard", etCode.getText().toString());
        String params = EncryptionUtil.getParameter(AuthenticationActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/realAuth.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_realAuth")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(AuthenticationActivity.this, "身份验证失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        RealAuthResult realAuthResult =
                                (RealAuthResult) GsonUtil.json2Object(response, RealAuthResult.class);
                        if (realAuthResult != null
                                && realAuthResult.getRet().equals("1")) {
                            AuthenticationActivity.this.finish();
                        }
                    }
                });
    }
}
