package com.tch.kuwanx.bean;

/**
 * Created by jiaop on 2017/10/31.
 */

public class SearchAreaCity<T> {

    private boolean isSel;

    private T item;

    public SearchAreaCity() {
    }

    public SearchAreaCity(boolean isSel, T item) {
        this.isSel = isSel;
        this.item = item;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
