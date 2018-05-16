package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/9.
 */

public class SearchWorkerResult {


    /**
     * msg_count : 4
     * can_msg_more : 0
     * msg_list : [{"id":26,"name":"张胜男","star_level":null,"strength":null,"work_age":2,"order_count":null,"worker_type_name":"油工","head_image_path":"http://t3.ychlink.com/storage/users/default.png","work_state":"空闲","work_area":"北京市海淀区","age":25},{"id":27,"name":"张胜男","star_level":null,"strength":null,"work_age":2,"order_count":null,"worker_type_name":"油工","head_image_path":"http://t3.ychlink.com/storage/users/default.png","work_state":"空闲","work_area":"北京市海淀区","age":25},{"id":10,"name":null,"star_level":null,"strength":null,"work_age":0,"order_count":null,"worker_type_name":"油工","head_image_path":"http://t3.ychlink.com/storage/users/default.png","work_state":"空闲","work_area":"北京","age":0},{"id":5,"name":"唐志超","star_level":null,"strength":"中华人民共和国中华人民共和国","work_age":1,"order_count":null,"worker_type_name":"油工","head_image_path":"http://t3.ychlink.com/storage/users/5/employer15028632565993df987a70b.png","work_state":"空闲","work_area":"北京市昌平区","age":26}]
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
         * id : 26
         * name : 张胜男
         * star_level : null
         * strength : null
         * work_age : 2
         * order_count : null
         * worker_type_name : 油工
         * head_image_path : http://t3.ychlink.com/storage/users/default.png
         * work_state : 空闲
         * work_area : 北京市海淀区
         * age : 25
         */

        private int id;
        private String name = "";
        private int star_level;
        private String strength = "";
        private int work_age;
        private int order_count;
        private String worker_type_name = "";
        private String head_image_path = "";
        private String work_state = "";
        private String work_area = "";
        private int age;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStar_level() {
            return star_level;
        }

        public void setStar_level(int star_level) {
            this.star_level = star_level;
        }

        public String getStrength() {
            return strength;
        }

        public void setStrength(String strength) {
            this.strength = strength;
        }

        public int getWork_age() {
            return work_age;
        }

        public void setWork_age(int work_age) {
            this.work_age = work_age;
        }

        public int getOrder_count() {
            return order_count;
        }

        public void setOrder_count(int order_count) {
            this.order_count = order_count;
        }

        public String getWorker_type_name() {
            return worker_type_name;
        }

        public void setWorker_type_name(String worker_type_name) {
            this.worker_type_name = worker_type_name;
        }

        public String getHead_image_path() {
            return head_image_path;
        }

        public void setHead_image_path(String head_image_path) {
            this.head_image_path = head_image_path;
        }

        public String getWork_state() {
            return work_state;
        }

        public void setWork_state(String work_state) {
            this.work_state = work_state;
        }

        public String getWork_area() {
            return work_area;
        }

        public void setWork_area(String work_area) {
            this.work_area = work_area;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
