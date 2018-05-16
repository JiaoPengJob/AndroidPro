package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop
 * 换物订单
 */

public class SwapOrderResult {

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

        private String cdid;
        private String headpic;
        private String id;
        private String name;
        private String order_status;
        private String pay_deposit;
        private String post_user_id;
        private String swap_user_id;
        private String type;

        public String getCdid() {
            return cdid;
        }

        public void setCdid(String cdid) {
            this.cdid = cdid;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getPay_deposit() {
            return pay_deposit;
        }

        public void setPay_deposit(String pay_deposit) {
            this.pay_deposit = pay_deposit;
        }

        public String getPost_user_id() {
            return post_user_id;
        }

        public void setPost_user_id(String post_user_id) {
            this.post_user_id = post_user_id;
        }

        public String getSwap_user_id() {
            return swap_user_id;
        }

        public void setSwap_user_id(String swap_user_id) {
            this.swap_user_id = swap_user_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
