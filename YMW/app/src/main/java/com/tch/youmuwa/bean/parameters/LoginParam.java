package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/7.
 */

public class LoginParam {

    private int type;
    private String mobile;
    private String password;

    public LoginParam() {
    }

    public LoginParam(int type, String mobile, String password) {
        this.type = type;
        this.mobile = mobile;
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
