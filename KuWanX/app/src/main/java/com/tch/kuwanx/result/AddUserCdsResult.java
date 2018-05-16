package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/17.
 * 添加物品
 */

public class AddUserCdsResult {

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
        private String img;
        private String userid;
        private String userselfinfo;
        private String createdate;
        private String id;

        public String getCdid() {
            return cdid;
        }

        public void setCdid(String cdid) {
            this.cdid = cdid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
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
    }
}
