package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/15.
 */

public class WorkerInfoParam {

    private int worker_id;

    public WorkerInfoParam() {
    }

    public WorkerInfoParam(int worker_id) {
        this.worker_id = worker_id;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }
}
