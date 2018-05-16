package com.tch.kuwanx.bean;

/**
 * Created by jiaop on 2017/11/6.
 */

public class ContactBean<T> {

    private boolean isSelect = false;
    private T bean;

    public ContactBean() {
    }

    public ContactBean(boolean isSelect, T bean) {
        this.isSelect = isSelect;
        this.bean = bean;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }
}
