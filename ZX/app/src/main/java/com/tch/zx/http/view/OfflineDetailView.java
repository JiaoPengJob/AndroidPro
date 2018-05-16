package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.OfflineDetailResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface OfflineDetailView extends View {

    void onSuccess(OfflineDetailResultBean offlineDetailResultBean);

    void onError(String result);

}
