package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/31.
 * 退群
 */

public class QuitGroupResult {

    private String msg;
    private ResultBean result;
    private String ret;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public static class ResultBean {

        private String group_id;
        private String member_app_user_id;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getMember_app_user_id() {
            return member_app_user_id;
        }

        public void setMember_app_user_id(String member_app_user_id) {
            this.member_app_user_id = member_app_user_id;
        }
    }
}
