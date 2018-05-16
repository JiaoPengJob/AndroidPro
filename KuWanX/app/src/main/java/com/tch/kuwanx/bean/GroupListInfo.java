package com.tch.kuwanx.bean;

import com.tch.kuwanx.result.GroupListResult;

import io.rong.imlib.model.Conversation;

/**
 * Created by jiaop on 2018/1/27.
 * 群组列表信息
 */

public class GroupListInfo {

    private Conversation conversation;
    private GroupListResult.ResultBean resultBean;

    public GroupListInfo() {
    }

    public GroupListInfo(Conversation conversation, GroupListResult.ResultBean resultBean) {
        this.conversation = conversation;
        this.resultBean = resultBean;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public GroupListResult.ResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(GroupListResult.ResultBean resultBean) {
        this.resultBean = resultBean;
    }
}
