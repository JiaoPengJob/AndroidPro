package com.tch.zx.activity.line.column;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.line.ColumnAdapter;
import com.tch.zx.http.bean.result.SpecialListResultBean;
import com.tch.zx.http.presenter.SpecialListPresenter;
import com.tch.zx.http.view.SpecialListView;
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
 * 专栏订阅列表页
 */
public class ColumnActivity extends BaseActivity {

    /**
     * 标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 列表
     */
    @BindView(R.id.rv_column_read)
    RecyclerView rv_column_read;
    @BindView(R.id.refreshColumnMain)
    SmartRefreshLayout refreshColumnMain;

    /**
     * 跳转
     */
    private Intent intent;
    /**
     * 接口
     */
    private SpecialListPresenter specialListPresenter;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_column);
        //集成使用Butterknife
        ButterKnife.bind(this);
        tv_title_top_all.setText("专栏订阅");

        refreshColumnMain.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initView();
            }
        });
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        specialListPresenter = new SpecialListPresenter(this);
        specialListPresenter.onCreate();
        specialListPresenter.attachView(specialListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        data = HelperUtil.getParameter(map);
        specialListPresenter.specialList(data);

    }

    /**
     * 加载列表信息
     */
    private void initRv(final List<SpecialListResultBean.ResultBean.ResponseObjectBean> list) {
        ColumnAdapter columnAdapter = new ColumnAdapter(this, list);
        rv_column_read.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_column_read.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 1, true));
        rv_column_read.setAdapter(columnAdapter);
        columnAdapter.setOnItemClickListener(new ColumnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //未支付
                if (list.get(position).getConfirmPay() == 0) {
                    intent = new Intent(ColumnActivity.this, ColumnInfoActivity.class);
                    intent.putExtra("specialColumnId", list.get(position).getSpecialColumnId());
                    startActivity(intent);
                } else {
                    intent = new Intent(ColumnActivity.this, HasBuySubscriptionActivityActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 后退点击监听事件
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBack() {
        ColumnActivity.this.finish();
    }

    /**
     * 接口回调
     */
    SpecialListView specialListView = new SpecialListView() {
        @Override
        public void onSuccess(SpecialListResultBean specialListResultBean) {
            if (refreshColumnMain.isRefreshing()) {
                refreshColumnMain.finishRefresh();
            }
            if (specialListResultBean.getResult().getResponseObject() != null
                    && specialListResultBean.getResult().getResponseObject().size() != 0) {
                initRv(specialListResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            if (refreshColumnMain.isRefreshing()) {
                refreshColumnMain.finishRefresh();
            }
            Log.e("Error", "specialListView:==" + result);
        }
    };

}
