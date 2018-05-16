package com.tch.kuwanx.result;

/**
 * Created by jiaop
 *
 */

public class RealAuthResult {

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

        private String userId;
        private String user_idcard;
        private String user_realname;
        private String authedstatus;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUser_idcard() {
            return user_idcard;
        }

        public void setUser_idcard(String user_idcard) {
            this.user_idcard = user_idcard;
        }

        public String getUser_realname() {
            return user_realname;
        }

        public void setUser_realname(String user_realname) {
            this.user_realname = user_realname;
        }

        public String getAuthedstatus() {
            return authedstatus;
        }

        public void setAuthedstatus(String authedstatus) {
            this.authedstatus = authedstatus;
        }
    }
}
