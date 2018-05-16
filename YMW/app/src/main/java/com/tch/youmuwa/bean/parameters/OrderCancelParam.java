package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/25.
 */

public class OrderCancelParam {

    private String order_number;
    private String description;

    public OrderCancelParam() {
    }

    public OrderCancelParam(String order_number, String description) {
        this.order_number = order_number;
        this.description = description;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
