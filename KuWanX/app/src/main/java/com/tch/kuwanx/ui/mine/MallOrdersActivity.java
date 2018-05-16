package com.tch.kuwanx.ui.mine;

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
import com.tch.kuwanx.result.DeleteOrderResult;
import com.tch.kuwanx.result.GoodOrderResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.MyNewEasyIndicator;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 我的商城订单
 */
public class MallOrdersActivity extends BaseActivity implements MyNewEasyIndicator.onTabClickListener, OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.scrollMall)
    HorizontalScrollView scrollMall;
    @BindView(R.id.indicatorMall)
    MyNewEasyIndicator indicatorMall;
    @BindView(R.id.rvMall)
    RecyclerView rvMall;
    @BindView(R.id.refreshMallOrders)
    SmartRefreshLayout refreshMallOrders;

    private int storeType;
    private String[] mTitles = {"全部", "待支付", "待发货", "待收货", "待评价"};
    private CommonAdapter mallAdapter;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_orders);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("我的商城订单");
        storeType = getIntent().getIntExtra("storeType", 0);
        setIndicatorTab();
        initMyChangeOrder();
        refreshMallOrders.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (storeType) {
            case 0:
                myGoodOrderHttp("a");
                break;
            case 1:
                myGoodOrderHttp("b");
                break;
            case 2:
                myGoodOrderHttp("c");
                break;
            case 3:
                myGoodOrderHttp("d");
                break;
            case 4:
                myGoodOrderHttp("e");
                break;
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        switch (storeType) {
            case 0:
                myGoodOrderHttp("a");
                break;
            case 1:
                myGoodOrderHttp("b");
                break;
            case 2:
                myGoodOrderHttp("c");
                break;
            case 3:
                myGoodOrderHttp("d");
                break;
            case 4:
                myGoodOrderHttp("e");
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        switch (storeType) {
            case 0:
                myGoodOrderHttp("a");
                break;
            case 1:
                myGoodOrderHttp("b");
                break;
            case 2:
                myGoodOrderHttp("c");
                break;
            case 3:
                myGoodOrderHttp("d");
                break;
            case 4:
                myGoodOrderHttp("e");
                break;
        }
    }

    private void initMyChangeOrder() {
        rvMall.setLayoutManager(new LinearLayoutManager(this));
        rvMall.setAdapter(mallAdapter = new CommonAdapter<GoodOrderResult.ResultBean>(this,
                R.layout.item_mall_order, new ArrayList<GoodOrderResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, final GoodOrderResult.ResultBean item, int position) {
                RecyclerView view = (RecyclerView) holder.getView(R.id.rvResItem);
                initMyChangeOrderRes(view, item.getGoodList());

                holder.setText(R.id.tvMallOrderType, "酷玩商城");
                switch (item.getOrder_status()) {
                    case "10":
                        holder.setText(R.id.tvMallState, "待支付");
                        holder.setVisible(R.id.btMallOrderPay, true);
                        holder.setVisible(R.id.btMallOrderDelete, true);
                        holder.setVisible(R.id.btMallOrderHad, false);
                        holder.setVisible(R.id.btMallOrderReview, false);
                        break;
                    case "20":
                        holder.setText(R.id.tvMallState, "待发货");
                        holder.setVisible(R.id.btMallOrderPay, false);
                        holder.setVisible(R.id.btMallOrderDelete, false);
                        holder.setVisible(R.id.btMallOrderHad, false);
                        holder.setVisible(R.id.btMallOrderReview, false);
                        break;
                    case "30":
                        holder.setText(R.id.tvMallState, "待收货");
                        holder.setVisible(R.id.btMallOrderPay, false);
                        holder.setVisible(R.id.btMallOrderDelete, false);
                        holder.setVisible(R.id.btMallOrderHad, true);
                        holder.setVisible(R.id.btMallOrderReview, false);
                        break;
                    case "40":
                        holder.setText(R.id.tvMallState, "待评价");
                        holder.setVisible(R.id.btMallOrderReview, true);
                        holder.setVisible(R.id.btMallOrderPay, false);
                        holder.setVisible(R.id.btMallOrderDelete, false);
                        holder.setVisible(R.id.btMallOrderHad, false);
                        break;
                    case "50":
                        holder.setText(R.id.tvMallState, "已评价");
                        holder.setVisible(R.id.btMallOrderDelete, true);
                        holder.setVisible(R.id.btMallOrderReview, false);
                        holder.setVisible(R.id.btMallOrderPay, false);
                        holder.setVisible(R.id.btMallOrderHad, false);
                        break;
                    case "-1":
                        holder.setText(R.id.tvMallState, "已删除");
                        holder.setVisible(R.id.btMallOrderDelete, false);
                        holder.setVisible(R.id.btMallOrderReview, false);
                        holder.setVisible(R.id.btMallOrderPay, false);
                        holder.setVisible(R.id.btMallOrderHad, false);
                        break;
                }

                int size = 0;
                for (GoodOrderResult.ResultBean.GoodListBean gg : item.getGoodList()) {
                    size += Integer.parseInt(gg.getGood_count());
                }

                holder.setText(R.id.tvMallOrderNumber, String.valueOf(size));
                holder.setText(R.id.tvMallOrderPrice, item.getOrder_amt());


                //删除订单
                holder.setOnClickListener(R.id.btMallOrderDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteOrderHttp(item.getId());
                    }
                });
            }
        });
    }

    private CommonAdapter orderResAdapter;

    private void initMyChangeOrderRes(RecyclerView view, List<GoodOrderResult.ResultBean.GoodListBean> list) {
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(orderResAdapter = new CommonAdapter<GoodOrderResult.ResultBean.GoodListBean>(this,
                R.layout.item_mall_order_res, new ArrayList<GoodOrderResult.ResultBean.GoodListBean>()) {
            @Override
            protected void convert(ViewHolder holder, final GoodOrderResult.ResultBean.GoodListBean item, int position) {
                if (!TextUtils.isEmpty(item.getGood_cover())) {
                    Glide.with(MallOrdersActivity.this)
                            .load(item.getGood_cover())
                            .into((ImageView) holder.getView(R.id.ivMallPhoto));
                } else {
                    holder.setImageResource(R.id.ivMallPhoto, R.drawable.placeholder);
                }

                holder.setText(R.id.tvMallOrderItemName, item.getGood_name());
                holder.setText(R.id.tvMallOrderItemCost, "￥" + item.getCost_price());
                holder.setText(R.id.tvMallOrderItemCurrent, "￥" + item.getCurrent_price());
                holder.setText(R.id.tvMallOrderItemNum, "X" + item.getGood_count());
            }
        });
        orderResAdapter.getDatas().clear();
        orderResAdapter.getDatas().addAll(list);
        orderResAdapter.notifyDataSetChanged();
    }

    /**
     * 加载标题栏
     */
    private void setIndicatorTab() {
        indicatorMall.setTabTitles(mTitles, storeType, scrollMall);
        indicatorMall.setOnTabClickListener(this);
    }

    @Override
    public void onTabClick(String s, int i) {
        if (mallAdapter.getDatas() != null) {
            mallAdapter.getDatas().clear();
            mallAdapter.notifyDataSetChanged();
        }
        index = 1;
        switch (i) {
            case 0:
                storeType = 0;
                myGoodOrderHttp("a");
                break;
            case 1:
                storeType = 1;
                myGoodOrderHttp("b");
                break;
            case 2:
                storeType = 2;
                myGoodOrderHttp("c");
                break;
            case 3:
                storeType = 3;
                myGoodOrderHttp("d");
                break;
            case 4:
                storeType = 4;
                myGoodOrderHttp("e");
                break;
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void ibTitleBack() {
        MallOrdersActivity.this.finish();
    }

    /**
     * 商城订单列表
     */
    private void myGoodOrderHttp(String type) {
        Map<String, Object> map = new HashMap<>();
//        map.put("appuser_id", DaoUtils.getUserId(MallOrdersActivity.this));
        map.put("appuser_id", "be8c68e7aa754021b3b03c4bdca80e78");
        map.put("type", type);
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(MallOrdersActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/myGoodOrder.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_myGoodOrder")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        MallOrdersActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshMallOrders != null) {
                            refreshMallOrders.finishLoadmore();
                            refreshMallOrders.finishRefresh();
                        }
                        Toasty.warning(MallOrdersActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshMallOrders != null) {
                            refreshMallOrders.finishLoadmore();
                            refreshMallOrders.finishRefresh();
                        }

                        GoodOrderResult goodOrderResult =
                                (GoodOrderResult) GsonUtil.json2Object(response, GoodOrderResult.class);
                        if (goodOrderResult != null
                                && goodOrderResult.getRet().equals("1")) {
                            if (isMore) {
                                mallAdapter.getDatas().addAll(goodOrderResult.getResult());
                                mallAdapter.notifyDataSetChanged();
                            } else {
                                mallAdapter.getDatas().clear();
                                mallAdapter.getDatas().addAll(goodOrderResult.getResult());
                                mallAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(MallOrdersActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 删除订单
     */
    private void deleteOrderHttp(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);
        String params = EncryptionUtil.getParameter(MallOrdersActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/deleteOrder.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_deleteOrder")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        MallOrdersActivity.this, "删除中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(MallOrdersActivity.this, "删除失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        DeleteOrderResult deleteOrderResult =
                                (DeleteOrderResult) GsonUtil.json2Object(response, DeleteOrderResult.class);
                        if (deleteOrderResult != null
                                && deleteOrderResult.getRet().equals("1")) {
                            switch (storeType) {
                                case 0:
                                    myGoodOrderHttp("a");
                                    break;
                                case 1:
                                    myGoodOrderHttp("b");
                                    break;
                                case 2:
                                    myGoodOrderHttp("c");
                                    break;
                                case 3:
                                    myGoodOrderHttp("d");
                                    break;
                                case 4:
                                    myGoodOrderHttp("e");
                                    break;
                            }
                        }
                    }
                });
    }

}
