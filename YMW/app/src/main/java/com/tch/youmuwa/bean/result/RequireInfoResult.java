package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/8/15.
 */

public class RequireInfoResult {


    /**
     * id : 63
     * type : 1
     * title : 12369
     * address : 北京市东城区地址001
     * worker_types : 油工、木工、泥瓦工、水电工
     * worker_id : 1
     * require_type : 2
     * description : 干活
     * state : 3
     * create_date : 2017-09-21 16:55:19
     * goto_price : 0.01
     * contacts : 黄河
     * contact_number : 15245623655
     * begin_date : 2017-09-22
     * end_date : 2017-09-23
     * grab_count : 1
     * max_count : 10
     * order_number : 2017092116554446164359
     * refuse_reason :
     */

    private int id;
    private int type;
    private String title = "";
    private String address = "";
    private String worker_types = "";
    private int worker_id;
    private int require_type;
    private String description = "";
    private int state;
    private String create_date = "";
    private String goto_price = "";
    private String contacts = "";
    private String contact_number = "";
    private String begin_date = "";
    private String end_date = "";
    private int grab_count;
    private int max_count;
    private String order_number = "";
    private String refuse_reason = "";
    private String price = "";

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorker_types() {
        return worker_types;
    }

    public void setWorker_types(String worker_types) {
        this.worker_types = worker_types;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }

    public int getRequire_type() {
        return require_type;
    }

    public void setRequire_type(int require_type) {
        this.require_type = require_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getGoto_price() {
        return goto_price;
    }

    public void setGoto_price(String goto_price) {
        this.goto_price = goto_price;
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

    public int getGrab_count() {
        return grab_count;
    }

    public void setGrab_count(int grab_count) {
        this.grab_count = grab_count;
    }

    public int getMax_count() {
        return max_count;
    }

    public void setMax_count(int max_count) {
        this.max_count = max_count;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getRefuse_reason() {
        return refuse_reason;
    }

    public void setRefuse_reason(String refuse_reason) {
        this.refuse_reason = refuse_reason;
    }
}
