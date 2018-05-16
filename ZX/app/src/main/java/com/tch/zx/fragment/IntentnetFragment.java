package com.tch.zx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.line.TopListActivity;
import com.tch.zx.activity.line.column.ColumnActivity;
import com.tch.zx.activity.line.greatclass.GreatClassItemPlayerActivity;
import com.tch.zx.activity.line.greatclass.GreatClassMainActivity;
import com.tch.zx.activity.line.greatclass.GreatClassPlayerActivity;
import com.tch.zx.activity.line.online.OnLinePlayerItemMainActivity;
import com.tch.zx.activity.line.online.OnLinePlayerMainActivity;
import com.tch.zx.adapter.ColumnSubsciptionAdapter;
import com.tch.zx.adapter.FineLittleClassAdapter;
import com.tch.zx.adapter.RvHotOnlinePlayerAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.LiveBean;
import com.tch.zx.dao.green.LiveBeanDao;
import com.tch.zx.dao.green.SmallBean;
import com.tch.zx.dao.green.SmallBeanDao;
import com.tch.zx.dao.green.SpecialBean;
import com.tch.zx.dao.green.SpecialBeanDao;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.presenter.HomePresenter;
import com.tch.zx.http.view.HomeView;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.GoTopScrollView;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 互联网
 */

public class IntentnetFragment extends Fragment {

    /**
     * 可置顶的滑动布局
     */
    @BindView(R.id.goto_top_view)
    GoTopScrollView goto_top_view;
    /**
     * 置顶图片
     */
    @BindView(R.id.iv_return_top)
    ImageView iv_return_top;
    /**
     * 热门直播的列表
     */
    @BindView(R.id.rv_hot_online_player)
    RecyclerView rv_hot_online_player;
    /**
     * 精品小课的列表
     */
    @BindView(R.id.rv_fine_little_class)
    RecyclerView rv_fine_little_class;
    /**
     * 榜单的列表
     */
    @BindView(R.id.rv_column_subscription)
    RecyclerView rv_column_subscription;
    /**
     * 精品小课展示列表适配器
     */
    private FineLittleClassAdapter fineLittleClassAdapter;
    /**
     * 热门直播展示列表适配器
     */
    private RvHotOnlinePlayerAdapter rvHotOnlinePlayerAdapter;
    /**
     * 专栏订阅展示列表适配器
     */
    private ColumnSubsciptionAdapter columnSubsciptionAdapter;

    /**
     * Fragment父布局
     */
    private View viewRoot;
    /**
     * 跳转
     */
    private Intent intent;
    /**
     * 接口
     */
    private HomePresenter homePresenter;

    private DaoSession daoSession;
    private LiveBeanDao liveBeanDao;
    private SpecialBeanDao specialBeanDao;
    private SmallBeanDao smallBeanDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_intentnet, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);

        initView();

        return viewRoot;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
        liveBeanDao = daoSession.getLiveBeanDao();
        smallBeanDao = daoSession.getSmallBeanDao();
        specialBeanDao = daoSession.getSpecialBeanDao();
        //设置置顶图片
        goto_top_view.setImgeViewOnClickGoToFirst(iv_return_top);
        //设置高度
        goto_top_view.setScreenHeight(10);

        if (liveBeanDao.loadAll().size() > 0 && smallBeanDao.loadAll().size() > 0 && specialBeanDao.loadAll().size() > 0) {
            setRecyclerViewData(liveBeanDao.loadAll());
            setFineClassData(smallBeanDao.loadAll());

        } else {
            initHome();
        }
    }

    /**
     * 加载热门直播列表数据
     */
    private void setRecyclerViewData(final List<LiveBean> list) {
        rvHotOnlinePlayerAdapter = new RvHotOnlinePlayerAdapter(getContext(), list);
        rv_hot_online_player.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_hot_online_player.addItemDecoration(new RecyclerViewDecoration(getContext(), "#949494", 1, true));
        rv_hot_online_player.setAdapter(rvHotOnlinePlayerAdapter);
        rv_hot_online_player.setNestedScrollingEnabled(false);
        rvHotOnlinePlayerAdapter.setOnItemClickListener(new RvHotOnlinePlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(getContext(), OnLinePlayerItemMainActivity.class);
                intent.putExtra("appUserId", list.get(position).getAppUserId());
                intent.putExtra("liveId", list.get(position).getLiveId());
                startActivity(intent);
            }
        });
    }

    /**
     * 加载精品小课列表数据
     */
    private void setFineClassData(final List<SmallBean> list) {
        fineLittleClassAdapter = new FineLittleClassAdapter(getContext(), list, 0);
        rv_fine_little_class.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_fine_little_class.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 10, false));
        rv_fine_little_class.setAdapter(fineLittleClassAdapter);
        rv_fine_little_class.setNestedScrollingEnabled(false);
        fineLittleClassAdapter.setOnItemClickListener(new FineLittleClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(getContext(), GreatClassPlayerActivity.class);
                intent.putExtra("appUserId", list.get(position).getAppUserId());
                intent.putExtra("smallClassId", String.valueOf(list.get(position).getSmallClassId()));
                intent.putExtra("intentType", "class");
                intent.putExtra("videoPath", list.get(position).getVideoPath());
                intent.putExtra("viewNum", list.get(position).getViewNum());
                intent.putExtra("viewId", list.get(position).getVideoId());
                startActivity(intent);
            }
        });
    }

    /**
     * 加载专栏订阅列表数据
     */
    private void setColumnSubspData(List<SpecialBean> list) {
        columnSubsciptionAdapter = new ColumnSubsciptionAdapter(getContext(), list);
        rv_column_subscription.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_column_subscription.addItemDecoration(new RecyclerViewDecoration(getContext(), "#949494", 1, true));
        rv_column_subscription.setAdapter(columnSubsciptionAdapter);
        rv_column_subscription.setNestedScrollingEnabled(false);
    }

    /**
     * 直播跳转事件
     */
    @OnClick(R.id.ll_online_player_look_all)
    public void netOnLineOnClick() {
        intent = new Intent(getContext(), OnLinePlayerMainActivity.class);
        startActivity(intent);
    }

    /**
     * 小课跳转事件
     */
    @OnClick(R.id.ll_class_look_all)
    public void netClassOnClick() {
        intent = new Intent(getContext(), GreatClassMainActivity.class);
        startActivity(intent);
    }

    /**
     * 专栏跳转事件
     */
    @OnClick(R.id.ll_column_look_all)
    public void netColumnOnClick() {
        intent = new Intent(getContext(), ColumnActivity.class);
        startActivity(intent);
    }

    /**
     * 榜单跳转事件
     */
    @OnClick(R.id.ll_hot_list_top_look_all)
    public void netTopListOnClick() {
        intent = new Intent(getContext(), TopListActivity.class);
        startActivity(intent);
    }

    /**
     * 获取服务器数据
     */
    private void initHome() {
        homePresenter = new HomePresenter(getContext());
        homePresenter.onCreate();
        homePresenter.attachView(homeView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", "1");

        String data = HelperUtil.getParameter(map);
        homePresenter.home(data);
    }

    private HomeView homeView = new HomeView() {
        @Override
        public void onSuccess(HomeResultBean baseResultBean) {
            if (baseResultBean.getResult() != null) {
                if (baseResultBean.getResult().getLive() != null && baseResultBean.getResult().getLive().size() != 0) {
//                    liveBeanDao.deleteAll();
                    List<LiveBean> liveList = new ArrayList<LiveBean>();
                    for (HomeResultBean.Live live : baseResultBean.getResult().getLive()) {
                        liveList.add(new LiveBean(
                                live.getAppUserId(),
                                live.getAppUserName(),
                                live.getLiveId(),
                                live.getLiveMoney(),
                                live.getLiveName(),
                                live.getLivePepoleNum(),
                                live.getLivePicMax(),
                                live.getLivePicMin(),
                                live.getLiveStatus(),
                                live.getLiveTime(),
                                live.getLiveVideo(),
                                live.getLiveViewNum(),
                                live.getPosition(),
                                live.getUserPic()
                        ));
                    }
                    setRecyclerViewData(liveList);
                }
                if (baseResultBean.getResult().getSmall() != null && baseResultBean.getResult().getSmall().size() != 0) {
//                    smallBeanDao.deleteAll();
                    List<SmallBean> classList = new ArrayList<SmallBean>();
                    for (HomeResultBean.Small small : baseResultBean.getResult().getSmall()) {
                        classList.add(new SmallBean(
                                small.getAppUserId(),
                                small.getAppUserName(),
                                small.getAppUserPic(),
                                small.getAudioPath(),
                                small.getModuleClassName(),
                                small.getPosition(),
                                small.getSignUpNum(),
                                small.getSmallClassId(),
                                small.getSmallClassModuleId(),
                                small.getSmallClassName(),
                                small.getSmallClassPicMax(),
                                small.getSmallClassPicMin(),
                                small.getTypeId(),
                                small.getTypeName(),
                                small.getVideoMoney(),
                                small.getVideoName(),
                                small.getVideoPath(),
                                small.getViewNum(),
                                small.getVideoId()
                        ));
                    }
                    setFineClassData(classList);
                }
                if (baseResultBean.getResult().getSpecial() != null && baseResultBean.getResult().getSpecial().size() != 0) {
//                    specialBeanDao.deleteAll();
                    List<SpecialBean> specialList = new ArrayList<SpecialBean>();
                    for (HomeResultBean.Special special : baseResultBean.getResult().getSpecial()) {
                        specialList.add(new SpecialBean(
                                special.getAppUserId(),
                                special.getAppUserName(),
                                special.getAppUserPic(),
                                special.getPosition(),
                                special.getSpecialColumnByName(),
                                special.getSpecialColumnClassCreateDate(),
                                special.getSpecialColumnClassId(),
                                special.getSpecialColumnId(),
                                special.getSpecialColumnName(),
                                special.getSpecialColumnPicMax(),
                                special.getSpecialColumnPicMin(),
                                special.getSpecialColumnPrice(),
                                special.getSubscriptionNumber()
                        ));
                    }
                    setColumnSubspData(specialList);
                }
            }
        }

        @Override
        public void onError(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }
    };

}
