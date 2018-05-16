package com.tch.kuwanx.ui.release;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.Utils;
import com.tch.kuwanx.view.CameraSurfaceView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 拍照页面
 */
public class TakePhotosActivity extends BaseActivity {

    @BindView(R.id.rvTookPhotos)
    RecyclerView rvTookPhotos;
    @BindView(R.id.surfaceView)
    CameraSurfaceView surfaceView;
    @BindView(R.id.ibPhotoSelect)
    ImageButton ibPhotoSelect;

    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private CommonAdapter photosAdapter;
    private Bundle bundle;
    private List<String> photos = new ArrayList<String>();
    private List<String> imgs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photos);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringArrayListExtra("imgs") != null) {
            imgs = getIntent().getStringArrayListExtra("imgs");
        }
        initTookPhotos();
        initDefaultPhoto();
    }

    protected void initDefaultPhoto() {
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //获取图片的详细信息
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            photos.add(desc);
        }
        if (photos.size() > 0) {
            Glide.with(this)
                    .load(photos.get(0))
                    .into(ibPhotoSelect);
        }
    }

    /**
     * 拍照
     */
    @OnClick(R.id.btTakePhoto)
    public void takePhoto() {
        surfaceView.getmCamera().takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                if (bitmaps.size() < 9) {
                    try {
                        Bitmap bitmap = Utils.bytes2Bitmap(bytes);
                        bitmap = Utils.rotateBitmapByDegree(bitmap, 90);
                        String str = Utils.compressImage(bitmap);
                        paths.add(str);
                        bitmaps.add(bitmap);
                        photosAdapter.notifyDataSetChanged();
                        camera.startPreview(); // 拍完照后，重新开始预览
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toasty.warning(TakePhotosActivity.this, "您已无法选择更多图片", Toast.LENGTH_SHORT, false).show();
                }
            }
        });
    }

    private String sdCardDir = Environment.getExternalStorageDirectory().getPath() + "/imgs/";

    /**
     * 加载已经拍摄的照片
     */
    private void initTookPhotos() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(TakePhotosActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTookPhotos.setLayoutManager(layoutManager);
        photosAdapter = new CommonAdapter<Bitmap>(TakePhotosActivity.this, R.layout.item_took_photo, bitmaps) {
            @Override
            protected void convert(ViewHolder holder, Bitmap item, final int position) {
                if (position < imgs.size()) {
                    holder.setVisible(R.id.ivPhotoBan, false);
                } else {
                    holder.setVisible(R.id.ivPhotoBan, true);
                }
                holder.setImageBitmap(R.id.ivPhotoShow, item);
                holder.setOnClickListener(R.id.ivPhotoBan, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bitmaps.remove(position);
                        paths.remove(position);
                        photosAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        rvTookPhotos.setAdapter(photosAdapter);
        photosAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showGallery(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (final String uri : imgs) {
            Glide.with(TakePhotosActivity.this)
                    .asBitmap()
                    .load(uri)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            bitmaps.add(resource);
                            paths.add(uri);
                            photosAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private ArrayList<String> paths = new ArrayList<String>();

    /**
     * 展示大图画廊
     */
    private void showGallery(int position) {
        Album.gallery(this)
                .requestCode(100)
                .checkedList(paths)
                .currentPosition(position)
                .navigationAlpha(250)
                .checkable(false)
                .widget(
                        Widget.newDarkBuilder(TakePhotosActivity.this)
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 下一步
     */
    @OnClick(R.id.btTitleFeatures)
    public void next() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("imgs", paths);
        this.setResult(0, intent);
        this.finish();
    }

    /**
     * 选择照片
     */
    @OnClick(R.id.ibPhotoSelect)
    public void selPhoto() {
        if (bitmaps.size() < 9) {
            selectPhotos();
        } else {
            Toasty.warning(this, "您已无法选择更多图片", Toast.LENGTH_SHORT, false).show();
        }
    }

    private ArrayList<AlbumFile> list = new ArrayList<>();

    private void selectPhotos() {
        Album.image(TakePhotosActivity.this) // 选择图片。
                .multipleChoice()
                .requestCode(100)
                .camera(false)
                .columnCount(4)
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
                .selectCount(9 - bitmaps.size())
                .checkedList(list)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(final int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        if (requestCode == 100) {
                            for (AlbumFile albumFile : result) {
                                paths.add(albumFile.getPath());
                                bitmaps.add(Utils.saveBefore(albumFile.getPath()));
                                photosAdapter.notifyDataSetChanged();
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

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void takePhotoTitleBack() {
        Utils.deleteFiles(new File(Environment.getExternalStorageDirectory().getPath() + "/imgs/"));
        Intent intent = new Intent();
        intent.putStringArrayListExtra("imgs", paths);
        this.setResult(0, intent);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Utils.deleteFiles(new File(Environment.getExternalStorageDirectory().getPath() + "/imgs/"));
            TakePhotosActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
