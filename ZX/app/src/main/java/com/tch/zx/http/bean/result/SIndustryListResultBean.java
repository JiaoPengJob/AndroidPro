package com.tch.zx.http.bean.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by peng on 2017/7/29.
 */

public class SIndustryListResultBean implements Serializable {


    /**
     * ret : 1
     * result : {"responseObject":[{"industrySTypeName":"理财","industrySTypeid":9},{"industrySTypeName":"基金","industrySTypeid":10}]}
     */

    private String ret;
    private ResultBean result;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        private List<ResponseObjectBean> responseObject;

        public List<ResponseObjectBean> getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(List<ResponseObjectBean> responseObject) {
            this.responseObject = responseObject;
        }

        public static class ResponseObjectBean implements Serializable {
            /**
             * industrySTypeName : 理财
             * industrySTypeid : 9
             */

            private String industrySTypeName;
            private String industrySTypeid;

            public String getIndustrySTypeName() {
                return industrySTypeName;
            }

            public void setIndustrySTypeName(String industrySTypeName) {
                this.industrySTypeName = industrySTypeName;
            }

            public String getIndustrySTypeid() {
                return industrySTypeid;
            }

            public void setIndustrySTypeid(String industrySTypeid) {
                this.industrySTypeid = industrySTypeid;
            }
        }
    }
}
