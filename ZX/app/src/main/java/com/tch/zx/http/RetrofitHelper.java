package com.tch.zx.http;

import android.content.Context;
import android.util.Log;

import com.tch.zx.http.subscriber.GsonResponseBodyConverter;
import com.tch.zx.http.subscriber.ResponseConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 用于Retrofit初始化
 */

public class RetrofitHelper {
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
    private static RetrofitHelper instance = null;
    /**
     * Retrofit对象
     */
    private Retrofit mRetrofit = null;
    /**
     * 基础url
     */
    public static String baseUrl = "http://47.93.191.99:9001/zhixian_api/";

    /**
     * 获取单例
     *
     * @param context
     * @return
     */
    public static RetrofitHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitHelper(context);
        }
        return instance;
    }

    /**
     * 构造函数
     *
     * @param mContext
     */
    private RetrofitHelper(Context mContext) {
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
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(getHttpLoggingInterceptor())
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 获取接口类
     *
     * @return
     */
    public HttpBaseService getServer() {
        return mRetrofit.create(HttpBaseService.class);
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
                        Log.e("OkHttp", "log = =" + message.toString());
                    }

                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

}
