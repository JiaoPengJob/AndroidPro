package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/30.
 */

public class BindMobileParam {

    private String mobile;
    private String code;

    public BindMobileParam() {
    }

    public BindMobileParam(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
