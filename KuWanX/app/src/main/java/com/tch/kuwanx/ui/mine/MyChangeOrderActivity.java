package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.SwapOrderResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.MyEasyIndicator;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 我的换物订单
 */
public class MyChangeOrderActivity extends BaseActivity implements MyEasyIndicator.onTabClickListener, OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.indicatorMyChangeOrder)
    MyEasyIndicator indicatorMyChangeOrder;
    @BindView(R.id.scrollMyChangeOrder)
    HorizontalScrollView scrollMyChangeOrder;
    @BindView(R.id.rvMyChangeOrder)
    RecyclerView rvMyChangeOrder;
    @BindView(R.id.refreshMyChangeOrder)
    SmartRefreshLayout refreshMyChangeOrder;

    private int waitType, orderStatus = 0;
    private String[] mTitles = {"全部", "待支付", "待发货", "待收货", "待还货", "待对方收货", "待评价"};
    private CommonAdapter myChangeOrderAdapter;
    private boolean isMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("我的换物订单");
        waitType = getIntent().getIntExtra("waitType", 0);
        switch (waitType) {
            case 1:
                orderStatus = 10;
                break;
            case 2:
                orderStatus = 20;
                break;
            case 3:
                orderStatus = 30;
                break;
            case 4:
                orderStatus = 40;
                break;
            case 5:
                orderStatus = 45;
                break;
            case 6:
                orderStatus = 50;
                break;
        }
        setIndicatorTab();
        initMyChangeOrder();
        refreshMyChangeOrder.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSwapOrderHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        switch (waitType) {
            case 0:
                orderStatus = 0;
                break;
            case 1:
                orderStatus = 10;
                break;
            case 2:
                orderStatus = 20;
                break;
            case 3:
                orderStatus = 30;
                break;
            case 4:
                orderStatus = 40;
                break;
            case 5:
                orderStatus = 45;
                break;
            case 6:
                orderStatus = 50;
                break;
        }
        getSwapOrderHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        switch (waitType) {
            case 0:
                orderStatus = 0;
                break;
            case 1:
                orderStatus = 10;
                break;
            case 2:
                orderStatus = 20;
                break;
            case 3:
                orderStatus = 30;
                break;
            case 4:
                orderStatus = 40;
                break;
            case 5:
                orderStatus = 45;
                break;
            case 6:
                orderStatus = 50;
                break;
        }
        getSwapOrderHttp();
    }

    private void initMyChangeOrder() {
        rvMyChangeOrder.setLayoutManager(new LinearLayoutManager(this));
        rvMyChangeOrder.setAdapter(myChangeOrderAdapter = new CommonAdapter<SwapOrderResult.ResultBean>(this,
                R.layout.item_my_change_order, new ArrayList<SwapOrderResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, final SwapOrderResult.ResultBean item, int position) {
                switch (item.getType()) {
                    case "1":
                        holder.setText(R.id.tvChangeOrderItemType, "X-BOX");
                        break;
                    case "2":
                        holder.setText(R.id.tvChangeOrderItemType, "PS4");
                        break;
                    case "3":
                        holder.setText(R.id.tvChangeOrderItemType, "SWITCH");
                        break;
                }

                switch (item.getOrder_status()) {
                    case "10":
                        holder.setText(R.id.tvChangeOrderItemState, "待支付");
                        holder.setVisible(R.id.btChangeOrderItemPay, true);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        holder.setText(R.id.btChangeOrderItemPay, "立即付款");
                        holder.setOnClickListener(R.id.btChangeOrderItemPay, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MyChangeOrderActivity.this, PostCommentsActivity.class);
                                intent.putExtra("orderId",item.getCdid());
                                intent.putExtra("postUserId",item.getPost_user_id());
                                intent.putExtra("swapUserId",item.getSwap_user_id());
                                intent.putExtra("cdImg",item.getHeadpic());
                                startActivity(intent);
                            }
                        });
                        break;
                    case "20":
                        holder.setText(R.id.tvChangeOrderItemState, "待发货");
                        holder.setVisible(R.id.btChangeOrderItemPay, true);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        holder.setText(R.id.btChangeOrderItemPay, "已发货");
                        break;
                    case "30":
                        holder.setText(R.id.tvChangeOrderItemState, "待收货");
                        holder.setVisible(R.id.btChangeOrderItemPay, true);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        holder.setText(R.id.btChangeOrderItemPay, "已收货");
                        break;
                    case "40":
                        holder.setText(R.id.tvChangeOrderItemState, "待还货");
                        holder.setVisible(R.id.btChangeOrderItemPay, true);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        holder.setText(R.id.btChangeOrderItemPay, "已还货");
                        break;
                    case "45":
                        holder.setText(R.id.tvChangeOrderItemState, "待收换货");
                        holder.setVisible(R.id.btChangeOrderItemPay, true);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        holder.setText(R.id.btChangeOrderItemPay, "已收货");
                        break;
                    case "50":
                        holder.setText(R.id.tvChangeOrderItemState, "待评价");
                        holder.setVisible(R.id.btChangeOrderItemPay, true);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        holder.setText(R.id.btChangeOrderItemPay, "立即评价");
                        holder.setOnClickListener(R.id.btChangeOrderItemPay, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MyChangeOrderActivity.this, PostCommentsActivity.class);
                                intent.putExtra("orderId",item.getCdid());
                                intent.putExtra("postUserId",item.getPost_user_id());
                                intent.putExtra("swapUserId",item.getSwap_user_id());
                                startActivity(intent);
                            }
                        });
                        break;
                    case "60":
                        holder.setText(R.id.tvChangeOrderItemState, "已完成");
                        holder.setVisible(R.id.btChangeOrderItemPay, false);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        break;
                    case "-1":
                        holder.setText(R.id.tvChangeOrderItemState, "无效订单");
                        holder.setVisible(R.id.btChangeOrderItemPay, false);
                        holder.setVisible(R.id.btChangeOrderItemDelete, false);
                        break;
                }

                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(MyChangeOrderActivity.this)
                            .load(item.getHeadpic())
                            .into((ImageView) holder.getView(R.id.ivChangeOrderItemImg));
                } else {
                    holder.setImageResource(R.id.ivChangeOrderItemImg, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvChangeOrderItemName, item.getName());
                holder.setText(R.id.tvChangeOrderItemPrice, "￥" + item.getPay_deposit());
            }
        });
        myChangeOrderAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 加载标题栏
     */
    private void setIndicatorTab() {
        indicatorMyChangeOrder.setTabTitles(mTitles, waitType, scrollMyChangeOrder);
        indicatorMyChangeOrder.setOnTabClickListener(this);
    }

    @Override
    public void onTabClick(String s, int i) {
        waitType = i;
        switch (i) {
            case 0:
                orderStatus = 0;
                break;
            case 1:
                orderStatus = 10;
                break;
            case 2:
                orderStatus = 20;
                break;
            case 3:
                orderStatus = 30;
                break;
            case 4:
                orderStatus = 40;
                break;
            case 5:
                orderStatus = 45;
                break;
            case 6:
                orderStatus = 50;
                break;
        }
        getSwapOrderHttp();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void myChangeOrderBack() {
        MyChangeOrderActivity.this.finish();
    }

    /**
     * 换物订单
     */
    private void getSwapOrderHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(MyChangeOrderActivity.this));
        if (orderStatus != 0) {
            map.put("order_status", String.valueOf(orderStatus));
        }
        String params = EncryptionUtil.getParameter(MyChangeOrderActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/getSwapOrder.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getSwapOrder")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        MyChangeOrderActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshMyChangeOrder != null) {
                            refreshMyChangeOrder.finishLoadmore();
                            refreshMyChangeOrder.finishRefresh();
                        }
                        Toasty.warning(MyChangeOrderActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshMyChangeOrder != null) {
                            refreshMyChangeOrder.finishLoadmore();
                            refreshMyChangeOrder.finishRefresh();
                        }

                        SwapOrderResult swapOrderResult = (SwapOrderResult) GsonUtil.json2Object(response, SwapOrderResult.class);
                        if (swapOrderResult != null
                                && swapOrderResult.getRet().equals("1")) {
                            if (isMore) {

                            } else {
                                myChangeOrderAdapter.getDatas().clear();
                                myChangeOrderAdapter.getDatas().addAll(swapOrderResult.getResult());
                                myChangeOrderAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(MyChangeOrderActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
