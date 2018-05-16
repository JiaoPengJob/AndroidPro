package com.tch.youmuwa.bean;

/**
 * Created by peng on 2017/8/11.
 */

public class ItemISSel {

    private boolean isSel;

    public ItemISSel() {
    }

    public ItemISSel(boolean isSel) {
        this.isSel = isSel;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }
}
