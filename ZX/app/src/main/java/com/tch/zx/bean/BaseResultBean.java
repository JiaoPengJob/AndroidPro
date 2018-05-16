package com.tch.zx.bean;

/**
 * Created by peng on 2017/7/14.
 */

public class BaseResultBean<T> {

    private String ret;
    private T result;

    public BaseResultBean() {
    }

    public BaseResultBean(String ret, T result) {
        this.ret = ret;
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
