package com.tch.kuwanx.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.GroupManagerMembers;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.DeleteGroupResult;
import com.tch.kuwanx.result.GroupMemberListResult;
import com.tch.kuwanx.result.QuitGroupResult;
import com.tch.kuwanx.result.UpdateGroupMemberNameResult;
import com.tch.kuwanx.result.UpdateGroupNameResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.message.ChooseContactActivity;
import com.tch.kuwanx.ui.message.GroupChatListActivity;
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
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 群组管理
 */
public class GroupManagerActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvGroupManagerMembers)
    RecyclerView rvGroupManagerMembers;
    @BindView(R.id.ibGroupManagerNotNews)
    ImageButton ibGroupManagerNotNews;
    @BindView(R.id.llGroupManagerParent)
    LinearLayout llGroupManagerParent;
    @BindView(R.id.tvGroupMsgUserName)
    TextView tvGroupMsgUserName;
    @BindView(R.id.tvGroupMsgName)
    TextView tvGroupMsgName;
    @BindView(R.id.btGroupManageSub)
    Button btGroupManageSub;

    private CommonAdapter groupMgrAdapter;
    private int isNotification;
    private String targetId, name;
    private String isGroupMain = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("群名（人数）");
        targetId = getIntent().getStringExtra("targetId");
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
            tvTitleContent.setText(name + "（人数）");
        }
        if (isNotification == 0) {
            ibGroupManagerNotNews.setImageResource(R.drawable.switch_sel);
        } else {
            ibGroupManagerNotNews.setImageResource(R.drawable.switch_unsel);
        }

        initGroupManagerMembers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupMembers();
    }

    private void initGroupManagerMembers() {
        rvGroupManagerMembers.setLayoutManager(new GridLayoutManager(this, 5));
        rvGroupManagerMembers.setAdapter(groupMgrAdapter = new CommonAdapter<GroupManagerMembers>(this,
                R.layout.item_group_members, new ArrayList<GroupManagerMembers>()) {
            @Override
            protected void convert(ViewHolder holder, GroupManagerMembers item, final int position) {
                if (position == groupMgrAdapter.getDatas().size() - 1) {
                    holder.setVisible(R.id.ibGroupMemberBan, false);
                    holder.getView(R.id.tvGroupMemberName).setVisibility(View.INVISIBLE);
                    holder.setImageResource(R.id.ivGroupMemberPhoto, R.drawable.add_member);
                } else {
                    holder.getView(R.id.tvGroupMemberName).setVisibility(View.VISIBLE);
                    //群主不显示自己删除，但是可以删除其他成员
                    if (isGroupMain.equals("yes")) {
                        holder.setVisible(R.id.ibGroupMemberBan, true);
                        if (item.getGroupMemberListResult().getId().equals(DaoUtils.getUserId(GroupManagerActivity.this))) {
                            holder.setVisible(R.id.ibGroupMemberBan, false);
                        }
                    } else {
                        holder.setVisible(R.id.ibGroupMemberBan, false);
                    }
                    if (!TextUtils.isEmpty(item.getGroupMemberListResult().getHeadpic())) {
                        Glide.with(GroupManagerActivity.this)
                                .load(item.getGroupMemberListResult().getHeadpic())
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into((ImageView) holder.getView(R.id.ivGroupMemberPhoto));
                    } else {
                        holder.setImageResource(R.id.ivGroupMemberPhoto, R.mipmap.app_icon);
                    }
                }

                holder.setText(R.id.tvGroupMemberName, item.getGroupMemberListResult().getMember_nickname());

                //删除
                holder.setOnClickListener(R.id.ibGroupMemberBan, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupMgrAdapter.getDatas().remove(position);
                        groupMgrAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        groupMgrAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == groupMgrAdapter.getDatas().size() - 1) {
                    Intent intent = new Intent(GroupManagerActivity.this, ChooseContactActivity.class);
                    intent.putExtra("activity", "GroupManager");
                    intent.putExtra("groupId", targetId);
                    intent.putExtra("groupName", tvGroupMsgName.getText().toString());
                    startActivity(intent);
                } else {

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @BindView(R.id.btLookOverMembers)
    Button btLookOverMembers;

    /**
     * 查看全部群成员
     */
    @OnClick(R.id.btLookOverMembers)
    public void lookOverMembers() {
        for (GroupManagerMembers gmm : (List<GroupManagerMembers>) groupMgrAdapter.getDatas()) {
            gmm.setShow(true);
        }
        groupMgrAdapter.notifyDataSetChanged();
        btLookOverMembers.setVisibility(View.GONE);
    }

    /**
     * 消息免打扰
     */
    @OnClick(R.id.ibGroupManagerNotNews)
    public void groupManagerNotNews() {
        if (isNotification == 1) {
            isNotification = 0;
            ibGroupManagerNotNews.setImageResource(R.drawable.switch_sel);
            initNotification(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB);
        } else if (isNotification == 0) {
            isNotification = 1;
            ibGroupManagerNotNews.setImageResource(R.drawable.switch_unsel);
            initNotification(Conversation.ConversationNotificationStatus.NOTIFY);
        }
    }

    /**
     * 设置消息免打扰
     */
    private void initNotification(Conversation.ConversationNotificationStatus notificationStatus) {
        RongIMClient.getInstance().setConversationNotificationStatus(Conversation.ConversationType.GROUP, targetId,
                notificationStatus, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        if (isNotification == 0) {
                            Toasty.warning(GroupManagerActivity.this, "会话免打扰！", Toast.LENGTH_SHORT, false).show();
                        } else {
                            Toasty.warning(GroupManagerActivity.this, "会话提醒！", Toast.LENGTH_SHORT, false).show();
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toasty.warning(GroupManagerActivity.this, "设置失败！", Toast.LENGTH_SHORT, false).show();
                    }
                });
    }

    /**
     * 修改群聊名称
     * 仅限群主才能修改
     */
    @OnClick(R.id.rlGroupName)
    public void updateGroupName() {
        if (isGroupMain.equals("yes")) {
            showUpdateGroupName();
        } else {
            Toasty.warning(GroupManagerActivity.this, "您没有权限！", Toast.LENGTH_SHORT, false).show();
        }
    }

    private CustomPopWindow groupGroupNamePop;
    private EditText etGroupName;
    private Button btGroupNamePopCancel, btGroupNamePopDefine;

    private void showUpdateGroupName() {
        View view = LayoutInflater.from(GroupManagerActivity.this).inflate(R.layout.pop_group_name, null);
        etGroupName = (EditText) view.findViewById(R.id.etGroupName);
        btGroupNamePopCancel = (Button) view.findViewById(R.id.btGroupNamePopCancel);
        btGroupNamePopDefine = (Button) view.findViewById(R.id.btGroupNamePopDefine);
        btGroupNamePopCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                if (groupGroupNamePop != null) {
                    groupGroupNamePop.dissmiss();
                }
            }
        });
        btGroupNamePopDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认
                if (groupGroupNamePop != null) {
                    groupGroupNamePop.dissmiss();
                }
                updateGroupNameHttp();
            }
        });
        groupGroupNamePop = new CustomPopWindow.PopupWindowBuilder(GroupManagerActivity.this)
                .setView(view)
                .size(Utils.getScreenWidth(GroupManagerActivity.this) - 40, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFocusable(true)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.6f)
                .create()
                .showAtLocation(llGroupManagerParent, Gravity.CENTER, 0, 0);
    }

    /**
     * 修改自己在群聊中的名称
     */
    @OnClick(R.id.rlGroupMineName)
    public void updateMineName() {
        showUpdateMineName();
    }

    private CustomPopWindow groupMineNamePop;
    private EditText etMineName;
    private Button btMineNamePopCancel, btMineNamePopDefine;

    private void showUpdateMineName() {
        View view = LayoutInflater.from(GroupManagerActivity.this).inflate(R.layout.pop_group_mine_name, null);
        etMineName = (EditText) view.findViewById(R.id.etMineName);
        btMineNamePopCancel = (Button) view.findViewById(R.id.btMineNamePopCancel);
        btMineNamePopDefine = (Button) view.findViewById(R.id.btMineNamePopDefine);
        btMineNamePopCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                if (groupMineNamePop != null) {
                    groupMineNamePop.dissmiss();
                }
            }
        });
        btMineNamePopDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认
                if (groupMineNamePop != null) {
                    groupMineNamePop.dissmiss();
                }
                updateGroupMineNameHttp();
            }
        });
        groupMineNamePop = new CustomPopWindow.PopupWindowBuilder(GroupManagerActivity.this)
                .setView(view)
                .size(Utils.getScreenWidth(GroupManagerActivity.this) - 40, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFocusable(true)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.6f)
                .create()
                .showAtLocation(llGroupManagerParent, Gravity.CENTER, 0, 0);
    }

    /**
     * 获取群组成员
     */
    private void getGroupMembers() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(GroupManagerActivity.this));
        map.put("group_id", targetId);
        String params = EncryptionUtil.getParameter(GroupManagerActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/getGroupMemberList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGroupMemberList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        GroupManagerActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(GroupManagerActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        GroupMemberListResult groupMemberListResult =
                                (GroupMemberListResult) GsonUtil.json2Object(response, GroupMemberListResult.class);
                        if (groupMemberListResult != null
                                && groupMemberListResult.getRet().equals("1")) {
                            isGroupMain = groupMemberListResult.getResult().getIsGroupMain();
                            if (isGroupMain.equals("yes")) {
                                btGroupManageSub.setText("解散本群");
                            } else {
                                btGroupManageSub.setText("退出本群");
                            }
                            tvTitleContent.setText(name + "（" + groupMemberListResult.getResult().getMemberList().size() + "）");
                            groupMgrAdapter.getDatas().clear();
                            for (int i = 0; i < groupMemberListResult.getResult().getMemberList().size(); i++) {
                                if (i < 9) {
                                    GroupManagerMembers g = new GroupManagerMembers();
                                    g.setShow(true);
                                    g.setGroupMemberListResult(groupMemberListResult.getResult().getMemberList().get(i));
                                    g.setAddImg(Utils.getDrawableResPath(GroupManagerActivity.this, R.drawable.add_member));
                                    groupMgrAdapter.getDatas().add(g);
                                } else {
                                    GroupManagerMembers g = new GroupManagerMembers();
                                    g.setShow(false);
                                    g.setGroupMemberListResult(groupMemberListResult.getResult().getMemberList().get(i));
                                    g.setAddImg(Utils.getDrawableResPath(GroupManagerActivity.this, R.drawable.add_member));
                                    groupMgrAdapter.getDatas().add(g);
                                }
                            }
                            GroupManagerMembers g = new GroupManagerMembers();
                            g.setShow(false);
                            g.setGroupMemberListResult(groupMemberListResult.getResult().getMemberList().get(0));
                            g.setAddImg(Utils.getDrawableResPath(GroupManagerActivity.this, R.drawable.add_member));
                            groupMgrAdapter.getDatas().add(g);
                            groupMgrAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 修改群名称的请求
     */
    private void updateGroupNameHttp() {
        Map<String, Object> map = new HashMap<>();
//        map.put("app_user_id", DaoUtils.getUserId(GroupManagerActivity.this));
        map.put("app_user_id", "be8c68e7aa754021b3b03c4bdca80e78");
        map.put("group_id", targetId);
        map.put("group_nickname", etGroupName.getText().toString());
        String params = EncryptionUtil.getParameter(GroupManagerActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/updateGroupName.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        GroupManagerActivity.this, "修改中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(GroupManagerActivity.this, "修改失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        UpdateGroupNameResult updateGroupNameResult =
                                (UpdateGroupNameResult) GsonUtil.json2Object(response, UpdateGroupNameResult.class);
                        if (updateGroupNameResult != null
                                && updateGroupNameResult.getRet().equals("1")) {
                            tvTitleContent.setText(etGroupName.getText().toString() + "（+" + "人数" + "）");
                            tvGroupMsgName.setText(etGroupName.getText().toString());
                        }
                    }
                });
    }

    /**
     * 修改我在本群名称的请求
     */
    private void updateGroupMineNameHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("member_app_user_id", DaoUtils.getUserId(GroupManagerActivity.this));
        map.put("group_id", targetId);
        map.put("member_nickname", etMineName.getText().toString());
        String params = EncryptionUtil.getParameter(GroupManagerActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/updateGroupMemberName.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        GroupManagerActivity.this, "修改中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(GroupManagerActivity.this, "修改失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        UpdateGroupMemberNameResult updateGroupMemberNameResult =
                                (UpdateGroupMemberNameResult) GsonUtil.json2Object(response, UpdateGroupMemberNameResult.class);
                        if (updateGroupMemberNameResult != null
                                && updateGroupMemberNameResult.getRet().equals("1")) {
                            Toasty.warning(GroupManagerActivity.this, "修改成功！", Toast.LENGTH_SHORT, false).show();
                            tvGroupMsgUserName.setText(etMineName.getText().toString());
                            getGroupMembers();
                        }
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void groupManagerBack() {
        GroupManagerActivity.this.finish();
    }

    /**
     * 解散群，退出群
     */
    @OnClick(R.id.btGroupManageSub)
    void btGroupManageSub() {
        if (isGroupMain.equals("yes")) {
            //解散
            deleteGroupHttp();
        } else {
            //退出
            quitGroupHttp();
        }
    }

    /**
     * 用户退群（非群主）
     */
    private void quitGroupHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("member_app_user_id", DaoUtils.getUserId(GroupManagerActivity.this));
        map.put("group_id", targetId);
        String params = EncryptionUtil.getParameter(GroupManagerActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/quitGroup.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_quitGroup")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        GroupManagerActivity.this, "退出中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(GroupManagerActivity.this, "退出失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        QuitGroupResult quitGroupResult =
                                (QuitGroupResult) GsonUtil.json2Object(response, QuitGroupResult.class);
                        if (quitGroupResult != null
                                && quitGroupResult.getRet().equals("1")) {
                            Intent intent = new Intent(GroupManagerActivity.this, GroupChatListActivity.class);
                            intent.putExtra("activity", "AddressBook");
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * 解散群（群主才能解散）
     */
    private void deleteGroupHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(GroupManagerActivity.this));
        map.put("group_id", targetId);
        String params = EncryptionUtil.getParameter(GroupManagerActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/deleteGroup.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_deleteGroup")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        GroupManagerActivity.this, "解散中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(GroupManagerActivity.this, "解散失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        DeleteGroupResult deleteGroupResult =
                                (DeleteGroupResult) GsonUtil.json2Object(response, DeleteGroupResult.class);
                        if (deleteGroupResult != null
                                && deleteGroupResult.getRet().equals("1")) {
                            Intent intent = new Intent(GroupManagerActivity.this, GroupChatListActivity.class);
                            intent.putExtra("activity", "AddressBook");
                            startActivity(intent);
                        }
                    }
                });
    }
}
