package com.tch.kuwanx.ui.store;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.GoodCommentsResult;
import com.tch.kuwanx.result.GoodDetailResult;
import com.tch.kuwanx.utils.GlideImageLoader;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.tch.kuwanx.view.MixtureTextView;
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
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.ParseException;
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
 * Created by Jiaop on 2017/10/24.
 * 商品
 */
public class ProductFragment extends Fragment implements OnRefreshListener {

    @BindView(R.id.productBanner)
    Banner productBanner;
    @BindView(R.id.rvProductComments)
    RecyclerView rvProductComments;
    @BindView(R.id.tvNorm)
    TextView tvNorm;
    @BindView(R.id.tvProFgTitle)
    MixtureTextView tvProFgTitle;
    @BindView(R.id.ivProFgTitleImg)
    ImageView ivProFgTitleImg;
    @BindView(R.id.tvProFgAddContent)
    TextView tvProFgAddContent;
    @BindView(R.id.tvProFgNewPrice)
    TextView tvProFgNewPrice;
    @BindView(R.id.tvProFgOldPrice)
    TextView tvProFgOldPrice;
    @BindView(R.id.tvProFgEmsMsg)
    TextView tvProFgEmsMsg;
    @BindView(R.id.tvProFgSellNum)
    TextView tvProFgSellNum;
    @BindView(R.id.tvProFgContent)
    TextView tvProFgContent;
    @BindView(R.id.tvProFgCommentNum)
    TextView tvProFgCommentNum;
    @BindView(R.id.refreshProductF)
    SmartRefreshLayout refreshProductF;

    private View viewRoot;
    private CommonAdapter productCommentsAdapter;
    private String goodId;

    public static ProductFragment getInstance(String goodId) {
        ProductFragment sf = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("goodId", goodId);
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_product, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        if (getArguments().getString("goodId") != null) {
            goodId = getArguments().getString("goodId");
        }
        initInfo();
        initProductComments();
        refreshProductF.setEnableLoadmore(false);
        refreshProductF.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getGoodDetailHttp();
        goodCommentsHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getGoodDetailHttp();
        goodCommentsHttp();
    }

    /**
     * 加载数据
     */
    private void initInfo() {
        tvProFgTitle.setText("标题");
        ivProFgTitleImg.setImageResource(R.drawable.the_new_product);
        tvProFgAddContent.setText("加送内容");
        tvProFgNewPrice.setText("¥" + "66.66");
        tvProFgOldPrice.setText("¥" + "88.88");
        tvProFgEmsMsg.setText("全场满500包邮（偏远地区除外）");
        tvProFgSellNum.setText("月销量" + "6666" + "件");
        tvProFgContent.setText("商品介绍");
    }

    private RecyclerView rvProComImgs;

    /**
     * 加载三个评论数据
     */
    private void initProductComments() {
        rvProductComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductComments.setAdapter(productCommentsAdapter = new CommonAdapter<GoodCommentsResult.ResultBean>(getActivity(),
                R.layout.item_product_comments, new ArrayList<GoodCommentsResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, GoodCommentsResult.ResultBean item, int position) {
                rvProComImgs = (RecyclerView) holder.getView(R.id.rvProComImgs);
                initHomeChangeImgs(rvProComImgs);

                Glide.with(getActivity())
                        .load("http://img17.3lian.com/d/file/201701/16/779db6efe9d4520e07e8bfb8b9e55175.jpg")
                        .apply(bitmapTransform(new CropCircleTransformation()))
                        .into((ImageView) holder.getView(R.id.ivProComItemPhoto));
                holder.setText(R.id.tvProComItemName, item.getNickname());
                try {
                    holder.setText(R.id.tvProComItemDate, Utils.longToString(Long.parseLong(item.getComm_createtime()), "yyyy-MM-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.setText(R.id.tvProComItemNorm, "规格");
                holder.setText(R.id.tvProComItemContent, item.getComm_content());
            }
        });
    }

    /**
     * 加载显示的图片
     *
     * @param view
     */
    private void initHomeChangeImgs(RecyclerView view) {
        final ArrayList<String> itemImgs = new ArrayList<String>();
        CommonAdapter homeChangeImgsAdapter;
        for (int i = 0; i < 8; i++) {
            itemImgs.add("http://img2.imgtn.bdimg.com/it/u=4215897016,3222246668&fm=27&gp=0.jpg");
        }
        view.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        view.setAdapter(homeChangeImgsAdapter = new CommonAdapter<String>(getActivity(), R.layout.img, itemImgs) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(getActivity()) / 3,
                        Utils.getScreenWidth(getActivity()) / 3
                );
                lp.setMargins(0, 0, 0, 0);
                ImageView ivPhoto = (ImageView) holder.getView(R.id.ivPhoto);
                ivPhoto.setLayoutParams(lp);
                Glide.with(getActivity())
                        .load(item)
                        .into(ivPhoto);
            }
        });
        homeChangeImgsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showGallery(itemImgs, position);
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
    private void showGallery(ArrayList<String> list, int position) {
        Album.gallery(this)
                .requestCode(100)
                .checkedList(list)
                .currentPosition(position)
                .navigationAlpha(250)
                .checkable(false)
                .widget(
                        Widget.newDarkBuilder(getActivity())
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 加载图片轮播器
     */
    private void initBanner(List<GoodDetailResult.ResultBean.ImgListBean> imgList) {
        //测试数据
        List<String> imgs = new ArrayList<>();
        imgs.clear();
        imgs.add(imgList.get(0).getBanner_one());
        imgs.add(imgList.get(0).getBanner_two());
        imgs.add(imgList.get(0).getBanner_three());

        productBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        productBanner.setImageLoader(new GlideImageLoader());
        productBanner.setBannerAnimation(Transformer.Default);
        productBanner.setViewPagerIsScroll(true);
        productBanner.isAutoPlay(true);
        productBanner.setDelayTime(1500);
        productBanner.setIndicatorGravity(BannerConfig.CENTER);
        productBanner.setImages(imgs);
        productBanner.start();
        productBanner.startAutoPlay();
        //点击事件监听
        productBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }

    /**
     * 选择规格
     */
    @OnClick(R.id.rlNorm)
    public void norm() {
        showNormPop();
    }

    private CustomPopWindow mNormPop;
    private TagFlowLayout flowLayout;
    private List<String> flowList;
    private TextView tvTabShow, tvArticleName, tvArticleNorm;
    private RoundedImageView rivArticlePhoto;
    private ImageButton ibArticlePlus, ibArticleMinus;
    private EditText etArticleNum;
    private Button btArticleBuy;
    public int articleNum = 1;

    public int getArticleNum() {
        return articleNum;
    }

    public void showNormPop() {
        flowList = new ArrayList<>();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_norm, null);
        rivArticlePhoto = (RoundedImageView) view.findViewById(R.id.rivArticlePhoto);
        tvArticleName = (TextView) view.findViewById(R.id.tvArticleName);
        tvArticleNorm = (TextView) view.findViewById(R.id.tvArticleNorm);
        ibArticlePlus = (ImageButton) view.findViewById(R.id.ibArticlePlus);
        ibArticleMinus = (ImageButton) view.findViewById(R.id.ibArticleMinus);
        etArticleNum = (EditText) view.findViewById(R.id.etArticleNum);
        btArticleBuy = (Button) view.findViewById(R.id.btArticleBuy);
        flowLayout = (TagFlowLayout) view.findViewById(R.id.flowLayout);
        ibArticlePlus.setOnClickListener(new NormClickListener());
        ibArticleMinus.setOnClickListener(new NormClickListener());
        btArticleBuy.setOnClickListener(new NormClickListener());
        etArticleNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    etArticleNum.setText("1");
                } else {
                    articleNum = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Glide.with(getActivity())
                .load(goodDetailResult.getResult().getGood_cover())
                .into(rivArticlePhoto);
        etArticleNum.setText(String.valueOf(articleNum));
        tvArticleNorm.setText(goodDetailResult.getResult().getSpec());
//        initFlowLayoutData();
        mNormPop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(((ProductDetailsActivity) getActivity()).getRlProductDetailsParent(), Gravity.BOTTOM, 0, 0);
    }

    private class NormClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ibArticlePlus:
                    etArticleNum.setText(String.valueOf(++articleNum));
                    break;
                case R.id.ibArticleMinus:
                    if (articleNum > 1) {
                        etArticleNum.setText(String.valueOf(--articleNum));
                    }
                    break;
                case R.id.btArticleBuy:
                    if (mNormPop != null) {
                        mNormPop.dissmiss();
                    }
                    break;
            }
        }
    }

    private String normSelect;

    /**
     * 加载流式布局
     */
    private void initFlowLayoutData() {
        flowList.add("字大大大");
        flowList.add("字大大撒多");
        flowList.add("字大");
        flowList.add("字大大");
        flowList.add("字大大");
        flowList.add("字大大撒多撒多撒");
        flowLayout.setAdapter(new TagAdapter<String>(flowList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                tvTabShow = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tv, flowLayout, false);
                tvTabShow.setText(s);
                return tvTabShow;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                ((TextView) view).setTextColor(Color.parseColor("#666666"));
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Logger.wtf("setOnTagClickListener选中项：" + flowList.get(position));
                normSelect = flowList.get(position);
                return true;
            }
        });
    }

    /**
     * 评论
     */
    @OnClick({R.id.rlProductReview, R.id.tvSeeComments})
    public void productReview() {
        ((ProductDetailsActivity) getActivity()).getProductIndicator().onPageSelected(2);
        ((ProductDetailsActivity) getActivity()).getProductViewPager().setCurrentItem(2);
    }

    public TextView getTvNorm() {
        return tvNorm;
    }

    /**
     * 商品页/商品详情
     */
    private void getGoodDetailHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("good_id", goodId);
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getGoodDetail.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGoodDetail")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        getActivity(), "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshProductF != null) {
                            refreshProductF.finishLoadmore();
                            refreshProductF.finishRefresh();
                        }

                        Toasty.warning(getActivity(), "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshProductF != null) {
                            refreshProductF.finishLoadmore();
                            refreshProductF.finishRefresh();
                        }

                        goodDetailResult =
                                (GoodDetailResult) GsonUtil.json2Object(response, GoodDetailResult.class);
                        if (goodDetailResult != null
                                && goodDetailResult.getRet().equals("1")) {
                            initHttpInfo(goodDetailResult.getResult());
                            initBanner(goodDetailResult.getResult().getImgList());
                        } else {
                            Toasty.warning(getActivity(), "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private GoodDetailResult goodDetailResult;

    /**
     * 加载服务器数据
     */
    private void initHttpInfo(GoodDetailResult.ResultBean info) {
        tvProFgTitle.setText(info.getGood_name());
        ivProFgTitleImg.setImageResource(R.drawable.the_new_product);
        tvProFgAddContent.setText(info.getGood_desc());
        tvProFgNewPrice.setText("¥" + info.getCurrent_price());
        tvProFgOldPrice.setText("¥" + info.getCost_price());
        tvProFgEmsMsg.setText("全场满500包邮（偏远地区除外）");
        tvProFgSellNum.setText("月销量" + info.getSale_num() + "件");
        tvProFgContent.setText(info.getGood_intr());
        tvNorm.setText(info.getSpec());
    }

    /**
     * 商品的评价列表
     */
    private void goodCommentsHttp() {
        Map<String, Object> map = new HashMap<>();
//        map.put("good_id", goodId);
        map.put("good_id", "ea0a10ba182f4983a5968b5909486a0d");
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getGoodComments.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGoodComments")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        getActivity(), "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshProductF != null) {
                            refreshProductF.finishLoadmore();
                            refreshProductF.finishRefresh();
                        }

                        Toasty.warning(getActivity(), "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshProductF != null) {
                            refreshProductF.finishLoadmore();
                            refreshProductF.finishRefresh();
                        }

                        GoodCommentsResult goodCommentsResult =
                                (GoodCommentsResult) GsonUtil.json2Object(response, GoodCommentsResult.class);
                        if (goodCommentsResult != null
                                && goodCommentsResult.getRet().equals("1")) {
                            tvProFgCommentNum.setText("（" + goodCommentsResult.getResult().size() + ")");
                            List<GoodCommentsResult.ResultBean> list =
                                    new ArrayList<>();
                            if (goodCommentsResult.getResult().size() < 2) {
                                list.add(goodCommentsResult.getResult().get(0));
                            } else if (goodCommentsResult.getResult().size() < 3) {
                                list.add(goodCommentsResult.getResult().get(0));
                                list.add(goodCommentsResult.getResult().get(1));
                            } else {
                                for (int i = 0; i < 3; i++) {
                                    list.add(goodCommentsResult.getResult().get(i));
                                }
                            }
                            productCommentsAdapter.getDatas().clear();
                            productCommentsAdapter.getDatas().addAll(list);
                            productCommentsAdapter.notifyDataSetChanged();
                        } else {
                            Toasty.warning(getActivity(), "获取失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
