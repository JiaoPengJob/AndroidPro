package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/31.
 * 解散群
 */

public class DeleteGroupResult {

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

        private String app_user_id;
        private String group_id;

        public String getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(String app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }
    }
}
