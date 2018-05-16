package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/21.
 */

public class GetRequireResult {

    private String area;
    private String keyword;
    private String page_index;
    private String paged = "10";

    public GetRequireResult() {
    }

    public GetRequireResult(String area, String keyword, String page_index, String paged) {
        this.area = area;
        this.keyword = keyword;
        this.page_index = page_index;
        this.paged = paged;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
