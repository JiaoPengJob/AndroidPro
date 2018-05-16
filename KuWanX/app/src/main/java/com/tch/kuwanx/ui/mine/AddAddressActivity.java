package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AddUserAddressResult;
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
 * 添加新地址
 */
public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rgAddressSex)
    RadioGroup rgAddressSex;
    @BindView(R.id.rbMan)
    RadioButton rbMan;
    @BindView(R.id.rbWoman)
    RadioButton rbWoman;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.etAddressName)
    EditText etAddressName;
    @BindView(R.id.etAddressPhone)
    EditText etAddressPhone;
    @BindView(R.id.etAddressDoor)
    EditText etAddressDoor;
    @BindView(R.id.ibAddressDefault)
    ImageButton ibAddressDefault;
    @BindView(R.id.tvFormatAddress)
    TextView tvFormatAddress;

    private int sex = 1;
    private boolean isDefault = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("添加新地址");
        btTitleFeatures.setVisibility(View.VISIBLE);
        btTitleFeatures.setText("确认");
        rgAddressSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rbMan.getId()) {
                    sex = 1;
                } else if (i == rbWoman.getId()) {
                    sex = 2;
                }
            }
        });
    }

    /**
     * 确认
     */
    @OnClick(R.id.btTitleFeatures)
    public void addAddress() {
        if (TextUtils.isEmpty(etAddressName.getText().toString())) {
            Toasty.warning(AddAddressActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT, false).show();
        } else if (TextUtils.isEmpty(etAddressPhone.getText().toString())) {
            Toasty.warning(AddAddressActivity.this, "手机不能为空！", Toast.LENGTH_SHORT, false).show();
        } else {
            //全部通过
            addUserAddress();
        }
    }

    /**
     * 选择地点，打开地图
     */
    @OnClick(R.id.rlAddressChoose)
    public void addressChoose() {
        Intent intent = new Intent(AddAddressActivity.this, MapActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                tvFormatAddress.setText(data.getStringExtra("formatAddress"));
                break;
        }
    }

    private String defaultAddress = "1";

    /**
     * 设为默认地址
     */
    @OnClick(R.id.ibAddressDefault)
    public void addressDefault() {
        if (isDefault) {
            isDefault = false;
            ibAddressDefault.setImageResource(R.drawable.oval_unselect);
            defaultAddress = "1";
        } else {
            isDefault = true;
            ibAddressDefault.setImageResource(R.drawable.default_sel);
            defaultAddress = "2";
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void addAddressBack() {
        AddAddressActivity.this.finish();
    }

    /**
     * 添加新地址
     */
    private void addUserAddress() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(AddAddressActivity.this));
        map.put("name", etAddressName.getText().toString());
        map.put("phone", etAddressPhone.getText().toString());
        map.put("region", tvFormatAddress.getText().toString());
        map.put("detail", etAddressDoor.getText().toString());
        map.put("sex", String.valueOf(sex));
        map.put("iddefault", defaultAddress);
        String params = EncryptionUtil.getParameter(AddAddressActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/addUserAddress.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addUserAddress")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        AddAddressActivity.this, "添加中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(AddAddressActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddUserAddressResult addUserAddressResult =
                                (AddUserAddressResult) GsonUtil.json2Object(response, AddUserAddressResult.class);
                        if (addUserAddressResult != null
                                && addUserAddressResult.getRet().equals("1")) {
                            AddAddressActivity.this.finish();
                        }else{
                            Toasty.warning(AddAddressActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
