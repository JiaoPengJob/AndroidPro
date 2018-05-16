package com.tch.zx.application;

import android.content.Context;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tch.zx.BuildConfig;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoMaster;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.http.bean.result.UserInfoResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.listener.MyConnectionStatusListener;
import com.tch.zx.listener.MyReceiveMessageListener;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.greendao.database.Database;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Application应用文件
 */

public class MyApplication extends MultiDexApplication implements RongIM.UserInfoProvider {

    //数据库session类
    private DaoSession daoSession;

    private BasePresenter<Object> presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        RxRetrofitApp.init(this);
        //链接或创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, false ? "zhixian_cache" : "zhixian_cache_db");
        Database db = false ? helper.getEncryptedWritableDb("super_secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要在setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        //融云
        RongIM.init(this);
        RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
        RongIM.setConnectionStatusListener(new MyConnectionStatusListener());

        /**
         * 极光推送
         */
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        RxRetrofitApp.init(this, BuildConfig.DEBUG);

        //七牛云配置
        StreamingEnv.init(getApplicationContext());

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        mWxApi = WXAPIFactory.createWXAPI(this, wxAppId, false);
        // 将该app注册到微信
        mWxApi.registerApp(wxAppId);
    }

    public static IWXAPI mWxApi;
    private String wxAppId = "wx1f503768b143634c";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获取数据库session
     *
     * @return
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public UserInfo getUserInfo(String s) {
        return getUserInfoByAppUserId(s);
    }

    private UserInfo getUserInfoByAppUserId(String id) {
        presenter = new BasePresenter<Object>(this);
        presenter.onCreate();
        presenter.attachView(new BaseView<Object>() {
            @Override
            public void onSuccess(BaseResultBean<Object> baseResultBean) {
                if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                    UserInfoResult userInfoResult = (UserInfoResult) GsonUtil.parseJson(baseResultBean.getResult(), UserInfoResult.class);
                    userInfo = new UserInfo(
                            userInfoResult.getResponseObject().getApp_user_id(),
                            userInfoResult.getResponseObject().getName(),
                            Uri.parse(userInfoResult.getResponseObject().getUser_picture())
                    );
                }
            }

            @Override
            public void onError(String result) {
                Log.e("ZX", "getUserInfoByAppUserIdView接口错误" + result);
            }
        });

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", id);

        String data = HelperUtil.getParameter(map);
        presenter.getUserInfoByAppUserId(data);
        return userInfo;
    }

    private UserInfo userInfo;
}
