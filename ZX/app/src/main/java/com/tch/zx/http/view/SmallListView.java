package com.tch.zx.http.view;

import com.tch.zx.bean.SmallListBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SmallListView extends View {

    void onSuccess(SmallListBean baseResultBean);

    void onError(String result);

}
