package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.RetResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SpecialCommentInsertView extends View {

    void onSuccess(RetResultBean retResultBean);

    void onError(String result);

}
