package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/7.
 */

public class RegisterParam {

    private String mobile;
    private int code;
    private String password;
    private String repassword;
    private int type;
    private int agreement;

    public RegisterParam() {
    }

    public RegisterParam(String mobile, int code, String password, String repassword, int type, int agreement) {
        this.mobile = mobile;
        this.code = code;
        this.password = password;
        this.repassword = repassword;
        this.type = type;
        this.agreement = agreement;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAgreement() {
        return agreement;
    }

    public void setAgreement(int agreement) {
        this.agreement = agreement;
    }
}
