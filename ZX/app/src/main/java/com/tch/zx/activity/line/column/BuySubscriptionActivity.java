package com.tch.zx.activity.line.column;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.bean.AliPayResult;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.bean.PayResult;
import com.tch.zx.bean.WeChatPayResult;
import com.tch.zx.http.bean.result.SpecialSubscribeResultBean;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.pay.alipay.AliPayModel;
import com.tch.zx.pay.alipay.AliPayTools;
import com.tch.zx.pay.alipay.OnRequestListener;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购买专栏订阅页面
 */
public class BuySubscriptionActivity extends BaseActivity {
    /**
     * 标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    @BindView(R.id.ivWechatSelect)
    ImageView ivWechatSelect;
    @BindView(R.id.ivAliSelect)
    ImageView ivAliSelect;
    @BindView(R.id.tvTitleColumn)
    TextView tvTitleColumn;
    @BindView(R.id.tvPriceColumn)
    TextView tvPriceColumn;
    @BindView(R.id.tv_sure_pay)
    TextView tv_sure_pay;

    private SpecialSubscribeResultBean.ResultBean.ResponseObjectBean subscribeResultBean;
    private int payType = 0;
    private BasePresenter<Object> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_buy_subscription);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        tv_title_top_all.setText("专栏订阅");
        if (getIntent().getSerializableExtra("subscribeResultBean") != null) {
            subscribeResultBean = (SpecialSubscribeResultBean.ResultBean.ResponseObjectBean) getIntent().getSerializableExtra("subscribeResultBean");
        }
        tvTitleColumn.setText(subscribeResultBean.getSpecialColumnName());
        tvPriceColumn.setText("￥" + subscribeResultBean.getSpecialColumnPrice() + "/年");
        tv_sure_pay.setText("确认支付   " + subscribeResultBean.getSpecialColumnPrice() + "元");
    }

    /**
     * 确认购买
     */
    @OnClick(R.id.tv_sure_pay)
    public void surePay() {
        if (payType == 0) {
            //微信支付
//            WXUtil.pay(iwxapi);
        } else {
            //支付宝支付
//            setPayInfo();
            createOrder();
        }
    }

    /**
     * 选中微信支付
     */
    @OnClick(R.id.rl_wechat_my)
    public void wechatPay() {
        ivWechatSelect.setImageResource(R.mipmap.select_pay);
        ivAliSelect.setImageResource(R.mipmap.unselect_pay);
        payType = 0;
    }

    /**
     * 微信支付
     */
    private void weChatPay(WeChatPayResult weChatPayResult) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(BuySubscriptionActivity.this, weChatPayResult.getAppid());
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
        BuySubscriptionActivity.this.finish();
    }

    /**
     * 选中支付宝支付
     */
    @OnClick(R.id.rl_ali_my)
    public void aliPay() {
        ivAliSelect.setImageResource(R.mipmap.select_pay);
        ivWechatSelect.setImageResource(R.mipmap.unselect_pay);
        payType = 1;
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
                PayTask alipay = new PayTask(BuySubscriptionActivity.this);
                //获取支付结果
                Map<String, String> result = alipay.payV2(aliPay.getResponseObject(), true);
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
                        Toast.makeText(BuySubscriptionActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        BuySubscriptionActivity.this.finish();
                    } else {
                        Toast.makeText(BuySubscriptionActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        BuySubscriptionActivity.this.finish();
                    }
                    break;
            }
        }
    };

    private String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCihgqayStqMCuo1TmCieqYMD2m8ZBT0nprnZKExDe6r6CyRbQ4Tbfl6DxdMZqo/yk3jVGXNhFGj3mO8u7fuw8/DOLY06mQw3Uut3R5g/d9maq3PB/naMXe67Yxd8eiWhozQsiX4OyY+wTh9f95JoP+PzdHyI2c9wdigtmTDnQXgGlhXMgvUL37ZlT8YRhOQXyFIORmglVwXpvOQx36Wv4QLCzuJCHz4DcIJMv34sJCaMdpoLd4MKnCFcOyfeE0RI6RnoWL/rtjyg2h1WIEPaM8XfNDMlF5q/DxhsWg7c2jFAAkUSVeMDmH5jsGjXCOWyG37CEbSHBc6ed1FRFYtd1BAgMBAAECggEANMTu5okQ5p6TXnM1BBSBrj5jljPy17SMeSqlayRGr//cXmXReUKKxkRcbSnw/JQ0rsvcxQsSGuX04xwvwUb8UU1+zkYTVWSRRMTeNaJKlmZxjqMf/MQBEztXYcJxeF5nCWPSu7WVPKqsUC3OcGi4DLI74bvVdzSiGjNAL8ZOnV5Wh5bjUamHCbFmlJurpywCZBohpHWQBTrn01YeF0T40/lQ9TDY8xLJFZYMNm2uCXz7iAcOBRBdWpPJoxqfbIK6oAdekKNVehZwWIFPGsCpIZ7+DEMPs/z5TjpRvZpVLl5oZwP9e7mU8MTpNCUYmXT4LzlEWC21Boz4UP44QGvMQQKBgQD2R/bt6+zE8D7jnR8sydo9Rx3KcDVicZjmO/fLTDEnGbXOBtrDi/rzBkVkQG6+FZDxP11IM/2XWtnw34L+Ahfv4XQBWCNsQQJ3xyZY/skTPNJsxcjgja27Os2n9c69myUkp0WrFToeGBp1jgIc0CsvR6P1MKGJCzVbR929alaqCQKBgQCo7+xYHwYTyPfe06ajKrT4YWNQQuWvN5HhbqkAHACfHf5LMPDGAK0FQEMRw5TUjk2Xnh+bWGHdtbIhWXSVRZ0tSk1+j2MS0jk1pVgSCBGco5fllsuTUR1LIDlsTTinhj9nv6RI7T0tGwF9DsqTRSPidad4sNgYLChePnxVKZ9HeQKBgADL8syKOtXYQRvTYkpuzLnuBPamrkWHQIteHksWEAKkXqYv930ycrleh9LvMmyC/VkKb27QXD2lGZdh4baYoyMvg4SNmG+9NhjVGBapnFTnmXaonSG805cLdabZcLXETrZzpBINm4aFZXCD6RXWxoz8mlcstRkQZDfLX7hBI86hAoGAZPG3z4/6GahkNAlT8Pt7106aAUN36xLRYFq3MRGKbYmGgXdk52tP92tGqD6tl7TQOGCLkk9law0+ux6wwuEt4focBBu5n21uMdfyoBOiMm6uPMm2Kvi1AEbpCQW7bJ6TFHM2vzzC6UjRwuAru4P8xTx4LW2+MfNwy5+au2h7fEECgYAJkdgKRbzDWFszbt7z8Y5DdtPX0tbX4NGmcGlnUXpduWrlS9gCMUbFMYEKGG83wi4KcuOv4FtQmdvcxu4sXufRs9QAIGWqCu869hmyVBNIRa4sDpv/2xrfdTIl7RJuGQvYpcxRODtvy+6JdRajEiAm4DNjAX8hQNkOFNz/jYSrdg==";
    private String APPID = "2017090608585644";

    private void setPayInfo() {
        AliPayTools.aliPay(BuySubscriptionActivity.this,
                APPID,//支付宝分配的APP_ID
                true,//是否是 RSA2 加密
                privateKey,// RSA 或 RSA2 字符串
                new AliPayModel("10892323200710191644",//订单ID (唯一)
                        "0.01",//价格
                        "测试用商品",//商品名称
                        "描述一下吧"),//商品描述详情 (用于显示在 支付宝 的交易记录里)
                new OnRequestListener() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(BuySubscriptionActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String s) {
                        Toast.makeText(BuySubscriptionActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createOrder() {

        presenter = new BasePresenter<Object>(BuySubscriptionActivity.this);
        presenter.onCreate();
        presenter.attachView(createOrderView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("app_user_id", HelperUtil.getAppUserId(BuySubscriptionActivity.this));
        map.put("class_name", subscribeResultBean.getSpecialColumnName());
        map.put("order_class_type", "3");
        map.put("order_class_type_id", subscribeResultBean.getSpecialColumnId());
        map.put("order_type", "2");
        map.put("order_money", subscribeResultBean.getSpecialColumnPrice());
        map.put("pay_type", "1");
        map.put("pay_user_id", subscribeResultBean.getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.createOrder(data);
    }

    private BaseView<Object> createOrderView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                if (payType == 0) {

                } else {
                    AliPayResult aliPayResult = (AliPayResult) GsonUtil.parseJson(baseResultBean.getResult(), AliPayResult.class);
                    aliPay(aliPayResult);
                }

            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "createOrderView接口错误" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBack() {
        onDestroy();
    }
}
