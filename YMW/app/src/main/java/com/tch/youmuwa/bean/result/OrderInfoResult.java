package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/8/22.
 */

public class OrderInfoResult {

    /**
     * number : 2017090416132859267439
     * work_days : null
     * state : 201
     * worker_id : 36
     * front_money : null
     * total_money : null
     * complete_date : null
     * type : 2
     * title : 测试4
     * address : 北京市朝阳区望京SOHO
     * worker_types : 油工
     * require_type : 1
     * description : 黄呵呵
     * create_date : 2017-09-04 16:13:28
     * price : 0.01
     * goto_price : 0.00
     * contacts : 哈哈
     * contact_number : 18695236955
     * begin_date : 2017-09-21
     * end_date : 2017-09-22
     * push_date : 2017-09-04 16:03:18
     * state_info : 施工中
     * worker_info : {"id":36,"name":"焦鹏","star_level":0,"strength":null,"work_age":1,"mobile":"13520518525","worker_type_name":"油工","head_image_path":"http://t3.ychlink.com/storage/users/default.png","age":22}
     */

    private String number = "";
    private String work_days = "";
    private int state;
    private int worker_id;
    private String front_money = "";
    private String total_money = "";
    private String complete_date = "";
    private int type;
    private String title = "";
    private String address = "";
    private String worker_types = "";
    private int require_type;
    private String description = "";
    private String create_date = "";
    private String price = "";
    private String goto_price = "";
    private String contacts = "";
    private String contact_number = "";
    private String begin_date = "";
    private String end_date = "";
    private String push_date = "";
    private String state_info = "";
    private WorkerInfoBean worker_info;
    private String dismiss_reason = "";
    private String dismiss_money = "";

    public String getDismiss_money() {
        return dismiss_money;
    }

    public void setDismiss_money(String dismiss_money) {
        this.dismiss_money = dismiss_money;
    }

    public String getDismiss_reason() {
        return dismiss_reason;
    }

    public void setDismiss_reason(String dismiss_reason) {
        this.dismiss_reason = dismiss_reason;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWork_days() {
        return work_days;
    }

    public void setWork_days(String work_days) {
        this.work_days = work_days;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
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

    public String getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(String complete_date) {
        this.complete_date = complete_date;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoto_price() {
        return goto_price;
    }

    public void setGoto_price(String goto_price) {
        this.goto_price = goto_price;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
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

    public String getPush_date() {
        return push_date;
    }

    public void setPush_date(String push_date) {
        this.push_date = push_date;
    }

    public String getState_info() {
        return state_info;
    }

    public void setState_info(String state_info) {
        this.state_info = state_info;
    }

    public WorkerInfoBean getWorker_info() {
        return worker_info;
    }

    public void setWorker_info(WorkerInfoBean worker_info) {
        this.worker_info = worker_info;
    }

    public static class WorkerInfoBean {
        /**
         * id : 36
         * name : 焦鹏
         * star_level : 0
         * strength : null
         * work_age : 1
         * mobile : 13520518525
         * worker_type_name : 油工
         * head_image_path : http://t3.ychlink.com/storage/users/default.png
         * age : 22
         */

        private int id;
        private String name = "";
        private int star_level;
        private String strength = "";
        private int work_age;
        private String mobile = "";
        private String worker_type_name = "";
        private String head_image_path = "";
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
