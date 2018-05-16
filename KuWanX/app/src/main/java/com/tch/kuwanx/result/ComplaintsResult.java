package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/3/20.
 * 投诉
 */

public class ComplaintsResult {

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

        private String complaint_content;
        private String complaint_user_id;
        private String user_id;
        private String id;
        private String complaint_time;
        private String complaint_status;

        public String getComplaint_content() {
            return complaint_content;
        }

        public void setComplaint_content(String complaint_content) {
            this.complaint_content = complaint_content;
        }

        public String getComplaint_user_id() {
            return complaint_user_id;
        }

        public void setComplaint_user_id(String complaint_user_id) {
            this.complaint_user_id = complaint_user_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComplaint_time() {
            return complaint_time;
        }

        public void setComplaint_time(String complaint_time) {
            this.complaint_time = complaint_time;
        }

        public String getComplaint_status() {
            return complaint_status;
        }

        public void setComplaint_status(String complaint_status) {
            this.complaint_status = complaint_status;
        }
    }
}
