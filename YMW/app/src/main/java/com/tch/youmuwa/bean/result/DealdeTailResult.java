package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/17.
 */

public class DealdeTailResult {


    /**
     * total : 3
     * num : 3
     * list : [{"id":32,"worker_id":36,"order_sn":"2017090412014038881540","workery_name":"焦鹏","behavior":"1","money":"0.01","remark":"辞退工资","create_date":"2017-09-05 11:20:53"},{"id":33,"worker_id":36,"order_sn":"2017090416132859267439","workery_name":"焦鹏","behavior":"1","money":"0.01","remark":"项目工资","create_date":"2017-09-05 11:23:19"},{"id":34,"worker_id":36,"order_sn":"2017090513272981895713","workery_name":"焦鹏","behavior":"1","money":"0.01","remark":"项目工资","create_date":"2017-09-05 13:38:58"}]
     */

    private int total = 0;
    private int num = 0;
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
         * id : 32
         * worker_id : 36
         * order_sn : 2017090412014038881540
         * workery_name : 焦鹏
         * behavior : 1
         * money : 0.01
         * remark : 辞退工资
         * create_date : 2017-09-05 11:20:53
         */

        private int id;
        private int worker_id;
        private String order_sn = "";
        private String workery_name = "";
        private String behavior = "";
        private String money = "";
        private String remark = "";
        private String create_date = "";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getWorker_id() {
            return worker_id;
        }

        public void setWorker_id(int worker_id) {
            this.worker_id = worker_id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getWorkery_name() {
            return workery_name;
        }

        public void setWorkery_name(String workery_name) {
            this.workery_name = workery_name;
        }

        public String getBehavior() {
            return behavior;
        }

        public void setBehavior(String behavior) {
            this.behavior = behavior;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }
    }
}
