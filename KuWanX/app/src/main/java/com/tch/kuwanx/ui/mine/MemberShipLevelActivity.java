package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 会员等级
 */
public class MemberShipLevelActivity extends BaseActivity {

    @BindView(R.id.rlLevelTitleView)
    RelativeLayout rlLevelTitleView;
    @BindView(R.id.viewLevelBgmDark)
    View viewLevelBgmDark;
    @BindView(R.id.llMemberShipLevel)
    LinearLayout llMemberShipLevel;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship_level);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }

    private CustomPopWindow menuPop;
    private Button btPointRecord, btLevelShare;
    private boolean isShare = false;

    /**
     * 菜单按钮
     */
    @OnClick(R.id.ibLevelMenu)
    public void levelMenu() {
        View view = LayoutInflater.from(MemberShipLevelActivity.this).
                inflate(R.layout.pop_level_menu, null);
        btPointRecord = (Button) view.findViewById(R.id.btPointRecord);
        btLevelShare = (Button) view.findViewById(R.id.btLevelShare);
        btPointRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShare = false;
                if (menuPop != null) {
                    menuPop.dissmiss();
                }
                intent = new Intent(MemberShipLevelActivity.this, PointsRecordActivity.class);
                startActivity(intent);
            }
        });
        btLevelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShare = true;
                if (menuPop != null) {
                    menuPop.dissmiss();
                }
                showShare();
            }
        });
        menuPop = new CustomPopWindow.PopupWindowBuilder(MemberShipLevelActivity.this)
                .setView(view)
                .size(280, 180)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (!isShare) {
                            viewLevelBgmDark.setVisibility(View.GONE);
                        }
                    }
                })
                .create()
                .showAsDropDown(rlLevelTitleView, 0, 2, Gravity.RIGHT);
        viewLevelBgmDark.setVisibility(View.VISIBLE);
    }

    private LinearLayout llFriends, llWeChat, llQQ, llQzone;
    private Button btMemberShare;

    /**
     * 显示分享菜单栏
     */
    private void showShare() {
        View view = LayoutInflater.from(MemberShipLevelActivity.this).
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

        menuPop = new CustomPopWindow.PopupWindowBuilder(MemberShipLevelActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewLevelBgmDark.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(llMemberShipLevel, Gravity.BOTTOM, 0, 0);
    }

    private class ShareClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (menuPop != null) {
                menuPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llFriends:
                    new ShareAction(MemberShipLevelActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llWeChat:
                    new ShareAction(MemberShipLevelActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQQ:
                    new ShareAction(MemberShipLevelActivity.this)
                            .setPlatform(SHARE_MEDIA.QQ)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQzone:
                    new ShareAction(MemberShipLevelActivity.this)
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
            Toasty.warning(MemberShipLevelActivity.this, "分享成功！", Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toasty.warning(MemberShipLevelActivity.this, "分享失败：" + t.getMessage(), Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toasty.warning(MemberShipLevelActivity.this, "取消分享！", Toast.LENGTH_SHORT, false).show();
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ibLevelBack)
    public void levelBack() {
        MemberShipLevelActivity.this.finish();
    }
}
