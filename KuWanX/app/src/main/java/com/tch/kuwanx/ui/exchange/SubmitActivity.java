package com.tch.kuwanx.ui.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.KwMsg;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.chat.ProposedExActivity;
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
 * 已提交
 */
public class SubmitActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvMineSubmitRes)
    RecyclerView rvMineSubmitRes;

    private KwMsg kwMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("置换信息查看");
        btTitleFeatures.setText("修改");
        btTitleFeatures.setVisibility(View.VISIBLE);

        if (getIntent().getSerializableExtra("kwMsg") != null) {
            kwMsg = (KwMsg) getIntent().getSerializableExtra("kwMsg");
        }

        initMineRes();
    }

    private CommonAdapter mineResAdapter;

    /**
     * 加载我提供物品的数据
     */
    private void initMineRes() {
        rvMineSubmitRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvMineSubmitRes.setAdapter(mineResAdapter = new CommonAdapter<String>(this, R.layout.img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        (Utils.getScreenWidth(SubmitActivity.this) - 50) / 3,
                        (Utils.getScreenWidth(SubmitActivity.this) - 50) / 3
                );
                lp.setMargins(5, 5, 5, 5);
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                Glide.with(SubmitActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPhoto));
            }
        });
        mineResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SubmitActivity.this, ArticleInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //临时数据
        for (int i = 0; i < 8; i++) {
            mineResAdapter.getDatas().add("http://g.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=03cfbe5096ef76c6d087f32fa826d1cc/7acb0a46f21fbe098d71af656d600c338744ad90.jpg");
        }
        mineResAdapter.notifyDataSetChanged();
    }

    /**
     * 修改
     */
    @OnClick(R.id.btTitleFeatures)
    public void submitUpdate() {
        Intent intent = new Intent(SubmitActivity.this, UpdateResActivity.class);
        startActivity(intent);
    }

    /**
     * 确认
     */
    @OnClick(R.id.btSubmitConfirm)
    public void submitConfirm() {

    }

    /**
     * 后退
     */
    @OnClick(R.id.ibTitleBack)
    public void submitBack() {
        SubmitActivity.this.finish();
    }
}
