package com.tch.zx.activity.line.greatclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.tch.zx.adapter.line.SmallCommentAdapter;
import com.tch.zx.adapter.line.SpecialCommentAdapter;
import com.tch.zx.adapter.line.TypesSelectAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.enjoy.fragment.EmotionMainFragment;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SmallCommentInsertResultBean;
import com.tch.zx.http.bean.result.SmallCommentResultBean;
import com.tch.zx.http.bean.result.SmallDetailsResultBean;
import com.tch.zx.http.bean.result.SpecialCommentResultBean;
import com.tch.zx.http.bean.result.SpecialDetailResultBean;
import com.tch.zx.http.model.SpecialCommentInsertModel;
import com.tch.zx.http.presenter.ConcernCancelPresenter;
import com.tch.zx.http.presenter.ConcernInsertPresenter;
import com.tch.zx.http.presenter.SmallCommentInsertPresenter;
import com.tch.zx.http.presenter.SmallCommentPresenter;
import com.tch.zx.http.presenter.SmallDetailsPresenter;
import com.tch.zx.http.presenter.SpecialCommentInsertPresenter;
import com.tch.zx.http.presenter.SpecialCommentPresenter;
import com.tch.zx.http.presenter.SpecialDetailPresenter;
import com.tch.zx.http.view.ConcernCancelView;
import com.tch.zx.http.view.ConcernInsertView;
import com.tch.zx.http.view.SmallCommentInsertView;
import com.tch.zx.http.view.SmallCommentView;
import com.tch.zx.http.view.SmallDetailsView;
import com.tch.zx.http.view.SpecialCommentInsertView;
import com.tch.zx.http.view.SpecialCommentView;
import com.tch.zx.http.view.SpecialDetailView;
import com.tch.zx.media.AudioMediaPlayerControler;
import com.tch.zx.media.MediaPlayerController;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.util.NetStateUtil;
import com.tch.zx.view.ObservableScrollView;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 精品小课播放页面
 */
public class GreatClassPlayerActivity extends BaseActivity {

    @BindView(R.id.rl_player_view)
    RelativeLayout rl_player_view;
    @BindView(R.id.rl_activity_player_class_main)
    RelativeLayout rl_activity_player_class_main;
    /*---------------------效果布局----------------------------------*/
    //用来显示弹窗的父布局
    @BindView(R.id.observableScrollView)
    ObservableScrollView observableScrollView;
    /*-------------------选择页面-------------------*/
    //选择布局
    @BindView(R.id.rl_choose_audio_video_view)
    RelativeLayout rl_choose_audio_video_view;
    /*--------------音频播放页面---------------*/
    //音频控制器展示布局
    @BindView(R.id.ll_audio_player_play)
    LinearLayout ll_audio_player_play;
    //控制器
    @BindView(R.id.ll_audio_player_controller)
    LinearLayout ll_audio_player_controller;
    //展示大图
    @BindView(R.id.iv_audio_photo_bgm)
    ImageView iv_audio_photo_bgm;
    @BindView(R.id.rl_audio_player_top)
    RelativeLayout rl_audio_player_top;

    /*------------------视频播放页面------------------------*/
    //视频播放展示布局
    @BindView(R.id.ll_video_player_play)
    LinearLayout ll_video_player_play;
    //视频的父布局
    @BindView(R.id.rl_player_play)
    RelativeLayout rl_player_play;

    /*-----------------------所有信息展示-------------------------------*/
    //信息展示的外部布局
    @BindView(R.id.ll_class_info)
    LinearLayout ll_class_info;
    @BindView(R.id.iv_info_class_choose)
    ImageView iv_info_class_choose;
    @BindView(R.id.tv_info_class_choose)
    TextView tv_info_class_choose;
    @BindView(R.id.view_info_bottom_class)
    View view_info_bottom_class;
    @BindView(R.id.iv_info_types_choose)
    ImageView iv_info_types_choose;
    @BindView(R.id.tv_info_types_choose)
    TextView tv_info_types_choose;
    @BindView(R.id.view_info_bottom_types)
    View view_info_bottom_types;
    @BindView(R.id.iv_info_talk_choose)
    ImageView iv_info_talk_choose;
    @BindView(R.id.tv_info_talk_choose)
    TextView tv_info_talk_choose;
    @BindView(R.id.view_info_bottom_talk)
    View view_info_bottom_talk;
    //信息滑动布局
    @BindView(R.id.sv_class_info)
    ObservableScrollView sv_class_info;
    //课程信息所有布局
    @BindView(R.id.ll_info_first)
    LinearLayout ll_info_first;
    //选集上面的布局
    @BindView(R.id.ll_info_second)
    LinearLayout ll_info_second;
    @BindView(R.id.civ_user_photo_player)
    CircleImageView civ_user_photo_player;
    @BindView(R.id.tvLiveName)
    TextView tvLiveName;
    @BindView(R.id.tvLivePosition)
    TextView tvLivePosition;
    @BindView(R.id.tvLiveUserName)
    TextView tvLiveUserName;
    @BindView(R.id.tvViewNum)
    TextView tvViewNum;
    @BindView(R.id.tvLiveTime)
    TextView tvLiveTime;
    @BindView(R.id.tvLiveMoney)
    TextView tvLiveMoney;
    @BindView(R.id.ivIsConcern)
    ImageView ivIsConcern;
    @BindView(R.id.tvAppUserIntroduce)
    TextView tvAppUserIntroduce;
    @BindView(R.id.tvSmallClassIntroduce)
    TextView tvSmallClassIntroduce;
    @BindView(R.id.rv_class_types_list)
    RecyclerView rv_class_types_list;
    @BindView(R.id.ll_info_top_types)
    LinearLayout ll_info_top_types;
    @BindView(R.id.rv_sub_talks)
    RecyclerView rv_sub_talks;
    @BindView(R.id.refreshLayoutSmallComment)
    SmartRefreshLayout refreshLayoutSmallComment;
    /**
     * 表情按钮
     */
    @BindView(R.id.iv_enjoy_talk_class)
    ImageView iv_enjoy_talk_class;
    /**
     * 表情键盘布局
     */
    private EmotionMainFragment emotionMainFragment;
    @BindView(R.id.et_class_input_enjoy)
    EditText et_class_input_enjoy;
    /**
     * 底部评论布局
     */
    @BindView(R.id.ll_bottom_talk_player)
    LinearLayout ll_bottom_talk_player;


    /*--------------------变量设置----------------------------*/
    //视频播放地址
    private String mediaPlayerUrl = "http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4";
    //音频播放地址
    private String audioPlayerUrl = "http://115.28.95.41:8080/uploadfile/zhixian/201706/8cca1bf717284bb899b5bba21fb8ad41.mp3 ";
    //记录滑动坐标
    private int oldY, newY;
    //记录滑动的距离
    private int distanceSlide = 0;
    private String appUserId = "", smallClassId = "", videoId = "";
    private SmallDetailsPresenter smallDetailsPresenter;
    private SmallCommentPresenter smallCommentPresenter;
    private SmallCommentInsertPresenter insertPresenter;
    private ConcernInsertPresenter concernInsertPresenter;
    private ConcernCancelPresenter cancelPresenter;
    private String appUserConcern = "";
    private String intentType = "";
    private String specialColumnClassId = "";
    private SpecialDetailPresenter detailPresenter;
    private SpecialCommentPresenter commentPresenter;
    private SpecialCommentInsertPresenter specialCommentInsertPresenter;
    private String videoPath, viewNum, viewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_great_class_player);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();

    }

    /**
     * 初始化
     */
    private void initView() {
        //设置输入框在输入法上面
        View decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(HelperUtil.getGlobalLayoutListener(decorView, contentView));
        et_class_input_enjoy.setFocusable(true);
        et_class_input_enjoy.setFocusableInTouchMode(true);
        et_class_input_enjoy.requestFocus();
        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        im.showSoftInput(rl_activity_player_class_main, 0);
        initEmotionMainFragment();
        if (NetStateUtil.isWifi(GreatClassPlayerActivity.this)) {
            rl_choose_audio_video_view.setVisibility(View.GONE);
        } else {
            rl_choose_audio_video_view.setVisibility(View.VISIBLE);
        }
        refreshLayoutSmallComment.setNestedScrollingEnabled(false);
        refreshLayoutSmallComment.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                smallCommentPresenter.attachView(smallCommentView);
            }
        });

        intentType = getIntent().getStringExtra("intentType");
        appUserId = getIntent().getStringExtra("appUserId");
        if (intentType != null) {
            if (intentType.equals("class")) {
                smallClassId = getIntent().getStringExtra("smallClassId");
                videoId = getIntent().getStringExtra("videoId");
                videoPath = getIntent().getStringExtra("videoPath");
                viewNum = getIntent().getStringExtra("viewNum");
                viewId = getIntent().getStringExtra("viewId");
                if (videoPath != null) {
                    initPlayer("3", videoPath, viewNum, viewId);
                }
                initSmallDetails();
                initSmallComment();
            } else if (intentType.equals("specialColumn")) {
                specialColumnClassId = getIntent().getStringExtra("specialColumnClassId");
                specialDetail();
                initSpecialComment();
            }
        }
        initViewAnim();
    }

    /**
     * 专栏详情接口
     */
    private void specialDetail() {
        detailPresenter = new SpecialDetailPresenter(this);
        detailPresenter.onCreate();
        detailPresenter.attachView(detailView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("specialColumnClassId", specialColumnClassId);

        String data = HelperUtil.getParameter(map);
        detailPresenter.specialDetail(data);
    }

    private SpecialDetailView detailView = new SpecialDetailView() {
        @Override
        public void onSuccess(SpecialDetailResultBean specialDetailResultBean) {
            if (specialDetailResultBean.getResult().getResponseObject() != null) {
                initSpecialDetailData(specialDetailResultBean.getResult().getResponseObject());
                initPlayer("3", specialDetailResultBean.getResult().getResponseObject().getSpecialColumnClassVideo(), specialDetailResultBean.getResult().getResponseObject().getViewNum(), String.valueOf(specialDetailResultBean.getResult().getResponseObject().getSpecialColumnClassId()));
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SpecialDetailView" + result);
        }
    };

    /**
     * 加载专栏信息
     */
    private void initSpecialDetailData(SpecialDetailResultBean.ResultBean.ResponseObjectBean detailBean) {
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                civ_user_photo_player.setImageBitmap(resource);
            }
        };
        Glide.with(GreatClassPlayerActivity.this).asBitmap().load(detailBean.getAppUserPic()).into(target);
        tvLiveName.setText(detailBean.getSpecialColumnClassName());
        tvLivePosition.setText(detailBean.getPosition());
        tvLiveUserName.setText(detailBean.getAppUserName());
        if (detailBean.getIsConcern() == 0) {
            ivIsConcern.setImageResource(R.mipmap.add_attention_player);
        } else {
            ivIsConcern.setImageResource(R.mipmap.has_attention);
        }
        ivIsConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivIsConcern.getDrawable().getCurrent().getConstantState()
                        .equals(getResources().getDrawable(R.mipmap.add_attention_player).getConstantState())) {
                    concernInsert();
                } else {
                    concernCancel();
                }
            }
        });
        tvAppUserIntroduce.setText(detailBean.getAppUserIntroduce());
        tvSmallClassIntroduce.setText(detailBean.getSpecialColumnClassIntroduce());

    }

    /**
     * 加载专栏评论
     */
    private void initSpecialComment() {
        commentPresenter = new SpecialCommentPresenter(this);
        commentPresenter.onCreate();
        commentPresenter.attachView(commentView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("specialColumnClassId", specialColumnClassId);
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        String data = HelperUtil.getParameter(map);
        commentPresenter.specialComment(data);
    }

    private SpecialCommentView commentView = new SpecialCommentView() {
        @Override
        public void onSuccess(SpecialCommentResultBean specialCommentResultBean) {
            if (specialCommentResultBean.getResult().getResponseObject() != null
                    && specialCommentResultBean.getResult().getResponseObject().size() > 0) {
                initSpecialCommentList(specialCommentResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SpecialCommentView" + result);
        }
    };

    private void initSpecialCommentList(List<SpecialCommentResultBean.ResultBean.ResponseObjectBean> list) {
        SpecialCommentAdapter specialCommentAdapter = new SpecialCommentAdapter(GreatClassPlayerActivity.this, list, "2");
        rv_sub_talks.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_sub_talks.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
        rv_sub_talks.setAdapter(specialCommentAdapter);
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
        emotionMainFragment.bindToContentView(et_class_input_enjoy);
        emotionMainFragment.bindToEmotionButton(iv_enjoy_talk_class);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main_class, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    /**
     * 链接小课详情接口
     */
    private void initSmallDetails() {
        smallDetailsPresenter = new SmallDetailsPresenter(this);
        smallDetailsPresenter.onCreate();
        smallDetailsPresenter.attachView(smallDetailsView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("smallClassId", smallClassId);

        String data = HelperUtil.getParameter(map);
        smallDetailsPresenter.smallDetails(data);
    }

    /**
     * 链接小课评论列表接口
     */
    private void initSmallComment() {
        smallCommentPresenter = new SmallCommentPresenter(this);
        smallCommentPresenter.onCreate();
        smallCommentPresenter.attachView(smallCommentView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("videoId", "22");
        map.put("pageNow", "1");
        map.put("pageSize", "10");

        String data = HelperUtil.getParameter(map);
        smallCommentPresenter.smallComment(data);
    }

    /**
     * 链接添加小课评论
     */
    private void initCommentInsert(String text) {
        insertPresenter = new SmallCommentInsertPresenter(this);
        insertPresenter.onCreate();
        insertPresenter.attachView(insertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("videoId", "22");
        map.put("commentContent", text);

        String data = HelperUtil.getParameter(map);
        insertPresenter.smallCommentInsert(data);
    }

    /**
     * 关注
     */
    private void concernInsert() {
        concernInsertPresenter = new ConcernInsertPresenter(GreatClassPlayerActivity.this);
        concernInsertPresenter.onCreate();
        concernInsertPresenter.attachView(concernInsertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", getAppId());
        map.put("appUserConcern", appUserConcern);

        String data = HelperUtil.getParameter(map);
        concernInsertPresenter.concernInsert(data);
    }

    /**
     * 获取当前用户id
     */
    private String getAppId() {
        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
        return userBeanDao.loadAll().get(0).getAppUserId();
    }

    /**
     * 取消关注
     */
    private void concernCancel() {
        cancelPresenter = new ConcernCancelPresenter(GreatClassPlayerActivity.this);
        cancelPresenter.onCreate();
        cancelPresenter.attachView(cancelView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", getAppId());
        map.put("appUserConcern", appUserConcern);

        String data = HelperUtil.getParameter(map);
        cancelPresenter.concernCancel(data);
    }

    /**
     * 加载评论列表数据
     *
     * @param commentBeans
     */
    private void initCommentData(final List<SmallCommentResultBean.ResultBean.ResponseObjectBean> commentBeans) {
        SmallCommentAdapter smallCommentAdapter = new SmallCommentAdapter(GreatClassPlayerActivity.this, commentBeans, "1");
        rv_sub_talks.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_sub_talks.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
        rv_sub_talks.setAdapter(smallCommentAdapter);
    }

    /**
     * 加载初始化数据展示
     *
     * @param smallDetailsBean
     */
    private void initViewData(final SmallDetailsResultBean.ResultBean.ResponseObjectBean smallDetailsBean) {
        appUserConcern = smallDetailsBean.getAppUserId();

        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                civ_user_photo_player.setImageBitmap(resource);
            }
        };
        Glide.with(GreatClassPlayerActivity.this).asBitmap().load(smallDetailsBean.getAppUserPic()).into(target);
        tvLiveName.setText(smallDetailsBean.getSmallClassName());
        tvLivePosition.setText(smallDetailsBean.getPosition());
        tvLiveUserName.setText(smallDetailsBean.getAppUserName());
        if (smallDetailsBean.getIsConcern() == 0) {
            ivIsConcern.setImageResource(R.mipmap.add_attention_player);
        } else {
            ivIsConcern.setImageResource(R.mipmap.has_attention);
        }
        ivIsConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivIsConcern.getDrawable().getCurrent().getConstantState()
                        .equals(getResources().getDrawable(R.mipmap.add_attention_player).getConstantState())) {
                    concernInsert();
                } else {
                    concernCancel();
                }
            }
        });
        tvAppUserIntroduce.setText(smallDetailsBean.getAppUserIntroduce());
        tvSmallClassIntroduce.setText(smallDetailsBean.getSmallClassIntroduce());

        if (smallDetailsBean.getVideoList().size() > 1) {
            ll_info_top_types.setVisibility(View.VISIBLE);
            ll_info_second.setVisibility(View.VISIBLE);
            //选集
            TypesSelectAdapter typesSelectAdapter = new TypesSelectAdapter(GreatClassPlayerActivity.this, smallDetailsBean.getVideoList());
            rv_class_types_list.setLayoutManager(new LinearLayoutManager(this));
            //设置分割线
            rv_class_types_list.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
            rv_class_types_list.setAdapter(typesSelectAdapter);
            typesSelectAdapter.setOnItemClickListener(new TypesSelectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (!viewId.equals(String.valueOf(smallDetailsBean.getVideoList().get(position).getVideoId()))) {
                        viewId = String.valueOf(smallDetailsBean.getVideoList().get(position).getVideoId());
                        initPlayer("3", smallDetailsBean.getVideoList().get(position).getVideoPath(), smallDetailsBean.getVideoList().get(position).getViewNum(), String.valueOf(smallDetailsBean.getVideoList().get(position).getVideoId()));
                    }
                }
            });
        } else {
            ll_info_top_types.setVisibility(View.GONE);
            ll_info_second.setVisibility(View.GONE);
            tvViewNum.setText(smallDetailsBean.getVideoList().get(0).getViewNum());
            if (smallDetailsBean.getVideoList().get(0).getVideoMoney() != 0) {
                if (smallDetailsBean.getVideoList().get(0).getConfirmPay() == 0) {
                    tvLiveMoney.setText("￥" + smallDetailsBean.getVideoList().get(0).getVideoMoney() + "元");
                } else {
                    tvLiveMoney.setText("已购买");
                }
            }
        }
    }

    /*--------------视频播放----------------------*/

    /**
     * 加载视频播放器
     *
     * @param type          直播还是回放
     * @param url           地址
     * @param viewNum       观看人数
     * @param collectTypeId videoId
     */
    private void initPlayer(String type, String url, String viewNum, String collectTypeId) {
        observableScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        MediaPlayerController mediaPlayerController = new MediaPlayerController(GreatClassPlayerActivity.this, getAppId(), "2", collectTypeId, type, url, viewNum, rl_player_play, rl_activity_player_class_main);
        mediaPlayerController.init();
    }


    /*------------------------------选择页面的点击事件--------------------------------------------*/

    /**
     * 选择/后退
     */
    @OnClick({R.id.iv_choose_return_last, R.id.iv_return_audio_player})
    public void returnInChoose() {
        GreatClassPlayerActivity.this.finish();
    }

    /**
     * 选择/音频模式
     */
    @OnClick(R.id.ll_choose_audio_mode)
    public void audioInChoose() {
        ll_video_player_play.setVisibility(View.GONE);
        rl_choose_audio_video_view.setVisibility(View.GONE);
        ll_audio_player_play.setVisibility(View.VISIBLE);
        rl_audio_player_top.setVisibility(View.VISIBLE);
        AudioMediaPlayerControler audioMediaPlayerControler = new AudioMediaPlayerControler(GreatClassPlayerActivity.this, audioPlayerUrl, ll_audio_player_controller, rl_activity_player_class_main);
        audioMediaPlayerControler.initAudioPlayer();
    }

    /**
     * 选择/视频模式
     */
    @OnClick(R.id.ll_choose_video_mode)
    public void videoInChoose() {
        rl_choose_audio_video_view.setVisibility(View.GONE);
//        initPlayer();
    }

    /**
     * 课程选择
     */
    @OnClick(R.id.ll_info_top_class)
    public void classChoose() {
        sv_class_info.post(new Runnable() {
            @Override
            public void run() {
                Message messageClass = new Message();
                messageClass.what = 30;
                messageClass.obj = 0;
                handler.sendMessage(messageClass);
            }
        });

        classSelect();
    }

    /**
     * 选集选择
     */
    @OnClick(R.id.ll_info_top_types)
    public void typesChoose() {
        sv_class_info.post(new Runnable() {
            @Override
            public void run() {
                Message messageClass = new Message();
                messageClass.what = 30;
                messageClass.obj = ll_info_first.getMeasuredHeight();
                handler.sendMessage(messageClass);
            }
        });
        typesSelect();
    }

    /**
     * 评论选择
     */
    @OnClick(R.id.ll_info_top_talk)
    public void talkChoose() {
        observableScrollView.setEnabled(false);
        sv_class_info.post(new Runnable() {
            @Override
            public void run() {
                Message messageClass = new Message();
                messageClass.what = 30;
                messageClass.obj = ll_info_first.getMeasuredHeight() + ll_info_second.getMeasuredHeight();
                handler.sendMessage(messageClass);
            }
        });
        talkSelect();
    }

    /**
     * 发表
     */
    @OnClick(R.id.tv_release_talk)
    public void insertCommentRelease() {
        if (!TextUtils.isEmpty(et_class_input_enjoy.getText().toString())) {
            if (intentType.equals("class")) {
                initCommentInsert(et_class_input_enjoy.getText().toString());
            } else if (intentType.equals("specialColumn")) {
                specialCommentInsert(et_class_input_enjoy.getText().toString());
            }
        }
    }

    /**
     * 添加专栏评论
     */
    private void specialCommentInsert(String content) {
        specialCommentInsertPresenter = new SpecialCommentInsertPresenter(this);
        specialCommentInsertPresenter.onCreate();
        specialCommentInsertPresenter.attachView(specialCommentInsertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", getAppId());
        map.put("specialColumnClassId", specialColumnClassId);
        map.put("specialCommentContent", content);

        String data = HelperUtil.getParameter(map);
        specialCommentInsertPresenter.specialCommentInsert(data);

    }

    private SpecialCommentInsertView specialCommentInsertView = new SpecialCommentInsertView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    Toast.makeText(GreatClassPlayerActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    commentPresenter.attachView(commentView);
                    et_class_input_enjoy.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    Toast.makeText(GreatClassPlayerActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SpecialCommentInsertView" + result);
        }
    };


    /*----------------展示效果----------------------------*/

    /**
     * 滑动动画效果
     */
    private void initViewAnim() {
        observableScrollView.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > oldy) {
                    //向下滑动
                } else {
                    //向上滑动
                }
            }
        });

        sv_class_info.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y == 0) {
                    classSelect();
                }
                if (y > oldy) {
                    //向下滑动
                    if (y > ll_info_first.getMeasuredHeight()) {
                        typesSelect();
                    } else if (y > ll_info_first.getMeasuredHeight() + ll_info_second.getMeasuredHeight()) {
                        talkSelect();
                    }
                } else {
                    //向上滑动
                    if (y > ll_bottom_talk_player.getMeasuredHeight()) {
                        typesSelect();
                    } else if (y > ll_bottom_talk_player.getMeasuredHeight() + ll_info_second.getMeasuredHeight()) {
                        classSelect();
                    }
                }
            }
        });

    }

    /**
     * 课程选择的效果
     */
    private void classSelect() {
        iv_info_class_choose.setImageResource(R.mipmap.class_seleted);
        tv_info_class_choose.setTextColor(Color.parseColor("#2EA168"));
        view_info_bottom_class.setVisibility(View.VISIBLE);

        iv_info_types_choose.setImageResource(R.mipmap.types_unseleted);
        tv_info_types_choose.setTextColor(Color.parseColor("#666666"));
        view_info_bottom_types.setVisibility(View.GONE);

        iv_info_talk_choose.setImageResource(R.mipmap.talk_unseleted);
        tv_info_talk_choose.setTextColor(Color.parseColor("#666666"));
        view_info_bottom_talk.setVisibility(View.GONE);
    }

    /**
     * 选集选择的效果
     */
    public void typesSelect() {
        iv_info_class_choose.setImageResource(R.mipmap.class_unseleted);
        tv_info_class_choose.setTextColor(Color.parseColor("#666666"));
        view_info_bottom_class.setVisibility(View.GONE);

        iv_info_types_choose.setImageResource(R.mipmap.types_seleted);
        tv_info_types_choose.setTextColor(Color.parseColor("#2EA168"));
        view_info_bottom_types.setVisibility(View.VISIBLE);

        iv_info_talk_choose.setImageResource(R.mipmap.talk_unseleted);
        tv_info_talk_choose.setTextColor(Color.parseColor("#666666"));
        view_info_bottom_talk.setVisibility(View.GONE);
    }

    /**
     * 评论选择的效果
     */
    public void talkSelect() {
        iv_info_class_choose.setImageResource(R.mipmap.class_unseleted);
        tv_info_class_choose.setTextColor(Color.parseColor("#666666"));
        view_info_bottom_class.setVisibility(View.GONE);

        iv_info_types_choose.setImageResource(R.mipmap.types_unseleted);
        tv_info_types_choose.setTextColor(Color.parseColor("#666666"));
        view_info_bottom_types.setVisibility(View.GONE);

        iv_info_talk_choose.setImageResource(R.mipmap.talk_seleted);
        tv_info_talk_choose.setTextColor(Color.parseColor("#2EA168"));
        view_info_bottom_talk.setVisibility(View.VISIBLE);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 30:
                    sv_class_info.scrollTo(0, (int) msg.obj);
                    break;
                case 31:
                    ivIsConcern.setImageResource(R.mipmap.has_attention);
                    break;
                case 32:
                    ivIsConcern.setImageResource(R.mipmap.add_attention_player);
                    break;
            }
        }
    };

    /**
     * 小课详情
     */
    private SmallDetailsView smallDetailsView = new SmallDetailsView() {
        @Override
        public void onSuccess(SmallDetailsResultBean smallDetailsResultBean) {
            if (smallDetailsResultBean.getResult().getResponseObject() != null) {
                initViewData(smallDetailsResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "smallDetailsView:==" + result);
        }
    };

    /**
     * 小课评论
     */
    private SmallCommentView smallCommentView = new SmallCommentView() {
        @Override
        public void onSuccess(SmallCommentResultBean smallCommentResultBean) {
            if (smallCommentResultBean.getResult().getResponseObject() != null) {
                initCommentData(smallCommentResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "smallCommentView:==" + result);
        }
    };

    /**
     * 添加小课评论
     */
    private SmallCommentInsertView insertView = new SmallCommentInsertView() {
        @Override
        public void onSuccess(SmallCommentInsertResultBean smallCommentInsertResultBean) {
            if (smallCommentInsertResultBean != null) {
                if (smallCommentInsertResultBean.getRet().equals("1")) {
                    initSmallComment();
                    et_class_input_enjoy.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    Toast.makeText(GreatClassPlayerActivity.this, "发表失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "insertView:==" + result);
        }
    };

    /**
     * 关注
     */
    private ConcernInsertView concernInsertView = new ConcernInsertView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    handler.sendEmptyMessage(31);
                } else {
                    Toast.makeText(GreatClassPlayerActivity.this, "关注失败!", Toast.LENGTH_SHORT).show();
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
    private ConcernCancelView cancelView = new ConcernCancelView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean.getRet().equals("1")) {
                handler.sendEmptyMessage(32);
            } else {
                Toast.makeText(GreatClassPlayerActivity.this, "取消关注失败!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "cancelView:==" + result);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.finish();
        MediaPlayerController.stopIjk();
        return super.onKeyDown(keyCode, event);
    }
}
