package com.tch.kuwanx.bean;

/**
 * Created by peng on 2017/8/30.
 */

public class AliPayResult {


    /**
     * alipay_sdk : lokielse/omnipay-alipay
     * app_id : 2017071707788002
     * biz_content : {"out_trade_no":"2017082517232997084743_9","body":"\u6e38\u6728\u86d9-\u4e0a\u95e8\u8d39","subject":"\u6e38\u6728\u86d9-\u4e0a\u95e8\u8d39","total_amount":"10.00","passback_params":"%7B%22order_number%22%3A%222017082517232997084743%22%7D","product_code":"QUICK_MSECURITY_PAY","spbill_create_ip":"192.168.0.1"}
     * charset : UTF-8
     * format : JSON
     * method : alipay.trade.app.pay
     * notify_url : http://localhost:8880/api/onpayresultalipay
     * sign_type : RSA
     * timestamp : 2017-08-28 18:00:55
     * version : 1.0
     * sign : fn6MVstLl+c6Jzh51dWhSmgNaYozsIxZs3iGQpi3myxfaferqBZ5sZxdfJxDZg6gcH+6A38npKWLeBT3CM/tEJNo6JBFZfCyWeRDxdxvglwzPYW9zKoIPe50vz96yQIHStZwMnZ8L0TMIPQEgfMWM8uRxFG1m14mZuDQejZip1/PNWMOI4xBnMJP9xuPVoeK3PuOCcelXXjkgfQfhPP9QsLGq0wFgjiEYIedLfZLkR1Nrjfd8jzsD3Ligh8sRS58Cef5KCpkTqrMKTiyBDW6Zm1fptnFjB8t/v9t6WamSQzznoki4ojwugXyGTDjx8mzGJjjiXV5fvvjVc2X5YWj3Q==
     * order_string : alipay_sdk=lokielse%2Fomnipay-alipay&app_id=2017071707788002&biz_content=%7B%22out_trade_no%22%3A%222017082517232997084743_9%22%2C%22body%22%3A%22%5Cu6e38%5Cu6728%5Cu86d9-%5Cu4e0a%5Cu95e8%5Cu8d39%22%2C%22subject%22%3A%22%5Cu6e38%5Cu6728%5Cu86d9-%5Cu4e0a%5Cu95e8%5Cu8d39%22%2C%22total_amount%22%3A%2210.00%22%2C%22passback_params%22%3A%22%257B%2522order_number%2522%253A%25222017082517232997084743%2522%257D%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22spbill_create_ip%22%3A%22192.168.0.1%22%7D&charset=UTF-8&format=JSON&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Flocalhost%3A8880%2Fapi%2Fonpayresultalipay&sign_type=RSA×tamp=2017-08-28+18%3A00%3A55&version=1.0&sign=fn6MVstLl%2Bc6Jzh51dWhSmgNaYozsIxZs3iGQpi3myxfaferqBZ5sZxdfJxDZg6gcH%2B6A38npKWLeBT3CM%2FtEJNo6JBFZfCyWeRDxdxvglwzPYW9zKoIPe50vz96yQIHStZwMnZ8L0TMIPQEgfMWM8uRxFG1m14mZuDQejZip1%2FPNWMOI4xBnMJP9xuPVoeK3PuOCcelXXjkgfQfhPP9QsLGq0wFgjiEYIedLfZLkR1Nrjfd8jzsD3Ligh8sRS58Cef5KCpkTqrMKTiyBDW6Zm1fptnFjB8t%2Fv9t6WamSQzznoki4ojwugXyGTDjx8mzGJjjiXV5fvvjVc2X5YWj3Q%3D%3D
     */

    private String alipay_sdk = "";
    private String app_id = "";
    private String biz_content = "";
    private String charset = "";
    private String format = "";
    private String method = "";
    private String notify_url = "";
    private String sign_type = "";
    private String timestamp = "";
    private String version = "";
    private String sign = "";
    private String order_string = "";

    public String getAlipay_sdk() {
        return alipay_sdk;
    }

    public void setAlipay_sdk(String alipay_sdk) {
        this.alipay_sdk = alipay_sdk;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrder_string() {
        return order_string;
    }

    public void setOrder_string(String order_string) {
        this.order_string = order_string;
    }
}
