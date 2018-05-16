package com.tch.kuwanx.ui.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.message.KwMessage;
import com.tch.kuwanx.module.SampleExtensionModule;
import com.tch.kuwanx.result.AddInterestResult;
import com.tch.kuwanx.result.EvaluateTagsResult;
import com.tch.kuwanx.result.FriendProfileResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.message.FriendVerifyActivity;
import com.tch.kuwanx.ui.mine.MemberShipLevelActivity;
import com.tch.kuwanx.ui.mine.OverViewActivity;
import com.tch.kuwanx.ui.mine.ReplaceRecordsActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.StarUtils;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
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
import es.dmoral.toasty.Toasty;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 好友主页
 */
public class FriendProfileActivity extends BaseActivity {

    @BindView(R.id.btProfileAttention)
    Button btProfileAttention;
    @BindView(R.id.btProfileSubmit)
    Button btProfileSubmit;
    @BindView(R.id.ivFriendProfilePhoto)
    ImageView ivFriendProfilePhoto;
    @BindView(R.id.tvWalletCount)
    TextView tvWalletCount;
    @BindView(R.id.tvWalletInfo)
    TextView tvWalletInfo;
    @BindView(R.id.rvFriendNotExBaby)
    RecyclerView rvFriendNotExBaby;
    @BindView(R.id.rvFriendExBaby)
    RecyclerView rvFriendExBaby;
    @BindView(R.id.flowProfileComments)
    TagFlowLayout flowProfileComments;
    @BindView(R.id.rvProfileEvaluate)
    RecyclerView rvProfileEvaluate;
    @BindView(R.id.sbLevel)
    SeekBar sbLevel;
    @BindView(R.id.srFriendProfile)
    SmartRefreshLayout srFriendProfile;

    private Intent intent;
    private CommonAdapter friendNotExBabyAdapter;
    private CommonAdapter friendExBabyAdapter;
    private int index;
    private boolean isLoadMore = false;
    private String friendId, activity, addType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra("friendId") != null) {
            friendId = getIntent().getStringExtra("friendId");
        }

        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
        }

        if (getIntent().getStringExtra("addType") != null) {
            addType = getIntent().getStringExtra("addType");
        }

        if (!TextUtils.isEmpty(activity)) {
            if (activity.equals("AddNewFriend")) {
                if (addType.equals("0")) {
                    btProfileSubmit.setText("添加好友");
                } else {
                    btProfileSubmit.setText("发信息");
                }
            } else {
                btProfileSubmit.setText("发信息");
            }
        } else {
            btProfileSubmit.setText("发信息");
        }

        btProfileAttention.setVisibility(View.VISIBLE);
        tvWalletInfo.setText("认证状态");
        sbLevel.setEnabled(false);
        sbLevel.setSelected(false);
        sbLevel.setClickable(false);
        sbLevel.setFocusable(false);
        sbLevel.setFocusableInTouchMode(false);
        initInfo();
        initFriendNotExBaby();
        initFriendExBaby();
        initFlowLayoutData(new ArrayList<String>());
        initMineEvaluate();

        //列表刷新
        srFriendProfile.setEnableRefresh(false);
        srFriendProfile.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                index++;
                isLoadMore = true;

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFriendInfo();
        evaluateTagsHttp();
    }

    /**
     * 加载个人信息
     */
    private void initInfo() {
        //圆形
        Glide.with(FriendProfileActivity.this)
                .load(R.mipmap.app_icon)
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(ivFriendProfilePhoto);
        tvWalletCount.setText("已认证");
    }

    /**
     * 未交换的宝贝
     */
    private void initFriendNotExBaby() {
        rvFriendNotExBaby.setLayoutManager(new GridLayoutManager(this, 4));
        rvFriendNotExBaby.setAdapter(friendNotExBabyAdapter = new CommonAdapter<FriendProfileResult.ResultBean.BabyListSelfBean>(this,
                R.layout.item_ex_baby, new ArrayList<FriendProfileResult.ResultBean.BabyListSelfBean>()) {
            @Override
            protected void convert(ViewHolder holder, FriendProfileResult.ResultBean.BabyListSelfBean item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((Utils.getScreenWidth(FriendProfileActivity.this) - 100) / 4,
                        (Utils.getScreenWidth(FriendProfileActivity.this) - 100) / 4);
                holder.getView(R.id.ivBabyPhoto).setLayoutParams(lp);

                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    //圆角
                    Glide.with(FriendProfileActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new RoundedCornersTransformation(20, 0,
                                    RoundedCornersTransformation.CornerType.ALL)))
                            .into((ImageView) holder.getView(R.id.ivBabyPhoto));
                } else {
                    holder.setImageResource(R.id.ivBabyPhoto, R.drawable.placeholder);
                }

                holder.setText(R.id.tvBabyName, item.getNAME());
                List<ImageView> stars = new ArrayList<ImageView>();
                stars.add((ImageView) holder.getView(R.id.ivStarOne));
                stars.add((ImageView) holder.getView(R.id.ivStarTwo));
                stars.add((ImageView) holder.getView(R.id.ivStarThree));
                stars.add((ImageView) holder.getView(R.id.ivStarFour));
                stars.add((ImageView) holder.getView(R.id.ivStarFive));
                StarUtils.showSomeStars(3, stars);
            }
        });
        friendNotExBabyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
     * 交换得到的宝贝
     */
    private void initFriendExBaby() {
        rvFriendExBaby.setLayoutManager(new GridLayoutManager(this, 4));
        rvFriendExBaby.setAdapter(friendExBabyAdapter = new CommonAdapter<FriendProfileResult.ResultBean.BabyListSwapBean>(this,
                R.layout.item_ex_baby, new ArrayList<FriendProfileResult.ResultBean.BabyListSwapBean>()) {
            @Override
            protected void convert(ViewHolder holder, FriendProfileResult.ResultBean.BabyListSwapBean item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((Utils.getScreenWidth(FriendProfileActivity.this) - 100) / 4,
                        (Utils.getScreenWidth(FriendProfileActivity.this) - 100) / 4);
                holder.getView(R.id.ivBabyPhoto).setLayoutParams(lp);
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    //圆角
                    Glide.with(FriendProfileActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new RoundedCornersTransformation(20, 0,
                                    RoundedCornersTransformation.CornerType.ALL)))
                            .into((ImageView) holder.getView(R.id.ivBabyPhoto));
                } else {
                    holder.setImageResource(R.id.ivBabyPhoto, R.drawable.placeholder);
                }

                holder.setText(R.id.tvBabyName, item.getNAME());
                List<ImageView> stars = new ArrayList<ImageView>();
                stars.add((ImageView) holder.getView(R.id.ivStarOne));
                stars.add((ImageView) holder.getView(R.id.ivStarTwo));
                stars.add((ImageView) holder.getView(R.id.ivStarThree));
                stars.add((ImageView) holder.getView(R.id.ivStarFour));
                stars.add((ImageView) holder.getView(R.id.ivStarFive));
                StarUtils.showSomeStars(4, stars);
            }
        });
        friendExBabyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private List<String> flowList = new ArrayList<>();
    private TextView tvTabShow;

    /**
     * 加载流式布局
     */
    private void initFlowLayoutData(List<String> list) {
        flowList.clear();
        flowList.addAll(list);
        flowProfileComments.setAdapter(new TagAdapter<String>(flowList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                tvTabShow = (TextView) LayoutInflater.from(FriendProfileActivity.this).inflate(R.layout.assess_tv, flowProfileComments, false);
                tvTabShow.setText(s);
                return tvTabShow;
            }
        });
    }

    private CommonAdapter profileCommentAdapter;

    /**
     * 加载评价列表
     */
    private void initMineEvaluate() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        rvProfileEvaluate.setLayoutManager(new LinearLayoutManager(FriendProfileActivity.this));
        rvProfileEvaluate.setAdapter(profileCommentAdapter = new CommonAdapter<String>(FriendProfileActivity.this, R.layout.item_mine_evaluate, list) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {

            }
        });
        profileCommentAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
     * 关注
     */
    @OnClick(R.id.btProfileAttention)
    public void profileAttention() {
        if (btProfileAttention.getText().toString().equals("关注")) {
            //未关注
            addInterestHttp();
        } else {
            //关注
            cancleInterestHttp();
        }
    }

    /**
     * 添加关注
     */
    private void addInterestHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", DaoUtils.getUserId(FriendProfileActivity.this));
        map.put("interest_user_id", friendId);
        String params = EncryptionUtil.getParameter(FriendProfileActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/addInterest.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        FriendProfileActivity.this, "关注中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(FriendProfileActivity.this, "关注失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddInterestResult addInterestResult =
                                (AddInterestResult) GsonUtil.json2Object(response, AddInterestResult.class);
                        if (addInterestResult != null
                                && addInterestResult.getRet().equals("1")) {
                            btProfileAttention.setText("已关注");
                        } else {
                            Toasty.warning(FriendProfileActivity.this, "关注失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 取消关注
     */
    private void cancleInterestHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", DaoUtils.getUserId(FriendProfileActivity.this));
        map.put("interest_user_id", friendId);
        String params = EncryptionUtil.getParameter(FriendProfileActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/cancleInterest.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        FriendProfileActivity.this, "取消关注中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(FriendProfileActivity.this, "取消关注失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddInterestResult addInterestResult =
                                (AddInterestResult) GsonUtil.json2Object(response, AddInterestResult.class);
                        if (addInterestResult != null
                                && addInterestResult.getRet().equals("1")) {
                            btProfileAttention.setText("关注");
                        } else {
                            Toasty.warning(FriendProfileActivity.this, "取消关注失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 会员等级
     */
    @OnClick(R.id.llMemberGrade)
    public void memberGradeProfile() {
        intent = new Intent(FriendProfileActivity.this, MemberShipLevelActivity.class);
        startActivity(intent);
    }

    /**
     * 认证状态
     */
    @OnClick(R.id.llWallet)
    public void walletProfile() {

    }

    /**
     * 综合评价
     */
    @OnClick(R.id.llOverView)
    public void overViewProfile() {
        intent = new Intent(FriendProfileActivity.this, OverViewActivity.class);
        startActivity(intent);
    }

    /**
     * 置换次数
     */
    @OnClick(R.id.llReplacementTimes)
    public void replacementTimesProfile() {
        intent = new Intent(FriendProfileActivity.this, ReplaceRecordsActivity.class);
        startActivity(intent);
    }

    /**
     * 添加好友或者发送消息
     */
    @OnClick(R.id.btProfileSubmit)
    public void profileSubmit() {
        if (btProfileSubmit.getText().toString().equals("添加好友")) {
            Intent intent = new Intent(FriendProfileActivity.this, FriendVerifyActivity.class);
            intent.putExtra("id", friendId);
            startActivity(intent);
        } else if (btProfileSubmit.getText().toString().equals("发信息")) {
            initSendMsg();
        }
    }

    /**
     * 发送消息
     */
    private void initSendMsg() {
        setMyExtensionModule();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("targetId", friendId)
                .appendQueryParameter("title", tvInfoName.getText().toString())
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Bundle bundle = new Bundle();
        bundle.putString("name", tvInfoName.getText().toString());
        if (friendProfileResult != null) {
            bundle.putString("img", friendProfileResult.getResult().getHeadpic());
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new SampleExtensionModule());
            }
        }
    }

    private FriendProfileResult friendProfileResult;

    /**
     * 获取好友信息
     */
    private void getFriendInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(FriendProfileActivity.this));
        map.put("other_user_id", friendId);
        String params = EncryptionUtil.getParameter(FriendProfileActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/myBabyList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_myBabyList")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
//                        Toasty.warning(FriendProfileActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                        Logger.e("获取好友信息失败！");
                    }

                    @Override
                    public void onSuccess(String response) {
                        friendProfileResult =
                                (FriendProfileResult) GsonUtil.json2Object(response, FriendProfileResult.class);
                        if (friendProfileResult != null
                                && friendProfileResult.getRet().equals("1")) {
                            if (!TextUtils.isEmpty(friendProfileResult.getResult().getHeadpic())) {
                                Glide.with(FriendProfileActivity.this)
                                        .load(friendProfileResult.getResult().getHeadpic())
                                        .apply(bitmapTransform(new CropCircleTransformation()))
                                        .into(ivFriendProfilePhoto);
                            } else {
                                ivFriendProfilePhoto.setImageResource(R.mipmap.app_icon);
                            }
                            tvInfoName.setText(friendProfileResult.getResult().getNickname());
                            tvInfoPhone.setText(friendProfileResult.getResult().getPhone());
                            if (friendProfileResult.getResult().getIsInterest().equals("1")) {
                                //已关注
                                btProfileAttention.setText("已关注");
                            } else {
                                //未关注
                                btProfileAttention.setText("关注");
                            }
                            if (friendProfileResult.getResult().getIsFriends().equals("0")) {
                                //不是好友
                                btProfileSubmit.setText("添加好友");
                            } else {
                                btProfileSubmit.setText("发信息");
                            }
                            tvReplacementTimes.setText(friendProfileResult.getResult().getSwapCount());
                            tvWalletCount.setText("认证状态");

                            friendNotExBabyAdapter.getDatas().clear();
                            friendNotExBabyAdapter.getDatas().addAll(friendProfileResult.getResult().getBabyListSelf());
                            friendNotExBabyAdapter.notifyDataSetChanged();

                            friendExBabyAdapter.getDatas().clear();
                            friendExBabyAdapter.getDatas().addAll(friendProfileResult.getResult().getBabyListSwap());
                            friendExBabyAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @BindView(R.id.tvInfoName)
    TextView tvInfoName;
    @BindView(R.id.tvInfoPhone)
    TextView tvInfoPhone;
    @BindView(R.id.tvReplacementTimes)
    TextView tvReplacementTimes;

    /**
     * 测试能否发送自定义消息
     */
    private void sendKwMsg() {
        KwMessage kwMessage = new KwMessage();
        kwMessage.setState("已发送");
        kwMessage.setMoney("123");
        kwMessage.setTitle("2B小姐姐");
        kwMessage.setPost_type("顺丰");
        kwMessage.setImg_url("http://i1.hdslb.com/bfs/archive/4243857d0ec18ee3e8b205d0cdef350000635338.jpg");
        kwMessage.setMessage_id("1");
        kwMessage.setContent("这是2B小姐姐的天下。。。。。。。");
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, "19900114", kwMessage, null, null,
                new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        Logger.wtf("消息成功存到本地数据库的回调");
                    }

                    @Override
                    public void onSuccess(Message message) {
                        Logger.wtf("消息发送成功的回调");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        Logger.wtf("消息发送失败的回调");
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibFriendProBack)
    public void friendProBack() {
        FriendProfileActivity.this.finish();
    }

    /**
     * 评价标签
     */
    private void evaluateTagsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        String params = EncryptionUtil.getParameter(FriendProfileActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/evaluateTags.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_evaluateTags")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
//                        Toasty.warning(FriendProfileActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                        Logger.e("获取好友评价标签失败！");
                    }

                    @Override
                    public void onSuccess(String response) {
                        EvaluateTagsResult evaluateTagsResult =
                                (EvaluateTagsResult) GsonUtil.json2Object(response, EvaluateTagsResult.class);
                        if (evaluateTagsResult != null
                                && evaluateTagsResult.getRet().equals("1")) {
                            initFlowLayoutData(evaluateTagsResult.getResult());
                        }
                    }
                });
    }

}
