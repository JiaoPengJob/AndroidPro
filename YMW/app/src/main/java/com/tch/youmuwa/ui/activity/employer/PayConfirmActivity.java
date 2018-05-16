package com.tch.youmuwa.ui.activity.employer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.PayResult;
import com.tch.youmuwa.bean.parameters.BeginPayParam;
import com.tch.youmuwa.bean.result.AliPayResult;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.WeChatPayResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.GsonUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 支付确认
 */
public class PayConfirmActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ivWechatSelectIcon)
    ImageView ivWechatSelectIcon;
    @BindView(R.id.ivAliSelectIcon)
    ImageView ivAliSelectIcon;
    @BindView(R.id.tvPayNumber)
    TextView tvPayNumber;

    private String price, name, detail;
    private String number = "";
    private PresenterImpl<Object> presenter;
    private boolean isWeChat = true;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private int pay_way = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_confirm);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("支付确认");
        if (getIntent().getStringExtra("price") != null) {
            price = getIntent().getStringExtra("price");
            double d = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            tvPayNumber.setText(String.valueOf(d));
        }
        if (getIntent().getStringExtra("number") != null) {
            number = getIntent().getStringExtra("number");
        }
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        }
        if (getIntent().getStringExtra("detail") != null) {
            detail = getIntent().getStringExtra("detail");
        }
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
    }

    /**
     * 微信
     */
    @OnClick(R.id.rlWechatPay)
    public void wechatPay() {
        isWeChat = true;
        pay_way = 1;
        ivWechatSelectIcon.setImageResource(R.mipmap.pay_icon_select);
        ivAliSelectIcon.setImageResource(R.mipmap.pay_icon_not_select);
    }

    /**
     * 支付宝
     */
    @OnClick(R.id.rlAliPay)
    public void aliPay() {
        isWeChat = false;
        pay_way = 2;
        ivWechatSelectIcon.setImageResource(R.mipmap.pay_icon_not_select);
        ivAliSelectIcon.setImageResource(R.mipmap.pay_icon_select);
    }

    /**
     * 链接支付接口
     */
    private void payClient() {
        mSVProgressHUD.showWithStatus("加载中...");
        BeginPayParam beginPayParam = new BeginPayParam(
                number,
                pay_way
        );
        presenter = new PresenterImpl<Object>(PayConfirmActivity.this);
        presenter.onCreate();
        presenter.beginpay(beginPayParam);
        presenter.attachView(payView);
    }

    private ClientBaseView<Object> payView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(PayConfirmActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                if (isWeChat) {
                    WeChatPayResult weChatPayResult = (WeChatPayResult) GsonUtil.parseJson(baseBean.getData(), WeChatPayResult.class);
                    weChatPay(weChatPayResult);
                } else {
                    AliPayResult aliPayResult = (AliPayResult) GsonUtil.parseJson(baseBean.getData(), AliPayResult.class);
                    aliPay(aliPayResult);
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "payView==" + result);
        }
    };

    /**
     * 微信支付
     */
    private void weChatPay(WeChatPayResult weChatPayResult) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(PayConfirmActivity.this, weChatPayResult.getAppid());
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
        PayConfirmActivity.this.finish();
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
                PayTask alipay = new PayTask(PayConfirmActivity.this);
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
                        Toast.makeText(PayConfirmActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        PayConfirmActivity.this.finish();
                    } else {
                        Toast.makeText(PayConfirmActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        PayConfirmActivity.this.finish();
                    }
                    break;
            }
        }
    };

    /**
     * 确认
     */
    @OnClick(R.id.btPayConfirm)
    public void payConfirm() {
        payClient();
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatPayConfirm() {
        PayConfirmActivity.this.finish();
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
                PayConfirmActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
