package com.tch.zx.http.exception;

import java.io.IOException;

/**
 * 请求返回数据异常处理类
 */

public class ResultException extends IOException {

    public ResultException() {
        super();
    }

    public ResultException(String message) {
        super(message);
    }

}
