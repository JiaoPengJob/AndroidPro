package com.tch.kuwanx.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.https.MyApiResult;
import com.tch.kuwanx.result.GoodAdResult;
import com.tch.kuwanx.result.SellHotGoodResult;
import com.tch.kuwanx.result.StoreThreeImgsResult;
import com.tch.kuwanx.result.StoreTopThreeResult;
import com.tch.kuwanx.utils.GlideRoundedCornersImageLoader;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.EasyIndicator;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.BaseSubscriber;
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
import io.reactivex.Observable;

/**
 * Created by Jiaop on 2017/10/24.
 * 商城
 */
public class StoreFragment extends Fragment implements EasyIndicator.onTabClickListener, OnRefreshListener, OnLoadmoreListener {

    @BindView(R.id.indicatorStore)
    EasyIndicator indicatorStore;
    @BindView(R.id.bannerStore)
    Banner bannerStore;
    @BindView(R.id.llStoreCenterView)
    LinearLayout llStoreCenterView;
    @BindView(R.id.rvStoreList)
    RecyclerView rvStoreList;
    @BindView(R.id.ivStoreShow)
    ImageView ivStoreShow;
    @BindView(R.id.llStoreNextContent)
    LinearLayout llStoreNextContent;
    @BindView(R.id.ivStoreShowThird)
    ImageView ivStoreShowThird;
    @BindView(R.id.ivStoreShowSecond)
    ImageView ivStoreShowSecond;
    @BindView(R.id.rvStoreListSecond)
    RecyclerView rvStoreListSecond;
    @BindView(R.id.rvStoreListThird)
    RecyclerView rvStoreListThird;
    @BindView(R.id.refreshStoreF)
    SmartRefreshLayout refreshStoreF;

    private View viewRoot;
    private String[] mTitles = {"首页", "游戏设备", "游戏光盘", "游戏周边"};
    private CommonAdapter storeAdapter;
    private boolean isMore = false;

    public static StoreFragment getInstance() {
        StoreFragment sf = new StoreFragment();
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_store, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        setIndicatorTab();
        initStoreListData();

        initStoreCdListData();
        initStoreRimListData();
        refreshStoreF.setOnRefreshListener(this);
        refreshStoreF.setOnLoadmoreListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getGoodAdHttp("1");
        mergeHttp();
        getSellThreeHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        if (position == 0) {
            llStoreCenterView.setVisibility(View.VISIBLE);
            llStoreNextContent.setVisibility(View.VISIBLE);
            mergeHttp();
            getSellThreeHttp();
        } else {
            llStoreCenterView.setVisibility(View.GONE);
            llStoreNextContent.setVisibility(View.GONE);
            getGoodListHttp(position + "0");
        }
        getGoodAdHttp(String.valueOf(position + 1));
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        if (position == 0) {
            llStoreCenterView.setVisibility(View.VISIBLE);
            llStoreNextContent.setVisibility(View.VISIBLE);
            mergeHttp();
            getSellThreeHttp();
        } else {
            llStoreCenterView.setVisibility(View.GONE);
            llStoreNextContent.setVisibility(View.GONE);
            getGoodListHttp(position + "0");
        }
        getGoodAdHttp(String.valueOf(position + 1));
    }

    @BindView(R.id.tvStoreCenterLeftTitle)
    TextView tvStoreCenterLeftTitle;
    @BindView(R.id.tvStoreCenterLeftNewPrice)
    TextView tvStoreCenterLeftNewPrice;
    @BindView(R.id.tvStoreCenterLeftOldPrice)
    TextView tvStoreCenterLeftOldPrice;
    @BindView(R.id.ivStoreCenterLeftImg)
    ImageView ivStoreCenterLeftImg;
    @BindView(R.id.tvStoreCenterRTTitle)
    TextView tvStoreCenterRTTitle;
    @BindView(R.id.tvStoreCenterRTNewPrice)
    TextView tvStoreCenterRTNewPrice;
    @BindView(R.id.tvStoreCenterRTOldPrice)
    TextView tvStoreCenterRTOldPrice;
    @BindView(R.id.ivStoreCenterRTImg)
    ImageView ivStoreCenterRTImg;
    @BindView(R.id.tvStoreCenterRBTitle)
    TextView tvStoreCenterRBTitle;
    @BindView(R.id.tvStoreCenterRBNewPrice)
    TextView tvStoreCenterRBNewPrice;
    @BindView(R.id.tvStoreCenterRBOldPrice)
    TextView tvStoreCenterRBOldPrice;
    @BindView(R.id.ivStoreCenterRBImg)
    ImageView ivStoreCenterRBImg;

    /**
     * 设置中间三个item数据
     */
    private void initCenterData(StoreTopThreeResult topThree) {
        tvStoreCenterLeftTitle.setText(topThree.getResult().get(0).getGood_name());
        //中间左侧新价格
        tvStoreCenterLeftNewPrice.setText("¥" + topThree.getResult().get(0).getCurrent_price());
        //中间左侧旧价格
        tvStoreCenterLeftOldPrice.setText("¥" + topThree.getResult().get(0).getCost_price());
        //中间左侧图片
        Glide.with(this)
                .load(topThree.getResult().get(0).getGood_cover())
                .into(ivStoreCenterLeftImg);

        tvStoreCenterRTTitle.setText(topThree.getResult().get(1).getGood_name());
        //中间右侧上面新价格
        tvStoreCenterRTNewPrice.setText("¥" + topThree.getResult().get(1).getCurrent_price());
        //中间右侧上面旧价格
        tvStoreCenterRTOldPrice.setText("¥" + topThree.getResult().get(1).getCost_price());
        //中间右侧上面图片
        Glide.with(this)
                .load(topThree.getResult().get(1).getGood_cover())
                .into(ivStoreCenterRTImg);

        tvStoreCenterRBTitle.setText(topThree.getResult().get(2).getGood_name());
        //中间右侧下面新价格
        tvStoreCenterRBNewPrice.setText("¥" + topThree.getResult().get(2).getCurrent_price());
        //中间右侧下面旧价格
        tvStoreCenterRBOldPrice.setText("¥" + topThree.getResult().get(2).getCost_price());
        //中间右侧下面图片
        Glide.with(this)
                .load(topThree.getResult().get(2).getGood_cover())
                .into(ivStoreCenterRBImg);
    }

    /**
     * 加载列表数据
     */
    private void initStoreListData() {
        rvStoreList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvStoreList.setAdapter(storeAdapter = new CommonAdapter<SellHotGoodResult.ResultBean>(getActivity(),
                R.layout.item_store_list, new ArrayList<SellHotGoodResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, SellHotGoodResult.ResultBean item, int position) {
                Glide.with(getActivity())
                        .load(item.getGood_cover())
                        .into((ImageView) holder.getView(R.id.ivStoreListImg));
                holder.setText(R.id.tvStoreListTitle, item.getGood_name());
                holder.setText(R.id.tvStoreListNewPrice, item.getCurrent_price());
                holder.setText(R.id.tvStoreListOldPrice, item.getCost_price());
            }
        });
        storeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("goodId",
                        ((List<SellHotGoodResult.ResultBean>) storeAdapter.getDatas()).get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CommonAdapter storeCdAdapter;

    /**
     * 加载游戏光盘列表数据
     */
    private void initStoreCdListData() {
        rvStoreListSecond.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvStoreListSecond.setAdapter(storeCdAdapter = new CommonAdapter<SellHotGoodResult.ResultBean>(getActivity(),
                R.layout.item_store_list, new ArrayList<SellHotGoodResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, SellHotGoodResult.ResultBean item, int position) {
                Glide.with(getActivity())
                        .load(item.getGood_cover())
                        .into((ImageView) holder.getView(R.id.ivStoreListImg));
                holder.setText(R.id.tvStoreListTitle, item.getGood_name());
                holder.setText(R.id.tvStoreListNewPrice, item.getCurrent_price());
                holder.setText(R.id.tvStoreListOldPrice, item.getCost_price());
            }
        });
        storeCdAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("goodId",
                        ((List<SellHotGoodResult.ResultBean>) storeCdAdapter.getDatas()).get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CommonAdapter storeRimAdapter;

    /**
     * 加载游戏光盘列表数据
     */
    private void initStoreRimListData() {
        rvStoreListThird.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvStoreListThird.setAdapter(storeRimAdapter = new CommonAdapter<SellHotGoodResult.ResultBean>(getActivity(),
                R.layout.item_store_list, new ArrayList<SellHotGoodResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, SellHotGoodResult.ResultBean item, int position) {
                Glide.with(getActivity())
                        .load(item.getGood_cover())
                        .into((ImageView) holder.getView(R.id.ivStoreListImg));
                holder.setText(R.id.tvStoreListTitle, item.getGood_name());
                holder.setText(R.id.tvStoreListNewPrice, item.getCurrent_price());
                holder.setText(R.id.tvStoreListOldPrice, item.getCost_price());
            }
        });
        storeRimAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("goodId",
                        ((List<SellHotGoodResult.ResultBean>) storeRimAdapter.getDatas()).get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 加载标题栏
     */
    private void setIndicatorTab() {
        indicatorStore.setTabTitles(mTitles);
        indicatorStore.setOnTabClickListener(this);
    }

    private int position = 0;

    @Override
    public void onTabClick(String s, int i) {
        position = i;
        if (i == 0) {
            llStoreCenterView.setVisibility(View.VISIBLE);
            llStoreNextContent.setVisibility(View.VISIBLE);
            mergeHttp();
            getSellThreeHttp();
        } else {
            llStoreCenterView.setVisibility(View.GONE);
            llStoreNextContent.setVisibility(View.GONE);
            getGoodListHttp(i + "0");
        }
        getGoodAdHttp(String.valueOf(i + 1));
    }

    /**
     * 加载上边图片轮播器
     */
    private void initBanner(List<String> imgs) {
        //设置banner样式
        bannerStore.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        bannerStore.setImageLoader(new GlideRoundedCornersImageLoader());
        //设置banner动画效果
        bannerStore.setBannerAnimation(Transformer.Default);
        //设置是否允许手动滑动轮播图
        bannerStore.setViewPagerIsScroll(true);
        //设置自动轮播，默认为true
        bannerStore.isAutoPlay(true);
        //设置轮播时间
        bannerStore.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        bannerStore.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片集合
        bannerStore.setImages(imgs);
        //banner设置方法全部调用完毕时最后调用
        bannerStore.start();
        //开始轮播
        bannerStore.startAutoPlay();
        //点击事件监听
        bannerStore.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }

    /**
     * 搜索
     */
    @OnClick(R.id.ibStoreSearch)
    public void storeSearch() {
        Intent intent = new Intent(getActivity(), StoreSearchActivity.class);
        startActivity(intent);
    }

    /**
     * 购物车
     */
    @OnClick(R.id.ibStoreShopCart)
    public void storeShopCart() {
        Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.llRecommendArticleFirst, R.id.llRecommendArticleSecond, R.id.llRecommendArticleThird})
    public void recommendArticle(View view) {
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        switch (view.getId()) {
            case R.id.llRecommendArticleFirst:
                intent.putExtra("goodId", storeTopThreeResult.getResult().get(0).getId());
                break;
            case R.id.llRecommendArticleSecond:
                intent.putExtra("goodId", storeTopThreeResult.getResult().get(1).getId());
                break;
            case R.id.llRecommendArticleThird:
                intent.putExtra("goodId", storeTopThreeResult.getResult().get(2).getId());
                break;
        }
        startActivity(intent);
    }

    /**
     * 合并请求
     */
    private void mergeHttp() {
        //商城首页-三个宣传图(游戏设备,游戏光盘,游戏周边)
        Map<String, Object> map1 = new HashMap<>();
        map1.put("a", "1");
        String params1 = EncryptionUtil.getParameter(getActivity(), map1);
        Observable<String> exchangeListObservable = EasyHttp.post(HttpUtils.URI_CENTER + "good/getGoodThreeBanner.jhtml")
                .params("data", params1)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGoodThreeBanner")
                .cacheTime(2)
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        //商城首页-三个宣传图对应的热销排行榜
        Map<String, Object> map2 = new HashMap<>();
        map2.put("good_type_id", "10");
        String params2 = EncryptionUtil.getParameter(getActivity(), map2);
        Observable<String> sellHotGoodObservable = EasyHttp.post(HttpUtils.URI_CENTER + "good/getSellHotGood.jhtml")
                .params("data", params2)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getSellHotGood")
                .cacheTime(2)
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        Observable.mergeDelayError(exchangeListObservable, sellHotGoodObservable)
                .subscribe(new BaseSubscriber<Object>() {
                    @Override
                    public void onError(ApiException e) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }
                    }

                    @Override
                    public void onNext(@NonNull Object object) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }

                        String result = object.toString();
                        if (result.indexOf("banner_three") != -1) {
                            StoreThreeImgsResult storeThreeImgsResult =
                                    (StoreThreeImgsResult) GsonUtil.json2Object(result, StoreThreeImgsResult.class);
                            if (storeThreeImgsResult != null
                                    && storeThreeImgsResult.getRet().equals("1")) {
                                Glide.with(getActivity())
                                        .load(storeThreeImgsResult.getResult().get(0).getBanner_one())
                                        .into(ivStoreShow);
                                Glide.with(getActivity())
                                        .load(storeThreeImgsResult.getResult().get(0).getBanner_two())
                                        .into(ivStoreShowSecond);
                                Glide.with(getActivity())
                                        .load(storeThreeImgsResult.getResult().get(0).getBanner_three())
                                        .into(ivStoreShowThird);
                            }
                        } else if (result.indexOf("good_type_id") != -1) {
                            SellHotGoodResult sellHotGoodResult =
                                    (SellHotGoodResult) GsonUtil.json2Object(result, SellHotGoodResult.class);
                            if (sellHotGoodResult != null
                                    && sellHotGoodResult.getRet().equals("1")) {
                                storeAdapter.getDatas().clear();
                                storeCdAdapter.getDatas().clear();
                                storeRimAdapter.getDatas().clear();
                                for (SellHotGoodResult.ResultBean item : sellHotGoodResult.getResult()) {
                                    if (item.getGood_type_id().equals("10")) {
                                        storeAdapter.getDatas().add(item);
                                    } else if (item.getGood_type_id().equals("20")) {
                                        storeCdAdapter.getDatas().add(item);
                                    } else if (item.getGood_type_id().equals("30")) {
                                        storeRimAdapter.getDatas().add(item);
                                    }
                                }
                                storeAdapter.notifyDataSetChanged();
                                storeCdAdapter.notifyDataSetChanged();
                                storeRimAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    /**
     * 商城-获取Banner
     */
    private void getGoodAdHttp(String bannerIndex) {
        Map<String, Object> map = new HashMap<>();
        map.put("ad_type", bannerIndex);
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getGoodAd.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGoodAd")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }

                        Logger.e("获取Banner失败");
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }

                        GoodAdResult goodAdResult =
                                (GoodAdResult) GsonUtil.json2Object(response, GoodAdResult.class);
                        if (goodAdResult != null
                                && goodAdResult.getRet().equals("1")) {
                            List<String> imgs = new ArrayList<>();
                            imgs.clear();
                            for (GoodAdResult.ResultBean item : goodAdResult.getResult()) {
                                imgs.add(item.getImg_url());
                            }
                            initBanner(imgs);
                        }
                    }
                });
    }

    /**
     * 商城-热销商品top3
     */
    private void getSellThreeHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getSellThree.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getSellThree")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }

                        Logger.e("获取Top3失败");
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }

                        storeTopThreeResult =
                                (StoreTopThreeResult) GsonUtil.json2Object(response, StoreTopThreeResult.class);
                        if (storeTopThreeResult != null
                                && storeTopThreeResult.getRet().equals("1")) {
                            initCenterData(storeTopThreeResult);
                        }
                    }
                });
    }

    private StoreTopThreeResult storeTopThreeResult;

    private int index = 0;
    private int size = 10;

    /**
     * 商城 - 获取商品列表
     */
    private void getGoodListHttp(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("good_type_id", type);
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getGoodList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGoodList")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }

                        Logger.e("获取商品列表失败");
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshStoreF != null) {
                            refreshStoreF.finishLoadmore();
                            refreshStoreF.finishRefresh();
                        }

                        SellHotGoodResult sellHotGoodResult =
                                (SellHotGoodResult) GsonUtil.json2Object(response, SellHotGoodResult.class);
                        if (sellHotGoodResult != null
                                && sellHotGoodResult.getRet().equals("1")) {
                            if (isMore) {
                                storeAdapter.getDatas().addAll(sellHotGoodResult.getResult());
                                storeAdapter.notifyDataSetChanged();
                            } else {
                                storeAdapter.getDatas().clear();
                                storeAdapter.getDatas().addAll(sellHotGoodResult.getResult());
                                storeAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
