package com.tch.kuwanx.ui.mine.article;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.SysCdsByIdResult;
import com.tch.kuwanx.result.UserCdByIdResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.GlideImageLoader;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
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
 * 物品详情
 */
public class ArticleInfoActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.bannerArticleInfo)
    Banner bannerArticleInfo;
    @BindView(R.id.rvArticleInfoImg)
    RecyclerView rvArticleInfoImg;
    @BindView(R.id.tvArticleCdsName)
    TextView tvArticleCdsName;
    @BindView(R.id.tvArticleCdsContent)
    TextView tvArticleCdsContent;

    private CommonAdapter articleInfoImgAdapter;
    private ArrayList<String> imgs = new ArrayList<String>();
    private String id, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("物品详情");
        initArticleInfoImg();

        if (getIntent().getStringExtra("id") != null
                && getIntent().getStringExtra("type") != null) {
            id = getIntent().getStringExtra("id");
            type = getIntent().getStringExtra("type");
            switch (type) {
                case "PersonalInformationActivity":
                    getSysCdsByIdHttp();
                    break;
                case "ArticleActivity":
                    getUserCdsByIdHttp();
                    break;
            }
        }
    }

    /**
     * 系统物品详情
     */
    private void getSysCdsByIdHttp() {
        Map<String, Object> map = new HashMap<>();
//        map.put("cdid", id);
        map.put("cdid", "044665d64ca544aabb002544a1c26017");
        String params = EncryptionUtil.getParameter(ArticleInfoActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getSysCdsById.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName()+"_getSysCdsById")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ArticleInfoActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ArticleInfoActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SysCdsByIdResult sysCdsByIdResult =
                                (SysCdsByIdResult) GsonUtil.json2Object(response, SysCdsByIdResult.class);
                        if (sysCdsByIdResult != null
                                && sysCdsByIdResult.getRet().equals("1")) {
                            initViewData(sysCdsByIdResult.getResult().get(0));
                        }
                    }
                });
    }

    /**
     * 用户物品详情
     */
    private void getUserCdsByIdHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("usercdid", id);
//        map.put("cdid", "044665d64ca544aabb002544a1c26017");
        String params = EncryptionUtil.getParameter(ArticleInfoActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserCdById.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getUserCdById")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ArticleInfoActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ArticleInfoActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        UserCdByIdResult userCdByIdResult =
                                (UserCdByIdResult) GsonUtil.json2Object(response, UserCdByIdResult.class);
                        if (userCdByIdResult != null
                                && userCdByIdResult.getRet().equals("1")) {
                            initViewUserData(userCdByIdResult.getResult());
                        }
                    }
                });
    }

    private void initViewData(SysCdsByIdResult.ResultBean sysCds) {
        tvArticleCdsName.setText(sysCds.getName());
        tvArticleCdsContent.setText(sysCds.getDetail());
        imgs.clear();
        for (SysCdsByIdResult.ResultBean.ImgListBean ib : sysCds.getImgList()) {
            imgs.add(ib.getPicpath());
        }
        articleInfoImgAdapter.getDatas().clear();
        articleInfoImgAdapter.getDatas().addAll(imgs);
        articleInfoImgAdapter.notifyDataSetChanged();
        initBanner();
    }

    private void initViewUserData(UserCdByIdResult.ResultBean userCds) {
        tvArticleCdsName.setText("名称");
        tvArticleCdsContent.setText(userCds.getUserselfinfo());
        imgs.clear();
        for (UserCdByIdResult.ResultBean.ImgListBean ib : userCds.getImgList()) {
            imgs.add(ib.getPicpath());
        }
        articleInfoImgAdapter.getDatas().clear();
        articleInfoImgAdapter.getDatas().addAll(imgs);
        articleInfoImgAdapter.notifyDataSetChanged();
        initBanner();
    }

    /**
     * 加载图片轮播器
     */
    private void initBanner() {
        final ArrayList<String> banners = new ArrayList<>();
        banners.clear();
        if (imgs.size() < 3) {
            for (int i = 0; i < imgs.size(); i++) {
                banners.add(imgs.get(i));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                banners.add(imgs.get(i));
            }
        }

        //设置banner样式
        bannerArticleInfo.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        bannerArticleInfo.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        bannerArticleInfo.setBannerAnimation(Transformer.Default);
        //设置是否允许手动滑动轮播图
        bannerArticleInfo.setViewPagerIsScroll(true);
        //设置自动轮播，默认为true
        bannerArticleInfo.isAutoPlay(true);
        //设置轮播时间
        bannerArticleInfo.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        bannerArticleInfo.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片集合
        bannerArticleInfo.setImages(banners);
        //banner设置方法全部调用完毕时最后调用
        bannerArticleInfo.start();
        //开始轮播
        bannerArticleInfo.startAutoPlay();
        //点击事件监听
        bannerArticleInfo.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                showGallery(banners, position);
            }
        });
    }

    /**
     * 加载图片数据
     */
    private void initArticleInfoImg() {
        rvArticleInfoImg.setLayoutManager(new GridLayoutManager(this, 4));
        rvArticleInfoImg.setAdapter(articleInfoImgAdapter = new CommonAdapter<String>(this,
                R.layout.item_article_info_img, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(ArticleInfoActivity.this) / 4 - 20,
                        Utils.getScreenWidth(ArticleInfoActivity.this) / 4 - 20
                );
                lp.setMargins(2, 2, 2, 2);
                ImageView ivShow = (ImageView) holder.getView(R.id.ivArticleInfoPhoto);
                ivShow.setLayoutParams(lp);
                if(!TextUtils.isEmpty(item)){
                    Glide.with(ArticleInfoActivity.this)
                            .load(item)
                            .into(ivShow);
                }else{
                    holder.setImageResource(R.id.ivArticleInfoPhoto, R.drawable.placeholder);
                }

            }
        });
        articleInfoImgAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showGallery(imgs, position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 展示大图画廊
     */
    private void showGallery(ArrayList<String> imgs, int position) {
        Album.gallery(this)
                .requestCode(100) // 请求码，会在listener中返回。
                .checkedList(imgs) // 要浏览的图片列表：ArrayList<String>。
                .currentPosition(position)
                .navigationAlpha(250) // Android5.0+的虚拟导航栏的透明度。
                .checkable(false) // 是否有浏览时的选择功能。
                .widget(
                        Widget.newDarkBuilder(ArticleInfoActivity.this)
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void articleInfoBack() {
        ArticleInfoActivity.this.finish();
    }
}
