package com.tch.zx.bean;

/**
 * Created by peng on 2017/7/14.
 */

public class Result<T> {

    private T responseObject;

    public Result() {
    }

    public Result(T responseObject) {
        this.responseObject = responseObject;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }
}
