package com.tch.zx.activity.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.contacts.AddLabelActivity;
import com.tch.zx.activity.login_register.TradeActivity;
import com.tch.zx.activity.message.FriendInfoActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GlideImageLoader;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.DynamicPhotoPopupWindow;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公司详情
 */
public class CompanyDetailsActivity extends BaseActivity {

    /**
     * 父布局
     */
    @BindView(R.id.ll_parent_company_details)
    LinearLayout ll_parent_company_details;
    /**
     * 公司Logo图片展示
     */
    @BindView(R.id.iv_upload_company_logo)
    ImageView iv_upload_company_logo;
    /**
     * 公司视频的展示
     */
    @BindView(R.id.iv_upload_company_video)
    ImageView iv_upload_company_video;
    @BindView(R.id.llUploadCompanyVideo1)
    LinearLayout llUploadCompanyVideo1;
    @BindView(R.id.llUploadCompanyVideo2)
    LinearLayout llUploadCompanyVideo2;
    @BindView(R.id.llUploadCompanyVideoRight1)
    LinearLayout llUploadCompanyVideoRight1;
    @BindView(R.id.llUploadCompanyVideoRight2)
    LinearLayout llUploadCompanyVideoRight2;
    @BindView(R.id.ivUploadCompanyVideo2)
    ImageView ivUploadCompanyVideo2;
    @BindView(R.id.ivUploadCompanyVideo3)
    ImageView ivUploadCompanyVideo3;
    @BindView(R.id.ivUploadCompanyVideo4)
    ImageView ivUploadCompanyVideo4;
    @BindView(R.id.ivUploadCompanyVideo5)
    ImageView ivUploadCompanyVideo5;
    @BindView(R.id.ivUCVDelete2)
    ImageView ivUCVDelete2;
    @BindView(R.id.ivUCVDelete3)
    ImageView ivUCVDelete3;
    @BindView(R.id.ivUCVDelete4)
    ImageView ivUCVDelete4;
    @BindView(R.id.ivUCVDelete5)
    ImageView ivUCVDelete5;
    @BindView(R.id.tvCompanyDetailsName)
    TextView tvCompanyDetailsName;
    @BindView(R.id.tvCompanyIndustry)
    TextView tvCompanyIndustry;
    @BindView(R.id.tvCompanyDetailsAddress)
    TextView tvCompanyDetailsAddress;

    /**
     * 设置popupwindow的布局参数
     */
    private WindowManager.LayoutParams params;
    /**
     * 跳转
     */
    private Intent intent;
    private int type = 0;
    private String[] paths;
    private List<String> pathList;
    private String detailsCompanyContent = "";
    private BasePresenter<Object> presenter;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private String lText, rText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_company_details);
        ButterKnife.bind(this);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        paths = new String[6];
        pathList = new ArrayList<String>();
        ifApiCan();
    }

    /**
     * 判断权限问题
     */
    private void ifApiCan() {
        if (ContextCompat.checkSelfPermission(CompanyDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(CompanyDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("TAG", "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(CompanyDetailsActivity.this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("TAG", "进行授权");
                ActivityCompat.requestPermissions(CompanyDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        } else {
            Log.i("TAG", "不需要授权 ");
            // 进行正常操作
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "同意授权");
                // 进行正常操作。
            } else {
                Log.i("TAG", "拒绝授权");
            }
        }
    }

    /**
     * 选择图片
     */
    private void selPhoto() {
        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())            // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)             // 监听接口（必填）
                .provider("com.tch.zx.FileProvider")            // provider (必填)
                .pathList(pathList)                             // 记录已选的图片
                .multiSelect(false)                             // 是否多选   默认：false
                .multiSelect(false, 9)                          // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(1)                                     // 配置多选时 的多选数量。    默认：9
                .crop(false)                                    // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 300, 300)                    // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                             // 是否显示相机按钮  默认：false
                .filePath("/ZX/Pictures")                       // 图片存放路径
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(CompanyDetailsActivity.this);
    }

    IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
        @Override
        public void onStart() {
            Log.i("TAG", "onStart: 开启");
        }

        @Override
        public void onSuccess(List<String> photoList) {
            Log.i("TAG", "onSuccess: 返回数据");
            switch (type) {
                case 0:
                    iv_upload_company_logo.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_upload_company_logo.setImageBitmap(BitmapFactory.decodeFile(photoList.get(0)));
                    paths[0] = photoList.get(0);
                    break;
                case 1:
                    iv_upload_company_video.setImageBitmap(BitmapFactory.decodeFile(photoList.get(0)));
                    llUploadCompanyVideo1.setVisibility(View.VISIBLE);
                    llUploadCompanyVideoRight1.setVisibility(View.INVISIBLE);
                    ivUCVDelete2.setVisibility(View.VISIBLE);
                    ivUCVDelete2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            llUploadCompanyVideo1.setVisibility(View.GONE);
                            paths[2] = "";
                        }
                    });
                    paths[1] = photoList.get(0);
                    break;
                case 2:
                    ivUploadCompanyVideo2.setImageBitmap(BitmapFactory.decodeFile(photoList.get(0)));
                    llUploadCompanyVideoRight1.setVisibility(View.VISIBLE);
                    ivUCVDelete2.setVisibility(View.GONE);
                    ivUCVDelete3.setVisibility(View.VISIBLE);
                    ivUCVDelete3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ivUCVDelete2.setVisibility(View.VISIBLE);
                            llUploadCompanyVideoRight1.setVisibility(View.INVISIBLE);
                            paths[3] = "";
                        }
                    });
                    paths[2] = photoList.get(0);
                    break;
                case 3:
                    ivUploadCompanyVideo3.setImageBitmap(BitmapFactory.decodeFile(photoList.get(0)));
                    llUploadCompanyVideo2.setVisibility(View.VISIBLE);
                    llUploadCompanyVideoRight2.setVisibility(View.INVISIBLE);
                    ivUCVDelete3.setVisibility(View.GONE);
                    ivUCVDelete4.setVisibility(View.VISIBLE);
                    ivUCVDelete4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            llUploadCompanyVideo2.setVisibility(View.GONE);
                            ivUCVDelete3.setVisibility(View.VISIBLE);
                            paths[4] = "";
                        }
                    });
                    paths[3] = photoList.get(0);
                    break;
                case 4:
                    ivUploadCompanyVideo4.setImageBitmap(BitmapFactory.decodeFile(photoList.get(0)));
                    llUploadCompanyVideo2.setVisibility(View.VISIBLE);
                    llUploadCompanyVideoRight2.setVisibility(View.VISIBLE);
                    ivUCVDelete4.setVisibility(View.GONE);
                    ivUCVDelete5.setVisibility(View.VISIBLE);
                    ivUCVDelete5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            llUploadCompanyVideoRight2.setVisibility(View.INVISIBLE);
                            ivUCVDelete4.setVisibility(View.VISIBLE);
                            paths[5] = "";
                        }
                    });
                    paths[4] = photoList.get(0);
                    break;
                case 5:
                    ivUploadCompanyVideo5.setImageBitmap(BitmapFactory.decodeFile(photoList.get(0)));
                    paths[5] = photoList.get(0);
                    break;
            }
        }

        @Override
        public void onCancel() {
            Log.i("TAG", "onCancel: 取消");
        }

        @Override
        public void onFinish() {
            Log.i("TAG", "onFinish: 结束");
        }

        @Override
        public void onError() {
            Log.i("TAG", "onError: 出错");
        }
    };

    /**
     * 选择公司Logo
     */
    @OnClick(R.id.rl_choose_upload_photo)
    public void chooseUploadPhoto() {
        type = 0;
        selPhoto();
    }

    /**
     * 选择公司图片
     */
    @OnClick(R.id.iv_upload_company_video)
    public void uploadCompanyVideo1() {
        type = 1;
        selPhoto();
    }

    /**
     * 选择公司图片
     */
    @OnClick(R.id.ivUploadCompanyVideo2)
    public void uploadCompanyVideo2() {
        type = 2;
        selPhoto();
    }

    /**
     * 选择公司图片
     */
    @OnClick(R.id.ivUploadCompanyVideo3)
    public void uploadCompanyVideo3() {
        type = 3;
        selPhoto();
    }

    /**
     * 选择公司图片
     */
    @OnClick(R.id.ivUploadCompanyVideo4)
    public void uploadCompanyVideo4() {
        type = 4;
        selPhoto();
    }

    /**
     * 选择公司图片
     */
    @OnClick(R.id.ivUploadCompanyVideo5)
    public void uploadCompanyVideo5() {
        type = 5;
        selPhoto();
    }

    /**
     * 保存
     */
    @OnClick(R.id.tv_save_company_details)
    public void saveCompanyDetails() {
        Log.e("TAG", "paths==0==" + paths[0] + "paths==1==" + paths[1] + "paths==2==" + paths[2]
                + "paths==3==" + paths[3] + "paths==4==" + paths[4] + "paths==5==" + paths[5]);
        for (int i = 1; i < 6; i++) {
            if (paths[i] != null && !paths[i].equals("")) {
                pathList.add(paths[i]);
            }
        }
        if (!TextUtils.isEmpty(tvCompanyDetailsName.getText().toString())
                && !tvCompanyDetailsName.getText().toString().equals("请输入公司名称")
                && !TextUtils.isEmpty(tvCompanyIndustry.getText().toString())
                && !tvCompanyIndustry.getText().toString().equals("请输入公司行业")
                && !TextUtils.isEmpty(tvCompanyDetailsAddress.getText().toString())
                && !tvCompanyDetailsAddress.getText().toString().equals("请输入公司地址")
                && detailsCompanyContent != null && !detailsCompanyContent.equals("")
                && pathList != null && pathList.size() > 0) {
            updateCompanyInfo();
        } else {
            Toast.makeText(CompanyDetailsActivity.this, "信息不完善！", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCompanyInfo() {
        presenter = new BasePresenter<Object>(CompanyDetailsActivity.this);
        presenter.onCreate();
        presenter.attachView(updateCompanyInfoView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("companyName", tvCompanyDetailsName.getText().toString());
        map.put("companyLogo", paths[0]);
        map.put("companyVideo", "");
        map.put("companyId", userBeanDao.loadAll().get(0).getCompanyId());
        map.put("companyAddress", tvCompanyDetailsAddress.getText().toString());
        map.put("companyIntroduce", detailsCompanyContent);
        map.put("companyIndustryFType", lText);
        map.put("companyIndustrySType", rText);
        map.put("companyPic", pathList);

        String data = HelperUtil.getParameter(map);
        presenter.updateCompanyInfo(data);
    }

    private BaseView<Object> updateCompanyInfoView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {

            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "getUserInfoView接口错误" + result);
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_company_details)
    public void returnCompanyDetails() {
        CompanyDetailsActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                tvCompanyDetailsName.setText(data.getStringExtra("content"));
                break;
            case 11:
                tvCompanyDetailsAddress.setText(data.getStringExtra("content"));
                break;
            case 12:
                if (data.getStringExtra("lText") != null
                        && data.getStringExtra("rText") != null) {
                    lText = data.getStringExtra("lText");
                    rText = data.getStringExtra("rText");
                    tvCompanyIndustry.setText(lText + " " + rText);
                }
                break;
            case 13:
                detailsCompanyContent = data.getStringExtra("content");
                break;
        }
    }

    /**
     * 编辑公司名称
     */
    @OnClick(R.id.rl_details_company_name)
    public void editDetailsCompanyName() {
        intent = new Intent(this, AddLabelActivity.class);
        intent.putExtra("intentType", 10);
        intent.putExtra("content", tvCompanyDetailsName.getText().toString());
        startActivityForResult(intent, 10);
    }

    /**
     * 编辑行业
     */
    @OnClick(R.id.rl_details_company_industry)
    public void editDetailsIndustry() {
        intent = new Intent(this, TradeActivity.class);
        intent.putExtra("intentType", 12);
        startActivityForResult(intent, 12);
    }

    /**
     * 编辑地址
     */
    @OnClick(R.id.rl_details_company_address)
    public void editDetailsAddress() {
        intent = new Intent(this, AddLabelActivity.class);
        intent.putExtra("intentType", 11);
        intent.putExtra("content", tvCompanyDetailsAddress.getText().toString());
        startActivityForResult(intent, 11);
    }

    /**
     * 编辑公司简介
     */
    @OnClick(R.id.rl_details_company_content)
    public void editDetailsContent() {
        intent = new Intent(this, IntroductionEditingActivity.class);
        intent.putExtra("intentType", 13);
        intent.putExtra("content", detailsCompanyContent);
        startActivityForResult(intent, 13);
    }
}
