package com.tch.zx.activity.line.greatclass;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.contacts.CommentDynamicAdapter;
import com.tch.zx.adapter.line.TypesSelectAdapter;
import com.tch.zx.enjoy.fragment.EmotionMainFragment;
import com.tch.zx.http.bean.result.GetDynamicCommentListResult;
import com.tch.zx.http.bean.result.LiveDetailsResultBean;
import com.tch.zx.media.AudioMediaPlayerControler;
import com.tch.zx.media.MediaPlayerController;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.ObservableScrollView;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * 精品小课/音视频播放
 */
public class GreatClassItemPlayerActivity extends BaseActivity {
    /**
     * 选集集合
     */
    @BindView(R.id.rv_class_types_list)
    RecyclerView rv_class_types_list;
    /**
     * 评论集合
     */
    @BindView(R.id.rv_sub_talks)
    RecyclerView rv_sub_talks;
    /**
     * 视频播放器布局
     */
    @BindView(R.id.rl_player_play)
    RelativeLayout rl_player_play;
    /**
     * 要展示dialog的父布局
     */
    @BindView(R.id.rl_great_class_item_player_parent)
    RelativeLayout rl_great_class_item_player_parent;
    /**
     * 选择音视频的父布局
     */
    @BindView(R.id.rl_choose_audio_video_view)
    RelativeLayout rl_choose_audio_video_view;
    /**
     * 音频图片父布局
     */
    @BindView(R.id.rl_audio_player_top)
    RelativeLayout rl_audio_player_top;
    /**
     * 音频控制器布局
     */
    @BindView(R.id.ll_audio_controller_slide)
    LinearLayout ll_audio_controller_slide;
    /**
     * 介绍信息的布局
     */
    @BindView(R.id.ll_great_class_content_parent)
    LinearLayout ll_great_class_content_parent;
    /**
     * 外部滑动布局
     */
    @BindView(R.id.sv_parent_set)
    ScrollView sv_parent_set;
    /**
     * 子滑动布局
     */
    @BindView(R.id.sv_class_info)
    ObservableScrollView sv_class_info;
    /**
     * 评论输入框
     */
    @BindView(R.id.et_class_input_enjoy)
    EditText et_class_input_enjoy;
    /**
     * 表情按钮
     */
    @BindView(R.id.iv_enjoy_talk_class)
    ImageView iv_enjoy_talk_class;
    /**
     * 表情键盘布局
     */
    @BindView(R.id.fl_emotionview_main_class)
    FrameLayout fl_emotionview_main_class;
    private EmotionMainFragment emotionMainFragment;
    /**
     * 模拟数据
     */
    private List<GetDynamicCommentListResult.ResponseObjectBean> list;
    /**
     * 视频地址
     */
    private String videoUrl = "http://baobab.wdjcdn.com/1456665467509qingshu.mp4";
    /**
     * 音频地址
     */
    private String audioUrl = "http://115.28.95.41:8080/uploadfile/zhixian/201706/8cca1bf717284bb899b5bba21fb8ad41.mp3";

    private String appUserId, smallClassId;

    private float y1 = 0;
    private float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_great_class_item_player);
        ButterKnife.bind(this);

        list = new ArrayList<GetDynamicCommentListResult.ResponseObjectBean>();

        setTypesList();
        setTalkList();

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
        sv_class_info.requestDisallowInterceptTouchEvent(true);
        sv_parent_set.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return true;
            }
        });
        sv_class_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    y2 = event.getY();
                    if (y1 - y2 > 10) {
                        //向上滑

                    } else if (y2 - y1 > 10) {
                        //向下滑

                    }
                }
                return false;
            }
        });
        initDatas();
    }

    /**
     * 加载视频播放器
     */
    private void initPlayer(String type, String url, String viewNum) {
        //http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8
        //http://baobab.wdjcdn.com/1456665467509qingshu.mp4
//        MediaPlayerController mediaPlayerController = new MediaPlayerController(this, type, url, viewNum, rl_player_play, rl_great_class_item_player_parent);
//        mediaPlayerController.init();
    }

    /**
     * 选择/后退
     */
    @OnClick({R.id.iv_choose_return_last, R.id.iv_return_audio_player})
    public void returnInChoose() {
        this.finish();
    }

    /**
     * 选择/音频模式
     */
    @OnClick(R.id.ll_choose_audio_mode)
    public void audioInChoose() {
        rl_choose_audio_video_view.setVisibility(View.GONE);
        rl_player_play.setVisibility(View.GONE);
        rl_audio_player_top.setVisibility(View.VISIBLE);
        ll_audio_controller_slide.setVisibility(View.VISIBLE);

        addAnimator();

        AudioMediaPlayerControler audioMediaPlayerControler = new AudioMediaPlayerControler(this, audioUrl, ll_audio_controller_slide, rl_great_class_item_player_parent);
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
     * 加载选集数据
     */
    private void setTypesList() {
//        TypesSelectAdapter typesSelectAdapter = new TypesSelectAdapter(this, list);
//        rv_class_types_list.setLayoutManager(new LinearLayoutManager(this));
//        //设置分割线
//        rv_class_types_list.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
//        rv_class_types_list.setAdapter(typesSelectAdapter);
    }

    /**
     * 加载评论数据
     */
    private void setTalkList() {
        CommentDynamicAdapter commentDynamicAdapter = new CommentDynamicAdapter(this, list);
        rv_sub_talks.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_sub_talks.addItemDecoration(new RecyclerViewDecoration(this, "#949494", 1, false));
        rv_sub_talks.setAdapter(commentDynamicAdapter);
    }


    /**
     * 添加音频上滑效果
     */
    private void addAnimator() {
        sv_parent_set.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });
        sv_parent_set.post(
                new Runnable() {
                    public void run() {
                        //sv_container.fullScroll(ScrollView.FOCUS_UP);
                        sv_parent_set.scrollTo(0, 0);
                    }
                });
    }

    /**
     * 初始化布局数据
     */
    private void initDatas() {
        et_class_input_enjoy.setFocusable(true);
        et_class_input_enjoy.setFocusableInTouchMode(true);
        et_class_input_enjoy.requestFocus();
        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        im.showSoftInput(et_class_input_enjoy, 0);
        initEmotionMainFragment();
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
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotionview_main_class, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    /**
     * 初始化监听器
     */
    public void initListentener() {

    }

    @Override
    public void onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }

}
