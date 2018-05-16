package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/15.
 */

public class RequiresListResult {

    /**
     * msg_count : 6
     * can_msg_more : 0
     * msg_list : [{"id":35,"type":2,"title":"哈哈哈哈","address":"上海市黄浦区刚回家不懂如何","worker_types":"泥瓦工","worker_id":1,"require_type":1,"description":"一会回家斤斤计较","state":1,"begin_date":"2017-08-24 00:00:00","end_date":"2017-08-31 00:00:00"},{"id":27,"type":1,"title":"第二单","address":"上海市上海市徐汇区感觉女出差干活","worker_types":"泥瓦工、水电工、其他","worker_id":0,"require_type":2,"description":"更多的","state":1,"begin_date":"2017-08-31 00:00:00","end_date":"2017-08-31 00:00:00","grab_count":0,"max_count":10},{"id":26,"type":1,"title":"第二单","address":"上海市上海市徐汇区感觉女出差干活","worker_types":"泥瓦工、水电工、其他","worker_id":0,"require_type":2,"description":"更多的","state":1,"begin_date":"2017-08-31 00:00:00","end_date":"2017-08-31 00:00:00","grab_count":0,"max_count":10},{"id":24,"type":1,"title":"第一单","address":"上海市上海市黄浦区刚回家不懂如何","worker_types":"泥瓦工、水电工","worker_id":0,"require_type":1,"description":"发个呵呵过分当然","state":1,"begin_date":"2017-08-25 00:00:00","end_date":"2017-08-31 00:00:00","grab_count":0,"max_count":10},{"id":34,"type":2,"title":"第一单","address":"上海市上海市黄浦区刚回家不懂如何","worker_types":"泥瓦工、水电工、油工","worker_id":5,"require_type":1,"description":"发个呵呵过分当然","state":3,"begin_date":"2017-08-25 00:00:00","end_date":"2017-08-31 00:00:00"},{"id":36,"type":2,"title":"第一单","address":"上海市上海市黄浦区刚回家不懂如何","worker_types":"泥瓦工、水电工、油工","worker_id":5,"require_type":1,"description":"发个呵呵过分当然","state":4,"begin_date":"2017-08-25 00:00:00","end_date":"2017-08-31 00:00:00"}]
     */

    private int msg_count;
    private int can_msg_more;
    private List<MsgListBean> msg_list = new ArrayList<MsgListBean>();

    public int getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(int msg_count) {
        this.msg_count = msg_count;
    }

    public int getCan_msg_more() {
        return can_msg_more;
    }

    public void setCan_msg_more(int can_msg_more) {
        this.can_msg_more = can_msg_more;
    }

    public List<MsgListBean> getMsg_list() {
        return msg_list;
    }

    public void setMsg_list(List<MsgListBean> msg_list) {
        this.msg_list = msg_list;
    }

    public static class MsgListBean {
        /**
         * id : 35
         * type : 2
         * title : 哈哈哈哈
         * address : 上海市黄浦区刚回家不懂如何
         * worker_types : 泥瓦工
         * worker_id : 1
         * require_type : 1
         * description : 一会回家斤斤计较
         * state : 1
         * begin_date : 2017-08-24 00:00:00
         * end_date : 2017-08-31 00:00:00
         * grab_count : 0
         * max_count : 10
         */

        private int id;
        private int type;
        private String title = "";
        private String address = "";
        private String worker_types = "";
        private int worker_id;
        private int require_type;
        private String description = "";
        private int state;
        private String begin_date = "";
        private String end_date = "";
        private int grab_count;
        private int max_count;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getWorker_types() {
            return worker_types;
        }

        public void setWorker_types(String worker_types) {
            this.worker_types = worker_types;
        }

        public int getWorker_id() {
            return worker_id;
        }

        public void setWorker_id(int worker_id) {
            this.worker_id = worker_id;
        }

        public int getRequire_type() {
            return require_type;
        }

        public void setRequire_type(int require_type) {
            this.require_type = require_type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public int getGrab_count() {
            return grab_count;
        }

        public void setGrab_count(int grab_count) {
            this.grab_count = grab_count;
        }

        public int getMax_count() {
            return max_count;
        }

        public void setMax_count(int max_count) {
            this.max_count = max_count;
        }
    }
}
