package com.tch.youmuwa.ui.activity.worker;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.CheckVersionResult;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.bean.result.SwitchRolesResult;
import com.tch.youmuwa.broadcastreceiver.JPushReceiver;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.service.LocationService;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.ui.fragment.HasReceivedOrdersFragment;
import com.tch.youmuwa.ui.fragment.MineFragment;
import com.tch.youmuwa.ui.fragment.SubscribeFragment;
import com.tch.youmuwa.ui.fragment.WorkerMineFragment;
import com.tch.youmuwa.ui.fragment.WorkerOrderFragment;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 工人主页
 */
public class WorkerMainActivity extends BaseActivtiy implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottomWorkerMenu)
    BottomNavigationBar bottomWorkerMenu;

    private ArrayList<Fragment> fragments;
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库
    private CheckVersionResult cvr;
    private Dialog dialog;
    private TextView tvVersionCancel, tvUpdateVersion;
    private RecyclerView rlVersion;
    private LinearLayoutManager layoutManager;
    private CommonAdapter commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);
        ButterKnife.bind(this);

        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        UserInfo user = userInfoDao.loadAll().get(0);
        JPushInterface.setAlias(WorkerMainActivity.this, user.getPhone(), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.e("TAG", "设置别名的结果为=" + i + ",arg1=" + s
                        + ",arg2=" + set);
            }
        });
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        bottomWorkerMenu.addItem(new BottomNavigationItem(R.mipmap.worker_subscribe_select, "我要接单").setInactiveIconResource(R.mipmap.worker_subscribe_not_select))
                .addItem(new BottomNavigationItem(R.mipmap.worker_order_select, "订单").setInactiveIconResource(R.mipmap.worker_order_not_select))
                .addItem(new BottomNavigationItem(R.mipmap.worker_mine_select, "我的").setInactiveIconResource(R.mipmap.worker_mine_not_select))
                .setFirstSelectedPosition(0)
                .setActiveColor(R.color.font_yellow)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .initialise();

        if (getIntent().getIntExtra("aid", 0) == 1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flWorkerFragmentShow, SubscribeFragment.newInstance("subscribe"))
                    .addToBackStack(null)
                    .commit();
            bottomWorkerMenu.selectTab(0);
        } else if (getIntent().getIntExtra("aid", 0) == 2) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flWorkerFragmentShow, WorkerMineFragment.newInstance(""))
                    .addToBackStack(null)
                    .commit();
            bottomWorkerMenu.selectTab(2);
        } else {
            setDefaultFragment();
            handler.sendEmptyMessage(0);
        }

        if (getIntent().getSerializableExtra("cvr") != null) {
            cvr = (CheckVersionResult) getIntent().getSerializableExtra("cvr");
            if (cvr.getCheckresult() == 1) {
                if (!SharedPrefsUtil.getValue(WorkerMainActivity.this, "cvr", "").equals(cvr.getRecent_version())) {
                    SharedPrefsUtil.putValue(WorkerMainActivity.this, "cvr", cvr.getRecent_version());
                    versionShow();
                }
            }
        }

        fragments = getFragments();
        bottomWorkerMenu.setTabSelectedListener(WorkerMainActivity.this);

        Intent intent = new Intent(WorkerMainActivity.this, MessageService.class);
        startService(intent);

    }

    /**
     * 显示版本
     */
    private void versionShow() {
        dialog = new Dialog(WorkerMainActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_check_version, null);
        tvVersionCancel = (TextView) layout.findViewById(R.id.tvVersionCancel);
        tvUpdateVersion = (TextView) layout.findViewById(R.id.tvUpdateVersion);
        rlVersion = (RecyclerView) layout.findViewById(R.id.rlVersion);
        initListData();
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(WorkerMainActivity.this) * 0.8);
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
        layoutManager = new LinearLayoutManager(WorkerMainActivity.this);
        rlVersion.setLayoutManager(layoutManager);
        commonAdapter = new CommonAdapter<String>(WorkerMainActivity.this, R.layout.dialog_version, cvr.getFeatures()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.tvVersionInfo, item);
            }
        };
        if (commonAdapter != null) {
            rlVersion.setAdapter(commonAdapter);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startLocationService();
        }
    };

    /**
     * 设置默认选中页
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.flWorkerFragmentShow, SubscribeFragment.newInstance("subscribe"));
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(SubscribeFragment.newInstance("subscribe"));
        fragments.add(WorkerOrderFragment.newInstance(""));
        fragments.add(WorkerMineFragment.newInstance(""));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.flWorkerFragmentShow, fragment);
                } else {
                    ft.add(R.id.flWorkerFragmentShow, fragment);
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
     * 开启定位数据
     */
    private void startLocationService() {
        Intent intent = new Intent(WorkerMainActivity.this, LocationService.class);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}
