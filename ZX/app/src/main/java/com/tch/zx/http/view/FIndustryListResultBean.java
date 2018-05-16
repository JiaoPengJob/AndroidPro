package com.tch.zx.http.view;

import java.io.Serializable;
import java.util.List;

/**
 * Created by peng on 2017/7/29.
 */

public class FIndustryListResultBean implements Serializable{


    /**
     * ret : 1
     * result : {"responseObject":[{"industryFTypeName":"金融","industryFTypeid":11},{"industryFTypeName":"餐饮","industryFTypeid":12},{"industryFTypeName":"建筑","industryFTypeid":13},{"industryFTypeName":"娱乐","industryFTypeid":14}]}
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

    public static class ResultBean implements Serializable{
        private List<ResponseObjectBean> responseObject;

        public List<ResponseObjectBean> getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(List<ResponseObjectBean> responseObject) {
            this.responseObject = responseObject;
        }

        public static class ResponseObjectBean implements Serializable {
            /**
             * industryFTypeName : 金融
             * industryFTypeid : 11
             */

            private String industryFTypeName;
            private String industryFTypeid;

            public String getIndustryFTypeName() {
                return industryFTypeName;
            }

            public void setIndustryFTypeName(String industryFTypeName) {
                this.industryFTypeName = industryFTypeName;
            }

            public String getIndustryFTypeid() {
                return industryFTypeid;
            }

            public void setIndustryFTypeid(String industryFTypeid) {
                this.industryFTypeid = industryFTypeid;
            }
        }
    }
}
