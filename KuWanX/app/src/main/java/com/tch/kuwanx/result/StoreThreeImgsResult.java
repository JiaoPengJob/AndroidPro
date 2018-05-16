package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/17.
 * 商城首页-三个宣传图(游戏设备,游戏光盘,游戏周边)
 */

public class StoreThreeImgsResult {

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

        private String banner_one;
        private String banner_three;
        private String banner_two;

        public String getBanner_one() {
            return banner_one;
        }

        public void setBanner_one(String banner_one) {
            this.banner_one = banner_one;
        }

        public String getBanner_three() {
            return banner_three;
        }

        public void setBanner_three(String banner_three) {
            this.banner_three = banner_three;
        }

        public String getBanner_two() {
            return banner_two;
        }

        public void setBanner_two(String banner_two) {
            this.banner_two = banner_two;
        }
    }
}
