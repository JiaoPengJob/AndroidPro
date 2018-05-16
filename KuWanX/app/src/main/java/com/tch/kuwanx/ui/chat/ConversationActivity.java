package com.tch.kuwanx.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.KwMsg;
import com.tch.kuwanx.message.KwMessage;
import com.tch.kuwanx.ui.exchange.ConfirmActivity;
import com.tch.kuwanx.ui.exchange.ConfirmReceivedActivity;
import com.tch.kuwanx.ui.exchange.EvaluationActivity;
import com.tch.kuwanx.ui.exchange.OtherSubmitActivity;
import com.tch.kuwanx.ui.exchange.PayActivity;
import com.tch.kuwanx.ui.exchange.ReceivedActivity;
import com.tch.kuwanx.ui.exchange.RepayActivity;
import com.tch.kuwanx.ui.exchange.SendActivity;
import com.tch.kuwanx.ui.exchange.SubmitActivity;
import com.tch.kuwanx.ui.friend.FriendProfileActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.StatusBarCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 私聊页面
 */
public class ConversationActivity extends FragmentActivity implements RongIMClient.OnReceiveMessageListener, RongIM.ConversationBehaviorListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.ibTitleFeatures)
    ImageButton ibTitleFeatures;

    //暂时使用变量，正式应调用对象
    private String name = "", img;
    private String targetId;
    private int type;
    private boolean isTop;
    private int isNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_conversation);
        //设置沉浸式状态栏
        StatusBarCompat.compat(this, Color.parseColor("#FCCB05"));
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        isTop = getIntent().getBooleanExtra("isTop", false);
        isNotification = getIntent().getIntExtra("isNotification", 1);
        if (getIntent().getStringExtra("img") != null) {
            img = getIntent().getStringExtra("img");
        }
        ibTitleFeatures.setVisibility(View.VISIBLE);
        if (type == 0) {
            ibTitleFeatures.setImageResource(R.drawable.person_icon);
        } else {
            ibTitleFeatures.setImageResource(R.drawable.msg_group_chat);
        }
        targetId = getIntent().getData().getQueryParameter("targetId");
        name = getIntent().getData().getQueryParameter("title");
        tvTitleContent.setText(name);
        RongIM.setOnReceiveMessageListener(this);
        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
        RongIM.setConversationBehaviorListener(this);
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        //头像点击
//        Intent intent = new Intent(ConversationActivity.this, PrivateChatCenterActivity.class);
//        intent.putExtra("targetId", targetId);
//        intent.putExtra("isTop", isTop);
//        intent.putExtra("isNotification", isNotification);
//        startActivity(intent);
        if (!userInfo.getUserId().equals(DaoUtils.getUserId(ConversationActivity.this))) {
            Intent intent = new Intent(ConversationActivity.this, FriendProfileActivity.class);
            intent.putExtra("friendId", targetId);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    private Intent intent;
    private Bundle bundle;

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if (message.getContent() instanceof KwMessage) {
            bundle = new Bundle();
            KwMessage kwMessage = (KwMessage) message.getContent();
            Logger.wtf("消息体：" + kwMessage.getState());
            if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                bundle.putSerializable("kwMsg", new KwMsg(kwMessage.getState(), kwMessage.getMessage_id(), "send"));
            } else {
                bundle.putSerializable("kwMsg", new KwMsg(kwMessage.getState(), kwMessage.getMessage_id(), "other"));
            }
            switch (kwMessage.getState()) {
                case "10":
                    intent = new Intent(context, SubmitActivity.class);
                    break;
                case "11":
                    intent = new Intent(context, ConfirmActivity.class);
                    break;
                case "12":
                    intent = new Intent(context, PayActivity.class);
                    break;
                case "13":
                    intent = new Intent(context, SendActivity.class);
                    break;
                case "14":
                    intent = new Intent(context, ReceivedActivity.class);
                    break;
                case "15":
                    intent = new Intent(context, RepayActivity.class);
                    break;
                case "16":
                    intent = new Intent(context, ConfirmReceivedActivity.class);
                    break;
                case "17":
                    intent = new Intent(context, EvaluationActivity.class);
                    break;
                case "20":
                    intent = new Intent(context, OtherSubmitActivity.class);
                    break;
                case "21":
                    intent = new Intent(context, PayActivity.class);
                    break;
                case "22":
                    intent = new Intent(context, SendActivity.class);
                    break;
                case "23":
                    intent = new Intent(context, ReceivedActivity.class);
                    break;
                case "24":
                    intent = new Intent(context, RepayActivity.class);
                    break;
                case "25":
                    intent = new Intent(context, ConfirmReceivedActivity.class);
                    break;
                case "26":
                    intent = new Intent(context, EvaluationActivity.class);
                    break;
            }
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        return false;
    }

    /**
     * 好友资料
     */
    @OnClick(R.id.ibTitleFeatures)
    public void ibTitleFeatures() {
        if (type == 0) {
            Intent intent = new Intent(ConversationActivity.this, PrivateChatCenterActivity.class);
            intent.putExtra("targetId", targetId);
            intent.putExtra("isTop", isTop);
            intent.putExtra("name", tvTitleContent.getText().toString());
            intent.putExtra("img", img);
            intent.putExtra("isNotification", isNotification);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ConversationActivity.this, GroupManagerActivity.class);
            intent.putExtra("targetId", targetId);
            intent.putExtra("isNotification", isNotification);
            intent.putExtra("name", tvTitleContent.getText().toString());
            startActivity(intent);
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void conversationBack() {
        ConversationActivity.this.finish();
    }

}
