package com.tch.kuwanx.bean;

/**
 * Created by jiaop on 2017/11/20.
 */
public class CartsItem<T> {

    private boolean ifEdit = false;
    private boolean isWillPay = false;
    private T item;

    public CartsItem() {
    }

    public CartsItem(boolean ifEdit, boolean isWillPay, T item) {
        this.ifEdit = ifEdit;
        this.isWillPay = isWillPay;
        this.item = item;
    }

    public boolean isIfEdit() {
        return ifEdit;
    }

    public void setIfEdit(boolean ifEdit) {
        this.ifEdit = ifEdit;
    }

    public boolean isWillPay() {
        return isWillPay;
    }

    public void setWillPay(boolean willPay) {
        isWillPay = willPay;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
