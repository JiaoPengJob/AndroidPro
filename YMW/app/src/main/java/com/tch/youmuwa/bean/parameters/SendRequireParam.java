package com.tch.youmuwa.bean.parameters;

import java.util.List;

/**
 * Created by peng on 2017/8/10.
 */

public class SendRequireParam {

    private String title;
    private int require_type;
    private int address_id;
    private String begin_date;
    private String end_date;
    private String description;
    private String contacts;
    private String contact_number;
    private List<Integer> worker_types;
    private String price;

    public SendRequireParam() {
    }

    public SendRequireParam(String title, int require_type, int address_id, String begin_date, String end_date, String description, String contacts, String contact_number, List<Integer> worker_types, String price) {
        this.title = title;
        this.require_type = require_type;
        this.address_id = address_id;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.description = description;
        this.contacts = contacts;
        this.contact_number = contact_number;
        this.worker_types = worker_types;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRequire_type() {
        return require_type;
    }

    public void setRequire_type(int require_type) {
        this.require_type = require_type;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public List<Integer> getWorker_types() {
        return worker_types;
    }

    public void setWorker_types(List<Integer> worker_types) {
        this.worker_types = worker_types;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
