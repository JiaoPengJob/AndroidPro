package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/22.
 */

public class OrdersListParam {

    private int page_index;
    private int paged;
    private String state;

    public OrdersListParam() {
    }

    public OrdersListParam(int page_index, int paged, String state) {
        this.page_index = page_index;
        this.paged = paged;
        this.state = state;
    }

    public int getPage_index() {
        return page_index;
    }

    public void setPage_index(int page_index) {
        this.page_index = page_index;
    }

    public int getPaged() {
        return paged;
    }

    public void setPaged(int paged) {
        this.paged = paged;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
