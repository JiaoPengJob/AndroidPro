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
import com.tch.zx.activity.line.greatclass.GreatClassPlayerActivity;
import com.tch.zx.activity.line.online.OnLinePlayerItemMainActivity;
import com.tch.zx.adapter.line.FreeExperienceAdapter;
import com.tch.zx.http.bean.result.SpecialSubscribeResultBean;
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
 * 付款之后的专栏页面
 */
public class HasBuySubscriptionActivityActivity extends BaseActivity {
    /**
     * 数据列表
     */
    @BindView(R.id.rv_has_buy_columns)
    RecyclerView rv_has_buy_columns;
    @BindView(R.id.refreshLayoutHasBuy)
    SmartRefreshLayout refreshLayoutHasBuy;
    @BindView(R.id.tvSpecialColumnName)
    TextView tvSpecialColumnName;
    @BindView(R.id.tvSpecialColumnByName)
    TextView tvSpecialColumnByName;
    @BindView(R.id.tvSubscriptionNumber)
    TextView tvSubscriptionNumber;
    @BindView(R.id.tvUpdated)
    TextView tvUpdated;
    @BindView(R.id.tv_flash_back)
    TextView tv_flash_back;

    private String specialColumnId = "";
    private SpecialWhetherPayPresenter whetherPayPresenter;
    private SpecialSubscribeResultBean.ResultBean.ResponseObjectBean subscribeResultBean;
    private boolean isFlash = true;//默认倒叙

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_has_buy_subscription_activity);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();

    }

    /**
     * 初始化
     */
    private void initView() {
        specialColumnId = getIntent().getStringExtra("specialColumnId");
        subscribeResultBean = (SpecialSubscribeResultBean.ResultBean.ResponseObjectBean) getIntent().getSerializableExtra("subscribeResultBean");

        if (subscribeResultBean != null) {
            tvSpecialColumnName.setText(subscribeResultBean.getSpecialColumnName());
            tvSpecialColumnByName.setText(subscribeResultBean.getSpecialColumnByName());
            tvSubscriptionNumber.setText(subscribeResultBean.getSubscriptionNumber() + "人订阅");
        }

        specialWhetherPay("2");

        refreshLayoutHasBuy.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                whetherPayPresenter.attachView(whetherPayView);
            }
        });
    }

    /**
     * 访问服务器
     */
    private void specialWhetherPay(String sort) {
        whetherPayPresenter = new SpecialWhetherPayPresenter(this);
        whetherPayPresenter.onCreate();
        whetherPayPresenter.attachView(whetherPayView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("specialColumnId", specialColumnId);
        map.put("status", "2");
        map.put("sort", sort);

        String data = HelperUtil.getParameter(map);
        whetherPayPresenter.specialWhetherPay(data);
    }

    /**
     * 加载列表数据
     */
    private void setListData(final List<SpecialWhetherPayResultBean.ResultBean.ResponseObjectBean> whetherPayResultBeans) {
        FreeExperienceAdapter freeExperienceAdapter = new FreeExperienceAdapter(this, whetherPayResultBeans);
        rv_has_buy_columns.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_has_buy_columns.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 10, false));
        rv_has_buy_columns.setAdapter(freeExperienceAdapter);
        freeExperienceAdapter.setOnItemClickListener(new FreeExperienceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HasBuySubscriptionActivityActivity.this, GreatClassPlayerActivity.class);
                intent.putExtra("intentType", "specialColumn");
                intent.putExtra("appUserId", whetherPayResultBeans.get(position).getAppUserId());
                intent.putExtra("specialColumnClassId", String.valueOf(whetherPayResultBeans.get(position).getSpecialColumnClassId()));
                startActivity(intent);
            }
        });
    }

    /**
     * 升序或倒叙选择
     */
    @OnClick(R.id.tv_flash_back)
    public void flash() {
        if (isFlash) {
            tv_flash_back.setText("倒序");
            specialWhetherPay("1");
            isFlash = false;
        } else {
            tv_flash_back.setText("升序");
            specialWhetherPay("2");
            isFlash = true;
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.iv_return_column_info_has_buy)
    public void returnBackHasBuy() {
        this.finish();
    }

    /**
     * 接口回调
     */
    private SpecialWhetherPayView whetherPayView = new SpecialWhetherPayView() {
        @Override
        public void onSuccess(SpecialWhetherPayResultBean whetherPayResultBean) {
            if (refreshLayoutHasBuy.isRefreshing()) {
                refreshLayoutHasBuy.finishRefresh();
            }
            if (whetherPayResultBean.getResult().getResponseObject() != null
                    && whetherPayResultBean.getResult().getResponseObject().size() > 0) {
                setListData(whetherPayResultBean.getResult().getResponseObject());
                tvUpdated.setText("已更新" + whetherPayResultBean.getResult().getResponseObject().size() + "条");
            }
        }

        @Override
        public void onError(String result) {
            if (refreshLayoutHasBuy.isRefreshing()) {
                refreshLayoutHasBuy.finishRefresh();
            }
            Log.e("Error", "SpecialWhetherPayView:==" + result);
        }
    };
}
