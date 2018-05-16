package com.tch.zx.activity.line.unline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.line.MapActivity;
import com.tch.zx.adapter.line.OffLineCommentAdapter;
import com.tch.zx.enjoy.fragment.EmotionMainFragment;
import com.tch.zx.enjoy.utils.ScreenUtils;
import com.tch.zx.http.bean.result.OfflineCommentResultBean;
import com.tch.zx.http.bean.result.OfflineDetailResultBean;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.OfflineCommentInsertModel;
import com.tch.zx.http.presenter.CollectCancelPresenter;
import com.tch.zx.http.presenter.CollectInsertPresenter;
import com.tch.zx.http.presenter.ConcernCancelPresenter;
import com.tch.zx.http.presenter.ConcernInsertPresenter;
import com.tch.zx.http.presenter.OfflineCommentInsertPresenter;
import com.tch.zx.http.presenter.OfflineCommentPresenter;
import com.tch.zx.http.presenter.OfflineDetailPresenter;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.CollectInsertView;
import com.tch.zx.http.view.ConcernCancelView;
import com.tch.zx.http.view.ConcernInsertView;
import com.tch.zx.http.view.OfflineCommentInsertView;
import com.tch.zx.http.view.OfflineCommentView;
import com.tch.zx.http.view.OfflineDetailView;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.MediaSharePopupWindow;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 线下信息展示页面
 */
public class LineDownInfoActivity extends BaseActivity {

    @BindView(R.id.ivOfflinePicMax)
    ImageView ivOfflinePicMax;
    @BindView(R.id.tvOfflineName)
    TextView tvOfflineName;
    @BindView(R.id.tvPositionOffline)
    TextView tvPositionOffline;
    @BindView(R.id.tvAppUserName)
    TextView tvAppUserName;
    @BindView(R.id.civ_user_photo_line_down)
    CircleImageView civ_user_photo_line_down;
    @BindView(R.id.tvOfflineSignupNum)
    TextView tvOfflineSignupNum;
    @BindView(R.id.tvOfflineTime)
    TextView tvOfflineTime;
    @BindView(R.id.tvOfflineAddress)
    TextView tvOfflineAddress;
    @BindView(R.id.tvAppUserIntroduce)
    TextView tvAppUserIntroduce;
    @BindView(R.id.tvOfflineClassIntroduce)
    TextView tvOfflineClassIntroduce;
    @BindView(R.id.tvOfflineMoney)
    TextView tvOfflineMoney;
    @BindView(R.id.iv_line_down_collection)
    ImageView iv_line_down_collection;
    @BindView(R.id.llParentView)
    LinearLayout llParentView;
    @BindView(R.id.svClassInfo)
    ScrollView svClassInfo;
    @BindView(R.id.iv_info_line_down_class_choose)
    ImageView iv_info_line_down_class_choose;
    @BindView(R.id.tv_info_line_down_class_choose)
    TextView tv_info_line_down_class_choose;
    @BindView(R.id.view_info_line_down_bottom_class)
    View view_info_line_down_bottom_class;
    @BindView(R.id.iv_info_line_down_talk_choose)
    ImageView iv_info_line_down_talk_choose;
    @BindView(R.id.tv_info_line_down_talk_choose)
    TextView tv_info_line_down_talk_choose;
    @BindView(R.id.view_info_line_down_bottom_talk)
    View view_info_line_down_bottom_talk;
    @BindView(R.id.llClassInfoHeight)
    LinearLayout llClassInfoHeight;
    @BindView(R.id.refreshOffLine)
    SmartRefreshLayout refreshOffLine;
    @BindView(R.id.ivConcernInsertOffLine)
    ImageView ivConcernInsertOffLine;
    @BindView(R.id.rvOfflineComment)
    RecyclerView rvOfflineComment;
    @BindView(R.id.etOfflineInput)
    EditText etOfflineInput;
    @BindView(R.id.iv_enjoy_talk_line_down)
    ImageView iv_enjoy_talk_line_down;
    @BindView(R.id.llInputParent)
    LinearLayout llInputParent;

    private String offlineId = "";
    private OfflineDetailPresenter offlineDetailPresenter;
    private CollectInsertPresenter collectInsertPresenter;
    private CollectCancelPresenter cancelPresenter;
    private WindowManager.LayoutParams params;
    private ConcernInsertPresenter concernInsertPresenter;
    private ConcernCancelPresenter concernCancelPresenter;
    private OfflineDetailResultBean.ResultBean.ResponseObjectBean offlineBean;
    private OfflineCommentPresenter offlineCommentPresenter;
    private EmotionMainFragment emotionMainFragment;
    private OfflineCommentInsertPresenter offlineCommentInsertPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_line_down_info);
        //集成使用Butterknife
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        if (getIntent().getStringExtra("offlineId") != null) {
            offlineId = getIntent().getStringExtra("offlineId");
            offlineDetail();
            initOffLineComment();
        }

        etOfflineInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置输入框在输入法上面
                View decorView = getWindow().getDecorView();
                View contentView = findViewById(Window.ID_ANDROID_CONTENT);
                decorView.getViewTreeObserver().addOnGlobalLayoutListener(HelperUtil.getGlobalLayoutListener(decorView, contentView));
                etOfflineInput.setFocusable(true);
                etOfflineInput.setFocusableInTouchMode(true);
                etOfflineInput.requestFocus();
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(llInputParent, 0);
                initEmotionMainFragment();
            }
        });

        refreshOffLine.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                offlineDetail();
                initOffLineComment();
            }
        });
    }

    /**
     * 加载视图信息
     */
    private void initViewData(OfflineDetailResultBean.ResultBean.ResponseObjectBean offlineDetail) {
        Glide.with(LineDownInfoActivity.this).load(offlineDetail.getOfflinePicMax()).into(ivOfflinePicMax);

        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                civ_user_photo_line_down.setImageBitmap(resource);
            }
        };
        Glide.with(LineDownInfoActivity.this).asBitmap().load(offlineDetail.getAppUserPic()).into(target);
        tvOfflineName.setText(offlineDetail.getOfflineName());
        tvPositionOffline.setText(offlineDetail.getPosition());
        tvAppUserName.setText("讲师" + offlineDetail.getAppUserName());
        tvOfflineSignupNum.setText(offlineDetail.getOfflineSignupNum() + "名额");
        tvOfflineTime.setText(offlineDetail.getOfflineStartTime() + "~" + offlineDetail.getOfflineEndTime());
        tvOfflineAddress.setText(offlineDetail.getOfflineAddress());
        tvAppUserIntroduce.setText(offlineDetail.getAppUserIntroduce());
        tvOfflineClassIntroduce.setText(offlineDetail.getOfflineClassIntroduce());
        tvOfflineMoney.setText("￥" + offlineDetail.getOfflineMoney() + "元");
    }

    /**
     * 链接接口
     */
    private void offlineDetail() {
        offlineDetailPresenter = new OfflineDetailPresenter(this);
        offlineDetailPresenter.onCreate();
        offlineDetailPresenter.attachView(offlineDetailView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", HelperUtil.getAppUserId(LineDownInfoActivity.this));
        map.put("offlineId", offlineId);

        String data = HelperUtil.getParameter(map);
        offlineDetailPresenter.offlineDetail(data);
    }

    private OfflineDetailView offlineDetailView = new OfflineDetailView() {
        @Override
        public void onSuccess(OfflineDetailResultBean offlineDetailResultBean) {
            if (offlineDetailResultBean.getResult().getResponseObject() != null) {
                offlineBean = offlineDetailResultBean.getResult().getResponseObject();
                initViewData(offlineDetailResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "offlineDetailView:==" + result);
        }
    };

    /**
     * 加载评论列表
     */
    private void initOffLineComment() {
        offlineCommentPresenter = new OfflineCommentPresenter(this);
        offlineCommentPresenter.onCreate();
        offlineCommentPresenter.attachView(offlineCommentView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offlineId", offlineId);
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        String data = HelperUtil.getParameter(map);
        offlineCommentPresenter.offlineComment(data);

    }

    private OfflineCommentView offlineCommentView = new OfflineCommentView() {
        @Override
        public void onSuccess(OfflineCommentResultBean offlineCommentResultBean) {
            if (offlineCommentResultBean.getResult().getResponseObject() != null
                    && offlineCommentResultBean.getResult().getResponseObject().size() > 0) {
                initCommentList(offlineCommentResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "OfflineCommentView:==" + result);
        }
    };

    /**
     * 加载列表数据
     */
    private void initCommentList(List<OfflineCommentResultBean.ResultBean.ResponseObjectBean> list) {
        OffLineCommentAdapter specialCommentAdapter = new OffLineCommentAdapter(LineDownInfoActivity.this, list);
        rvOfflineComment.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rvOfflineComment.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
        rvOfflineComment.setAdapter(specialCommentAdapter);
        rvOfflineComment.setNestedScrollingEnabled(false);
    }

    /**
     * 进入地图
     */
    @OnClick(R.id.rl_intent_map)
    public void openMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ivBack)
    public void backReturn() {
        this.finish();
    }

    /**
     * 分享
     */
    @OnClick(R.id.iv_line_down_share)
    public void shareOffLine() {
        shareInfo();
    }

    /**
     * 分享
     */
    private void shareInfo() {
        MediaSharePopupWindow mediaSharePopupWindow = new MediaSharePopupWindow(LineDownInfoActivity.this);
        //设置Popupwindow显示位置（从底部弹出）
        mediaSharePopupWindow.showAtLocation(llParentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mediaSharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
    }

    /**
     * 收藏
     */
    @OnClick(R.id.iv_line_down_collection)
    public void collection() {
        if (iv_line_down_collection.getDrawable().getCurrent().getConstantState()
                .equals(getResources().getDrawable(R.mipmap.player_collection).getConstantState())) {
            collectInsert();
        } else {
            collectCancel();
        }
    }

    /**
     * 收藏
     */
    private void collectInsert() {
        collectInsertPresenter = new CollectInsertPresenter(LineDownInfoActivity.this);
        collectInsertPresenter.onCreate();
        collectInsertPresenter.attachView(collectInsertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", HelperUtil.getAppUserId(LineDownInfoActivity.this));
        map.put("collectType", "4");
        map.put("collectTypeId", offlineId);

        String data = HelperUtil.getParameter(map);
        collectInsertPresenter.collectInsert(data);

    }

    /**
     * 收藏
     */
    private CollectInsertView collectInsertView = new CollectInsertView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    iv_line_down_collection.setImageResource(R.mipmap.player_collection_audio);
                    Toast.makeText(LineDownInfoActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LineDownInfoActivity.this, "收藏失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "collectInsertView" + result);
        }
    };

    /**
     * 取消收藏
     */
    private void collectCancel() {
        cancelPresenter = new CollectCancelPresenter(LineDownInfoActivity.this);
        cancelPresenter.onCreate();
        cancelPresenter.attachView(cancelView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", HelperUtil.getAppUserId(LineDownInfoActivity.this));
        map.put("collectType", "4");
        map.put("collectTypeId", offlineId);

        String data = HelperUtil.getParameter(map);
        cancelPresenter.collectCancel(data);

    }

    private CollectCancelView cancelView = new CollectCancelView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    iv_line_down_collection.setImageResource(R.mipmap.player_collection);
                    Toast.makeText(LineDownInfoActivity.this, "取消收藏成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LineDownInfoActivity.this, "取消收藏失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "collectCancelView" + result);
        }
    };

    /**
     * 课程点击监听
     */
    @OnClick(R.id.ll_info_line_down_class)
    public void classInfo() {
        svClassInfo.post(new Runnable() {
            @Override
            public void run() {
                Message messageClass = new Message();
                messageClass.what = 40;
                messageClass.obj = 0;
                handler.sendMessage(messageClass);
            }
        });

        classSelect();
    }

    /**
     * 课程选择的效果
     */
    private void classSelect() {
        iv_info_line_down_class_choose.setImageResource(R.mipmap.class_seleted);
        tv_info_line_down_class_choose.setTextColor(Color.parseColor("#2EA168"));
        view_info_line_down_bottom_class.setVisibility(View.VISIBLE);

        iv_info_line_down_talk_choose.setImageResource(R.mipmap.talk_unseleted);
        tv_info_line_down_talk_choose.setTextColor(Color.parseColor("#666666"));
        view_info_line_down_bottom_talk.setVisibility(View.GONE);

    }

    /**
     * 评论点击监听
     */
    @OnClick(R.id.ll_info_top_talk)
    public void talkInfo() {
        svClassInfo.post(new Runnable() {
            @Override
            public void run() {
                Message messageClass = new Message();
                messageClass.what = 30;
                messageClass.obj = llClassInfoHeight.getMeasuredHeight();
                handler.sendMessage(messageClass);
            }
        });
        talkSelect();
    }

    /**
     * 评论选择的效果
     */
    public void talkSelect() {
        iv_info_line_down_class_choose.setImageResource(R.mipmap.class_unseleted);
        tv_info_line_down_class_choose.setTextColor(Color.parseColor("#666666"));
        view_info_line_down_bottom_class.setVisibility(View.GONE);

        iv_info_line_down_talk_choose.setImageResource(R.mipmap.talk_seleted);
        tv_info_line_down_talk_choose.setTextColor(Color.parseColor("#2EA168"));
        view_info_line_down_bottom_talk.setVisibility(View.VISIBLE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 40:
                    svClassInfo.scrollTo(0, (int) msg.obj);
                    break;
            }
        }
    };

    /**
     * 关注
     */
    @OnClick(R.id.ivConcernInsertOffLine)
    public void concernInsertOffLine() {
        if (ivConcernInsertOffLine.getDrawable().getCurrent().getConstantState()
                .equals(getResources().getDrawable(R.mipmap.add_attention_player).getConstantState())) {
            concernInsert();
        } else {
            concernCancel();
        }
    }

    /**
     * 关注
     */
    private void concernInsert() {
        concernInsertPresenter = new ConcernInsertPresenter(LineDownInfoActivity.this);
        concernInsertPresenter.onCreate();
        concernInsertPresenter.attachView(concernInsertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", HelperUtil.getAppUserId(LineDownInfoActivity.this));
        map.put("appUserConcern", offlineBean.getAppUserId());

        String data = HelperUtil.getParameter(map);
        concernInsertPresenter.concernInsert(data);
    }

    /**
     * 关注
     */
    private ConcernInsertView concernInsertView = new ConcernInsertView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    ivConcernInsertOffLine.setImageResource(R.mipmap.has_attention);
                } else {
                    Toast.makeText(LineDownInfoActivity.this, "关注失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "concernInsertView:==" + result);
        }
    };

    /**
     * 取消关注
     */
    private void concernCancel() {
        concernCancelPresenter = new ConcernCancelPresenter(LineDownInfoActivity.this);
        concernCancelPresenter.onCreate();
        concernCancelPresenter.attachView(concernCancelView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", HelperUtil.getAppUserId(LineDownInfoActivity.this));
        map.put("appUserConcern", offlineBean.getAppUserId());

        String data = HelperUtil.getParameter(map);
        concernCancelPresenter.concernCancel(data);
    }

    /**
     * 取消关注
     */
    private ConcernCancelView concernCancelView = new ConcernCancelView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean.getRet().equals("1")) {
                ivConcernInsertOffLine.setImageResource(R.mipmap.add_attention_player);
            } else {
                Toast.makeText(LineDownInfoActivity.this, "取消关注失败!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "cancelView:==" + result);
        }
    };

    /**
     * 立即咨询
     */
    @OnClick(R.id.ivAdvisory)
    public void advisory() {
        HelperUtil.callPhone(LineDownInfoActivity.this, offlineBean.getAppUserPhone());
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(etOfflineInput);
        emotionMainFragment.bindToEmotionButton(iv_enjoy_talk_line_down);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_offline, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    /**
     * 发表
     */
    @OnClick(R.id.tv_line_down_talk)
    public void submitInpput() {
        if (!TextUtils.isEmpty(etOfflineInput.getText().toString())) {
            sendNewComment(etOfflineInput.getText().toString());
        }
    }

    private void sendNewComment(String text) {
        offlineCommentInsertPresenter = new OfflineCommentInsertPresenter(this);
        offlineCommentInsertPresenter.onCreate();
        offlineCommentInsertPresenter.attachView(offlineCommentInsertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", HelperUtil.getAppUserId(LineDownInfoActivity.this));
        map.put("offlineId", offlineId);
        map.put("offlineCommentContent", text);

        String data = HelperUtil.getParameter(map);
        offlineCommentInsertPresenter.offlineCommentInsert(data);

    }

    private OfflineCommentInsertView offlineCommentInsertView = new OfflineCommentInsertView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    offlineCommentPresenter.attachView(offlineCommentView);
                    etOfflineInput.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    Toast.makeText(LineDownInfoActivity.this, "发表失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "OfflineCommentInsertView:==" + result);
        }
    };

    /**
     * 判断是否拦截返回键操作
     */
    @Override
    public void onBackPressed() {
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }

}
