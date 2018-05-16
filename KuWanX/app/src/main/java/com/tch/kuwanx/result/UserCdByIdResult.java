package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/17.
 * 用户物品-详情
 */

public class UserCdByIdResult {

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

        private String cdid;
        private String createdate;
        private String id;
        private String userid;
        private String userselfinfo;
        private List<ImgListBean> imgList;

        public String getCdid() {
            return cdid;
        }

        public void setCdid(String cdid) {
            this.cdid = cdid;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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

            private String cdid;
            private String id;
            private String picpath;

            public String getCdid() {
                return cdid;
            }

            public void setCdid(String cdid) {
                this.cdid = cdid;
            }

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
        }
    }
}
