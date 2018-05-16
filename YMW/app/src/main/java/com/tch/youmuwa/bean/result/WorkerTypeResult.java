package com.tch.youmuwa.bean.result;

import org.greenrobot.greendao.annotation.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/8/11.
 */
public class WorkerTypeResult {


    /**
     * msg : 请求成功
     * state : 1
     * data : [{"id":1,"type":1,"name":"油工"},{"id":2,"type":2,"name":"木工"},{"id":3,"type":3,"name":"泥瓦工"},{"id":4,"type":4,"name":"水电工"},{"id":5,"type":5,"name":"安装工"},{"id":6,"type":6,"name":"保洁工"},{"id":7,"type":7,"name":"小工"},{"id":8,"type":8,"name":"其他"}]
     */

    private String msg = "";
    private int state;
    private List<DataBean> data = new ArrayList<DataBean>();

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * type : 1
         * name : 油工
         */

        private int id;
        private int type;
        private String name = "";

        public DataBean(int id, int type, String name) {
            this.id = id;
            this.type = type;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
