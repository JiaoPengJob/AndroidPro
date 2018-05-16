package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/9/13.
 */

public class AppShareResult {

    /**
     * title : 游木蛙
     * image : http://www.youmuwa.com/images/shareapp.png
     * description : 一站式找工人家装服务平台。为家装用户提供省钱、省心、省时间的装修服务。
     * url : http://www.youmuwa.com/api/shareapp
     */

    private String title = "";
    private String image = "";
    private String description = "";
    private String url = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
