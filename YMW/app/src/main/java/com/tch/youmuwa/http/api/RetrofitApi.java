package com.tch.youmuwa.http.api;

import android.content.Context;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jkyeo.basicparamsinterceptor.BasicParamsInterceptor;
import com.tch.youmuwa.http.subscriber.LogInterceptor;
import com.tch.youmuwa.util.HelperUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 用于Retrofit初始化
 */

public class RetrofitApi {
    /**
     * 文本对象
     */
    private Context mCntext;
    /**
     * Okhttp链接对象
     */
    private OkHttpClient client;
    /**
     * 获取单例
     */
    private static RetrofitApi instance = null;
    /**
     * Retrofit对象
     */
    private Retrofit mRetrofit = null;
    /**
     * 基础url
     */
    private String baseUrl = "http://api.youmuwa.com/api/";
    /**
     * 测试用url
     */
    private String testUrl = "http://a.test.ychlink.com/api/";

    private String testUrl1 = "http://t3.ychlink.com/api/";

    private String appid = "5987ac44a8e90";
    private String secret = "f340bdb02f3824f41f8f6fefb0acb7b2";


    /**
     * 获取单例
     *
     * @param context
     * @return
     */
    public static RetrofitApi getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitApi(context);
        }
        return instance;
    }

    /**
     * 构造函数
     *
     * @param mContext
     */
    private RetrofitApi(Context mContext) {
        mCntext = mContext;
        init();
    }

    /**
     * 初始化加载
     */
    private void init() {
        resetApp();
    }

    /**
     * 创建Retrofit
     */
    private void resetApp() {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("appid", appid);
        paramsMap.put("secret", secret);
        paramsMap.put("timestamp", HelperUtil.getTimestamp());

        BasicParamsInterceptor basicParamsInterceptor =
                new BasicParamsInterceptor.Builder()
                        .addParamsMap(paramsMap)
                        .build();

        File cacheFile = new File(mCntext.getExternalCacheDir().toString(), "cache_ymw");

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mCntext));

        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(new Cache(cacheFile, 10 * 1024 * 1024))
                .cookieJar(cookieJar)
                .addInterceptor(basicParamsInterceptor)
                .addNetworkInterceptor(getHttpLoggingInterceptor())
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void stopClient() {
        if (client != null) {
            client.dispatcher().cancelAll();
        }
    }

    /**
     * 获取接口类
     *
     * @return
     */
    public HttpApi getServer() {
        return mRetrofit.create(HttpApi.class);
    }

    /**
     * 创建日志拦截器
     *
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {

                    @Override
                    public void log(String message) {
                        Log.e("OkHttp", "log = =" + message);
                    }

                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
