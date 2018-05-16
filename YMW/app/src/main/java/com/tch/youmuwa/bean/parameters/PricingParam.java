package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/28.
 */

public class PricingParam {
    private int id;
    private String price;

    public PricingParam() {
    }

    public PricingParam(int id, String price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
