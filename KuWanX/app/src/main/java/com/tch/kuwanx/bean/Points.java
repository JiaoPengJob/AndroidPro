package com.tch.kuwanx.bean;

/**
 * Created by jiaop on 2017/11/16.
 */

public class Points<T> {

    private String date;
    private T item;

    public Points() {
    }

    public Points(String date, T item) {
        this.date = date;
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
