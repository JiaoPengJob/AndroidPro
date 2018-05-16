package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/10.
 */

public class ProjectAddrsParam {

    private int page_index = 0;
    private int paged = 20;

    public ProjectAddrsParam() {
    }

    public ProjectAddrsParam(int page_index, int paged) {
        this.page_index = page_index;
        this.paged = paged;
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
