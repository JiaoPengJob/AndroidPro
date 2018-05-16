package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/27.
 * 群组列表
 */

public class GroupListResult {

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

        private String group_id;
        private String group_nickname;
        private List<ImgListBean> imgList;
        private List<MemberListBean> memberList;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGroup_nickname() {
            return group_nickname;
        }

        public void setGroup_nickname(String group_nickname) {
            this.group_nickname = group_nickname;
        }

        public List<ImgListBean> getImgList() {
            return imgList;
        }

        public void setImgList(List<ImgListBean> imgList) {
            this.imgList = imgList;
        }

        public List<MemberListBean> getMemberList() {
            return memberList;
        }

        public void setMemberList(List<MemberListBean> memberList) {
            this.memberList = memberList;
        }

        public static class ImgListBean {

            private String headpic;

            public String getHeadpic() {
                return headpic;
            }

            public void setHeadpic(String headpic) {
                this.headpic = headpic;
            }
        }

        public static class MemberListBean {

            private String headpic;
            private String id;
            private String member_app_user_id;
            private String member_nickname;

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

            public String getMember_app_user_id() {
                return member_app_user_id;
            }

            public void setMember_app_user_id(String member_app_user_id) {
                this.member_app_user_id = member_app_user_id;
            }

            public String getMember_nickname() {
                return member_nickname;
            }

            public void setMember_nickname(String member_nickname) {
                this.member_nickname = member_nickname;
            }
        }
    }
}
