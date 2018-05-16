package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/30.
 * 申请添加好友
 */

public class AddFriendResult {

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
        private String apply_app_user_id;
        private String verification_message;
        private String id;
        private String apply_status;
        private String create_date;
        private String new_status;

        public String getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(String app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getApply_app_user_id() {
            return apply_app_user_id;
        }

        public void setApply_app_user_id(String apply_app_user_id) {
            this.apply_app_user_id = apply_app_user_id;
        }

        public String getVerification_message() {
            return verification_message;
        }

        public void setVerification_message(String verification_message) {
            this.verification_message = verification_message;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApply_status() {
            return apply_status;
        }

        public void setApply_status(String apply_status) {
            this.apply_status = apply_status;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getNew_status() {
            return new_status;
        }

        public void setNew_status(String new_status) {
            this.new_status = new_status;
        }
    }
}
