package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/2/6.
 * 省
 */

public class ProvinceResult {

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

    public static class ResultBean {

        private String dname;
        private String id;
        private String parentid;
        private String pinyin;
        private String sorder;
        private String spinyin;
        private String value;

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getSorder() {
            return sorder;
        }

        public void setSorder(String sorder) {
            this.sorder = sorder;
        }

        public String getSpinyin() {
            return spinyin;
        }

        public void setSpinyin(String spinyin) {
            this.spinyin = spinyin;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
