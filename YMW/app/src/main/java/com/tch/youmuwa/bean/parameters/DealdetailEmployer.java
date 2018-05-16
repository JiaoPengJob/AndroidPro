package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/31.
 */

public class DealdetailEmployer {

    private String date;
    private int page_index;
    private int paged = 10;

    public DealdetailEmployer() {
    }

    public DealdetailEmployer(String date, int page_index) {
        this.date = date;
        this.page_index = page_index;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
