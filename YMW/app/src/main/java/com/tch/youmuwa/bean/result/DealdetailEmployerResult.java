package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoePeng on 2017/8/26.
 */

public class DealdetailEmployerResult {

    /**
     * msg_list : [{"type":"工资支付","money":"0.01","complete_date":"2017-09-05","isin":0},{"type":"工资支付","money":"0.01","complete_date":"2017-09-05","isin":0},{"type":"辞退工资","money":"0.01","complete_date":"2017-09-05","isin":0}]
     * can_msg_more : 0
     * msg_count : 3
     */

    private int can_msg_more = 0;
    private int msg_count = 0;
    private List<MsgListBean> msg_list = new ArrayList<MsgListBean>();

    public int getCan_msg_more() {
        return can_msg_more;
    }

    public void setCan_msg_more(int can_msg_more) {
        this.can_msg_more = can_msg_more;
    }

    public int getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(int msg_count) {
        this.msg_count = msg_count;
    }

    public List<MsgListBean> getMsg_list() {
        return msg_list;
    }

    public void setMsg_list(List<MsgListBean> msg_list) {
        this.msg_list = msg_list;
    }

    public static class MsgListBean {
        /**
         * type : 工资支付
         * money : 0.01
         * complete_date : 2017-09-05
         * isin : 0
         */

        private String type = "";
        private String money = "";
        private String complete_date = "";
        private int isin = 0;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getComplete_date() {
            return complete_date;
        }

        public void setComplete_date(String complete_date) {
            this.complete_date = complete_date;
        }

        public int getIsin() {
            return isin;
        }

        public void setIsin(int isin) {
            this.isin = isin;
        }
    }
}
