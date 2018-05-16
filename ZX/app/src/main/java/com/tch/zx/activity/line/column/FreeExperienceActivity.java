package com.tch.zx.activity.line.column;

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
import com.tch.zx.adapter.line.FreeExperienceAdapter;
import com.tch.zx.http.bean.result.SpecialWhetherPayResultBean;
import com.tch.zx.http.presenter.SpecialWhetherPayPresenter;
import com.tch.zx.http.view.SpecialWhetherPayView;
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
 * 免费体验
 */
public class FreeExperienceActivity extends BaseActivity {
    /**
     * 标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 数据列表
     */
    @BindView(R.id.rv_column_free_experience)
    RecyclerView rv_column_free_experience;
    @BindView(R.id.refreshLayoutFree)
    SmartRefreshLayout refreshLayoutFree;
    /**
     * 跳转
     */
    private Intent intent;

    private String specialColumnId = "";
    private SpecialWhetherPayPresenter whetherPayPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_free_experience);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();

    }

    /**
     * 初始化页面
     */
    private void initView() {
        tv_title_top_all.setText("免费体验");
        specialColumnId = getIntent().getStringExtra("specialColumnId");

        whetherPayPresenter = new SpecialWhetherPayPresenter(this);
        whetherPayPresenter.onCreate();
        whetherPayPresenter.attachView(whetherPayView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("specialColumnId", specialColumnId);
        map.put("status", "1");
        map.put("sort", "2");

        String data = HelperUtil.getParameter(map);
        whetherPayPresenter.specialWhetherPay(data);

        refreshLayoutFree.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                whetherPayPresenter.attachView(whetherPayView);
            }
        });
    }

    /**
     * 加载列表数据
     */
    private void initRv(final List<SpecialWhetherPayResultBean.ResultBean.ResponseObjectBean> whetherPayResultBeans) {
        FreeExperienceAdapter freeExperienceAdapter = new FreeExperienceAdapter(this, whetherPayResultBeans);
        rv_column_free_experience.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_column_free_experience.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 30, false));
        rv_column_free_experience.setAdapter(freeExperienceAdapter);
        freeExperienceAdapter.setOnItemClickListener(new FreeExperienceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(FreeExperienceActivity.this, OnLinePlayerItemMainActivity.class);
                intent.putExtra("appUserId", whetherPayResultBeans.get(position).getAppUserId());
                intent.putExtra("liveId", whetherPayResultBeans.get(position).getSpecialColumnId());
                startActivity(intent);
            }
        });
    }

    /**
     * 后退
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBack() {
        FreeExperienceActivity.this.finish();
    }

    /**
     * 接口回调
     */
    private SpecialWhetherPayView whetherPayView = new SpecialWhetherPayView() {
        @Override
        public void onSuccess(SpecialWhetherPayResultBean whetherPayResultBean) {
            if (refreshLayoutFree.isRefreshing()) {
                refreshLayoutFree.finishRefresh();
            }
            if (whetherPayResultBean.getResult().getResponseObject() != null
                    && whetherPayResultBean.getResult().getResponseObject().size() > 0) {
                initRv(whetherPayResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            if (refreshLayoutFree.isRefreshing()) {
                refreshLayoutFree.finishRefresh();
            }
            Log.e("Error", "SpecialWhetherPayView:==" + result);
        }
    };
}
