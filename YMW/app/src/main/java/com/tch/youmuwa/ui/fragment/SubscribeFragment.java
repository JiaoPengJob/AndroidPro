package com.tch.youmuwa.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.GetRequireResult;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.ui.activity.worker.RejectSingleReasonActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerDemandDetailsActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerSearchActivity;
import com.tch.youmuwa.ui.popupWindow.WorkerPlacePopupWindow;
import com.tch.youmuwa.ui.view.RecyclerViewDecoration;
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

/**
 * 我要接单
 */

public class SubscribeFragment extends ViewPagerFragment implements PopupInterface, CloudListener, FragmentBackHandler {

    @BindView(R.id.rvSubscribe)
    RecyclerView rvSubscribe;
    @BindView(R.id.rlPlaceTitle)
    RelativeLayout rlPlaceTitle;
    @BindView(R.id.tvPlaces)
    TextView tvPlaces;
    @BindView(R.id.etPlaceSearch)
    EditText etPlaceSearch;
    @BindView(R.id.refreshSubscribe)
    SmartRefreshLayout refreshSubscribe;
    @BindView(R.id.llListViewPar)
    LinearLayout llListViewPar;

    private PresenterImpl<Object> presenter;
    private CommonAdapter adapter;
    private Intent intent;
    private String area = "";
    private int index = 0;
    private List<GetRequireResult.ListBean> list;
    private Dialog dialog;//弹出框
    private SVProgressHUD mSVProgressHUD;//加载显示
    private boolean isLoadMore = false;
    private GetRequireResult getRequireResult;

    public static SubscribeFragment newInstance(String param) {
        SubscribeFragment fragment = new SubscribeFragment();
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
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);
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
            area = "";
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
        list = new ArrayList<GetRequireResult.ListBean>();
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        CloudManager.getInstance().init(this);
        handler.sendEmptyMessage(0);
        refreshSubscribe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                list.clear();
                getRequire(area, "");
            }
        });

        refreshSubscribe.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (getRequireResult != null && list.size() < getRequireResult.getTotal()) {
                    isLoadMore = true;
                    index++;
                    if (TextUtils.isEmpty(etPlaceSearch.getText().toString())) {
                        getRequire(area, "");
                    } else {
                        getRequire(area, etPlaceSearch.getText().toString());
                    }
                } else {
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (TextUtils.isEmpty(etPlaceSearch.getText().toString())) {
//            getRequire(area, "");
//        } else {
//            getRequire(area, etPlaceSearch.getText().toString());
//        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TextUtils.isEmpty(etPlaceSearch.getText().toString())) {
                getRequire(area, "");
            } else {
                getRequire(area, etPlaceSearch.getText().toString());
            }
        }
    };

    /**
     * 获取列表
     */
    private void getRequire(String area, String keyWord) {
        mSVProgressHUD.showWithStatus("加载中...");
        Map<String, String> options = new HashMap<String, String>();
        options.put("area", area);
        options.put("keyword", keyWord);
        options.put("page_index", String.valueOf(index));
        options.put("paged", "10");

        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getrequire(options);
        presenter.attachView(requireView);
    }

    private ClientBaseView<Object> requireView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshSubscribe.isRefreshing()) {
                refreshSubscribe.finishRefresh();
            }

            if (refreshSubscribe.isLoading()) {
                refreshSubscribe.finishLoadmore();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                getRequireResult = (GetRequireResult) GsonUtil.parseJson(baseBean.getData(), GetRequireResult.class);
                if (getRequireResult.getTotal() > 0) {
                    if (isLoadMore) {
                        for (GetRequireResult.ListBean mb : getRequireResult.getList()) {
                            list.add(mb);
                        }
                    } else {
                        list = getRequireResult.getList();
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    setListData();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshSubscribe.isRefreshing()) {
                refreshSubscribe.finishRefresh();
            }

            if (refreshSubscribe.isLoading()) {
                refreshSubscribe.finishLoadmore();
            }
            Log.e("Error", "requireView==" + result);
        }
    };

    /**
     * 已接单
     */
    @OnClick(R.id.btHasOrder)
    public void hasOrder() {
        HasReceivedOrdersFragment newFragment = new HasReceivedOrdersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flWorkerFragmentShow, newFragment);
        transaction.commit();
    }

    /**
     * 加载列表数据
     */
    private void setListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvSubscribe.setLayoutManager(layoutManager);
        rvSubscribe.addItemDecoration(new RecyclerViewDecoration(getContext(), "#F0F0F0", 1, false));
        rvSubscribe.setAdapter(adapter = new CommonAdapter<GetRequireResult.ListBean>(getContext(), R.layout.item_subscribe, list) {
            @Override
            protected void convert(ViewHolder holder, final GetRequireResult.ListBean item, int position) {
                holder.setText(R.id.tvItemSubscribeTitle, item.getTitle());
                holder.setText(R.id.tvItemSubscribeRequireType, item.getRequire_type());
                holder.setText(R.id.tvItemSubscribeTime, HelperUtil.StringDateToSingle(item.getBegin_date() + " ") + "--" + HelperUtil.StringDateToSingle(item.getEnd_date() + " "));
                holder.setText(R.id.tvItemSubscribeArea, item.getAddress());
                holder.setText(R.id.tvItemSubscribeType, item.getWorker_types());
                holder.setText(R.id.tvItemSubscribeDescription, item.getDescription());

                switch (item.getIsgrab()) {
                    case 1://未抢单
                        holder.setVisible(R.id.tvIsGrab, true);
                        holder.setVisible(R.id.llSubscribeView, false);
                        break;
//                    case 2://抢单中
//
//                        break;
//                    case 3://抢单失败
//
//                        break;
//                    case 4://抢单成功
//
//                        break;
                    case 5://指定工人下单
                        holder.setVisible(R.id.tvIsGrab, false);
                        holder.setVisible(R.id.llSubscribeView, true);
                        break;
//                    case 6://接单成功
//
//                        break;
                    case 7://工种不匹配
                        holder.setVisible(R.id.tvIsGrab, true);
                        holder.setText(R.id.tvIsGrab, "工种\n不符");
                        holder.setBackgroundRes(R.id.tvIsGrab, R.drawable.circle_grab_gray);
                        holder.setVisible(R.id.llSubscribeView, false);
                        break;
//                    case 8://抢单人数已满
//
//                        break;
                }

                //接单
                holder.setOnClickListener(R.id.tvSubscribeOrders, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        receiveClient(item.getId());
                    }
                });
                //抢单
                holder.setOnClickListener(R.id.tvIsGrab, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getIsgrab() == 1) {
                            graBorder(item.getId());
                        }
                    }
                });
                //拒单
                holder.setOnClickListener(R.id.tvSubscribeRejectOrders, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(getContext(), RejectSingleReasonActivity.class);
                        intent.putExtra("id", item.getId());
                        startActivity(intent);
                    }
                });
            }
        });

        if (list != null && list.size() >= 10) {
            HelperUtil.moveToPosition(layoutManager, rvSubscribe, (index + 10));
        }

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(getContext(), WorkerDemandDetailsActivity.class);
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
     * 工人接单
     */
    private void receiveClient(int id) {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.receive(id);
        presenter.attachView(receiveView);
    }

    private ClientBaseView<Object> receiveView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                //成功显示dialog
                ordersSuccess();
                if (TextUtils.isEmpty(etPlaceSearch.getText().toString())) {
                    getRequire(area, "");
                } else {
                    getRequire(area, etPlaceSearch.getText().toString());
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "receiveView--" + result);
        }
    };

    /**
     * 施工区域
     */
    @OnClick(R.id.llWorkerArea)
    public void workerArea() {
        WorkerPlacePopupWindow areaPopupWindow = new WorkerPlacePopupWindow(getContext(), SubscribeFragment.this);
        areaPopupWindow.showAsDropDown(rlPlaceTitle);
        llListViewPar.setAlpha(0.5f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llListViewPar.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(int index, String result) {
        if (index == 1) {
            if (result.equals("全市")) {
                area = "";
            } else {
                area = result;
            }
            tvPlaces.setText(result);
            list.clear();
            adapter.notifyDataSetChanged();
            getRequire(area, etPlaceSearch.getText().toString());
        }
    }

    /**
     * 显示接单成功
     */
    private void ordersSuccess() {
        dialog = new Dialog(getContext(), R.style.dialog);
        //获取自定义布局
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_orders_success, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(getContext()) * 0.8);
        p.height = (int) (HelperUtil.getScreenHeight(getContext()) * 0.4);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 显示已抢单
     */
    private void grabSingle() {
        dialog = new Dialog(getContext(), R.style.dialog);
        //获取自定义布局
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_grab_single, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(getContext()) * 0.8);
        p.height = (int) (HelperUtil.getScreenHeight(getContext()) * 0.4);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 搜索订单
     */
    @OnClick({R.id.rlSearchOrders, R.id.etPlaceSearch})
    public void searchOrders() {
        intent = new Intent(getContext(), WorkerSearchActivity.class);
        intent.putExtra("area", area);
        startActivity(intent);
    }

    /**
     * 抢单
     */
    private void graBorder(int id) {
        mSVProgressHUD.showWithStatus("抢单中...");
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.graborder(id);
        presenter.attachView(graBorderView);
    }

    private ClientBaseView<Object> graBorderView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() == 1) {
                //成功显示dialog
                grabSingle();
                if (TextUtils.isEmpty(etPlaceSearch.getText().toString())) {
                    getRequire(area, "");
                } else {
                    getRequire(area, etPlaceSearch.getText().toString());
                }
            } else {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "graBorderView==" + result);
        }
    };

    /**
     * LBS云检索
     */
    private void lbsSearch() {
        LocalSearchInfo info = new LocalSearchInfo();
        //access_key（必须），最大长度50
        info.ak = "IbAfhXeHpd46LkBk8RIZ5TUCtWV4kXT9";
        //geo table 表主键（必须）
        info.geoTableId = 174799;
        //标签，可选，空格分隔的多字符串，最长45个字符，样例：美食 小吃
        info.tags = "";
        //检索关键字，可选。最长45个字符。
        info.q = "区县";
        //检索区域名称，必选。市或区的名字，如北京市，海淀区。最长25个字符。
        info.region = "北京市";
        /**
         * localSearch(LocalSearchInfo info)
         * 区域检索，如果所有参数都合法，返回true，否则返回 fasle，
         * 检索的结果在 CloudListener 中的 onGetSearchResult() 函数中。
         * */
        CloudManager.getInstance().localSearch(info);
    }

    /**
     * void onGetSearchResult(CloudSearchResult result,int error)
     * 当详情检索完成时回调此函数
     */
    @Override
    public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {
        Log.e("TAG", "onGetSearchResult/Error===" + i);
        if (cloudSearchResult != null) {
            Log.e("TAG", "onGetSearchResult===" + cloudSearchResult);
        }
    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {
        Log.e("TAG", "Error===" + i);
        if (cloudRgcResult != null) {
            Log.e("TAG", "onGetCloudRgcResult===" + cloudRgcResult.message);
        }
    }

    /**
     * DetailSearchResult:详细信息检索结果类
     * 字段：  poiInfo 详细信息结果数据
     */
    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
        if (detailSearchResult != null) {
            if (detailSearchResult.poiInfo != null) {
                Toast.makeText(getContext(), detailSearchResult.poiInfo.title, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "status:" + detailSearchResult.status, Toast.LENGTH_SHORT).show();
            }
        }
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
}
