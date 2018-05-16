package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/13.
 * 置换评论
 */

public class AddPostCommentsResult {

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
        private String comm_content;
        private String post_id;
        private String comm_id;
        private String comm_createtime;
        private String comm_status;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getComm_content() {
            return comm_content;
        }

        public void setComm_content(String comm_content) {
            this.comm_content = comm_content;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getComm_id() {
            return comm_id;
        }

        public void setComm_id(String comm_id) {
            this.comm_id = comm_id;
        }

        public String getComm_createtime() {
            return comm_createtime;
        }

        public void setComm_createtime(String comm_createtime) {
            this.comm_createtime = comm_createtime;
        }

        public String getComm_status() {
            return comm_status;
        }

        public void setComm_status(String comm_status) {
            this.comm_status = comm_status;
        }
    }
}
