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
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.bean.result.SwitchRolesResult;
import com.tch.youmuwa.bean.result.UserInfoResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.service.LocationService;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.employer.MessageCenterActivity;
import com.tch.youmuwa.ui.activity.login.LoginActivity;
import com.tch.youmuwa.ui.activity.mine.MineDataActivity;
import com.tch.youmuwa.ui.activity.mine.PerfectDataActivity;
import com.tch.youmuwa.ui.activity.mine.SettingsActivity;
import com.tch.youmuwa.ui.activity.mine.TransactionDetailsActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerMainActivity;
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
 * 我的
 */

public class MineFragment extends ViewPagerFragment implements FragmentBackHandler, MessageInterface {

    @BindView(R.id.ivMineInfoPhoto)
    ImageView ivMineInfoPhoto;
    @BindView(R.id.tvMineInfoName)
    TextView tvMineInfoName;
    @BindView(R.id.tvMineInfoType)
    TextView tvMineInfoType;
    @BindView(R.id.tvMsgNum)
    TextView tvMsgNum;

    private Intent intent;
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;
    private PresenterImpl<Object> presenter;
    private UserInfoResult userInfoResult;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private List<MsgResult.MsgListBean> msgList;
    private Timer timer;
    private TimerTask task;
    private MsgResult msgR;
    private UserInfo ui;

    public static MineFragment newInstance(String param) {
        MineFragment fragment = new MineFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        msgList = new ArrayList<MsgResult.MsgListBean>();
//        getUserInfo();
        MessageService.setMi(this);
    }

    @Override
    public void getMsg(MsgResult msg) {
        if (msg != null) {
            msgR = msg;
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            initView();
        } else {
            if (presenter != null) {
                presenter.onStop();
                presenter = null;
            }
            if (handler != null) {
                handler = null;
            }
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
                Glide.with(getActivity())
                        .asBitmap()
                        .load(userInfoResult.getAvator())
                        .into(new BitmapImageViewTarget(ivMineInfoPhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                if (getContext() != null) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivMineInfoPhoto.setImageDrawable(circularBitmapDrawable);
                                } else {
                                    ivMineInfoPhoto.setImageResource(R.mipmap.photo);
                                }
                            }
                        });
                tvMineInfoName.setText(userInfoResult.getName().toString());
                if (userInfoResult.getType() == 1) {
                    tvMineInfoType.setText("雇主");
                } else {
                    tvMineInfoType.setText("工人");
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
        intent = new Intent(getContext(), MineDataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfoResult", userInfoResult);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 交易明细
     */
    @OnClick(R.id.rlMineTransactionDetails)
    public void mineTransactionDetails() {
        intent = new Intent(getContext(), TransactionDetailsActivity.class);
        startActivity(intent);
    }

    /**
     * 设置
     */
    @OnClick(R.id.rlSettings)
    public void settings() {
        intent = new Intent(getContext(), SettingsActivity.class);
        intent.putExtra("fragmentType", "employer");
        startActivity(intent);
    }

    /**
     * 消息
     */
    @OnClick({R.id.ivMsgIcon, R.id.tvMsgNum})
    public void msgShow() {
        intent = new Intent(getContext(), MessageCenterActivity.class);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btDropOut)
    public void dropOut() {
        logout();
    }

    /**
     * 切换角色
     */
    @OnClick(R.id.llSwitchRole)
    public void switchRole() {
        switchRoles(2);
    }

    /**
     * 切换角色
     */
    private void switchRoles(int index) {
        mSVProgressHUD.showWithStatus("切换中...");
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.switchroles(index);
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
                SwitchRolesResult switchRolesResult = (SwitchRolesResult) GsonUtil.parseJson(baseBean.getData(), SwitchRolesResult.class);
                if (switchRolesResult.getResult() == 0) {
                    intent = new Intent(getContext(), PerfectDataActivity.class);
                    startActivity(intent);
                } else {
                    SharedPrefsUtil.putValue(getContext(), "isEmployer", false);
                    intent = new Intent(getContext(), WorkerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    int num = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                    for (int i = 0; i < num; i++) {
                        getActivity().getSupportFragmentManager().getBackStackEntryAt(i);
                    }
                    startActivity(intent);
                    getActivity().finish();
                }
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
            Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {

                Intent serviceIntent = new Intent(getContext(), MessageService.class);
                getActivity().stopService(serviceIntent);

                Intent mapIntent = new Intent(getContext(), LocationService.class);
                getActivity().stopService(mapIntent);

                intent = new Intent(getContext(), LoginActivity.class);
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
            Log.e("Error", "logoutView==" + result);
        }
    };

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
                    if (msgR != null) {
                        msgList = new ArrayList<MsgResult.MsgListBean>();
                        for (MsgResult.MsgListBean mb : msgR.getMsg_list()) {
                            if (mb.getIs_read() == 0) {
                                msgList.add(mb);
                            }
                        }
                        if (msgList.size() <= 0) {
                            tvMsgNum.setVisibility(View.GONE);
                        } else {
                            tvMsgNum.setVisibility(View.VISIBLE);
                            tvMsgNum.setText(String.valueOf(msgList.size()));
                            if (getActivity() != null) {
                                ShortcutBadger.applyCount(getActivity(), msgList.size());
                            }
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
        getUserInfo();
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
                    tvMsgNum.setVisibility(View.GONE);
                } else {
                    msgList = new ArrayList<MsgResult.MsgListBean>();
                    for (MsgResult.MsgListBean mb : msg.getMsg_list()) {
                        if (mb.getIs_read() == 0) {
                            msgList.add(mb);
                        }
                    }
                    if (msgList.size() <= 0) {
                        tvMsgNum.setVisibility(View.GONE);
                    } else {
                        tvMsgNum.setVisibility(View.VISIBLE);
                        tvMsgNum.setText(String.valueOf(msgList.size()));
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
