package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.UploadPicResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface UploadPicView extends View {

    void onSuccess(UploadPicResultBean uploadPicResultBean);

    void onError(String result);

}
