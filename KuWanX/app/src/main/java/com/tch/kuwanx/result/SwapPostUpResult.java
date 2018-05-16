package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/13.
 * 点赞
 */

public class SwapPostUpResult {

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

        private String appuser_id;
        private String post_id;
        private String id;
        private String up_time;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUp_time() {
            return up_time;
        }

        public void setUp_time(String up_time) {
            this.up_time = up_time;
        }
    }
}
