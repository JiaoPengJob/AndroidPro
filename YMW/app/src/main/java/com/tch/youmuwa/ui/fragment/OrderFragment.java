package com.tch.youmuwa.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.OrderInfoParam;
import com.tch.youmuwa.bean.parameters.OrdersListParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.bean.result.OrdersListResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.employer.DismissReasonActivity;
import com.tch.youmuwa.ui.activity.employer.MessageCenterActivity;
import com.tch.youmuwa.ui.activity.employer.OrderCancelActivity;
import com.tch.youmuwa.ui.activity.employer.OrderDetailsActivity;
import com.tch.youmuwa.ui.activity.employer.OrderEvaluationActivity;
import com.tch.youmuwa.ui.activity.employer.PayConfirmActivity;
import com.tch.youmuwa.ui.activity.employer.PlaceOrderActivity;
import com.tch.youmuwa.ui.activity.login.TermsServiceContentActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 订单
 */

public class OrderFragment extends ViewPagerFragment implements FragmentBackHandler, MessageInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.ivOrderAllIcon)
    ImageView ivOrderAllIcon;
    @BindView(R.id.tvOrderAll)
    TextView tvOrderAll;
    @BindView(R.id.ivOrderAll)
    ImageView ivOrderAll;
    @BindView(R.id.ivPendingPaymentIcon)
    ImageView ivPendingPaymentIcon;
    @BindView(R.id.tvPendingPayment)
    TextView tvPendingPayment;
    @BindView(R.id.ivPendingPayment)
    ImageView ivPendingPayment;
    @BindView(R.id.ivOrderWhole)
    ImageView ivOrderWhole;
    @BindView(R.id.tvOrderWhole)
    TextView tvOrderWhole;
    @BindView(R.id.ivOrderWholeIcon)
    ImageView ivOrderWholeIcon;
    @BindView(R.id.ivOrderProcessingIcon)
    ImageView ivOrderProcessingIcon;
    @BindView(R.id.tvOrderProcessing)
    TextView tvOrderProcessing;
    @BindView(R.id.ivOrderProcessing)
    ImageView ivOrderProcessing;
    @BindView(R.id.refreshOrderCenter)
    SmartRefreshLayout refreshOrderCenter;
    @BindView(R.id.rvOrderCenter)
    RecyclerView rvOrderCenter;
    @BindView(R.id.tvDMsgCount)
    TextView tvDMsgCount;
    @BindView(R.id.llOrderNet)
    LinearLayout llOrderNet;

    private Intent intent;
    private CommonAdapter adapter;
    private PresenterImpl<Object> presenter;
    private int index = 0;
    private int type = 0;
    private OrdersListResult ordersList;
    private List<OrdersListResult.MsgListBean> list;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private Dialog dialog;
    private TextView tvDismissOrderDismiss, tvDetermineOrderDismiss, tvDismissOrderProtocol, tvDetermineOrderProtocol, tvName, tvProName, tvThirdPact;
    private String number, price, name, detail = "";
    private List<MsgResult.MsgListBean> msgList;
    private boolean isLoadMore = false;
    private String nameDialog;
    private MsgResult msgR;

    public static OrderFragment newInstance(String param) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("size", param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            initView();
        } else {
            list = null;
            index = 0;
            type = 0;
            isLoadMore = false;
            list.clear();
            list = null;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                adapter = null;
            }
            if (presenter != null) {
                presenter.onStop();
                presenter = null;
            }
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        MessageService.setMi(this);
        type = 0;
        list = new ArrayList<OrdersListResult.MsgListBean>();
        msgList = new ArrayList<MsgResult.MsgListBean>();
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        mSVProgressHUD.getOutAnimation();
        refreshOrderCenter.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                list.clear();
                netShow();
            }
        });
        refreshOrderCenter.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                if (ordersList != null && ordersList.getCan_msg_more() != 0) {
                    index++;
                    netShow();
                } else {
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
//        netShow();
    }

    @Override
    public void getMsg(MsgResult msg) {
        if (msg != null) {
            msgR = msg;
            handler.sendEmptyMessage(0);
        }
    }

    private void netShow() {
        if (HelperUtil.isNetworkConnected(getContext())) {
            llOrderNet.setVisibility(View.GONE);
            refreshOrderCenter.setVisibility(View.VISIBLE);
            clientOrdersList();
        } else {
            llOrderNet.setVisibility(View.VISIBLE);
            refreshOrderCenter.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新
     */
    @OnClick(R.id.btRefreshNet)
    public void refreshNet() {
        netShow();
    }

    /**
     * 获取列表信息
     */
    private void clientOrdersList() {
        mSVProgressHUD.showWithStatus("加载中...");
        OrdersListParam ordersListParam = new OrdersListParam(
                index,
                10,
                String.valueOf(type)
        );
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getorderslist(ordersListParam);
        presenter.attachView(ordersListView);
    }

    private ClientBaseView<Object> ordersListView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshOrderCenter.isRefreshing()) {
                refreshOrderCenter.finishRefresh();
            }
            if (refreshOrderCenter.isLoading()) {
                refreshOrderCenter.finishLoadmore();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
                ivOrderWholeIcon.setVisibility(View.GONE);
                ivOrderAll.setVisibility(View.GONE);
                ivPendingPayment.setVisibility(View.GONE);
                ivOrderProcessing.setVisibility(View.GONE);
            } else {
                ordersList = (OrdersListResult) GsonUtil.parseJson(baseBean.getData(), OrdersListResult.class);
                if (ordersList.getMsg_list().size() == 0) {
                    ivOrderWholeIcon.setVisibility(View.GONE);
                    ivOrderAll.setVisibility(View.GONE);
                    ivPendingPayment.setVisibility(View.GONE);
                    ivOrderProcessing.setVisibility(View.GONE);
                }
                if (isLoadMore) {
                    for (OrdersListResult.MsgListBean mb : ordersList.getMsg_list()) {
                        list.add(mb);
                    }
                } else {
                    list = ordersList.getMsg_list();
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                initListData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshOrderCenter.isRefreshing()) {
                refreshOrderCenter.finishRefresh();
            }
            if (refreshOrderCenter.isLoading()) {
                refreshOrderCenter.finishLoadmore();
            }
            Log.e("Error", "ordersListView==" + result);
        }
    };

    /**
     * 加载列表信息
     */
    private void initListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvOrderCenter.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<OrdersListResult.MsgListBean>(getContext(), R.layout.item_order_fragment, list) {
            @Override
            protected void convert(ViewHolder holder, OrdersListResult.MsgListBean order, final int position) {
                holder.setText(R.id.tvOrderNumber, order.getNumber());
                holder.setText(R.id.tvOrderTime, HelperUtil.StringDateToSingle(order.getBegin_date() + " ") + "--" + HelperUtil.StringDateToSingle(order.getEnd_date() + " "));
                holder.setText(R.id.tvOrderTitle, order.getTitle());
                holder.setText(R.id.tvStateInfo, order.getState_info());
                holder.setText(R.id.tvOrderPrice, order.getPrice());
                holder.setVisible(R.id.tvOrderPriceInfo, false);
                holder.setVisible(R.id.tvOrderPrice, false);
                if (order.getRequire_type() == 1) {
                    holder.setText(R.id.tvOrderTypes, "点工");
                    switch (order.getState()) {
                        case 201://施工中
                            holder.setVisible(R.id.tvOrderAgain, false);
                            holder.setVisible(R.id.tvOrderClear, true);
                            holder.setText(R.id.tvOrderClear, "辞退");
                            //辞退点击事件
                            holder.setOnClickListener(R.id.tvOrderClear, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    dismissConfirm(list.get(position).getNumber());
                                }
                            });
                            break;
                        case 202://"已完成",//待雇主确认
                            holder.setVisible(R.id.tvOrderClear, true);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderClear, "辞退");
                            holder.setText(R.id.tvOrderAgain, "确认完成");
                            //辞退
                            holder.setOnClickListener(R.id.tvOrderClear, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    dismissConfirm(list.get(position).getNumber());
                                }
                            });
                            //确认完成
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    number = list.get(position).getNumber();
                                    price = list.get(position).getPrice();
                                    name = list.get(position).getTitle();
                                    orderConfirm();
//                                    payConfirm(list.get(position).getNumber(), list.get(position).getPrice(), list.get(position).getTitle(), "");
                                }
                            });
                            break;
                        case 203://待支付尾款
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "立即支付");
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    payConfirm(list.get(position).getNumber(), list.get(position).getPrice(), list.get(position).getTitle(), "");
                                }
                            });
                            break;
                        case 204://完成待评价
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "评价");
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent = new Intent(getContext(), OrderEvaluationActivity.class);
                                    intent.putExtra("orderNumber", list.get(position).getNumber());
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 205://完成已评价
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "再次下单");
                            //再次下单
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    number = list.get(position).getNumber();
                                    price = list.get(position).getPrice();
                                    name = list.get(position).getTitle();
                                    intent = new Intent(getContext(), PlaceOrderActivity.class);
                                    intent.putExtra("workerId", list.get(position).getWorker_id());
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 220://辞退中
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, false);
                            break;
                        case 221://"待付款", //辞退待雇主付款
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "立即支付");
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    payConfirm(list.get(position).getNumber(), list.get(position).getPrice(), list.get(position).getTitle(), "");
                                }
                            });
                            break;
                        case 222://已辞退
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "再次下单");
                            //再次下单
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    number = list.get(position).getNumber();
                                    price = list.get(position).getPrice();
                                    name = list.get(position).getTitle();
                                    intent = new Intent(getContext(), PlaceOrderActivity.class);
                                    intent.putExtra("workerId", list.get(position).getWorker_id());
                                    startActivity(intent);
                                }
                            });
                            break;
                    }
                } else {
                    holder.setText(R.id.tvOrderTypes, "包工");
                    switch (order.getState()) {
                        case 100://待支付上门费
                            holder.setVisible(R.id.tvOrderClear, true);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderClear, "取消订单");
                            holder.setText(R.id.tvOrderAgain, "立即支付");
                            //取消订单
                            holder.setOnClickListener(R.id.tvOrderClear, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent = new Intent(getContext(), OrderCancelActivity.class);
                                    intent.putExtra("orderNumber", list.get(position).getNumber());
                                    startActivity(intent);
                                }
                            });
                            //立即支付
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    payConfirm(list.get(position).getNumber(), list.get(position).getPrice(), list.get(position).getTitle(), "");
                                }
                            });
                            break;
                        case 101://估价中
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, false);
                            break;
                        case 102://待支付定金
                            holder.setVisible(R.id.tvOrderClear, true);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderClear, "取消订单");
                            holder.setText(R.id.tvOrderAgain, "立即支付");
                            //取消订单
                            holder.setOnClickListener(R.id.tvOrderClear, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent = new Intent(getContext(), OrderCancelActivity.class);
                                    intent.putExtra("orderNumber", list.get(position).getNumber());
                                    startActivity(intent);
                                }
                            });
                            //立即支付
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    payConfirm(list.get(position).getNumber(), list.get(position).getPrice(), list.get(position).getTitle(), "");
                                }
                            });
                            break;
                        case 103://施工中
                            holder.setVisible(R.id.tvOrderAgain, false);
                            holder.setVisible(R.id.tvOrderClear, true);
                            holder.setText(R.id.tvOrderClear, "辞退");
                            //辞退点击事件
                            holder.setOnClickListener(R.id.tvOrderClear, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    dismissConfirm(list.get(position).getNumber());
                                }
                            });
                            break;
                        case 104://"已完成",//待雇主确认
                            holder.setVisible(R.id.tvOrderClear, true);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderClear, "辞退");
                            holder.setText(R.id.tvOrderAgain, "确认完成");
                            //辞退
                            holder.setOnClickListener(R.id.tvOrderClear, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    dismissConfirm(list.get(position).getNumber());
                                }
                            });
                            //确认完成
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    number = list.get(position).getNumber();
                                    price = list.get(position).getPrice();
                                    name = list.get(position).getTitle();
                                    orderConfirm();
//                                    payConfirm(number, price, name, detail);
                                }
                            });
                            break;
                        case 105://待支付尾款
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "立即支付");
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    payConfirm(list.get(position).getNumber(), list.get(position).getPrice(), list.get(position).getTitle(), "");
                                }
                            });
                            break;
                        case 106://完成待评价
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "评价");
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent = new Intent(getContext(), OrderEvaluationActivity.class);
                                    intent.putExtra("orderNumber", list.get(position).getNumber());
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 107://完成已评价
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "再次下单");
                            //再次下单
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent = new Intent(getContext(), PlaceOrderActivity.class);
                                    intent.putExtra("workerId", list.get(position).getWorker_id());
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 120://"已取消", //待付上门费时取消
                        case 121://"已取消", //待支付定金前被取消
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "再次下单");
                            //再次下单
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent = new Intent(getContext(), PlaceOrderActivity.class);
                                    intent.putExtra("workerId", list.get(position).getWorker_id());
                                    startActivity(intent);
                                }
                            });
                            break;
                        case 122://辞退中
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, false);
                            break;
                        case 123://"待付款", //辞退待雇主付款
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "立即支付");
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nameDialog = list.get(position).getWorker_name();
                                    payConfirm(list.get(position).getNumber(), list.get(position).getPrice(), list.get(position).getTitle(), "");
                                }
                            });
                            break;
                        case 124://已辞退
                            holder.setVisible(R.id.tvOrderClear, false);
                            holder.setVisible(R.id.tvOrderAgain, true);
                            holder.setText(R.id.tvOrderAgain, "再次下单");
                            //再次下单
                            holder.setOnClickListener(R.id.tvOrderAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent = new Intent(getContext(), PlaceOrderActivity.class);
                                    intent.putExtra("workerId", list.get(position).getWorker_id());
                                    startActivity(intent);
                                }
                            });
                            break;
                    }
                }
            }
        };

        if (adapter != null) {
            rvOrderCenter.setAdapter(adapter);
        }

        if (list != null && list.size() >= 10) {
            HelperUtil.moveToPosition(layoutManager, rvOrderCenter, (index + 10));
        }
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(getContext(), OrderDetailsActivity.class);
                intent.putExtra("number", list.get(position).getNumber());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 显示辞退
     */
    private void dismissConfirm(final String num) {
        dialog = new Dialog(getContext(), R.style.dialog);
        //获取自定义布局
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_order_dismiss, null);
        tvDismissOrderDismiss = (TextView) layout.findViewById(R.id.tvDismissOrderDismiss);
        tvDetermineOrderDismiss = (TextView) layout.findViewById(R.id.tvDetermineOrderDismiss);
        tvName = (TextView) layout.findViewById(R.id.tvName);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(getContext()) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        tvName.setText(nameDialog);
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
                intent = new Intent(getContext(), DismissReasonActivity.class);
                intent.putExtra("orderNum", num);
                startActivity(intent);
            }
        });
    }

    /**
     * 显示支付确认
     */
    private void payConfirm(final String number, final String price, final String name, final String detail) {
        dialog = new Dialog(getContext(), R.style.dialog);
        //获取自定义布局
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_order_protocol, null);
        tvDismissOrderProtocol = (TextView) layout.findViewById(R.id.tvDismissOrderProtocol);
        tvDetermineOrderProtocol = (TextView) layout.findViewById(R.id.tvDetermineOrderProtocol);
        tvProName = (TextView) layout.findViewById(R.id.tvProName);
        tvThirdPact = (TextView) layout.findViewById(R.id.tvThirdPact);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(getContext()) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        tvProName.setText(nameDialog);
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
                intent = new Intent(getContext(), PayConfirmActivity.class);
                intent.putExtra("number", number);
                intent.putExtra("price", price);
                intent.putExtra("name", name);
                intent.putExtra("detail", detail);
                startActivity(intent);
            }
        });
        tvThirdPact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent intent = new Intent(getContext(), TermsServiceContentActivity.class);
                intent.putExtra("activity", "third");
                startActivity(intent);
            }
        });
    }

    /**
     * 全部
     */
    @OnClick(R.id.llOrderWhole)
    public void orderWhole() {
        rvOrderCenter.removeAllViews();
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        type = 0;
        netShow();
        ivOrderWhole.setImageResource(R.mipmap.order_all_sel);
        tvOrderWhole.setTextColor(Color.parseColor("#31D09A"));
        ivOrderWholeIcon.setVisibility(View.VISIBLE);

        ivOrderAllIcon.setImageResource(R.mipmap.order_confirmed_not_select);
        tvOrderAll.setTextColor(Color.parseColor("#666666"));
        ivOrderAll.setVisibility(View.GONE);

        ivPendingPaymentIcon.setImageResource(R.mipmap.pending_payment_not_select);
        tvPendingPayment.setTextColor(Color.parseColor("#666666"));
        ivPendingPayment.setVisibility(View.GONE);

        ivOrderProcessingIcon.setImageResource(R.mipmap.order_processing_not_select);
        tvOrderProcessing.setTextColor(Color.parseColor("#666666"));
        ivOrderProcessing.setVisibility(View.GONE);
    }

    /**
     * 已完成
     */
    @OnClick(R.id.llOrderAll)
    public void orderAll() {
        rvOrderCenter.removeAllViews();
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        type = 3;
        netShow();

        ivOrderWhole.setImageResource(R.mipmap.order_all_not_select);
        tvOrderWhole.setTextColor(Color.parseColor("#666666"));
        ivOrderWholeIcon.setVisibility(View.GONE);


        ivOrderAllIcon.setImageResource(R.mipmap.order_confirmed_select);
        tvOrderAll.setTextColor(Color.parseColor("#31D09A"));
        ivOrderAll.setVisibility(View.VISIBLE);

        ivPendingPaymentIcon.setImageResource(R.mipmap.pending_payment_not_select);
        tvPendingPayment.setTextColor(Color.parseColor("#666666"));
        ivPendingPayment.setVisibility(View.GONE);

        ivOrderProcessingIcon.setImageResource(R.mipmap.order_processing_not_select);
        tvOrderProcessing.setTextColor(Color.parseColor("#666666"));
        ivOrderProcessing.setVisibility(View.GONE);
    }

    /**
     * 待付款
     */
    @OnClick(R.id.llPendingPayment)
    public void pendingPayment() {
        rvOrderCenter.removeAllViews();
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        type = 1;
        netShow();

        ivOrderWhole.setImageResource(R.mipmap.order_all_not_select);
        tvOrderWhole.setTextColor(Color.parseColor("#666666"));
        ivOrderWholeIcon.setVisibility(View.GONE);

        ivOrderAllIcon.setImageResource(R.mipmap.order_confirmed_not_select);
        tvOrderAll.setTextColor(Color.parseColor("#666666"));
        ivOrderAll.setVisibility(View.GONE);

        ivPendingPaymentIcon.setImageResource(R.mipmap.pending_payment_select);
        tvPendingPayment.setTextColor(Color.parseColor("#31D09A"));
        ivPendingPayment.setVisibility(View.VISIBLE);

        ivOrderProcessingIcon.setImageResource(R.mipmap.order_processing_not_select);
        tvOrderProcessing.setTextColor(Color.parseColor("#666666"));
        ivOrderProcessing.setVisibility(View.GONE);
    }

    /**
     * 进行中
     */
    @OnClick(R.id.llOrderProcessing)
    public void orderProcessing() {
        rvOrderCenter.removeAllViews();
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        type = 2;
        netShow();

        ivOrderWhole.setImageResource(R.mipmap.order_all_not_select);
        tvOrderWhole.setTextColor(Color.parseColor("#666666"));
        ivOrderWholeIcon.setVisibility(View.GONE);

        ivOrderAllIcon.setImageResource(R.mipmap.order_confirmed_not_select);
        tvOrderAll.setTextColor(Color.parseColor("#666666"));
        ivOrderAll.setVisibility(View.GONE);

        ivPendingPaymentIcon.setImageResource(R.mipmap.pending_payment_not_select);
        tvPendingPayment.setTextColor(Color.parseColor("#666666"));
        ivPendingPayment.setVisibility(View.GONE);

        ivOrderProcessingIcon.setImageResource(R.mipmap.order_processing_select);
        tvOrderProcessing.setTextColor(Color.parseColor("#31D09A"));
        ivOrderProcessing.setVisibility(View.VISIBLE);
    }

    /**
     * 消息
     */
    @OnClick(R.id.rlMsgNumber)
    public void msgShowOrder() {
        intent = new Intent(getContext(), MessageCenterActivity.class);
        startActivity(intent);
    }

    /**
     * 确认完成
     */
    private void orderConfirm() {
        OrderInfoParam orderInfo = new OrderInfoParam(
                number
        );
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.orderconfirm(orderInfo);
        presenter.attachView(orderConfirmView);
    }

    private ClientBaseView<Object> orderConfirmView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                clientOrdersList();
//                payConfirm(number, price, name, detail);
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "orderConfirmView==" + result);
        }
    };

    @Override
    public boolean onBackPressed() {
        if (mSVProgressHUD.isShowing()) {
            mSVProgressHUD.dismiss();
            if (presenter != null) {
                presenter.onStop();
            }
            return true;
        } else {
            return false;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msgR != null) {
                msgList = new ArrayList<MsgResult.MsgListBean>();
                for (MsgResult.MsgListBean mb : msgR.getMsg_list()) {
                    if (mb.getIs_read() == 0) {
                        msgList.add(mb);
                    }
                }
                if (msgList.size() <= 0) {
                    tvDMsgCount.setVisibility(View.GONE);
                } else {
                    tvDMsgCount.setVisibility(View.VISIBLE);
                    tvDMsgCount.setText(String.valueOf(msgList.size()));
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (list != null) {
            list.clear();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        netShow();
        getMsgs();
    }

    /**
     * 获取消息列表
     */
    private void getMsgs() {
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getmessagelist();
        presenter.attachView(msgView);
    }

    private ClientBaseView<Object> msgView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                MsgResult msg = (MsgResult) GsonUtil.parseJson(baseBean.getData(), MsgResult.class);
                if (msg.getMsg_list().size() <= 0) {
                    tvDMsgCount.setVisibility(View.GONE);
                } else {
                    msgList = new ArrayList<MsgResult.MsgListBean>();
                    for (MsgResult.MsgListBean mb : msg.getMsg_list()) {
                        if (mb.getIs_read() == 0) {
                            msgList.add(mb);
                        }
                    }
                    if (msgList.size() <= 0) {
                        tvDMsgCount.setVisibility(View.GONE);
                    } else {
                        tvDMsgCount.setVisibility(View.VISIBLE);
                        tvDMsgCount.setText(String.valueOf(msgList.size()));
                        ShortcutBadger.applyCount(getContext(), msgList.size());
                    }
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "msgView--" + result);
        }
    };
}
