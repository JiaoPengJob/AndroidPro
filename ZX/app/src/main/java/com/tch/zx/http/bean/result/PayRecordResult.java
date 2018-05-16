package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/10/16.
 */

public class PayRecordResult {

    private List<ResponseObjectBean> responseObject;

    public List<ResponseObjectBean> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<ResponseObjectBean> responseObject) {
        this.responseObject = responseObject;
    }

    public static class ResponseObjectBean {
        /**
         * createtime : 2017-07-14 16:36:24.0
         * orderMoney : 0
         * payUserId : d8f08b34f2b7490481097328bf3a66fb
         * title : 直播测试002
         */

        private String createtime;
        private String orderMoney;
        private String payUserId;
        private String title;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getOrderMoney() {
            return orderMoney;
        }

        public void setOrderMoney(String orderMoney) {
            this.orderMoney = orderMoney;
        }

        public String getPayUserId() {
            return payUserId;
        }

        public void setPayUserId(String payUserId) {
            this.payUserId = payUserId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
