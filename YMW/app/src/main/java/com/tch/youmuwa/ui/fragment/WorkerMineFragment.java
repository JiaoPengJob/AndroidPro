package com.tch.youmuwa.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.bean.result.UserInfoResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.service.LocationService;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.ui.activity.employer.MessageCenterActivity;
import com.tch.youmuwa.ui.activity.login.LoginActivity;
import com.tch.youmuwa.ui.activity.mine.SettingsActivity;
import com.tch.youmuwa.ui.activity.worker.WalletActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerMineDataActivity;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 工人/我的
 */

public class WorkerMineFragment extends ViewPagerFragment implements FragmentBackHandler, MessageInterface {

    @BindView(R.id.ivWorkerMinePhoto)
    ImageView ivWorkerMinePhoto;
    @BindView(R.id.tvWorkerMineName)
    TextView tvWorkerMineName;
    @BindView(R.id.tvWorkerMineType)
    TextView tvWorkerMineType;
    @BindView(R.id.tvMsgSizes)
    TextView tvMsgSizes;

    private Intent intent;
    private PresenterImpl<Object> presenter;
    private UserInfoResult userInfoResult;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private List<MsgResult.MsgListBean> msgList;
    private Timer timer;
    private TimerTask task;
    private MsgResult msgResult;

    public static WorkerMineFragment newInstance(String param) {
        WorkerMineFragment fragment = new WorkerMineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("size", param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            initView();
        } else {
            msgList = null;
            userInfoResult = null;
            if (presenter != null) {
                presenter.onStop();
                presenter = null;
            }
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        MessageService.setMi(this);
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        msgList = new ArrayList<MsgResult.MsgListBean>();
        getUserInfo();
    }

    @Override
    public void getMsg(MsgResult msgResult) {
        if (msgResult != null) {
            this.msgResult = msgResult;
            handler.sendEmptyMessage(0);
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getuserinfo();
        presenter.attachView(UserInfoView);
    }

    private ClientBaseView<Object> UserInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                userInfoResult = (UserInfoResult) GsonUtil.parseJson(baseBean.getData(), UserInfoResult.class);
                Glide.with(getContext())
                        .asBitmap()
                        .load(userInfoResult.getAvator())
                        .into(new BitmapImageViewTarget(ivWorkerMinePhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                if (getContext() != null) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivWorkerMinePhoto.setImageDrawable(circularBitmapDrawable);
                                } else {
                                    ivWorkerMinePhoto.setImageResource(R.mipmap.photo);
                                }
                            }
                        });
                tvWorkerMineName.setText(userInfoResult.getName().toString());
                if (userInfoResult.getType() == 1) {
                    tvWorkerMineType.setText("雇主");
                } else {
                    tvWorkerMineType.setText("工人");
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "UserInfoView==" + result);
        }
    };

    /**
     * 个人资料
     */
    @OnClick(R.id.rlMineData)
    public void mineData() {
        intent = new Intent(getContext(), WorkerMineDataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfoResult", userInfoResult);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 钱包
     */
    @OnClick(R.id.rlWallet)
    public void mineWallet() {
        intent = new Intent(getContext(), WalletActivity.class);
        startActivity(intent);
    }

    /**
     * 设置
     */
    @OnClick(R.id.rlSettings)
    public void settings() {
        intent = new Intent(getContext(), SettingsActivity.class);
        intent.putExtra("fragmentType", "worker");
        startActivity(intent);
    }

    /**
     * 消息
     */
    @OnClick({R.id.ivMsgIcon, R.id.tvMsgSizes})
    public void msgShow() {
        intent = new Intent(getContext(), MessageCenterActivity.class);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btDropOut)
    public void dropOut() {
        intent = new Intent(getContext(), LocationService.class);
        getContext().stopService(intent);
        logout();
    }

    /**
     * 退出登录
     */
    private void logout() {
        mSVProgressHUD.showWithStatus("退出登录中...");
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.logout();
        presenter.attachView(logoutView);
    }

    private ClientBaseView<Object> logoutView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() == 1) {
                Intent serviceIntent = new Intent(getContext(), MessageService.class);
                getActivity().stopService(serviceIntent);
                Intent mapIntent = new Intent(getContext(), LocationService.class);
                getActivity().stopService(mapIntent);
                intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "logoutView==" + result);
        }
    };

    /**
     * 切换角色
     */
    @OnClick(R.id.llSwitchRole)
    public void switchRole() {
        intent = new Intent(getContext(), LocationService.class);
        getContext().stopService(intent);
        switchRoles();
    }

    /**
     * 切换角色
     */
    private void switchRoles() {
        mSVProgressHUD.showWithStatus("切换角色中...");
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.switchroles(1);
        presenter.attachView(SwitchRolesView);
    }

    private ClientBaseView<Object> SwitchRolesView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                stopLocationService();
                SharedPrefsUtil.putValue(getContext(), "isEmployer", true);
                intent = new Intent(getContext(), EmployerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                int num = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < num; i++) {
                    getActivity().getSupportFragmentManager().getBackStackEntryAt(i);
                }
                startActivity(intent);
                getActivity().finish();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "SwitchRolesView==" + result);
        }
    };

    /**
     * 关闭定位数据
     */
    private void stopLocationService() {
        Intent intent = new Intent(getContext(), LocationService.class);
        getContext().startService(intent);
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.rlClearCache)
    public void clearCache() {
        try {
            Log.e("TAG", "缓存大小：" + CacheDataManager.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CacheDataManager.clearAllCache(getContext());
        mSVProgressHUD.showWithStatus("清除缓存中...");
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(task, 3000);
    }

    @Override
    public boolean onBackPressed() {
        if (mSVProgressHUD.isShowing()) {
            mSVProgressHUD.dismiss();
            if (presenter != null) {
                presenter.onStop();
            }
            return true;
        } else {
            return false;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msgResult != null) {
                        msgList = new ArrayList<MsgResult.MsgListBean>();
                        for (MsgResult.MsgListBean mb : msgResult.getMsg_list()) {
                            if (mb.getIs_read() == 0) {
                                msgList.add(mb);
                            }
                        }
                        if (msgList.size() <= 0) {
                            tvMsgSizes.setVisibility(View.GONE);
                        } else {
                            tvMsgSizes.setVisibility(View.VISIBLE);
                            tvMsgSizes.setText(String.valueOf(msgList.size()));
                        }
                    }
                    break;
                case 1:
                    if (mSVProgressHUD.isShowing()) {
                        mSVProgressHUD.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getMsgs();
    }

    /**
     * 获取消息列表
     */
    private void getMsgs() {
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getmessagelist();
        presenter.attachView(msgView);
    }

    private ClientBaseView<Object> msgView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                MsgResult msg = (MsgResult) GsonUtil.parseJson(baseBean.getData(), MsgResult.class);
                if (msg.getMsg_list().size() <= 0) {
                    tvMsgSizes.setVisibility(View.GONE);
                } else {
                    msgList = new ArrayList<MsgResult.MsgListBean>();
                    for (MsgResult.MsgListBean mb : msg.getMsg_list()) {
                        if (mb.getIs_read() == 0) {
                            msgList.add(mb);
                        }
                    }
                    if (msgList.size() <= 0) {
                        tvMsgSizes.setVisibility(View.GONE);
                    } else {
                        tvMsgSizes.setVisibility(View.VISIBLE);
                        tvMsgSizes.setText(String.valueOf(msgList.size()));
                        ShortcutBadger.applyCount(getContext(), msgList.size());
                    }
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "msgView--" + result);
        }
    };
}
