package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop
 * 商城-获取Banner
 */

public class GoodAdResult {

    private String msg;
    private String ret;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {

        private String ad_id;
        private String ad_order;
        private String ad_status;
        private String ad_type;
        private String create_id;
        private String create_time;
        private String img_url;

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getAd_order() {
            return ad_order;
        }

        public void setAd_order(String ad_order) {
            this.ad_order = ad_order;
        }

        public String getAd_status() {
            return ad_status;
        }

        public void setAd_status(String ad_status) {
            this.ad_status = ad_status;
        }

        public String getAd_type() {
            return ad_type;
        }

        public void setAd_type(String ad_type) {
            this.ad_type = ad_type;
        }

        public String getCreate_id() {
            return create_id;
        }

        public void setCreate_id(String create_id) {
            this.create_id = create_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
