package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/8/29.
 */

public class MarkResult {

    private String title = "";
    private double longitude;
    private double latitude;
    private String worker_type_name = "";
    private String work_state = "";
    private String head_image_path = "";
    private String worker_id = "";

    public MarkResult() {
    }

    public MarkResult(String title, double longitude, double latitude, String worker_type_name, String work_state, String head_image_path, String worker_id) {
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.worker_type_name = worker_type_name;
        this.work_state = work_state;
        this.head_image_path = head_image_path;
        this.worker_id = worker_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getWorker_type_name() {
        return worker_type_name;
    }

    public void setWorker_type_name(String worker_type_name) {
        this.worker_type_name = worker_type_name;
    }

    public String getWork_state() {
        return work_state;
    }

    public void setWork_state(String work_state) {
        this.work_state = work_state;
    }

    public String getHead_image_path() {
        return head_image_path;
    }

    public void setHead_image_path(String head_image_path) {
        this.head_image_path = head_image_path;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }
}
