package com.tch.youmuwa.ui.activity.employer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.SearchWorkerParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.SearchWorkerResult;
import com.tch.youmuwa.dao.EmployerSearchHistory;
import com.tch.youmuwa.dao.EmployerSearchHistoryDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 雇主搜索
 */
public class SearchActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.etEmployerSearch)
    EditText etEmployerSearch;
    @BindView(R.id.rvEmployerSearchHistory)
    RecyclerView rvEmployerSearchHistory;
    @BindView(R.id.llSearchHistory)
    LinearLayout llSearchHistory;
    @BindView(R.id.rvEmployerSearchData)
    RecyclerView rvEmployerSearchData;
    @BindView(R.id.llSearchData)
    LinearLayout llSearchData;
    @BindView(R.id.ibSearchTextClear)
    ImageButton ibSearchTextClear;
    @BindView(R.id.parentSearch)
    LinearLayout parentSearch;
    @BindView(R.id.llEditInput)
    LinearLayout llEditInput;
    /**
     * 设置的参数
     */
    private EmployerSearchHistoryDao employerSearchHistoryDao;//搜索历史数据库
    private List<EmployerSearchHistory> employerSearchHistories;//搜索历史
    private PresenterImpl<Object> presenter;//接口
    private String city;//城市
    private CommonAdapter adapter;
    private CommonAdapter itemAdapter;//适配器
    private SearchWorkerResult.MsgListBean msgListBean;//搜索返回结果
    private SVProgressHUD mSVProgressHUD;//加载显示
    private List<SearchWorkerResult.MsgListBean> msg_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        if (getIntent().getStringExtra("city") != null) {
            city = getIntent().getStringExtra("city");
        }
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        msg_list = new ArrayList<SearchWorkerResult.MsgListBean>();
        //获取数据库
        employerSearchHistoryDao = ((MyApplication) getApplication()).getDaoSession().getEmployerSearchHistoryDao();
        initListData();

        etEmployerSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    if (!TextUtils.isEmpty(etEmployerSearch.getText().toString())) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(parentSearch, InputMethodManager.SHOW_FORCED);
                        imm.hideSoftInputFromWindow(parentSearch.getWindowToken(), 0); //强制隐藏键盘
                        clientSearchWorker();
                        employerSearchHistoryDao.insert(new EmployerSearchHistory(etEmployerSearch.getText().toString()));
                    }
                }
                return true;
            }
        });
        etEmployerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")) {
                    ibSearchTextClear.setVisibility(View.VISIBLE);
                    rvEmployerSearchData.removeAllViews();
                    msg_list.clear();
                    if (itemAdapter != null) {
                        itemAdapter.notifyDataSetChanged();
                    }
                    llSearchHistory.setVisibility(View.GONE);
                } else {
                    ibSearchTextClear.setVisibility(View.GONE);
                    llSearchHistory.setVisibility(View.VISIBLE);
                    initListData();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    llSearchHistory.setVisibility(View.VISIBLE);
                    initListData();
                } else {
                    llSearchHistory.setVisibility(View.GONE);
                    llSearchData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.llEditInput)
    public void inputFource() {
        etEmployerSearch.setFocusable(true);
        etEmployerSearch.setFocusableInTouchMode(true);
        etEmployerSearch.requestFocus();
    }

    /**
     * 加载历史列表数据
     */
    private void initListData() {
        employerSearchHistories = employerSearchHistoryDao.loadAll();
        //去除重复
        employerSearchHistories = HelperUtil.removeESHuplicate(employerSearchHistories);
        Collections.reverse(employerSearchHistories);

        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        rvEmployerSearchHistory.setLayoutManager(layoutManager);
        rvEmployerSearchHistory.setAdapter(adapter = new CommonAdapter<EmployerSearchHistory>(SearchActivity.this, R.layout.item_employer_search_history, employerSearchHistories) {
            @Override
            protected void convert(ViewHolder holder, EmployerSearchHistory employerSearchHistory, int position) {
                holder.setText(R.id.tvSearchText, employerSearchHistory.getEmployerSearchHistory());
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                etEmployerSearch.setText(employerSearchHistories.get(position).getEmployerSearchHistory());
                clientSearchWorker();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 加载搜索数据列表数据
     */
    private void initSearchData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        rvEmployerSearchData.setLayoutManager(layoutManager);
        rvEmployerSearchData.setAdapter(itemAdapter = new CommonAdapter<SearchWorkerResult.MsgListBean>(SearchActivity.this, R.layout.item_employer_search_info, msg_list) {
            @Override
            protected void convert(ViewHolder holder, SearchWorkerResult.MsgListBean msg, int position) {
                holder.setText(R.id.tvWorkerName, msg.getName());
                holder.setText(R.id.tvSearchWorkerType, msg.getWorker_type_name());
                holder.setText(R.id.tvSearchWorkerYear, String.valueOf(msg.getWork_age()));
                holder.setText(R.id.tvSearchOrdersNumber, String.valueOf(msg.getOrder_count()));
                holder.setText(R.id.tvSearchGoodAt, msg.getStrength());
                holder.setText(R.id.tvSearchConstructionState, msg.getWork_state());
                if (msg.getWork_state().equals("空闲")) {
                    holder.setBackgroundRes(R.id.tvSearchConstructionState, R.drawable.oval_guide_button);
                } else if (msg.getWork_state().equals("施工")) {
                    holder.setBackgroundRes(R.id.tvSearchConstructionState, R.drawable.oval_worker_type_select);
                }
                final ImageView ivSearchWorkerPhoto = (ImageView) holder.getView(R.id.ivSearchWorkerPhoto);
                Glide.with(SearchActivity.this)
                        .asBitmap()
                        .load(msg.getHead_image_path())
                        .into(new BitmapImageViewTarget(ivSearchWorkerPhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(SearchActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivSearchWorkerPhoto.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                LinearLayout llSearchStarParent = (LinearLayout) holder.getView(R.id.llSearchStarParent);
                for (int i = 0; i < msg.getStar_level(); i++) {
                    ImageView star = new ImageView(SearchActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(6, 0, 0, 0);
                    star.setLayoutParams(lp);
                    star.setImageResource(R.mipmap.star);
                    llSearchStarParent.addView(star);
                }
            }
        });

        itemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SearchActivity.this, WorkerInfoActivity.class);
                intent.putExtra("workerId", msg_list.get(position).getId());
                intent.putExtra("isSpecify", msg_list.get(position).getWorker_type_name());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 清除文本框
     */
    @OnClick(R.id.ibSearchTextClear)
    public void clearEditText() {
        etEmployerSearch.setText("");
    }

    /**
     * 清除搜索历史
     */
    @OnClick(R.id.ibClearEmployerSearch)
    public void clearEmployerSearch() {
        employerSearchHistoryDao.deleteAll();
        initListData();
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatSearch() {
        SearchActivity.this.finish();
    }

    /**
     * 搜索工人
     */
    private void clientSearchWorker() {
        mSVProgressHUD.showWithStatus("加载中...");
        SearchWorkerParam searchWorkerParam = new SearchWorkerParam(
                etEmployerSearch.getText().toString(),
                0,
                30,
                city
        );
        presenter = new PresenterImpl<Object>(SearchActivity.this);
        presenter.onCreate();
        presenter.searchworker(searchWorkerParam);
        presenter.attachView(searchWorkerView);
    }

    private ClientBaseView<Object> searchWorkerView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(SearchActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                SearchWorkerResult searchWorkerResult = GsonUtil.parseJson(baseBean.getData(), SearchWorkerResult.class);
                msg_list.clear();
                if (itemAdapter != null) {
                    itemAdapter.notifyDataSetChanged();
                }
                msg_list = searchWorkerResult.getMsg_list();
                initSearchData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "searchWorkerView:==" + result);
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
                SearchActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
