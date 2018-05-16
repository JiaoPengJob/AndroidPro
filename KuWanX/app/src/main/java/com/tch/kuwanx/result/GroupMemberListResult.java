package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/31.
 * 群成员列表
 */

public class GroupMemberListResult {

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

        private String app_user_id;
        private String create_date;
        private String group_nickname;
        private String id;
        private String isGroupMain;
        private List<MemberListBean> memberList;

        public String getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(String app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getGroup_nickname() {
            return group_nickname;
        }

        public void setGroup_nickname(String group_nickname) {
            this.group_nickname = group_nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsGroupMain() {
            return isGroupMain;
        }

        public void setIsGroupMain(String isGroupMain) {
            this.isGroupMain = isGroupMain;
        }

        public List<MemberListBean> getMemberList() {
            return memberList;
        }

        public void setMemberList(List<MemberListBean> memberList) {
            this.memberList = memberList;
        }

        public static class MemberListBean {

            private String headpic;
            private String id;
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

            public String getMember_nickname() {
                return member_nickname;
            }

            public void setMember_nickname(String member_nickname) {
                this.member_nickname = member_nickname;
            }
        }
    }
}
