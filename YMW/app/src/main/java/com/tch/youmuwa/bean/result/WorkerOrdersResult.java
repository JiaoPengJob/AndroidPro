package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/28.
 */

public class WorkerOrdersResult {

    /**
     * total : 1
     * num : 1
     * list : [{"id":30,"title":"需求点工","require_id":42,"worker_id":5,"number":"2017082517273486753067","front_money":"","total_money":"","state":201,"begin_date":"2017-08-25","end_date":"2017-08-28","state_msg":"施工中","complete_date":"","evaluate_grade":"","evaluate_content":"","evaluate_img_paths":"","evaluate_date":"","work_days":"","create_date":"2017-08-25 17:27:34"}]
     */

    private int total;
    private int num;
    private List<ListBean> list = new ArrayList<ListBean>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 30
         * title : 需求点工
         * require_id : 42
         * worker_id : 5
         * number : 2017082517273486753067
         * front_money :
         * total_money :
         * state : 201
         * begin_date : 2017-08-25
         * end_date : 2017-08-28
         * state_msg : 施工中
         * complete_date :
         * evaluate_grade :
         * evaluate_content :
         * evaluate_img_paths :
         * evaluate_date :
         * work_days :
         * create_date : 2017-08-25 17:27:34
         */

        private int id;
        private String title = "";
        private int require_id;
        private int worker_id;
        private String number = "";
        private String front_money = "";
        private String total_money = "";
        private int state;
        private String begin_date = "";
        private String end_date = "";
        private String state_msg = "";
        private String complete_date = "";
        private String evaluate_grade = "";
        private String evaluate_content = "";
        private String evaluate_img_paths = "";
        private String evaluate_date = "";
        private String work_days = "";
        private String create_date = "";
        private int require_type;
        private String every_price = "";

        public String getEvery_price() {
            return every_price;
        }

        public void setEvery_price(String every_price) {
            this.every_price = every_price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRequire_id() {
            return require_id;
        }

        public void setRequire_id(int require_id) {
            this.require_id = require_id;
        }

        public int getWorker_id() {
            return worker_id;
        }

        public void setWorker_id(int worker_id) {
            this.worker_id = worker_id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getFront_money() {
            return front_money;
        }

        public void setFront_money(String front_money) {
            this.front_money = front_money;
        }

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
            this.total_money = total_money;
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

        public String getState_msg() {
            return state_msg;
        }

        public void setState_msg(String state_msg) {
            this.state_msg = state_msg;
        }

        public String getComplete_date() {
            return complete_date;
        }

        public void setComplete_date(String complete_date) {
            this.complete_date = complete_date;
        }

        public String getEvaluate_grade() {
            return evaluate_grade;
        }

        public void setEvaluate_grade(String evaluate_grade) {
            this.evaluate_grade = evaluate_grade;
        }

        public String getEvaluate_content() {
            return evaluate_content;
        }

        public void setEvaluate_content(String evaluate_content) {
            this.evaluate_content = evaluate_content;
        }

        public String getEvaluate_img_paths() {
            return evaluate_img_paths;
        }

        public void setEvaluate_img_paths(String evaluate_img_paths) {
            this.evaluate_img_paths = evaluate_img_paths;
        }

        public String getEvaluate_date() {
            return evaluate_date;
        }

        public void setEvaluate_date(String evaluate_date) {
            this.evaluate_date = evaluate_date;
        }

        public String getWork_days() {
            return work_days;
        }

        public void setWork_days(String work_days) {
            this.work_days = work_days;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public int getRequire_type() {
            return require_type;
        }

        public void setRequire_type(int require_type) {
            this.require_type = require_type;
        }
    }
}
