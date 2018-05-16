package com.tch.zx.http.subscriber;

import android.util.Log;

import com.google.gson.Gson;
import com.tch.zx.http.bean.result.SearchInfoResultBean;
import com.tch.zx.http.exception.ResultException;
import com.tch.zx.http.presenter.SearchInfoPresenter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 自定义Gson响应体变换器
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;


    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.e("TAG", "GsonResponseBodyConverter==" + response);
        return null;
    }
}
