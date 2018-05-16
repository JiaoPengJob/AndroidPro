package com.tch.kuwanx.application;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tch.kuwanx.R;
import com.tch.kuwanx.dao.DaoMaster;
import com.tch.kuwanx.dao.DaoSession;
import com.tch.kuwanx.listener.MyReceiveMessageListener;
import com.tch.kuwanx.utils.GlideAlbumLoader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.cookie.CookieManger;
import com.zhouyou.http.interceptor.NoCacheInterceptor;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

import org.greenrobot.greendao.database.Database;

import java.util.Locale;

import io.rong.imkit.RongIM;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by peng on 2017/10/23.
 */

public class BaseApplication extends Application {

    private DaoSession daoSession;

    private String baseUri = "http://192.168.1.111:8090/";
//    private String baseUri = "http://47.93.191.99:9004/";

    @Override
    public void onCreate() {
        super.onCreate();

        //设置Log输出日志
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) 是否显示线程信息. Default true
                .methodCount(0)         // (Optional) 要显示多少种方法行. Default 2
                .methodOffset(3)        // (Optional) 跳过堆栈跟踪中的一些方法调用. Default 5
                .tag("KuWanX:")   // (Optional) 每个日志的自定义标签. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        //设置布局中字体显示
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PingFang-SC-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //友盟
        UMShareAPI.get(this);
        Config.DEBUG = true;

        //拍照及相册
        Album.initialize(
                AlbumConfig.newBuilder(this)
                        .setAlbumLoader(new GlideAlbumLoader())
                        .setLocale(Locale.getDefault())
                        .build()
        );

        //融云
        RongIM.init(this);
        RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());

        //加载数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "kuwanx_db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        initEasyHttp();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("101458097", "9db6c0cfe5f86e0153145cf39d83c63a");
    }

    /**
     * 初始化Http链接
     */
    private void initEasyHttp() {
        EasyHttp.init(this);//默认初始化,必须调用

        //全局设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.put("User-Agent", "");
        //全局设置请求参数
        HttpParams params = new HttpParams();
        params.put("appId", "");

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        EasyHttp.getInstance()
                //可以全局统一设置全局URL
                //设置全局URL  url只能是域名 或者域名+端口号
                .setBaseUrl(baseUri)
                // 打开该调试开关并设置TAG,不需要就不要加入该行
                // 最后的true表示是否打印内部异常，一般打开方便调试错误
                .debug("EasyHttp", true)
                //如果使用默认的60秒,以下三行也不需要设置
                .setReadTimeOut(30 * 1000)
                .setWriteTimeOut(30 * 100)
                .setConnectTimeout(30 * 100)

                //可以全局统一设置超时重连次数,默认为3次,那么最差的情况会请求4次(一次原始请求,三次重连请求),
                //不需要可以设置为0
                .setRetryCount(3)//网络不好自动重试3次
                //可以全局统一设置超时重试间隔时间,默认为500ms,不需要可以设置为0
                .setRetryDelay(500)//每次延时500ms重试
                //可以全局统一设置超时重试间隔叠加时间,默认为0ms不叠加
                .setRetryIncreaseDelay(200)//每次延时叠加500ms

                //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体请看CacheMode
                //先使用缓存，不管是否存在，仍然请求网络，会先把缓存回调给你，
                //等网络请求回来发现数据是一样的就不会再返回，否则再返回
                //（这样做的目的是防止数据是一样的你也需要刷新界面）
                .setCacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                //可以全局统一设置缓存时间,默认永不过期
                .setCacheTime(0)//-1表示永久缓存,单位:秒 ，Okhttp和自定义RxCache缓存都起作用
                //全局设置自定义缓存保存转换器，主要针对自定义RxCache缓存
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                //全局设置自定义缓存大小，默认50M
                .setCacheMaxSize(10 * 1024 * 1024)//设置缓存大小为100M
                //设置缓存版本，如果缓存有变化，修改版本后，缓存就不会被加载。特别是用于版本重大升级时缓存不能使用的情况
                .setCacheVersion(1)//缓存版本为1
                .addNetworkInterceptor(new NoCacheInterceptor())//设置网络拦截器
                .setCookieStore(new CookieManger(this));//设置cookie

    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.base_white, R.color.base_yellow);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context)
                        .setDrawableSize(20)
                        .setAccentColorId(R.color.base_yellow);
            }
        });
    }
}
