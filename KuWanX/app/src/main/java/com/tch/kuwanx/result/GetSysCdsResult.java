package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop
 * 添加物品的搜索物品
 */

public class GetSysCdsResult {

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
        private String headpic;
        private String name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
