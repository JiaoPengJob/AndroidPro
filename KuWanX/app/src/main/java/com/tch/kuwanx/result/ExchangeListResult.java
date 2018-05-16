package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop
 * 换回/不换回
 */

public class ExchangeListResult {

    private String msg;
    private PagingBean paging;
    private String ret;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PagingBean getPaging() {
        return paging;
    }

    public void setPaging(PagingBean paging) {
        this.paging = paging;
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

    public static class PagingBean {

        private String firstResult;
        private String pageCount;
        private String pageNow;
        private String pageSize;
        private String pagecode;
        private PageindexBean pageindex;
        private String rowCount;
        private String startPage;

        public String getFirstResult() {
            return firstResult;
        }

        public void setFirstResult(String firstResult) {
            this.firstResult = firstResult;
        }

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }

        public String getPageNow() {
            return pageNow;
        }

        public void setPageNow(String pageNow) {
            this.pageNow = pageNow;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getPagecode() {
            return pagecode;
        }

        public void setPagecode(String pagecode) {
            this.pagecode = pagecode;
        }

        public PageindexBean getPageindex() {
            return pageindex;
        }

        public void setPageindex(PageindexBean pageindex) {
            this.pageindex = pageindex;
        }

        public String getRowCount() {
            return rowCount;
        }

        public void setRowCount(String rowCount) {
            this.rowCount = rowCount;
        }

        public String getStartPage() {
            return startPage;
        }

        public void setStartPage(String startPage) {
            this.startPage = startPage;
        }

        public static class PageindexBean {

            private String endindex;
            private String startindex;

            public String getEndindex() {
                return endindex;
            }

            public void setEndindex(String endindex) {
                this.endindex = endindex;
            }

            public String getStartindex() {
                return startindex;
            }

            public void setStartindex(String startindex) {
                this.startindex = startindex;
            }
        }
    }

    public static class ResultBean {

        private String cdid;
        private String cds_address;
        private String headpic;
        private String id;
        private String my_cds_description;
        private String nickname;
        private String publish_time;
        private String swap_cds;
        private String swap_deposit;
        private String userselfinfo;
        private String comments_count = "0";
        private String up_count = "0";
        private String isUp = "no";
        private List<ImgListBean> imgList;

        public String getUp_count() {
            return up_count;
        }

        public void setUp_count(String up_count) {
            this.up_count = up_count;
        }

        public String getIsUp() {
            return isUp;
        }

        public void setIsUp(String isUp) {
            this.isUp = isUp;
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

        public String getMy_cds_description() {
            return my_cds_description;
        }

        public void setMy_cds_description(String my_cds_description) {
            this.my_cds_description = my_cds_description;
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

        public String getSwap_deposit() {
            return swap_deposit;
        }

        public void setSwap_deposit(String swap_deposit) {
            this.swap_deposit = swap_deposit;
        }

        public String getUserselfinfo() {
            return userselfinfo;
        }

        public void setUserselfinfo(String userselfinfo) {
            this.userselfinfo = userselfinfo;
        }

        public String getComments_count() {
            return comments_count;
        }

        public void setComments_count(String comments_count) {
            this.comments_count = comments_count;
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
