package com.tch.youmuwa.ui.activity.worker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.UpWorkerInfoParam;
import com.tch.youmuwa.bean.parameters.UploadAvatorParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.UploadAvatorResult;
import com.tch.youmuwa.bean.result.UserInfoResult;
import com.tch.youmuwa.bean.result.WorkerTypeResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.myinterface.WorkerTypeInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.mine.SettingPwdActivity;
import com.tch.youmuwa.ui.popupWindow.ReplacePhotoPopupWindow;
import com.tch.youmuwa.ui.popupWindow.SexPopupWindow;
import com.tch.youmuwa.ui.popupWindow.WorkTypePopupWindow;
import com.tch.youmuwa.ui.popupWindow.WorkYearPopupWindow;
import com.tch.youmuwa.ui.popupWindow.WorkerSexPopupWindow;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.PhotoUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工人/个人资料
 */
public class WorkerMineDataActivity extends BaseActivtiy implements PopupInterface, WorkerTypeInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.llParentWorkerMineData)
    LinearLayout llParentWorkerMineData;
    @BindView(R.id.ivWorkerMinePhoto)
    ImageView ivWorkerMinePhoto;
    @BindView(R.id.tvWorkerMineSex)
    TextView tvWorkerMineSex;
    @BindView(R.id.tvWorkerMineAreaShow)
    TextView tvWorkerMineAreaShow;
    @BindView(R.id.tvWorkerMineDataName)
    TextView tvWorkerMineDataName;
    @BindView(R.id.tvWorkerMineDataPhone)
    TextView tvWorkerMineDataPhone;
    @BindView(R.id.etPerfectPAge)
    TextView etPerfectPAge;
    @BindView(R.id.tvWorkerMineDataType)
    TextView tvWorkerMineDataType;
    @BindView(R.id.tvWorkerMineDataYear)
    TextView tvWorkerMineDataYear;
    /**
     * 设置的参数
     */
    private String path;
    private Intent intent;
    private PresenterImpl<Object> presenter;
    private UserInfoResult userInfoResult;
    private String workerGoodAtContent;
    private String workerAge = "0";//工龄
    private String workerType;//工种
    private SVProgressHUD mSVProgressHUD;//加载显示
    private int sex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_mine_data);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("个人资料");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        tvWorkerMineDataYear.setText(HelperUtil.getCurrentTime("yyyy") + "-" + (Integer.parseInt(HelperUtil.getCurrentTime("yyyy")) - 1) + "年");
        if (getIntent().getSerializableExtra("userInfoResult") != null) {
            userInfoResult = (UserInfoResult) getIntent().getSerializableExtra("userInfoResult");
            Glide.with(WorkerMineDataActivity.this)
                    .asBitmap()
                    .load(userInfoResult.getAvator())
                    .into(new BitmapImageViewTarget(ivWorkerMinePhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(WorkerMineDataActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivWorkerMinePhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            tvWorkerMineDataName.setText(userInfoResult.getName().toString());
            if (userInfoResult.getSex() == 0 || userInfoResult.getSex() == 1) {
                tvWorkerMineSex.setText("男");
            } else {
                tvWorkerMineSex.setText("女");
            }
            sex = userInfoResult.getSex();
            tvWorkerMineDataPhone.setText(userInfoResult.getMobile());
            workerType = String.valueOf(userInfoResult.getWorker_type());
            workerGoodAtContent = userInfoResult.getStrength();
            switch (userInfoResult.getWorker_type()) {
                case 1:
                    tvWorkerMineDataType.setText("油工");
                    break;
                case 2:
                    tvWorkerMineDataType.setText("木工");
                    break;
                case 3:
                    tvWorkerMineDataType.setText("泥瓦工");
                    break;
                case 4:
                    tvWorkerMineDataType.setText("水电工");
                    break;
                case 5:
                    tvWorkerMineDataType.setText("安装工");
                    break;
                case 6:
                    tvWorkerMineDataType.setText("保洁工");
                    break;
                case 7:
                    tvWorkerMineDataType.setText("小工");
                    break;
                case 8:
                    tvWorkerMineDataType.setText("其他");
                    break;
                default:
                    tvWorkerMineDataType.setText("请选择您的工种");
                    break;
            }
            etPerfectPAge.setText(String.valueOf(userInfoResult.getAge()) + "岁");
            tvWorkerMineDataYear.setText(String.valueOf(userInfoResult.getWork_age()) + "年");
            workerAge = String.valueOf(userInfoResult.getWork_age());
            tvWorkerMineAreaShow.setText(userInfoResult.getWork_province() + "-" + userInfoResult.getWork_city() + "-" + userInfoResult.getWork_area());
        }
    }

    /**
     * 头像
     */
    @OnClick(R.id.rlWorkerMinePhoto)
    public void workerMinePhoto() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(llParentWorkerMineData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(llParentWorkerMineData.getWindowToken(), 0); //强制隐藏键盘
        }
        final ReplacePhotoPopupWindow popupWindow = new ReplacePhotoPopupWindow(this);
        //设置Popupwindow显示位置（从底部弹出）
        popupWindow.showAtLocation(llParentWorkerMineData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        llParentWorkerMineData.setAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llParentWorkerMineData.setAlpha(1f);
            }
        });
        //从相册选取图片
        popupWindow.getTvAlbum().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                PhotoUtil.selectPictureFromAlbum(WorkerMineDataActivity.this);
            }
        });
        //拍照
        popupWindow.getTvShoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                PhotoUtil.photograph(WorkerMineDataActivity.this);
            }
        });
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
                    picture = new File(WorkerMineDataActivity.this.getFilesDir() + PhotoUtil.imageName);
                }
            }

            path = PhotoUtil.getPath(this);// 生成一个地址用于存放剪辑后的图片
            if (TextUtils.isEmpty(path)) {
                Log.e("TAG", "随机生成的用于存放剪辑后的图片的地址失败");
                return;
            }
            Uri imageUri = HelperUtil.getUri(this, path);
            PhotoUtil.startPhotoZoom(WorkerMineDataActivity.this, Uri.fromFile(picture), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
        }

        if (data == null)
            return;
        // 读取相册缩放图片
        if (requestCode == PhotoUtil.PHOTOZOOM) {
            path = PhotoUtil.getPath(this);// 生成一个地址用于存放剪辑后的图片
            if (TextUtils.isEmpty(path)) {
                Log.e("TAG", "随机生成的用于存放剪辑后的图片的地址失败");
                return;
            }
            Uri imageUri = HelperUtil.getUri(this, path);
            PhotoUtil.startPhotoZoom(WorkerMineDataActivity.this, data.getData(), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
        }
        // 处理结果
        if (requestCode == PhotoUtil.PHOTORESOULT) {
            /**
             * 在这里处理剪辑结果，可以获取缩略图，获取剪辑图片的地址。得到这些信息可以选则用于上传图片等等操作
             *  如，根据path获取剪辑后的图片
             **/
            if (path != null && !path.equals("")) {
                final Bitmap bitmap = PhotoUtil.convertToBitmap(path, PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH);
                if (bitmap != null) {
                    Glide.with(WorkerMineDataActivity.this)
                            .asBitmap()
                            .load(path)
                            .into(new BitmapImageViewTarget(ivWorkerMinePhoto) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(WorkerMineDataActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivWorkerMinePhoto.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                    //进行上传
                    updateUserPhoto(HelperUtil.bitmapToString(bitmap));
                }
            }
        }
        //地址显示
        if (requestCode == 5) {
            tvWorkerMineAreaShow.setText(data.getStringExtra("workerAreaMag"));
        }
        //获取擅长内容
        if (requestCode == 6) {
            workerGoodAtContent = data.getStringExtra("workerGoodAtContent");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传头像
     */
    private void updateUserPhoto(String photo) {
        mSVProgressHUD.showWithStatus("加载中...");
        UploadAvatorParam uploadAvatorParam = new UploadAvatorParam("data:image/jpeg;base64," + photo);
        presenter = new PresenterImpl<Object>(WorkerMineDataActivity.this);
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
                Glide.with(WorkerMineDataActivity.this)
                        .asBitmap()
                        .load(uplaodAvatorResult.getAvator())
                        .into(new BitmapImageViewTarget(ivWorkerMinePhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(WorkerMineDataActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivWorkerMinePhoto.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                Toast.makeText(WorkerMineDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
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
     * 性别
     */
    @OnClick(R.id.rlWorkerMineSex)
    public void workerMineSex() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(llParentWorkerMineData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(llParentWorkerMineData.getWindowToken(), 0); //强制隐藏键盘
        }
        WorkerSexPopupWindow sexPopupWindow = new WorkerSexPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        sexPopupWindow.showAtLocation(llParentWorkerMineData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        llParentWorkerMineData.setAlpha(0.8f);
        sexPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llParentWorkerMineData.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(int index, String result) {
        if (index == 6) {
            tvWorkerMineSex.setText(result);
        } else if (index == 1) {
            tvWorkerMineDataYear.setText(result + "年");
            workerAge = result;
        }
    }

    /**
     * 地址
     */
    @OnClick(R.id.rlWokerMineAddressSetting)
    public void wokerMineAddressSetting() {
        intent = new Intent(WorkerMineDataActivity.this, WorkerAreaMangerActivity.class);
        startActivityForResult(intent, 5);
    }

    /**
     * 擅长
     */
    @OnClick(R.id.rlWorkerGoodAt)
    public void workerGoodAt() {
        intent = new Intent(WorkerMineDataActivity.this, WorkerGoodAtActivity.class);
        intent.putExtra("good", workerGoodAtContent);
        startActivityForResult(intent, 6);
    }

    /**
     * 设置密码
     */
    @OnClick(R.id.rlWorkerMineSettingPwd)
    public void workerMineSettingPwd() {
        intent = new Intent(WorkerMineDataActivity.this, SettingPwdActivity.class);
        intent.putExtra("activity", "WorkerMineDataActivity");
        startActivity(intent);
    }

    /**
     * 选择工种
     */
    @OnClick({R.id.tvWorkerMineDataType, R.id.ivWorkerMineDataType})
    public void workerMineDataType() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(llParentWorkerMineData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(llParentWorkerMineData.getWindowToken(), 0); //强制隐藏键盘
        }
        WorkTypePopupWindow popupWindow = new WorkTypePopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        popupWindow.showAtLocation(llParentWorkerMineData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        llParentWorkerMineData.setAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llParentWorkerMineData.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(WorkerTypeResult.DataBean type) {
        tvWorkerMineDataType.setText(type.getName());
        workerType = String.valueOf(type.getType());
    }

    /**
     * 工龄
     */
    @OnClick({R.id.tvWorkerMineDataYear, R.id.ivWorkerMineDataYear})
    public void workerMineDataYear() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(llParentWorkerMineData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(llParentWorkerMineData.getWindowToken(), 0); //强制隐藏键盘
        }
        WorkYearPopupWindow yearPopupWindow = new WorkYearPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        yearPopupWindow.showAtLocation(llParentWorkerMineData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        llParentWorkerMineData.setAlpha(0.8f);
        yearPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llParentWorkerMineData.setAlpha(1f);
            }
        });
    }

    /**
     * 确定
     */
    @OnClick(R.id.btMineDataSave)
    public void mineDataSave() {
        String cityStr = tvWorkerMineAreaShow.getText().toString();
        String province = cityStr.substring(0, cityStr.indexOf("-"));
        String city = cityStr.substring(cityStr.indexOf("-") + 1, cityStr.lastIndexOf("-"));
        String area = cityStr.substring(cityStr.lastIndexOf("-") + 1, cityStr.length());
        if (tvWorkerMineSex.getText().toString().equals("男")) {
            sex = 1;
        } else {
            sex = 2;
        }
        updateWorkerInfo(sex, province, city, area);
    }

    /**
     * 工人修改个人资料
     */
    private void updateWorkerInfo(int sex, String province, String city, String area) {
        mSVProgressHUD.showWithStatus("加载中...");
        UpWorkerInfoParam workerInfoParam = new UpWorkerInfoParam(
                sex,
                province,
                city,
                area,
                workerAge,
                workerType,
                workerGoodAtContent
        );
        presenter = new PresenterImpl<Object>(WorkerMineDataActivity.this);
        presenter.onCreate();
        presenter.upworkerinfo(workerInfoParam);
        presenter.attachView(workerInfoView);
    }

    private ClientBaseView<Object> workerInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                Intent intent = new Intent(WorkerMineDataActivity.this, WorkerMainActivity.class);
                intent.putExtra("aid", 2);
                startActivity(intent);
                WorkerMineDataActivity.this.finish();
            } else {
                Toast.makeText(WorkerMineDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "workerInfoView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatWorkerMineData() {
        WorkerMineDataActivity.this.finish();
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
                WorkerMineDataActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
