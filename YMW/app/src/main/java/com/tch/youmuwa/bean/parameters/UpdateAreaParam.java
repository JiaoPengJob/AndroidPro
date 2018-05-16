package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/14.
 */

public class UpdateAreaParam {

    private String worker_province;
    private String worker_city;
    private String worker_area;

    public UpdateAreaParam() {
    }

    public UpdateAreaParam(String worker_province, String worker_city, String worker_area) {
        this.worker_province = worker_province;
        this.worker_city = worker_city;
        this.worker_area = worker_area;
    }

    public String getWorker_province() {
        return worker_province;
    }

    public void setWorker_province(String worker_province) {
        this.worker_province = worker_province;
    }

    public String getWorker_city() {
        return worker_city;
    }

    public void setWorker_city(String worker_city) {
        this.worker_city = worker_city;
    }

    public String getWorker_area() {
        return worker_area;
    }

    public void setWorker_area(String worker_area) {
        this.worker_area = worker_area;
    }
}
