package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/14.
 */

public class DefaultedProjectAddressParam {

    private int address_id;

    public DefaultedProjectAddressParam() {
    }

    public DefaultedProjectAddressParam(int address_id) {
        this.address_id = address_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }
}
