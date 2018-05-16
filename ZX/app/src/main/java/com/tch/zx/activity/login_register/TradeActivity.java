package com.tch.zx.activity.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.login_register.ItemLeftTradeAdapter;
import com.tch.zx.adapter.login_register.ItemRightTradeAdapter;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.SIndustryListResultBean;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.presenter.FIndustryListPresenter;
import com.tch.zx.http.presenter.SIndustryListPresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.http.view.FIndustryListResultBean;
import com.tch.zx.http.view.FIndustryListView;
import com.tch.zx.http.view.SIndustryListView;
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
 * 行业编辑
 */
public class TradeActivity extends BaseActivity {

    /**
     * 左侧列表
     */
    @BindView(R.id.rv_trade_left)
    RecyclerView rv_trade_left;

    /**
     * 右侧列表
     */
    @BindView(R.id.rv_trade_right)
    RecyclerView rv_trade_right;

    /**
     * 左侧列表适配器
     */
    private ItemLeftTradeAdapter itemLeftTradeAdapter;

    /**
     * 右侧列表适配器
     */
    private ItemRightTradeAdapter itemRightTradeAdapter;

    private FIndustryListPresenter fIndustryListPresenter;
    private SIndustryListPresenter sIndustryListPresenter;

    private String lText = " ", rText = " ";
    private int intentType;
    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_trade);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        intentType = getIntent().getIntExtra("intentType", 0);
        initFData();
//        getIndustryList();
    }

    private FIndustryListResultBean.ResultBean.ResponseObjectBean robL;

    /**
     * 加载数据
     */
    private void initLData(final List<FIndustryListResultBean.ResultBean.ResponseObjectBean> list) {
        lText = list.get(0).getIndustryFTypeName();
        robL = list.get(0);
        initRightData(String.valueOf(list.get(0).getIndustryFTypeid()));
        itemLeftTradeAdapter = new ItemLeftTradeAdapter(this, list);
        rv_trade_left.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_trade_left.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
        itemLeftTradeAdapter.setOnItemClickListener(new ItemLeftTradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemLeftTradeAdapter.setSelectedPosition(position);
                robL = list.get(position);
                lText = list.get(position).getIndustryFTypeName();
                initRightData(String.valueOf(list.get(position).getIndustryFTypeid()));
                itemLeftTradeAdapter.notifyDataSetChanged();
            }
        });
        rv_trade_left.setAdapter(itemLeftTradeAdapter);
    }

    private SIndustryListResultBean.ResultBean.ResponseObjectBean robR;

    private void initRData(final List<SIndustryListResultBean.ResultBean.ResponseObjectBean> list) {
        rText = list.get(0).getIndustrySTypeName();
        robR = list.get(0);
        itemRightTradeAdapter = new ItemRightTradeAdapter(this, list);
        rv_trade_right.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_trade_right.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
        itemRightTradeAdapter.setOnItemClickListener(new ItemRightTradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemRightTradeAdapter.setSelectedPosition(position);
                robR = list.get(position);
                rText = list.get(position).getIndustrySTypeName();
                itemRightTradeAdapter.notifyDataSetChanged();
            }
        });
        rv_trade_right.setAdapter(itemRightTradeAdapter);
        itemRightTradeAdapter.notifyDataSetChanged();
    }

    /**
     * 头标题返回点击事件
     */
    @OnClick(R.id.ll_return_back_top_trade)
    public void backOnClick() {
        Intent intent = new Intent();
        if (intentType == 12) {
            intent.putExtra("lText", "");
            intent.putExtra("rText", "");
            setResult(12, intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("lRob", robL);
            bundle.putSerializable("rRob", robR);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    /**
     * 头标题完成点击事件
     */
    @OnClick(R.id.tv_done_trade)
    public void doneTradeOnClick() {
        Intent intent = new Intent();
        if (intentType == 12) {
            intent.putExtra("lText", lText);
            intent.putExtra("rText", rText);
            setResult(12, intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("lRob", robL);
            bundle.putSerializable("rRob", robR);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    /**
     * 获取列表
     */
    private void getIndustryList() {
        presenter = new BasePresenter<Object>(TradeActivity.this);
        presenter.onCreate();
        presenter.attachView(industryListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "a");

        String data = HelperUtil.getParameter(map);
        presenter.getIndustryList(data);
    }

    private BaseView<Object> industryListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {

            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "industryListView接口错误" + result);
        }
    };

    /**
     * 加载左侧列表
     */
    private void initFData() {
        fIndustryListPresenter = new FIndustryListPresenter(this);
        fIndustryListPresenter.onCreate();
        fIndustryListPresenter.attachView(fIndustryListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", "abc");

        String data = HelperUtil.getParameter(map);
        fIndustryListPresenter.fIndustryList(data);
    }

    private FIndustryListView fIndustryListView = new FIndustryListView() {
        @Override
        public void onSuccess(FIndustryListResultBean fIndustryListResultBean) {
            if (fIndustryListResultBean.getResult().getResponseObject() != null
                    && fIndustryListResultBean.getResult().getResponseObject().size() > 0) {
                initLData(fIndustryListResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "FIndustryListView" + result);
        }
    };

    /**
     * 加载右侧列表
     */
    private void initRightData(String industryFTypeid) {
        sIndustryListPresenter = new SIndustryListPresenter(this);
        sIndustryListPresenter.onCreate();
        sIndustryListPresenter.attachView(sIndustryListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("industryFTypeid", industryFTypeid);

        String data = HelperUtil.getParameter(map);
        sIndustryListPresenter.sIndustryList(data);
    }

    private SIndustryListView sIndustryListView = new SIndustryListView() {
        @Override
        public void onSuccess(SIndustryListResultBean sIndustryListResultBean) {
            if (sIndustryListResultBean.getResult().getResponseObject() != null
                    && sIndustryListResultBean.getResult().getResponseObject().size() > 0) {
                initRData(sIndustryListResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SIndustryListView" + result);
        }
    };
}
