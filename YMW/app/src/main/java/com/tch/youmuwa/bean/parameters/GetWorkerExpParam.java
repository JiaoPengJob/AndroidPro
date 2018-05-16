package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/9.
 */

public class GetWorkerExpParam {

    private int worker_id;
    private int page_index = 0;
    private int paged = 10;

    public GetWorkerExpParam() {
    }

    public GetWorkerExpParam(int worker_id, int page_index, int paged) {
        this.worker_id = worker_id;
        this.page_index = page_index;
        this.paged = paged;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
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
