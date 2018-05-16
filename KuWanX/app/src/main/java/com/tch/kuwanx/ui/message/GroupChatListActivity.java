package com.tch.kuwanx.ui.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.GroupListInfo;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.interfaces.GroupInterface;
import com.tch.kuwanx.module.NullExtensionModule;
import com.tch.kuwanx.result.GroupListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
import io.rong.imkit.RongIM;
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
 * 群组列表
 */
public class GroupChatListActivity extends BaseActivity implements GroupInterface, OnRefreshLoadmoreListener {

    @BindView(R.id.rlGroupTitle)
    RelativeLayout rlGroupTitle;
    @BindView(R.id.viewGroupDark)
    View viewGroupDark;
    @BindView(R.id.rvGroupList)
    RecyclerView rvGroupList;
    @BindView(R.id.tvGroupListTitle)
    TextView tvGroupListTitle;
    @BindView(R.id.refreshGroupChatList)
    SmartRefreshLayout refreshGroupChatList;

    private CommonAdapter groupListAdapter;
    private Intent intent;
    private String activity;
    private boolean isMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
        }

        switch (activity) {
            case "MsgMain":
                tvGroupListTitle.setText("消息");
                break;
            case "AddressBook":
                tvGroupListTitle.setText("我的群组");
                break;
        }

        initGroupList();
        initPrivateConversationList();
        refreshGroupChatList.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupListHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        getGroupListHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        getGroupListHttp();
    }

    private RecyclerView rvGroupListPhotos;

    private void initGroupList() {
        rvGroupList.setLayoutManager(new LinearLayoutManager(this));
        rvGroupList.setAdapter(groupListAdapter = new CommonAdapter<GroupListInfo>(this,
                R.layout.item_group_list_parent, new ArrayList<GroupListInfo>()) {
            @Override
            protected void convert(ViewHolder holder, GroupListInfo info, int position) {
                Conversation item = info.getConversation();
                GroupListResult.ResultBean resultBean = info.getResultBean();
                rvGroupListPhotos = (RecyclerView) holder.getView(R.id.rvGroupListPhotos);
                initGroupListPhoto(rvGroupListPhotos, resultBean.getImgList());

                if (item != null) {
                    if (item.getUnreadMessageCount() == 0) {
                        holder.setVisible(R.id.tvGroupListItemUnread, false);
                    } else {
                        holder.setVisible(R.id.tvGroupListItemUnread, true);
                        holder.setText(R.id.tvGroupListItemUnread, String.valueOf(item.getUnreadMessageCount()));
                    }
                } else {
                    holder.setVisible(R.id.tvGroupListItemUnread, false);
                }

                holder.setText(R.id.tvGroupListName, resultBean.getGroup_nickname());

                if (item != null) {
                    if (item.getLatestMessage() instanceof TextMessage) {
                        holder.setText(R.id.tvGroupListMsg, ((TextMessage) item.getLatestMessage()).getContent());
                    } else if (item.getLatestMessage() instanceof ImageMessage) {
                        holder.setText(R.id.tvGroupListMsg, "[图片]");
                    } else if (item.getLatestMessage() instanceof VoiceMessage) {
                        holder.setText(R.id.tvGroupListMsg, "[语音]");
                    } else if (item.getLatestMessage() instanceof FileMessage) {
                        holder.setText(R.id.tvGroupListMsg, "[文件]");
                    } else if (item.getLatestMessage() instanceof LocationMessage) {
                        holder.setText(R.id.tvGroupListMsg, "[位置]");
                    } else if (item.getObjectName() != null
                            && item.getObjectName().equals("RCD:KWMsg")) {
                        holder.setText(R.id.tvGroupListMsg, "[物品交换]");
                    } else {
                        holder.setText(R.id.tvGroupListMsg, "未知数据");
                    }
                    holder.setText(R.id.tvGroupListTime, Utils.getDateArea(String.valueOf(item.getReceivedTime())));
                    if (item.getNotificationStatus() == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                        holder.setVisible(R.id.ivGroupListState, true);
                    } else {
                        holder.setVisible(R.id.ivGroupListState, false);
                    }
                } else {
                    holder.setVisible(R.id.tvGroupListMsg, false);
                    holder.setVisible(R.id.tvGroupListTime, false);
                    holder.setVisible(R.id.ivGroupListState, false);
                }
            }
        });
        groupListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                intentChat(((Conversation) groupListAdapter.getDatas().get(position)).getNotificationStatus().getValue());
                if (((GroupListInfo) groupListAdapter.getDatas().get(position)).getConversation() != null) {
                    intentChat(
                            ((GroupListInfo) groupListAdapter.getDatas().get(position)).getResultBean().getGroup_id(),
                            ((GroupListInfo) groupListAdapter.getDatas().get(position)).getResultBean().getGroup_nickname(),
                            ((GroupListInfo) groupListAdapter.getDatas().get(position)).getConversation().getNotificationStatus().getValue()
                    );
                } else {
                    intentChat(
                            ((GroupListInfo) groupListAdapter.getDatas().get(position)).getResultBean().getGroup_id(),
                            ((GroupListInfo) groupListAdapter.getDatas().get(position)).getResultBean().getGroup_nickname(),
                            1
                    );
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CommonAdapter listPhotosAdapter;

    /**
     * 加载群组头像信息
     */
    private void initGroupListPhoto(RecyclerView recyclerView, List<GroupListResult.ResultBean.ImgListBean> imgs) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(listPhotosAdapter = new CommonAdapter<GroupListResult.ResultBean.ImgListBean>(this,
                R.layout.img, new ArrayList<GroupListResult.ResultBean.ImgListBean>()) {
            @Override
            protected void convert(ViewHolder holder, GroupListResult.ResultBean.ImgListBean item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(30, 30);
                lp.setMargins(1, 1, 1, 1);
                holder.getView(R.id.ivPhoto).setLayoutParams(lp);
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(GroupChatListActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivPhoto));
                } else {
                    holder.setImageResource(R.id.ivPhoto, R.mipmap.app_icon);
                }
            }
        });
        listPhotosAdapter.getDatas().clear();
        listPhotosAdapter.getDatas().addAll(imgs);
        listPhotosAdapter.notifyDataSetChanged();
    }

    /**
     * 进入聊天页面
     */
    private void intentChat(String id, String name, int isNotification) {
        setMyExtensionModule();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.GROUP.getName().toLowerCase())
                .appendQueryParameter("targetId", id)
                .appendQueryParameter("title", name)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putInt("isNotification", isNotification);
        bundle.putString("name", name);
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
                RongExtensionManager.getInstance().registerExtensionModule(new NullExtensionModule());
            }
        }
    }

    /**
     * 是否接收到了群组信息
     *
     * @param refresh
     */
    @Override
    public void ifRefreshGroup(boolean refresh) {
        if (refresh) {
            initPrivateConversationList();
        }
    }

    private List<Conversation> cs = new ArrayList<>();

    /**
     * 获取群聊会话列表
     */
    private void initPrivateConversationList() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null) {
                    cs.clear();
                    cs.addAll(conversations);
                } else {
                    Logger.wtf("进入获取群组会话列表==conversations为null");
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.wtf("获取群聊会话列表失败");
            }
        }, Conversation.ConversationType.GROUP);
    }

    /**
     * 菜单弹出框
     */
    @OnClick(R.id.ibGroupAddMenu)
    public void groupAddMenu() {
        showMsgAddMenu();
    }

    private CustomPopWindow groupMenuPop;
    private LinearLayout llSponsorGroupChat, llAddFriends, llInviteFriends;

    private void showMsgAddMenu() {
        View view = LayoutInflater.from(GroupChatListActivity.this).inflate(R.layout.pop_msg_menu, null);
        llSponsorGroupChat = (LinearLayout) view.findViewById(R.id.llSponsorGroupChat);
        llAddFriends = (LinearLayout) view.findViewById(R.id.llAddFriends);
        llInviteFriends = (LinearLayout) view.findViewById(R.id.llInviteFriends);
        llSponsorGroupChat.setOnClickListener(new MenuClickListener());
        llAddFriends.setOnClickListener(new MenuClickListener());
        llInviteFriends.setOnClickListener(new MenuClickListener());
        groupMenuPop = new CustomPopWindow.PopupWindowBuilder(GroupChatListActivity.this)
                .setView(view)
                .size(280, 280)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewGroupDark.setVisibility(View.GONE);
                    }
                })
                .create()
                .showAsDropDown(rlGroupTitle, 0, 2, Gravity.RIGHT);
        viewGroupDark.setVisibility(View.VISIBLE);
    }

    private class MenuClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (groupMenuPop != null) {
                groupMenuPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llSponsorGroupChat:
                    intent = new Intent(GroupChatListActivity.this, ChooseContactActivity.class);
                    startActivity(intent);
                    break;
                case R.id.llAddFriends:
                    intent = new Intent(GroupChatListActivity.this, AddNewFriendActivity.class);
                    startActivity(intent);
                    break;
                case R.id.llInviteFriends:

                    break;
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibGroupBack)
    public void groupBack() {
        GroupChatListActivity.this.finish();
    }

    /**
     * 群聊列表
     */
    private void getGroupListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(GroupChatListActivity.this));
        String params = EncryptionUtil.getParameter(GroupChatListActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/getGroupList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGroupList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        GroupChatListActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshGroupChatList != null) {
                            refreshGroupChatList.finishLoadmore();
                            refreshGroupChatList.finishRefresh();
                        }
                        Toasty.warning(GroupChatListActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshGroupChatList != null) {
                            refreshGroupChatList.finishLoadmore();
                            refreshGroupChatList.finishRefresh();
                        }

                        GroupListResult groupListResult =
                                (GroupListResult) GsonUtil.json2Object(response, GroupListResult.class);
                        if (groupListResult != null
                                && groupListResult.getRet().equals("1")) {
                            if (activity.equals("MsgMain")) {
                                //消息
                                if (isMore) {

                                } else {
                                    groupListAdapter.getDatas().clear();
                                }
                                for (int i = 0; i < cs.size(); i++) {
                                    GroupListInfo info = new GroupListInfo();
                                    info.setConversation(cs.get(i));
                                    for (int j = 0; j < groupListResult.getResult().size(); j++) {
                                        if (cs.get(i).getTargetId().equals(groupListResult.getResult().get(j).getGroup_id())) {
                                            info.setResultBean(groupListResult.getResult().get(j));
                                        }
                                    }
                                    groupListAdapter.getDatas().add(info);
                                }
                                groupListAdapter.notifyDataSetChanged();
                            } else {
                                //全部群组
                                if (isMore) {

                                } else {
                                    groupListAdapter.getDatas().clear();
                                }
                                for (int i = 0; i < groupListResult.getResult().size(); i++) {
                                    GroupListInfo info = new GroupListInfo();
                                    info.setResultBean(groupListResult.getResult().get(i));
                                    for (int j = 0; j < cs.size(); j++) {
                                        if (cs.get(j).getTargetId().equals(groupListResult.getResult().get(i).getGroup_id())) {
                                            info.setConversation(cs.get(j));
                                        }
                                    }
                                    groupListAdapter.getDatas().add(info);
                                }
                                groupListAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Toasty.warning(GroupChatListActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
