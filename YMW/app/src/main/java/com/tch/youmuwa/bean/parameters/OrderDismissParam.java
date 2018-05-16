package com.tch.youmuwa.bean.parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/25.
 */

public class OrderDismissParam {

    private String order_number;
    private String description;
    private List<String> reasons = new ArrayList<String>();

    public OrderDismissParam() {
    }

    public OrderDismissParam(String order_number, String description) {
        this.order_number = order_number;
        this.description = description;
    }

    public OrderDismissParam(String order_number, String description, List<String> reasons) {
        this.order_number = order_number;
        this.description = description;
        this.reasons = reasons;
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

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}
