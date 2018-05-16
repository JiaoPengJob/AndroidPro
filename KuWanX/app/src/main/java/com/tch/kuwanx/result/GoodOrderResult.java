package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/23.
 * 商城订单列表
 */

public class GoodOrderResult {

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


        private String id;
        private String order_amt;
        private String order_no;
        private String order_status;
        private String order_time;
        private List<GoodListBean> goodList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_amt() {
            return order_amt;
        }

        public void setOrder_amt(String order_amt) {
            this.order_amt = order_amt;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public List<GoodListBean> getGoodList() {
            return goodList;
        }

        public void setGoodList(List<GoodListBean> goodList) {
            this.goodList = goodList;
        }

        public static class GoodListBean {

            private String cost_price;
            private String current_price;
            private String good_count;
            private String good_cover;
            private String good_id;
            private String good_intr;
            private String good_name;
            private String good_type_id;

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
        }
    }
}
