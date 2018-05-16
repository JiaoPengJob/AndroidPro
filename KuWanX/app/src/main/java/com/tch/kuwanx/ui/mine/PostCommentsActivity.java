package com.tch.kuwanx.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.CommentsImg;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.EvaluateTagsResult;
import com.tch.kuwanx.result.UpdateImg;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.UIProgressResponseCallBack;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 发布评价
 */
public class PostCommentsActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.ivResPhoto)
    ImageView ivResPhoto;
    @BindView(R.id.ivPraise)
    ImageView ivPraise;
    @BindView(R.id.tvPraise)
    TextView tvPraise;
    @BindView(R.id.ivAverage)
    ImageView ivAverage;
    @BindView(R.id.tvAverage)
    TextView tvAverage;
    @BindView(R.id.ivBadReview)
    ImageView ivBadReview;
    @BindView(R.id.tvBadReview)
    TextView tvBadReview;
    @BindView(R.id.rvCommentPhotos)
    RecyclerView rvCommentPhotos;
    @BindView(R.id.postCommentFlow)
    TagFlowLayout postCommentFlow;
    @BindView(R.id.depictRatingBar)
    ScaleRatingBar depictRatingBar;
    @BindView(R.id.logisticsRatingBar)
    ScaleRatingBar logisticsRatingBar;
    @BindView(R.id.attitudeRatingBar)
    ScaleRatingBar attitudeRatingBar;
    @BindView(R.id.etPostComment)
    EditText etPostComment;
    @BindView(R.id.llPostComments)
    LinearLayout llPostComments;

    private CommonAdapter commentPhotosAdapter;
    private String orderId, postUserId, swapUserId, cdImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("发布评价");
        btTitleFeatures.setText("发布");
        btTitleFeatures.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(getIntent().getStringExtra("orderId"))) {
            orderId = getIntent().getStringExtra("orderId");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("postUserId"))) {
            postUserId = getIntent().getStringExtra("postUserId");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("swapUserId"))) {
            swapUserId = getIntent().getStringExtra("swapUserId");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("cdImg"))) {
            cdImg = getIntent().getStringExtra("cdImg");
            Glide.with(PostCommentsActivity.this)
                    .load(cdImg)
                    .into(ivResPhoto);
        } else {
            ivResPhoto.setImageResource(R.mipmap.app_icon);
        }

        initCommentPhotos();
        initRatingBarStyle(depictRatingBar);
        initRatingBarStyle(logisticsRatingBar);
        initRatingBarStyle(attitudeRatingBar);
        initStarsClick();
        etPostComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
                if ((view.getId() == R.id.etPostComment && Utils.canVerticalScroll(etPostComment))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        evaluateTagsHttp();
    }

    private int depictStars = 3, logisticsStars = 3, attitudeStars = 3;

    /**
     * 五角星选择
     */
    private void initStarsClick() {
        depictRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                depictStars = (int) rating;
            }
        });
        logisticsRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                logisticsStars = (int) rating;
            }
        });
        attitudeRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                attitudeStars = (int) rating;
            }
        });
    }

    /**
     * 设置五角星样式
     *
     * @param scaleRatingBar
     */
    private void initRatingBarStyle(ScaleRatingBar scaleRatingBar) {
        scaleRatingBar.setNumStars(5);
        scaleRatingBar.setRating(0);
        scaleRatingBar.setStarPadding(10);
        scaleRatingBar.setStepSize(1);
        scaleRatingBar.setTouchable(true);
        scaleRatingBar.setClearRatingEnabled(true);
    }

    /**
     * 加载评论图片
     */
    private void initCommentPhotos() {
        rvCommentPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        rvCommentPhotos.setAdapter(commentPhotosAdapter = new CommonAdapter<CommentsImg>(this, R.layout.item_img_post,
                new ArrayList<CommentsImg>()) {
            @Override
            protected void convert(ViewHolder holder, CommentsImg item, int position) {
                ImageView img = (ImageView) holder.getView(R.id.ivPostImg);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((Utils.getScreenWidth(PostCommentsActivity.this) - 60) / 3,
                        (Utils.getScreenWidth(PostCommentsActivity.this) - 60) / 3);
                lp.setMargins(5, 5, 5, 5);
                img.setLayoutParams(lp);
                Glide.with(PostCommentsActivity.this)
                        .load(item.getPath())
                        .into(img);
            }
        });

        commentPhotosAdapter.getDatas().clear();
        commentPhotosAdapter.getDatas().add(new CommentsImg(true, Utils.getDrawableResPath(PostCommentsActivity.this, R.drawable.add_photo)));
        commentPhotosAdapter.notifyDataSetChanged();

        commentPhotosAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CommentsImg ci = (CommentsImg) commentPhotosAdapter.getDatas().get(position);
                if (ci.isSkip()) {
                    showSel();
                } else {
                    ArrayList<String> imgs = new ArrayList<>();
                    for (CommentsImg cImg : (List<CommentsImg>) commentPhotosAdapter.getDatas()) {
                        imgs.add(cImg.getPath());
                    }
                    showGallery(imgs, position);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CustomPopWindow mAvatarPop;
    private Button btTakeAvatar, btChooseAvatar, btExitAvatar;

    private void showSel() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_avatar, null);
        btTakeAvatar = (Button) view.findViewById(R.id.btTakeAvatar);
        btChooseAvatar = (Button) view.findViewById(R.id.btChooseAvatar);
        btExitAvatar = (Button) view.findViewById(R.id.btExitAvatar);
        btTakeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAvatar();
                if (mAvatarPop != null) {
                    mAvatarPop.dissmiss();
                }
            }
        });
        btChooseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotos();
                if (mAvatarPop != null) {
                    mAvatarPop.dissmiss();
                }
            }
        });
        btExitAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAvatarPop != null) {
                    mAvatarPop.dissmiss();
                }
            }
        });
        mAvatarPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(llPostComments, Gravity.BOTTOM, 0, 0);
    }

    private void takeAvatar() {
        Album.camera(this) // 相机功能。
                .image() // 拍照。
                .requestCode(10)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        if (requestCode == 10) {
                            updateImg(result);
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                    }
                })
                .start();
    }

    private int count = 0;

    private void selectPhotos() {
        for (CommentsImg cImg : (List<CommentsImg>) commentPhotosAdapter.getDatas()) {
            if (!cImg.isSkip()) {
                count += 1;
            }
        }
        Album.image(PostCommentsActivity.this) // 选择图片。
                .multipleChoice()
                .requestCode(100)
                .camera(false)
                .columnCount(4)
//                .selectCount(6 - count)
                .selectCount(1)
                .widget(
                        Widget.newLightBuilder(this)
                                .title("相册") // 标题。
                                .statusBarColor(Color.parseColor("#FFDA44")) // 状态栏颜色。
                                .toolBarColor(Color.parseColor("#FFDA44")) // Toolbar颜色。
                                .navigationBarColor(Color.parseColor("#FFDA44")) // Android5.0+的虚拟导航栏颜色。
                                .mediaItemCheckSelector(Color.parseColor("#FFDA44"), Color.parseColor("#FFDA44")) // 图片或者视频选择框的选择器。
                                .bucketItemCheckSelector(Color.parseColor("#FFDA44"), Color.parseColor("#FFDA44")) // 切换文件夹时文件夹选择框的选择器。
                                .buttonStyle( // 用来配置当没有发现图片/视频时的拍照按钮和录视频按钮的风格。
                                        Widget.ButtonStyle.newLightBuilder(this) // 同Widget的Builder模式。
                                                .setButtonSelector(Color.WHITE, Color.parseColor("#FFDA44")) // 按钮的选择器。
                                                .build()
                                )
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(final int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        if (requestCode == 100) {
                            for (final AlbumFile albumFile : result) {
                                luban(albumFile.getPath());
                            }
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                    }
                })
                .start();
    }

    private void luban(String path) {
        String sdCardDir = Environment.getExternalStorageDirectory().getPath() + "/imgs/";
        Luban.with(this)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(300)                                  // 忽略不压缩图片的大小
                .setTargetDir(sdCardDir)                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        updateImg(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    /**
     * 上传图片
     *
     * @param path
     */
    private void updateImg(String path) {
        final UIProgressResponseCallBack listener = new UIProgressResponseCallBack() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                int progress = (int) (bytesRead * 100 / contentLength);
                if (done) {
                    Logger.e("开始执行...");
                }
            }
        };
        final File file = new File(path);
        EasyHttp.post(HttpUtils.URI_CENTER + "pic/upload.jhtml")
                .params("userId", DaoUtils.getUserId(PostCommentsActivity.this))
                .params("avatar", file, file.getName(), listener)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .cacheKey(this.getClass().getSimpleName() + "_upload")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("上传失败");
                    }

                    @Override
                    public void onSuccess(String response) {
                        UpdateImg updateImg =
                                (UpdateImg) GsonUtil.json2Object(response, UpdateImg.class);
                        if (updateImg != null &&
                                updateImg.getRet().equals("1")) {
//                            if (commentPhotosAdapter.getDatas().size() < 6) {
                            commentPhotosAdapter.getDatas().add(commentPhotosAdapter.getDatas().size() - 1,
                                    new CommentsImg(false, updateImg.getErrorstring()));
                            commentPhotosAdapter.notifyDataSetChanged();
//                            }

                            Utils.deleteFiles(file);
                        } else {
                            Logger.e("上传失败");
                        }
                    }
                });
    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void updateImg(File file) {
        final UIProgressResponseCallBack listener = new UIProgressResponseCallBack() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                int progress = (int) (bytesRead * 100 / contentLength);
                if (done) {
                    Logger.e("开始执行...");
                }
            }
        };
        EasyHttp.post(HttpUtils.URI_CENTER + "pic/upload.jhtml")
                .params("userId", DaoUtils.getUserId(PostCommentsActivity.this))
                .params("avatar", file, file.getName(), listener)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .cacheKey(this.getClass().getSimpleName() + "_upload")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("上传失败");
                    }

                    @Override
                    public void onSuccess(String response) {
                        UpdateImg updateImg =
                                (UpdateImg) GsonUtil.json2Object(response, UpdateImg.class);
                        if (updateImg != null &&
                                updateImg.getRet().equals("1")) {
//                            if (commentPhotosAdapter.getDatas().size() < 6) {
                            commentPhotosAdapter.getDatas().add(commentPhotosAdapter.getDatas().size() - 1,
                                    new CommentsImg(false, updateImg.getErrorstring()));
                            commentPhotosAdapter.notifyDataSetChanged();
//                            }
                        } else {
                            Logger.e("上传失败");
                        }
                    }
                });
    }

    /**
     * 展示大图画廊
     */
    private void showGallery(ArrayList<String> list, int position) {
        Album.gallery(this)
                .requestCode(10) // 请求码，会在listener中返回。
                .checkedList(list) // 要浏览的图片列表：ArrayList<String>。
                .currentPosition(position)
                .navigationAlpha(250) // Android5.0+的虚拟导航栏的透明度。
                .checkable(false) // 是否有浏览时的选择功能。
                .widget(
                        Widget.newDarkBuilder(PostCommentsActivity.this)
                                .title("预览")
                                .build()
                )
                .start();
    }

    private List<String> flowList = new ArrayList<>();
    private List<String> flowSelList = new ArrayList<>();
    private TextView tvTabShow;

    /**
     * 加载流式布局
     */
    private void initFlowLayoutData() {
        postCommentFlow.setAdapter(new TagAdapter<String>(flowList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                tvTabShow = (TextView) LayoutInflater.from(PostCommentsActivity.this).inflate(R.layout.tv, postCommentFlow, false);
                tvTabShow.setText(s);
                return tvTabShow;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                ((TextView) view).setTextColor(Color.parseColor("#666666"));
            }
        });
        postCommentFlow.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                flowSelList = new ArrayList<String>();
                for (Integer index : selectPosSet) {
                    flowSelList.add(flowList.get(index));
                }
            }
        });
    }

    private String evaluate_level = "1";

    /**
     * 好评
     */
    @OnClick(R.id.llPraise)
    public void praise() {
        ivPraise.setImageResource(R.drawable.praise);
        tvPraise.setTextColor(Color.parseColor("#F7C705"));
        ivAverage.setImageResource(R.drawable.average_unsel);
        tvAverage.setTextColor(Color.parseColor("#BFBDBC"));
        ivBadReview.setImageResource(R.drawable.bad_review_unsel);
        tvBadReview.setTextColor(Color.parseColor("#BFBDBC"));
        evaluate_level = "1";
    }

    /**
     * 中评
     */
    @OnClick(R.id.llAverage)
    public void average() {
        ivPraise.setImageResource(R.drawable.praise_unsel);
        tvPraise.setTextColor(Color.parseColor("#BFBDBC"));
        ivAverage.setImageResource(R.drawable.average_sel);
        tvAverage.setTextColor(Color.parseColor("#F7C705"));
        ivBadReview.setImageResource(R.drawable.bad_review_unsel);
        tvBadReview.setTextColor(Color.parseColor("#BFBDBC"));
        evaluate_level = "2";
    }

    /**
     * 差评
     */
    @OnClick(R.id.llBadReview)
    public void badReview() {
        ivPraise.setImageResource(R.drawable.praise_unsel);
        tvPraise.setTextColor(Color.parseColor("#BFBDBC"));
        ivAverage.setImageResource(R.drawable.average_unsel);
        tvAverage.setTextColor(Color.parseColor("#BFBDBC"));
        ivBadReview.setImageResource(R.drawable.bad_review);
        tvBadReview.setTextColor(Color.parseColor("#F7C705"));
        evaluate_level = "3";
    }

    /**
     * 发布
     */
    @OnClick(R.id.btTitleFeatures)
    public void submitComment() {
        List<String> imgList = new ArrayList<>();
        for (CommentsImg cImg : (List<CommentsImg>) commentPhotosAdapter.getDatas()) {
            if (!cImg.isSkip()) {
                imgList.add(cImg.getPath());
            }
        }
        if (depictStars != 0 && logisticsStars != 0 && attitudeStars != 0
                && flowSelList.size() > 0
                && imgList.size() > 0
                && !TextUtils.isEmpty(etPostComment.getText().toString())) {
            if (DaoUtils.getUserId(PostCommentsActivity.this).equals(postUserId)) {
                addSwapOrderEvaluateHttp(swapUserId, DaoUtils.getUserId(PostCommentsActivity.this),
                        Utils.join(flowSelList, ","),
                        Utils.join(imgList, ","));
            } else if (DaoUtils.getUserId(PostCommentsActivity.this).equals(swapUserId)) {
                addSwapOrderEvaluateHttp(postUserId, DaoUtils.getUserId(PostCommentsActivity.this),
                        Utils.join(flowSelList, ","),
                        Utils.join(imgList, ","));
            }
        }else{
            Toasty.warning(PostCommentsActivity.this, "发布信息不完整！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void postCommentsBack() {
        PostCommentsActivity.this.finish();
    }

    /**
     * 评价
     */
    private void addSwapOrderEvaluateHttp(String appraiser, String evaluate_id, String labels, String imgs) {
        Map<String, Object> map = new HashMap<>();
        map.put("swap_order_id", orderId);
        map.put("appraiser", appraiser);
        map.put("evaluate_level", evaluate_level);
        map.put("evaluate_content", etPostComment.getText().toString());
        map.put("baby_evaluation", String.valueOf(depictStars));
        map.put("communication_evaluation", String.valueOf(attitudeStars));
        map.put("delivery_evaluation", String.valueOf(logisticsStars));
        map.put("label_id", labels);
        map.put("evaluate_id", evaluate_id);
        map.put("img_url", imgs);
        String params = EncryptionUtil.getParameter(PostCommentsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/addSwapOrderEvaluate.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addSwapOrderEvaluate")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        PostCommentsActivity.this, "发布中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(PostCommentsActivity.this, "发布失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {

                    }
                });
    }

    /**
     * 获取评价标签
     */
    private void evaluateTagsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        String params = EncryptionUtil.getParameter(PostCommentsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/evaluateTags.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_evaluateTags")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("获取标签失败");
                    }

                    @Override
                    public void onSuccess(String response) {
                        EvaluateTagsResult evaluateTagsResult =
                                (EvaluateTagsResult) GsonUtil.json2Object(response, EvaluateTagsResult.class);
                        if (evaluateTagsResult != null
                                && evaluateTagsResult.getRet().equals("1")) {
                            flowList.clear();
                            flowList.addAll(evaluateTagsResult.getResult());
                            initFlowLayoutData();
                        } else {
                            Logger.e("获取标签失败");
                        }
                    }
                });
    }

}
