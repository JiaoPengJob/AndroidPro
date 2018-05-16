package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/30.
 */

public class MsgParam {

    private String message_id;

    public MsgParam() {
    }

    public MsgParam(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
