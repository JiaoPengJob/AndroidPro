package com.tch.youmuwa.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tch.youmuwa.bean.parameters.HasParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GetRequireResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.worker.WorkerDemandDetailsActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerOrderInfoActivity;
import com.tch.youmuwa.ui.view.RecyclerViewDecoration;
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

/**
 * 已接单
 */
public class HasReceivedOrdersFragment extends ViewPagerFragment implements FragmentBackHandler {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.refreshHasReceivedOrders)
    SmartRefreshLayout refreshHasReceivedOrders;
    @BindView(R.id.rvHasReceivedOrders)
    RecyclerView rvHasReceivedOrders;
    @BindView(R.id.llHasOrdersNet)
    LinearLayout llHasOrdersNet;

    private CommonAdapter adapter;
    private TextView tvHasReceivedSeeOrders;
    private Dialog dialog;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private List<GetRequireResult.ListBean> list;
    private GetRequireResult getRequireResult;
    private int index = 0;
    private boolean isLoadMore = false;

    public static HasReceivedOrdersFragment newInstance(String param) {
        HasReceivedOrdersFragment fragment = new HasReceivedOrdersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tabType", param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_has_received_orders, container, false);
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
            isLoadMore = false;
            getRequireResult = null;
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
        title.setText("已接单");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        list = new ArrayList<GetRequireResult.ListBean>();
        refreshHasReceivedOrders.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                list.clear();
                netShow();
            }
        });
        refreshHasReceivedOrders.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (getRequireResult != null && list.size() < getRequireResult.getTotal()) {
                    isLoadMore = true;
                    index++;
                    netShow();
                } else {
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
    }

    private void netShow() {
        if (HelperUtil.isNetworkConnected(getContext())) {
            llHasOrdersNet.setVisibility(View.GONE);
            refreshHasReceivedOrders.setVisibility(View.VISIBLE);
            getallReady();
        } else {
            llHasOrdersNet.setVisibility(View.VISIBLE);
            refreshHasReceivedOrders.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新
     */
    @OnClick(R.id.btRefreshNet)
    public void refreshNet() {
        netShow();
    }

    @Override
    public void onResume() {
        super.onResume();
        netShow();
    }

    /**
     * 获取已接单列表
     */
    private void getallReady() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getalready(index, 10);
        presenter.attachView(getAllReadyView);
    }

    private ClientBaseView<Object> getAllReadyView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshHasReceivedOrders.isRefreshing()) {
                refreshHasReceivedOrders.finishRefresh();
            }

            if (refreshHasReceivedOrders.isLoading()) {
                refreshHasReceivedOrders.finishLoadmore();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                getRequireResult = (GetRequireResult) GsonUtil.parseJson(baseBean.getData(), GetRequireResult.class);
                if (isLoadMore) {
                    for (GetRequireResult.ListBean bean : getRequireResult.getList()) {
                        list.add(bean);
                    }
                } else {
                    list = getRequireResult.getList();
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

            if (refreshHasReceivedOrders.isRefreshing()) {
                refreshHasReceivedOrders.finishRefresh();
            }

            if (refreshHasReceivedOrders.isLoading()) {
                refreshHasReceivedOrders.finishLoadmore();
            }
            Log.e("Error", "getAllReadyView==" + result);
        }
    };

    private int indexSel = 0;

    /**
     * 加载列表信息
     */
    private void initListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHasReceivedOrders.setLayoutManager(layoutManager);
        rvHasReceivedOrders.addItemDecoration(new RecyclerViewDecoration(getContext(), "#F0F0F0", 1, false));
        adapter = new CommonAdapter<GetRequireResult.ListBean>(getContext(), R.layout.item_has_received_orders, list) {
            @Override
            protected void convert(final ViewHolder holder, final GetRequireResult.ListBean item, final int position) {
                holder.setText(R.id.tvHasReceivedItemTitle, item.getTitle());
                holder.setText(R.id.tvHasReceivedItemRequireType, item.getRequire_type());
                holder.setText(R.id.tvHasReceivedItemWorkTime, HelperUtil.StringDateToSingle(item.getBegin_date() + " ") + "--" + HelperUtil.StringDateToSingle(item.getEnd_date() + " "));
                holder.setText(R.id.tvHasReceivedItemWorkArea, item.getAddress());
                holder.setText(R.id.tvHasReceivedItemWorkTypes, item.getWorker_types());
                holder.setText(R.id.tvHasReceivedItemDescription, item.getDescription());

                if (item.getIsgrab() == 2) {
                    //抢单中
                    holder.setText(R.id.tvHasSubscribeRejectOrders, "抢单中");
//                    holder.setTextColor(R.id.tvHasSubscribeRejectOrders, R.color.yellow_f6);
                    holder.setVisible(R.id.tvHasReceivedSeeOrders, true);
                    holder.setText(R.id.tvHasReceivedSeeOrders, "取消抢单");
                    holder.setOnClickListener(R.id.tvHasReceivedSeeOrders, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //取消抢单
                            diamissOrdersClient(String.valueOf(item.getId()));
                        }
                    });
                } else if (item.getIsgrab() == 3) {
                    //抢单失败
                    holder.setText(R.id.tvHasSubscribeRejectOrders, "抢单失败");
                    holder.setTextColor(R.id.tvHasSubscribeRejectOrders, R.color.search_hint);
                    holder.setVisible(R.id.tvHasReceivedSeeOrders, false);
                } else if (item.getIsgrab() == 4) {
                    //抢单成功
                    holder.setText(R.id.tvHasSubscribeRejectOrders, "抢单成功");
//                    holder.setTextColor(R.id.tvHasSubscribeRejectOrders, R.color.yellow_f6);
                    holder.setVisible(R.id.tvHasReceivedSeeOrders, true);
                    holder.setText(R.id.tvHasReceivedSeeOrders, "查看订单");
                    holder.setOnClickListener(R.id.tvHasReceivedSeeOrders, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), WorkerOrderInfoActivity.class);
                            intent.putExtra("id", item.getOrder_id());
                            startActivity(intent);
                        }
                    });
                } else if (item.getIsgrab() == 6) {
                    //接单成功
                    holder.setText(R.id.tvHasSubscribeRejectOrders, "接单成功");
//                    holder.setTextColor(R.id.tvHasSubscribeRejectOrders, R.color.yellow_f6);
                    holder.setVisible(R.id.tvHasReceivedSeeOrders, true);
                    holder.setText(R.id.tvHasReceivedSeeOrders, "查看订单");
                    holder.setOnClickListener(R.id.tvHasReceivedSeeOrders, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), WorkerOrderInfoActivity.class);
                            intent.putExtra("id", item.getOrder_id());
                            startActivity(intent);
                        }
                    });
                } else {
                    holder.setVisible(R.id.tvHasReceivedSeeOrders, false);
                    holder.setVisible(R.id.tvHasSubscribeRejectOrders, false);
                }
            }
        };

        if (adapter != null) {
            rvHasReceivedOrders.setAdapter(adapter);
        }

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getContext(), WorkerDemandDetailsActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        if (list != null && list.size() >= 10) {
            HelperUtil.moveToPosition(layoutManager, rvHasReceivedOrders, (index + 10));
        }
    }

    /**
     * 链接取消抢单
     */
    private void diamissOrdersClient(String id) {
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.cancelorder(id);
        presenter.attachView(cancelOrderView);
    }

    private ClientBaseView<Object> cancelOrderView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                netShow();
                dismissOrders();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "cancelOrderView==" + result);
        }
    };

    /**
     * 取消抢单
     */
    private void dismissOrders() {
        dialog = new Dialog(getContext(), R.style.dialog);
        //获取自定义布局
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_dismiss_orders, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(getContext()) * 0.8);
        p.height = (int) (HelperUtil.getScreenHeight(getContext()) * 0.4);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatHasReceivedOrders() {
        SubscribeFragment newFragment = new SubscribeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flWorkerFragmentShow, newFragment);
        transaction.commit();
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
            SubscribeFragment newFragment = new SubscribeFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.flWorkerFragmentShow, newFragment);
            transaction.commit();
            return true;
        }
    }

}
