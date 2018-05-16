package com.tch.kuwanx.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.zhouwei.library.CustomPopWindow;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.HomeActivity;
import com.tch.kuwanx.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jiaop on 2017/10/24.
 * 消息
 * 设置消息未读数
 * ((MainActivity) getActivity()).updateMsgCount(3, 999);
 */
public class MsgFragment extends Fragment {

    @BindView(R.id.msgTabView)
    SegmentTabLayout msgTabView;
    @BindView(R.id.cvpMsg)
    CustomViewPager cvpMsg;
    @BindView(R.id.rlMsgTitle)
    RelativeLayout rlMsgTitle;
    @BindView(R.id.viewBgmDark)
    View viewBgmDark;

    private View viewRoot;
    private String[] mTitles = {"消息", "通讯录"};
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Intent intent;

    public static MsgFragment getInstance() {
        MsgFragment sf = new MsgFragment();
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_msg, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    public static FragmentPagerAdapter fpa;

    private void initView() {
        msgTabView.setTabData(mTitles);
        fragments.add(MsgMainFragment.getInstance());
        fragments.add(AddressBookFragment.getInstance());
        cvpMsg.setAdapter(fpa = new FragmentPagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

        });
        msgTabView.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                cvpMsg.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        cvpMsg.setPagingEnabled(false);
    }

    /**
     * 显示添加按钮内容
     */
    @OnClick(R.id.ibMsgAddMenu)
    public void msgAddMenuClick() {
        showMsgAddMenu();
    }

    private CustomPopWindow msgMenuPop;
    private LinearLayout llSponsorGroupChat, llAddFriends, llInviteFriends;

    private void showMsgAddMenu() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_msg_menu, null);
        llSponsorGroupChat = (LinearLayout) view.findViewById(R.id.llSponsorGroupChat);
        llAddFriends = (LinearLayout) view.findViewById(R.id.llAddFriends);
        llInviteFriends = (LinearLayout) view.findViewById(R.id.llInviteFriends);
        llSponsorGroupChat.setOnClickListener(new MenuClickListener());
        llAddFriends.setOnClickListener(new MenuClickListener());
        llInviteFriends.setOnClickListener(new MenuClickListener());
        msgMenuPop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(view)
                .size(280, 280)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewBgmDark.setVisibility(View.GONE);
                        ((HomeActivity) getActivity()).getViewHomeMenuDark().setVisibility(View.GONE);
                    }
                })
                .create()
                .showAsDropDown(rlMsgTitle, 0, 2, Gravity.RIGHT);
        viewBgmDark.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).getViewHomeMenuDark().setVisibility(View.VISIBLE);
    }

    private class MenuClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (msgMenuPop != null) {
                msgMenuPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llSponsorGroupChat:
                    intent = new Intent(getActivity(), ChooseContactActivity.class);
                    startActivity(intent);
                    break;
                case R.id.llAddFriends:
                    intent = new Intent(getActivity(), AddNewFriendActivity.class);
                    startActivity(intent);
                    break;
                case R.id.llInviteFriends:

                    break;
            }
        }
    }
}
