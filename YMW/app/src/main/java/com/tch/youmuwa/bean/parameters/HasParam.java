package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/10/13.
 */

public class HasParam {

    private String page_index;
    private String paged;

    public HasParam() {
    }

    public HasParam(String page_index, String paged) {
        this.page_index = page_index;
        this.paged = paged;
    }

    public String getPage_index() {
        return page_index;
    }

    public void setPage_index(String page_index) {
        this.page_index = page_index;
    }

    public String getPaged() {
        return paged;
    }

    public void setPaged(String paged) {
        this.paged = paged;
    }
}
