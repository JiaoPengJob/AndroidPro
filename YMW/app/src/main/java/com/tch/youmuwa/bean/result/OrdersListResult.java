package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/22.
 */

public class OrdersListResult {


    /**
     * msg_list : [{"number":"2017090513475210009766","require_type":2,"title":"哈哈哈哈哈哈哈哈","state":120,"begin_date":"2017-09-20","end_date":"2017-09-22","worker_id":36,"worker_types":[1],"worker_name":"焦鹏","state_info":"已取消","price":null},{"number":"2017090513273567454305","require_type":1,"title":"呵呵哈哈哈哈","state":221,"begin_date":"2017-09-22","end_date":"2017-09-24","worker_id":36,"worker_types":[1],"worker_name":"焦鹏","state_info":"待付款","price":"0.01"},{"number":"2017090513272981895713","require_type":1,"title":"呵呵","state":205,"begin_date":"2017-09-21","end_date":"2017-09-22","worker_id":36,"worker_types":[1],"worker_name":"焦鹏","state_info":"完成已评价","price":"0.01"},{"number":"2017090416132859267439","require_type":1,"title":"测试4","state":205,"begin_date":"2017-09-21","end_date":"2017-09-22","worker_id":36,"worker_types":[1],"worker_name":"焦鹏","state_info":"完成已评价","price":"0.01"},{"number":"2017090412014038881540","require_type":1,"title":"指定工人下单","state":222,"begin_date":"2017-09-06","end_date":"2017-09-08","worker_id":36,"worker_types":[1],"worker_name":"焦鹏","state_info":"已辞退","price":"0.01"}]
     * can_msg_more : 0
     * msg_count : 5
     */

    private int can_msg_more;
    private int msg_count;
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
         * number : 2017090513475210009766
         * require_type : 2
         * title : 哈哈哈哈哈哈哈哈
         * state : 120
         * begin_date : 2017-09-20
         * end_date : 2017-09-22
         * worker_id : 36
         * worker_types : [1]
         * worker_name : 焦鹏
         * state_info : 已取消
         * price : null
         */

        private String number = "";
        private int require_type;
        private String title = "";
        private int state;
        private String begin_date = "";
        private String end_date = "";
        private int worker_id;
        private String worker_name = "";
        private String state_info = "";
        private String price = "";
        private List<Integer> worker_types = new ArrayList<Integer>();

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getRequire_type() {
            return require_type;
        }

        public void setRequire_type(int require_type) {
            this.require_type = require_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getBegin_date() {
            return begin_date;
        }

        public void setBegin_date(String begin_date) {
            this.begin_date = begin_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public int getWorker_id() {
            return worker_id;
        }

        public void setWorker_id(int worker_id) {
            this.worker_id = worker_id;
        }

        public String getWorker_name() {
            return worker_name;
        }

        public void setWorker_name(String worker_name) {
            this.worker_name = worker_name;
        }

        public String getState_info() {
            return state_info;
        }

        public void setState_info(String state_info) {
            this.state_info = state_info;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<Integer> getWorker_types() {
            return worker_types;
        }

        public void setWorker_types(List<Integer> worker_types) {
            this.worker_types = worker_types;
        }
    }
}
