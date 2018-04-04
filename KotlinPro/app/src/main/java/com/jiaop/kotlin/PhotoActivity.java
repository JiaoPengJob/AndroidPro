package com.jiaop.kotlin;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.jiaop.libs.base.JPBaseActivity;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.OnClick;

public class PhotoActivity extends JPBaseActivity {

    @Override
    protected void initView() {

    }

    @OnClick(R.id.btChoose)
    void choosePhotos() {
        Album.album(this) // 图片和视频混选。
                .multipleChoice() // 多选模式，单选模式为：singleChoice()。
                .requestCode(100) // 请求码，会在listener中返回。
                .columnCount(4) // 页面列表的列数。
                .selectCount(4)  // 最多选择几张图片。
                .camera(false) // 是否在Item中出现相机。
                .cameraVideoQuality(1) // 视频质量，[0, 1]。
                .cameraVideoLimitDuration(Long.MAX_VALUE) // 视频最长时长，单位是毫秒。
                .cameraVideoLimitBytes(Long.MAX_VALUE) // 视频最大大小，单位byte。
                //.checkedList() // 要反选的列表，比如选择一次再次选择，可以把上次选择的传入。
                //.filterSize() // 文件大小的过滤。
                //.filterMimeType() // 文件格式的过滤。
                //.filterDuration() // 视频时长的过滤。
                //.afterFilterVisibility() // 显示被过滤掉的文件，但它们是不可用的。
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        Log.e("TAG", "result == " + result);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        // 用户取消了操作。
                        Toast.makeText(PhotoActivity.this, "用户取消了操作", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initWiFiData() {

    }

    @Override
    protected void initNetData() {

    }

    @Override
    protected void initOfflineData() {

    }

    @Override
    protected int statusBarColor() {
        return R.color.btColor;
    }
}
