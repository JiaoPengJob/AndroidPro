package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/27.
 * 刷新token
 */

public class RefreshTokenResult {

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

        private String id;
        private String yunToken;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getYunToken() {
            return yunToken;
        }

        public void setYunToken(String yunToken) {
            this.yunToken = yunToken;
        }
    }
}
