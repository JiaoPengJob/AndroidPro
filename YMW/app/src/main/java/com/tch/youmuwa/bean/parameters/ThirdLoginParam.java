package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/8.
 */

public class ThirdLoginParam {

    private int platform_type;
    private String openid;

    public ThirdLoginParam() {
    }

    public ThirdLoginParam(int platform_type, String openid) {
        this.platform_type = platform_type;
        this.openid = openid;
    }

    public int getPlatform_type() {
        return platform_type;
    }

    public void setPlatform_type(int platform_type) {
        this.platform_type = platform_type;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
