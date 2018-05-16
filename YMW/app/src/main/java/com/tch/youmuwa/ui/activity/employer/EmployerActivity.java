package com.tch.youmuwa.ui.activity.employer;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.result.CheckVersionResult;
import com.tch.youmuwa.bean.result.RequiresListResult;
import com.tch.youmuwa.broadcastreceiver.JPushReceiver;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.service.LocationService;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.fragment.DemandFragment;
import com.tch.youmuwa.ui.fragment.MapFragment;
import com.tch.youmuwa.ui.fragment.MineFragment;
import com.tch.youmuwa.ui.fragment.OrderFragment;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 雇主主页
 */
public class EmployerActivity extends BaseActivtiy implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottomEmployerMenu)
    BottomNavigationBar bottomEmployerMenu;

    private ArrayList<Fragment> fragments;
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库
    private CheckVersionResult cvr;
    private Dialog dialog;
    private TextView tvVersionCancel, tvUpdateVersion;
    private RecyclerView rlVersion;
    private LinearLayoutManager layoutManager;
    private CommonAdapter commonAdapter;
    private UserInfo user;
    private Handler handler = new Handler();
    private Intent intent;
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        ButterKnife.bind(this);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        if (userInfoDao.loadAll().size() > 0) {
            user = userInfoDao.loadAll().get(0);
        }
        JPushInterface.setAlias(EmployerActivity.this, user.getPhone(), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.e("TAG", "设置别名的结果为=" + i + ",arg1=" + s
                        + ",arg2=" + set);
            }
        });

        Log.e("TAG","进入EmployerActivity--onCreate");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG","进入EmployerActivity--onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Fragment f : fragments) {
            getSupportFragmentManager().beginTransaction().remove(f);
        }
        fragments.clear();
    }

    /**
     * 初始化
     */
    private void initView() {

        bottomEmployerMenu.addItem(new BottomNavigationItem(R.mipmap.employer_home_select, "首页").setInactiveIconResource(R.mipmap.employer_home_not_select))
                .addItem(new BottomNavigationItem(R.mipmap.employer_needs_select, "需求").setInactiveIconResource(R.mipmap.employer_needs_not_select))
                .addItem(new BottomNavigationItem(R.mipmap.employer_order_select, "订单").setInactiveIconResource(R.mipmap.employer_order_not_select))
                .addItem(new BottomNavigationItem(R.mipmap.employer_mine_select, "我的").setInactiveIconResource(R.mipmap.employer_mine_not_select))
                .setFirstSelectedPosition(0)
                .setActiveColor(R.color.green_31d09a)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .initialise();
        setDefaultFragment();
        fragments = getFragments();

        if (getIntent().getIntExtra("aid", 0) == 1) {
            ft
                    .replace(R.id.flFragmentShow, fragments.get(2))
                    .addToBackStack(null)
                    .commit();
            bottomEmployerMenu.selectTab(2);
        } else if (getIntent().getIntExtra("aid", 0) == 2) {
            ft
                    .replace(R.id.flFragmentShow, fragments.get(1))
                    .addToBackStack(null)
                    .commit();
            bottomEmployerMenu.selectTab(1);
        } else if (getIntent().getIntExtra("aid", 0) == 3) {
            ft
                    .replace(R.id.flFragmentShow, fragments.get(3))
                    .addToBackStack(null)
                    .commit();
            bottomEmployerMenu.selectTab(3);
        } else {
            stopLocationService();
        }

        if (getIntent().getSerializableExtra("cvr") != null) {
            cvr = (CheckVersionResult) getIntent().getSerializableExtra("cvr");
            if (cvr.getCheckresult() == 1) {
                if (!SharedPrefsUtil.getValue(EmployerActivity.this, "cvr", "").equals(cvr.getRecent_version())) {
                    SharedPrefsUtil.putValue(EmployerActivity.this, "cvr", cvr.getRecent_version());
                    versionShow();
                }
            }
        }

        bottomEmployerMenu.setTabSelectedListener(EmployerActivity.this);

        Intent intent = new Intent(EmployerActivity.this, MessageService.class);
        startService(intent);
    }

    /**
     * 显示版本
     */
    private void versionShow() {
        dialog = new Dialog(EmployerActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_check_version, null);
        tvVersionCancel = (TextView) layout.findViewById(R.id.tvVersionCancel);
        tvUpdateVersion = (TextView) layout.findViewById(R.id.tvUpdateVersion);
        rlVersion = (RecyclerView) layout.findViewById(R.id.rlVersion);
        initListData();
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(EmployerActivity.this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvVersionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //确认
        tvUpdateVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(cvr.getDownload_url());
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }

    /**
     * 加载列表数据
     */
    private void initListData() {
        layoutManager = new LinearLayoutManager(EmployerActivity.this);
        rlVersion.setLayoutManager(layoutManager);
        commonAdapter = new CommonAdapter<String>(EmployerActivity.this, R.layout.dialog_version, cvr.getFeatures()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.tvVersionInfo, item);
            }
        };
        if (commonAdapter != null) {
            rlVersion.setAdapter(commonAdapter);
        }
    }

    /**
     * 设置默认选中页
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.flFragmentShow, MapFragment.newInstance(""));
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(MapFragment.newInstance(""));
        fragments.add(DemandFragment.newInstance(""));
        fragments.add(OrderFragment.newInstance(""));
        fragments.add(MineFragment.newInstance(""));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    if (position == 0) {
                        if (fragment != null) {
                            if (fragment.isVisible()) {
                                ft.hide(fragment);
                            } else {
                                ft.show(fragment);
                            }
                        }
                    } else {
                        ft.replace(R.id.flFragmentShow, fragment);
                    }
                } else {
                    if (position == 0) {
                        if (fragment != null) {
                            Log.e("TAG","000000000000000");
                            if (!fragment.isVisible()) {
                                Log.e("TAG","111111111111111");
                                ft.show(fragments.get(0));
                            }
                            intent = new Intent(EmployerActivity.this, MessageService.class);
                            startService(intent);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stopService(intent);
                                }
                            }, 10000);
                        } else {
                            Log.e("TAG","2222222222222222");
                            ft.add(R.id.flFragmentShow, fragment);
                        }
                    } else {
                        ft.add(R.id.flFragmentShow, fragment);
                    }
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 关闭定位数据
     */
    private void stopLocationService() {
        Intent intent = new Intent(EmployerActivity.this, LocationService.class);
        stopService(intent);
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}
