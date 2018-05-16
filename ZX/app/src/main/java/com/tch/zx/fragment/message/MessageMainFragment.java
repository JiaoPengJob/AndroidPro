package com.tch.zx.fragment.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.contacts.GroupChatAllActivity;
import com.tch.zx.activity.message.ConversationActivity;
import com.tch.zx.activity.message.SystemMsgActivity;
import com.tch.zx.adapter.message.ReceiveMsgAdapter;
import com.tch.zx.util.ActionItem;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;
import com.tch.zx.view.TitlePopup;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 消息主页
 */

public class MessageMainFragment extends Fragment {

    /**
     * Fragment父布局
     */
    private View viewRoot;
    /**
     * 消息列表
     */
    @BindView(R.id.rv_message_receive)
    SwipeMenuRecyclerView rv_message_receive;
    /**
     * 顶部菜单按钮
     */
    @BindView(R.id.iv_top_menu)
    ImageView iv_top_menu;
    @BindView(R.id.tvGroupUnCount)
    TextView tvGroupUnCount;

    /**
     * 适配器
     */
    private ReceiveMsgAdapter receiveMsgAdapter;
    /**
     * 弹窗
     */
    private TitlePopup titlePopup;

    private List<Conversation> conversationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_message_main, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);

        initView();

        return viewRoot;
    }

    /**
     * 初始化
     */
    private void initView() {
        conversationList = new ArrayList<Conversation>();
        initPopMenuView();
        getContentList();
    }

    private void getContentList() {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversations) {
                    if (conversations != null && conversations.size() > 0) {
                        conversationList = conversations;
                        Log.e("TAG", "conversations数量" + conversations.size());
                        Log.e("TAG", "当前时间戳==" + System.currentTimeMillis());
                        handler.sendEmptyMessage(0);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("TAG", "获取私聊会话列表==" + errorCode.getMessage());
                }
            }, Conversation.ConversationType.PRIVATE);

            RongIM.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    if (integer >= 99) {
                        tvGroupUnCount.setVisibility(View.VISIBLE);
                        tvGroupUnCount.setText("99+");
                    } else if (integer == 0) {
                        tvGroupUnCount.setVisibility(View.GONE);
                    } else {
                        tvGroupUnCount.setVisibility(View.VISIBLE);
                        tvGroupUnCount.setText(String.valueOf(integer));
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("TAG", "获取群组会话列表未读数目==" + errorCode.getMessage());
                }
            }, Conversation.ConversationType.GROUP);
        }
    }

    /**
     * 加载列表数据
     */
    private void setData(final List<Conversation> list) {

        receiveMsgAdapter = new ReceiveMsgAdapter(getActivity(), list);
        rv_message_receive.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_message_receive.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 1, false));
        rv_message_receive.setAdapter(receiveMsgAdapter);
        receiveMsgAdapter.setOnItemClickListener(new ReceiveMsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Uri uri = Uri.parse("rong://" + "com.tch.zx.activity.message").buildUpon()
                        .appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                        .appendQueryParameter("targetId", list.get(position).getTargetId())
                        .appendQueryParameter("title", list.get(position).getConversationTitle())
                        .build();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                Bundle bundle = new Bundle();
//                bundle.putSerializable("userInfo", userInfoResult.getResponseObject());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        receiveMsgAdapter.notifyDataSetChanged();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setData(conversationList);
                    break;
            }
        }
    };

    /**
     * 查看所有群消息
     */
    @OnClick(R.id.rl_togeter_msg_all)
    public void intentTogeterTalk() {
        Intent intent = new Intent(getContext(), GroupChatAllActivity.class);
        intent.putExtra("groupType", "unGroup");
        startActivity(intent);
    }

    /**
     * 初始化组件
     */
    private void initPopMenuView() {
        //实例化标题栏按钮并设置监听
        iv_top_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });

        //实例化标题栏弹窗
        titlePopup = new TitlePopup(getContext(), (int) Math.floor(HelperUtil.getScreenWidth(getContext()) / 2), 400);
        initMenuData();
    }

    /**
     * 初始化数据
     */
    private void initMenuData() {
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(getContext(), "添加好友", R.mipmap.add_friend_menu));
        titlePopup.addAction(new ActionItem(getContext(), "发起群聊", R.mipmap.togeter_chat_menu));
        titlePopup.addAction(new ActionItem(getContext(), "邀请好友", R.mipmap.send_friends_menu));
    }

    /**
     * 系统消息点击事件
     */
    @OnClick(R.id.rlLineSystemInfo)
    public void lineSystemInfo() {
        Intent intent = new Intent(getContext(), SystemMsgActivity.class);
        startActivity(intent);
    }

}
