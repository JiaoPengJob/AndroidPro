package com.tch.youmuwa.bean.result;

import java.io.Serializable;

/**
 * Created by peng on 2017/8/15.
 */

public class WorkerInfo{

    /**
     * id : 27
     * name : 张胜男
     * worker_type : 1
     * star_level : 0
     * strength : null
     * work_age : 2
     * order_count : 0
     * worker_type_name : 油工
     * head_image_path : http://t3.ychlink.com/storage/users/default.png
     * work_state : 空闲
     * work_area : 北京市海淀区
     * age : 25
     * share : {"title":"张胜男","image":"http://t3.ychlink.com/storage/users/default.png","description":"工种：油工    工龄：2年<br /> 擅长方面：","url":"http://t3.ychlink.com/api/shareworker/27"}
     */

    private int id;
    private String name = "";
    private int worker_type;
    private int star_level;
    private String strength = "";
    private int work_age;
    private int order_count;
    private String worker_type_name = "";
    private String head_image_path = "";
    private String work_state = "";
    private String work_area = "";
    private int age;
    private String mobile = "";
    private ShareBean share;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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

    public int getWorker_type() {
        return worker_type;
    }

    public void setWorker_type(int worker_type) {
        this.worker_type = worker_type;
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

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    public static class ShareBean {
        /**
         * title : 张胜男
         * image : http://t3.ychlink.com/storage/users/default.png
         * description : 工种：油工    工龄：2年<br /> 擅长方面：
         * url : http://t3.ychlink.com/api/shareworker/27
         */

        private String title;
        private String image;
        private String description;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
