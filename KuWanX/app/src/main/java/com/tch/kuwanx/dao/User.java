package com.tch.kuwanx.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jiaop on 2017/11/27.
 * 用户信息
 */
@Entity
public class User {

    @Id
    private Long id;

    private String phone;

    private String userId;

    private String nickname;

    private String user_idcard;

    private String user_realname;

    private String paypassword;

    private String accountnum;

    private String yunToken;

    private String headpic;

    @Generated(hash = 283243032)
    public User(Long id, String phone, String userId, String nickname,
            String user_idcard, String user_realname, String paypassword,
            String accountnum, String yunToken, String headpic) {
        this.id = id;
        this.phone = phone;
        this.userId = userId;
        this.nickname = nickname;
        this.user_idcard = user_idcard;
        this.user_realname = user_realname;
        this.paypassword = paypassword;
        this.accountnum = accountnum;
        this.yunToken = yunToken;
        this.headpic = headpic;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_idcard() {
        return this.user_idcard;
    }

    public void setUser_idcard(String user_idcard) {
        this.user_idcard = user_idcard;
    }

    public String getUser_realname() {
        return this.user_realname;
    }

    public void setUser_realname(String user_realname) {
        this.user_realname = user_realname;
    }

    public String getPaypassword() {
        return this.paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public String getAccountnum() {
        return this.accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public String getYunToken() {
        return this.yunToken;
    }

    public void setYunToken(String yunToken) {
        this.yunToken = yunToken;
    }

    public String getHeadpic() {
        return this.headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    
}
