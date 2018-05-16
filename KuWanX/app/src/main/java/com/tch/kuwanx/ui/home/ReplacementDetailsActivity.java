package com.tch.kuwanx.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.https.MyApiResult;
import com.tch.kuwanx.result.AddPostCommentsResult;
import com.tch.kuwanx.result.PostSwapListResult;
import com.tch.kuwanx.result.SwapCdCommentsResult;
import com.tch.kuwanx.result.SwapCdDetailResult;
import com.tch.kuwanx.result.SwapPostCollectResult;
import com.tch.kuwanx.result.SwapPostUpResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GlideImageLoader;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.tch.kuwanx.view.EasyIndicator;
import com.tch.kuwanx.view.SuspensionScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.reactivex.Observable;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 置换详情
 */
public class ReplacementDetailsActivity extends BaseActivity implements EasyIndicator.onTabClickListener,
        SuspensionScrollView.OnScrollListener, OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.repDetBanner)
    Banner repDetBanner;
    @BindView(R.id.repDetIndicator)
    EasyIndicator repDetIndicator;
    @BindView(R.id.repDetIndicatorBottom)
    EasyIndicator repDetIndicatorBottom;
    @BindView(R.id.ssvRepDet)
    SuspensionScrollView ssvRepDet;
    @BindView(R.id.llRepDetTopView)
    LinearLayout llRepDetTopView;
    @BindView(R.id.llRepDetPseudoCenterView)
    LinearLayout llRepDetPseudoCenterView;
    @BindView(R.id.rvRepDetLeaveData)
    RecyclerView rvRepDetLeaveData;
    @BindView(R.id.rvRepDetData)
    RecyclerView rvRepDetData;
    @BindView(R.id.rlRepDetLeaveData)
    RelativeLayout rlRepDetLeaveData;
    @BindView(R.id.rlRepDetData)
    RelativeLayout rlRepDetData;
    @BindView(R.id.btRepDetReplacement)
    Button btRepDetReplacement;
    @BindView(R.id.rivRepDetUserPhoto)
    ImageView rivRepDetUserPhoto;
    @BindView(R.id.tvRepDetUserName)
    TextView tvRepDetUserName;
    @BindView(R.id.viewRepDetDark)
    View viewRepDetDark;
    @BindView(R.id.rlRepDetParent)
    RelativeLayout rlRepDetParent;
    @BindView(R.id.refreshRepDet)
    SmartRefreshLayout refreshRepDet;

    private String[] mTitles = {"留言", "置换"};
    private int layoutHeight;
    private CommonAdapter repDetAdapter;
    private CommonAdapter repDetLeaveAdapter;
    private boolean input;
    private String id;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("详情");
        input = getIntent().getBooleanExtra("input", false);
        if (input) {
            rlRepDetParent.post(new Runnable() {
                @Override
                public void run() {
                    showEmojicon(1);
                }
            });
        }
        id = getIntent().getStringExtra("id");
        initBanner();
        setChangeTab();
        initSuspensionEffect();
        setRepDetData();
        setRepDetLeaveData();
        refreshRepDet.setOnRefreshLoadmoreListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        pageIndex = 1;
        mergeHttp();
    }

    private boolean isMore = false;

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        if (position == 0) {
            pageIndex += 1;
        } else {
            pageIndex_PostSwap += 1;
        }
        mergeHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        pageIndex = 1;
        pageIndex_PostSwap = 1;
        mergeHttp();
    }

    @BindView(R.id.tvRepDetUserLoc)
    TextView tvRepDetUserLoc;
    @BindView(R.id.tvRepDetUserTime)
    TextView tvRepDetUserTime;
    @BindView(R.id.tvRepDetContent)
    TextView tvRepDetContent;
    @BindView(R.id.tvRepDetMyRes)
    TextView tvRepDetMyRes;
    @BindView(R.id.tvRepDetMyPrice)
    TextView tvRepDetMyPrice;
    @BindView(R.id.tvRepDetMyDate)
    TextView tvRepDetMyDate;
    @BindView(R.id.tvRepDetChangeDetail)
    TextView tvRepDetChangeDetail;
    @BindView(R.id.tvRepDetChangeArea)
    TextView tvRepDetChangeArea;
    @BindView(R.id.tvRepDetLowPrice)
    TextView tvRepDetLowPrice;
    @BindView(R.id.tvRepDetComment)
    TextView tvRepDetComment;

    private int zanCount = 0, commentCount = 0;
    private String otherId = "";

    /**
     * 加载信息
     */
    private void initInfo(SwapCdDetailResult.ResultBean rb) {
        //判断是否是发帖人
        if (rb.getAppuser_id().equals(DaoUtils.getUserId(ReplacementDetailsActivity.this))) {
            btRepDetReplacement.setBackground(getResources().getDrawable(R.drawable.oval_replacement_button_unable));
            btRepDetReplacement.setTextColor(Color.parseColor("#FFFFFF"));
            btRepDetReplacement.setClickable(false);
        } else {
            otherId = rb.getAppuser_id();
            btRepDetReplacement.setBackground(getResources().getDrawable(R.drawable.oval_replacement_button));
            btRepDetReplacement.setTextColor(Color.parseColor("#333333"));
            btRepDetReplacement.setClickable(true);
        }

        if (!TextUtils.isEmpty(rb.getHeadpic())) {
            Glide.with(ReplacementDetailsActivity.this)
                    .load(rb.getHeadpic())
                    .apply(bitmapTransform(new CropCircleTransformation()))
                    .into(rivRepDetUserPhoto);
        } else {
            rivRepDetUserPhoto.setImageResource(R.mipmap.app_icon);
        }

        tvRepDetUserName.setText(rb.getNickname());
        tvRepDetUserLoc.setText(rb.getCds_address());
        tvRepDetUserTime.setText(rb.getPublish_time());
        tvRepDetContent.setText(rb.getUserselfinfo());
        tvRepDetMyRes.setText(rb.getSwap_cds());
        tvRepDetMyPrice.setText(rb.getPay_deposit());
        tvRepDetMyDate.setText(rb.getSwap_cycle());
        tvRepDetChangeDetail.setText(rb.getDelivery_mode());
        tvRepDetChangeArea.setText(rb.getCds_address());
        tvRepDetLowPrice.setText(rb.getSwap_deposit());
        zanCount = Integer.parseInt(rb.getUp_count());
        tvRepDetZan.setText("(" + zanCount + ")");
        commentCount = Integer.parseInt(rb.getComments_count());
        tvRepDetComment.setText("(" + commentCount + ")");
        if (rb.getIsUp().equals("yes")) {
            ifZan = 1;
            ivRepDetZan.setImageResource(R.mipmap.item_zan_sel);
            tvRepDetZan.setTextColor(Color.parseColor("#FFDA44"));
        } else {
            ifZan = 2;
            ivRepDetZan.setImageResource(R.mipmap.item_zan_unsel);
            tvRepDetZan.setTextColor(Color.parseColor("#7D7B7B"));
        }
    }

    private int ifItemZan = 2;

    /**
     * 加载留言列表数据
     */
    private void setRepDetLeaveData() {
        rvRepDetLeaveData.setLayoutManager(new LinearLayoutManager(this));
        rvRepDetLeaveData.setAdapter(repDetLeaveAdapter = new CommonAdapter<SwapCdCommentsResult.ResultBean>(this,
                R.layout.item_rep_det_leave_parent, new ArrayList<SwapCdCommentsResult.ResultBean>()) {
            @Override
            protected void convert(final ViewHolder holder, final SwapCdCommentsResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(ReplacementDetailsActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivItemRepDetUserPhoto));
                } else {
                    holder.setImageResource(R.id.ivItemRepDetUserPhoto, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvItemRepDetUserName, item.getNickname());
                holder.setText(R.id.tvItemRepDetContent, item.getComm_content());
                try {
                    holder.setText(R.id.tvItemRepDetTime, Utils.getDateArea(String.valueOf(Utils.stringToLong(
                            item.getComm_time(), "yyyy.MM.dd HH:mm:ss"
                    ))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.setText(R.id.tvItemRepDetNum, item.getComments_count() + "回复");
                final int leaveZan;
                leaveZan = Integer.parseInt(item.getComm_thumbup());
                holder.setText(R.id.tvItemRepDetZan, "(" + leaveZan + ")");
                holder.setOnClickListener(R.id.ivItemRepDetZan, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ifItemZan == 1) {
                            swapCommentsDownHttp(item.getComm_id(), (ImageView) holder.getView(R.id.ivItemRepDetZan)
                                    , (TextView) holder.getView(R.id.tvItemRepDetZan), leaveZan);
                        } else {
                            swapCommentsUpHttp(item.getComm_id(), (ImageView) holder.getView(R.id.ivItemRepDetZan)
                                    , (TextView) holder.getView(R.id.tvItemRepDetZan), leaveZan);
                        }
                    }
                });

            }
        });
        repDetLeaveAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                repDetLeaveSel = position;
                showEmojicon(2);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 置换物品的留言点赞
     */
    private void swapCommentsUpHttp(String id, final ImageView img, final TextView tvShow, final int count) {
        Map<String, Object> map = new HashMap<>();
        map.put("comm_id", id);
        map.put("appuser_id", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/swapCommentsUp.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_swapCommentsUp")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ReplacementDetailsActivity.this, "点赞失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostUpResult postUpResult =
                                (SwapPostUpResult) GsonUtil.json2Object(response, SwapPostUpResult.class);
                        if (postUpResult != null
                                && postUpResult.getRet().equals("1")) {
                            ifItemZan = 1;
                            img.setImageResource(R.mipmap.item_zan_sel);
                            tvShow.setTextColor(Color.parseColor("#FFDA44"));
                            tvShow.setText("(" + (count + 1) + ")");
                            mergeHttp();
                        } else {
                            Toasty.warning(ReplacementDetailsActivity.this, "点赞失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 置换物品的留言点赞
     */
    private void swapCommentsDownHttp(String id, final ImageView img, final TextView tvShow, final int count) {
        Map<String, Object> map = new HashMap<>();
        map.put("comm_id", id);
        map.put("appuser_id", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/swapCommentsDown.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_swapCommentsDown")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ReplacementDetailsActivity.this, "取消点赞失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostUpResult postUpResult =
                                (SwapPostUpResult) GsonUtil.json2Object(response, SwapPostUpResult.class);
                        if (postUpResult != null
                                && postUpResult.getRet().equals("1")) {
                            ifItemZan = 2;
                            img.setImageResource(R.mipmap.item_zan_unsel);
                            tvShow.setTextColor(Color.parseColor("#7D7B7B"));
                            tvShow.setText("(" + (count - 1) + ")");
                            mergeHttp();
                        } else {
                            Toasty.warning(ReplacementDetailsActivity.this, "取消点赞失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private int repDetLeaveSel = 0;

    /**
     * 加载置换列表数据
     */
    private void setRepDetData() {
        rvRepDetData.setLayoutManager(new LinearLayoutManager(this));
        rvRepDetData.setAdapter(repDetAdapter = new CommonAdapter<PostSwapListResult.ResultBean>(this,
                R.layout.item_rep_det_parent, new ArrayList<PostSwapListResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, PostSwapListResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(ReplacementDetailsActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivItemRepDetUserPhoto_));
                } else {
                    holder.setImageResource(R.id.ivItemRepDetUserPhoto_, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvRepDetName_, item.getNickname());
                holder.setText(R.id.tvRepDetContent_, item.getSwap_good());
                try {
                    holder.setText(R.id.tvRepDetTime_, Utils.getDateArea(String.valueOf(Utils.stringToLong(
                            item.getPublish_time(), "yyyy.MM.dd HH:mm:ss"
                    ))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        repDetAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
     * 加载图片轮播器
     */
    private void initBanner() {
        //测试数据
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            imgs.add("http://pic31.nipic.com/20130722/13289210_111601446172_2.gif");
        }

        repDetBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        repDetBanner.setImageLoader(new GlideImageLoader());
        repDetBanner.setBannerAnimation(Transformer.Default);
        repDetBanner.setViewPagerIsScroll(true);
        repDetBanner.isAutoPlay(true);
        repDetBanner.setDelayTime(1500);
        repDetBanner.setIndicatorGravity(BannerConfig.CENTER);
        repDetBanner.setImages(imgs);
        repDetBanner.start();
        repDetBanner.startAutoPlay();
        repDetBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }

    /**
     * 设置换回或不换回tab
     */
    private void setChangeTab() {
        repDetIndicator.setTabTitles(mTitles);
        repDetIndicator.setOnTabClickListener(this);
        repDetIndicatorBottom.setTabTitles(mTitles);
        repDetIndicatorBottom.setOnTabClickListener(this);
    }

    private int position = 0;

    @Override
    public void onTabClick(String s, int i) {
        position = i;
        repDetIndicator.onPageSelected(i);
        repDetIndicatorBottom.onPageSelected(i);
        if (i == 0) {
            rlRepDetLeaveData.setVisibility(View.VISIBLE);
            rlRepDetData.setVisibility(View.GONE);
        } else {
            rlRepDetLeaveData.setVisibility(View.GONE);
            rlRepDetData.setVisibility(View.VISIBLE);
        }
        ssvRepDet.scrollTo(0, mScrollY);
//        if (llRepDetPseudoCenterView.getVisibility() == View.GONE) {
//            showSuspend();
//        }
//        if (llRepDetPseudoCenterView.getVisibility() == View.INVISIBLE) {
//            showSuspend();
//        }
    }

    /**
     * 设置悬浮效果
     */
    private void initSuspensionEffect() {
        llRepDetTopView.post(new Runnable() {
            @Override
            public void run() {
                layoutHeight = llRepDetTopView.getHeight();
            }
        });
        ssvRepDet.setOnScrollListener(this);
    }

    private int mScrollY = 0;

    @Override
    public void onScroll(int scrollY) {
        mScrollY = scrollY;
        if (scrollY >= layoutHeight) {
            if (llRepDetPseudoCenterView.getVisibility() == View.GONE) {
                showSuspend();
            }
            if (llRepDetPseudoCenterView.getVisibility() == View.INVISIBLE) {
                showSuspend();
            }
        } else if (scrollY <= layoutHeight) {
            if (llRepDetPseudoCenterView.getVisibility() == View.VISIBLE) {
                removeSuspend();
            }
        }
    }

    private void showSuspend() {
        llRepDetPseudoCenterView.setVisibility(View.VISIBLE);
    }

    private void removeSuspend() {
        llRepDetPseudoCenterView.setVisibility(View.GONE);
    }

    /**
     * 置换按钮
     */
    @OnClick(R.id.btRepDetReplacement)
    public void repDetReplacement() {
        Intent intent = new Intent(ReplacementDetailsActivity.this, ProposedReplacementActivity.class);
        intent.putExtra("post_id", id);
        intent.putExtra("otherId", otherId);
        startActivity(intent);
    }

    @BindView(R.id.ivRepDetZan)
    ImageView ivRepDetZan;
    @BindView(R.id.tvRepDetZan)
    TextView tvRepDetZan;

    private int ifZan = 2;

    /**
     * 点赞
     */
    @OnClick(R.id.ivRepDetZan)
    public void repDetZan() {
        if (ifZan == 1) {
            zanDownHttp();
        } else {
            zanHttp();
        }
    }

    /**
     * 点赞
     */
    private void zanHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", id);
        map.put("appuser_id", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
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
                        Toasty.warning(ReplacementDetailsActivity.this, "点赞失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostUpResult postUpResult =
                                (SwapPostUpResult) GsonUtil.json2Object(response, SwapPostUpResult.class);
                        if (postUpResult != null
                                && postUpResult.getRet().equals("1")) {
                            ifZan = 1;
                            ivRepDetZan.setImageResource(R.mipmap.item_zan_sel);
                            tvRepDetZan.setTextColor(Color.parseColor("#FFDA44"));
                            tvRepDetZan.setText("(" + (zanCount + 1) + ")");
                        } else {
                            Toasty.warning(ReplacementDetailsActivity.this, "点赞失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });

    }

    /**
     * 取消点赞
     */
    private void zanDownHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", id);
        map.put("appuser_id", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
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
                        Toasty.warning(ReplacementDetailsActivity.this, "取消点赞失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostUpResult postUpResult =
                                (SwapPostUpResult) GsonUtil.json2Object(response, SwapPostUpResult.class);
                        if (postUpResult != null
                                && postUpResult.getRet().equals("1")) {
                            ifZan = 2;
                            ivRepDetZan.setImageResource(R.mipmap.item_zan_unsel);
                            tvRepDetZan.setTextColor(Color.parseColor("#7D7B7B"));
                            tvRepDetZan.setText("(" + (zanCount - 1) + ")");
                        } else {
                            Toasty.warning(ReplacementDetailsActivity.this, "取消点赞失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });

    }

    @BindView(R.id.ivRepDetCollection)
    ImageView ivRepDetCollection;

    private int ifCollection = 2;

    /**
     * 收藏
     */
    @OnClick(R.id.ivRepDetCollection)
    public void repDetCollection() {
        if (ifCollection == 1) {
            swapPostNoCollectHttp();
        } else {
            swapPostCollectHttp();
        }
    }

    /**
     * 置换物品-收藏
     */
    private void swapPostCollectHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", id);
        map.put("appuser_id", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/swapPostCollect.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_swapPostCollect")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ReplacementDetailsActivity.this, "收藏失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostCollectResult swapPostCollectResult =
                                (SwapPostCollectResult) GsonUtil.json2Object(response, SwapPostCollectResult.class);
                        if (swapPostCollectResult != null
                                && swapPostCollectResult.getRet().equals("1")) {
                            ifCollection = 1;
                            ivRepDetCollection.setImageResource(R.drawable.collection_sel);
                        } else {
                            Toasty.warning(ReplacementDetailsActivity.this, swapPostCollectResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 置换物品-取消收藏
     */
    private void swapPostNoCollectHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", id);
        map.put("appuser_id", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/swapPostNoCollect.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_swapPostNoCollect")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ReplacementDetailsActivity.this, "取消收藏失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SwapPostCollectResult swapPostCollectResult =
                                (SwapPostCollectResult) GsonUtil.json2Object(response, SwapPostCollectResult.class);
                        if (swapPostCollectResult != null
                                && swapPostCollectResult.getRet().equals("1")) {
                            ifCollection = 2;
                            ivRepDetCollection.setImageResource(R.mipmap.item_collection);
                        } else {
                            Toasty.warning(ReplacementDetailsActivity.this, swapPostCollectResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 分享
     */
    @OnClick(R.id.ivRepDetShare)
    public void repDetShare() {
        showShare();
    }

    private LinearLayout llFriends, llWeChat, llQQ, llQzone;
    private Button btMemberShare;
    private CustomPopWindow sharePop;

    /**
     * 显示分享菜单栏
     */
    private void showShare() {
        View view = LayoutInflater.from(ReplacementDetailsActivity.this).
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

        sharePop = new CustomPopWindow.PopupWindowBuilder(ReplacementDetailsActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewRepDetDark.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(rlRepDetParent, Gravity.BOTTOM, 0, 0);
        viewRepDetDark.setVisibility(View.VISIBLE);
    }

    private class ShareClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (sharePop != null) {
                sharePop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llFriends:
                    new ShareAction(ReplacementDetailsActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llWeChat:
                    new ShareAction(ReplacementDetailsActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQQ:
                    new ShareAction(ReplacementDetailsActivity.this)
                            .setPlatform(SHARE_MEDIA.QQ)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQzone:
                    new ShareAction(ReplacementDetailsActivity.this)
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
            Toasty.warning(ReplacementDetailsActivity.this, "分享成功！", Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toasty.warning(ReplacementDetailsActivity.this, "分享失败：" + t.getMessage(), Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toasty.warning(ReplacementDetailsActivity.this, "取消分享！", Toast.LENGTH_SHORT, false).show();
        }
    };

    /**
     * 评论
     */
    @OnClick({R.id.ivRepDetComment, R.id.tvRepDetComment})
    public void repDetComment() {
        showEmojicon(1);
    }

    private CustomPopWindow emojPop;
    private EmojiconEditText editEmoHome;
    private Button btEmojSend;

    /**
     * 评论 显示带有表情的输入框
     */
    private void showEmojicon(final int type) {
        View view = LayoutInflater.from(ReplacementDetailsActivity.this).
                inflate(R.layout.pop_comment, null);
        editEmoHome = (EmojiconEditText) view.findViewById(R.id.editEmoHome);
        btEmojSend = (Button) view.findViewById(R.id.btEmojSend);

        btEmojSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送
                if (type == 1) {
                    postComments(editEmoHome.getText().toString());
                } else {
                    postCommentsApply(editEmoHome.getText().toString());
                }
            }
        });

        emojPop = new CustomPopWindow.PopupWindowBuilder(ReplacementDetailsActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewRepDetDark.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(rlRepDetParent, Gravity.BOTTOM, 0, 0);
        viewRepDetDark.setVisibility(View.VISIBLE);
        Utils.showInput(ReplacementDetailsActivity.this);
    }

    /**
     * 发表评论，一级
     *
     * @param content
     */
    private void postComments(String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", id);
        map.put("appuser_id", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        map.put("comm_content", content);
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/addPostComments.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName() + "_addPostComments")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ReplacementDetailsActivity.this, "评论失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddPostCommentsResult postCommentsResult =
                                (AddPostCommentsResult) GsonUtil.json2Object(response, AddPostCommentsResult.class);
                        if (postCommentsResult != null
                                && postCommentsResult.getRet().equals("1")) {
                            if (emojPop != null) {
                                emojPop.dissmiss();
                            }
                            Utils.showInput(ReplacementDetailsActivity.this);
                            Toasty.warning(ReplacementDetailsActivity.this, "评论成功！", Toast.LENGTH_SHORT, false).show();
                            mergeHttp();
                        }
                    }
                });
    }

    /**
     * 发表评论，二级
     *
     * @param content
     */
    private void postCommentsApply(String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("comm_id", ((List<SwapCdCommentsResult.ResultBean>) repDetLeaveAdapter.getDatas()).get(repDetLeaveSel).getComm_id());
        map.put("comm_level", "1");
        map.put("comm_appuser", DaoUtils.getUserId(ReplacementDetailsActivity.this));
        map.put("comm_content", content);
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/addPostCommentsApply.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName() + "_addPostCommentsApply")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ReplacementDetailsActivity.this, "评论失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddPostCommentsResult postCommentsResult =
                                (AddPostCommentsResult) GsonUtil.json2Object(response, AddPostCommentsResult.class);
                        if (postCommentsResult != null
                                && postCommentsResult.getRet().equals("1")) {
                            if (emojPop != null) {
                                emojPop.dissmiss();
                            }
                            Utils.showInput(ReplacementDetailsActivity.this);
                            Toasty.warning(ReplacementDetailsActivity.this, "评论成功！", Toast.LENGTH_SHORT, false).show();
                            mergeHttp();
                        }
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void repDetailsTitleBack() {
        this.finish();
    }

    private int pageSize_PostSwap = 10;
    private int pageIndex_PostSwap = 1;

    /**
     * 合并请求
     */
    private void mergeHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("post_id", id);
        String params = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map);

        //置换的物品详情
        Observable<String> swapCdDetailObservable = EasyHttp.post(HttpUtils.URI_CENTER + "index/swapCdDetail.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        Map<String, Object> map1 = new HashMap<>();
        map1.put("post_id", id);
        map1.put("pageSize", String.valueOf(pageSize));
        map1.put("pageIndex", String.valueOf(pageIndex));
        String params1 = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map1);

        //置换物品的留言列表
        Observable<String> swapCdComObservable = EasyHttp.post(HttpUtils.URI_CENTER + "index/swapCdComments.jhtml")
                .params("data", params1)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        Map<String, Object> map2 = new HashMap<>();
        map2.put("post_id", id);
        map2.put("pageSize", String.valueOf(pageSize_PostSwap));
        map2.put("pageIndex", String.valueOf(pageIndex_PostSwap));
        String params2 = EncryptionUtil.getParameter(ReplacementDetailsActivity.this, map2);

        //置换物品置换的列表
        Observable<String> swapPostSwapObservable = EasyHttp.post(HttpUtils.URI_CENTER + "index/swapPostSwapList.jhtml")
                .params("data", params2)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });


        Observable.mergeDelayError(swapCdDetailObservable, swapCdComObservable, swapPostSwapObservable).subscribe(new BaseSubscriber<Object>() {
            @Override
            public void onError(ApiException e) {
                if (refreshRepDet != null) {
                    refreshRepDet.finishLoadmore();
                    refreshRepDet.finishRefresh();
                }
            }

            @Override
            public void onNext(@NonNull Object object) {
                if (refreshRepDet != null) {
                    refreshRepDet.finishLoadmore();
                    refreshRepDet.finishRefresh();
                }
                String result = object.toString();
                if (result.indexOf("swap_cds") != -1) {
                    SwapCdDetailResult swapCdDetailResult =
                            (SwapCdDetailResult) GsonUtil.json2Object(result, SwapCdDetailResult.class);
                    if (swapCdDetailResult != null
                            && swapCdDetailResult.getRet().equals("1")) {
                        initInfo(swapCdDetailResult.getResult());
                    }
                } else if (result.indexOf("comm_status") != -1) {
                    SwapCdCommentsResult commentsResult =
                            (SwapCdCommentsResult) GsonUtil.json2Object(result, SwapCdCommentsResult.class);
                    if (commentsResult != null
                            && commentsResult.getRet().equals("1")) {
                        if (isMore) {

                        } else {
                            repDetLeaveAdapter.getDatas().clear();
                            repDetLeaveAdapter.getDatas().addAll(commentsResult.getResult());
                            repDetLeaveAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (result.indexOf("swap_good") != -1) {
                    PostSwapListResult postSwapListResult =
                            (PostSwapListResult) GsonUtil.json2Object(result, PostSwapListResult.class);
                    if (postSwapListResult != null
                            && postSwapListResult.getRet().equals("1")) {
                        if (isMore) {

                        } else {
                            repDetAdapter.getDatas().clear();
                            repDetAdapter.getDatas().addAll(postSwapListResult.getResult());
                            repDetAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

}
