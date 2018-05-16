package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop
 * 主页Banner图
 */

public class IndexCarouselResult {

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

        private String banner_cover;
        private String banner_link;
        private String id;

        public String getBanner_cover() {
            return banner_cover;
        }

        public void setBanner_cover(String banner_cover) {
            this.banner_cover = banner_cover;
        }

        public String getBanner_link() {
            return banner_link;
        }

        public void setBanner_link(String banner_link) {
            this.banner_link = banner_link;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
