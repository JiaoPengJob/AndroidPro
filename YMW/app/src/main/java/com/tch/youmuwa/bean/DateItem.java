package com.tch.youmuwa.bean;

/**
 * 记录日历时间
 */
public class DateItem {

    private int dateOfMonth = 0;
    private boolean isselect = false;
    private int year;
    private int month;
    private int day;
    private boolean hasSelect;

    public DateItem() {
    }

    public DateItem(int year, int month, int dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDateOfMonth() {
        return dateOfMonth;
    }

    public void setDateOfMonth(int dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    public boolean isselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isHasSelect() {
        return hasSelect;
    }

    public void setHasSelect(boolean hasSelect) {
        this.hasSelect = hasSelect;
    }


}
