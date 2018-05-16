package com.tch.kuwanx.https.encryption;

import java.util.Map;

/**
 * 链接服务器时生成参数的基类
 */

public class BaseBean {

    private Map<String, Object> body;

    private Header header;

    public BaseBean() {
    }

    public BaseBean(Map<String, Object> body, Header header) {
        this.body = body;
        this.header = header;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
