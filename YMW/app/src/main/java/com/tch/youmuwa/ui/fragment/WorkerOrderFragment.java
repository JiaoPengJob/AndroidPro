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
import com.tch.youmuwa.bean.parameters.ContractorFinishedParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.bean.result.WorkerOrdersResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.employer.MessageCenterActivity;
import com.tch.youmuwa.ui.activity.worker.ActualWageActivity;
import com.tch.youmuwa.ui.activity.worker.PointWageActivity;
import com.tch.youmuwa.ui.activity.worker.ProjectValuationActivity;
import com.tch.youmuwa.ui.activity.worker.WageSettlementActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerOrderInfoActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 工人订单
 */
public class WorkerOrderFragment extends ViewPagerFragment implements FragmentBackHandler, MessageInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.ivWorkerOrderAllIcon)
    ImageView ivWorkerOrderAllIcon;
    @BindView(R.id.tvWorkerOrderAll)
    TextView tvWorkerOrderAll;
    @BindView(R.id.ivWorkerOrderAll)
    ImageView ivWorkerOrderAll;
    @BindView(R.id.ivWorkerOrderEvaluatedIcon)
    ImageView ivWorkerOrderEvaluatedIcon;
    @BindView(R.id.tvWorkerOrderEvaluated)
    TextView tvWorkerOrderEvaluated;
    @BindView(R.id.ivWorkerOrderEvaluated)
    ImageView ivWorkerOrderEvaluated;
    @BindView(R.id.ivWorkerOrderConfirmedIcon)
    ImageView ivWorkerOrderConfirmedIcon;
    @BindView(R.id.tvWorkerOrderConfirmed)
    TextView tvWorkerOrderConfirmed;
    @BindView(R.id.ivWorkerOrderConfirmed)
    ImageView ivWorkerOrderConfirmed;
    @BindView(R.id.refreshWorkerOrders)
    SmartRefreshLayout refreshWorkerOrders;
    @BindView(R.id.rvWorkerOrders)
    RecyclerView rvWorkerOrders;
    @BindView(R.id.tvMsgNumbers)
    TextView tvMsgNumbers;
    @BindView(R.id.llWorkerOrdersNet)
    LinearLayout llWorkerOrdersNet;

    private Intent intent;
    private CommonAdapter adapter;
    private TextView tvWorkerOrdersItemButton;
    private Dialog dialog;
    private TextView tvWorkerOrdersCancel, tvWorkerOrdersConfirm;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private int index = 0;
    private List<WorkerOrdersResult.ListBean> list;
    private List<MsgResult.MsgListBean> msgList;
    private boolean isLoadMore = false;
    private int type = 0;
    private WorkerOrdersResult workerOrders;
    private MsgResult msgR;

    public static WorkerOrderFragment newInstance(String param) {
        WorkerOrderFragment fragment = new WorkerOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_worker_order, container, false);
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
            msgList = null;
            workerOrders = null;
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
        list = new ArrayList<WorkerOrdersResult.ListBean>();
        msgList = new ArrayList<MsgResult.MsgListBean>();
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        refreshWorkerOrders.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                index = 0;
                isLoadMore = false;
                list.clear();
                netShow();
            }
        });
        refreshWorkerOrders.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (workerOrders != null && list.size() < workerOrders.getTotal()) {
                    isLoadMore = true;
                    index++;
                    netShow();
                } else {
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
        netShow();
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
            llWorkerOrdersNet.setVisibility(View.GONE);
            refreshWorkerOrders.setVisibility(View.VISIBLE);
            getWorkerOrders();
        } else {
            llWorkerOrdersNet.setVisibility(View.VISIBLE);
            refreshWorkerOrders.setVisibility(View.GONE);
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
     * 获取工人端订单列表
     */
    private void getWorkerOrders() {
        mSVProgressHUD.showWithStatus("加载中...");
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("state", type);
        map.put("page_index", index);
        map.put("paged", 10);

        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getorder(map);
        presenter.attachView(workerOrdersView);
    }

    private ClientBaseView<Object> workerOrdersView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshWorkerOrders.isRefreshing()) {
                refreshWorkerOrders.finishRefresh();
            }

            if (refreshWorkerOrders.isLoading()) {
                refreshWorkerOrders.finishLoadmore();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
                ivWorkerOrderAll.setVisibility(View.GONE);
                ivWorkerOrderConfirmed.setVisibility(View.GONE);
                ivWorkerOrderEvaluatedIcon.setVisibility(View.GONE);
            } else {
                workerOrders = (WorkerOrdersResult) GsonUtil.parseJson(baseBean.getData(), WorkerOrdersResult.class);
                if (workerOrders.getList().size() == 0) {
                    ivWorkerOrderAll.setVisibility(View.GONE);
                    ivWorkerOrderConfirmed.setVisibility(View.GONE);
                    ivWorkerOrderEvaluatedIcon.setVisibility(View.GONE);
                }
                if (isLoadMore) {
                    for (WorkerOrdersResult.ListBean mb : workerOrders.getList()) {
                        list.add(mb);
                    }
                } else {
                    list = workerOrders.getList();
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

            if (refreshWorkerOrders.isRefreshing()) {
                refreshWorkerOrders.finishRefresh();
            }

            if (refreshWorkerOrders.isLoading()) {
                refreshWorkerOrders.finishLoadmore();
            }
            Log.e("Error", "workerOrdersView--" + result);
        }
    };

    private int typeSel = 0;

    /**
     * 加载列表信息
     */
    private void initListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvWorkerOrders.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<WorkerOrdersResult.ListBean>(getContext(), R.layout.item_worker_orders, list) {
            @Override
            protected void convert(ViewHolder holder, final WorkerOrdersResult.ListBean item, int position) {
                holder.setText(R.id.tvWONumber, item.getNumber());
                if (item.getRequire_type() == 1) {
                    holder.setText(R.id.tvWOType, "点工");
                } else {
                    holder.setText(R.id.tvWOType, "包工");
                }
                holder.setText(R.id.tvWOTime, HelperUtil.StringDateToSingle(item.getBegin_date() + " ") + HelperUtil.StringDateToSingle(item.getEnd_date() + " "));
                holder.setText(R.id.tvWOTitle, item.getTitle());
                holder.setText(R.id.tvWOInfo, item.getState_msg());
                switch (item.getState()) {
                    case 201://施工中
                        holder.setVisible(R.id.tvWorkerOrdersItemButton, true);
                        holder.setText(R.id.tvWorkerOrdersItemButton, "确认完工");
                        typeSel = 2;
                        holder.setOnClickListener(R.id.tvWorkerOrdersItemButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                workDoneSuccess(item.getId(), Double.parseDouble(item.getEvery_price()), item.getRequire_type());
                            }
                        });
                        break;
                    case 202:
                    case 203:
                    case 204:
                    case 205:
                    case 221:
                    case 222:
                    case 100:
                    case 102:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 120:
                    case 121:
                    case 123:
                    case 124:
                        holder.setVisible(R.id.tvWorkerOrdersItemButton, false);
                        break;
                    case 220:
                    case 122:
                        holder.setVisible(R.id.tvWorkerOrdersItemButton, true);
                        holder.setText(R.id.tvWorkerOrdersItemButton, "确认辞退");
                        typeSel = 3;
                        holder.setOnClickListener(R.id.tvWorkerOrdersItemButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点工
                                if (item.getRequire_type() == 1) {
                                    intent = new Intent(getContext(), PointWageActivity.class);
                                    intent.putExtra("id", item.getId());
                                    intent.putExtra("price", Double.parseDouble(item.getEvery_price()));
                                    startActivity(intent);
                                } else {
                                    //包工
                                    intent = new Intent(getContext(), ActualWageActivity.class);
                                    intent.putExtra("id", item.getId());
                                    startActivity(intent);
                                }
                            }
                        });
                        break;
                    case 101://定价中
                        holder.setVisible(R.id.tvWorkerOrdersItemButton, true);
                        holder.setText(R.id.tvWorkerOrdersItemButton, "定价");
                        holder.setOnClickListener(R.id.tvWorkerOrdersItemButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent = new Intent(getContext(), ProjectValuationActivity.class);
                                intent.putExtra("id", item.getId());
                                startActivity(intent);
                            }
                        });
                        typeSel = 1;
                        break;
                    case 103:
                        holder.setVisible(R.id.tvWorkerOrdersItemButton, true);
                        holder.setText(R.id.tvWorkerOrdersItemButton, "确认完工");
                        typeSel = 2;
                        holder.setOnClickListener(R.id.tvWorkerOrdersItemButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                contractorFinished(item.getId());
//                                workDoneSuccess(item.getId(), Double.parseDouble(item.getEvery_price()), item.getRequire_type());
                            }
                        });
                        break;
                }

//                tvWorkerOrdersItemButton = (TextView) holder.getView(R.id.tvWorkerOrdersItemButton);
//                holder.setOnClickListener(R.id.tvWorkerOrdersItemButton, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (tvWorkerOrdersItemButton.getText().toString().equals("定价")) {
//                            intent = new Intent(getContext(), ProjectValuationActivity.class);
//                            intent.putExtra("id", item.getId());
//                            startActivity(intent);
//                        } else if (tvWorkerOrdersItemButton.getText().toString().equals("确认完工")) {
////                            if (item.getRequire_type() == 1) {
////                                intent = new Intent(getContext(), WageSettlementActivity.class);
////                                intent.putExtra("id", item.getId());
////                                intent.putExtra("price", Double.parseDouble(item.getEvery_price()));
////                                startActivity(intent);
////                                workDoneSuccess(item.getId(), Double.parseDouble(item.getEvery_price()), item.getRequire_type());
////                            } else {
//                            workDoneSuccess(item.getId(), Double.parseDouble(item.getEvery_price()), item.getRequire_type());
////                            }
//                        } else if (tvWorkerOrdersItemButton.getText().toString().equals("确认辞退")) {
//                            //点工
//                            if (item.getRequire_type() == 1) {
//                                intent = new Intent(getContext(), PointWageActivity.class);
//                                intent.putExtra("id", item.getId());
//                                intent.putExtra("price", Double.parseDouble(item.getEvery_price()));
//                                startActivity(intent);
//                            } else {
//                                //包工
//                                intent = new Intent(getContext(), ActualWageActivity.class);
//                                intent.putExtra("id", item.getId());
//                                startActivity(intent);
//                            }
//                        }
////                        if (tvWorkerOrdersItemButton.getText().toString().equals("定价")) {
////                            intent = new Intent(getContext(), ProjectValuationActivity.class);
////                            intent.putExtra("id", item.getId());
////                            startActivity(intent);
////                        }
//                    }
//                });
            }
        };
        if (adapter != null) {
            rvWorkerOrders.setAdapter(adapter);
        }
        if (list != null && list.size() >= 10) {
            HelperUtil.moveToPosition(layoutManager, rvWorkerOrders, (index + 10));
        }
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(getContext(), WorkerOrderInfoActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 确认完工
     */
    private void workDoneSuccess(final int id, final double price, final int type) {
        dialog = new Dialog(getContext(), R.style.dialog);
        //获取自定义布局
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_work_done, null);
        tvWorkerOrdersCancel = (TextView) layout.findViewById(R.id.tvWorkerOrdersCancel);
        tvWorkerOrdersConfirm = (TextView) layout.findViewById(R.id.tvWorkerOrdersConfirm);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(getContext()) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvWorkerOrdersCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //确定
        tvWorkerOrdersConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //点工
                if (type == 1) {
                    intent = new Intent(getContext(), WageSettlementActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("price", price);
                    startActivity(intent);
                } else {
                    //包工
                    intent = new Intent(getContext(), WageSettlementActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 包工确认完工
     */
    private void contractorFinished(int id) {
        mSVProgressHUD.showWithStatus("加载中...");
        ContractorFinishedParam contractorFinished = new ContractorFinishedParam(
                id
        );
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.achieve2(contractorFinished);
        presenter.attachView(contractorFinishedView);
    }

    private ClientBaseView<Object> contractorFinishedView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                netShow();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "contractorFinishedView" + result);
        }
    };

    /**
     * 全部
     */
    @OnClick(R.id.llWorkerOrderAll)
    public void WorkerOrderAll() {
        rvWorkerOrders.removeAllViews();
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        type = 0;
        netShow();
        ivWorkerOrderAllIcon.setImageResource(R.mipmap.worker_order_all_sel);
        tvWorkerOrderAll.setTextColor(Color.parseColor("#FBC83F"));
        ivWorkerOrderAll.setVisibility(View.VISIBLE);

        ivWorkerOrderConfirmedIcon.setImageResource(R.mipmap.worker_going_not_sel);
        tvWorkerOrderConfirmed.setTextColor(Color.parseColor("#666666"));
        ivWorkerOrderConfirmed.setVisibility(View.GONE);

        ivWorkerOrderEvaluated.setImageResource(R.mipmap.worker_has_done_not_sel);
        tvWorkerOrderEvaluated.setTextColor(Color.parseColor("#666666"));
        ivWorkerOrderEvaluatedIcon.setVisibility(View.GONE);
    }

    /**
     * 进行中
     */
    @OnClick(R.id.llWorkerOrderConfirmed)
    public void orderConfirmed() {
        rvWorkerOrders.removeAllViews();
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        type = 1;
        netShow();
        ivWorkerOrderAllIcon.setImageResource(R.mipmap.worker_order_all_not_sel);
        tvWorkerOrderAll.setTextColor(Color.parseColor("#666666"));
        ivWorkerOrderAll.setVisibility(View.GONE);

        ivWorkerOrderConfirmedIcon.setImageResource(R.mipmap.worker_going_sel);
        tvWorkerOrderConfirmed.setTextColor(Color.parseColor("#FBC83F"));
        ivWorkerOrderConfirmed.setVisibility(View.VISIBLE);

        ivWorkerOrderEvaluated.setImageResource(R.mipmap.worker_has_done_not_sel);
        tvWorkerOrderEvaluated.setTextColor(Color.parseColor("#666666"));
        ivWorkerOrderEvaluatedIcon.setVisibility(View.GONE);
    }

    /**
     * 已完成
     */
    @OnClick(R.id.llWorkerOrderEvaluated)
    public void workerOrderEvaluated() {
        rvWorkerOrders.removeAllViews();
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        type = 2;
        netShow();
        ivWorkerOrderAllIcon.setImageResource(R.mipmap.worker_order_all_not_sel);
        tvWorkerOrderAll.setTextColor(Color.parseColor("#666666"));
        ivWorkerOrderAll.setVisibility(View.GONE);

        ivWorkerOrderConfirmedIcon.setImageResource(R.mipmap.worker_going_not_sel);
        tvWorkerOrderConfirmed.setTextColor(Color.parseColor("#666666"));
        ivWorkerOrderConfirmed.setVisibility(View.GONE);

        ivWorkerOrderEvaluated.setImageResource(R.mipmap.worker_has_done_sel);
        tvWorkerOrderEvaluated.setTextColor(Color.parseColor("#FBC83F"));
        ivWorkerOrderEvaluatedIcon.setVisibility(View.VISIBLE);
    }

    /**
     * 消息
     */
    @OnClick(R.id.rlWorkerMsgNumber)
    public void msgShowOrder() {
        intent = new Intent(getContext(), MessageCenterActivity.class);
        startActivity(intent);
    }

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
                    tvMsgNumbers.setVisibility(View.GONE);
                } else {
                    tvMsgNumbers.setVisibility(View.VISIBLE);
                    tvMsgNumbers.setText(String.valueOf(msgList.size()));
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        netShow();
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
                    tvMsgNumbers.setVisibility(View.GONE);
                } else {
                    msgList = new ArrayList<MsgResult.MsgListBean>();
                    for (MsgResult.MsgListBean mb : msg.getMsg_list()) {
                        if (mb.getIs_read() == 0) {
                            msgList.add(mb);
                        }
                    }
                    if (msgList.size() <= 0) {
                        tvMsgNumbers.setVisibility(View.GONE);
                    } else {
                        tvMsgNumbers.setVisibility(View.VISIBLE);
                        tvMsgNumbers.setText(String.valueOf(msgList.size()));
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
