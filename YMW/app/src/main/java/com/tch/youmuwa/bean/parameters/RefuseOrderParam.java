package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/28.
 */

public class RefuseOrderParam {
    private int id;
    private String cause;

    public RefuseOrderParam() {
    }

    public RefuseOrderParam(int id, String cause) {
        this.id = id;
        this.cause = cause;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
