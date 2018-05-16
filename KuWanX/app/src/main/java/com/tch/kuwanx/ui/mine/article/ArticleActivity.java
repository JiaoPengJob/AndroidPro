package com.tch.kuwanx.ui.mine.article;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.UserCdsResult;
import com.tch.kuwanx.ui.BaseActivity;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 物品管理
 */
public class ArticleActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvArticleList)
    RecyclerView rvArticleList;
    @BindView(R.id.llArticleBlank)
    LinearLayout llArticleBlank;
    @BindView(R.id.refreshArticle)
    SmartRefreshLayout refreshArticle;

    private Intent intent;
    private CommonAdapter articleAdapter;
    private boolean isMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("物品管理");
        initArticleListData();
        refreshArticle.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserCdsHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        getUserCdsHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        getUserCdsHttp();
    }

    /**
     * 加载数据信息
     */
    private void initArticleListData() {
        rvArticleList.setLayoutManager(new LinearLayoutManager(this));
        rvArticleList.setAdapter(articleAdapter = new CommonAdapter<UserCdsResult.ResultBean>(this,
                R.layout.item_article, new ArrayList<UserCdsResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, UserCdsResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(ArticleActivity.this)
                            .load(item.getHeadpic())
                            .into((ImageView) holder.getView(R.id.ivArticleListItemImg));
                } else {
                    holder.setImageResource(R.id.ivSearchFriendsImg, R.drawable.placeholder);
                }

                holder.setText(R.id.tvArticleListItemName, item.getName());
                holder.setText(R.id.tvArticleListItemPrice, "￥");
            }
        });
        articleAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(ArticleActivity.this, ArticleInfoActivity.class);
                intent.putExtra("id", ((List<UserCdsResult.ResultBean>) articleAdapter.getDatas()).get(position).getId());
                intent.putExtra("type", "ArticleActivity");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 添加物品
     */
    @OnClick(R.id.btAddArticle)
    public void addArticle() {
        intent = new Intent(ArticleActivity.this, AddItemsActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void articleBack() {
        ArticleActivity.this.finish();
    }

    /**
     * 获取用户物品列表
     */
    private void getUserCdsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(ArticleActivity.this));
        String params = EncryptionUtil.getParameter(ArticleActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserCds.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_article_getUserCds")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ArticleActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshArticle != null) {
                            refreshArticle.finishLoadmore();
                            refreshArticle.finishRefresh();
                        }
                        Toasty.warning(ArticleActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshArticle != null) {
                            refreshArticle.finishLoadmore();
                            refreshArticle.finishRefresh();
                        }

                        UserCdsResult userCdsResult =
                                (UserCdsResult) GsonUtil.json2Object(response, UserCdsResult.class);
                        if (userCdsResult != null
                                && userCdsResult.getRet().equals("1")) {
                            if (userCdsResult.getResult() != null
                                    && userCdsResult.getResult().size() > 0) {
                                llArticleBlank.setVisibility(View.GONE);
                                rvArticleList.setVisibility(View.VISIBLE);
                                if (isMore) {

                                } else {
                                    articleAdapter.getDatas().clear();
                                    articleAdapter.getDatas().addAll(userCdsResult.getResult());
                                    articleAdapter.notifyDataSetChanged();
                                }
                            } else {
                                rvArticleList.setVisibility(View.GONE);
                                llArticleBlank.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rvArticleList.setVisibility(View.GONE);
                            llArticleBlank.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
