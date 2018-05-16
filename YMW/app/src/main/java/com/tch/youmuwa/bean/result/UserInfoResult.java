package com.tch.youmuwa.bean.result;

import java.io.Serializable;

/**
 * Created by peng on 2017/8/14.
 */

public class UserInfoResult implements Serializable {

    /**
     * type : 2
     * id : 36
     * user_id : 53
     * mobile : 13520518525
     * name : 焦鹏
     * worker_type : 1
     * work_province : 北京市
     * work_city : 北京市
     * work_area : 昌平区
     * sex : 0
     * age : 22
     * avator : http://t3.ychlink.com/storage/users/36/worker150460185659ae670014179.jpg
     * work_state : 1
     * work_age : 1
     * strength : null
     */

    private int type;
    private int id;
    private int user_id;
    private String mobile = "";
    private String name = "未设置";
    private int worker_type;
    private String work_province = "";
    private String work_city = "";
    private String work_area = "";
    private int sex;
    private int age;
    private String avator = "";
    private int work_state;
    private int work_age;
    private String strength = "";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorker_type() {
        return worker_type;
    }

    public void setWorker_type(int worker_type) {
        this.worker_type = worker_type;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public int getWork_state() {
        return work_state;
    }

    public void setWork_state(int work_state) {
        this.work_state = work_state;
    }

    public int getWork_age() {
        return work_age;
    }

    public void setWork_age(int work_age) {
        this.work_age = work_age;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
