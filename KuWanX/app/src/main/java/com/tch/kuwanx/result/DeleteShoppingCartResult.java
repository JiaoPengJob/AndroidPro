package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/18.
 * 购物车删除
 */

public class DeleteShoppingCartResult {

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
        /**
         * cart_id : 4706ab1da38841c0adf082ef3a44f8b7
         */

        private String cart_id;

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }
    }
}
