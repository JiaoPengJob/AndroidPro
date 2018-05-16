package com.tch.youmuwa.bean.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/10.
 */

public class ProjectAddsResult {


    /**
     * can_msg_more : 0
     * msg_list : [{"id":5,"addr_province":"上海市","addr_city":"上海市","addr_area":"黄浦区","addr_detail":"对对对"},{"id":6,"addr_province":"省份","addr_city":"城市","addr_area":"区县","addr_detail":"烦烦烦"}]
     */

    private int can_msg_more;
    private List<MsgListBean> msg_list = new ArrayList<MsgListBean>();

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

    public static class MsgListBean implements Serializable{
        /**
         * id : 5
         * addr_province : 上海市
         * addr_city : 上海市
         * addr_area : 黄浦区
         * addr_detail : 对对对
         */

        private int id;
        private String addr_province = "";
        private String addr_city = "";
        private String addr_area = "";
        private String addr_detail = "";
        private int is_default;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddr_province() {
            return addr_province;
        }

        public void setAddr_province(String addr_province) {
            this.addr_province = addr_province;
        }

        public String getAddr_city() {
            return addr_city;
        }

        public void setAddr_city(String addr_city) {
            this.addr_city = addr_city;
        }

        public String getAddr_area() {
            return addr_area;
        }

        public void setAddr_area(String addr_area) {
            this.addr_area = addr_area;
        }

        public String getAddr_detail() {
            return addr_detail;
        }

        public void setAddr_detail(String addr_detail) {
            this.addr_detail = addr_detail;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }
    }
}
