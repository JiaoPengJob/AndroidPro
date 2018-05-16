package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/10/12.
 */

public class GroupMemberListResult {

    private List<GetGroupListResult.ResponseObjectBean.MenberListBean> responseObject;

    public List<GetGroupListResult.ResponseObjectBean.MenberListBean> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<GetGroupListResult.ResponseObjectBean.MenberListBean> responseObject) {
        this.responseObject = responseObject;
    }
}
