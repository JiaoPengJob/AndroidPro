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
 * 已还货
 */
public class RepayActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvRepayMineRes)
    RecyclerView rvRepayMineRes;
    @BindView(R.id.rvRepayHisRes)
    RecyclerView rvRepayHisRes;

    private KwMsg kwMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repay);
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

    private CommonAdapter repayMineResAdapter;

    /**
     * 我提供的物品
     */
    private void initConfirmMineRes() {
        rvRepayMineRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvRepayMineRes.setAdapter(repayMineResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(RepayActivity.this) / 3,
                        Utils.getScreenWidth(RepayActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(RepayActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        repayMineResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(RepayActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 4; i++) {
            repayMineResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/574e9258d109b3de42e26f85c6bf6c81810a4cfa.jpg");
        }
        repayMineResAdapter.notifyDataSetChanged();
    }

    private CommonAdapter repayHisResAdapter;

    /**
     * 他提供的物品
     */
    private void initConfirmHisRes() {
        rvRepayHisRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvRepayHisRes.setAdapter(repayHisResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(RepayActivity.this) / 3,
                        Utils.getScreenWidth(RepayActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(RepayActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        repayHisResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(RepayActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 6; i++) {
            repayHisResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/9f2f070828381f302f04f279a3014c086f06f0c5.jpg");
        }
        repayHisResAdapter.notifyDataSetChanged();
    }

    /**
     * 确认收货
     */
    @OnClick(R.id.btRepayConfirm)
    public void repayConfirm() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void payBack() {
        RepayActivity.this.finish();
    }
}
