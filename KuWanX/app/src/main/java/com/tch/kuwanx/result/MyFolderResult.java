package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/23.
 * 我的收藏夹列表
 */

public class MyFolderResult {

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

        private String cost_price;
        private String create_time;
        private String current_price;
        private String good_cover;
        private String good_name;
        private String id;
        private String spec;
        private String stock_num;

        public String getCost_price() {
            return cost_price;
        }

        public void setCost_price(String cost_price) {
            this.cost_price = cost_price;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getGood_cover() {
            return good_cover;
        }

        public void setGood_cover(String good_cover) {
            this.good_cover = good_cover;
        }

        public String getGood_name() {
            return good_name;
        }

        public void setGood_name(String good_name) {
            this.good_name = good_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public String getStock_num() {
            return stock_num;
        }

        public void setStock_num(String stock_num) {
            this.stock_num = stock_num;
        }
    }
}
