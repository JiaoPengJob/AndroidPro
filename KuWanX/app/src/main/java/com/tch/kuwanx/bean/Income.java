package com.tch.kuwanx.bean;

/**
 * Created by jiaop on 2018/1/26.
 * 收益
 */

public class Income {

    private int plus;
    private int less;

    public Income() {
    }

    public Income(int plus, int less) {
        this.plus = plus;
        this.less = less;
    }

    public int getPlus() {
        return plus;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public int getLess() {
        return less;
    }

    public void setLess(int less) {
        this.less = less;
    }
}
