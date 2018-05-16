package com.tch.kuwanx.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.MyPostResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.NewEasyIndicator;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 我的帖子
 */
public class MyPostsActivity extends BaseActivity implements NewEasyIndicator.onTabClickListener, OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.myPostsIndicator)
    NewEasyIndicator myPostsIndicator;
    @BindView(R.id.rvMyPosts)
    RecyclerView rvMyPosts;
    @BindView(R.id.refreshMyPosts)
    SmartRefreshLayout refreshMyPosts;

    private String[] mTitles = {"我的帖子", "我收藏的帖子"};
    private CommonAdapter myPostsAdapter;
    private ArrayList<String> ninePhotos = new ArrayList<>();
    private int postIndex;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        postIndex = getIntent().getIntExtra("postIndex", 0);
        tvTitleContent.setText("我的帖子");
        myPostsIndicator.setTabTitles(mTitles, postIndex);
        myPostsIndicator.onPageSelected(postIndex);
        myPostsIndicator.setOnTabClickListener(this);
        initMyPosts();
        refreshMyPosts.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        index = 1;
        switch (postIndex) {
            case 0:
                myPostHttp();
                break;
            case 1:
                myCollectPostHttp();
                break;
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        switch (postIndex) {
            case 0:
                myPostHttp();
                break;
            case 1:
                myCollectPostHttp();
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        switch (postIndex) {
            case 0:
                myPostHttp();
                break;
            case 1:
                myCollectPostHttp();
                break;
        }
    }

    @Override
    public void onTabClick(String s, int i) {
        if (myPostsAdapter.getDatas() != null) {
            myPostsAdapter.getDatas().clear();
        }
        postIndex = i;
        index = 1;
        switch (i) {
            case 0:
                myPostHttp();
                break;
            case 1:
                myCollectPostHttp();
                break;
        }
    }

    private void initMyPosts() {
        for (int i = 0; i < 2; i++) {
            ninePhotos.add("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=3ce74294ab6eddc426b2bcff0ceb9acb/9c16fdfaaf51f3de869cd51592eef01f3a297990.jpg");
        }
        rvMyPosts.setLayoutManager(new LinearLayoutManager(this));
        rvMyPosts.setAdapter(myPostsAdapter = new CommonAdapter<MyPostResult.ResultBean>(this,
                R.layout.item_my_posts, new ArrayList<MyPostResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, MyPostResult.ResultBean item, int position) {
                ((NineGridImageView) holder.getView(R.id.ngivMyPosts)).setAdapter(mNineAdapter);
                ((NineGridImageView) holder.getView(R.id.ngivMyPosts)).setImagesData(ninePhotos);

                Glide.with(MyPostsActivity.this)
                        .load(item.getHeadpic())
                        .apply(bitmapTransform(new CropCircleTransformation()))
                        .into((ImageView) holder.getView(R.id.ivPostsImg));
                holder.setText(R.id.tvPostsName, item.getNickname());
                holder.setText(R.id.tvPostsTime, item.getPublish_time());
                holder.setText(R.id.tvPostsContent, item.getSwap_cds());
                holder.setText(R.id.tvPostsAddress, item.getCds_address());
            }
        });
        myPostsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private NineGridImageViewAdapter<String> mNineAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String photo) {
            Glide.with(MyPostsActivity.this)
                    .load(photo)
                    .into(imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> photoList) {
            showGallery(index);
        }
    };

    /**
     * 展示大图画廊
     */
    private void showGallery(int position) {
        Album.gallery(this)
                .requestCode(100) // 请求码，会在listener中返回。
                .checkedList(ninePhotos) // 要浏览的图片列表：ArrayList<String>。
                .currentPosition(position)
                .navigationAlpha(250) // Android5.0+的虚拟导航栏的透明度。
                .checkable(false) // 是否有浏览时的选择功能。
                .widget(
                        Widget.newDarkBuilder(MyPostsActivity.this)
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void myPostsBack() {
        MyPostsActivity.this.finish();
    }

    /**
     * 我的帖子
     */
    private void myPostHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(MyPostsActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(MyPostsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/myPost.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_myPost")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        MyPostsActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshMyPosts != null) {
                            refreshMyPosts.finishLoadmore();
                            refreshMyPosts.finishRefresh();
                        }
                        Toasty.warning(MyPostsActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshMyPosts != null) {
                            refreshMyPosts.finishLoadmore();
                            refreshMyPosts.finishRefresh();
                        }

                        MyPostResult myPostResult =
                                (MyPostResult) GsonUtil.json2Object(response, MyPostResult.class);
                        if (myPostResult != null
                                && myPostResult.getRet().equals("1")) {
                            if (isMore) {
                                myPostsAdapter.getDatas().addAll(myPostResult.getResult());
                                myPostsAdapter.notifyDataSetChanged();
                            } else {
                                myPostsAdapter.getDatas().clear();
                                myPostsAdapter.getDatas().addAll(myPostResult.getResult());
                                myPostsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    /**
     * 我收藏的帖子
     */
    private void myCollectPostHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(MyPostsActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(MyPostsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/myCollectPost.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_myCollectPost")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        MyPostsActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshMyPosts != null) {
                            refreshMyPosts.finishLoadmore();
                            refreshMyPosts.finishRefresh();
                        }
                        Toasty.warning(MyPostsActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshMyPosts != null) {
                            refreshMyPosts.finishLoadmore();
                            refreshMyPosts.finishRefresh();
                        }

                        MyPostResult myPostResult =
                                (MyPostResult) GsonUtil.json2Object(response, MyPostResult.class);
                        if (myPostResult != null
                                && myPostResult.getRet().equals("1")) {
                            if (isMore) {
                                myPostsAdapter.getDatas().addAll(myPostResult.getResult());
                                myPostsAdapter.notifyDataSetChanged();
                            } else {
                                myPostsAdapter.getDatas().clear();
                                myPostsAdapter.getDatas().addAll(myPostResult.getResult());
                                myPostsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}
