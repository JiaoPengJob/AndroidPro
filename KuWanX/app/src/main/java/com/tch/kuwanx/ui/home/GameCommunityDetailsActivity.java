package com.tch.kuwanx.ui.home;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.ChromeClientCallbackManager;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.GameCommunityDetailResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 游戏社区详情页
 */
public class GameCommunityDetailsActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.ibTitleFeatures)
    ImageButton ibTitleFeatures;
    @BindView(R.id.viewGameComFg)
    View viewGameComFg;
    @BindView(R.id.rlGameComParent)
    RelativeLayout rlGameComParent;
    @BindView(R.id.llGameComDetContent)
    LinearLayout llGameComDetContent;
    @BindView(R.id.llGameComDetWeb)
    LinearLayout llGameComDetWeb;
    @BindView(R.id.tvGameComDetContent)
    TextView tvGameComDetContent;
    @BindView(R.id.tvGameComDetName)
    TextView tvGameComDetName;
    @BindView(R.id.tvGameComDetDate)
    TextView tvGameComDetDate;

    private String showType, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_community_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() throws NullPointerException {
        tvTitleContent.setText("游戏社区");
        ibTitleFeatures.setVisibility(View.VISIBLE);
        ibTitleFeatures.setImageResource(R.mipmap.share);

        showType = getIntent().getStringExtra("showType");
        switch (showType) {
            case "onlyWeb":
                link = getIntent().getStringExtra("link");
                llGameComDetWeb.setVisibility(View.VISIBLE);
                llGameComDetContent.setVisibility(View.GONE);
                showWebView(link);
                break;
            case "showAll":
                llGameComDetWeb.setVisibility(View.VISIBLE);
                llGameComDetContent.setVisibility(View.VISIBLE);
                communityId = getIntent().getStringExtra("communityId");
                gameCommunityDetailHttp();
                break;
        }
    }

    private AgentWeb mAgentWeb;

    /**
     * 加载WebView
     */
    private void showWebView(String link) {
        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent(llGameComDetWeb,
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go(link);
    }

    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {

        }
    };

    /**
     * 分享
     */
    @OnClick(R.id.ibTitleFeatures)
    public void gcdTitleFeatures() {
        showShare();
    }

    private LinearLayout llFriends, llWeChat, llQQ, llQzone;
    private Button btMemberShare;
    private CustomPopWindow sharePop;

    /**
     * 显示分享菜单栏
     */
    private void showShare() {
        View view = LayoutInflater.from(GameCommunityDetailsActivity.this).
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

        sharePop = new CustomPopWindow.PopupWindowBuilder(GameCommunityDetailsActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewGameComFg.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(rlGameComParent, Gravity.BOTTOM, 0, 0);
        viewGameComFg.setVisibility(View.VISIBLE);
    }

    private class ShareClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (sharePop != null) {
                sharePop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llFriends:
                    new ShareAction(GameCommunityDetailsActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llWeChat:
                    new ShareAction(GameCommunityDetailsActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQQ:
                    new ShareAction(GameCommunityDetailsActivity.this)
                            .setPlatform(SHARE_MEDIA.QQ)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQzone:
                    new ShareAction(GameCommunityDetailsActivity.this)
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
            Toasty.warning(GameCommunityDetailsActivity.this, "分享成功！", Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toasty.warning(GameCommunityDetailsActivity.this, "分享失败：" + t.getMessage(), Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toasty.warning(GameCommunityDetailsActivity.this, "取消分享！", Toast.LENGTH_SHORT, false).show();
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void gcdTitleBack() {
        this.finish();
    }

    private String communityId;

    /**
     * 游戏社区详情
     */
    private void gameCommunityDetailHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("community_id", communityId);
        String params = EncryptionUtil.getParameter(GameCommunityDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/getGameCommunityDetail.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(communityId)
                .cacheTime(1)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("游戏社区详情" + e);
                    }

                    @Override
                    public void onSuccess(String response) {
                        GameCommunityDetailResult gameCommunityDetailResult
                                = (GameCommunityDetailResult) GsonUtil.json2Object(response, GameCommunityDetailResult.class);
                        if (gameCommunityDetailResult != null
                                && gameCommunityDetailResult.getRet().equals("1")) {
                            tvGameComDetContent.setText(Utils.html2Spanned(gameCommunityDetailResult.getResult().getContent()));
                            tvGameComDetName.setText(gameCommunityDetailResult.getResult().getTitle());
                            try {
                                tvGameComDetDate.setText(Utils.longToString(Long.parseLong(gameCommunityDetailResult.getResult().getCreate_time()),
                                        "yyyy-MM-dd"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            showWebView(gameCommunityDetailResult.getResult().getUrl());
                        }
                    }
                });
    }

}
