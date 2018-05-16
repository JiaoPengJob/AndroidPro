package com.tch.zx.activity.line.online;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.TabBar;
import com.tch.zx.dao.green.TabBarDao;
import com.tch.zx.fragment.line.HotOnlinePlayerRemdFragment;
import com.tch.zx.http.bean.result.TypeResultBean;
import com.tch.zx.http.presenter.TypePresenter;
import com.tch.zx.http.view.TypeView;
import com.tch.zx.util.HelperUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 热门直播主页面
 */
public class OnLinePlayerMainActivity extends BaseActivity {

    /**
     * tab标签栏
     */
    @BindView(R.id.stl_general)
    SmartTabLayout stl_general;
    /**
     * 标签栏viewpager
     */
    @BindView(R.id.vp_general)
    ViewPager vp_general;
    /**
     * 头标题info
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    private TypePresenter typePresenter;
    private List<TypeResultBean.ResponseObject> typeList = new ArrayList<TypeResultBean.ResponseObject>();
    private DaoSession daoSession;
    private TabBarDao typeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_on_line_player_main);
        //集成使用Butterknife
        ButterKnife.bind(this);
        initView();

    }

    /**
     * 初始化View
     */
    private void initView() {
        tv_title_top_all.setText("热门直播");
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        typeDao = daoSession.getTabBarDao();
        initTabType();
    }

    /**
     * 加载tabView信息
     */
    private void setViewPagersData(List<TabBar> list) {
        //向选项卡布局中添加子布局Fragment
        FragmentPagerItems items = new FragmentPagerItems(this);
        items.add(FragmentPagerItem.of("推荐", HotOnlinePlayerRemdFragment.class, new Bundler().putString("typeId", "0").get()));
        for (TabBar type : list) {
            items.add(FragmentPagerItem.of(type.getTypeStr(), HotOnlinePlayerRemdFragment.class, new Bundler().putString("typeId", String.valueOf(type.getTypeId())).get()));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), items);
        vp_general.setAdapter(adapter);
        stl_general.setViewPager(vp_general);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBackOnClick() {
        OnLinePlayerMainActivity.this.finish();
    }

    /**
     * 获取tab标签
     */
    private void initTabType() {
        if (typeDao.loadAll() != null && typeDao.loadAll().size() > 0) {
            setViewPagersData(typeDao.loadAll());
        } else {
            typePresenter = new TypePresenter(OnLinePlayerMainActivity.this);
            typePresenter.onCreate();
            typePresenter.attachView(typeView);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("param", "0");
            String data = HelperUtil.getParameter(map);
            typePresenter.type(data);
        }

    }

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
            Log.e("Error", "TypeView" + result);
        }
    };

    /**
     * 缓存首页标签
     */
    private void cacheType(List<TypeResultBean.ResponseObject> list) {
        for (TypeResultBean.ResponseObject ro : list) {
            typeDao.insert(new TabBar(ro.getTypeId(), ro.getTypeName()));
        }
    }

}
