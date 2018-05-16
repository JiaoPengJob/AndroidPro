package com.tch.kuwanx.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.https.MyApiResult;
import com.tch.kuwanx.listener.ActionBarClickListener;
import com.tch.kuwanx.result.ExchangeListResult;
import com.tch.kuwanx.result.IndexCarouselResult;
import com.tch.kuwanx.result.SwapPostUpResult;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GlideImageLoader;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.tch.kuwanx.view.EasyIndicator;
import com.tch.kuwanx.view.TranslucentActionBar;
import com.tch.kuwanx.view.TranslucentScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
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
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Jiaop on 2017/10/24.
 * 主页
 */
public class HomeFragment extends Fragment implements ActionBarClickListener, TranslucentScrollView.TranslucentChangedListener,
        TranslucentScrollView.OnScrollListener, EasyIndicator.onTabClickListener, OnRefreshLoadmoreListener {

    @BindView(R.id.rootScrollView)
    TranslucentScrollView rootScrollView;
    @BindView(R.id.zoomView)
    LinearLayout zoomView;
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rvCardShow)
    RecyclerView rvCardShow;
    @BindView(R.id.homeIndicator)
    EasyIndicator homeIndicator;
    @BindView(R.id.rvChangeData)
    RecyclerView rvChangeData;
    @BindView(R.id.topView)
    LinearLayout topView;
    @BindView(R.id.llCenterView)
    LinearLayout llCenterView;
    @BindView(R.id.llCenterViewBottom)
    LinearLayout llCenterViewBottom;
    @BindView(R.id.homeIndicatorBottom)
    EasyIndicator homeIndicatorBottom;
    @BindView(R.id.viewHomeFg)
    View viewHomeFg;
    @BindView(R.id.rlHomeFg)
    RelativeLayout rlHomeFg;
    @BindView(R.id.refreshHome)
    SmartRefreshLayout refreshHome;

    private View viewRoot;
    private CommonAdapter cardViewAdapter;
    private CommonAdapter changeAdapter;
    private String[] mTitles = {"换回", "不换回"};
    private int layoutHeight;
    private Intent intent;
    private boolean isMore = false;

    public static HomeFragment getInstance() {
        HomeFragment sf = new HomeFragment();
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        initRootView();
        setRvCardShow();
        setChangeTab();
        initSuspensionEffect();
        setChangeData();
        refreshHome.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mergeHttp();
    }

    /**
     * 上拉加载
     *
     * @param refreshlayout
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        exchangeList_pageIndex += 1;
        mergeHttp();
    }

    /**
     * 下拉刷新
     *
     * @param refreshlayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        exchangeList_pageIndex = 1;
        mergeHttp();
    }

    private int exchangeList_pageSize = 10;
    private int exchangeList_pageIndex = 1;

    /**
     * 合并请求
     */
    private void mergeHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        String params = EncryptionUtil.getParameter(getActivity(), map);
        //首页轮播图
        Observable<String> carouselObservable = EasyHttp.post(HttpUtils.URI_CENTER + "index/indexCarousel.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName() + "_indexCarousel")
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        //首页换回不换回列表
        Map<String, Object> map1 = new HashMap<>();
        map1.put("swap_type", tabIndex);
        map1.put("pageSize", String.valueOf(exchangeList_pageSize));
        map1.put("pageIndex", String.valueOf(exchangeList_pageIndex));
        String params1 = EncryptionUtil.getParameter(getActivity(), map1);
        Observable<String> exchangeListObservable = EasyHttp.post(HttpUtils.URI_CENTER + "index/exchangeList.jhtml")
                .params("data", params1)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName() + "_exchangeList")
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        Observable.mergeDelayError(exchangeListObservable, carouselObservable).subscribe(new BaseSubscriber<Object>() {
            @Override
            public void onError(ApiException e) {
                if (refreshHome != null) {
                    refreshHome.finishLoadmore();
                    refreshHome.finishRefresh();
                }
            }

            @Override
            public void onNext(@NonNull Object object) {
                String result = object.toString();
                if (result.indexOf("banner_cover") != -1) {
                    IndexCarouselResult indexCarouselResult =
                            (IndexCarouselResult) GsonUtil.json2Object(result, IndexCarouselResult.class);
                    if (indexCarouselResult != null
                            && indexCarouselResult.getRet().equals("1")) {
                        initBanner(indexCarouselResult.getResult());
                    } else {
                        Logger.e("主页Banner图 == " + indexCarouselResult.getMsg());
                    }
                } else if (result.indexOf("swap_cds") != -1) {
                    ExchangeListResult exchangeListResult =
                            (ExchangeListResult) GsonUtil.json2Object(result, ExchangeListResult.class);
                    if (exchangeListResult != null
                            && exchangeListResult.getRet().equals("1")) {
                        if (isMore) {
                            //更多
                            if (refreshHome != null) {
                                refreshHome.finishLoadmore();
                            }
                            changeAdapter.getDatas().addAll(exchangeListResult.getResult());
                            changeAdapter.notifyDataSetChanged();
                        } else {
                            changeAdapter.getDatas().clear();
                            changeAdapter.getDatas().addAll(exchangeListResult.getResult());
                            changeAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if (refreshHome != null) {
                    refreshHome.finishLoadmore();
                    refreshHome.finishRefresh();
                }
            }
        });
    }

    /**
     * 加载滑动容器
     */
    private void initRootView() {
        //初始actionBar
        actionBar.setData("首页", 0, null, 0, null, null);
        //开启渐变
        actionBar.setNeedTranslucent();
        //设置状态栏高度
        actionBar.setStatusBarHeight(Utils.getStatusBarHeight(getActivity()) / 5);
        //设置透明度变化监听
        rootScrollView.setTranslucentChangedListener(this);
        //关联需要渐变的视图
        rootScrollView.setTransView(actionBar);
        //关联伸缩的视图
        rootScrollView.setPullZoomView(zoomView);
    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onTranslucentChanged(int transAlpha) {
        actionBar.tvTitle.setVisibility(transAlpha > 45 ? View.VISIBLE : View.GONE);
    }

    /**
     * 加载图片轮播器
     */
    private void initBanner(final List<IndexCarouselResult.ResultBean> list) {
        List<String> imgs = new ArrayList<>();
        for (IndexCarouselResult.ResultBean rb : list) {
            imgs.add(rb.getBanner_cover());
        }

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置是否允许手动滑动轮播图
        banner.setViewPagerIsScroll(true);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片集合
        banner.setImages(imgs);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        //开始轮播
        banner.startAutoPlay();
        //点击事件监听
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (list.get(position).getBanner_link() != null
                        && Utils.isUri(list.get(position).getBanner_link())) {
                    intent = new Intent(getActivity(), GameCommunityDetailsActivity.class);
                    intent.putExtra("showType", "onlyWeb");
                    intent.putExtra("link", list.get(position).getBanner_link());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 加载卡片布局的显示
     */
    private void setRvCardShow() throws NullPointerException {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCardShow.setLayoutManager(layoutManager);
        rvCardShow.setAdapter(cardViewAdapter = new CommonAdapter<String>(getActivity(),
                R.layout.item_card_view_show,
                new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                Glide.with(getActivity())
                        .load(item)
                        .into((RoundedImageView) holder.getView(R.id.rivCardViewShow));
            }
        });

        cardViewAdapter.getDatas().clear();
        for (int i = 0; i < 5; i++) {
            cardViewAdapter.getDatas().add("");
        }
        cardViewAdapter.notifyDataSetChanged();
    }

    /**
     * 设置换回或不换回tab
     */
    private void setChangeTab() {
        homeIndicator.setTabTitles(mTitles);
        homeIndicator.setOnTabClickListener(this);
        homeIndicatorBottom.setTabTitles(mTitles);
        homeIndicatorBottom.setOnTabClickListener(this);
    }

    private String tabIndex = "1";

    @Override
    public void onTabClick(String s, int i) {
        homeIndicator.onPageSelected(i);
        homeIndicatorBottom.onPageSelected(i);
        tabIndex = String.valueOf(i + 1);
        changesHttp(String.valueOf(i + 1));
    }

    private int changeZan = 2;

    /**
     * 加载底部列表数据
     */
    private void setChangeData() throws NullPointerException {
        rvChangeData.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvChangeData.setAdapter(changeAdapter = new CommonAdapter<ExchangeListResult.ResultBean>(getActivity(),
                R.layout.item_home_change, new ArrayList<ExchangeListResult.ResultBean>()) {
            @Override
            protected void convert(final ViewHolder holder, final ExchangeListResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(getActivity())
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivChangeShow));
                } else {
                    holder.setImageResource(R.id.ivChangeShow, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvChangeName, item.getNickname());
                holder.setText(R.id.tvChangeDate, item.getPublish_time());
                holder.setText(R.id.tvChangeLocation, item.getCds_address());
                if (item.getImgList() != null
                        && item.getImgList().size() == 1) {
                    holder.setVisible(R.id.ivHomeChangeImg, true);
                    holder.setVisible(R.id.rvHomeChangeImgs, false);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            Utils.getScreenWidth(getActivity()),
                            Utils.getScreenWidth(getActivity()) / 3
                    );
                    lp.setMargins(5, 5, 5, 5);
                    ImageView ivPhoto = (ImageView) holder.getView(R.id.ivHomeChangeImg);
                    ivPhoto.setLayoutParams(lp);
                    if (!TextUtils.isEmpty(item.getImgList().get(0).getPicpath())) {
                        Glide.with(getActivity())
                                .load(item.getImgList().get(0).getPicpath())
                                .into(ivPhoto);
                    } else {
                        holder.setImageResource(R.id.ivHomeChangeImg, R.drawable.placeholder);
                    }
                } else if (item.getImgList() != null
                        && item.getImgList().size() > 1) {
                    holder.setVisible(R.id.ivHomeChangeImg, false);
                    holder.setVisible(R.id.rvHomeChangeImgs, true);
                    RecyclerView rvHomeChangeImgs = (RecyclerView) holder.getView(R.id.rvHomeChangeImgs);
                    initHomeChangeImgs(rvHomeChangeImgs, item.getImgList());
                } else if (item.getImgList() != null
                        && item.getImgList().size() < 1) {
                    holder.setVisible(R.id.ivHomeChangeImg, false);
                    holder.setVisible(R.id.rvHomeChangeImgs, false);
                } else {
                    holder.setVisible(R.id.ivHomeChangeImg, false);
                    holder.setVisible(R.id.rvHomeChangeImgs, false);
                }
                holder.setText(R.id.tvHomeChangeContent, item.getMy_cds_description());
                holder.setText(R.id.tvChangeNewPrice, item.getSwap_deposit());
                holder.setText(R.id.tvChangeNeed, item.getSwap_cds());
                holder.setText(R.id.tvChangeZan, "(" + item.getUp_count() + ")");
                holder.setText(R.id.tvChangeComment, "(" + item.getComments_count() + ")");

                if (item.getIsUp().equals("yes")) {
                    holder.setImageResource(R.id.ivChangeZan, R.mipmap.item_zan_sel);
                    holder.setTextColor(R.id.tvChangeZan, Color.parseColor("#FFDA44"));
                    changeZan = 1;
                } else if (item.getIsUp().equals("no")) {
                    holder.setImageResource(R.id.ivChangeZan, R.mipmap.item_zan_unsel);
                    holder.setTextColor(R.id.tvChangeZan, Color.parseColor("#7D7B7B"));
                    changeZan = 2;
                }

                //赞
                holder.setOnClickListener(R.id.ivChangeZan, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (changeZan == 1) {
                            zanDownHttp(item.getId());
                        } else {
                            zanHttp(item.getId());
                        }
                    }
                });
                //评论
                holder.setOnClickListener(R.id.ivChangeComment, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(getActivity(), ReplacementDetailsActivity.class);
                        intent.putExtra("input", true);
                        intent.putExtra("id", item.getId());
                        startActivity(intent);
                    }
                });
                //分享
                holder.setOnClickListener(R.id.ivChangeShare, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showShare();
                    }
                });
            }
        });
        changeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(getActivity(), ReplacementDetailsActivity.class);
                intent.putExtra("input", false);
                intent.putExtra("id", ((ExchangeListResult.ResultBean) changeAdapter.getDatas().get(position)).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 获取换回或者不换回数据
     *
     * @param type
     */
    private void changesHttp(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("swap_type", type);
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/exchangeList.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("获取换回或者不换回数据" + e);
                    }

                    @Override
                    public void onSuccess(String response) {
                        ExchangeListResult exchangeListResult =
                                (ExchangeListResult) GsonUtil.json2Object(response, ExchangeListResult.class);
                        if (exchangeListResult != null
                                && exchangeListResult.getRet().equals("1")) {
                            changeAdapter.getDatas().clear();
                            changeAdapter.getDatas().addAll(exchangeListResult.getResult());
                            changeAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    private LinearLayout llFriends, llWeChat, llQQ, llQzone;
    private Button btMemberShare;
    private CustomPopWindow sharePop;

    /**
     * 显示分享菜单栏
     */
    private void showShare() {
        View view = LayoutInflater.from(getActivity()).
                inflate(R.layout.pop_share, null);
        llFriends = (LinearLayout) view.findViewById(R.id.llFriends);
        llWeChat = (LinearLayout) view.findViewById(R.id.llWeChat);
        llQQ = (LinearLayout) view.findViewById(R.id.llQQ);
        llQzone = (LinearLayout) view.findViewById(R.id.llQzone);
        btMemberShare = (Button) view.findViewById(R.id.btMemberShare);
        btMemberShare.setOnClickListener(new ShareClick());
        llFriends.setOnClickListener(new ShareClick());
        llWeChat.setOnClickListener(new ShareClick());
        llQQ.setOnClickListener(new ShareClick());
        llQzone.setOnClickListener(new ShareClick());

        sharePop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewHomeFg.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(rlHomeFg, Gravity.BOTTOM, 0, 0);
        viewHomeFg.setVisibility(View.VISIBLE);
    }

    private class ShareClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (sharePop != null) {
                sharePop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llFriends:
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llWeChat:
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQQ:
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.QQ)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQzone:
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
            }
        }
    }

    private UMShareListener shareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toasty.warning(getActivity(), "分享成功！", Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toasty.warning(getActivity(), "分享失败：" + t.getMessage(), Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toasty.warning(getActivity(), "取消分享！", Toast.LENGTH_SHORT, false).show();
        }
    };

    private CommonAdapter homeChangeImgsAdapter;
    private ArrayList<String> itemImgs;

    /**
     * 加载显示的图片
     *
     * @param view
     */
    private void initHomeChangeImgs(RecyclerView view, final List<ExchangeListResult.ResultBean.ImgListBean> list) {
        itemImgs = new ArrayList<>();
        for (ExchangeListResult.ResultBean.ImgListBean ib : list) {
            itemImgs.add(ib.getPicpath());
        }
        view.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        view.setAdapter(homeChangeImgsAdapter = new CommonAdapter<ExchangeListResult.ResultBean.ImgListBean>(getActivity(),
                R.layout.img, new ArrayList<ExchangeListResult.ResultBean.ImgListBean>()) {
            @Override
            protected void convert(ViewHolder holder, ExchangeListResult.ResultBean.ImgListBean item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(getActivity()) / 3,
                        Utils.getScreenWidth(getActivity()) / 3
                );
                lp.setMargins(5, 5, 5, 5);
                ImageView ivPhoto = (ImageView) holder.getView(R.id.ivPhoto);
                ivPhoto.setLayoutParams(lp);
                if (!TextUtils.isEmpty(item.getPicpath())) {
                    Glide.with(getActivity())
                            .load(item.getPicpath())
                            .into(ivPhoto);
                } else {
                    holder.setImageResource(R.id.ivPhoto, R.drawable.placeholder);
                }
            }
        });
        homeChangeImgsAdapter.getDatas().clear();
        homeChangeImgsAdapter.getDatas().addAll(list);
        homeChangeImgsAdapter.notifyDataSetChanged();
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
                .requestCode(100) // 请求码，会在listener中返回。
                .checkedList(list) // 要浏览的图片列表：ArrayList<String>。
                .currentPosition(position)
                .navigationAlpha(250) // Android5.0+的虚拟导航栏的透明度。
                .checkable(false) // 是否有浏览时的选择功能。
                .widget(
                        Widget.newDarkBuilder(getActivity())
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 设置悬浮效果
     */
    private void initSuspensionEffect() {
        topView.post(new Runnable() {
            @Override
            public void run() {
                layoutHeight = topView.getHeight() - 68;
            }
        });
        rootScrollView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= layoutHeight) {
            if (llCenterViewBottom.getVisibility() == View.GONE) {
                showSuspend();
            }
            if (llCenterViewBottom.getVisibility() == View.INVISIBLE) {
                showSuspend();
            }
        } else if (scrollY <= layoutHeight) {
            if (llCenterViewBottom.getVisibility() == View.VISIBLE) {
                removeSuspend();
            }
        }
    }

    private void showSuspend() {
        llCenterViewBottom.setVisibility(View.VISIBLE);
    }

    private void removeSuspend() {
        llCenterViewBottom.setVisibility(View.GONE);
    }

    /**
     * 游戏社区
     */
    @OnClick(R.id.llHomeGameCommunity)
    public void homeGameCommunity() {
        intent = new Intent(getActivity(), GameCommunityActivity.class);
        startActivity(intent);
    }

    /**
     * 中间模块点击事件
     */
    @OnClick(R.id.llHomePsFour)
    public void gameOptPSClick() {
        intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("switchType", 1);
        startActivity(intent);
    }

    @OnClick(R.id.llHomeXBox)
    public void gameOptXBoxClick() {
        intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("switchType", 2);
        startActivity(intent);
    }

    @OnClick(R.id.llHomeSwitch)
    public void gameOptSwitchClick() {
        intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("switchType", 3);
        startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    };

    /**
     * 点赞
     *
     * @param postId
     */
    private void zanHttp(final String postId) {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", postId);
        map.put("appuser_id", DaoUtils.getUserId(getActivity()));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/swapPostUp.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName() + "_swapPostUp")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(getActivity(), "点赞失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostUpResult postUpResult =
                                (SwapPostUpResult) GsonUtil.json2Object(response, SwapPostUpResult.class);
                        if (postUpResult != null
                                && postUpResult.getRet().equals("1")) {
                            for (ExchangeListResult.ResultBean rb : (List<ExchangeListResult.ResultBean>) changeAdapter.getDatas()) {
                                if (rb.getId() == postId) {
                                    rb.setIsUp("yes");
                                    rb.setUp_count(String.valueOf(Integer.parseInt(rb.getUp_count()) + 1));
                                }
                            }
                            changeAdapter.notifyDataSetChanged();
                        } else {
                            Toasty.warning(getActivity(), "点赞失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });

    }

    /**
     * 取消点赞
     *
     * @param postId
     */
    private void zanDownHttp(final String postId) {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", postId);
        map.put("appuser_id", DaoUtils.getUserId(getActivity()));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/swapPostDown.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName() + "_swapPostUp")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(getActivity(), "点赞失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostUpResult postUpResult =
                                (SwapPostUpResult) GsonUtil.json2Object(response, SwapPostUpResult.class);
                        if (postUpResult != null
                                && postUpResult.getRet().equals("1")) {
                            for (ExchangeListResult.ResultBean rb : (List<ExchangeListResult.ResultBean>) changeAdapter.getDatas()) {
                                if (rb.getId() == postId) {
                                    rb.setIsUp("no");
                                    rb.setUp_count(String.valueOf(Integer.parseInt(rb.getUp_count()) - 1));
                                }
                            }
                            changeAdapter.notifyDataSetChanged();
                        } else {
                            Toasty.warning(getActivity(), "取消点赞失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

}
