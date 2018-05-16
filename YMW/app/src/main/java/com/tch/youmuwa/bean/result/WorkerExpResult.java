package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/10.
 */

public class WorkerExpResult {


    /**
     * msg_count : 1
     * msg_list : [{"title":"橱柜安装","fromto_date":"2017/06/17\u20142017/06/29","description":"安装家庭定制的大型橱柜，避免安装过程中的磕碰"}]
     * can_msg_more : 0
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
         * title : 橱柜安装
         * fromto_date : 2017/06/17—2017/06/29
         * description : 安装家庭定制的大型橱柜，避免安装过程中的磕碰
         */

        private String title = "";
        private String fromto_date = "";
        private String description = "";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFromto_date() {
            return fromto_date;
        }

        public void setFromto_date(String fromto_date) {
            this.fromto_date = fromto_date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
