package com.tch.kuwanx.ui.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.KwMsg;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.mine.article.ArticleInfoActivity;
import com.tch.kuwanx.utils.Utils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 已确认
 */
public class ConfirmActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvConfirmMineRes)
    RecyclerView rvConfirmMineRes;
    @BindView(R.id.rvConfirmHisRes)
    RecyclerView rvConfirmHisRes;

    private KwMsg kwMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("置换信息查看");

        if (getIntent().getSerializableExtra("kwMsg") != null) {
            kwMsg = (KwMsg) getIntent().getSerializableExtra("kwMsg");
        }

        initConfirmMineRes();
        initConfirmHisRes();
    }

    private CommonAdapter confirmMineResAdapter;

    /**
     * 我提供的物品
     */
    private void initConfirmMineRes() {
        rvConfirmMineRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvConfirmMineRes.setAdapter(confirmMineResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(ConfirmActivity.this) / 3,
                        Utils.getScreenWidth(ConfirmActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(ConfirmActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        confirmMineResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ConfirmActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 4; i++) {
            confirmMineResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/574e9258d109b3de42e26f85c6bf6c81810a4cfa.jpg");
        }
        confirmMineResAdapter.notifyDataSetChanged();
    }

    private CommonAdapter confirmHisResAdapter;

    /**
     * 他提供的物品
     */
    private void initConfirmHisRes() {
        rvConfirmHisRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvConfirmHisRes.setAdapter(confirmHisResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(ConfirmActivity.this) / 3,
                        Utils.getScreenWidth(ConfirmActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(ConfirmActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        confirmHisResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ConfirmActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 6; i++) {
            confirmHisResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/9f2f070828381f302f04f279a3014c086f06f0c5.jpg");
        }
        confirmHisResAdapter.notifyDataSetChanged();
    }

    /**
     * 支付押金
     */
    @OnClick(R.id.btPayDeposit)
    public void btPayDeposit() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void confirmBack() {
        ConfirmActivity.this.finish();
    }


}
