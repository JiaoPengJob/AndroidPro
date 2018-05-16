package com.tch.zx.activity.message;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tch.zx.R;
import com.tch.zx.adapter.message.GroupUserInfoAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.GetGroupListResult;
import com.tch.zx.http.bean.result.GroupMemberListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.SwitchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群组信息页
 */
public class GroupInfoActivity extends AppCompatActivity {

    /**
     * 群成员列表
     */
    @BindView(R.id.gv_group_user_number)
    GridView gv_group_user_number;
    //消息免打扰
    @BindView(R.id.sv_no_disturb)
    SwitchView sv_no_disturb;
    @BindView(R.id.tvGroupInfoName)
    TextView tvGroupInfoName;
    @BindView(R.id.tvGroupInfoNikeName)
    TextView tvGroupInfoNikeName;

    //群成员适配器
    private GroupUserInfoAdapter groupUserInfoAdapter;
    private GetGroupListResult.ResponseObjectBean groupInfo;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private BasePresenter<Object> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_group_info);
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
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        if (getIntent().getSerializableExtra("groupInfo") != null) {
            groupInfo = (GetGroupListResult.ResponseObjectBean) getIntent().getSerializableExtra("groupInfo");
            tvGroupInfoName.setText(groupInfo.getGroup_nickname());
            tvGroupInfoNikeName.setText(groupInfo.getMember_nickname());
            setGroupPeople(groupInfo.getMenberList());
        }
        setSwitchButton();
    }

    /**
     * 加载群成员列表
     */
    private void setGroupPeople(final List<GetGroupListResult.ResponseObjectBean.MenberListBean> list) {
        groupUserInfoAdapter = new GroupUserInfoAdapter(this, list);
        gv_group_user_number.setAdapter(groupUserInfoAdapter);
        groupUserInfoAdapter.notifyDataSetChanged();
        gv_group_user_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == list.size() - 1) {
                    groupInfo.getMenberList().remove(groupInfo.getMenberList().size() - 1);
                    Intent intent = new Intent(GroupInfoActivity.this, SelectFriendsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("groupInfoResult", groupInfo);
                    bundle.putString("groupName", tvGroupInfoName.getText().toString());
                    bundle.putString("type", "add");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 12);
                }
            }
        });
    }

    /**
     * 查看更多群成员
     */
    @OnClick(R.id.llLookAllGroupPeople)
    public void llLookAllGroupPeople() {
        getGroupMemberList();
    }

    private void getGroupMemberList() {
        presenter = new BasePresenter<Object>(GroupInfoActivity.this);
        presenter.onCreate();
        presenter.attachView(getGroupMemberListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("groupId", groupInfo.getId());

        String data = HelperUtil.getParameter(map);
        presenter.getGroupMemberList(data);
    }

    private BaseView<Object> getGroupMemberListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                GroupMemberListResult groupMemberListResult = (GroupMemberListResult) GsonUtil.parseJson(baseResultBean.getResult(), GroupMemberListResult.class);
                if (groupMemberListResult.getResponseObject() != null) {
                    setGroupPeople(groupMemberListResult.getResponseObject());
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "getGroupMemberListView接口错误" + result);
        }
    };


    /**
     * 设置免打扰选择
     */
    private void setSwitchButton() {
        sv_no_disturb.setState(false);

        sv_no_disturb.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                sv_no_disturb.toggleSwitch(true);
            }

            @Override
            public void toggleToOff() {
                sv_no_disturb.toggleSwitch(false);
            }
        });
    }

    /**
     * 修改群聊名称
     */
    @OnClick(R.id.rlGroupInfoName)
    public void groupInfoName() {
        if (groupInfo.getApp_user_id().equals(userBeanDao.loadAll().get(0).getAppUserId())) {
            Intent intent = new Intent(GroupInfoActivity.this, UpdateGroupInfoActivity.class);
            intent.putExtra("title", "群聊名称");
            intent.putExtra("groupId", groupInfo.getId());
            intent.putExtra("groupName", groupInfo.getGroup_nickname());
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(GroupInfoActivity.this, "无权限！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 修改自己的昵称
     */
    @OnClick(R.id.rlMineNikeName)
    public void mineNikeName() {
        Intent intent = new Intent(GroupInfoActivity.this, UpdateGroupInfoActivity.class);
        intent.putExtra("title", "我在本群昵称");
        intent.putExtra("groupId", groupInfo.getId());
        intent.putExtra("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        intent.putExtra("nickName", groupInfo.getMember_nickname());
        startActivityForResult(intent, 11);
    }

    private String newGroupName, newNikeName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            newGroupName = data.getStringExtra("newGroupName");
            tvGroupInfoName.setText(newGroupName);
        } else if (resultCode == 11) {
            newNikeName = data.getStringExtra("newNikeName");
            tvGroupInfoNikeName.setText(newNikeName);
        } else if (resultCode == 12) {
            groupInfo = (GetGroupListResult.ResponseObjectBean) data.getSerializableExtra("groupInfoResult");
            setGroupPeople(groupInfo.getMenberList());
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.llReturnBackGI)
    public void returnBackGI() {
        groupInfo.getMenberList().remove(groupInfo.getMenberList().size() - 1);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("groupInfoResult", groupInfo);
        bundle.putString("newGroupName", tvGroupInfoName.getText().toString());
        bundle.putString("newNikeName", tvGroupInfoNikeName.getText().toString());
        intent.putExtras(bundle);
        setResult(1, intent);
        GroupInfoActivity.this.finish();
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
            groupInfo.getMenberList().remove(groupInfo.getMenberList().size() - 1);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("groupInfoResult", groupInfo);
            bundle.putString("newGroupName", tvGroupInfoName.getText().toString());
            bundle.putString("newNikeName", tvGroupInfoNikeName.getText().toString());
            intent.putExtras(bundle);
            setResult(1, intent);
            GroupInfoActivity.this.finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
