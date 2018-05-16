package com.tch.youmuwa.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.GetRequiresListParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.bean.result.RequiresListResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.CPopupInterface;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.employer.MessageCenterActivity;
import com.tch.youmuwa.ui.activity.employer.OrderCenterActivity;
import com.tch.youmuwa.ui.activity.employer.ReleaseOrderActivity;
import com.tch.youmuwa.ui.popupWindow.RadioCalendarPopupWindow;
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
 * 需求
 */

public class DemandFragment extends ViewPagerFragment implements CPopupInterface, FragmentBackHandler, MessageInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.refreshDemand)
    SmartRefreshLayout refreshDemand;
    @BindView(R.id.rvDemand)
    RecyclerView rvDemand;
    @BindView(R.id.rlTitleInfo)
    RelativeLayout rlTitleInfo;
    @BindView(R.id.tvDemandTimeShow)
    TextView tvDemandTimeShow;
    @BindView(R.id.etDemandSearch)
    EditText etDemandSearch;
    @BindView(R.id.llDemandListView)
    LinearLayout llDemandListView;
    @BindView(R.id.tvMsgSize)
    TextView tvMsgSize;
    @BindView(R.id.llDemandNet)
    LinearLayout llDemandNet;
    @BindView(R.id.btReleaseOrder)
    Button btReleaseOrder;
    @BindView(R.id.rlParentView)
    RelativeLayout rlParentView;

    private CommonAdapter commonAdapter;
    private Intent intent;
    private PresenterImpl<Object> presenter;
    private String date;
    private int index = 0;
    private RequiresListResult requiresListResult;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private List<MsgResult.MsgListBean> msgList;
    private List<RequiresListResult.MsgListBean> requiresListItem;
    private RadioCalendarPopupWindow radioCalendarPopupWindow;
    private boolean isLoadMore = false;
    private LinearLayoutManager layoutManager;
    private MsgResult msgR;
    private int year = 0, month = 0, day = 0;

    public static DemandFragment newInstance(String param) {
        DemandFragment fragment = new DemandFragment();
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
        View view = inflater.inflate(R.layout.fragment_demand, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            initView();
        } else {
            index = 0;
            mSVProgressHUD = null;
            requiresListItem = null;
            msgList = null;
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
        year = Integer.parseInt(HelperUtil.getCurrentTime("yyyy"));
        month = Integer.parseInt(HelperUtil.getCurrentTime("MM"));
        day = Integer.parseInt(HelperUtil.getCurrentTime("dd"));
        MessageService.setMi(this);
        etDemandSearch.setFocusable(false);
        etDemandSearch.setFocusableInTouchMode(false);
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        requiresListItem = new ArrayList<RequiresListResult.MsgListBean>();
        msgList = new ArrayList<MsgResult.MsgListBean>();
        tvDemandTimeShow.setText("全部");
        refreshDemand.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                index = 0;
                isLoadMore = false;
                requiresListItem.clear();
                netShow();
            }
        });
        refreshDemand.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                if (requiresListResult != null && requiresListResult.getCan_msg_more() != 0) {
                    index++;
                    netShow();
                } else {
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
        etDemandSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    if (!TextUtils.isEmpty(etDemandSearch.getText().toString())) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(rlParentView, InputMethodManager.SHOW_FORCED);
                        imm.hideSoftInputFromWindow(rlParentView.getWindowToken(), 0); //强制隐藏键盘
                        requiresListItem.clear();
                        if (commonAdapter != null) {
                            commonAdapter.notifyDataSetChanged();
                        }
                        clientRequiresList();
                    }
                }
                return true;
            }
        });
//        netShow();
    }

    @OnClick(R.id.etDemandSearch)
    public void demandSearchClick() {
        etDemandSearch.setFocusable(true);
        etDemandSearch.setFocusableInTouchMode(true);
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
            llDemandNet.setVisibility(View.GONE);
            refreshDemand.setVisibility(View.VISIBLE);
            clientRequiresList();
        } else {
            llDemandNet.setVisibility(View.VISIBLE);
            refreshDemand.setVisibility(View.GONE);
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
        getMsgs();
    }

    /**
     * 获取需求列表信息
     */
    private void clientRequiresList() {
        mSVProgressHUD.showWithStatus("加载中...");
        GetRequiresListParam requiresListParam = new GetRequiresListParam(
                index,
                10,
                date,
                etDemandSearch.getText().toString()
        );
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getrequireslist(requiresListParam);
        presenter.attachView(requiresListView);
    }

    private ClientBaseView<Object> requiresListView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshDemand.isRefreshing()) {
                refreshDemand.finishRefresh();
            }

            if (refreshDemand.isLoading()) {
                refreshDemand.finishLoadmore();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                requiresListResult = (RequiresListResult) GsonUtil.parseJson(baseBean.getData(), RequiresListResult.class);
                if (isLoadMore) {
                    for (RequiresListResult.MsgListBean mb : requiresListResult.getMsg_list()) {
                        requiresListItem.add(mb);
                    }
                } else {
                    requiresListItem = requiresListResult.getMsg_list();
                }
                if (commonAdapter != null) {
                    commonAdapter.notifyDataSetChanged();
                }
                initListData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshDemand.isRefreshing()) {
                refreshDemand.finishRefresh();
            }

            if (refreshDemand.isLoading()) {
                refreshDemand.finishLoadmore();
            }
            Log.e("Errror", "requiresListView==" + result);
        }
    };

    /**
     * 加载列表数据
     */
    private void initListData() {
        layoutManager = new LinearLayoutManager(getContext());
        rvDemand.setLayoutManager(layoutManager);
        commonAdapter = new CommonAdapter<RequiresListResult.MsgListBean>(getContext(), R.layout.item_demand, requiresListItem) {
            @Override
            protected void convert(ViewHolder holder, RequiresListResult.MsgListBean requiresListItem, int position) {
                holder.setText(R.id.tvRequiresListItemTitle, requiresListItem.getTitle());
                holder.setText(R.id.tvRequiresListItemTime, HelperUtil.StringDateToSingle(requiresListItem.getBegin_date()) + "--" + HelperUtil.StringDateToSingle(requiresListItem.getEnd_date()));
                holder.setText(R.id.tvRequiresListItemArea, requiresListItem.getAddress());
                holder.setText(R.id.tvRequiresListItemType, requiresListItem.getWorker_types());
                holder.setText(R.id.tvRequiresListItemDescription, requiresListItem.getDescription());

                if (requiresListItem.getState() == 1) {
                    //由工人抢单
                    if (requiresListItem.getType() == 1) {
                        holder.setText(R.id.tvRequiresListItemCount, requiresListItem.getGrab_count() + "/" + requiresListItem.getMax_count());
                        holder.setBackgroundRes(R.id.tvRequiresListItemCount, R.drawable.oval_worker_type_select);
                    } else {
                        holder.setText(R.id.tvRequiresListItemCount, "待接单");
                        holder.setBackgroundRes(R.id.tvRequiresListItemCount, R.drawable.oval_worker_type_select);
                    }
                } else if (requiresListItem.getState() == 3) {
                    holder.setText(R.id.tvRequiresListItemCount, "已接单");
                    holder.setBackgroundRes(R.id.tvRequiresListItemCount, R.drawable.oval_worker_type_select);
                } else if (requiresListItem.getState() == 4) {
                    holder.setText(R.id.tvRequiresListItemCount, "已拒单");
                    holder.setBackgroundRes(R.id.tvRequiresListItemCount, R.drawable.oval_type_background);
                } else if (requiresListItem.getState() == 2) {
                    holder.setText(R.id.tvRequiresListItemCount, "已取消");
                    holder.setBackgroundRes(R.id.tvRequiresListItemCount, R.drawable.oval_employer_search);
                }

                if (requiresListItem.getRequire_type() == 1) {
                    holder.setText(R.id.tvRequiresListItemState, "点工");
                } else {
                    holder.setText(R.id.tvRequiresListItemState, "包工");
                }
            }
        };
        if (commonAdapter != null) {
            rvDemand.setAdapter(commonAdapter);
        }

        if (requiresListItem != null && requiresListItem.size() >= 10) {
            HelperUtil.moveToPosition(layoutManager, rvDemand, (index + 10));
        }

        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (requiresListItem.get(position) != null) {
                    Intent intent = new Intent(getContext(), OrderCenterActivity.class);
                    intent.putExtra("requireId", requiresListItem.get(position).getId());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 选择时间
     */
    @OnClick(R.id.llDateIcon)
    public void showCalender() {
        if (radioCalendarPopupWindow != null && radioCalendarPopupWindow.getRootView() != null) {
            radioCalendarPopupWindow.getRootView().setVisibility(View.VISIBLE);
        } else {
            radioCalendarPopupWindow = new RadioCalendarPopupWindow(getContext(), DemandFragment.this, year, month, day);
            radioCalendarPopupWindow.showAsDropDown(rlTitleInfo);
            llDemandListView.setAlpha(0.8f);
            radioCalendarPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    llDemandListView.setAlpha(1f);
                }
            });
        }
    }

    /**
     * 日期回调
     */
    @Override
    public void getResult(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        String monthStr, dayStr;
        if (month < 10) {
            monthStr = "0" + String.valueOf(month);
        } else {
            monthStr = String.valueOf(month);
        }

        if (day < 10) {
            dayStr = "0" + String.valueOf(day);
        } else {
            dayStr = String.valueOf(day);
        }

        if (tvDemandTimeShow.getText().toString().equals(String.valueOf(year) + "/" + monthStr + "/" + dayStr)) {
            date = "";
            tvDemandTimeShow.setText("全部");
        } else {
            date = String.valueOf(year) + "-" + monthStr + "-" + dayStr;
            tvDemandTimeShow.setText(String.valueOf(year) + "/" + monthStr + "/" + dayStr);
        }


        requiresListItem.clear();
        if (commonAdapter != null) {
            commonAdapter.notifyDataSetChanged();
        }
        netShow();
    }

    /**
     * 发布需求
     */
    @OnClick(R.id.btReleaseOrder)
    public void releaseOrder() {
        intent = new Intent(getContext(), ReleaseOrderActivity.class);
        startActivity(intent);
    }

    /**
     * 消息
     */
    @OnClick(R.id.rlMessage)
    public void msgShowDemand() {
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
                    tvMsgSize.setVisibility(View.GONE);
                } else {
                    tvMsgSize.setVisibility(View.VISIBLE);
                    tvMsgSize.setText(String.valueOf(msgList.size()));
                }
            }
        }
    };

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
                    tvMsgSize.setVisibility(View.GONE);
                } else {
                    msgList = new ArrayList<MsgResult.MsgListBean>();
                    for (MsgResult.MsgListBean mb : msg.getMsg_list()) {
                        if (mb.getIs_read() == 0) {
                            msgList.add(mb);
                        }
                    }
                    if (msgList.size() <= 0) {
                        tvMsgSize.setVisibility(View.GONE);
                    } else {
                        tvMsgSize.setVisibility(View.VISIBLE);
                        tvMsgSize.setText(String.valueOf(msgList.size()));
                        ShortcutBadger.applyCount(getContext(), msgList.size()); //for 1.1.4+
//                        ShortcutBadger.with(getContext()).count(msgList.size()); //for 1.1.3
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
