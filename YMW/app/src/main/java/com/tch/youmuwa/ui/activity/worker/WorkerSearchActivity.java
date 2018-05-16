package com.tch.youmuwa.ui.activity.worker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GetRequireResult;
import com.tch.youmuwa.dao.WorkerSearchHistory;
import com.tch.youmuwa.dao.WorkerSearchHistoryDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.view.RecyclerViewDecoration;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工人端搜索
 */
public class WorkerSearchActivity extends BaseActivtiy {

    @BindView(R.id.etWorkerSearch)
    EditText etWorkerSearch;
    @BindView(R.id.rvWorkerSearchHistory)
    RecyclerView rvWorkerSearchHistory;
    @BindView(R.id.parentWorkerSearch)
    LinearLayout parentWorkerSearch;
    @BindView(R.id.ibWorkerSearchTextClear)
    ImageView ibWorkerSearchTextClear;
    @BindView(R.id.llWorkerSearchHistory)
    LinearLayout llWorkerSearchHistory;
    @BindView(R.id.llWorkerSearchData)
    LinearLayout llWorkerSearchData;
    @BindView(R.id.refreshWorkerSearch)
    SmartRefreshLayout refreshWorkerSearch;
    @BindView(R.id.rvWorkerSearchData)
    RecyclerView rvWorkerSearchData;

    private String area;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private WorkerSearchHistoryDao workerSearchHistoryDao;//搜索历史数据库
    private List<WorkerSearchHistory> workerSearchHistories;//搜索历史
    private CommonAdapter adapter;
    private CommonAdapter itemAdapter;//适配器
    private PresenterImpl<Object> presenter;
    private int index = 0;
    private List<GetRequireResult.ListBean> list;
    private Intent intent;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_search);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        list = new ArrayList<GetRequireResult.ListBean>();
        if (getIntent().getStringExtra("area") != null) {
            area = getIntent().getStringExtra("area");
        }
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);

        //获取数据库
        workerSearchHistoryDao = ((MyApplication) getApplication()).getDaoSession().getWorkerSearchHistoryDao();
        initListData();

        etWorkerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etWorkerSearch.setFocusable(true);
                etWorkerSearch.setFocusableInTouchMode(true);
            }
        });
        etWorkerSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    if (!TextUtils.isEmpty(etWorkerSearch.getText().toString())) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(parentWorkerSearch, InputMethodManager.SHOW_FORCED);
                        imm.hideSoftInputFromWindow(parentWorkerSearch.getWindowToken(), 0); //强制隐藏键盘
                        workerSearchHistoryDao.insert(new WorkerSearchHistory(etWorkerSearch.getText().toString()));
                        getRequire(area, etWorkerSearch.getText().toString());
                    }
                }
                return true;
            }
        });
        etWorkerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")) {
                    ibWorkerSearchTextClear.setVisibility(View.VISIBLE);
                    list.clear();
                    rvWorkerSearchData.removeAllViews();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    llWorkerSearchHistory.setVisibility(View.GONE);
                } else {
                    ibWorkerSearchTextClear.setVisibility(View.GONE);
                    llWorkerSearchHistory.setVisibility(View.VISIBLE);
                    initListData();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    llWorkerSearchHistory.setVisibility(View.VISIBLE);
                    initListData();
                } else {
                    llWorkerSearchHistory.setVisibility(View.GONE);
                    llWorkerSearchData.setVisibility(View.VISIBLE);
                }
            }
        });

        refreshWorkerSearch.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                if (TextUtils.isEmpty(etWorkerSearch.getText().toString())) {
                    getRequire(area, "");
                } else {
                    getRequire(area, etWorkerSearch.getText().toString());
                }
            }
        });
        refreshWorkerSearch.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
                index++;
                if (TextUtils.isEmpty(etWorkerSearch.getText().toString())) {
                    getRequire(area, "");
                } else {
                    getRequire(area, etWorkerSearch.getText().toString());
                }
            }
        });
    }

    /**
     * 加载历史列表数据
     */
    private void initListData() {
        workerSearchHistories = workerSearchHistoryDao.loadAll();
        //去除重复
        workerSearchHistories = HelperUtil.removeWSHuplicate(workerSearchHistories);
        Collections.reverse(workerSearchHistories);

        LinearLayoutManager layoutManager = new LinearLayoutManager(WorkerSearchActivity.this);
        rvWorkerSearchHistory.setLayoutManager(layoutManager);
        rvWorkerSearchHistory.setAdapter(adapter = new CommonAdapter<WorkerSearchHistory>(WorkerSearchActivity.this, R.layout.item_employer_search_history, workerSearchHistories) {
            @Override
            protected void convert(ViewHolder holder, WorkerSearchHistory workerSearchHistory, int position) {
                holder.setText(R.id.tvSearchText, workerSearchHistory.getWorkerSearchHistory());
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                etWorkerSearch.setText(workerSearchHistories.get(position).getWorkerSearchHistory());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

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

        presenter = new PresenterImpl<Object>(WorkerSearchActivity.this);
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
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerSearchActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                llWorkerSearchHistory.setVisibility(View.GONE);
                llWorkerSearchData.setVisibility(View.VISIBLE);
                GetRequireResult getRequireResult = (GetRequireResult) GsonUtil.parseJson(baseBean.getData(), GetRequireResult.class);
                for (GetRequireResult.ListBean bean : getRequireResult.getList()) {
                    list.add(bean);
                }
                setListData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "requireView==" + result);
        }
    };

    /**
     * 加载列表数据
     */
    private void setListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(WorkerSearchActivity.this);
        rvWorkerSearchData.setLayoutManager(layoutManager);
        rvWorkerSearchData.addItemDecoration(new RecyclerViewDecoration(WorkerSearchActivity.this, "#F0F0F0", 1, false));
        rvWorkerSearchData.setAdapter(adapter = new CommonAdapter<GetRequireResult.ListBean>(WorkerSearchActivity.this, R.layout.item_subscribe, list) {
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
                    case 5://指定工人下单
                        holder.setVisible(R.id.tvIsGrab, false);
                        holder.setVisible(R.id.llSubscribeView, true);
                        break;
                    case 7://工种不匹配
                        holder.setVisible(R.id.tvIsGrab, false);
                        holder.setVisible(R.id.llSubscribeView, false);
                        break;
                }


                //接单
                holder.setOnClickListener(R.id.tvSubscribeOrders, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //成功显示dialog
                        ordersSuccess();
                    }
                });
                //抢单
                holder.setOnClickListener(R.id.tvIsGrab, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        graBorder(item.getId());
                    }
                });
                //拒单
                holder.setOnClickListener(R.id.tvSubscribeRejectOrders, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(WorkerSearchActivity.this, RejectSingleReasonActivity.class);
                        intent.putExtra("id", item.getId());
                        startActivity(intent);
                    }
                });
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(WorkerSearchActivity.this, WorkerDemandDetailsActivity.class);
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
     * 显示接单成功
     */
    private void ordersSuccess() {
        dialog = new Dialog(WorkerSearchActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_orders_success, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(WorkerSearchActivity.this) * 0.8);
        p.height = (int) (HelperUtil.getScreenHeight(WorkerSearchActivity.this) * 0.4);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 显示已抢单
     */
    private void grabSingle() {
        dialog = new Dialog(WorkerSearchActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_grab_single, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(WorkerSearchActivity.this) * 0.8);
        p.height = (int) (HelperUtil.getScreenHeight(WorkerSearchActivity.this) * 0.4);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 清除文本框
     */
    @OnClick(R.id.ibWorkerSearchTextClear)
    public void workerSearchTextClear() {
        etWorkerSearch.setText("");
    }

    /**
     * 清除搜索历史
     */
    @OnClick(R.id.ibClearWorkerSearch)
    public void clearWorkerSearch() {
        workerSearchHistoryDao.deleteAll();
        initListData();
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibWorkerRetreat)
    public void workerRetreat() {
        WorkerSearchActivity.this.finish();
    }

    /**
     * 抢单
     */
    private void graBorder(int id) {
        mSVProgressHUD.showWithStatus("抢单中...");
        presenter = new PresenterImpl<Object>(WorkerSearchActivity.this);
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
            } else {
                Toast.makeText(WorkerSearchActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
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
                WorkerSearchActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
