package com.tch.kuwanx.bean;

import io.rong.imlib.model.Conversation;

/**
 * Created by jiaop on 2018/1/31.
 */

public class MyConversation extends Conversation {

    private String name;
    private String headimg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
}
