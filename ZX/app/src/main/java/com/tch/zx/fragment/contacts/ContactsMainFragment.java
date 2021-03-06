package com.tch.zx.fragment.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tch.zx.R;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.activity.contacts.SendDynamicActivity;
import com.tch.zx.util.ActionItem;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.TitlePopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 人脉/主页
 */

public class ContactsMainFragment extends Fragment {

    /**
     * Fragment父布局
     */
    private View viewRoot;

    /**
     * 整体选项卡布局
     */
    @BindView(R.id.stl_contact_main)
    SmartTabLayout stl_contact_main;
    /**
     * 整体选项卡子布局ViewPager
     */
    @BindView(R.id.vp_contact_main)
    ViewPager vp_contact_main;
    /**
     * 加号弹出窗口的按钮
     */
    @BindView(R.id.iv_add_menu)
    ImageView iv_add_menu;
    /**
     * 照相按钮
     */
    @BindView(R.id.iv_camera_menu)
    ImageView iv_camera_menu;

    /**
     * 弹窗
     */
    private TitlePopup titlePopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_contact_main, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        setViewPagersData();
        initPopMenuView();
        return viewRoot;
    }

    /**
     * 加载首页tabView信息
     */
    private void setViewPagersData() {
        //向选项卡布局中添加子布局Fragment，
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add("通讯录", AddressBookFragment.class)
                .add("合作", CooperationFragment.class)
                .add("动态", DynamicFragment.class)
                .create());
        vp_contact_main.setAdapter(adapter);
        stl_contact_main.setViewPager(vp_contact_main);

        stl_contact_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    iv_add_menu.setVisibility(View.VISIBLE);
                    iv_camera_menu.setVisibility(View.GONE);
                } else if (position == 1) {
                    iv_add_menu.setVisibility(View.GONE);
                    iv_camera_menu.setVisibility(View.GONE);
                } else if (position == 2) {
                    iv_add_menu.setVisibility(View.GONE);
                    iv_camera_menu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化组件
     */
    private void initPopMenuView() {
        //实例化标题栏按钮并设置监听
        iv_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });

        //实例化标题栏弹窗
        titlePopup = new TitlePopup(getContext(), (int) Math.floor(HelperUtil.getScreenWidth(getContext()) / 2), 400);
//        titlePopup = new TitlePopup(getContext(),300, 360);
        initMenuData();
    }

    /**
     * 初始化数据
     */
    private void initMenuData() {
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(getContext(), "添加好友", R.mipmap.add_friend_menu));
        titlePopup.addAction(new ActionItem(getContext(), "发起群聊", R.mipmap.togeter_chat_menu));
        titlePopup.addAction(new ActionItem(getContext(), "邀请好友", R.mipmap.send_friends_menu));
    }

    /**
     * 发送朋友圈动态
     */
    @OnClick(R.id.iv_camera_menu)
    public void cameraSendDynamic() {
        Intent intent = new Intent(getContext(), SendDynamicActivity.class);
        startActivity(intent);
    }
}
