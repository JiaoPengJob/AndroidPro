package com.tch.zx.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tch.zx.R;
import com.tch.zx.util.ConstantData;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 打赏的弹出选项
 */

public class RewardPopupWindow extends PopupWindow implements View.OnClickListener {

    //文本对象
    private Context context;
    //父布局
    private View view;
    //整体布局
    private RelativeLayout rl_popup_reward;
    //取消
    private TextView tv_reward_popup;
    //10元
    private TextView tv_payment_ten;
    //20元
    private TextView tv_payment_twenty;
    //50元
    private TextView tv_payment_fifty;
    //100元
    private TextView tv_payment_hundred;
    //输入框
    private EditText et_popup_pay;
    //微信支付
    private TextView tv_popup_wechat_pay;
    //支付宝支付
    private TextView tv_popup_ali_pay;

    /**
     * 无参构造
     *
     * @param context
     */
    public RewardPopupWindow(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.popup_reward, null);
        initView();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // 设置外部可点击
        setOutsideTouchable(true);
        // view添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = rl_popup_reward.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.share_popup_anim);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        rl_popup_reward = (RelativeLayout) view.findViewById(R.id.rl_popup_reward);
        tv_reward_popup = (TextView) view.findViewById(R.id.tv_reward_popup);
        tv_payment_ten = (TextView) view.findViewById(R.id.tv_payment_ten);
        tv_payment_twenty = (TextView) view.findViewById(R.id.tv_payment_twenty);
        tv_payment_fifty = (TextView) view.findViewById(R.id.tv_payment_fifty);
        tv_payment_hundred = (TextView) view.findViewById(R.id.tv_payment_hundred);
        et_popup_pay = (EditText) view.findViewById(R.id.et_popup_pay);
        tv_popup_wechat_pay = (TextView) view.findViewById(R.id.tv_popup_wechat_pay);
        tv_popup_ali_pay = (TextView) view.findViewById(R.id.tv_popup_ali_pay);

        tv_payment_ten.setAlpha(0.7f);
        tv_payment_twenty.setAlpha(0.7f);
        tv_payment_fifty.setAlpha(0.7f);
        tv_payment_hundred.setAlpha(0.7f);

        tv_reward_popup.setOnClickListener(this);
        tv_payment_ten.setOnClickListener(this);
        tv_payment_twenty.setOnClickListener(this);
        tv_payment_fifty.setOnClickListener(this);
        tv_payment_hundred.setOnClickListener(this);
        et_popup_pay.setOnClickListener(this);
        tv_popup_wechat_pay.setOnClickListener(this);
        tv_popup_ali_pay.setOnClickListener(this);
    }

    /**
     * 点击事件监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //取消
            case R.id.tv_reward_popup:
                dismiss();
                break;
            //10元
            case R.id.tv_payment_ten:
                if (tv_payment_ten.getAlpha() == 0.7f) {
                    tv_payment_ten.setAlpha(1f);
                    tv_payment_twenty.setAlpha(0.7f);
                    tv_payment_fifty.setAlpha(0.7f);
                    tv_payment_hundred.setAlpha(0.7f);
                } else {
                    tv_payment_ten.setAlpha(0.7f);
                }
                break;
            //20元
            case R.id.tv_payment_twenty:
                if (tv_payment_twenty.getAlpha() == 0.7f) {
                    tv_payment_ten.setAlpha(0.7f);
                    tv_payment_twenty.setAlpha(1f);
                    tv_payment_fifty.setAlpha(0.7f);
                    tv_payment_hundred.setAlpha(0.7f);
                } else {
                    tv_payment_twenty.setAlpha(0.7f);
                }
                break;
            //50元
            case R.id.tv_payment_fifty:
                if (tv_payment_fifty.getAlpha() == 0.7f) {
                    tv_payment_ten.setAlpha(0.7f);
                    tv_payment_twenty.setAlpha(0.7f);
                    tv_payment_fifty.setAlpha(1f);
                    tv_payment_hundred.setAlpha(0.7f);
                } else {
                    tv_payment_fifty.setAlpha(0.7f);
                }
                break;
            //100元
            case R.id.tv_payment_hundred:
                if (tv_payment_hundred.getAlpha() == 0.7f) {
                    tv_payment_ten.setAlpha(0.7f);
                    tv_payment_twenty.setAlpha(0.7f);
                    tv_payment_fifty.setAlpha(0.7f);
                    tv_payment_hundred.setAlpha(1f);
                } else {
                    tv_payment_hundred.setAlpha(0.7f);
                }
                break;
            //输入框
            case R.id.et_popup_pay:
                et_popup_pay.setFocusable(true);
                et_popup_pay.setFocusableInTouchMode(true);
                tv_payment_ten.setAlpha(0.7f);
                tv_payment_twenty.setAlpha(0.7f);
                tv_payment_fifty.setAlpha(0.7f);
                tv_payment_hundred.setAlpha(0.7f);
                break;
            //微信支付
            case R.id.tv_popup_wechat_pay:

                break;
            //支付宝支付
            case R.id.tv_popup_ali_pay:
                aliPay("");
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) context);
                String result = alipay.pay(orderInfo, true);

                Message msg = new Message();
                msg.what = ConstantData.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantData.SDK_PAY_FLAG:

                    break;
                case ConstantData.SDK_AUTH_FLAG:

                    break;
            }
        }
    };


}
