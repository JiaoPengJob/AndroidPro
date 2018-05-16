package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/8/22.
 */

public class RequireDetailsResult {

    /**
     * id : 89
     * isgrab : 6
     * type : 2
     * title : 指定工人下单
     * order_id : 30
     * order_number : 2017090412014038881540
     * address : 北京市朝阳区望京SOHO
     * worker_types : 油工
     * worker_id : 36
     * require_type : 点工
     * require_type_code : 1
     * price : 0.01
     * goto_price : 0.00
     * begin_date : 2017-09-06
     * end_date : 2017-09-08
     * description : 测试
     * contacts : 呵呵
     * contact_number : 18696325632
     * grab_count : 0
     * max_count : 10
     * state : 3
     * create_date : 2017-09-04
     */

    private int id;
    private int isgrab;
    private int type;
    private String title = "";
    private int order_id;
    private String order_number = "";
    private String address = "";
    private String worker_types = "";
    private int worker_id;
    private String require_type = "";
    private int require_type_code;
    private String price = "";
    private String goto_price = "";
    private String begin_date = "";
    private String end_date = "";
    private String description = "";
    private String contacts = "";
    private String contact_number = "";
    private int grab_count;
    private int max_count;
    private int state;
    private String create_date = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsgrab() {
        return isgrab;
    }

    public void setIsgrab(int isgrab) {
        this.isgrab = isgrab;
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

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
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

    public String getRequire_type() {
        return require_type;
    }

    public void setRequire_type(String require_type) {
        this.require_type = require_type;
    }

    public int getRequire_type_code() {
        return require_type_code;
    }

    public void setRequire_type_code(int require_type_code) {
        this.require_type_code = require_type_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoto_price() {
        return goto_price;
    }

    public void setGoto_price(String goto_price) {
        this.goto_price = goto_price;
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
}