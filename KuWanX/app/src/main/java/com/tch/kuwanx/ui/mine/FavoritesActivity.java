package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.MyFolderResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.store.ConfirmOrderActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 收藏夹
 */
public class FavoritesActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvFavorites)
    RecyclerView rvFavorites;
    @BindView(R.id.refreshFavorites)
    SmartRefreshLayout refreshFavorites;

    private CommonAdapter favoritesAdapter;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("收藏夹");
        initFavorites();
        refreshFavorites.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myFolderHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        myFolderHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        myFolderHttp();
    }

    private void initFavorites() {
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        rvFavorites.setAdapter(favoritesAdapter = new CommonAdapter<MyFolderResult.ResultBean>(this,
                R.layout.item_favorites, new ArrayList<MyFolderResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, final MyFolderResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getGood_cover())) {
                    Glide.with(FavoritesActivity.this)
                            .load(item.getGood_cover())
                            .into((ImageView) holder.getView(R.id.ivFavoritePhoto));
                } else {
                    holder.setImageResource(R.id.ivFavoritePhoto, R.drawable.placeholder);
                }

                holder.setText(R.id.tvFavoriteResName, item.getGood_name());
                holder.setText(R.id.tvFavoriteResNorm, item.getSpec());
                holder.setText(R.id.tvFavoriteResNum, "X" + item.getStock_num());
                holder.setText(R.id.tvFavoritePrice, item.getCurrent_price());
                holder.setText(R.id.tvFavoriteOldPrice, item.getCost_price());
                //点击购买
                holder.setOnClickListener(R.id.tvFavoriteBuy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FavoritesActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("goodId", item.getId());
                        intent.putExtra("number", "1");
                        startActivity(intent);
                    }
                });
            }
        });
        favoritesAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
    public void favoritesBack() {
        FavoritesActivity.this.finish();
    }

    /**
     * 我的收藏夹列表
     */
    private void myFolderHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(FavoritesActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(FavoritesActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/myFolder.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_myFolder")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        FavoritesActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshFavorites != null) {
                            refreshFavorites.finishLoadmore();
                            refreshFavorites.finishRefresh();
                        }
                        Toasty.warning(FavoritesActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshFavorites != null) {
                            refreshFavorites.finishLoadmore();
                            refreshFavorites.finishRefresh();
                        }

                        MyFolderResult myFolderResult =
                                (MyFolderResult) GsonUtil.json2Object(response, MyFolderResult.class);
                        if (myFolderResult != null
                                && myFolderResult.getRet().equals("1")) {
                            if (isMore) {
                                favoritesAdapter.getDatas().addAll(myFolderResult.getResult());
                                favoritesAdapter.notifyDataSetChanged();
                            } else {
                                favoritesAdapter.getDatas().clear();
                                favoritesAdapter.getDatas().addAll(myFolderResult.getResult());
                                favoritesAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}
