package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/3/6.
 * 发布置换
 */

public class PublishPostResult {

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
        private String cds_address;
        private String delivery_mode;
        private String my_cds_description;
        private String my_cds_id;
        private String pay_deposit;
        private String swap_cds;
        private String swap_cycle;
        private String swap_deposit;
        private String swap_type;
        private String id;
        private String post_status;
        private String publish_time;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getCds_address() {
            return cds_address;
        }

        public void setCds_address(String cds_address) {
            this.cds_address = cds_address;
        }

        public String getDelivery_mode() {
            return delivery_mode;
        }

        public void setDelivery_mode(String delivery_mode) {
            this.delivery_mode = delivery_mode;
        }

        public String getMy_cds_description() {
            return my_cds_description;
        }

        public void setMy_cds_description(String my_cds_description) {
            this.my_cds_description = my_cds_description;
        }

        public String getMy_cds_id() {
            return my_cds_id;
        }

        public void setMy_cds_id(String my_cds_id) {
            this.my_cds_id = my_cds_id;
        }

        public String getPay_deposit() {
            return pay_deposit;
        }

        public void setPay_deposit(String pay_deposit) {
            this.pay_deposit = pay_deposit;
        }

        public String getSwap_cds() {
            return swap_cds;
        }

        public void setSwap_cds(String swap_cds) {
            this.swap_cds = swap_cds;
        }

        public String getSwap_cycle() {
            return swap_cycle;
        }

        public void setSwap_cycle(String swap_cycle) {
            this.swap_cycle = swap_cycle;
        }

        public String getSwap_deposit() {
            return swap_deposit;
        }

        public void setSwap_deposit(String swap_deposit) {
            this.swap_deposit = swap_deposit;
        }

        public String getSwap_type() {
            return swap_type;
        }

        public void setSwap_type(String swap_type) {
            this.swap_type = swap_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPost_status() {
            return post_status;
        }

        public void setPost_status(String post_status) {
            this.post_status = post_status;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }
    }
}
