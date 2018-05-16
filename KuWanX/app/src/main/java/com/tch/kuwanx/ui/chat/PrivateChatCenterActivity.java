package com.tch.kuwanx.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.message.ChooseContactActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 私聊个人中心
 */
public class PrivateChatCenterActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.ibNotDisturbNews)
    ImageButton ibNotDisturbNews;
    @BindView(R.id.ibStickChat)
    ImageButton ibStickChat;
    @BindView(R.id.ivFriendAvatar)
    ImageView ivFriendAvatar;
    @BindView(R.id.tvFriendName)
    TextView tvFriendName;

    private String targetId, name = "", img;
    private boolean isTop;
    private int isNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat_center);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("好友昵称");
        targetId = getIntent().getStringExtra("targetId");
        isTop = getIntent().getBooleanExtra("isTop", false);
        isNotification = getIntent().getIntExtra("isNotification", 1);
        if (getIntent().getStringExtra("img") != null) {
            img = getIntent().getStringExtra("img");
            Glide.with(PrivateChatCenterActivity.this)
                    .load(img)
                    .into(ivFriendAvatar);
        } else {
            ivFriendAvatar.setImageResource(R.drawable.default_head);
        }
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
            tvFriendName.setText(name);
            tvTitleContent.setText(name);
        }
        if (isTop) {
            ibStickChat.setImageResource(R.drawable.switch_sel);
        } else {
            ibStickChat.setImageResource(R.drawable.switch_unsel);
        }

        if (isNotification == 0) {
            ibNotDisturbNews.setImageResource(R.drawable.switch_sel);
        } else {
            ibNotDisturbNews.setImageResource(R.drawable.switch_unsel);
        }
    }

    /**
     * 好友头像
     */
    @OnClick(R.id.ivFriendAvatar)
    public void friendAvatar() {

    }

    /**
     * 创建群组
     */
    @OnClick(R.id.llCreateGroup)
    public void createGroup() {
        Intent intent = new Intent(PrivateChatCenterActivity.this, ChooseContactActivity.class);
        startActivity(intent);
    }

    /**
     * 置顶聊天
     */
    @OnClick(R.id.ibStickChat)
    public void stickChat() {
        if (!isTop) {
            isTop = true;
            ibStickChat.setImageResource(R.drawable.switch_sel);
        } else {
            isTop = false;
            ibStickChat.setImageResource(R.drawable.switch_unsel);
        }
        initStickChat();
    }

    /**
     * 设置置顶聊天
     */
    private void initStickChat() {
        RongIMClient.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, targetId, isTop, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (isTop) {
                    Toasty.warning(PrivateChatCenterActivity.this, "会话置顶成功！", Toast.LENGTH_SHORT, false).show();
                } else {
                    Toasty.warning(PrivateChatCenterActivity.this, "取消会话置顶成功！", Toast.LENGTH_SHORT, false).show();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toasty.warning(PrivateChatCenterActivity.this, "设置失败！", Toast.LENGTH_SHORT, false).show();
            }
        });
    }

    /**
     * 消息免打扰
     */
    @OnClick(R.id.ibNotDisturbNews)
    public void notDisturbNews() {
        if (isNotification == 1) {
            isNotification = 0;
            ibNotDisturbNews.setImageResource(R.drawable.switch_sel);
            initNotification(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB);
        } else if (isNotification == 0) {
            isNotification = 1;
            ibNotDisturbNews.setImageResource(R.drawable.switch_unsel);
            initNotification(Conversation.ConversationNotificationStatus.NOTIFY);
        }
    }

    /**
     * 设置消息免打扰
     */
    private void initNotification(Conversation.ConversationNotificationStatus notificationStatus) {
        RongIMClient.getInstance().setConversationNotificationStatus(Conversation.ConversationType.PRIVATE, targetId,
                notificationStatus, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        if (isNotification == 0) {
                            Toasty.warning(PrivateChatCenterActivity.this, "会话免打扰！", Toast.LENGTH_SHORT, false).show();
                        } else {
                            Toasty.warning(PrivateChatCenterActivity.this, "会话提醒！", Toast.LENGTH_SHORT, false).show();
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toasty.warning(PrivateChatCenterActivity.this, "设置失败！", Toast.LENGTH_SHORT, false).show();
                    }
                });
    }

    /**
     * 清空聊天记录
     */
    @OnClick(R.id.btClearChatRecord)
    public void clearChatRecord() {
        clearMsgs();
    }

    /**
     * 清空聊天信息
     */
    private void clearMsgs() {
        RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Toasty.warning(PrivateChatCenterActivity.this, "清空成功！", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toasty.warning(PrivateChatCenterActivity.this, "清空失败！", Toast.LENGTH_SHORT, false).show();
            }
        });
    }

    /**
     * 投诉
     */
    @OnClick(R.id.btComplaints)
    public void complaints() {
        Intent intent = new Intent(PrivateChatCenterActivity.this, ComplaintsActivity.class);
        intent.putExtra("targetId", targetId);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void privateChatCenterBack() {
        PrivateChatCenterActivity.this.finish();
    }
}
