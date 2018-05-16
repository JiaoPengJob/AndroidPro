package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/7.
 */

public class GetCodeParam {

    private String mobile;
    private int type;

    public GetCodeParam() {
    }

    public GetCodeParam(String mobile, int type) {
        this.mobile = mobile;
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
