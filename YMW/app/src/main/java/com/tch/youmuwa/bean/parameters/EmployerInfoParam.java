package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/9/13.
 */

public class EmployerInfoParam {

    private String name;
    private int sex;

    public EmployerInfoParam() {
    }

    public EmployerInfoParam(String name, int sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
