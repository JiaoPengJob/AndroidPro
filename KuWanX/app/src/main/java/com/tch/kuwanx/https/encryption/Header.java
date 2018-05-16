package com.tch.kuwanx.https.encryption;

/**
 * Http参数请求内容头
 */

public class Header {

    private String channel;

    private String sign;

    private String timestamp;

    public Header() {
    }

    public Header(String channel, String sign, String timestamp) {
        this.channel = channel;
        this.sign = sign;
        this.timestamp = timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
