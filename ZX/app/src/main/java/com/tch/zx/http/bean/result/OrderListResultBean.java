package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/15.
 */

public class OrderListResultBean {


    /**
     * ret : 1
     * result : {"pageNow":2,"responseObject":[{"appUserName":"测试账号001","byName":"","createtime":"2017-07-14 16:36:24.0","id":"1","livePepoleNum":"1000","liveStatus":"2","liveTime":"2017-06-21 18:50:17","name":"直播测试002","orderClassType":"1","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/0f96ed0b053b49499a3a6fcfb6a166c1.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/ed3de99e1dc848c89f11092e7ae5a60e.jpg","position":"市场运营部经理","price":"100.10","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"500"},{"appUserName":"测试账号001","byName":"","createtime":"2017-07-14 13:45:30.0","id":"23","livePepoleNum":"","liveStatus":"","liveTime":"","name":"测试课程","orderClassType":"2","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg","position":"市场运营部经理","price":"80.52","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"842"},{"appUserName":"测试账号001","byName":"","createtime":"2017-07-13 19:44:39.0","id":"2","livePepoleNum":"1452","liveStatus":"2","liveTime":"2017-06-07 00:00:00","name":"测试1","orderClassType":"1","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg","position":"市场运营部经理","price":"444.50","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"1000"},{"appUserName":"测试账号001","byName":"数学天地","createtime":"2017-07-13 16:36:29.0","id":"4","livePepoleNum":"","liveStatus":"","liveTime":"","name":"数学","orderClassType":"3","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/bd47875a18cb4ed89338e2868bbd0a6e.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/dbfd8b0da9a44a0a927a9be31ab5c7be.jpg","position":"市场运营部经理","price":"100.03","updateDate":"2017-06-27 16:45:05","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":""},{"appUserName":"测试账号001","byName":"","createtime":"2017-07-12 16:36:33.0","id":"22","livePepoleNum":"","liveStatus":"","liveTime":"","name":"测试课程","orderClassType":"2","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg","position":"市场运营部经理","price":"50.52","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"1560"},{"appUserName":"测试账号001","byName":"马克思","createtime":"2017-07-11 16:46:09.0","id":"12","livePepoleNum":"","liveStatus":"","liveTime":"","name":"政治","orderClassType":"3","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/c9de7d28948e4c86b569f25934c98e7d.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/19edfe58b1dc40efb0267c2aa19af0bb.jpg","position":"市场运营部经理","price":"120.50","updateDate":"2017-06-27 16:47:04","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":""}]}
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
        /**
         * pageNow : 2
         * responseObject : [{"appUserName":"测试账号001","byName":"","createtime":"2017-07-14 16:36:24.0","id":"1","livePepoleNum":"1000","liveStatus":"2","liveTime":"2017-06-21 18:50:17","name":"直播测试002","orderClassType":"1","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/0f96ed0b053b49499a3a6fcfb6a166c1.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/ed3de99e1dc848c89f11092e7ae5a60e.jpg","position":"市场运营部经理","price":"100.10","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"500"},{"appUserName":"测试账号001","byName":"","createtime":"2017-07-14 13:45:30.0","id":"23","livePepoleNum":"","liveStatus":"","liveTime":"","name":"测试课程","orderClassType":"2","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg","position":"市场运营部经理","price":"80.52","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"842"},{"appUserName":"测试账号001","byName":"","createtime":"2017-07-13 19:44:39.0","id":"2","livePepoleNum":"1452","liveStatus":"2","liveTime":"2017-06-07 00:00:00","name":"测试1","orderClassType":"1","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg","position":"市场运营部经理","price":"444.50","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"1000"},{"appUserName":"测试账号001","byName":"数学天地","createtime":"2017-07-13 16:36:29.0","id":"4","livePepoleNum":"","liveStatus":"","liveTime":"","name":"数学","orderClassType":"3","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/bd47875a18cb4ed89338e2868bbd0a6e.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/dbfd8b0da9a44a0a927a9be31ab5c7be.jpg","position":"市场运营部经理","price":"100.03","updateDate":"2017-06-27 16:45:05","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":""},{"appUserName":"测试账号001","byName":"","createtime":"2017-07-12 16:36:33.0","id":"22","livePepoleNum":"","liveStatus":"","liveTime":"","name":"测试课程","orderClassType":"2","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg","position":"市场运营部经理","price":"50.52","updateDate":"","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":"1560"},{"appUserName":"测试账号001","byName":"马克思","createtime":"2017-07-11 16:46:09.0","id":"12","livePepoleNum":"","liveStatus":"","liveTime":"","name":"政治","orderClassType":"3","payUserId":"d8f08b34f2b7490481097328bf3a66fb","picMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/c9de7d28948e4c86b569f25934c98e7d.jpg","picMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/19edfe58b1dc40efb0267c2aa19af0bb.jpg","position":"市场运营部经理","price":"120.50","updateDate":"2017-06-27 16:47:04","userPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","viewNum":""}]
         */

        private int pageNow;
        private List<ResponseObjectBean> responseObject;

        public int getPageNow() {
            return pageNow;
        }

        public void setPageNow(int pageNow) {
            this.pageNow = pageNow;
        }

        public List<ResponseObjectBean> getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(List<ResponseObjectBean> responseObject) {
            this.responseObject = responseObject;
        }

        public static class ResponseObjectBean {
            /**
             * appUserName : 测试账号001
             * byName :
             * createtime : 2017-07-14 16:36:24.0
             * id : 1
             * livePepoleNum : 1000
             * liveStatus : 2
             * liveTime : 2017-06-21 18:50:17
             * name : 直播测试002
             * orderClassType : 1
             * payUserId : d8f08b34f2b7490481097328bf3a66fb
             * picMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/0f96ed0b053b49499a3a6fcfb6a166c1.jpg
             * picMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/ed3de99e1dc848c89f11092e7ae5a60e.jpg
             * position : 市场运营部经理
             * price : 100.10
             * updateDate :
             * userPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * viewNum : 500
             */

            private String appUserName;
            private String byName;
            private String createtime;
            private String id;
            private String livePepoleNum;
            private String liveStatus;
            private String liveTime;
            private String name;
            private String orderClassType;
            private String payUserId;
            private String picMax;
            private String picMin;
            private String position;
            private String price;
            private String updateDate;
            private String userPic;
            private String viewNum;

            public String getAppUserName() {
                return appUserName;
            }

            public void setAppUserName(String appUserName) {
                this.appUserName = appUserName;
            }

            public String getByName() {
                return byName;
            }

            public void setByName(String byName) {
                this.byName = byName;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLivePepoleNum() {
                return livePepoleNum;
            }

            public void setLivePepoleNum(String livePepoleNum) {
                this.livePepoleNum = livePepoleNum;
            }

            public String getLiveStatus() {
                return liveStatus;
            }

            public void setLiveStatus(String liveStatus) {
                this.liveStatus = liveStatus;
            }

            public String getLiveTime() {
                return liveTime;
            }

            public void setLiveTime(String liveTime) {
                this.liveTime = liveTime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOrderClassType() {
                return orderClassType;
            }

            public void setOrderClassType(String orderClassType) {
                this.orderClassType = orderClassType;
            }

            public String getPayUserId() {
                return payUserId;
            }

            public void setPayUserId(String payUserId) {
                this.payUserId = payUserId;
            }

            public String getPicMax() {
                return picMax;
            }

            public void setPicMax(String picMax) {
                this.picMax = picMax;
            }

            public String getPicMin() {
                return picMin;
            }

            public void setPicMin(String picMin) {
                this.picMin = picMin;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(String updateDate) {
                this.updateDate = updateDate;
            }

            public String getUserPic() {
                return userPic;
            }

            public void setUserPic(String userPic) {
                this.userPic = userPic;
            }

            public String getViewNum() {
                return viewNum;
            }

            public void setViewNum(String viewNum) {
                this.viewNum = viewNum;
            }
        }
    }
}
