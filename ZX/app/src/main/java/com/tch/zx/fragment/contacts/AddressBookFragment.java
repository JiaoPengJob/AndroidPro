package com.tch.zx.fragment.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.contacts.AttentionActivity;
import com.tch.zx.activity.contacts.GroupChatAllActivity;
import com.tch.zx.activity.contacts.NewFridensActivity;
import com.tch.zx.activity.message.FriendInfoActivity;
import com.tch.zx.activity.personal.DetailedInfoActivity;
import com.tch.zx.activity.personal.PersonalContentActivity;
import com.tch.zx.adapter.contacts.AddressBookAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.bean.CityBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.FriendListByAppUserIdResult;
import com.tch.zx.http.bean.result.FriendListResult;
import com.tch.zx.http.presenter.FriendListByAppUserIdPresenter;
import com.tch.zx.http.view.FriendListByAppUserIdView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.IndexBar;
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
 * 人脉/通讯录
 */

public class AddressBookFragment extends Fragment {

    private View viewRoot;
    /**
     * 联系人列表
     */
    @BindView(R.id.rvList)
    SwipeMenuRecyclerView rvList;
    /**
     * 侧边索引栏
     */
    @BindView(R.id.indexBar)
    IndexBar indexBar;
    /**
     * 点击索引栏提示信息
     */
    @BindView(R.id.tvSideBarHint)
    TextView tvSideBarHint;
    /**
     * 联系人列表适配器
     */
    private AddressBookAdapter addressBookAdapter;
    /**
     * 加载侧边栏信息
     */
    private List<CityBean> mDatas;
    private LinearLayoutManager mManager;
    /**
     * 跳转
     */
    private Intent intent;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private List<FriendListResult.ResponseObjectBean> friendListRobs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_address_book, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        friendListByAppUserId();
    }

    /**
     * 加载联系人列表数据
     */
    private void setAddressBookList() {
        mDatas = new ArrayList<CityBean>();
        for (FriendListResult.ResponseObjectBean rob : friendListRobs) {
            mDatas.add(new CityBean(rob.getName()));
        }

        addressBookAdapter = new AddressBookAdapter(getContext(), friendListRobs);
        rvList.setLayoutManager(mManager = new LinearLayoutManager(getContext()));
        //设置分割线
        rvList.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 1, false));
        rvList.setAdapter(addressBookAdapter);
        addressBookAdapter.setOnItemClickListener(new AddressBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(getContext(), FriendInfoActivity.class);
                intent.putExtra("userId", friendListRobs.get(position).getApp_user_id());
                startActivity(intent);
            }
        });

        indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mDatas);//设置数据源
    }

    private FriendListByAppUserIdPresenter presenter;

    /**
     * 获取好友列表
     */
    private void friendListByAppUserId() {
        presenter = new FriendListByAppUserIdPresenter(getContext());
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
            if (baseResultBean.getRet().equals("1") && baseResultBean.getResult() != null) {
                FriendListResult friendList = (FriendListResult) GsonUtil.parseJson(baseResultBean.getResult(), FriendListResult.class);
                friendListRobs = friendList.getResponseObject();
                setAddressBookList();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "friendListByAppUserIdView接口错误" + result);
        }
    };

    /**
     * 新朋友点击事件
     */
    @OnClick(R.id.rl_new_friends_contacts)
    public void newFriends() {
        intent = new Intent(getContext(), NewFridensActivity.class);
        startActivity(intent);
    }


    /**
     * 关注点击事件
     */
    @OnClick(R.id.rl_attention_contacts)
    public void attentionClick() {
        intent = new Intent(getContext(), AttentionActivity.class);
        startActivity(intent);
    }


    /**
     * 群聊点击事件
     */
    @OnClick(R.id.rl_group_chat)
    public void groupChatClick() {
        intent = new Intent(getContext(), GroupChatAllActivity.class);
        intent.putExtra("groupType", "addressBook");
        startActivity(intent);
    }
}
