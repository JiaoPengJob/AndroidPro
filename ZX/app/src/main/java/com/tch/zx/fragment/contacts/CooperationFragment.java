package com.tch.zx.fragment.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.adapter.ChiefAdapter;
import com.tch.zx.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 人脉/合作
 */
public class CooperationFragment extends Fragment {
    @BindView(R.id.cvp_view_pager_cooperation)
    CustomViewPager cvp_view_pager_cooperation;
    @BindView(R.id.fth_host_cooperation)
    FragmentTabHost fth_host_cooperation;
    @BindView(R.id.iv_release_industry)
    ImageView iv_release_industry;

    private View viewRoot;
    private LayoutInflater inflater;

    /**
     * 加载Fragment的集合
     */
    private List<Fragment> fragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_cooperation, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    /**
     * 初始化
     */
    private void initView() {
        initTabHost();
        initPager();
        bindTabAndPager();
    }

    private void initTabHost() {
        fth_host_cooperation.setup(getContext(), getChildFragmentManager(), R.id.fragment);
        fth_host_cooperation.getTabWidget().setDividerDrawable(null);
        fth_host_cooperation.addTab(fth_host_cooperation.newTabSpec("cooperation_first").setIndicator(createView(R.drawable.selector_industry, "行业")), IndustryFragment.class, null);
        fth_host_cooperation.addTab(fth_host_cooperation.newTabSpec("cooperation_give").setIndicator(createView(R.drawable.selector_give, "提供")), OfferIndustryFragment.class, null);
        fth_host_cooperation.addTab(fth_host_cooperation.newTabSpec("cooperation_industry").setIndicator(createView(R.drawable.selector_demand, "需求")), NeedIndustryFragment.class, null);
    }

    /**
     * 初始化 pager 绑定适配器
     */
    private void initPager() {
        fragments = new ArrayList<>();
        fragments.add(new IndustryFragment());
        fragments.add(new OfferIndustryFragment());
        fragments.add(new NeedIndustryFragment());
        ChiefAdapter adapter = new ChiefAdapter(getChildFragmentManager(), fragments);
        cvp_view_pager_cooperation.setAdapter(adapter);
    }

    /**
     * 为TabHost和viewPager 添加监听 让其联动
     */
    private void bindTabAndPager() {
        fth_host_cooperation.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                iv_release_industry.setVisibility(View.VISIBLE);
                int position = fth_host_cooperation.getCurrentTab();
                if (position != 3) {
                    ftransaction.remove(releaseSynergismFragment);
                }
                cvp_view_pager_cooperation.setCurrentItem(position, true);
            }
        });
        //设置Viewpager不可滑动
        cvp_view_pager_cooperation.setPagingEnabled(false);
    }

    /**
     * 创建图标
     *
     * @param icon
     * @param tab
     * @return 返回view
     */
    private View createView(int icon, String tab) {
        View view = inflater.inflate(R.layout.layout_tab_cooperation_top, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_top_menu_img);
        TextView title = (TextView) view.findViewById(R.id.icon_top_menu_text);
        imageView.setImageResource(icon);
        title.setText(tab);
        return view;
    }

    private FragmentTransaction ftransaction;
    private ReleaseSynergismFragment releaseSynergismFragment;

    /**
     * 发布点击事件
     */
    @OnClick(R.id.iv_release_industry)
    public void releaseIndustryClick() {
//        info_default_industry.setVisibility(View.GONE);
//        release_industry.setVisibility(View.VISIBLE);
//        setGroupData(ftg_release_industry, true);
//        setGroupData(ftg_release_give, true);
//        setGroupData(ftg_release_demand, true);
        iv_release_industry.setVisibility(View.GONE);
        ftransaction = getChildFragmentManager().beginTransaction();
        releaseSynergismFragment = new ReleaseSynergismFragment();
        ftransaction.replace(R.id.fragment, releaseSynergismFragment);
        ftransaction.commit();
    }
}
