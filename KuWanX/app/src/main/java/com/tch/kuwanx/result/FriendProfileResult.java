package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/30.
 * 好友信息
 */

public class FriendProfileResult {

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

        private String accountnum;
        private String availablenum;
        private String headpic;
        private String id;
        private String isFriends;
        private String isInterest;
        private String nickname;
        private String phone;
        private String swapCount;
        private String userlevel;
        private List<BabyListSelfBean> babyListSelf;
        private List<BabyListSwapBean> babyListSwap;

        public String getAccountnum() {
            return accountnum;
        }

        public void setAccountnum(String accountnum) {
            this.accountnum = accountnum;
        }

        public String getAvailablenum() {
            return availablenum;
        }

        public void setAvailablenum(String availablenum) {
            this.availablenum = availablenum;
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

        public String getIsFriends() {
            return isFriends;
        }

        public void setIsFriends(String isFriends) {
            this.isFriends = isFriends;
        }

        public String getIsInterest() {
            return isInterest;
        }

        public void setIsInterest(String isInterest) {
            this.isInterest = isInterest;
        }

        public void setSwapCount(String swapCount) {
            this.swapCount = swapCount;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSwapCount() {
            return swapCount;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }

        public List<BabyListSelfBean> getBabyListSelf() {
            return babyListSelf;
        }

        public void setBabyListSelf(List<BabyListSelfBean> babyListSelf) {
            this.babyListSelf = babyListSelf;
        }

        public List<BabyListSwapBean> getBabyListSwap() {
            return babyListSwap;
        }

        public void setBabyListSwap(List<BabyListSwapBean> babyListSwap) {
            this.babyListSwap = babyListSwap;
        }

        public static class BabyListSelfBean {

            private String NAME;
            private String cdid;
            private String headpic;
            private String id;

            public String getNAME() {
                return NAME;
            }

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            public String getCdid() {
                return cdid;
            }

            public void setCdid(String cdid) {
                this.cdid = cdid;
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
        }

        public static class BabyListSwapBean {

            private String NAME;
            private String cdid;
            private String headpic;
            private String id;

            public String getNAME() {
                return NAME;
            }

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            public String getCdid() {
                return cdid;
            }

            public void setCdid(String cdid) {
                this.cdid = cdid;
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
        }
    }
}
