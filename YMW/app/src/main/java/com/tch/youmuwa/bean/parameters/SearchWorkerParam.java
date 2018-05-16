package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/9.
 */

public class SearchWorkerParam {

    private String keyword;
    private int page_index = 0;
    private int paged = 20;
    private String city;

    public SearchWorkerParam() {
    }

    public SearchWorkerParam(String keyword, int page_index, int paged, String city) {
        this.keyword = keyword;
        this.page_index = page_index;
        this.paged = paged;
        this.city = city;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
