package com.tch.kuwanx.bean;

/**
 * Created by jiaop on 2017/11/7.
 */

public class IndexBean<T> extends BaseIndexPinyinBean  {

    private String name;
    private T item;

    public IndexBean() {
    }

    public IndexBean(String name, T item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public String getTarget() {
        return name;
    }
}
