package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/15.
 */

public class WorkerSofRequireResult {


    /**
     * msg_count : 2
     * msg_list : [{"id":5,"name":"\"李师傅\"","star_level":3,"strength":"\"擅长xxx方面\"","work_age":"\"2-5年\"","age":24,"order_count":15,"worker_type_name":"\"木工\"","work_area":"\"北京市昌平区\"","head_image_path":"http://www.youmuwa.com/storage/users/default.png"},{"id":4,"name":"\"陈师傅\"","star_level":3,"strength":"\"擅长xxx方面\"","work_age":"\"2-5年\"","age":23,"order_count":15,"worker_type_name":"\"木工\"","work_area":"\"北京市昌平区\"","head_image_path":"http://www.youmuwa.com/storage/users/default.png"}]
     */

    private int msg_count;
    private List<MsgListBean> msg_list = new ArrayList<MsgListBean>();

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
         * id : 5
         * name : "李师傅"
         * star_level : 3
         * strength : "擅长xxx方面"
         * work_age : "2-5年"
         * age : 24
         * order_count : 15
         * worker_type_name : "木工"
         * work_area : "北京市昌平区"
         * head_image_path : http://www.youmuwa.com/storage/users/default.png
         */

        private int id;
        private String name = "";
        private int star_level;
        private String strength = "";
        private String work_age = "";
        private int age;
        private int order_count;
        private String worker_type_name = "";
        private String work_area = "";
        private String head_image_path = "";

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

        public String getWork_age() {
            return work_age;
        }

        public void setWork_age(String work_age) {
            this.work_age = work_age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
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

        public String getWork_area() {
            return work_area;
        }

        public void setWork_area(String work_area) {
            this.work_area = work_area;
        }

        public String getHead_image_path() {
            return head_image_path;
        }

        public void setHead_image_path(String head_image_path) {
            this.head_image_path = head_image_path;
        }
    }
}
