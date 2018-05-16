package com.tch.kuwanx.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiaop on 2018/2/1.
 * 系统消息
 */

public class SysMsgListResult implements Serializable{

    private String msg;
    private String ret;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{

        private String create_time;
        private String id;
        private String img_text;
        private String msg_desc;
        private String msg_title;
        private String msg_type;
        private String vedio_title;
        private String vedio_url;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_text() {
            return img_text;
        }

        public void setImg_text(String img_text) {
            this.img_text = img_text;
        }

        public String getMsg_desc() {
            return msg_desc;
        }

        public void setMsg_desc(String msg_desc) {
            this.msg_desc = msg_desc;
        }

        public String getMsg_title() {
            return msg_title;
        }

        public void setMsg_title(String msg_title) {
            this.msg_title = msg_title;
        }

        public String getMsg_type() {
            return msg_type;
        }

        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }

        public String getVedio_title() {
            return vedio_title;
        }

        public void setVedio_title(String vedio_title) {
            this.vedio_title = vedio_title;
        }

        public String getVedio_url() {
            return vedio_url;
        }

        public void setVedio_url(String vedio_url) {
            this.vedio_url = vedio_url;
        }
    }
}
