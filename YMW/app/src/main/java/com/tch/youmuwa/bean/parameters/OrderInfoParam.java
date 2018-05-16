package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/22.
 */

public class OrderInfoParam {

    private String order_number;

    public OrderInfoParam() {
    }

    public OrderInfoParam(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

}
