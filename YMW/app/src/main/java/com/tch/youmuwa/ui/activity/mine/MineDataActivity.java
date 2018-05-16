package com.tch.youmuwa.ui.activity.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.EmployerInfoParam;
import com.tch.youmuwa.bean.parameters.UploadAvatorParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.UploadAvatorResult;
import com.tch.youmuwa.bean.result.UserInfoResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.employer.AreaManagerActivity;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.ui.popupWindow.ReplacePhotoPopupWindow;
import com.tch.youmuwa.ui.popupWindow.SexPopupWindow;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.PhotoUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人资料
 */
public class MineDataActivity extends BaseActivtiy implements PopupInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.llParentMineData)
    LinearLayout llParentMineData;
    @BindView(R.id.ivMinePhoto)
    ImageView ivMinePhoto;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.etMineDataName)
    EditText etMineDataName;
    @BindView(R.id.tvMineDataPhone)
    TextView tvMineDataPhone;
    /**
     * 设置的参数
     */
    private String path;//选择的图片的地址
    private Intent intent;//跳转
    private PresenterImpl<Object> presenter;//接口
    private UserInfoResult userInfoResult;//用户信息返回结果
    private SVProgressHUD mSVProgressHUD;//加载显示
    private Bitmap bitmap;
    private int sex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_data);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("个人资料");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getSerializableExtra("userInfoResult") != null) {
            userInfoResult = (UserInfoResult) getIntent().getSerializableExtra("userInfoResult");
            Glide.with(MineDataActivity.this)
                    .asBitmap()
                    .load(userInfoResult.getAvator())
                    .into(new BitmapImageViewTarget(ivMinePhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(MineDataActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivMinePhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            if (!userInfoResult.getName().toString().equals("未设置")) {
                etMineDataName.setText(userInfoResult.getName().toString());
            }
            if (userInfoResult.getSex() == 0) {
                sex = 1;
                tvSex.setText("未设置");
            } else if (userInfoResult.getSex() == 1) {
                sex = 1;
                tvSex.setText("男");
            } else {
                sex = 2;
                tvSex.setText("女");
            }
            tvMineDataPhone.setText(userInfoResult.getMobile());
        }
    }

    /**
     * 更换头像
     */
    @OnClick(R.id.rlMinePhoto)
    public void minePhoto() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(llParentMineData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(llParentMineData.getWindowToken(), 0); //强制隐藏键盘
        }
        final ReplacePhotoPopupWindow popupWindow = new ReplacePhotoPopupWindow(this);
        //设置Popupwindow显示位置（从底部弹出）
        popupWindow.showAtLocation(llParentMineData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        llParentMineData.setAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llParentMineData.setAlpha(1f);
            }
        });
        //从相册选取图片
        popupWindow.getTvAlbum().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                PhotoUtil.selectPictureFromAlbum(MineDataActivity.this);
            }
        });
        //拍照
        popupWindow.getTvShoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                showContacts(llParentMineData);
                PhotoUtil.photograph(MineDataActivity.this);
            }
        });
    }

    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void showContacts(View v) {
        if (ActivityCompat.checkSelfPermission(MineDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestContactsPermissions(v);
        } else {
            Log.e("TAG", "Contact permissions have already been granted. Displaying contact details.");
        }
    }

    private void requestContactsPermissions(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MineDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ) {
            Snackbar.make(view, "permission_contacts_rationale",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MineDataActivity.this, PERMISSIONS_CONTACT, 1000);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(MineDataActivity.this, PERMISSIONS_CONTACT, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            Log.e("TAG", "权限设置成功");
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * activity回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == PhotoUtil.NONE)
            return;
        //拍照
        if (requestCode == PhotoUtil.PHOTOGRAPH) {
            // 设置文件保存路径这里放在跟目录下
            File picture = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                if (!picture.exists()) {
                    picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                }
            } else {
                picture = new File(this.getFilesDir() + PhotoUtil.imageName);
                if (!picture.exists()) {
                    picture = new File(MineDataActivity.this.getFilesDir() + PhotoUtil.imageName);
                }
            }

            path = PhotoUtil.getPath(this);// 生成一个地址用于存放剪辑后的图片
            if (TextUtils.isEmpty(path)) {
                Log.e("Error", "随机生成的用于存放剪辑后的图片的地址失败");
                return;
            }
            Uri imageUri = HelperUtil.getUri(this, path);
            PhotoUtil.startPhotoZoom(MineDataActivity.this, Uri.fromFile(picture), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
        }

        if (data == null)
            return;
        // 读取相册缩放图片
        if (requestCode == PhotoUtil.PHOTOZOOM) {
            path = PhotoUtil.getPath(this);// 生成一个地址用于存放剪辑后的图片
            if (TextUtils.isEmpty(path)) {
                Log.e("Error", "随机生成的用于存放剪辑后的图片的地址失败");
                return;
            }
            Uri imageUri = HelperUtil.getUri(this, path);
            PhotoUtil.startPhotoZoom(MineDataActivity.this, data.getData(), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
        }
        // 处理结果
        if (requestCode == PhotoUtil.PHOTORESOULT) {
            /**
             * 在这里处理剪辑结果，可以获取缩略图，获取剪辑图片的地址。得到这些信息可以选则用于上传图片等等操作
             *  如，根据path获取剪辑后的图片
             **/
            if (path != null && !path.equals("")) {
                bitmap = PhotoUtil.convertToBitmap(path, PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH);
                if (bitmap != null) {
                    Glide.with(MineDataActivity.this)
                            .asBitmap()
                            .load(path)
                            .into(new BitmapImageViewTarget(ivMinePhoto) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(MineDataActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivMinePhoto.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                    handler.sendEmptyMessage(0);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //进行上传
            updateUserPhoto(HelperUtil.bitmapToString(bitmap));
        }
    };

    /**
     * 上传头像
     */
    private void updateUserPhoto(String photo) {
        mSVProgressHUD.showWithStatus("加载中...");
        UploadAvatorParam uploadAvatorParam = new UploadAvatorParam("data:image/jpeg;base64," + photo);
        presenter = new PresenterImpl<Object>(MineDataActivity.this);
        presenter.onCreate();
        presenter.uploadavator("data:image/jpeg;base64," + photo);
        presenter.attachView(updateAvatorView);
    }

    private ClientBaseView<Object> updateAvatorView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                UploadAvatorResult uplaodAvatorResult = (UploadAvatorResult) GsonUtil.parseJson(baseBean.getData(), UploadAvatorResult.class);
                Glide.with(MineDataActivity.this)
                        .asBitmap()
                        .load(uplaodAvatorResult.getAvator())
                        .into(new BitmapImageViewTarget(ivMinePhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(MineDataActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivMinePhoto.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                Toast.makeText(MineDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "updateAvatorView==" + result);
        }
    };

    /**
     * 设置密码
     */
    @OnClick(R.id.rlSettingPwd)
    public void settingPwd() {
        intent = new Intent(MineDataActivity.this, SettingPwdActivity.class);
        startActivity(intent);
    }

    /**
     * 地址管理
     */
    @OnClick(R.id.rlAddressSetting)
    public void addressSetting() {
        intent = new Intent(MineDataActivity.this, AreaManagerActivity.class);
        intent.putExtra("activity", "MineDataActivity");
        startActivity(intent);
    }

    /**
     * 性别
     */
    @OnClick(R.id.rlSex)
    public void sex() {
        SexPopupWindow sexPopupWindow = new SexPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        sexPopupWindow.showAtLocation(llParentMineData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        llParentMineData.setAlpha(0.8f);
        sexPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llParentMineData.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(int index, String result) {
        if (index == 6) {
            tvSex.setText(result);
            if (result.equals("男")) {
                sex = 1;
            } else {
                sex = 2;
            }
        }
    }

    /**
     * 保存
     */
    @OnClick(R.id.btMineDataSave)
    public void mineDataSave() {
        mSVProgressHUD.showWithStatus("保存中...");
        upemployerinfo();
    }

    /**
     * 保存雇主信息
     */
    private void upemployerinfo() {
        EmployerInfoParam ei = new EmployerInfoParam(
                etMineDataName.getText().toString(),
                sex
        );
        presenter = new PresenterImpl<Object>(MineDataActivity.this);
        presenter.onCreate();
        presenter.upemployerinfo(ei);
        presenter.attachView(employerInfoView);
    }

    private ClientBaseView<Object> employerInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(MineDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
//                Intent intent = new Intent(MineDataActivity.this, EmployerActivity.class);
//                intent.putExtra("aid", 3);
//                startActivity(intent);
                MineDataActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "employerInfoView--" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatMineData() {
//        Intent intent = new Intent(MineDataActivity.this, EmployerActivity.class);
//        intent.putExtra("aid", 3);
//        startActivity(intent);
        MineDataActivity.this.finish();
    }

    /**
     * 监听后退物理按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bl = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
                if (presenter != null) {
                    presenter.onStop();
                }
                bl = false;
            } else {
//                Intent intent = new Intent(MineDataActivity.this, EmployerActivity.class);
//                intent.putExtra("aid", 3);
//                startActivity(intent);
                MineDataActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
