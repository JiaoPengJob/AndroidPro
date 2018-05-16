package com.tch.kuwanx.ui.exchange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
 * 已确认收货
 */
public class ConfirmReceivedActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvConfirmReceivedMineRes)
    RecyclerView rvConfirmReceivedMineRes;
    @BindView(R.id.rvConfirmReceivedHisRes)
    RecyclerView rvConfirmReceivedHisRes;

    private KwMsg kwMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_received);
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

    private CommonAdapter confirmReceivedMineResAdapter;

    /**
     * 我提供的物品
     */
    private void initConfirmMineRes() {
        rvConfirmReceivedMineRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvConfirmReceivedMineRes.setAdapter(confirmReceivedMineResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(ConfirmReceivedActivity.this) / 3,
                        Utils.getScreenWidth(ConfirmReceivedActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(ConfirmReceivedActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        confirmReceivedMineResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ConfirmReceivedActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 4; i++) {
            confirmReceivedMineResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/574e9258d109b3de42e26f85c6bf6c81810a4cfa.jpg");
        }
        confirmReceivedMineResAdapter.notifyDataSetChanged();
    }

    private CommonAdapter confirmReceivedHisResAdapter;

    /**
     * 他提供的物品
     */
    private void initConfirmHisRes() {
        rvConfirmReceivedHisRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvConfirmReceivedHisRes.setAdapter(confirmReceivedHisResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(ConfirmReceivedActivity.this) / 3,
                        Utils.getScreenWidth(ConfirmReceivedActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(ConfirmReceivedActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        confirmReceivedHisResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ConfirmReceivedActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 6; i++) {
            confirmReceivedHisResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/9f2f070828381f302f04f279a3014c086f06f0c5.jpg");
        }
        confirmReceivedHisResAdapter.notifyDataSetChanged();
    }

    /**
     * 评价
     */
    @OnClick(R.id.btConfirmReceivedComment)
    public void confirmReceivedComment() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void confirmReceivedBack() {
        ConfirmReceivedActivity.this.finish();
    }
}
