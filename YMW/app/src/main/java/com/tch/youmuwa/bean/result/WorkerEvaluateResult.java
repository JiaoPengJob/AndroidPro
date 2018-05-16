package com.tch.youmuwa.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/10.
 */

public class WorkerEvaluateResult {


    /**
     * msg_count : 1
     * msg_list : [{"head_image_path":"http://www.youmuwa.com/xxx/雇主头像.png","name":"雇主昵称","evaluate_date":"2017-06-30","title":"橱柜安装","evaluate_content":"评论内容","evaluate_grade":4,"evaluate_img_paths":["http://www.youmuwa.com/评价图片1.png","http://www.youmuwa.com/评价图片2.png","http://www.youmuwa.com/评价图片3.png"]}]
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
         * head_image_path : http://www.youmuwa.com/xxx/雇主头像.png
         * name : 雇主昵称
         * evaluate_date : 2017-06-30
         * title : 橱柜安装
         * evaluate_content : 评论内容
         * evaluate_grade : 4
         * evaluate_img_paths : ["http://www.youmuwa.com/评价图片1.png","http://www.youmuwa.com/评价图片2.png","http://www.youmuwa.com/评价图片3.png"]
         */

        private String head_image_path = "";
        private String name = "";
        private String evaluate_date = "";
        private String title = "";
        private String evaluate_content = "";
        private int evaluate_grade = 0;
        private ArrayList<String> evaluate_img_paths = new ArrayList<String>();

        public String getHead_image_path() {
            return head_image_path;
        }

        public void setHead_image_path(String head_image_path) {
            this.head_image_path = head_image_path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEvaluate_date() {
            return evaluate_date;
        }

        public void setEvaluate_date(String evaluate_date) {
            this.evaluate_date = evaluate_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEvaluate_content() {
            return evaluate_content;
        }

        public void setEvaluate_content(String evaluate_content) {
            this.evaluate_content = evaluate_content;
        }

        public int getEvaluate_grade() {
            return evaluate_grade;
        }

        public void setEvaluate_grade(int evaluate_grade) {
            this.evaluate_grade = evaluate_grade;
        }

        public ArrayList<String> getEvaluate_img_paths() {
            return evaluate_img_paths;
        }

        public void setEvaluate_img_paths(ArrayList<String> evaluate_img_paths) {
            this.evaluate_img_paths = evaluate_img_paths;
        }
    }
}
