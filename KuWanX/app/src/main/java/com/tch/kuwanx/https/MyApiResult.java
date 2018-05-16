package com.tch.kuwanx.https;

import com.zhouyou.http.model.ApiResult;

/**
 * Created by jiaop on 2017/12/1.
 * 获取接口返回数据类
 */

public class MyApiResult<T> extends ApiResult<T> {

    private int ret;
    private String msg;
    private T result;

    @Override
    public int getCode() {
        return ret;
    }

    @Override
    public void setCode(int code) {
        this.ret = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public T getData() {
        return result;
    }

    @Override
    public void setData(T data) {
        result = data;
    }

    @Override
    public boolean isOk() {
        return ret == 1 ? true : false;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "ret='" + ret + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
