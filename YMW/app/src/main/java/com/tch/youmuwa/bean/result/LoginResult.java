package com.tch.youmuwa.bean.result;

/**
 * 登录返回结果
 */

public class LoginResult {

    /**
     * id : 18
     * mobile : 13520518525
     * password : $2y$10$XQD4WjF30qOSfYd8sTbfKO6Usvc9IVqcpu64yuNvu8hcc4o4LchJS
     */

    private int id;
    private String mobile = "";
    private String password = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
