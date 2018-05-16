package com.tch.kuwanx.ui.mine.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AddUserCdsResult;
import com.tch.kuwanx.result.GetSysCdsResult;
import com.tch.kuwanx.result.SysCdsByIdResult;
import com.tch.kuwanx.result.UpdateImg;
import com.tch.kuwanx.ui.release.TakePhotosActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GlideImageLoader;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.UIProgressResponseCallBack;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.Disposable;

/**
 * 添加物品
 */
public class AddItemsActivity extends Activity implements OnRefreshLoadmoreListener {

    @BindView(R.id.etItemName)
    EditText etItemName;
    @BindView(R.id.rvAddItems)
    RecyclerView rvAddItems;
    @BindView(R.id.llItemsFirstView)
    LinearLayout llItemsFirstView;
    @BindView(R.id.bannerItems)
    Banner bannerItems;
    @BindView(R.id.rvItemsAddPhotos)
    RecyclerView rvItemsAddPhotos;
    @BindView(R.id.rlItemsSecondView)
    RelativeLayout rlItemsSecondView;
    @BindView(R.id.refreshAddItems)
    SmartRefreshLayout refreshAddItems;

    private CommonAdapter itemsAdapter;
    private CommonAdapter itemsPhotoAdapter;
    private boolean isMore = false;
    private int size = 10, pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_add_items);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initEditStyle();
        initItemsData();
        refreshAddItems.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        if (!TextUtils.isEmpty(etItemName.getText().toString())) {
            pageIndex += 1;
            getSysCdsHttp();
        } else {
            if (refreshAddItems != null) {
                refreshAddItems.finishLoadmore();
                refreshAddItems.finishRefresh();
            }
            Toasty.warning(AddItemsActivity.this, "请输入搜索的内容！", Toast.LENGTH_SHORT, false).show();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        if (!TextUtils.isEmpty(etItemName.getText().toString())) {
            pageIndex = 1;
            getSysCdsHttp();
        } else {
            if (refreshAddItems != null) {
                refreshAddItems.finishLoadmore();
                refreshAddItems.finishRefresh();
            }
            Toasty.warning(AddItemsActivity.this, "请输入搜索的内容！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 设置输入框样式
     */
    private void initEditStyle() {
        etItemName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    //开始搜索
                    if (!TextUtils.isEmpty(etItemName.getText().toString())) {
                        getSysCdsHttp();
                    } else {
                        Toasty.warning(AddItemsActivity.this, "请输入搜索的内容！", Toast.LENGTH_SHORT, false).show();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 添加物品的搜索物品
     */
    private void getSysCdsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(AddItemsActivity.this));
        map.put("search", etItemName.getText().toString());
        map.put("a", "1");
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(pageIndex));
        String params = EncryptionUtil.getParameter(AddItemsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getSysCds.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getSysCds")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        AddItemsActivity.this, "搜索中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshAddItems != null) {
                            refreshAddItems.finishLoadmore();
                            refreshAddItems.finishRefresh();
                        }
                        Toasty.warning(AddItemsActivity.this, "搜索失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshAddItems != null) {
                            refreshAddItems.finishLoadmore();
                            refreshAddItems.finishRefresh();
                        }

                        GetSysCdsResult sysCdsResult =
                                (GetSysCdsResult) GsonUtil.json2Object(response, GetSysCdsResult.class);
                        if (sysCdsResult != null
                                && sysCdsResult.getRet().equals("1")) {
                            if (sysCdsResult.getResult() != null
                                    && sysCdsResult.getResult().size() > 0) {
                                if (isMore) {
                                    itemsAdapter.getDatas().addAll(sysCdsResult.getResult());
                                    itemsAdapter.notifyDataSetChanged();
                                } else {
                                    itemsAdapter.getDatas().clear();
                                    itemsAdapter.getDatas().addAll(sysCdsResult.getResult());
                                    itemsAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toasty.warning(AddItemsActivity.this, "未搜索到数据！", Toast.LENGTH_SHORT, false).show();
                            }
                        } else {
                            Toasty.warning(AddItemsActivity.this, "未搜索到数据！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 加载数据
     */
    private void initItemsData() {
        rvAddItems.setLayoutManager(new LinearLayoutManager(this));
        rvAddItems.setAdapter(itemsAdapter = new CommonAdapter<GetSysCdsResult.ResultBean>(this,
                R.layout.item_items_add, new ArrayList<GetSysCdsResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, GetSysCdsResult.ResultBean item, int position) {
                holder.setText(R.id.tvAddItem, item.getName());
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(AddItemsActivity.this)
                            .load(item.getHeadpic())
                            .into((ImageView) holder.getView(R.id.ivAddItemImg));
                } else {
                    holder.setImageResource(R.id.ivAddItemImg, R.drawable.placeholder);
                }
            }
        });
        itemsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                cdId = ((List<GetSysCdsResult.ResultBean>) itemsAdapter.getDatas()).get(position).getCdid();
                getSysCdsByIdHttp(
                        ((List<GetSysCdsResult.ResultBean>) itemsAdapter.getDatas()).get(position).getCdid()
                );
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private String cdId = "";

    /**
     * 显示第二个布局
     */
    private void hideFirstView() {
        llItemsFirstView.startAnimation(AnimationUtils.makeOutAnimation(AddItemsActivity.this, false));
        llItemsFirstView.setVisibility(View.GONE);
        rlItemsSecondView.startAnimation(AnimationUtils.makeInAnimation(AddItemsActivity.this, false));
        rlItemsSecondView.setVisibility(View.VISIBLE);
        initBannerData();
        initItemsPhotos();
    }

    /**
     * 显示第一个布局
     */
    private void showFirstView() {
        llItemsFirstView.startAnimation(AnimationUtils.makeInAnimation(AddItemsActivity.this, true));
        llItemsFirstView.setVisibility(View.VISIBLE);
        rlItemsSecondView.startAnimation(AnimationUtils.makeOutAnimation(AddItemsActivity.this, true));
        rlItemsSecondView.setVisibility(View.GONE);
    }

    private ArrayList<String> imgs = new ArrayList<>();

    /**
     * 设置banner
     */
    private void initBannerData() {
        imgs.clear();
        for (int i = 0; i < sysCdsByIdResult.getResult().get(0).getImgList().size(); i++) {
            imgs.add(sysCdsByIdResult.getResult().get(0).getImgList().get(i).getPicpath());
        }

        //设置banner样式
        bannerItems.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        bannerItems.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        bannerItems.setBannerAnimation(Transformer.Default);
        //设置是否允许手动滑动轮播图
        bannerItems.setViewPagerIsScroll(true);
        //设置自动轮播，默认为true
        bannerItems.isAutoPlay(true);
        //设置轮播时间
        bannerItems.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        bannerItems.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片集合
        bannerItems.setImages(imgs);
        //banner设置方法全部调用完毕时最后调用
        bannerItems.start();
        //开始轮播
        bannerItems.startAutoPlay();
        //点击事件监听
        bannerItems.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });

        tvItemsAddContent.setText(sysCdsByIdResult.getResult().get(0).getDetail());
    }

    @BindView(R.id.tvItemsAddContent)
    TextView tvItemsAddContent;
    @BindView(R.id.etItemsAddInput)
    EditText etItemsAddInput;

    /**
     * 加载图片数据
     */
    private void initItemsPhotos() {
        if (imgs.size() < 9) {
            //此处应该修改添加图片的图标
            imgs.add(Utils.getDrawableResPath(AddItemsActivity.this, R.drawable.add_photo));
        }

        rvItemsAddPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        rvItemsAddPhotos.setAdapter(itemsPhotoAdapter = new CommonAdapter<String>(this,
                R.layout.item_article_info_img, imgs) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                ImageView imgView = (ImageView) holder.getView(R.id.ivArticleInfoPhoto);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((Utils.getScreenWidth(AddItemsActivity.this) - 220) / 3
                        , (Utils.getScreenWidth(AddItemsActivity.this) - 220) / 3);
                lp.setMargins(2, 4, 2, 4);
                imgView.setLayoutParams(lp);
                imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(AddItemsActivity.this)
                        .load(item)
                        .into(imgView);
            }
        });
        itemsPhotoAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == imgs.size() - 1) {
                    imgs.remove(imgs.size() - 1);
                    Intent intent = new Intent(AddItemsActivity.this, TakePhotosActivity.class);
                    intent.putStringArrayListExtra("imgs", imgs);
                    startActivityForResult(intent, 10);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 展示大图画廊
     */
    private void showGallery(int position) {
        Album.gallery(this)
                .requestCode(100)
                .checkedList(imgs)
                .currentPosition(position)
                .navigationAlpha(250)
                .checkable(false)
                .widget(
                        Widget.newDarkBuilder(AddItemsActivity.this)
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 添加
     */
    @OnClick(R.id.btItemsAdd)
    public void itemsAdd() {
        if (!TextUtils.isEmpty(etItemsAddInput.getText().toString())) {
            if (ifUpdate
                    && imgResult != null
                    && imgResult.size() > 0) {
                String imgStr = Utils.join(imgResult, ",");
                addUserAddress(imgStr);
            } else {
                Toasty.warning(AddItemsActivity.this, "请重试！", Toast.LENGTH_SHORT, false).show();
            }
        } else {
            Toasty.warning(AddItemsActivity.this, "物品简介不能为空！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 添加物品
     *
     * @param imgStr
     */
    private void addUserAddress(String imgStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(AddItemsActivity.this));
        map.put("cdid", cdId);
        map.put("userselfinfo", etItemsAddInput.getText().toString());
        map.put("img", imgStr);
        String params = EncryptionUtil.getParameter(AddItemsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/addUserCds.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addUserCds")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        AddItemsActivity.this, "添加中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(AddItemsActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddUserCdsResult addUserCdsResult =
                                (AddUserCdsResult) GsonUtil.json2Object(response, AddUserCdsResult.class);
                        if (addUserCdsResult != null
                                && addUserCdsResult.getRet().equals("1")) {
                            Toasty.warning(AddItemsActivity.this, "添加成功！", Toast.LENGTH_SHORT, false).show();
                            AddItemsActivity.this.finish();
                        } else {
                            Toasty.warning(AddItemsActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private ArrayList<String> imgRes = new ArrayList<>();
    private ArrayList<String> upRes = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                imgRes = data.getStringArrayListExtra("imgs");

                for (String uri : imgRes) {
                    if (!imgs.contains(uri)) {
                        upRes.add(uri);
                    }
                }

                Message msg = new Message();
                msg.obj = index;
                msg.what = 0;
                handler.sendMessage(msg);

                if (imgRes.size() < 9) {
                    imgRes.add(Utils.getDrawableResPath(AddItemsActivity.this, R.drawable.add_photo));
                }
                itemsPhotoAdapter.getDatas().clear();
                itemsPhotoAdapter.getDatas().addAll(imgRes);
                itemsPhotoAdapter.notifyDataSetChanged();
                break;
        }
    }

    private int index = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (llItemsFirstView.getVisibility() == View.GONE) {
                showFirstView();
            } else {
                AddItemsActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 物品详情
     */
    private void getSysCdsByIdHttp(String id) {
        Map<String, Object> map = new HashMap<>();
//        map.put("cdid", id);
        map.put("cdid", "044665d64ca544aabb002544a1c26017");
        String params = EncryptionUtil.getParameter(AddItemsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getSysCdsById.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getSysCdsById")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        AddItemsActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(AddItemsActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        sysCdsByIdResult =
                                (SysCdsByIdResult) GsonUtil.json2Object(response, SysCdsByIdResult.class);
                        if (sysCdsByIdResult != null
                                && sysCdsByIdResult.getRet().equals("1")) {
                            hideFirstView();
                        }
                    }
                });
    }

    private SysCdsByIdResult sysCdsByIdResult;

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
//                    Logger.e("开始执行...");
                }
            }
        };
        File file = new File(path);
        mSubscription = EasyHttp.post(HttpUtils.URI_CENTER + "pic/upload.jhtml")
                .params("userId", DaoUtils.getUserId(AddItemsActivity.this))
                .params("avatar", file, file.getName(), listener)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .cacheKey(this.getClass().getSimpleName() + "_uploadAvatar")
                .cacheTime(0)
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
                            imgResult.add(updateImg.getErrorstring());
                            index += 1;
                            if (index < upRes.size()) {
                                Message msg = new Message();
                                msg.obj = index;
                                msg.what = 0;
                                handler.sendMessage(msg);
                            } else {
                                ifUpdate = true;
                            }
                            Logger.e("上传完成 == " + imgResult);
                            Logger.e("是否上传完成 == " + ifUpdate);
                        }
                    }
                });
    }

    private Disposable mSubscription;
    private boolean ifUpdate = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    updateImg(upRes.get(index));
                    break;
            }
        }
    };

    private List<String> imgResult = new ArrayList<>();
}
