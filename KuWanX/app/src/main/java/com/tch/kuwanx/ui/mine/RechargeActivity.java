package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.AliPayResult;
import com.tch.kuwanx.bean.PayResult;
import com.tch.kuwanx.bean.WeChatPayResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etRechargeMoney)
    EditText etRechargeMoney;
    @BindView(R.id.btReTwoHundred)
    Button btReTwoHundred;
    @BindView(R.id.btReFiveHundred)
    Button btReFiveHundred;
    @BindView(R.id.btReOneThousand)
    Button btReOneThousand;
    @BindView(R.id.btReTenThousand)
    Button btReTenThousand;
    @BindView(R.id.ibAPay)
    ImageButton ibAPay;
    @BindView(R.id.ibWechatPay)
    ImageButton ibWechatPay;

    private int payType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("充值");
        etRechargeMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                if (temp.length() == 2) {
                    if (temp.indexOf("0") == 0) {
                        if (!temp.substring(1).equals(".")) {
                            etRechargeMoney.setText("0");
                            etRechargeMoney.setSelection(etRechargeMoney.length());
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
     * 输入框点击
     */
    @OnClick(R.id.etRechargeMoney)
    public void rechargeMoney() {
        btReTwoHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReFiveHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReOneThousand.setBackgroundResource(R.drawable.oval_recharge);
        btReTenThousand.setBackgroundResource(R.drawable.oval_recharge);

        btReTwoHundred.setTextColor(Color.parseColor("#333333"));
        btReFiveHundred.setTextColor(Color.parseColor("#333333"));
        btReOneThousand.setTextColor(Color.parseColor("#333333"));
        btReTenThousand.setTextColor(Color.parseColor("#333333"));
    }

    /**
     * 200
     */
    @OnClick(R.id.btReTwoHundred)
    public void reTwoHundred() {
        btReTwoHundred.setBackgroundResource(R.drawable.oval_recharge_item);
        btReFiveHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReOneThousand.setBackgroundResource(R.drawable.oval_recharge);
        btReTenThousand.setBackgroundResource(R.drawable.oval_recharge);

        btReTwoHundred.setTextColor(Color.parseColor("#FFFFFF"));
        btReFiveHundred.setTextColor(Color.parseColor("#333333"));
        btReOneThousand.setTextColor(Color.parseColor("#333333"));
        btReTenThousand.setTextColor(Color.parseColor("#333333"));

        etRechargeMoney.setText("200");
    }

    /**
     * 500
     */
    @OnClick(R.id.btReFiveHundred)
    public void reFiveHundred() {
        btReTwoHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReFiveHundred.setBackgroundResource(R.drawable.oval_recharge_item);
        btReOneThousand.setBackgroundResource(R.drawable.oval_recharge);
        btReTenThousand.setBackgroundResource(R.drawable.oval_recharge);

        btReTwoHundred.setTextColor(Color.parseColor("#333333"));
        btReFiveHundred.setTextColor(Color.parseColor("#FFFFFF"));
        btReOneThousand.setTextColor(Color.parseColor("#333333"));
        btReTenThousand.setTextColor(Color.parseColor("#333333"));

        etRechargeMoney.setText("500");
    }

    /**
     * 1000
     */
    @OnClick(R.id.btReOneThousand)
    public void reOneThousand() {
        btReTwoHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReFiveHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReOneThousand.setBackgroundResource(R.drawable.oval_recharge_item);
        btReTenThousand.setBackgroundResource(R.drawable.oval_recharge);

        btReTwoHundred.setTextColor(Color.parseColor("#333333"));
        btReFiveHundred.setTextColor(Color.parseColor("#333333"));
        btReOneThousand.setTextColor(Color.parseColor("#FFFFFF"));
        btReTenThousand.setTextColor(Color.parseColor("#333333"));

        etRechargeMoney.setText("1000");
    }

    /**
     * 10000
     */
    @OnClick(R.id.btReTenThousand)
    public void reTenThousand() {
        btReTwoHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReFiveHundred.setBackgroundResource(R.drawable.oval_recharge);
        btReOneThousand.setBackgroundResource(R.drawable.oval_recharge);
        btReTenThousand.setBackgroundResource(R.drawable.oval_recharge_item);

        btReTwoHundred.setTextColor(Color.parseColor("#333333"));
        btReFiveHundred.setTextColor(Color.parseColor("#333333"));
        btReOneThousand.setTextColor(Color.parseColor("#333333"));
        btReTenThousand.setTextColor(Color.parseColor("#FFFFFF"));

        etRechargeMoney.setText("10000");
    }

    /**
     * 支付宝支付
     */
    @OnClick(R.id.ibAPay)
    public void aPay() {
        payType = 1;
        ibAPay.setImageResource(R.drawable.recharge_sel);
        ibWechatPay.setImageResource(R.drawable.oval_unselect);
    }

    /**
     * 微信支付
     */
    @OnClick(R.id.ibWechatPay)
    public void wechatPay() {
        payType = 2;
        ibAPay.setImageResource(R.drawable.oval_unselect);
        ibWechatPay.setImageResource(R.drawable.recharge_sel);
    }

    /**
     * 充值
     */
    @OnClick(R.id.btRechargeSubmit)
    public void rechargeSubmit() {
        if (payType == 1) {

        } else if (payType == 2) {

        }
    }

    /**
     * 支付宝支付
     *
     * @param aliPay
     */
    private void aliPay(final AliPayResult aliPay) {
        //异步处理
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //新建任务
                PayTask alipay = new PayTask(RechargeActivity.this);
                //获取支付结果
                Map<String, String> result = alipay.payV2(aliPay.getOrder_string(), true);
                Message msg = new Message();
                msg.what = 1001;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toasty.warning(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT, false).show();
                        RechargeActivity.this.finish();
                    } else {
                        Toasty.warning(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT, false).show();
                        RechargeActivity.this.finish();
                    }
                    break;
            }
        }
    };

    /**
     * 微信支付
     */
    private void weChatPay(WeChatPayResult weChatPayResult) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(RechargeActivity.this, weChatPayResult.getAppid());
        msgApi.registerApp(weChatPayResult.getAppid());
        PayReq request = new PayReq();
        request.appId = weChatPayResult.getAppid();
        request.partnerId = weChatPayResult.getPartnerid();
        request.prepayId = weChatPayResult.getPrepayid();
        request.packageValue = weChatPayResult.getPackageX();
        request.nonceStr = weChatPayResult.getNoncestr();
        request.timeStamp = String.valueOf(weChatPayResult.getTimestamp());
        request.sign = weChatPayResult.getSign();
        msgApi.sendReq(request);
        RechargeActivity.this.finish();
    }

    /**
     * 充值说明
     */
    @OnClick(R.id.tvRechargeDes)
    public void rechargeDes() {
        Intent intent = new Intent();
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void rechargeBack() {
        RechargeActivity.this.finish();
    }
}
