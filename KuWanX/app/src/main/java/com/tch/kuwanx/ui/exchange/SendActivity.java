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
 * 已发货
 */
public class SendActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvSendMineRes)
    RecyclerView rvSendMineRes;
    @BindView(R.id.rvSendHisRes)
    RecyclerView rvSendHisRes;

    private KwMsg kwMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
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

    private CommonAdapter sendMineResAdapter;

    /**
     * 我提供的物品
     */
    private void initConfirmMineRes() {
        rvSendMineRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvSendMineRes.setAdapter(sendMineResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(SendActivity.this) / 3,
                        Utils.getScreenWidth(SendActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(SendActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        sendMineResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SendActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 4; i++) {
            sendMineResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/574e9258d109b3de42e26f85c6bf6c81810a4cfa.jpg");
        }
        sendMineResAdapter.notifyDataSetChanged();
    }

    private CommonAdapter sendHisResAdapter;

    /**
     * 他提供的物品
     */
    private void initConfirmHisRes() {
        rvSendHisRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvSendHisRes.setAdapter(sendHisResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(SendActivity.this) / 3,
                        Utils.getScreenWidth(SendActivity.this) / 3
                );
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(SendActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        sendHisResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SendActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 6; i++) {
            sendHisResAdapter.getDatas().add("http://g.hiphotos.baidu.com/image/pic/item/9f2f070828381f302f04f279a3014c086f06f0c5.jpg");
        }
        sendHisResAdapter.notifyDataSetChanged();
    }

    /**
     * 确认收货
     */
    @OnClick(R.id.btConfirmReceiving)
    public void confirmReceiving() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void payBack() {
        SendActivity.this.finish();
    }
}
