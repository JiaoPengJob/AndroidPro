package com.tch.zx.activity.line.unline;

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
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.line.online.OnLinePlayerItemMainActivity;
import com.tch.zx.adapter.line.LineDownMainAdapter;
import com.tch.zx.http.bean.result.OfflineListResultBean;
import com.tch.zx.http.presenter.OfflineListPresenter;
import com.tch.zx.http.view.OfflineListView;
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
 * 线下主页面/列表页
 */
public class LineDownMainActivity extends BaseActivity {
    /**
     * 列表
     */
    @BindView(R.id.rv_unline_main_list)
    RecyclerView rv_unline_main_list;
    /**
     * 标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    @BindView(R.id.refreshLineDown)
    SmartRefreshLayout refreshLineDown;
    /**
     * 接口
     */
    private OfflineListPresenter offlineListPresenter;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_line_down_main);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();

    }

    /**
     * 初始化页面
     */
    private void initView() {
        tv_title_top_all.setText("线下");

        offlineListPresenter = new OfflineListPresenter(this);
        offlineListPresenter.onCreate();
        offlineListPresenter.attachView(offlineListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNow", "2");
        map.put("pageSize", "10");

        data = HelperUtil.getParameter(map);
        offlineListPresenter.offlineList(data);

        refreshLineDown.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                offlineListPresenter.offlineList(data);
                refreshlayout.finishRefresh(2000);
            }
        });
    }

    /**
     * 加载列表数据
     */
    private void setListData(final List<OfflineListResultBean.ResultBean.ResponseObjectBean> list) {
        LineDownMainAdapter lineDownMainAdapter = new LineDownMainAdapter(this, list);
        rv_unline_main_list.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_unline_main_list.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 10, false));
        rv_unline_main_list.setAdapter(lineDownMainAdapter);
        lineDownMainAdapter.setOnItemClickListener(new LineDownMainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(LineDownMainActivity.this, LineDownInfoActivity.class);
                intent.putExtra("offlineId", list.get(position).getOfflineId());
                startActivity(intent);
            }
        });
    }

    /**
     * 后退
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBack() {
        LineDownMainActivity.this.finish();
    }

    private OfflineListView offlineListView = new OfflineListView() {
        @Override
        public void onSuccess(OfflineListResultBean baseResultBean) {
            if (baseResultBean.getResult().getResponseObject() != null
                    && baseResultBean.getResult().getResponseObject().size() != 0) {
                setListData(baseResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "offlineListView:==" + result);
        }
    };

}
