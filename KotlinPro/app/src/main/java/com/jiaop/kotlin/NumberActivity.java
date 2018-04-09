package com.jiaop.kotlin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiaop.libs.base.JPBaseActivity;
import com.jiaop.libs.views.JPCustomViewPager;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NumberActivity extends JPBaseActivity {

    @BindView(R.id.tabbar)
    JPTabBar mTabbar;
    @BindView(R.id.view_pager)
    JPCustomViewPager mViewPager;

    @Titles
    private static final String[] mTitles = {"首页", "商城", "消息", "我的"};

    @SeleIcons
    private static final int[] mSeleIcons = {R.drawable.home_red, R.drawable.store_red,
            R.drawable.msg_red, R.drawable.my_red};

    @NorIcons
    private static final int[] mNormalIcons = {R.drawable.home_black, R.drawable.store_black,
            R.drawable.msg_black, R.drawable.my_black};

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void initView() {
        fragments.add(new HomeFragment());
        fragments.add(new StoreFragment());
        fragments.add(new MsgFragment());
        fragments.add(new MyFragment());

        mViewPager.setPagingEnabled(false);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mTabbar.setContainer(mViewPager);

        mTabbar.showBadge(1,"99+");
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_number;
    }

    @Override
    protected void initWiFiData() {

    }

    @Override
    protected void initNetData() {

    }

    @Override
    protected void initOfflineData() {

    }

    @Override
    protected int statusBarColor() {
        return R.color.btColor;
    }


}


