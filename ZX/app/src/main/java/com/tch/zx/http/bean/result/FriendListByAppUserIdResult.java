package com.tch.zx.http.bean.result;

/**
 * Created by peng on 2017/9/26.
 */

public class FriendListByAppUserIdResult {

    private String app_user_id;
    private String name;
    private String user_picture;
    private String company_name;
    private String company_position;

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_position() {
        return company_position;
    }

    public void setCompany_position(String company_position) {
        this.company_position = company_position;
    }
}
