package com.tch.youmuwa.bean.result;

/**
 * 服务器返回结果基类
 */

public class BaseBean<T> {

    private String msg = "";

    private int state;

    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
