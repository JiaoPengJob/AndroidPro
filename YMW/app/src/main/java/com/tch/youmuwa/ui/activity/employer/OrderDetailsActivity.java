package com.tch.youmuwa.ui.activity.employer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.JpushParam;
import com.tch.youmuwa.bean.parameters.OrderInfoParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.OrderInfoResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.login.TermsServiceContentActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvOrderAgain)
    TextView tvOrderAgain;
    @BindView(R.id.llOrderWorkerStars)
    LinearLayout llOrderWorkerStars;
    @BindView(R.id.tvOrderWorkerPhoneNum)
    TextView tvOrderWorkerPhoneNum;
    @BindView(R.id.tvOrderWorkerGoodAt)
    TextView tvOrderWorkerGoodAt;
    @BindView(R.id.tvOrderWorkerTypes)
    TextView tvOrderWorkerTypes;
    @BindView(R.id.tvOrderWorkerYears)
    TextView tvOrderWorkerYears;
    @BindView(R.id.tvOrderWorkerAge)
    TextView tvOrderWorkerAge;
    @BindView(R.id.tvOrderWorkerName)
    TextView tvOrderWorkerName;
    @BindView(R.id.ivOrderWorkerPhoto)
    ImageView ivOrderWorkerPhoto;
    @BindView(R.id.tvOrderUserPhone)
    TextView tvOrderUserPhone;
    @BindView(R.id.tvOrderUserName)
    TextView tvOrderUserName;
    @BindView(R.id.tvOrderDescription)
    TextView tvOrderDescription;
    @BindView(R.id.tvOrderWorkArea)
    TextView tvOrderWorkArea;
    @BindView(R.id.tvOrderWorkTime)
    TextView tvOrderWorkTime;
    @BindView(R.id.tvOrderOnDoorPrice)
    TextView tvOrderOnDoorPrice;
    @BindView(R.id.llOrderOnDoorPrice)
    LinearLayout llOrderOnDoorPrice;
    @BindView(R.id.tvOrderDayPrice)
    TextView tvOrderDayPrice;
    @BindView(R.id.llOrderDayPrice)
    LinearLayout llOrderDayPrice;
    @BindView(R.id.tvOrderWorkerType)
    TextView tvOrderWorkerType;
    @BindView(R.id.tvOrderNeedType)
    TextView tvOrderNeedType;
    @BindView(R.id.tvOrderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.tvOrderState)
    TextView tvOrderState;
    @BindView(R.id.tvOrderDate)
    TextView tvOrderDate;
    @BindView(R.id.tvOrderWTitle)
    TextView tvOrderWTitle;
    @BindView(R.id.tvLeftButton)
    TextView tvLeftButton;
    @BindView(R.id.tvRightButton)
    TextView tvRightButton;
    @BindView(R.id.llBottomSelect)
    LinearLayout llBottomSelect;
    @BindView(R.id.tvOrderSendTime)
    TextView tvOrderSendTime;
    @BindView(R.id.llOrderEvaluatedInfo)
    LinearLayout llOrderEvaluatedInfo;
    @BindView(R.id.llOrderEvaluatedInfoLeave)
    LinearLayout llOrderEvaluatedInfoLeave;
    @BindView(R.id.tvOrderEvaluatedInfo)
    TextView tvOrderEvaluatedInfo;
    @BindView(R.id.llOrderDismiss)
    LinearLayout llOrderDismiss;
    @BindView(R.id.tvOrderDismiss)
    TextView tvOrderDismiss;
    @BindView(R.id.tvOrderDismissInfo)
    TextView tvOrderDismissInfo;

    @BindView(R.id.llOrderPrice)
    LinearLayout llOrderPrice;
    @BindView(R.id.tvOrderPrice)
    TextView tvOrderPrice;
    @BindView(R.id.llOrderDingjin)
    LinearLayout llOrderDingjin;
    @BindView(R.id.tvOrderDingjin)
    TextView tvOrderDingjin;
    /**
     * 设置的参数
     */
    private TextView tvDismissOrderProtocol, tvThirdPact, tvDetermineOrderProtocol, tvDismissOrderDismiss, tvDetermineOrderDismiss, tvProName, tvName;//弹出框的按钮
    private Intent intent;//跳转
    private Dialog dialog;//弹出框
    private String phone = "";//手机号
    private String number;//标识
    private PresenterImpl<Object> presenter;//链接
    private SVProgressHUD mSVProgressHUD;//加载显示
    private String price = "";
    private OrderInfoResult orderInfo;
    private JpushParam jp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("订单详情");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getStringExtra("number") != null) {
            number = getIntent().getStringExtra("number");
        }

        if (getIntent().getSerializableExtra("jp") != null) {
            jp = (JpushParam) getIntent().getSerializableExtra("jp");
            number = jp.getOrder_number();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initView();
        orderInfo();
    }

    /**
     * 再次下单
     */
    @OnClick(R.id.tvOrderAgain)
    public void orderAgain() {
        intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
        intent.putExtra("workerId", orderInfo.getWorker_info().getId());
        startActivity(intent);
    }

    /**
     * 显示订单协议弹窗
     */
    private void showDialog() {
        dialog = new Dialog(this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_order_protocol, null);
        tvDismissOrderProtocol = (TextView) layout.findViewById(R.id.tvDismissOrderProtocol);
        tvDetermineOrderProtocol = (TextView) layout.findViewById(R.id.tvDetermineOrderProtocol);
        tvThirdPact = (TextView) layout.findViewById(R.id.tvThirdPact);
        tvThirdPact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, TermsServiceContentActivity.class);
                intent.putExtra("activity", "third");
                startActivity(intent);
            }
        });
        tvProName = (TextView) layout.findViewById(R.id.tvProName);
        tvProName.setText(orderInfo.getWorker_info().getName());
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvDismissOrderProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //确认
        tvDetermineOrderProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(OrderDetailsActivity.this, PayConfirmActivity.class);
                intent.putExtra("number", number);
                intent.putExtra("price", price);
                intent.putExtra("name", orderInfo.getTitle());
                intent.putExtra("detail", orderInfo.getDescription());
                startActivity(intent);
            }
        });
    }

    /**
     * 打电话
     */
    @OnClick(R.id.ivPlayPhone)
    public void playPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 获取详情
     */
    private void orderInfo() {
        mSVProgressHUD.showWithStatus("加载中...");
        OrderInfoParam orderInfoParam = new OrderInfoParam(
                number
        );
        presenter = new PresenterImpl<Object>(OrderDetailsActivity.this);
        presenter.onCreate();
        presenter.getorderinfo(orderInfoParam);
        presenter.attachView(orderInfoView);
    }

    private ClientBaseView<Object> orderInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(OrderDetailsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                orderInfo = (OrderInfoResult) GsonUtil.parseJson(baseBean.getData(), OrderInfoResult.class);
                initViewData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "orderInfoView==" + result);
        }
    };

    /**
     * 加载页面数据
     */
    private void initViewData() {
        phone = orderInfo.getWorker_info().getMobile();
        tvOrderNumber.setText(orderInfo.getNumber());
        tvOrderState.setText(orderInfo.getState_info());
        tvOrderDate.setText(orderInfo.getCreate_date());
        tvOrderWTitle.setText(orderInfo.getTitle());
        tvOrderSendTime.setText(HelperUtil.StringDateToSingle(orderInfo.getPush_date() + " "));
        if (orderInfo.getRequire_type() == 1) {
            tvOrderWorkerType.setText("点工");
            llOrderDayPrice.setVisibility(View.VISIBLE);
            llOrderOnDoorPrice.setVisibility(View.GONE);
            tvOrderDayPrice.setText(orderInfo.getPrice());
            price = orderInfo.getPrice();
            switch (orderInfo.getState()) {
                case 201://施工中
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("辞退");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    //辞退
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissConfirm(number);
                        }
                    });
                    break;
                case 202://"已完成",//待雇主确认
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.GONE);
                    tvLeftButton.setVisibility(View.VISIBLE);
                    tvRightButton.setVisibility(View.VISIBLE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    tvLeftButton.setText("辞退");
                    tvRightButton.setText("确认完成");
                    //辞退
                    tvLeftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissConfirm(number);
                        }
                    });
                    //确认完成
                    tvRightButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
//                            startActivity(intent);
                            orderConfirm();
//                            payConfirm(number);
                        }
                    });
                    break;
                case 203://待支付尾款---------------------
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("立即支付");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    price = orderInfo.getPrice();
                    //立即支付
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payConfirm(number);
                        }
                    });
                    break;
                case 205://完成已评价
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("再次下单");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    //再次下单
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
                            intent.putExtra("workerId", orderInfo.getWorker_id());
                            startActivity(intent);
                        }
                    });
                    break;
                case 204://完成待评价
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("评价");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    //立即支付
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, OrderEvaluationActivity.class);
                            intent.putExtra("orderNumber", number);
                            startActivity(intent);
                        }
                    });
                    break;
                case 221://"待付款", //辞退待雇主付款------------------------
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("立即支付");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.VISIBLE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    //立即支付
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payConfirm(number);
                        }
                    });
                    break;
                case 220://辞退中
                    llBottomSelect.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.VISIBLE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    break;
                case 222://已辞退
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("再次下单");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.VISIBLE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    //再次下单
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
                            intent.putExtra("workerId", orderInfo.getWorker_id());
                            startActivity(intent);
                        }
                    });
                    break;
            }
        } else {
            tvOrderWorkerType.setText("包工");
            llOrderDayPrice.setVisibility(View.GONE);
            llOrderOnDoorPrice.setVisibility(View.VISIBLE);
            tvOrderOnDoorPrice.setText(orderInfo.getGoto_price());
            price = orderInfo.getGoto_price();
            switch (orderInfo.getState()) {
                case 100://待支付上门费
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.GONE);
                    tvLeftButton.setVisibility(View.VISIBLE);
                    tvRightButton.setVisibility(View.VISIBLE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    tvLeftButton.setText("取消订单");
                    tvRightButton.setText("立即支付");
                    //取消订单
                    tvLeftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, OrderCancelActivity.class);
                            intent.putExtra("orderNumber", number);
                            startActivity(intent);
                        }
                    });
                    //立即支付
                    tvRightButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            price = orderInfo.getTotal_money();
//                            payConfirm(number);
                            intent = new Intent(OrderDetailsActivity.this, PayConfirmActivity.class);
                            intent.putExtra("number", number);
                            intent.putExtra("price", price);
                            intent.putExtra("name", orderInfo.getTitle());
                            intent.putExtra("detail", orderInfo.getDescription());
                            startActivity(intent);
                        }
                    });
                    break;
                case 101://估价中
                    llBottomSelect.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    break;
                case 102://待支付定金-------------------
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.GONE);
                    tvLeftButton.setVisibility(View.VISIBLE);
                    tvRightButton.setVisibility(View.VISIBLE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    llOrderPrice.setVisibility(View.VISIBLE);
                    llOrderDingjin.setVisibility(View.VISIBLE);
                    tvOrderPrice.setText(orderInfo.getTotal_money());
                    tvOrderDingjin.setText(orderInfo.getFront_money());

                    tvLeftButton.setText("取消订单");
                    tvRightButton.setText("立即支付");
                    price = orderInfo.getFront_money();
                    //取消订单
                    tvLeftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, OrderCancelActivity.class);
                            intent.putExtra("orderNumber", number);
                            startActivity(intent);
                        }
                    });
                    //立即支付
                    tvRightButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payConfirm(number);
                        }
                    });
                    break;
                case 103://施工中
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("辞退");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    //辞退
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissConfirm(number);
                        }
                    });
                    break;
                case 104://"已完成",//待雇主确认
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.GONE);
                    tvLeftButton.setVisibility(View.VISIBLE);
                    tvRightButton.setVisibility(View.VISIBLE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    tvLeftButton.setText("辞退");
                    tvRightButton.setText("确认完成");
                    //辞退
                    tvLeftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissConfirm(number);
                        }
                    });
                    //确认完成
                    tvRightButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
//                            startActivity(intent);
                            orderConfirm();
                        }
                    });
                    break;
                case 105://待支付尾款
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("立即支付");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    price = String.valueOf(Double.parseDouble(orderInfo.getTotal_money()) - Double.parseDouble(orderInfo.getFront_money()));
                    //立即支付
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            payConfirm(number);
                            intent = new Intent(OrderDetailsActivity.this, PayConfirmActivity.class);
                            intent.putExtra("number", number);
                            intent.putExtra("price", price);
                            intent.putExtra("name", orderInfo.getTitle());
                            intent.putExtra("detail", orderInfo.getDescription());
                            startActivity(intent);
                        }
                    });
                    break;
                case 106://完成待评价
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("评价");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    //立即支付
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, OrderEvaluationActivity.class);
                            intent.putExtra("orderNumber", number);
                            startActivity(intent);
                        }
                    });
                    break;
                case 107://完成已评价
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("再次下单");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.VISIBLE);
                    llOrderDismiss.setVisibility(View.GONE);
                    //再次下单
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
                            intent.putExtra("workerId", orderInfo.getWorker_id());
                            startActivity(intent);
                        }
                    });
                    break;
                case 120://"已取消", //待付上门费时取消
                case 121://"已取消", //待支付定金前被取消
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("再次下单");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.GONE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    //再次下单
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
                            intent.putExtra("workerId", orderInfo.getWorker_id());
                            startActivity(intent);
                        }
                    });
                    break;
                case 124://已辞退
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("再次下单");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.VISIBLE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    //再次下单
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(OrderDetailsActivity.this, PlaceOrderActivity.class);
                            intent.putExtra("workerId", orderInfo.getWorker_id());
                            startActivity(intent);
                        }
                    });
                    break;
                case 122://辞退中
                    llBottomSelect.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.VISIBLE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    break;
                case 123://"待付款", //辞退待雇主付款
                    llBottomSelect.setVisibility(View.VISIBLE);
                    tvOrderAgain.setVisibility(View.VISIBLE);
                    tvOrderAgain.setText("立即支付");
                    tvLeftButton.setVisibility(View.GONE);
                    tvRightButton.setVisibility(View.GONE);
                    llOrderEvaluatedInfo.setVisibility(View.GONE);
                    llOrderDismiss.setVisibility(View.VISIBLE);
                    tvOrderDismissInfo.setText("辞退原因");
                    tvOrderDismiss.setText(orderInfo.getDismiss_reason());
                    price = orderInfo.getDismiss_money();
                    //立即支付
                    tvOrderAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            payConfirm(number);
                            intent = new Intent(OrderDetailsActivity.this, PayConfirmActivity.class);
                            intent.putExtra("number", number);
                            intent.putExtra("price", price);
                            intent.putExtra("name", orderInfo.getTitle());
                            intent.putExtra("detail", orderInfo.getDescription());
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
        tvOrderNeedType.setText(orderInfo.getWorker_types());
        tvOrderWorkArea.setText(orderInfo.getAddress());
        tvOrderWorkTime.setText(HelperUtil.StringDateToSingle(orderInfo.getBegin_date() + " ") + "--" + HelperUtil.StringDateToSingle(orderInfo.getEnd_date() + " "));
        tvOrderDescription.setText(orderInfo.getDescription());
        tvOrderUserName.setText(orderInfo.getContacts());
        tvOrderUserPhone.setText(orderInfo.getContact_number());
        Glide.with(OrderDetailsActivity.this)
                .asBitmap()
                .load(orderInfo.getWorker_info().getHead_image_path())
                .into(new BitmapImageViewTarget(ivOrderWorkerPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(OrderDetailsActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivOrderWorkerPhoto.setImageDrawable(circularBitmapDrawable);
                    }
                });
        tvOrderWorkerName.setText(orderInfo.getWorker_info().getName());
        tvOrderWorkerAge.setText(String.valueOf(orderInfo.getWorker_info().getAge()));
        tvOrderWorkerYears.setText(String.valueOf(orderInfo.getWorker_info().getWork_age()));
        tvOrderWorkerTypes.setText(orderInfo.getWorker_info().getWorker_type_name());
        tvOrderWorkerGoodAt.setText(orderInfo.getWorker_info().getStrength());
        tvOrderWorkerPhoneNum.setText(orderInfo.getWorker_info().getMobile());
        llOrderWorkerStars.removeAllViews();
        for (int i = 0; i < orderInfo.getWorker_info().getStar_level(); i++) {
            ImageView star = new ImageView(OrderDetailsActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(6, 0, 0, 0);
            star.setLayoutParams(lp);
            star.setImageResource(R.mipmap.star);
            llOrderWorkerStars.addView(star);
        }

    }

    /**
     * 确认完成
     */
    private void orderConfirm() {
        mSVProgressHUD.showWithStatus("加载中...");
        OrderInfoParam orderInfo = new OrderInfoParam(
                number
        );
        presenter = new PresenterImpl<Object>(OrderDetailsActivity.this);
        presenter.onCreate();
        presenter.orderconfirm(orderInfo);
        presenter.attachView(orderConfirmView);
    }

    private ClientBaseView<Object> orderConfirmView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(OrderDetailsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
//                payConfirm(number);
                orderInfo();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "orderConfirmView==" + result);
        }
    };

    /**
     * 显示辞退协议弹窗
     */
    private void dismissConfirm(final String num) {
        dialog = new Dialog(OrderDetailsActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_order_dismiss, null);
        tvDismissOrderDismiss = (TextView) layout.findViewById(R.id.tvDismissOrderDismiss);
        tvDetermineOrderDismiss = (TextView) layout.findViewById(R.id.tvDetermineOrderDismiss);
        tvName = (TextView) layout.findViewById(R.id.tvName);
        tvName.setText(orderInfo.getWorker_info().getName());
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(OrderDetailsActivity.this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvDismissOrderDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //确认
        tvDetermineOrderDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                intent = new Intent(OrderDetailsActivity.this, DismissReasonActivity.class);
                intent.putExtra("orderNum", num);
                startActivity(intent);
            }
        });
    }

    /**
     * 显示支付确认
     */
    private void payConfirm(final String num) {
        dialog = new Dialog(OrderDetailsActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_order_protocol, null);
        tvDismissOrderProtocol = (TextView) layout.findViewById(R.id.tvDismissOrderProtocol);
        tvDetermineOrderProtocol = (TextView) layout.findViewById(R.id.tvDetermineOrderProtocol);
        tvProName = (TextView) layout.findViewById(R.id.tvProName);
        tvThirdPact = (TextView) layout.findViewById(R.id.tvThirdPact);
        tvThirdPact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent intent = new Intent(OrderDetailsActivity.this, TermsServiceContentActivity.class);
                intent.putExtra("activity", "third");
                startActivity(intent);
            }
        });
        dialog.setContentView(layout);
        dialog.show();
        tvProName.setText(orderInfo.getWorker_info().getName());
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(OrderDetailsActivity.this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvDismissOrderProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //确认
        tvDetermineOrderProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                intent = new Intent(OrderDetailsActivity.this, PayConfirmActivity.class);
                intent.putExtra("number", num);
                intent.putExtra("price", price);
                intent.putExtra("name", orderInfo.getTitle());
                intent.putExtra("detail", orderInfo.getDescription());
                startActivity(intent);
            }
        });
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatOrderDetail() {
        if (jp != null) {
            intent = new Intent(OrderDetailsActivity.this, EmployerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            OrderDetailsActivity.this.finish();
        }
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
                if (jp != null) {
                    intent = new Intent(OrderDetailsActivity.this, EmployerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    OrderDetailsActivity.this.finish();
                }
                bl = true;
            }
        }
        return bl;
    }
}
