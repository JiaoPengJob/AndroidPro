package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/29.
 */

public class BeginPayParam {

    private String order_number;
    private int pay_way;

    public BeginPayParam() {
    }

    public BeginPayParam(String order_number, int pay_way) {
        this.order_number = order_number;
        this.pay_way = pay_way;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public int getPay_way() {
        return pay_way;
    }

    public void setPay_way(int pay_way) {
        this.pay_way = pay_way;
    }
}
