package com.tch.kuwanx.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.GoodDetailResult;
import com.tch.kuwanx.result.SubmitOrderResult;
import com.tch.kuwanx.result.UserAddressResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.mine.ShipAddressActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.PayPsdInputView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 确认订单
 * item_order_sub,列表样式购物车
 */
public class ConfirmOrderActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etConfirmNum)
    EditText etConfirmNum;
    @BindView(R.id.tvConfirmSelNum)
    TextView tvConfirmSelNum;
    @BindView(R.id.tvConfirmCount)
    TextView tvConfirmCount;
    @BindView(R.id.tvConfirmOrderName)
    TextView tvConfirmOrderName;

    private int articleNum = 1;
    private String goodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("确认订单");
        if (getIntent().getStringExtra("goodId") != null) {
            goodId = getIntent().getStringExtra("goodId");
            articleNum = getIntent().getIntExtra("number", 1);
        }
        etConfirmNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    etConfirmNum.setText("1");
                } else {
                    articleNum = Integer.parseInt(charSequence.toString());
                }
//                tvConfirmSelNum.setText(etConfirmNum.getText().toString());
                tvConfirmCount.setText("共" + etConfirmNum.getText().toString() + "件商品");
                tvConfirmSelCount.setText(String.valueOf(Integer.parseInt(goodDetail.getResult().getCurrent_price()) * articleNum));
                tvConfirmOrderSun.setText(String.valueOf(Integer.parseInt(goodDetail.getResult().getCurrent_price()) * articleNum));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        getUserAddressHttp();
        getGoodDetailHttp();
    }

    /**
     * 减号
     */
    @OnClick(R.id.ibConfirmMinus)
    public void confirmMinus() {
        if (articleNum > 1) {
            etConfirmNum.setText(String.valueOf(--articleNum));
            tvConfirmSelCount.setText(String.valueOf(Integer.parseInt(goodDetail.getResult().getCurrent_price()) * articleNum));
            tvConfirmOrderSun.setText(String.valueOf(Integer.parseInt(goodDetail.getResult().getCurrent_price()) * articleNum));
        }
    }

    /**
     * 加号
     */
    @OnClick(R.id.ibConfirmPlus)
    public void confirmPlus() {
        etConfirmNum.setText(String.valueOf(++articleNum));
        tvConfirmSelCount.setText(String.valueOf(Integer.parseInt(goodDetail.getResult().getCurrent_price()) * articleNum));
        tvConfirmOrderSun.setText(String.valueOf(Integer.parseInt(goodDetail.getResult().getCurrent_price()) * articleNum));
    }

    /**
     * 选择地址
     */
    @OnClick(R.id.rlConfirmOrderAddress)
    void confirmOrderAddress() {
        Intent intent = new Intent(ConfirmOrderActivity.this, ShipAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("activity", "ConfirmOrderActivity");
        bundle.putSerializable("address", defaultAddress);
        intent.putExtras(bundle);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                UserAddressResult.ResultBean rb = (UserAddressResult.ResultBean) data.getSerializableExtra("address");
                tvConfirmOrderName.setText(rb.getName());
                tvConfirmOrderPhone.setText(rb.getPhone());
                tvConfirmOrderAddress.setText(rb.getDetail());
                break;
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void OrderTitleBack() {
        ConfirmOrderActivity.this.finish();
    }

    /**
     * 商品页/商品详情
     */
    private void getGoodDetailHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("good_id", goodId);
        String params = EncryptionUtil.getParameter(ConfirmOrderActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getGoodDetail.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGoodDetail")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ConfirmOrderActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ConfirmOrderActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        goodDetail =
                                (GoodDetailResult) GsonUtil.json2Object(response, GoodDetailResult.class);
                        if (goodDetail != null
                                && goodDetail.getRet().equals("1")) {
                            initHttpInfo(goodDetail.getResult());
                        } else {
                            Toasty.warning(ConfirmOrderActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private GoodDetailResult goodDetail;

    @BindView(R.id.ivConfirmOrderCdImg)
    ImageView ivConfirmOrderCdImg;
    @BindView(R.id.tvConfirmOrderCdName)
    TextView tvConfirmOrderCdName;
    @BindView(R.id.tvConfirmOrderCdContent)
    TextView tvConfirmOrderCdContent;
    @BindView(R.id.tvConfirmOrderCdPrice)
    TextView tvConfirmOrderCdPrice;
    @BindView(R.id.tvConfirmSelCount)
    TextView tvConfirmSelCount;
    @BindView(R.id.tvConfirmOrderSun)
    TextView tvConfirmOrderSun;
    @BindView(R.id.tvConfirmOrderType)
    TextView tvConfirmOrderType;

    private void initHttpInfo(GoodDetailResult.ResultBean goodDetail) {
        Glide.with(ConfirmOrderActivity.this)
                .load(goodDetail.getGood_cover())
                .into(ivConfirmOrderCdImg);
        tvConfirmOrderCdName.setText(goodDetail.getGood_name());
        tvConfirmOrderCdContent.setText(goodDetail.getGood_intr());
        tvConfirmOrderCdPrice.setText(goodDetail.getCurrent_price());
        //String.valueOf(Integer.parseInt(goodDetail.getCurrent_price()) * articleNum)
//        tvConfirmSelNum.setText(String.valueOf(articleNum));
        tvConfirmSelCount.setText(String.valueOf(Integer.parseInt(goodDetail.getCurrent_price()) * articleNum));
        tvConfirmOrderSun.setText(String.valueOf(Integer.parseInt(goodDetail.getCurrent_price()) * articleNum));

        if (goodDetail.getGood_type_id().equals("10")) {
            tvConfirmOrderType.setText("游戏设备");
        } else if (goodDetail.getGood_type_id().equals("20")) {
            tvConfirmOrderType.setText("游戏光盘");
        } else if (goodDetail.getGood_type_id().equals("30")) {
            tvConfirmOrderType.setText("游戏周边");
        }
    }

    /**
     * 获取用户地址
     */
    private void getUserAddressHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(ConfirmOrderActivity.this));
        String params = EncryptionUtil.getParameter(ConfirmOrderActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserAddress.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getUserAddress")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ConfirmOrderActivity.this, "获取用户地址失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        UserAddressResult userAddressResult =
                                (UserAddressResult) GsonUtil.json2Object(response, UserAddressResult.class);
                        if (userAddressResult != null
                                && userAddressResult.getRet().equals("1")) {
                            for (UserAddressResult.ResultBean item : userAddressResult.getResult()) {
                                if (item.getIddefault().equals("2")) {
                                    defaultAddress = item;
                                    tvConfirmOrderName.setText(item.getName());
                                    tvConfirmOrderPhone.setText(item.getPhone());
                                    tvConfirmOrderAddress.setText(item.getDetail());
                                }
                            }
                        } else {
                            Toasty.warning(ConfirmOrderActivity.this, "获取用户地址失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private UserAddressResult.ResultBean defaultAddress;

    @BindView(R.id.tvConfirmOrderPhone)
    TextView tvConfirmOrderPhone;
    @BindView(R.id.tvConfirmOrderAddress)
    TextView tvConfirmOrderAddress;

    /**
     * 提交
     */
    @OnClick(R.id.btSubmitOrder)
    void btSubmitOrder() {
        addUserAddress();
    }

    @BindView(R.id.etConfirmOrderMsg)
    EditText etConfirmOrderMsg;

    private void addUserAddress() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ConfirmOrderActivity.this));
        map.put("consignee", tvConfirmOrderName.getText().toString());//收货人
        map.put("consignee_phone", tvConfirmOrderPhone.getText().toString());
        map.put("good_id", goodId);
        map.put("good_count", etConfirmNum.getText().toString());
        map.put("consignee_adress", tvConfirmOrderAddress.getText().toString());
        map.put("order_amt", tvConfirmOrderSun.getText().toString());
        map.put("user_message", etConfirmOrderMsg.getText().toString());
        map.put("peisong_mode", "shunfeng");

        String params = EncryptionUtil.getParameter(ConfirmOrderActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/submitOrder.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_submitOrder")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ConfirmOrderActivity.this, "提交中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ConfirmOrderActivity.this, "提交失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SubmitOrderResult submitOrderResult =
                                (SubmitOrderResult) GsonUtil.json2Object(response, SubmitOrderResult.class);
                        if (submitOrderResult != null
                                && submitOrderResult.getRet().equals("1")) {
                            showPay();
                        } else {
                            Toasty.warning(ConfirmOrderActivity.this, "提交失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private CustomPopWindow payPop;
    @BindView(R.id.rlConfirmOrder)
    RelativeLayout rlConfirmOrder;
    @BindView(R.id.viewConfirmOrderBlack)
    View viewConfirmOrderBlack;
    private ImageButton ibPopPayCancel;
    private PayPsdInputView payPassword;
    private TextView tvPopPayNumber;

    /**
     * 显示支付弹窗
     */
    private void showPay() {
        View view = LayoutInflater.from(ConfirmOrderActivity.this).
                inflate(R.layout.pop_pay, null);
        ibPopPayCancel = (ImageButton) view.findViewById(R.id.ibPopPayCancel);
        payPassword = (PayPsdInputView) view.findViewById(R.id.payPassword);
        tvPopPayNumber = (TextView) view.findViewById(R.id.tvPopPayNumber);
        tvPopPayNumber.setText(tvConfirmOrderSun.getText().toString());
        ibPopPayCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payPop != null) {
                    payPop.dissmiss();
                }
            }
        });
        payPassword.setComparePassword(DaoUtils.getUserpayPwd(ConfirmOrderActivity.this), new PayPsdInputView.onPasswordListener() {
            @Override
            public void onDifference() {
                Toasty.warning(ConfirmOrderActivity.this, "支付密码不正确！", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onEqual(String psd) {
                Toasty.warning(ConfirmOrderActivity.this, "完成支付", Toast.LENGTH_SHORT, false).show();
                if (payPop != null) {
                    payPop.dissmiss();
                }
                ConfirmOrderActivity.this.finish();
            }
        });
        payPop = new CustomPopWindow.PopupWindowBuilder(ConfirmOrderActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewConfirmOrderBlack.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(rlConfirmOrder, Gravity.CENTER, 0, -60);
        viewConfirmOrderBlack.setVisibility(View.VISIBLE);
    }

}
