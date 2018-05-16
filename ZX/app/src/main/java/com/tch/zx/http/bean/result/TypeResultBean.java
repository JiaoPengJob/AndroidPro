package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/14.
 */

public class TypeResultBean {

    private String ret;

    private Result result;

    public TypeResultBean() {
    }

    public TypeResultBean(String ret, Result result) {
        this.ret = ret;
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private List<ResponseObject> responseObject;

        public Result() {
        }

        public Result(List<ResponseObject> responseObject) {
            this.responseObject = responseObject;
        }

        public List<ResponseObject> getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(List<ResponseObject> responseObject) {
            this.responseObject = responseObject;
        }
    }

    public class ResponseObject {
        private int typeId;
        private String typeName;

        public ResponseObject() {
        }

        public ResponseObject(int typeId, String typeName) {
            this.typeId = typeId;
            this.typeName = typeName;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

}
