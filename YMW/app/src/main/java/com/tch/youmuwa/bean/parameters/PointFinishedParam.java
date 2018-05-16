package com.tch.youmuwa.bean.parameters;

/**
 * Created by JoePeng on 2017/8/27.
 */

public class PointFinishedParam {

    private int id;
    private int worker_days;

    public PointFinishedParam() {
    }

    public PointFinishedParam(int id, int worker_days) {
        this.id = id;
        this.worker_days = worker_days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorker_days() {
        return worker_days;
    }

    public void setWorker_days(int worker_days) {
        this.worker_days = worker_days;
    }
}
