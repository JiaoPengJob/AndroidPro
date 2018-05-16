package com.tch.zx.activity.message;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.contacts.GroupChatAllActivity;
import com.tch.zx.adapter.message.SelectPeoplesAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.FriendListResult;
import com.tch.zx.http.bean.result.GetGroupListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.presenter.FriendListByAppUserIdPresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.http.view.FriendListByAppUserIdView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择联系人
 */
public class SelectFriendsActivity extends BaseActivity {

    /**
     * 联系人列表
     */
    @BindView(R.id.rv_peoples)
    RecyclerView rv_peoples;
    @BindView(R.id.tv_build_new_group)
    TextView tv_build_new_group;
    @BindView(R.id.refreshPeoples)
    SmartRefreshLayout refreshPeoples;

    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private List<FriendListResult.ResponseObjectBean> friendListRobs;
    private FriendListByAppUserIdPresenter presenter;
    private List<FriendListResult.ResponseObjectBean> selectList;
    private SelectPeoplesAdapter selectPeoplesAdapter;
    private BasePresenter<Object> basePresenter;
    private StringBuffer sb;
    private String type, groupName;
    private GetGroupListResult.ResponseObjectBean groupInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_select_friends);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();

        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
            tv_build_new_group.setText("邀请");
        }

        if (getIntent().getSerializableExtra("groupInfoResult") != null) {
            groupInfo = (GetGroupListResult.ResponseObjectBean) getIntent().getSerializableExtra("groupInfoResult");
        }
        if (getIntent().getStringExtra("groupName") != null) {
            groupName = getIntent().getStringExtra("groupName");
        }

        friendListByAppUserId();

        refreshPeoples.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                friendListByAppUserId();
            }
        });
    }

    /**
     * 获取好友列表
     */
    private void friendListByAppUserId() {
        presenter = new FriendListByAppUserIdPresenter(SelectFriendsActivity.this);
        presenter.onCreate();
        presenter.attachView(friendListByAppUserIdView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.getFriendListByAppUserId(data);
    }

    private FriendListByAppUserIdView friendListByAppUserIdView = new FriendListByAppUserIdView() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshPeoples.isRefreshing()) {
                refreshPeoples.finishRefresh();
            }
            if (baseResultBean.getRet().equals("1") && baseResultBean.getResult() != null) {
                FriendListResult friendList = (FriendListResult) GsonUtil.parseJson(baseResultBean.getResult(), FriendListResult.class);
                friendListRobs = friendList.getResponseObject();
                setPeoplesData();
            }
        }

        @Override
        public void onError(String result) {
            if (refreshPeoples.isRefreshing()) {
                refreshPeoples.finishRefresh();
            }
            Log.e("ZX", "friendListByAppUserIdView接口错误" + result);
        }
    };

    /**
     * 加载联系人列表数据
     */
    private void setPeoplesData() {
        selectPeoplesAdapter = new SelectPeoplesAdapter(this, friendListRobs);
        rv_peoples.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_peoples.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 1, true));
        rv_peoples.setAdapter(selectPeoplesAdapter);
    }

    /**
     * 创建
     */
    @OnClick(R.id.tv_build_new_group)
    public void buildNewGroup() {
        selectList = selectPeoplesAdapter.getSelectedItem();
        sb = new StringBuffer();
        for (FriendListResult.ResponseObjectBean s : selectList) {
            sb.append(s.getApp_user_id() + ",");
        }
        sb.append(userBeanDao.loadAll().get(0).getAppUserId());
        if (tv_build_new_group.getText().toString().equals("创建")) {
            createGroupByParams();
        } else if (tv_build_new_group.getText().toString().equals("邀请")) {
            addGroupMember();
        }
    }

    /**
     * 添加好友
     */
    private void addGroupMember() {
        String param = sb.toString().substring(0, sb.toString().lastIndexOf(","));
        basePresenter = new BasePresenter<Object>(SelectFriendsActivity.this);
        basePresenter.onCreate();
        basePresenter.attachView(addGroupMemberView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupId", groupInfo.getId());
        map.put("groupName", groupName);
        map.put("memberList", param);

        String data = HelperUtil.getParameter(map);
        basePresenter.addGroupMember(data);
    }

    private BaseView<Object> addGroupMemberView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {

                for (FriendListResult.ResponseObjectBean s : selectList) {
                    groupInfo.getMenberList().add(new GetGroupListResult.ResponseObjectBean.MenberListBean(
                            String.valueOf(s.isIs_friend()),
                            s.getApp_user_id(),
                            s.getName(),
                            s.getName(),
                            s.getUser_picture()
                    ));
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("groupInfoResult", groupInfo);
                intent.putExtras(bundle);
                setResult(12, intent);
                SelectFriendsActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "addGroupMemberView接口错误" + result);
        }
    };


    /**
     * 创建群组
     */
    private void createGroupByParams() {
        basePresenter = new BasePresenter(SelectFriendsActivity.this);
        basePresenter.onCreate();
        basePresenter.attachView(createGroupView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("groupNickName", "群聊");
        map.put("memberList", sb.toString());

        String data = HelperUtil.getParameter(map);
        basePresenter.createGroupByParams(data);
    }

    private BaseView<Object> createGroupView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                Intent intent = new Intent(SelectFriendsActivity.this, GroupChatAllActivity.class);
                startActivity(intent);
                SelectFriendsActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "createGroupView接口错误" + result);
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_select_friends)
    public void returnBackSelectFriends() {
        this.finish();
    }

}
