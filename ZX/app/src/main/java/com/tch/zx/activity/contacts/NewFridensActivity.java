package com.tch.zx.activity.contacts;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.activity.message.FriendInfoActivity;
import com.tch.zx.adapter.contacts.NewFriendsAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.FriendAppyResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
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
 * 新朋友
 */
public class NewFridensActivity extends BaseActivity {

    /**
     * 列表
     */
    @BindView(R.id.rv_add_new_friends)
    RecyclerView rv_add_new_friends;
    @BindView(R.id.refreshNewFriends)
    SmartRefreshLayout refreshNewFriends;

    /**
     * 列表适配器
     */
    private NewFriendsAdapter newFriendsAdapter;
    /**
     * dialog提示内容
     */
    private TextView tv_dialog_context;
    /**
     * dialog的取消
     */
    private TextView tv_exit_dialog;
    /**
     * dialog的确定
     */
    private TextView tv_sure_dialog;

    private Intent intent;
    private BasePresenter presenter;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private List<FriendAppyResult.ResponseObjectBean> friendAppyRobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_new_fridens);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        queryFriend();
        refreshNewFriends.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                queryFriend();
            }
        });
    }

    /**
     * 获取好友请求列表
     */
    private void queryFriend() {
        presenter = new BasePresenter<Object>(NewFridensActivity.this);
        presenter.onCreate();
        presenter.attachView(queryFriendAppyListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.queryFriendAppyList(data);

    }

    private BaseView<Object> queryFriendAppyListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshNewFriends.isRefreshing()) {
                refreshNewFriends.finishRefresh();
            }
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                FriendAppyResult friendAppy = (FriendAppyResult) GsonUtil.parseJson(baseResultBean.getResult(), FriendAppyResult.class);
                friendAppyRobs = friendAppy.getResponseObject();
                setListData();
            }
        }

        @Override
        public void onError(String result) {
            if (refreshNewFriends.isRefreshing()) {
                refreshNewFriends.finishRefresh();
            }
            Log.e("ZX", "queryFriendAppyListView接口错误" + result);
        }
    };


    /**
     * 加载列表数据
     */
    private void setListData() {
        newFriendsAdapter = new NewFriendsAdapter(this, friendAppyRobs);
        rv_add_new_friends.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_add_new_friends.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 1, false));
        rv_add_new_friends.setAdapter(newFriendsAdapter);
        newFriendsAdapter.setOnItemClickListener(new NewFriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(NewFridensActivity.this, FriendInfoActivity.class);
                intent.putExtra("activity", "NewFridensActivity");
                intent.putExtra("userId", friendAppyRobs.get(position).getApp_user_id());
                startActivity(intent);
            }
        });
    }

    /**
     * 清除点击事件
     */
    @OnClick(R.id.tv_clear_list_data)
    public void clearData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_pay_column, null);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        tv_dialog_context = (TextView) layout.findViewById(R.id.tv_dialog_context);
        tv_exit_dialog = (TextView) layout.findViewById(R.id.tv_exit_dialog);
        tv_sure_dialog = (TextView) layout.findViewById(R.id.tv_sure_dialog);
        tv_dialog_context.setText("清除所有新朋友添加信息");
        //取消
        tv_exit_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        //确定
        tv_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_add_new_friends.removeAllViews();
                friendAppyRobs.clear();
                newFriendsAdapter.notifyDataSetChanged();
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_new_friend)
    public void returnBackNewFriend() {
        this.finish();
    }


}
