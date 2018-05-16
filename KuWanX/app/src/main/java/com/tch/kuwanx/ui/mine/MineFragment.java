package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.https.MyApiResult;
import com.tch.kuwanx.result.UserInfoResult;
import com.tch.kuwanx.ui.message.AttentionActivity;
import com.tch.kuwanx.ui.mine.article.ArticleActivity;
import com.tch.kuwanx.ui.mine.settings.SettingsActivity;
import com.tch.kuwanx.ui.store.ShoppingCartActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.BaseSubscriber;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Jiaop on 2017/10/24.
 * 我的
 */
public class MineFragment extends Fragment implements OnRefreshLoadmoreListener {

    @BindView(R.id.assessFlow)
    TagFlowLayout assessFlow;
    @BindView(R.id.rvMineEvaluate)
    RecyclerView rvMineEvaluate;
    @BindView(R.id.sbLevel)
    SeekBar sbLevel;
    @BindView(R.id.btProfileAttention)
    Button btProfileAttention;
    @BindView(R.id.tvWalletCount)
    TextView tvWalletCount;
    @BindView(R.id.refreshMine)
    SmartRefreshLayout refreshMine;

    private View viewRoot;
    private List<String> flowList;
    private TextView tvTabShow;
    private CommonAdapter mineEvaluateAdapter;
    private Intent intent;
    private boolean isMore = false;

    public static MineFragment getInstance() {
        MineFragment sf = new MineFragment();
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        btProfileAttention.setVisibility(View.GONE);
        sbLevel.setEnabled(false);
        sbLevel.setSelected(false);
        sbLevel.setClickable(false);
        sbLevel.setFocusable(false);
        sbLevel.setFocusableInTouchMode(false);

        tvWalletCount.setText(DaoUtils.getUserAccountnum(getActivity()));

        initFlowLayoutData();
        initMineEvaluate();
        refreshMine.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mergeHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        mergeHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        mergeHttp();
    }

    /**
     * 加载流式布局
     */
    private void initFlowLayoutData() {
        flowList = new ArrayList<>();
        flowList.add("评价");
        flowList.add("评价多个字");
        flowList.add("看");
        flowList.add("布局是什");
        flowList.add("可以");
        flowList.add("哈哈哈哈哈");
        assessFlow.setAdapter(new TagAdapter<String>(flowList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                tvTabShow = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.assess_tv, assessFlow, false);
                tvTabShow.setText(s);
                return tvTabShow;
            }
        });
    }

    /**
     * 加载评价列表
     */
    private void initMineEvaluate() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        rvMineEvaluate.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMineEvaluate.setAdapter(mineEvaluateAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_mine_evaluate, list) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {

            }
        });
        mineEvaluateAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
     * 个人资料
     */
    @OnClick(R.id.ibPersonalInfo)
    public void personalInfo() {
        intent = new Intent(getActivity(), PersonalInformationActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**
     * 设置
     */
    @OnClick(R.id.ibSettings)
    public void settings() {
        intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * 会员等级
     */
    @OnClick(R.id.llMemberGrade)
    public void memberGrade() {
        intent = new Intent(getActivity(), MemberShipLevelActivity.class);
        startActivity(intent);
    }

    /**
     * 钱包
     */
    @OnClick(R.id.llWallet)
    public void wallet() {
        intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
    }

    /**
     * 综合评价
     */
    @OnClick(R.id.llOverView)
    public void overView() {
        intent = new Intent(getActivity(), OverViewActivity.class);
        startActivity(intent);
    }

    /**
     * 置换次数
     */
    @OnClick(R.id.llReplacementTimes)
    public void replacementTimes() {
        intent = new Intent(getActivity(), ReplaceRecordsActivity.class);
        startActivity(intent);
    }

    /**
     * 我的帖子
     */
    @OnClick(R.id.llMyPosts)
    public void myPosts() {
        intent = new Intent(getActivity(), MyPostsActivity.class);
        intent.putExtra("postIndex", 0);
        startActivity(intent);
    }

    /**
     * 我的收藏
     */
    @OnClick(R.id.llMineCollection)
    public void llMineCollection() {
        intent = new Intent(getActivity(), MyPostsActivity.class);
        intent.putExtra("postIndex", 1);
        startActivity(intent);
    }

    /**
     * 物品管理
     */
    @OnClick(R.id.llArticle)
    public void articleManage() {
        intent = new Intent(getActivity(), ArticleActivity.class);
        startActivity(intent);
    }


    /**
     * 收藏夹
     */
    @OnClick(R.id.llFavorites)
    public void favorites() {
        intent = new Intent(getActivity(), FavoritesActivity.class);
        startActivity(intent);
    }

    /**
     * 购物车
     */
    @OnClick(R.id.llShopCart)
    public void shopCart() {
        intent = new Intent(getActivity(), ShoppingCartActivity.class);
        startActivity(intent);
    }

    /**
     * 我的换物订单
     *
     * @param view
     */
    @OnClick({R.id.llWaitPaid, R.id.llWaitShip, R.id.llWaitReceiving, R.id.llWaitReturnedGoods,
            R.id.llWaitReceivingParty, R.id.llWaitAppraise})
    public void myChangeOrder(View view) {
        int waitType = 0;
        switch (view.getId()) {
            case R.id.llWaitPaid:
                waitType = 1;
                break;
            case R.id.llWaitShip:
                waitType = 2;
                break;
            case R.id.llWaitReceiving:
                waitType = 3;
                break;
            case R.id.llWaitReturnedGoods:
                waitType = 4;
                break;
            case R.id.llWaitReceivingParty:
                waitType = 5;
                break;
            case R.id.llWaitAppraise:
                waitType = 6;
                break;
        }
        intent = new Intent(getActivity(), MyChangeOrderActivity.class);
        intent.putExtra("waitType", waitType);
        startActivity(intent);
    }

    /**
     * 我的购物订单
     *
     * @param view
     */
    @OnClick({R.id.llStorePaid, R.id.llStoreShip, R.id.llStoreReceiving, R.id.llStoreAppraise})
    public void storeOrder(View view) {
        int storeType = 0;
        switch (view.getId()) {
            case R.id.llStorePaid:
                storeType = 1;
                break;
            case R.id.llStoreShip:
                storeType = 2;
                break;
            case R.id.llStoreReceiving:
                storeType = 3;
                break;
            case R.id.llStoreAppraise:
                storeType = 4;
                break;
        }
        intent = new Intent(getActivity(), MallOrdersActivity.class);
        intent.putExtra("storeType", storeType);
        startActivity(intent);
    }

    /**
     * 我的关注
     */
    @OnClick(R.id.llMineAttention)
    public void mineAttention() {
        intent = new Intent(getActivity(), AttentionActivity.class);
        intent.putExtra("att", 1);
        startActivity(intent);
    }

    /**
     * 合并请求
     */
    private void mergeHttp() {
        //获取用户资料
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(getActivity()));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        Observable<String> userObservable = EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserById.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getUserById")
                .cacheTime(2)
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        //
        Map<String, Object> map1 = new HashMap<>();
        map1.put("swap_type", "1");
        String params1 = EncryptionUtil.getParameter(getActivity(), map1);
        Observable<String> exchangeListObservable = EasyHttp.post(HttpUtils.URI_CENTER + "index/exchangeList.jhtml")
                .params("data", params1)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_exchangeList")
                .cacheTime(2)
                .execute(new CallClazzProxy<MyApiResult<String>, String>(String.class) {
                });

        Observable.mergeDelayError(userObservable, exchangeListObservable).subscribe(new BaseSubscriber<Object>() {
            @Override
            public void onError(ApiException e) {
                if (refreshMine != null) {
                    refreshMine.finishLoadmore();
                    refreshMine.finishRefresh();
                }
            }

            @Override
            public void onNext(@NonNull Object object) {
                if (refreshMine != null) {
                    refreshMine.finishLoadmore();
                    refreshMine.finishRefresh();
                }

                String result = object.toString();
                if (result.indexOf("paypassword") != -1) {
                    UserInfoResult userInfo =
                            (UserInfoResult) GsonUtil.json2Object(result, UserInfoResult.class);
                    if (userInfo != null
                            && userInfo.getRet().equals("1")) {
                        initUserInfo(userInfo.getResult());
                    }
                } else if (result.indexOf("swap_cds") != -1) {

                }
            }
        });
    }

    @BindView(R.id.ivFriendProfilePhoto)
    ImageView ivFriendProfilePhoto;
    @BindView(R.id.tvInfoName)
    TextView tvInfoName;
    @BindView(R.id.tvInfoPhone)
    TextView tvInfoPhone;
    private UserInfoResult.ResultBean user;

    /**
     * 加载用户信息
     *
     * @param userInfo
     */
    private void initUserInfo(UserInfoResult.ResultBean userInfo) {
        user = userInfo;
        if (TextUtils.isEmpty(userInfo.getHeadpic())) {
            ivFriendProfilePhoto.setImageResource(R.mipmap.app_icon);
        } else {
            Glide.with(getActivity())
                    .load(userInfo.getHeadpic())
                    .apply(bitmapTransform(new CropCircleTransformation()))
                    .into(ivFriendProfilePhoto);
        }
        tvInfoName.setText(userInfo.getNickname());
        tvInfoPhone.setText(userInfo.getAccount());
    }
}
