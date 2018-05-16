package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/14.
 */

public class SwitchRolesParam {

    private int type;

    public SwitchRolesParam() {
    }

    public SwitchRolesParam(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
