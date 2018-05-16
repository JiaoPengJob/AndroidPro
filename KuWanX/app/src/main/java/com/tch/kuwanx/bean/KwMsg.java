package com.tch.kuwanx.bean;

import java.io.Serializable;

/**
 * Created by jiaop on 2017/12/2.
 * 消息中传递数据用
 */

public class KwMsg implements Serializable{

    private String state;
    private String msgId;
    private String type;

    public KwMsg() {
    }

    public KwMsg(String state, String msgId, String type) {
        this.state = state;
        this.msgId = msgId;
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
