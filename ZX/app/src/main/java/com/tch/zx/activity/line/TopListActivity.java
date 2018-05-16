package com.tch.zx.activity.line;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.line.greatclass.GreatClassItemPlayerActivity;
import com.tch.zx.activity.line.online.OnLinePlayerItemMainActivity;
import com.tch.zx.adapter.FineLittleClassAdapter;
import com.tch.zx.adapter.line.HotOnlinePlayerRemdAdapter;
import com.tch.zx.bean.HomeBean;
import com.tch.zx.dao.green.SmallBean;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.LiveListResultBean;
import com.tch.zx.util.ConstantData;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 热门榜单
 */
public class TopListActivity extends BaseActivity {
    /**
     * 小课数据列表
     */
    @BindView(R.id.rl_top_list)
    RecyclerView rl_top_list;
    /**
     * 直播数据列表
     */
    @BindView(R.id.rl_top_list_player)
    RecyclerView rl_top_list_player;
    /**
     * 标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 小课文字
     */
    @BindView(R.id.tv_top_list_class)
    TextView tv_top_list_class;
    @BindView(R.id.view_top_list_class)
    View view_top_list_class;
    /**
     * 直播文字
     */
    @BindView(R.id.tv_top_list_player)
    TextView tv_top_list_player;
    @BindView(R.id.view_top_list_player)
    View view_top_list_player;
    @BindView(R.id.refreshTopList)
    SmartRefreshLayout refreshTopList;
    @BindView(R.id.refreshTopListPlayer)
    SmartRefreshLayout refreshTopListPlayer;

    /**
     * 小课列表适配器
     */
    private FineLittleClassAdapter fineLittleClassAdapter;
    /**
     * 直播列表适配器
     */
    private HotOnlinePlayerRemdAdapter hotOnlinePlayerRemdAdapter;
    /**
     * 跳转
     */
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_top_list);
        ButterKnife.bind(this);
        initView();

    }

    /**
     * 初始化页面
     */
    private void initView() {
        tv_title_top_all.setText("热门榜单");
        setClassData();
//        setPlayerData();
    }

    /**
     * 加载小课列表数据
     */
    private void setClassData() {
        List<SmallBean> list = new ArrayList<SmallBean>();
        for (int i = 0; i < 5; i++) {
//            list.add(new HomeResultBean().getResult().getSmall());
        }

        fineLittleClassAdapter = new FineLittleClassAdapter(this, list, ConstantData.TOP_LIST_TYPE);
        rl_top_list.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rl_top_list.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 10, false));
        rl_top_list.setAdapter(fineLittleClassAdapter);
        fineLittleClassAdapter.setOnItemClickListener(new FineLittleClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(TopListActivity.this, GreatClassItemPlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 加载直播列表数据
     */
    private void setPlayerData(List<LiveListResultBean.ResultBean.ResponseObjectBean> list) {

        hotOnlinePlayerRemdAdapter = new HotOnlinePlayerRemdAdapter(this, list);
        rl_top_list_player.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rl_top_list_player.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 10, false));
        rl_top_list_player.setAdapter(hotOnlinePlayerRemdAdapter);
        hotOnlinePlayerRemdAdapter.setOnItemClickListener(new HotOnlinePlayerRemdAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(TopListActivity.this, OnLinePlayerItemMainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 小课点击
     */
    @OnClick(R.id.ll_top_list_class)
    public void classOnClick() {
        tv_top_list_class.setTextColor(Color.parseColor("#2EA168"));
        tv_top_list_player.setTextColor(Color.parseColor("#666666"));
        view_top_list_class.setVisibility(View.VISIBLE);
        view_top_list_player.setVisibility(View.GONE);
        rl_top_list.setVisibility(View.VISIBLE);
        rl_top_list_player.setVisibility(View.GONE);

    }

    /**
     * 直播点击
     */
    @OnClick(R.id.ll_top_list_player)
    public void playerOnClick() {
        tv_top_list_class.setTextColor(Color.parseColor("#666666"));
        tv_top_list_player.setTextColor(Color.parseColor("#2EA168"));
        view_top_list_class.setVisibility(View.GONE);
        view_top_list_player.setVisibility(View.VISIBLE);
        rl_top_list.setVisibility(View.GONE);
        rl_top_list_player.setVisibility(View.VISIBLE);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBackTopList() {
        this.finish();
    }
}
