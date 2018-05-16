package com.tch.youmuwa.bean.parameters;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by peng on 2017/8/25.
 */

public class OrderEvaluateParam {

    private String order_number;
    private String content;
    private int grade;
    private List<String> images;

    public OrderEvaluateParam() {
    }

    public OrderEvaluateParam(String order_number, String content, int grade, List<String> images) {
        this.order_number = order_number;
        this.content = content;
        this.grade = grade;
        this.images = images;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
