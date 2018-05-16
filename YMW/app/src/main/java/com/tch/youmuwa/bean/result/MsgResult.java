package com.tch.youmuwa.bean.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/30.
 */

public class MsgResult {

    private List<MsgListBean> msg_list = new ArrayList<MsgListBean>();

    public List<MsgListBean> getMsg_list() {
        return msg_list;
    }

    public void setMsg_list(List<MsgListBean> msg_list) {
        this.msg_list = msg_list;
    }

    public static class MsgListBean implements Serializable{
        /**
         * id : 2
         * content : cdcsddca
         * is_read : 0
         * create_date : 2017-08-29 18:39:01
         */

        private int id;
        private String content;
        private int is_read;
        private String create_date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }
    }
}
