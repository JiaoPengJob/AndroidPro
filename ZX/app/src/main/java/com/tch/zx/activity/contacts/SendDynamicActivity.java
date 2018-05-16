package com.tch.zx.activity.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.NetworkImgAsyncTask;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.UploadPicResultBean;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.presenter.UploadPicPresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.http.view.UploadPicView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.DynamicPhotoPopupWindow;
import com.tch.zx.view.FlowTagGroup;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 发表朋友圈状态
 */
public class SendDynamicActivity extends BaseActivity {
    /**
     * 流式图片展示
     */
    @BindView(R.id.ftg_item_send_dynamic_img)
    FlowTagGroup ftg_item_send_dynamic_img;
    /**
     * 展示popup父布局
     */
    @BindView(R.id.ll_dynamic_parent_main)
    LinearLayout ll_dynamic_parent_main;
    @BindView(R.id.etSendDynamicInput)
    EditText etSendDynamicInput;

    /**
     * 需要发送的图片
     */
    private List<Bitmap> sendPhotos = new ArrayList<Bitmap>();

    /**
     * 设置popupwindow的布局参数
     */
    private WindowManager.LayoutParams params;
    private UploadPicPresenter uploadPicPresenter;
    private Map<String, RequestBody> partList;
    private List<String> paths;
    private MultipartBody.Builder builder;
    private BasePresenter<Object> presenter;
    private Map<String, String> paramMap;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_send_dynamic);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        partList = new HashMap<String, RequestBody>();
        paths = new ArrayList<String>();
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        setFTGImgData(sendPhotos);
    }

    /**
     * 加载图片
     */
    private void setFTGImgData(List<Bitmap> list) {
        ftg_item_send_dynamic_img.removeAllViews();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(HelperUtil.getScreenWidth(this) / 4, HelperUtil.getScreenWidth(this) / 4);
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(lp);
        mlp.setMargins(10, 10, 10, 10);

        for (int i = 0; i < list.size(); i++) {
            //默认存在添加图片的按钮
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(mlp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(list.get(i));
            ftg_item_send_dynamic_img.addView(imageView);
        }
        ImageView imgButton = new ImageView(this);
        imgButton.setLayoutParams(mlp);
        imgButton.setScaleType(ImageView.ScaleType.FIT_XY);
        imgButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.add_image_icon));
        //添加图片的点击事件监听
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardInfo();
            }
        });
        ftg_item_send_dynamic_img.addView(imgButton);
    }

    /**
     * 选择图片
     */
    private void rewardInfo() {
        final DynamicPhotoPopupWindow rewardPopupWindow = new DynamicPhotoPopupWindow(this);
        //设置Popupwindow显示位置（从底部弹出）
        rewardPopupWindow.showAtLocation(ll_dynamic_parent_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        rewardPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

        rewardPopupWindow.getTv_popup_take_photo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 启动相机
                startActivityForResult(intent1, 60);
                rewardPopupWindow.dismiss();
            }
        });

        rewardPopupWindow.getTv_popup_choose_photo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 61);
                rewardPopupWindow.dismiss();
            }
        });
    }

    /**
     * 取消
     */
    @OnClick(R.id.ll_return_send_dynamic)
    public void returnSendDynamic() {
        this.finish();
    }

    /**
     * 发送
     */
    @OnClick(R.id.tv_send_dynamic)
    public void sendDynamicHasSure() {
        if (!TextUtils.isEmpty(etSendDynamicInput.getText().toString())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("param", "a");
            String data = HelperUtil.getParameter(map);
            paramMap = new HashMap<String, String>();
            paramMap.put("body", data);
            //设置上传的文件
            Map<String, File> fileMap = new HashMap<String, File>();
            for (int i = 0; i < paths.size(); i++) {
                fileMap.put("img" + i, new File(paths.get(i)));
            }
            NetworkImgAsyncTask networkImgAsyncTask = new NetworkImgAsyncTask(RetrofitHelper.baseUrl + "public/uploadPic.jhtml",
                    "", paramMap, fileMap, handler, 101);
            networkImgAsyncTask.execute();
        } else {
            Toast.makeText(SendDynamicActivity.this, "输入内容不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 接收服务器返回的结果
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 101:
                    //接收文件文件返回数据
                    Log.e("TAG", "上传图片返回结果" + message.obj.toString());
                    if (message.obj.toString() != null) {
                        UploadPicResultBean uploadPicResultBean = (UploadPicResultBean) GsonUtil.parseJson(message.obj.toString(), UploadPicResultBean.class);
                        StringBuffer sb = new StringBuffer();
                        if (uploadPicResultBean.getRet().equals("1")) {
                            for (String s : uploadPicResultBean.getResult().getResponseObject()) {
                                sb.append(s + ",");
                            }
                            String imgStr;
                            if (sb.length() > 0) {
                                imgStr = sb.substring(0, sb.toString().length() - 1).toString();
                            } else {
                                imgStr = "";
                            }
                            addDynamic(imgStr);
                        }
                    } else {
                        Toast.makeText(SendDynamicActivity.this, "图片上传失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        }
    });

    /**
     * 发送动态
     */
    private void addDynamic(String content_picture) {
        presenter = new BasePresenter<Object>(SendDynamicActivity.this);
        presenter.onCreate();
        presenter.attachView(addDynamicView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("dynamic_content", etSendDynamicInput.getText().toString());
        map.put("content_picture", content_picture);

        String data = HelperUtil.getParameter(map);
        presenter.addDynamic(data);
    }

    private BaseView<Object> addDynamicView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                SendDynamicActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "addDynamicView接口错误" + result);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 60:
                    //通过这种方法取出的拍摄会默认压缩，因为如果相机的像素比较高拍摄出来的图会比较高清，
                    //如果图太大会造成内存溢出（OOM），因此此种方法会默认给图片尽心压缩
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    File f = HelperUtil.compressImage(bitmap);
                    paths.add(f.getPath());
                    sendPhotos.add(bitmap);
                    setFTGImgData(sendPhotos);
                    break;
                case 61:
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        paths.add(path);
                        Bitmap bm = BitmapFactory.decodeFile(path);
                        sendPhotos.add(bm);
                        setFTGImgData(sendPhotos);
                    }
                    break;
            }
        }
    }

}
