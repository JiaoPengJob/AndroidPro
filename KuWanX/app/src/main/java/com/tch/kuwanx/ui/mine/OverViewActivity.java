package com.tch.kuwanx.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 综合评价
 */
public class OverViewActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.overViewFlow)
    TagFlowLayout overViewFlow;
    @BindView(R.id.rvOverViewEvaluate)
    RecyclerView rvOverViewEvaluate;

    private List<String> flowList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("综合评价");
        initFlowLayoutData();
        initMineEvaluate();
    }

    /**
     * 加载流式布局
     */
    private void initFlowLayoutData() {
        flowList = new ArrayList<>();
        flowList.add("我");
        flowList.add("在这里");
        flowList.add("重新设置了一些");
        flowList.add("评价数据");
        flowList.add("来看看这个页面的显示效果");
        flowList.add("怎么样");
        overViewFlow.setAdapter(new TagAdapter<String>(flowList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTabShow = (TextView) LayoutInflater.from(OverViewActivity.this).inflate(R.layout.assess_tv, overViewFlow, false);
                tvTabShow.setText(s);
                return tvTabShow;
            }
        });
    }

    private CommonAdapter overViewAdapter;

    /**
     * 加载评价列表
     */
    private void initMineEvaluate() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        rvOverViewEvaluate.setLayoutManager(new LinearLayoutManager(OverViewActivity.this));
        rvOverViewEvaluate.setAdapter(overViewAdapter = new CommonAdapter<String>(OverViewActivity.this, R.layout.item_mine_evaluate, list) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {

            }
        });
        overViewAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void overViewBack() {
        OverViewActivity.this.finish();
    }
}
