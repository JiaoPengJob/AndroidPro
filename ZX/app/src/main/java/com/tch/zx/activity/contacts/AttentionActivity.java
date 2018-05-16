package com.tch.zx.activity.contacts;

import android.content.Intent;
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
import com.tch.zx.activity.personal.DetailedInfoActivity;
import com.tch.zx.adapter.contacts.AttentionAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.FriendListResult;
import com.tch.zx.http.bean.result.GetFollowUserListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.presenter.Presenter;
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

/**
 * 通讯录/已关注
 */
public class AttentionActivity extends BaseActivity {

    /**
     * 列表
     */
    @BindView(R.id.smrv_attention)
    SwipeMenuRecyclerView smrv_attention;
    @BindView(R.id.refreshAttention)
    SmartRefreshLayout refreshAttention;

    /**
     * 适配器
     */
    private AttentionAdapter attentionAdapter;
    private BasePresenter<Object> presenter;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private List<FriendListResult.ResponseObjectBean> attentionListRobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        refreshAttention.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getFollowUserListByAppUserId();
            }
        });
        getFollowUserListByAppUserId();
    }

    private void getFollowUserListByAppUserId() {
        presenter = new BasePresenter<Object>(AttentionActivity.this);
        presenter.onCreate();
        presenter.attachView(getFollowUserListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.getFollowUserListByAppUserId(data);
    }

    private BaseView<Object> getFollowUserListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshAttention.isRefreshing()) {
                refreshAttention.finishRefresh();
            }
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                FriendListResult friendList = (FriendListResult) GsonUtil.parseJson(baseResultBean.getResult(), FriendListResult.class);
                attentionListRobs = friendList.getResponseObject();
                setListData();
            }
        }

        @Override
        public void onError(String result) {
            if (refreshAttention.isRefreshing()) {
                refreshAttention.finishRefresh();
            }
            Log.e("ZX", "getFollowUserListView接口错误" + result);
        }
    };

    /**
     * 记载列表数据
     */
    private void setListData() {
        attentionAdapter = new AttentionAdapter(this, userBeanDao.loadAll().get(0).getAppUserId(), attentionListRobs);

        smrv_attention.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        smrv_attention.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 1, false));
        smrv_attention.setAdapter(attentionAdapter);
        attentionAdapter.setOnItemClickListener(new AttentionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AttentionActivity.this, DetailedInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_attention)
    public void returnAttention() {
        this.finish();
    }

}
