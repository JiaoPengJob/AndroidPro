package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SmallCommentInsertResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SmallCommentInsertView extends View {

    void onSuccess(SmallCommentInsertResultBean smallCommentInsertResultBean);

    void onError(String result);

}
