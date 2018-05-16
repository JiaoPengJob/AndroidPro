package com.tch.zx.activity.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.message.GroupChatActivity;
import com.tch.zx.adapter.message.GroupChatAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.GetGroupListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * 加入的所有群组
 */
public class GroupChatAllActivity extends BaseActivity {
    /**
     * 列表
     */
    @BindView(R.id.smrv_together_msggroup_chat_receive)
    SwipeMenuRecyclerView smrv_together_msggroup_chat_receive;
    @BindView(R.id.refreshTogetherChatD)
    SmartRefreshLayout refreshTogetherChatD;

    private BasePresenter<Object> presenter;
    private GetGroupListResult getGroupListResult;
    private GetGroupListResult newGroupListResult = new GetGroupListResult();
    /**
     * 适配器
     */
    private GroupChatAdapter groupChatAdapter;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private String groupType;
    private List<Conversation> groups = new ArrayList<Conversation>();
    private List<GetGroupListResult.ResponseObjectBean> newListnew = new ArrayList<GetGroupListResult.ResponseObjectBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_group_chat_all);
        ButterKnife.bind(this);

        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();

        if (getIntent().getStringExtra("groupType") != null) {
            groupType = getIntent().getStringExtra("groupType");
        }

        refreshTogetherChatD.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getContentList();
                getGroupListByAppUserId();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentList();
        getGroupListByAppUserId();
    }

    private void getGroupListByAppUserId() {
        presenter = new BasePresenter<Object>(GroupChatAllActivity.this);
        presenter.onCreate();
        presenter.attachView(getGroupListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.getGroupListByAppUserId(data);
    }

    private BaseView<Object> getGroupListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshTogetherChatD.isRefreshing()) {
                refreshTogetherChatD.finishRefresh();
            }
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                getGroupListResult = (GetGroupListResult) GsonUtil.parseJson(baseResultBean.getResult(), GetGroupListResult.class);
                setData();
            }
        }

        @Override
        public void onError(String result) {
            if (refreshTogetherChatD.isRefreshing()) {
                refreshTogetherChatD.finishRefresh();
            }
            Log.e("ZX", "getGroupListView接口错误" + result);
        }
    };

    /**
     * 加载列表数据
     */
    private void setData() {
        //在适配器中进行判断是否显示全部，应做修改
//        if (groupType.equals("unGroup")) {
        if (groups.size() > 0) {
            for (int i = 0; i < getGroupListResult.getResponseObject().size(); i++) {
                for (int j = 0; j < groups.size(); j++) {
                    getGroupListResult.getResponseObject().get(i).setLastMessage(groups.get(j).getLatestMessage().encode().toString());
                    getGroupListResult.getResponseObject().get(i).setLastTime(String.valueOf(groups.get(j).getReceivedTime()));
                    if (getGroupListResult.getResponseObject().get(i).getId() == groups.get(j).getTargetId()) {
                        getGroupListResult.getResponseObject().get(i).setGroupType("unGroup");
                    } else {
                        getGroupListResult.getResponseObject().get(i).setGroupType("addressBook");
                    }
                    newListnew.add(getGroupListResult.getResponseObject().get(i));
                }
            }
            newGroupListResult.setResponseObject(newListnew);
        }
//        } else {
//            newGroupListResult = getGroupListResult;
//        }

        if (newGroupListResult != null) {
            groupChatAdapter = new GroupChatAdapter(this, newGroupListResult.getResponseObject(), groupType);
            smrv_together_msggroup_chat_receive.setLayoutManager(new LinearLayoutManager(this));
//            //设置分割线
//            smrv_together_msggroup_chat_receive.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 1, false));
            smrv_together_msggroup_chat_receive.setAdapter(groupChatAdapter);
            groupChatAdapter.setOnItemClickListener(new GroupChatAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                Intent intent = new Intent(GroupChatAllActivity.this, GroupChatActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("groupInfo", getGroupListResult.getResponseObject().get(position));
//                intent.putExtras(bundle);
//                startActivity(intent);
//                RongIM.getInstance().startConversation(GroupChatAllActivity.this, Conversation.ConversationType.GROUP, getGroupListResult.getResponseObject().get(position).getId(), getGroupListResult.getResponseObject().get(position).getGroup_nickname());
                    Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                            .appendPath("conversation").appendPath(Conversation.ConversationType.GROUP.getName().toLowerCase())
                            .appendQueryParameter("targetId", newGroupListResult.getResponseObject().get(position).getId())
                            .appendQueryParameter("title", newGroupListResult.getResponseObject().get(position).getGroup_nickname())
                            .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("groupInfo", newGroupListResult.getResponseObject().get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    private void getContentList() {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversations) {
                    if (conversations != null && conversations.size() > 0) {
                        groups = conversations;
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("TAG", "获取群聊会话列表==" + errorCode.getMessage());
                }
            }, Conversation.ConversationType.GROUP);
        }
    }

    /**
     * 后退
     */
    @OnClick(R.id.ll_group_chat_title_return)
    public void returnBack() {
        this.finish();
    }
}
