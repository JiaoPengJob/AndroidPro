package com.tch.zx.activity.message;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateGroupInfoActivity extends AppCompatActivity {

    @BindView(R.id.tvUGITitle)
    TextView tvUGITitle;
    @BindView(R.id.etInputGroupName)
    EditText etInputGroupName;

    private String title, groupName, appUserId, nickName, groupId;
    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_update_group_info);
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
        if (getIntent().getStringExtra("title") != null) {
            title = getIntent().getStringExtra("title");
            tvUGITitle.setText(title);
        }
        if (getIntent().getStringExtra("groupId") != null) {
            groupId = getIntent().getStringExtra("groupId");
        }

        if (getIntent().getStringExtra("groupName") != null) {
            groupName = getIntent().getStringExtra("groupName");
            etInputGroupName.setText(groupName);
        }
        if (getIntent().getStringExtra("appUserId") != null) {
            appUserId = getIntent().getStringExtra("appUserId");
        }
        if (getIntent().getStringExtra("nickName") != null) {
            nickName = getIntent().getStringExtra("nickName");
            etInputGroupName.setText(nickName);
        }
    }

    /**
     * 保存
     */
    @OnClick(R.id.tvUpdateGroupInfoSub)
    public void updateGroupInfoSub() {
        if (title.equals("群聊名称")) {
            modifyGroupName();
        } else if (title.equals("我在本群昵称")) {
            modifyGroupMemberNickName();
        }
    }

    /**
     * 修改群名称
     */
    private void modifyGroupName() {
        presenter = new BasePresenter<Object>(UpdateGroupInfoActivity.this);
        presenter.onCreate();
        presenter.attachView(modifyGroupNameView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupId", groupId);
        map.put("groupName", etInputGroupName.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.modifyGroupName(data);
    }

    private BaseView<Object> modifyGroupNameView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                Intent intent = new Intent();
                intent.putExtra("newGroupName", etInputGroupName.getText().toString());
                setResult(10, intent);
                UpdateGroupInfoActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "modifyGroupNameView接口错误" + result);
        }
    };

    /**
     * 修改群昵称
     */
    private void modifyGroupMemberNickName() {
        presenter = new BasePresenter<Object>(UpdateGroupInfoActivity.this);
        presenter.onCreate();
        presenter.attachView(modifyGroupMemberNickNameView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("groupId", groupId);
        map.put("nickName", etInputGroupName.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.modifyGroupMemberNickName(data);
    }

    private BaseView<Object> modifyGroupMemberNickNameView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                Intent intent = new Intent();
                intent.putExtra("newNikeName", etInputGroupName.getText().toString());
                setResult(11, intent);
                UpdateGroupInfoActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "modifyGroupMemberNickNameView接口错误" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.llUGIReturnBack)
    public void uGIReturnBack() {
        UpdateGroupInfoActivity.this.finish();
    }
}
