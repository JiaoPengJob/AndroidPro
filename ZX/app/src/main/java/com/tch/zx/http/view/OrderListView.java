package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.OrderListResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface OrderListView extends View {

    void onSuccess(OrderListResultBean orderListResultBean);

    void onError(String result);

}
