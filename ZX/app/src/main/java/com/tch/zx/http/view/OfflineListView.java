package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.OfflineListResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface OfflineListView extends View {

    void onSuccess(OfflineListResultBean baseResultBean);

    void onError(String result);

}
