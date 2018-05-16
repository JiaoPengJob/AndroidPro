package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/18.
 */

public class SmallDetailsResultBean {


    /**
     * ret : 1
     * result : {"responseObject":{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"sdfasdfasf","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","isConcern":1,"position":"市场运营部经理","smallClassId":1,"smallClassIntroduce":"asdfasdf","smallClassName":"测试课程","smallClassPicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","smallClassPicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg","videoList":[{"audioPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/878a30775d3f48b6a0afcb981fcfe59e.jpg","confirmPay":1,"smallClassNote":"","videoId":22,"videoMoney":50.52,"videoName":"测试1","videoPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","viewNum":"1560"},{"audioPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/f5b253e741254132adc489893d4bb768.jpg","confirmPay":1,"smallClassNote":"","videoId":23,"videoMoney":80.52,"videoName":"测试2","videoPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","viewNum":"842"}]}}
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
         * responseObject : {"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"sdfasdfasf","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","isConcern":1,"position":"市场运营部经理","smallClassId":1,"smallClassIntroduce":"asdfasdf","smallClassName":"测试课程","smallClassPicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","smallClassPicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg","videoList":[{"audioPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/878a30775d3f48b6a0afcb981fcfe59e.jpg","confirmPay":1,"smallClassNote":"","videoId":22,"videoMoney":50.52,"videoName":"测试1","videoPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","viewNum":"1560"},{"audioPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/f5b253e741254132adc489893d4bb768.jpg","confirmPay":1,"smallClassNote":"","videoId":23,"videoMoney":80.52,"videoName":"测试2","videoPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","viewNum":"842"}]}
         */

        private ResponseObjectBean responseObject;

        public ResponseObjectBean getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(ResponseObjectBean responseObject) {
            this.responseObject = responseObject;
        }

        public static class ResponseObjectBean {
            /**
             * appUserId : d8f08b34f2b7490481097328bf3a66fb
             * appUserIntroduce : sdfasdfasf
             * appUserName : 测试账号001
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * isConcern : 1
             * position : 市场运营部经理
             * smallClassId : 1
             * smallClassIntroduce : asdfasdf
             * smallClassName : 测试课程
             * smallClassPicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * smallClassPicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg
             * videoList : [{"audioPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/878a30775d3f48b6a0afcb981fcfe59e.jpg","confirmPay":1,"smallClassNote":"","videoId":22,"videoMoney":50.52,"videoName":"测试1","videoPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","viewNum":"1560"},{"audioPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/f5b253e741254132adc489893d4bb768.jpg","confirmPay":1,"smallClassNote":"","videoId":23,"videoMoney":80.52,"videoName":"测试2","videoPath":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","viewNum":"842"}]
             */

            private String appUserId;
            private String appUserIntroduce;
            private String appUserName;
            private String appUserPic;
            private int isConcern;
            private String position;
            private int smallClassId;
            private String smallClassIntroduce;
            private String smallClassName;
            private String smallClassPicMax;
            private String smallClassPicMin;
            private List<VideoListBean> videoList;

            public String getAppUserId() {
                return appUserId;
            }

            public void setAppUserId(String appUserId) {
                this.appUserId = appUserId;
            }

            public String getAppUserIntroduce() {
                return appUserIntroduce;
            }

            public void setAppUserIntroduce(String appUserIntroduce) {
                this.appUserIntroduce = appUserIntroduce;
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

            public int getIsConcern() {
                return isConcern;
            }

            public void setIsConcern(int isConcern) {
                this.isConcern = isConcern;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public int getSmallClassId() {
                return smallClassId;
            }

            public void setSmallClassId(int smallClassId) {
                this.smallClassId = smallClassId;
            }

            public String getSmallClassIntroduce() {
                return smallClassIntroduce;
            }

            public void setSmallClassIntroduce(String smallClassIntroduce) {
                this.smallClassIntroduce = smallClassIntroduce;
            }

            public String getSmallClassName() {
                return smallClassName;
            }

            public void setSmallClassName(String smallClassName) {
                this.smallClassName = smallClassName;
            }

            public String getSmallClassPicMax() {
                return smallClassPicMax;
            }

            public void setSmallClassPicMax(String smallClassPicMax) {
                this.smallClassPicMax = smallClassPicMax;
            }

            public String getSmallClassPicMin() {
                return smallClassPicMin;
            }

            public void setSmallClassPicMin(String smallClassPicMin) {
                this.smallClassPicMin = smallClassPicMin;
            }

            public List<VideoListBean> getVideoList() {
                return videoList;
            }

            public void setVideoList(List<VideoListBean> videoList) {
                this.videoList = videoList;
            }

            public static class VideoListBean {
                /**
                 * audioPath : http://115.28.95.41:8080/uploadfile/zhixian/201706/878a30775d3f48b6a0afcb981fcfe59e.jpg
                 * confirmPay : 1
                 * smallClassNote :
                 * videoId : 22
                 * videoMoney : 50.52
                 * videoName : 测试1
                 * videoPath : http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4
                 * viewNum : 1560
                 */

                private String audioPath;
                private int confirmPay;
                private String smallClassNote;
                private int videoId;
                private double videoMoney;
                private String videoName;
                private String videoPath;
                private String viewNum;

                public String getAudioPath() {
                    return audioPath;
                }

                public void setAudioPath(String audioPath) {
                    this.audioPath = audioPath;
                }

                public int getConfirmPay() {
                    return confirmPay;
                }

                public void setConfirmPay(int confirmPay) {
                    this.confirmPay = confirmPay;
                }

                public String getSmallClassNote() {
                    return smallClassNote;
                }

                public void setSmallClassNote(String smallClassNote) {
                    this.smallClassNote = smallClassNote;
                }

                public int getVideoId() {
                    return videoId;
                }

                public void setVideoId(int videoId) {
                    this.videoId = videoId;
                }

                public double getVideoMoney() {
                    return videoMoney;
                }

                public void setVideoMoney(double videoMoney) {
                    this.videoMoney = videoMoney;
                }

                public String getVideoName() {
                    return videoName;
                }

                public void setVideoName(String videoName) {
                    this.videoName = videoName;
                }

                public String getVideoPath() {
                    return videoPath;
                }

                public void setVideoPath(String videoPath) {
                    this.videoPath = videoPath;
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
}
