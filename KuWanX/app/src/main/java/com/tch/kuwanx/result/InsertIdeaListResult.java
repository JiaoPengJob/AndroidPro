package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/2/2.
 * 意见反馈
 */

public class InsertIdeaListResult {

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

        private String fm_appuserid;
        private String fm_content;
        private String fm_phone;
        private String fm_createdate;

        public String getFm_appuserid() {
            return fm_appuserid;
        }

        public void setFm_appuserid(String fm_appuserid) {
            this.fm_appuserid = fm_appuserid;
        }

        public String getFm_content() {
            return fm_content;
        }

        public void setFm_content(String fm_content) {
            this.fm_content = fm_content;
        }

        public String getFm_phone() {
            return fm_phone;
        }

        public void setFm_phone(String fm_phone) {
            this.fm_phone = fm_phone;
        }

        public String getFm_createdate() {
            return fm_createdate;
        }

        public void setFm_createdate(String fm_createdate) {
            this.fm_createdate = fm_createdate;
        }
    }
}
