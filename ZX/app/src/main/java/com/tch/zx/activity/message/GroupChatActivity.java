package com.tch.zx.activity.message;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.adapter.message.GroupUserInfoAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.GetGroupListResult;
import com.tch.zx.util.RongUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 群聊
 */
public class GroupChatActivity extends AppCompatActivity {

    @BindView(R.id.tvGroupName)
    TextView tvGroupName;

    private GetGroupListResult.ResponseObjectBean groupInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_group_chat);
        //集成使用Butterknife
        ButterKnife.bind(this);
        //设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initView();
    }

    private void initView() {

        if (getIntent().getSerializableExtra("groupInfo") != null) {
            groupInfo = (GetGroupListResult.ResponseObjectBean) getIntent().getSerializableExtra("groupInfo");
            tvGroupName.setText(groupInfo.getGroup_nickname() + "(" + groupInfo.getMenberList().size() + ")");
//            RongIM.getInstance().startConversation(GroupChatActivity.this, Conversation.ConversationType.GROUP, groupInfo.getId(), groupInfo.getGroup_nickname());
//            initInputView();
        }

    }

    private void initInputView() {
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                message.getSenderUserId();
                return null;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                if (message.getSentStatus() == Message.SentStatus.FAILED) {
                    if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                        //不在聊天室
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                        //不在讨论组
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                        //不在群组
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                        //你在他的黑名单中
                    }
                }

                MessageContent messageContent = message.getContent();

                if (messageContent instanceof TextMessage) {//文本消息
                    TextMessage textMessage = (TextMessage) messageContent;
                    Log.e("TAG", "onSent-TextMessage:" + textMessage.getContent());
                } else if (messageContent instanceof ImageMessage) {//图片消息
                    ImageMessage imageMessage = (ImageMessage) messageContent;
                    Log.e("TAG", "onSent-ImageMessage:" + imageMessage.getRemoteUri());
                } else if (messageContent instanceof VoiceMessage) {//语音消息
                    VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                    Log.e("TAG", "onSent-voiceMessage:" + voiceMessage.getUri().toString());
                } else if (messageContent instanceof RichContentMessage) {//图文消息
                    RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                    Log.e("TAG", "onSent-RichContentMessage:" + richContentMessage.getContent());
                } else {
                    Log.e("TAG", "onSent-其他消息，自己来判断处理");
                }

                return false;
            }
        });
    }

    /**
     * 点击群人头
     */
    @OnClick(R.id.iv_group_chat_head)
    public void intentGroupInfo() {
        Intent intent = new Intent(GroupChatActivity.this, GroupInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("groupInfo", groupInfo);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    private String newGroupName, newNikeName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            groupInfo = (GetGroupListResult.ResponseObjectBean) data.getSerializableExtra("groupInfoResult");
            newGroupName = data.getStringExtra("newGroupName");
            newNikeName = data.getStringExtra("newNikeName");
            tvGroupName.setText(newGroupName + "(" + groupInfo.getMenberList().size() + ")");
            groupInfo.setGroup_nickname(newGroupName);
            groupInfo.setMember_nickname(newNikeName);
        }
    }

    /**
     * 后退
     */
    @OnClick(R.id.llGroupChatBack)
    public void llGroupChatBack() {
        GroupChatActivity.this.finish();
    }

    /**
     * 物理返回键监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            GroupChatActivity.this.finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
