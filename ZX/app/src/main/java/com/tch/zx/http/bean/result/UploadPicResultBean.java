package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/29.
 */

public class UploadPicResultBean {


    /**
     * ret : 1
     * result : {"responseObject":["http://115.28.95.41:8080/uploadfile/zhixian/201707/bf89c2562c244c51a986f66d769c4424.jpg","http://115.28.95.41:8080/uploadfile/zhixian/201707/037e65629d32482f9c2688f2fb2e694e.jpg","http://115.28.95.41:8080/uploadfile/zhixian/201707/9e1555dd96014515a562f0acaf568e33.jpg"]}
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

    public static class ResultBean {
        private List<String> responseObject;

        public List<String> getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(List<String> responseObject) {
            this.responseObject = responseObject;
        }
    }
}
