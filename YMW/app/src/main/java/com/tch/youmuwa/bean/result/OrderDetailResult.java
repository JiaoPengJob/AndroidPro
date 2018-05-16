package com.tch.youmuwa.bean.result;

/**
 * Created by JoePeng on 2017/8/27.
 */

public class OrderDetailResult {

    /**
     * id : 30
     * require_id : 89
     * worker_id : 36
     * number : 2017090412014038881540
     * total_money :
     * title : 指定工人下单
     * type : 2
     * require_type : 点工
     * description : 测试
     * goto_price : 0.00
     * front_money :
     * worker_types : 油工
     * address : 北京市朝阳区望京SOHO
     * state : 220
     * state_msg : 辞退中
     * require_create_date : 2017/09/04
     * complete_date :
     * work_days :
     * contacts : 呵呵
     * contact_number : 18696325632
     * work_time : 2017/09/06—2017/09/08
     * create_date : 2017-09-04 12:01
     * cancel_date :
     * cancel_cause :
     * dismiss_money : 0.00
     * dismiss_cause : ..态度不好....施工质量不满意..呵呵
     * dismiss_date : 2017-09-04
     */

    private int id;
    private int require_id;
    private int worker_id;
    private String number = "";
    private String total_money = "";
    private String title = "";
    private int type;
    private String require_type = "";
    private String description = "";
    private String goto_price = "";
    private String front_money = "";
    private String worker_types = "";
    private String address = "";
    private int state;
    private String state_msg = "";
    private String require_create_date = "";
    private String complete_date = "";
    private String work_days = "";
    private String contacts = "";
    private String contact_number = "";
    private String work_time = "";
    private String create_date = "";
    private String cancel_date = "";
    private String cancel_cause = "";
    private String dismiss_money = "";
    private String dismiss_cause = "";
    private String dismiss_date = "";
    private String wages = "";
    private String begin_date = "";
    private String end_date = "";

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

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequire_id() {
        return require_id;
    }

    public void setRequire_id(int require_id) {
        this.require_id = require_id;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRequire_type() {
        return require_type;
    }

    public void setRequire_type(String require_type) {
        this.require_type = require_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoto_price() {
        return goto_price;
    }

    public void setGoto_price(String goto_price) {
        this.goto_price = goto_price;
    }

    public String getFront_money() {
        return front_money;
    }

    public void setFront_money(String front_money) {
        this.front_money = front_money;
    }

    public String getWorker_types() {
        return worker_types;
    }

    public void setWorker_types(String worker_types) {
        this.worker_types = worker_types;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getState_msg() {
        return state_msg;
    }

    public void setState_msg(String state_msg) {
        this.state_msg = state_msg;
    }

    public String getRequire_create_date() {
        return require_create_date;
    }

    public void setRequire_create_date(String require_create_date) {
        this.require_create_date = require_create_date;
    }

    public String getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(String complete_date) {
        this.complete_date = complete_date;
    }

    public String getWork_days() {
        return work_days;
    }

    public void setWork_days(String work_days) {
        this.work_days = work_days;
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

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCancel_date() {
        return cancel_date;
    }

    public void setCancel_date(String cancel_date) {
        this.cancel_date = cancel_date;
    }

    public String getCancel_cause() {
        return cancel_cause;
    }

    public void setCancel_cause(String cancel_cause) {
        this.cancel_cause = cancel_cause;
    }

    public String getDismiss_money() {
        return dismiss_money;
    }

    public void setDismiss_money(String dismiss_money) {
        this.dismiss_money = dismiss_money;
    }

    public String getDismiss_cause() {
        return dismiss_cause;
    }

    public void setDismiss_cause(String dismiss_cause) {
        this.dismiss_cause = dismiss_cause;
    }

    public String getDismiss_date() {
        return dismiss_date;
    }

    public void setDismiss_date(String dismiss_date) {
        this.dismiss_date = dismiss_date;
    }
}
