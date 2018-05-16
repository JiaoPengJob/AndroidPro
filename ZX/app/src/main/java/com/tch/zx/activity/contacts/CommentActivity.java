package com.tch.zx.activity.contacts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.contacts.CommentDynamicAdapter;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.GetDynamicCommentListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 动态评论页面
 */
public class CommentActivity extends BaseActivity {

    /**
     * 评论信息的列表
     */
    @BindView(R.id.rv_comment_dynamic_list)
    RecyclerView rv_comment_dynamic_list;
    @BindView(R.id.etSendDynamicComment)
    EditText etSendDynamicComment;
    @BindView(R.id.refreshCommentDynamic)
    SmartRefreshLayout refreshCommentDynamic;

    /**
     * 评论列表适配器
     */
    private CommentDynamicAdapter commentDynamicAdapter;
    private BasePresenter<Object> presenter;
    private String id, userId;
    private GetDynamicCommentListResult dynamicCommentListResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        //设置输入框在输入法上面
        View decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(HelperUtil.getGlobalLayoutListener(decorView, contentView));
        if (getIntent().getStringExtra("id") != null && getIntent().getStringExtra("userId") != null) {
            id = getIntent().getStringExtra("id");
            userId = getIntent().getStringExtra("userId");
            getDynamicCommentList();
        }
        refreshCommentDynamic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDynamicCommentList();
            }
        });
    }

    private void getDynamicCommentList() {
        presenter = new BasePresenter<Object>(CommentActivity.this);
        presenter.onCreate();
        presenter.attachView(getDynamicCommentListView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dynamic_id", id);
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        String data = HelperUtil.getParameter(map);
        presenter.getDynamicCommentList(data);
    }

    private BaseView<Object> getDynamicCommentListView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (refreshCommentDynamic.isRefreshing()) {
                refreshCommentDynamic.finishRefresh();
            }
            if (baseResultBean.getResult() != null) {
                dynamicCommentListResult = (GetDynamicCommentListResult) GsonUtil.parseJson(baseResultBean.getResult(), GetDynamicCommentListResult.class);
                setRecyclerViewData();
            }
        }

        @Override
        public void onError(String result) {
            if (refreshCommentDynamic.isRefreshing()) {
                refreshCommentDynamic.finishRefresh();
            }
            Log.e("ZX", "getDynamicCommentListView接口错误" + result);
        }
    };


    /**
     * 加载评论信息列表数据
     */
    private void setRecyclerViewData() {
        commentDynamicAdapter = new CommentDynamicAdapter(this, dynamicCommentListResult.getResponseObject());
        rv_comment_dynamic_list.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_comment_dynamic_list.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
        rv_comment_dynamic_list.setAdapter(commentDynamicAdapter);
        rv_comment_dynamic_list.setFocusable(false);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_comment)
    public void returnComment() {
        this.finish();
    }

    /**
     * 发布
     */
    @OnClick(R.id.tv_send_comment_dynamic)
    public void sendDynamicInfo() {
        if (!TextUtils.isEmpty(etSendDynamicComment.getText().toString())) {
            addDynamicComment();
        } else {
            Toast.makeText(CommentActivity.this, "评论内容不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDynamicComment() {
        presenter = new BasePresenter<Object>(CommentActivity.this);
        presenter.onCreate();
        presenter.attachView(addDynamicCommentView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dynamic_id", id);
        map.put("app_user_id", userId);
        map.put("content", etSendDynamicComment.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.addDynamicComment(data);
    }

    private BaseView<Object> addDynamicCommentView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            Toast.makeText(CommentActivity.this, baseResultBean.getResult().toString(), Toast.LENGTH_SHORT).show();
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                getDynamicCommentList();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "addDynamicCommentView接口错误" + result);
        }
    };

}
