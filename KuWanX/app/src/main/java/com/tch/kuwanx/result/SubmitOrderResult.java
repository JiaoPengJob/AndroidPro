package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/19.
 * 提交订单
 */

public class SubmitOrderResult {

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
        private String consignee;
        private String consignee_adress;
        private String consignee_phone;
        private String good_count;
        private String good_id;
        private String order_amt;
        private String peisong_mode;
        private String user_message;
        private String id;
        private String order_no;
        private String order_time;
        private String order_status;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getConsignee_adress() {
            return consignee_adress;
        }

        public void setConsignee_adress(String consignee_adress) {
            this.consignee_adress = consignee_adress;
        }

        public String getConsignee_phone() {
            return consignee_phone;
        }

        public void setConsignee_phone(String consignee_phone) {
            this.consignee_phone = consignee_phone;
        }

        public String getGood_count() {
            return good_count;
        }

        public void setGood_count(String good_count) {
            this.good_count = good_count;
        }

        public String getGood_id() {
            return good_id;
        }

        public void setGood_id(String good_id) {
            this.good_id = good_id;
        }

        public String getOrder_amt() {
            return order_amt;
        }

        public void setOrder_amt(String order_amt) {
            this.order_amt = order_amt;
        }

        public String getPeisong_mode() {
            return peisong_mode;
        }

        public void setPeisong_mode(String peisong_mode) {
            this.peisong_mode = peisong_mode;
        }

        public String getUser_message() {
            return user_message;
        }

        public void setUser_message(String user_message) {
            this.user_message = user_message;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }
    }
}
