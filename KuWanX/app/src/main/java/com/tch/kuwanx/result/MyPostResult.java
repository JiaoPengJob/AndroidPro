package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/23.
 * 我的帖子/ 我收藏的帖子
 */

public class MyPostResult {

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

        private String cds_address;
        private String headpic;
        private String id;
        private String nickname;
        private String publish_time;
        private String swap_cds;

        public String getCds_address() {
            return cds_address;
        }

        public void setCds_address(String cds_address) {
            this.cds_address = cds_address;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }

        public String getSwap_cds() {
            return swap_cds;
        }

        public void setSwap_cds(String swap_cds) {
            this.swap_cds = swap_cds;
        }
    }
}
