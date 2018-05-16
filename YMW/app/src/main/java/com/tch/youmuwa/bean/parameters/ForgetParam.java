package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/8.
 */

public class ForgetParam {

    private String mobile;
    private String smscode;
    private String password;
    private String repassword;

    public ForgetParam() {
    }

    public ForgetParam(String mobile, String smscode, String password, String repassword) {
        this.mobile = mobile;
        this.smscode = smscode;
        this.password = password;
        this.repassword = repassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
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
