package com.tch.youmuwa.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tch.youmuwa.dao.DaoMaster;
import com.tch.youmuwa.dao.DaoSession;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.greendao.database.Database;

import cn.jpush.android.api.JPushInterface;

/**
 * 应用全局对象
 */

public class MyApplication extends Application {

    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        //分包
        MultiDex.install(this);
        //百度地图
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要在setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //加载数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "ymw-db-encrypted" : "ymw-db");
//        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        //友盟
        //初始化sdk
        UMShareAPI.get(this);
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = false;

//        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

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
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    //各个平台的配置
    {
        //微信
        PlatformConfig.setWeixin("wxef807a23ff724c9d", "d045bbd97abedfe9b21a2af45b4b5c12");
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("2660554133", "e67f07ca1de48dc53962733f54d65a77", "http://sns.whalecloud.com/sina2/callback");
        //QQ
        PlatformConfig.setQQZone("101419394", "43e1aa6d49c6b70c3b10a8bd0ea9b7c2");
    }
}
