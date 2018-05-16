package com.tch.zx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.line.PurchasedActivity;
import com.tch.zx.activity.line.SearchMainActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.TabBar;
import com.tch.zx.dao.green.TabBarDao;
import com.tch.zx.http.bean.result.TypeResultBean;
import com.tch.zx.http.presenter.TypePresenter;
import com.tch.zx.http.view.TypeView;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页推荐的展示页面
 */

public class ChiefFragment extends Fragment {

    /**
     * Fragment父布局
     */
    private View viewRoot;

    /**
     * 整体选项卡布局
     */
    @BindView(R.id.stl_recomment_main)
    SmartTabLayout stl_recomment_main;
    /**
     * 整体选项卡子布局ViewPager
     */
    @BindView(R.id.vp_recomment_main)
    ViewPager vp_recomment_main;
    /**
     * 下拉刷新布局
     */
    @BindView(R.id.refresh_chief)
    SmartRefreshLayout refresh_chief;
    /**
     * 获取选项卡内容
     */
    private TypePresenter typePresenter;
    /**
     * 请求参数
     */
    private String data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_chief, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);

        daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
        typeDao = daoSession.getTabBarDao();

        initView();

        return viewRoot;
    }

    /**
     * 初始化
     */
    private void initView() {
        if (typeDao.loadAll() != null && typeDao.loadAll().size() > 0) {
            setViewPagersData(typeDao.loadAll());
        } else {
            typePresenter = new TypePresenter(getContext());
            typePresenter.onCreate();
            typePresenter.attachView(typeView);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("param", "0");
            data = HelperUtil.getParameter(map);
            typePresenter.type(data);
        }
        refresh_chief.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                typePresenter = new TypePresenter(getContext());
                typePresenter.onCreate();
                typePresenter.attachView(typeView);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("param", "0");
                data = HelperUtil.getParameter(map);
                typePresenter.type(data);
            }
        });
    }

    /**
     * 加载首页tabView信息
     */
    private void setViewPagersData(List<TabBar> list) {
        FragmentPagerItems items = new FragmentPagerItems(getContext());
        items.add(FragmentPagerItem.of("推荐", RecommendFragment.class));
        for (int i = 0; i < list.size(); i++) {
            items.add(FragmentPagerItem.of(list.get(i).getTypeStr(), IntentnetFragment.class));
        }

        //向选项卡布局中添加子布局Fragment，
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), items);
        vp_recomment_main.setAdapter(adapter);
        stl_recomment_main.setViewPager(vp_recomment_main);
    }

    /**
     * 搜索按钮点击事件
     */
    @OnClick(R.id.ib_search_main)
    public void searchMainOnClick() {
        Intent intent = new Intent(getContext(), SearchMainActivity.class);
        startActivity(intent);
    }

    private DaoSession daoSession;
    private TabBarDao typeDao;

    /**
     * 缓存首页标签
     */
    private void cacheType(List<TypeResultBean.ResponseObject> list) {
        for (TypeResultBean.ResponseObject ro : list) {
            typeDao.insert(new TabBar(ro.getTypeId(), ro.getTypeName()));
        }
    }

    /**
     * 已购点击事件
     */
    @OnClick(R.id.tv_has_buy)
    public void hasBuyOnClick() {
        Intent intent = new Intent(getContext(), PurchasedActivity.class);
        startActivity(intent);
    }

    /**
     * 访问服务器回调
     */
    private TypeView typeView = new TypeView() {
        @Override
        public void onSuccess(TypeResultBean baseResultBean) {
            if (baseResultBean.getResult().getResponseObject() != null) {
                typeDao.deleteAll();
                cacheType(baseResultBean.getResult().getResponseObject());
                setViewPagersData(typeDao.loadAll());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("TAG", "OnError" + result);
        }
    };

}
