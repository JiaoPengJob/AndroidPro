package com.tch.kuwanx.bean;

import com.tch.kuwanx.result.GroupMemberListResult;

/**
 * Created by jiaop on 2018/1/31.
 * 群成员管理
 */

public class GroupManagerMembers {

    private String addImg;
    private boolean isShow;
    private GroupMemberListResult.ResultBean.MemberListBean groupMemberListResult;

    public GroupManagerMembers() {
    }

    public GroupManagerMembers(String addImg, boolean isShow, GroupMemberListResult.ResultBean.MemberListBean groupMemberListResult) {
        this.addImg = addImg;
        this.isShow = isShow;
        this.groupMemberListResult = groupMemberListResult;
    }

    public String getAddImg() {
        return addImg;
    }

    public void setAddImg(String addImg) {
        this.addImg = addImg;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public GroupMemberListResult.ResultBean.MemberListBean getGroupMemberListResult() {
        return groupMemberListResult;
    }

    public void setGroupMemberListResult(GroupMemberListResult.ResultBean.MemberListBean groupMemberListResult) {
        this.groupMemberListResult = groupMemberListResult;
    }
}
