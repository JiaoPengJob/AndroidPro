package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/18.
 * 加入购物车
 */

public class AddShoppingCartResult {

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
        private String good_count;
        private String good_id;
        private String cart_id;
        private String create_time;
        private String cart_status;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
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

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCart_status() {
            return cart_status;
        }

        public void setCart_status(String cart_status) {
            this.cart_status = cart_status;
        }
    }
}
