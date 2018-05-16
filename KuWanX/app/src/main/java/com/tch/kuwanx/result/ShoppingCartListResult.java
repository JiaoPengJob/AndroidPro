package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/18.
 * 用户购物车列表
 */

public class ShoppingCartListResult {

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

        private String appuser_id;
        private String cart_id;
        private String cost_price;
        private String current_price;
        private String good_count;
        private String good_cover;
        private String good_id;
        private String good_intr;
        private String good_name;
        private String good_type_id;
        private String spec;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getCost_price() {
            return cost_price;
        }

        public void setCost_price(String cost_price) {
            this.cost_price = cost_price;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getGood_count() {
            return good_count;
        }

        public void setGood_count(String good_count) {
            this.good_count = good_count;
        }

        public String getGood_cover() {
            return good_cover;
        }

        public void setGood_cover(String good_cover) {
            this.good_cover = good_cover;
        }

        public String getGood_id() {
            return good_id;
        }

        public void setGood_id(String good_id) {
            this.good_id = good_id;
        }

        public String getGood_intr() {
            return good_intr;
        }

        public void setGood_intr(String good_intr) {
            this.good_intr = good_intr;
        }

        public String getGood_name() {
            return good_name;
        }

        public void setGood_name(String good_name) {
            this.good_name = good_name;
        }

        public String getGood_type_id() {
            return good_type_id;
        }

        public void setGood_type_id(String good_type_id) {
            this.good_type_id = good_type_id;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }
    }
}
