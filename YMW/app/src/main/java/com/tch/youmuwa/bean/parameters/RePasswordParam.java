package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/16.
 */

public class RePasswordParam {

    private int code;
    private String password;
    private String repassword;

    public RePasswordParam() {
    }

    public RePasswordParam(int code, String password, String repassword) {
        this.code = code;
        this.password = password;
        this.repassword = repassword;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
}
