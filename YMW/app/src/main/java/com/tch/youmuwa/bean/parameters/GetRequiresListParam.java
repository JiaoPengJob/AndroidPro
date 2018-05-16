package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/15.
 */

public class GetRequiresListParam {

    private int page_index;
    private int paged;
    private String push_date;
    private String keyword;

    public GetRequiresListParam() {
    }

    public GetRequiresListParam(int page_index, int paged, String push_date, String keyword) {
        this.page_index = page_index;
        this.paged = paged;
        this.push_date = push_date;
        this.keyword = keyword;
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

    public String getPush_date() {
        return push_date;
    }

    public void setPush_date(String push_date) {
        this.push_date = push_date;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
