package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/11.
 */

public class CompleteParam {

    private String name;
    private String idcard;
    private int age;
    private int worker_type;
    private String worker_age;
    private String worker_province;
    private String worker_city;
    private String worker_area;
    private String coordinate;

    public CompleteParam() {
    }

    public CompleteParam(String name, String idcard, int age, int worker_type, String worker_age, String worker_province, String worker_city, String worker_area, String coordinate) {
        this.name = name;
        this.idcard = idcard;
        this.age = age;
        this.worker_type = worker_type;
        this.worker_age = worker_age;
        this.worker_province = worker_province;
        this.worker_city = worker_city;
        this.worker_area = worker_area;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWorker_type() {
        return worker_type;
    }

    public void setWorker_type(int worker_type) {
        this.worker_type = worker_type;
    }

    public String getWorker_age() {
        return worker_age;
    }

    public void setWorker_age(String worker_age) {
        this.worker_age = worker_age;
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

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }
}
