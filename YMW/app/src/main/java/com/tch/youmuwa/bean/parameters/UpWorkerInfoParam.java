package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/14.
 */

public class UpWorkerInfoParam {

    private int sex;
    private String work_province;
    private String work_city;
    private String work_area;
    private String worker_age;
    private String worker_type;
    private String strength;

    public UpWorkerInfoParam() {
    }

    public UpWorkerInfoParam(int sex, String work_province, String work_city, String work_area, String worker_age, String worker_type, String strength) {
        this.sex = sex;
        this.work_province = work_province;
        this.work_city = work_city;
        this.work_area = work_area;
        this.worker_age = worker_age;
        this.worker_type = worker_type;
        this.strength = strength;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getWork_province() {
        return work_province;
    }

    public void setWork_province(String work_province) {
        this.work_province = work_province;
    }

    public String getWork_city() {
        return work_city;
    }

    public void setWork_city(String work_city) {
        this.work_city = work_city;
    }

    public String getWork_area() {
        return work_area;
    }

    public void setWork_area(String work_area) {
        this.work_area = work_area;
    }

    public String getWorker_age() {
        return worker_age;
    }

    public void setWorker_age(String worker_age) {
        this.worker_age = worker_age;
    }

    public String getWorker_type() {
        return worker_type;
    }

    public void setWorker_type(String worker_type) {
        this.worker_type = worker_type;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
