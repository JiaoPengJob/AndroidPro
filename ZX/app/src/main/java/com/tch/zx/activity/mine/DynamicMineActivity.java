package com.tch.zx.activity.mine;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.contacts.SendDynamicActivity;
import com.tch.zx.adapter.mine.CollectionAllAdapter;
import com.tch.zx.adapter.mine.DynamicMineAdapter;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.bean.TestBean;
import com.tch.zx.http.bean.result.GetDynamicListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的动态
 */
public class DynamicMineActivity extends BaseActivity {

    /**
     * 数据列表
     */
    @BindView(R.id.rv_dynamic_mine_main)
    RecyclerView rv_dynamic_mine_main;
    @BindView(R.id.iv_dynamic_send_new)
    ImageView iv_dynamic_send_new;
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    @BindView(R.id.refreshDynamicMineMain)
    SmartRefreshLayout refreshDynamicMineMain;

    private String id, activity;
    private BasePresenter<Object> presenter;
    private GetDynamicListResult getDynamicListResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_dynamic_mine);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
            getDynamicList();
        }
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            if (!activity.equals("Mine")) {
                iv_dynamic_send_new.setVisibility(View.GONE);
                tv_title_top_all.setText("好友动态");
            }
        }
        refreshDynamicMineMain.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDynamicList();
            }
        });
    }

    private void getDynamicList() {
        presenter = new BasePresenter<Object>(DynamicMineActivity.this);
        presenter.onCreate();
        presenter.attachView(getDynamicListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", id);
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        String data = HelperUtil.getParameter(map);
        presenter.getDynamicList(data);
    }

    private BaseView<Object> getDynamicListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshDynamicMineMain.isRefreshing()) {
                refreshDynamicMineMain.finishRefresh();
            }
            if (baseResultBean.getRet() != null) {
                if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                    getDynamicListResult = (GetDynamicListResult) GsonUtil.parseJson(baseResultBean.getResult(), GetDynamicListResult.class);
                    setRecyclerViewData();
                }
            }
        }

        @Override
        public void onError(String result) {
            if (refreshDynamicMineMain.isRefreshing()) {
                refreshDynamicMineMain.finishRefresh();
            }
            Log.e("ZX", "getDynamicListView接口错误" + result);
        }
    };

    /**
     * 加载动态列表数据
     */
    private void setRecyclerViewData() {
        DynamicMineAdapter dynamicMineAdapter = new DynamicMineAdapter(this, getDynamicListResult.getResponseObject());
        rv_dynamic_mine_main.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_dynamic_mine_main.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 10, false));
        rv_dynamic_mine_main.setAdapter(dynamicMineAdapter);
    }

    /**
     * 发布新动态
     */
    @OnClick(R.id.iv_dynamic_send_new)
    public void sendNewDynamic() {
        Intent intent = new Intent(this, SendDynamicActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_dynamic_mine)
    public void returnDynamicMine() {
        this.finish();
    }
}
