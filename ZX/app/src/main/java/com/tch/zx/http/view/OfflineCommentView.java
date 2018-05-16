package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.OfflineCommentResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface OfflineCommentView extends View {

    void onSuccess(OfflineCommentResultBean offlineCommentResultBean);

    void onError(String result);

}
