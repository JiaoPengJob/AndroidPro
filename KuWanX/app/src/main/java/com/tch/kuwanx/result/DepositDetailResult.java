package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/2/27.
 * 押金明细
 */

public class DepositDetailResult {

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

        private String appuser_id;
        private String id;
        private String transaction_amt;
        private String transaction_mode;
        private String transaction_source;
        private String transaction_time;

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTransaction_amt() {
            return transaction_amt;
        }

        public void setTransaction_amt(String transaction_amt) {
            this.transaction_amt = transaction_amt;
        }

        public String getTransaction_mode() {
            return transaction_mode;
        }

        public void setTransaction_mode(String transaction_mode) {
            this.transaction_mode = transaction_mode;
        }

        public String getTransaction_source() {
            return transaction_source;
        }

        public void setTransaction_source(String transaction_source) {
            this.transaction_source = transaction_source;
        }

        public String getTransaction_time() {
            return transaction_time;
        }

        public void setTransaction_time(String transaction_time) {
            this.transaction_time = transaction_time;
        }
    }
}
