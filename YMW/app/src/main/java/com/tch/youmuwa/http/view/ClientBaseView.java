package com.tch.youmuwa.http.view;

import com.tch.youmuwa.bean.result.BaseBean;

/**
 * 返回结果与UI交互的接口
 */

public interface ClientBaseView<T> {

    void onSuccess(BaseBean<T> baseBean);

    void onError(String result);

}
