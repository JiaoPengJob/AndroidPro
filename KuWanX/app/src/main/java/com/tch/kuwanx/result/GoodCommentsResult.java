package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/17.
 * 商品的评价列表
 */

public class GoodCommentsResult {

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

        public void setRowCount(String rowCount) {
            this.rowCount = rowCount;
        }

        public void setStartPage(String startPage) {
            this.startPage = startPage;
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

        public String getStartPage() {
            return startPage;
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

        private String appuser_id;
        private String comm_content;
        private String comm_createtime;
        private String comm_id;
        private String comm_status;
        private String good_id;
        private String nickname;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getComm_content() {
            return comm_content;
        }

        public void setComm_content(String comm_content) {
            this.comm_content = comm_content;
        }

        public String getComm_createtime() {
            return comm_createtime;
        }

        public void setComm_createtime(String comm_createtime) {
            this.comm_createtime = comm_createtime;
        }

        public String getComm_id() {
            return comm_id;
        }

        public void setComm_id(String comm_id) {
            this.comm_id = comm_id;
        }

        public String getComm_status() {
            return comm_status;
        }

        public void setComm_status(String comm_status) {
            this.comm_status = comm_status;
        }

        public String getGood_id() {
            return good_id;
        }

        public void setGood_id(String good_id) {
            this.good_id = good_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
