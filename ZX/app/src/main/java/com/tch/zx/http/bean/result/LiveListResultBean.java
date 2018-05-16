package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/21.
 */

public class LiveListResultBean {


    /**
     * ret : 1
     * result : {"pageNow":2,"responseObject":[{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":1,"liveMoney":100.1,"liveName":"直播测试002","livePepoleNum":"1000","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/0f96ed0b053b49499a3a6fcfb6a166c1.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/ed3de99e1dc848c89f11092e7ae5a60e.jpg","liveStatus":"2","liveTime":"2017-06-21 18:50:17.0","liveViewNum":"500","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":2,"liveMoney":444.5,"liveName":"测试1","livePepoleNum":"1452","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveViewNum":"1000","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":5,"liveMoney":333.05,"liveName":"市场营销","livePepoleNum":"56","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e1fa6e339b9e4fa8b246e05781923da6.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/2280a3b716604022a08fdebd5aa2afa6.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveViewNum":"50","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":7,"liveMoney":22.56,"liveName":"管理","livePepoleNum":"4251","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/110748a85df44b04b2f4be2caa720ac9.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/2b16cf8f310149c69cd05b2c52a6e308.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveViewNum":"121","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":10,"liveMoney":44.1,"liveName":"测试","livePepoleNum":"123","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/82d2dd3f864a4f428ab2db503137a72d.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/cdf30e662a144c8a88b77793ec06ab2b.jpg","liveStatus":"2","liveTime":"2017-06-24 00:00:00.0","liveViewNum":"100","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":4,"liveMoney":11.1,"liveName":"三国","livePepoleNum":"1424","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/90d0f841ac414c9ba39d0915d6c93048.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/243d19bc2f5b42b991dfd99a64af986b.jpg","liveStatus":"1","liveTime":"2017-07-22 14:53:21.0","liveViewNum":"500","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":6,"liveMoney":22.01,"liveName":"HTTP","livePepoleNum":"20","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/31d204419d6f49beae7a89997a3abbe9.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e3d8ff61ee614f228e3faa91fb4bfbba.jpg","liveStatus":"1","liveTime":"2017-07-23 14:53:26.0","liveViewNum":"456","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":11,"liveMoney":22.03,"liveName":"网站","livePepoleNum":"785","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/7e5281781e70479096a0330f7451716f.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/5bc1a237b0e448e09c7d6662564e7505.jpg","liveStatus":"1","liveTime":"2017-07-24 14:53:31.0","liveViewNum":"4534","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":12,"liveMoney":33.15,"liveName":"水浒","livePepoleNum":"425","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/7e1ec169f9154de48cf0f141df8fde33.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1bdfa54ab4d949b8a414464a9d2c7751.jpg","liveStatus":"1","liveTime":"2017-07-25 14:53:41.0","liveViewNum":"836","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":8,"liveMoney":66.85,"liveName":"三国","livePepoleNum":"854","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/24984d807cfc42198c934afc7ae66476.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/6f99714966ce43c891de573db2cbc437.jpg","liveStatus":"3","liveTime":"2017-06-24 00:00:00.0","liveViewNum":"4564","position":"市场运营部经理"}]}
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
         * responseObject : [{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":1,"liveMoney":100.1,"liveName":"直播测试002","livePepoleNum":"1000","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/0f96ed0b053b49499a3a6fcfb6a166c1.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/ed3de99e1dc848c89f11092e7ae5a60e.jpg","liveStatus":"2","liveTime":"2017-06-21 18:50:17.0","liveViewNum":"500","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":2,"liveMoney":444.5,"liveName":"测试1","livePepoleNum":"1452","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveViewNum":"1000","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":5,"liveMoney":333.05,"liveName":"市场营销","livePepoleNum":"56","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e1fa6e339b9e4fa8b246e05781923da6.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/2280a3b716604022a08fdebd5aa2afa6.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveViewNum":"50","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":7,"liveMoney":22.56,"liveName":"管理","livePepoleNum":"4251","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/110748a85df44b04b2f4be2caa720ac9.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/2b16cf8f310149c69cd05b2c52a6e308.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveViewNum":"121","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":10,"liveMoney":44.1,"liveName":"测试","livePepoleNum":"123","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/82d2dd3f864a4f428ab2db503137a72d.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/cdf30e662a144c8a88b77793ec06ab2b.jpg","liveStatus":"2","liveTime":"2017-06-24 00:00:00.0","liveViewNum":"100","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":4,"liveMoney":11.1,"liveName":"三国","livePepoleNum":"1424","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/90d0f841ac414c9ba39d0915d6c93048.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/243d19bc2f5b42b991dfd99a64af986b.jpg","liveStatus":"1","liveTime":"2017-07-22 14:53:21.0","liveViewNum":"500","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":6,"liveMoney":22.01,"liveName":"HTTP","livePepoleNum":"20","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/31d204419d6f49beae7a89997a3abbe9.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e3d8ff61ee614f228e3faa91fb4bfbba.jpg","liveStatus":"1","liveTime":"2017-07-23 14:53:26.0","liveViewNum":"456","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":11,"liveMoney":22.03,"liveName":"网站","livePepoleNum":"785","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/7e5281781e70479096a0330f7451716f.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/5bc1a237b0e448e09c7d6662564e7505.jpg","liveStatus":"1","liveTime":"2017-07-24 14:53:31.0","liveViewNum":"4534","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":12,"liveMoney":33.15,"liveName":"水浒","livePepoleNum":"425","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/7e1ec169f9154de48cf0f141df8fde33.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1bdfa54ab4d949b8a414464a9d2c7751.jpg","liveStatus":"1","liveTime":"2017-07-25 14:53:41.0","liveViewNum":"836","position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","liveId":8,"liveMoney":66.85,"liveName":"三国","livePepoleNum":"854","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/24984d807cfc42198c934afc7ae66476.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/6f99714966ce43c891de573db2cbc437.jpg","liveStatus":"3","liveTime":"2017-06-24 00:00:00.0","liveViewNum":"4564","position":"市场运营部经理"}]
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
             * appUserId : d8f08b34f2b7490481097328bf3a66fb
             * appUserName : 测试账号001
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * liveId : 1
             * liveMoney : 100.1
             * liveName : 直播测试002
             * livePepoleNum : 1000
             * livePicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/0f96ed0b053b49499a3a6fcfb6a166c1.jpg
             * livePicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/ed3de99e1dc848c89f11092e7ae5a60e.jpg
             * liveStatus : 2
             * liveTime : 2017-06-21 18:50:17.0
             * liveViewNum : 500
             * position : 市场运营部经理
             */

            private String appUserId;
            private String appUserName;
            private String appUserPic;
            private int liveId;
            private double liveMoney;
            private String liveName;
            private String livePepoleNum;
            private String livePicMax;
            private String livePicMin;
            private String liveStatus;
            private String liveTime;
            private String liveViewNum;
            private String position;

            public String getAppUserId() {
                return appUserId;
            }

            public void setAppUserId(String appUserId) {
                this.appUserId = appUserId;
            }

            public String getAppUserName() {
                return appUserName;
            }

            public void setAppUserName(String appUserName) {
                this.appUserName = appUserName;
            }

            public String getAppUserPic() {
                return appUserPic;
            }

            public void setAppUserPic(String appUserPic) {
                this.appUserPic = appUserPic;
            }

            public int getLiveId() {
                return liveId;
            }

            public void setLiveId(int liveId) {
                this.liveId = liveId;
            }

            public double getLiveMoney() {
                return liveMoney;
            }

            public void setLiveMoney(double liveMoney) {
                this.liveMoney = liveMoney;
            }

            public String getLiveName() {
                return liveName;
            }

            public void setLiveName(String liveName) {
                this.liveName = liveName;
            }

            public String getLivePepoleNum() {
                return livePepoleNum;
            }

            public void setLivePepoleNum(String livePepoleNum) {
                this.livePepoleNum = livePepoleNum;
            }

            public String getLivePicMax() {
                return livePicMax;
            }

            public void setLivePicMax(String livePicMax) {
                this.livePicMax = livePicMax;
            }

            public String getLivePicMin() {
                return livePicMin;
            }

            public void setLivePicMin(String livePicMin) {
                this.livePicMin = livePicMin;
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

            public String getLiveViewNum() {
                return liveViewNum;
            }

            public void setLiveViewNum(String liveViewNum) {
                this.liveViewNum = liveViewNum;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }
        }
    }
}
