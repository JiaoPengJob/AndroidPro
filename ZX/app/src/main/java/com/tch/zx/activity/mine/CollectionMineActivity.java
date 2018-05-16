package com.tch.zx.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.mine.CollectionAllAdapter;
import com.tch.zx.bean.TestBean;
import com.tch.zx.view.GoTopScrollView;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的收藏
 */
public class CollectionMineActivity extends BaseActivity {

    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 我的收藏的数据列表
     */
    @BindView(R.id.rv_collection_mine_main)
    RecyclerView rv_collection_mine_main;
    /**
     * 置顶布局
     */
    @BindView(R.id.goto_top_collection_mine)
    GoTopScrollView goto_top_collection_mine;
    /**
     * 置顶的图片
     */
    @BindView(R.id.iv_top_collection_mine)
    ImageView iv_top_collection_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_collection_mine);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("我的收藏");
        //设置置顶图片
        goto_top_collection_mine.setImgeViewOnClickGoToFirst(iv_top_collection_mine);
        //设置高度
        goto_top_collection_mine.setScreenHeight(10);
        setRecyclerViewData();
    }

    /**
     * 加载所有收藏列表数据
     */
    private void setRecyclerViewData() {

        List<TestBean> list = new ArrayList<TestBean>();
        list.add(new TestBean("", 0));
        list.add(new TestBean("", 1));
        list.add(new TestBean("", 2));
        list.add(new TestBean("", 3));
        list.add(new TestBean("", 2));
        list.add(new TestBean("", 1));
        list.add(new TestBean("", 0));

        CollectionAllAdapter collectionAllAdapter = new CollectionAllAdapter(this, list);
        rv_collection_mine_main.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_collection_mine_main.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 1, true));
        rv_collection_mine_main.setAdapter(collectionAllAdapter);
//        rv_collection_mine_main.setNestedScrollingEnabled(false);
        collectionAllAdapter.setOnItemClickListener(new CollectionAllAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //判断进入直播播放页面还是小课播放页面
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnCollectionMine() {
        this.finish();
    }
}
