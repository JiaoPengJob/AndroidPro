package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/13.
 * 置换的物品详情
 */

public class SwapCdDetailResult {

    private String msg;
    private ResultBean result;
    private String ret;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public static class ResultBean {

        private String appuser_id;
        private String cdid;
        private String cds_address;
        private String delivery_mode;
        private String headpic;
        private String id;
        private String isCollect;
        private String isUp;
        private String my_cds_id;
        private String nickname;
        private String pay_deposit;
        private String publish_time;
        private String swap_cds;
        private String swap_cycle;
        private String swap_deposit;
        private String swap_type;
        private String userselfinfo;
        private List<ImgListBean> imgList;
        private String up_count;
        private String comments_count;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getUp_count() {
            return up_count;
        }

        public void setUp_count(String up_count) {
            this.up_count = up_count;
        }

        public String getComments_count() {
            return comments_count;
        }

        public void setComments_count(String comments_count) {
            this.comments_count = comments_count;
        }

        public String getCdid() {
            return cdid;
        }

        public void setCdid(String cdid) {
            this.cdid = cdid;
        }

        public String getCds_address() {
            return cds_address;
        }

        public void setCds_address(String cds_address) {
            this.cds_address = cds_address;
        }

        public String getDelivery_mode() {
            return delivery_mode;
        }

        public void setDelivery_mode(String delivery_mode) {
            this.delivery_mode = delivery_mode;
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

        public String getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(String isCollect) {
            this.isCollect = isCollect;
        }

        public String getIsUp() {
            return isUp;
        }

        public void setIsUp(String isUp) {
            this.isUp = isUp;
        }

        public String getMy_cds_id() {
            return my_cds_id;
        }

        public void setMy_cds_id(String my_cds_id) {
            this.my_cds_id = my_cds_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPay_deposit() {
            return pay_deposit;
        }

        public void setPay_deposit(String pay_deposit) {
            this.pay_deposit = pay_deposit;
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

        public String getSwap_cycle() {
            return swap_cycle;
        }

        public void setSwap_cycle(String swap_cycle) {
            this.swap_cycle = swap_cycle;
        }

        public String getSwap_deposit() {
            return swap_deposit;
        }

        public void setSwap_deposit(String swap_deposit) {
            this.swap_deposit = swap_deposit;
        }

        public String getSwap_type() {
            return swap_type;
        }

        public void setSwap_type(String swap_type) {
            this.swap_type = swap_type;
        }

        public String getUserselfinfo() {
            return userselfinfo;
        }

        public void setUserselfinfo(String userselfinfo) {
            this.userselfinfo = userselfinfo;
        }

        public List<ImgListBean> getImgList() {
            return imgList;
        }

        public void setImgList(List<ImgListBean> imgList) {
            this.imgList = imgList;
        }

        public static class ImgListBean {

            private String id;
            private String picpath;
            private String usercdid;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPicpath() {
                return picpath;
            }

            public void setPicpath(String picpath) {
                this.picpath = picpath;
            }

            public String getUsercdid() {
                return usercdid;
            }

            public void setUsercdid(String usercdid) {
                this.usercdid = usercdid;
            }
        }
    }
}
