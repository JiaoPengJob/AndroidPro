package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/17.
 * 热销商品top3
 */

public class StoreTopThreeResult {

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

        private String brand_id;
        private String cost_price;
        private String create_id;
        private String create_time;
        private String current_price;
        private String good_cover;
        private String good_desc;
        private String good_intr;
        private String good_name;
        private String good_no;
        private String good_status;
        private String good_type_id;
        private String good_unit;
        private String id;
        private String is_sell_hot;
        private String is_shelves;
        private String sale_num;
        private String spec;
        private String stock_num;
        private String good_detail;

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getCost_price() {
            return cost_price;
        }

        public void setCost_price(String cost_price) {
            this.cost_price = cost_price;
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

        public String getGood_desc() {
            return good_desc;
        }

        public void setGood_desc(String good_desc) {
            this.good_desc = good_desc;
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

        public String getGood_no() {
            return good_no;
        }

        public void setGood_no(String good_no) {
            this.good_no = good_no;
        }

        public String getGood_status() {
            return good_status;
        }

        public void setGood_status(String good_status) {
            this.good_status = good_status;
        }

        public String getGood_type_id() {
            return good_type_id;
        }

        public void setGood_type_id(String good_type_id) {
            this.good_type_id = good_type_id;
        }

        public String getGood_unit() {
            return good_unit;
        }

        public void setGood_unit(String good_unit) {
            this.good_unit = good_unit;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_sell_hot() {
            return is_sell_hot;
        }

        public void setIs_sell_hot(String is_sell_hot) {
            this.is_sell_hot = is_sell_hot;
        }

        public String getIs_shelves() {
            return is_shelves;
        }

        public void setIs_shelves(String is_shelves) {
            this.is_shelves = is_shelves;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
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

        public String getGood_detail() {
            return good_detail;
        }

        public void setGood_detail(String good_detail) {
            this.good_detail = good_detail;
        }
    }
}
