package com.tch.youmuwa.bean.parameters;

import java.io.Serializable;

/**
 * Created by peng on 2017/9/7.
 */

public class JpushParam implements Serializable {

    private String message_id;
    private String unreadcount;
    private String type;
    private String user_type;
    private String require_id;
    private String order_number;
    private String order_id;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getUnreadcount() {
        return unreadcount;
    }

    public void setUnreadcount(String unreadcount) {
        this.unreadcount = unreadcount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getRequire_id() {
        return require_id;
    }

    public void setRequire_id(String require_id) {
        this.require_id = require_id;
    }
}
