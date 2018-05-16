package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/22.
 */

public class DeleteAreaParam {

    private int address_id;

    public DeleteAreaParam() {
    }

    public DeleteAreaParam(int address_id) {
        this.address_id = address_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }
}
