package com.tch.kuwanx.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.adapter.TabAdapter;
import com.tch.kuwanx.bean.TabItem;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.message.KwMessage;
import com.tch.kuwanx.provider.KwProvider;
import com.tch.kuwanx.result.FriendsListResult;
import com.tch.kuwanx.result.NewFriendsResult;
import com.tch.kuwanx.result.RefreshTokenResult;
import com.tch.kuwanx.ui.home.HomeFragment;
import com.tch.kuwanx.ui.home.ReleaseFragment;
import com.tch.kuwanx.ui.message.MsgFragment;
import com.tch.kuwanx.ui.mine.MineFragment;
import com.tch.kuwanx.ui.release.ReleaseActivity;
import com.tch.kuwanx.ui.store.StoreFragment;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.CustomViewPager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 主页面
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.pager)
    CustomViewPager pager;
    @BindView(R.id.host)
    FragmentTabHost host;
    @BindView(R.id.viewHomeMenuDark)
    View viewHomeMenuDark;

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<TabItem> tabItems = new ArrayList<TabItem>();
    private int pageIndex, hostIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        initTabItem();
        initTabHost();
        initPager();
        bindTabAndPager();

        Logger.e("Token == " + DaoUtils.getUserToken(HomeActivity.this));
        connect(DaoUtils.getUserToken(HomeActivity.this));

        if (Build.VERSION.SDK_INT >= 23) {
            initPermission();
        }

        refreshTokenHttp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        newFriendsHttp();
    }

    /**
     * 加载权限
     */
    private void initPermission() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(
                        Permission.STORAGE
                        , Permission.CAMERA
                        , Permission.LOCATION
                )
                .callback(this)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(HomeActivity.this, rationale).show();
                    }
                })
                .start();
    }

    // 权限申请成功回调的方法，用注解即可，这里的100就是请求时的requestCode。
    @PermissionYes(100)
    private void getPermissionYes(List<String> grantedPermissions) {
        // TODO 申请权限成功。
        Logger.wtf("申请权限成功！");
    }

    @PermissionNo(100)
    private void getPermissionNo(List<String> deniedPermissions) {
        // TODO 申请权限失败。
        Toasty.warning(this, "申请权限失败！", Toast.LENGTH_SHORT, false).show();
    }

    private void initTabItem() {
        tabItems.add(new TabItem(
                this,
                R.drawable.selector_home_home,
                "首页",
                "home",
                HomeFragment.class
        ));
        tabItems.add(new TabItem(
                this,
                R.drawable.selector_home_store,
                "商城",
                "store",
                StoreFragment.class
        ));
        tabItems.add(new TabItem(
                this,
                "发布",
                "center",
                ReleaseFragment.class
        ));
        tabItems.add(new TabItem(
                this,
                R.drawable.selector_home_msg,
                "消息",
                "msg",
                MsgFragment.class
        ));
        tabItems.add(new TabItem(
                this,
                R.drawable.selector_home_mine,
                "我的",
                "mine",
                MineFragment.class
        ));
    }

    private void initTabHost() {
        host.setup(this, getSupportFragmentManager(), R.id.pager);
        host.getTabWidget().setDividerDrawable(null);
        for (TabItem ti : tabItems) {
            host.addTab(host.newTabSpec(ti.getTabSpec()).setIndicator(ti.getTabView()),
                    ti.getFragmentClass(), null);
        }
    }

    private void initPager() {
        fragments.add(HomeFragment.getInstance());
        fragments.add(StoreFragment.getInstance());
        fragments.add(MsgFragment.getInstance());
        fragments.add(MineFragment.getInstance());
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragments.size());
        host.setCurrentTab(hostIndex);
        pager.setCurrentItem(pageIndex, true);
    }

    private void bindTabAndPager() {
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int position = host.getCurrentTab();
                switch (position) {
                    case 0:
                        hostIndex = 0;
                        pageIndex = 0;
                        host.setCurrentTab(0);
                        pager.setCurrentItem(0, true);
                        break;
                    case 1:
                        hostIndex = 1;
                        pageIndex = 1;
                        host.setCurrentTab(1);
                        pager.setCurrentItem(1, true);
                        break;
                    case 2:
                        Intent intent = new Intent(HomeActivity.this, ReleaseActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        hostIndex = 3;
                        pageIndex = 2;
                        host.setCurrentTab(3);
                        pager.setCurrentItem(2, true);
                        break;
                    case 4:
                        hostIndex = 4;
                        pageIndex = 3;
                        host.setCurrentTab(4);
                        pager.setCurrentItem(3, true);
                        break;
                }
            }
        });
        //设置Viewpager不可滑动
        pager.setPagingEnabled(false);
    }

    /**
     * 修改底部未读消息数
     *
     * @param tabIndex
     * @param msgCount
     */
    public void updateMsgCount(int tabIndex, int msgCount) {
        tabItems.get(tabIndex).setNewMsgCount(msgCount);
    }

    public View getViewHomeMenuDark() {
        return viewHomeMenuDark;
    }

    /**
     * 连接服务器，在整个应用程序全局，只需要调用一次，需在@link init方法之后调用。
     * 如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Logger.wtf("融云：Token 错误");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Logger.wtf("融云：链接成功");
                RongIM.registerMessageType(KwMessage.class);
                RongIM.registerMessageTemplate(new KwProvider());
//                RongIM.getInstance().setMessageAttachedUserInfo(true);
                Logger.e("头像 == " + DaoUtils.getUser(HomeActivity.this).getHeadpic());
                if (DaoUtils.getUser(HomeActivity.this).getHeadpic() != null) {
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public UserInfo getUserInfo(String userId) {
                            return new UserInfo(
                                    DaoUtils.getUserId(HomeActivity.this),
                                    DaoUtils.getUser(HomeActivity.this).getNickname(),
                                    Uri.parse(DaoUtils.getUser(HomeActivity.this).getHeadpic())
                            );//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                        }
                    }, true);
//                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(
//                            DaoUtils.getUserId(HomeActivity.this),
//                            DaoUtils.getUser(HomeActivity.this).getNickname(),
//                            Uri.parse(DaoUtils.getUser(HomeActivity.this).getHeadpic())));
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(DaoUtils.getUserId(HomeActivity.this),
                            DaoUtils.getUser(HomeActivity.this).getNickname(),
                            Uri.parse(DaoUtils.getUser(HomeActivity.this).getHeadpic())));
                } else {
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public UserInfo getUserInfo(String userId) {
                            return new UserInfo(
                                    DaoUtils.getUserId(HomeActivity.this),
                                    DaoUtils.getUser(HomeActivity.this).getNickname(),
                                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                                            + getResources().getResourcePackageName(R.drawable.default_head) + "/"
                                            + getResources().getResourceTypeName(R.drawable.default_head) + "/"
                                            + getResources().getResourceEntryName(R.drawable.default_head)))
                                    ;//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                        }
                    }, true);
//                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(
//                            DaoUtils.getUserId(HomeActivity.this),
//                            DaoUtils.getUser(HomeActivity.this).getNickname(),
//                            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//                                    + getResources().getResourcePackageName(R.drawable.default_head) + "/"
//                                    + getResources().getResourceTypeName(R.drawable.default_head) + "/"
//                                    + getResources().getResourceEntryName(R.drawable.default_head))));
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(DaoUtils.getUserId(HomeActivity.this),
                            DaoUtils.getUser(HomeActivity.this).getNickname(),
                            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                                    + getResources().getResourcePackageName(R.drawable.default_head) + "/"
                                    + getResources().getResourceTypeName(R.drawable.default_head) + "/"
                                    + getResources().getResourceEntryName(R.drawable.default_head))));
                }

                getFriendsListHttp();
                initSetTotalNum();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.wtf("融云：链接失败");
            }
        });
    }

    /**
     * 获取好友列表
     */
    private void getFriendsListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(HomeActivity.this));
        String params = EncryptionUtil.getParameter(HomeActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/getFriendsList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getFriendsList")
                .cacheTime(2)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
//                        Toasty.warning(HomeActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                        Logger.e("获取好友信息");
                    }

                    @Override
                    public void onSuccess(String response) {
                        FriendsListResult friendsListResult =
                                (FriendsListResult) GsonUtil.json2Object(response, FriendsListResult.class);
                        if (friendsListResult != null
                                && friendsListResult.getRet().equals("1")) {
                            for (final FriendsListResult.ResultBean item : friendsListResult.getResult()) {
                                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                                    @Override
                                    public UserInfo getUserInfo(String userId) {
                                        if (userId.equals(item.getFriends_app_user_id())) {
                                            return new UserInfo(
                                                    item.getFriends_app_user_id(),
                                                    item.getNickname(),
                                                    Uri.parse(item.getHeadpic())
                                            );
                                        } else {
                                            return null;
                                        }
                                        //根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                                    }

                                }, true);
                            }
                        }
                    }
                });
    }

    /**
     * 设置未读数
     */
    private void initSetTotalNum() {
        Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP};
        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                int number = i + newFriendsNum;
                if (number >= 99) {
                    updateMsgCount(3, 99);
                } else {
                    updateMsgCount(3, i + newFriendsNum);
                }
            }
        }, conversationTypes);
    }

    private int newFriendsNum = 0;

    /**
     * 新朋友列表
     */
    private void newFriendsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(HomeActivity.this));
        String params = EncryptionUtil.getParameter(HomeActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/newFriends.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_newFriends____")
                .cacheTime(2)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
//                        Toasty.warning(getActivity(), "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                        Logger.e("获取新朋友列表数量失败！");
                    }

                    @Override
                    public void onSuccess(String response) {
                        NewFriendsResult newFriendsResult =
                                (NewFriendsResult) GsonUtil.json2Object(response, NewFriendsResult.class);
                        if (newFriendsResult != null
                                && newFriendsResult.getRet().equals("1")) {
                            if (newFriendsResult.getResult().size() > 0) {
                                List<NewFriendsResult.ResultBean> list = new ArrayList<>();
                                list.clear();
                                for (NewFriendsResult.ResultBean item : newFriendsResult.getResult()) {
                                    if (item.getApply_status().equals("0")) {
                                        list.add(item);
                                    }
                                }
                                if (list.size() > 0) {
                                    newFriendsNum = list.size();
                                } else {
                                    newFriendsNum = 0;
                                }
                            } else {
                                newFriendsNum = 0;
                            }
                        } else {
                            newFriendsNum = 0;
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 刷新Token
     */
    private void refreshTokenHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(HomeActivity.this));
        String params = EncryptionUtil.getParameter(HomeActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/refreshToken.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_refreshToken")
                .cacheTime(2)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("获取token失败");
                    }

                    @Override
                    public void onSuccess(String response) {
                        RefreshTokenResult refreshTokenResult =
                                (RefreshTokenResult) GsonUtil.json2Object(response, RefreshTokenResult.class);
                        if (refreshTokenResult != null
                                && refreshTokenResult.getRet().equals("1")) {
                            connect(refreshTokenResult.getResult().getYunToken());
                            DaoUtils.refreshUserToken(HomeActivity.this, refreshTokenResult.getResult().getYunToken());
                        }
                    }
                });
    }

}
