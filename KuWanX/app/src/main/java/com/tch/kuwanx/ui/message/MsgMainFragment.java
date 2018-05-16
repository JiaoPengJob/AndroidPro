package com.tch.kuwanx.ui.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.MyConversation;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.interfaces.GroupInterface;
import com.tch.kuwanx.interfaces.PrivateInterface;
import com.tch.kuwanx.listener.MyReceiveMessageListener;
import com.tch.kuwanx.module.SampleExtensionModule;
import com.tch.kuwanx.result.FriendsListResult;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Jiaop on 2017/10/24.
 * 消息（子页）
 */
public class MsgMainFragment extends Fragment implements PrivateInterface, GroupInterface, OnRefreshLoadmoreListener {

    @BindView(R.id.rvMsgPrivateChat)
    RecyclerView rvMsgPrivateChat;
    @BindView(R.id.tvMsgMainGroupUnread)
    TextView tvMsgMainGroupUnread;
    @BindView(R.id.refreshMsgMain)
    SmartRefreshLayout refreshMsgMain;

    private View viewRoot;
    private boolean isMore = false;
    private int privateNum = 0, groupNum = 0;

    public static MsgMainFragment getInstance() {
        MsgMainFragment sf = new MsgMainFragment();
        return sf;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_msg_main, null);
        ButterKnife.bind(this, viewRoot);
        refreshMsgMain.setOnRefreshLoadmoreListener(this);
        return viewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener(this, this));
        initChatListData();
        initPrivateConversationList();
        initSetTotalNum();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        initPrivateConversationList();
        initSetTotalNum();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        initPrivateConversationList();
        initSetTotalNum();
    }

    @Override
    public void ifRefreshPrivate(boolean refresh) {
        if (refresh) {
            initPrivateConversationList();
            initSetTotalNum();
        }
    }

    private CommonAdapter adapter;

    private void initChatListData() {
        rvMsgPrivateChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMsgPrivateChat.setAdapter(adapter = new CommonAdapter<MyConversation>(getActivity(),
                R.layout.item_msg_private_parent, new ArrayList<MyConversation>()) {
            @Override
            protected void convert(final ViewHolder holder, MyConversation item, final int position) {
                if (!TextUtils.isEmpty(item.getHeadimg())) {
                    Glide.with(getActivity())
                            .load(item.getHeadimg())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivMsgPriChatPhoto));
                } else {
                    holder.setImageResource(R.id.ivMsgPriChatPhoto, R.mipmap.app_icon);
                }

                if (item.getUnreadMessageCount() == 0) {
                    holder.setVisible(R.id.tvMsgPriChatUnread, false);
                } else {
                    holder.setVisible(R.id.tvMsgPriChatUnread, true);
                }
                holder.setText(R.id.tvMsgPriChatUnread, String.valueOf(item.getUnreadMessageCount()));

                if (!TextUtils.isEmpty(item.getName())) {
                    holder.setText(R.id.tvMsgPriChatName, item.getName());
                } else {
                    holder.setText(R.id.tvMsgPriChatName, "陌生人");
                }

                holder.setText(R.id.tvMsgPriChatTime, Utils.getDateArea(String.valueOf(item.getReceivedTime())));
                if (item.getLatestMessage() instanceof TextMessage) {
                    holder.setText(R.id.tvMsgPriChatNewTxt, ((TextMessage) item.getLatestMessage()).getContent());
                } else if (item.getLatestMessage() instanceof ImageMessage) {
                    holder.setText(R.id.tvMsgPriChatNewTxt, "[图片]");
                } else if (item.getLatestMessage() instanceof VoiceMessage) {
                    holder.setText(R.id.tvMsgPriChatNewTxt, "[语音]");
                } else if (item.getLatestMessage() instanceof FileMessage) {
                    holder.setText(R.id.tvMsgPriChatNewTxt, "[文件]");
                } else if (item.getLatestMessage() instanceof LocationMessage) {
                    holder.setText(R.id.tvMsgPriChatNewTxt, "[位置]");
                } else if (item.getObjectName() != null
                        && item.getObjectName().equals("RCD:KWMsg")) {
                    holder.setText(R.id.tvMsgPriChatNewTxt, "[物品交换]");
                } else {
                    holder.setText(R.id.tvMsgPriChatNewTxt, "未知数据");
                }

                holder.setOnClickListener(R.id.tvMsgItemDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((SwipeHorizontalMenuLayout) holder.getConvertView()).smoothCloseMenu();
                        adapter.getDatas().remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intentChat(((MyConversation) adapter.getDatas().get(position)).getTargetId(),
                        ((MyConversation) adapter.getDatas().get(position)).getName(),
                        ((MyConversation) adapter.getDatas().get(position)).isTop()
                        , ((MyConversation) adapter.getDatas().get(position)).getNotificationStatus().getValue(),
                        ((MyConversation) adapter.getDatas().get(position)).getHeadimg());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 获取私聊会话列表
     */
    private void initPrivateConversationList() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (refreshMsgMain != null) {
                    refreshMsgMain.finishLoadmore();
                    refreshMsgMain.finishRefresh();
                }
                if (conversations != null) {
                    ArrayList<MyConversation> list = new ArrayList<>();
                    for (Conversation c : conversations) {
                        list.add(Utils.getMyMc(c));
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("cons", list);
                    Message message = new Message();
                    message.what = 0;
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    Logger.wtf("进入获取私聊会话列表==conversations为null");
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if (refreshMsgMain != null) {
                    refreshMsgMain.finishLoadmore();
                    refreshMsgMain.finishRefresh();
                }
                Logger.wtf("获取私聊会话列表失败");
            }
        }, Conversation.ConversationType.PRIVATE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                List<MyConversation> list = msg.getData().getParcelableArrayList("cons");
                adapter.getDatas().clear();
                adapter.getDatas().addAll(list);
                adapter.notifyDataSetChanged();
                privateNum = list.size();
                getFriendsListHttp();
            }
        }
    };

    /**
     * 群聊信息列表
     */
    @OnClick(R.id.rlGroupInfo)
    public void groupInfo() {
        Intent intent = new Intent(getActivity(), GroupChatListActivity.class);
        intent.putExtra("activity", "MsgMain");
        startActivity(intent);
    }

    /**
     * 进入聊天页面
     */
    private void intentChat(String id, String name, boolean isTop, int isNotification, String img) {
        setMyExtensionModule();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("targetId", id)
                .appendQueryParameter("title", name)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTop", isTop);
        bundle.putInt("isNotification", isNotification);
        bundle.putInt("type", 0);
        bundle.putString("name", name);
        bundle.putString("img", img);
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

    /**
     * 设置未读数
     */
    private void initSetTotalNum() {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (refreshMsgMain != null) {
                    refreshMsgMain.finishLoadmore();
                    refreshMsgMain.finishRefresh();
                }
                groupNum = i;
                if (i == 0) {
                    tvMsgMainGroupUnread.setVisibility(View.GONE);
                } else {
                    tvMsgMainGroupUnread.setVisibility(View.VISIBLE);
                    tvMsgMainGroupUnread.setText(String.valueOf(i));
                }
            }
        }, Conversation.ConversationType.GROUP);
    }

    @Override
    public void ifRefreshGroup(boolean refresh) {
        if (refresh) {
            initSetTotalNum();
        }
    }

    /**
     * 获取好友列表
     */
    private void getFriendsListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(getActivity()));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/getFriendsList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getFriendsList")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
//                        Toasty.warning(FriendProfileActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                        Logger.e("获取信息失败！");
                    }

                    @Override
                    public void onSuccess(String response) {
                        FriendsListResult friendsListResult =
                                (FriendsListResult) GsonUtil.json2Object(response, FriendsListResult.class);
                        if (friendsListResult != null
                                && friendsListResult.getRet().equals("1")) {
                            for (FriendsListResult.ResultBean item : friendsListResult.getResult()) {
                                for (MyConversation c : (List<MyConversation>) adapter.getDatas()) {
                                    if (item.getFriends_app_user_id().equals(c.getTargetId())) {
                                        c.setName(item.getNickname());
                                        c.setHeadimg(item.getHeadpic());
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}



