package com.tch.youmuwa.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户信息
 */
@Entity
public class UserInfo {

    @Id(autoincrement = true)
    private Long id;

    private int type;
    private int result;
    private String phone;
    private String pwd;
    private boolean ifPwdLogin;
    @Generated(hash = 899494593)
    public UserInfo(Long id, int type, int result, String phone, String pwd,
            boolean ifPwdLogin) {
        this.id = id;
        this.type = type;
        this.result = result;
        this.phone = phone;
        this.pwd = pwd;
        this.ifPwdLogin = ifPwdLogin;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getResult() {
        return this.result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public boolean getIfPwdLogin() {
        return this.ifPwdLogin;
    }
    public void setIfPwdLogin(boolean ifPwdLogin) {
        this.ifPwdLogin = ifPwdLogin;
    }
    
}
