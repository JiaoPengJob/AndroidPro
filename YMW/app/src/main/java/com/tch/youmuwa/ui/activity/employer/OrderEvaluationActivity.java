package com.tch.youmuwa.ui.activity.employer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.OrderEvaluateParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.popupWindow.ReplacePhotoPopupWindow;
import com.tch.youmuwa.ui.view.FlowTagGroup;
import com.tch.youmuwa.ui.view.ImageDialog;
import com.tch.youmuwa.util.GlideImageLoader;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.ImageUtils;
import com.tch.youmuwa.util.PhotoUtil;

import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单评价
 */
public class OrderEvaluationActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ivWhiteStar1)
    ImageView ivWhiteStar1;
    @BindView(R.id.ivWhiteStar2)
    ImageView ivWhiteStar2;
    @BindView(R.id.ivWhiteStar3)
    ImageView ivWhiteStar3;
    @BindView(R.id.ivWhiteStar4)
    ImageView ivWhiteStar4;
    @BindView(R.id.ivWhiteStar5)
    ImageView ivWhiteStar5;
    //    @BindView(R.id.ftg_img)
    FlowTagGroup ftg_img;
    @BindView(R.id.rlParentOrderEvaluation)
    RelativeLayout rlParentOrderEvaluation;
    @BindView(R.id.tvInputNumber)
    TextView tvInputNumber;
    @BindView(R.id.etOrderEvaluation)
    EditText etOrderEvaluation;
    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;
    /**
     * 设置的参数
     */
    private String path;//图片地址
    private List<Bitmap> photos = new ArrayList<Bitmap>();//图片集合
    private String orderNumber;
    private int leave = 1;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private ArrayList<String> pathList;
    private LinearLayout llImageParent;
    private ArrayList<String> paths = new ArrayList<String>();
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluation);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("订单评价");
        pathList = new ArrayList<String>();
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getStringExtra("orderNumber") != null) {
            orderNumber = getIntent().getStringExtra("orderNumber");
        }
        etOrderEvaluation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etOrderEvaluation.length() <= 500) {
                    handler.sendEmptyMessage(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                tvInputNumber.setText(String.valueOf(etOrderEvaluation.length()));
            }
        }
    };

    /**
     *
     */
    @OnClick(R.id.ivAdd)
    public void ivAdd() {
        if (ContextCompat.checkSelfPermission(OrderEvaluationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(OrderEvaluationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("TAG", "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(OrderEvaluationActivity.this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("TAG", "进行授权");
                ActivityCompat.requestPermissions(OrderEvaluationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        } else {
            Log.i("TAG", "不需要授权 ");
            // 进行正常操作
        }

        setphotos();

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

    IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
        @Override
        public void onStart() {
            Log.i("TAG", "onStart: 开启");
        }

        @Override
        public void onSuccess(List<String> photoList) {
            Log.i("TAG", "onSuccess: 返回数据");
            paths.clear();
            paths.addAll(photoList);
            rvPhotos.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
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
     * 加载图片选择器
     */
    private void setphotos() {
//        final ImageConfig imageConfig
//                = new ImageConfig.Builder(
//                // GlideLoader 可用自己用的缓存库
//                new GlideLoader())
//                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
//                .steepToolBarColor(getResources().getColor(R.color.green_31d09a))
//                // 标题的背景颜色 （默认黑色）
//                .titleBgColor(getResources().getColor(R.color.green_31d09a))
//                // 提交按钮字体的颜色  （默认白色）
//                .titleSubmitTextColor(getResources().getColor(R.color.white))
//                // 标题颜色 （默认白色）
//                .titleTextColor(getResources().getColor(R.color.white))
//                // 开启多选   （默认为多选）  (单选 为 singleSelect)
////                        .crop()
//                // 多选时的最大数量   （默认 9 张）
//                .mutiSelectMaxSize(3)
//                // 已选择的图片路径
//                .pathList(paths)
//                // 拍照后存放的图片路径（默认 /temp/picture）
//                .filePath("/ImageSelector/Pictures")
//                // 开启拍照功能 （默认开启）
//                .showCamera()
//                .requestCode(800)
//                .build();
//
//        ImageSelector.open(OrderEvaluationActivity.this, imageConfig);   // 开启图片选择器

        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.tch.youmuwa.fileprovider")   // provider (必填)
                .pathList(paths)                         // 记录已选的图片
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(3)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 300, 300)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(OrderEvaluationActivity.this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvPhotos.setLayoutManager(gridLayoutManager);
        adapter = new CommonAdapter<String>(OrderEvaluationActivity.this, R.layout.image, paths) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(HelperUtil.getScreenWidth(OrderEvaluationActivity.this) * 4
                        , HelperUtil.getScreenHeight(OrderEvaluationActivity.this) / 7);
                image = (ImageView) holder.getView(R.id.image);
                llImageParent = holder.getView(R.id.llImageParent);
                llImageParent.setLayoutParams(lp);
                Glide.with(OrderEvaluationActivity.this)
                        .load(item)
                        .into(image);
            }
        };
        rvPhotos.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(OrderEvaluationActivity.this, ImageLookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("urls", paths);
                bundle.putInt("index", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private ImageView image;

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatOrderEvaluation() {
        OrderEvaluationActivity.this.finish();
    }

    int i;
    ImageView imageView;

    /**
     * 加载图片
     */
    private void setFTGImgData(final List<Bitmap> list) {
        ftg_img.removeAllViews();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(HelperUtil.getScreenWidth(this) / 4, HelperUtil.getScreenWidth(this) / 5);
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(lp);
        mlp.setMargins(10, 10, 10, 10);

        for (i = 0; i < list.size(); i++) {
            imageView = new ImageView(this);
            imageView.setLayoutParams(mlp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(list.get(i));
            ftg_img.addView(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setDrawingCacheEnabled(true);
                ImageDialog imageDialog = new ImageDialog(OrderEvaluationActivity.this, 1, HelperUtil.getScreenWidth(OrderEvaluationActivity.this), HelperUtil.getScreenHeight(OrderEvaluationActivity.this), list.get(i - 1));
                imageDialog.show();
            }
        });
        //默认存在添加图片的按钮
        ImageView imgButton = null;
        if (list.size() < 3) {
            imgButton = new ImageView(this);
            imgButton.setLayoutParams(mlp);
            imgButton.setScaleType(ImageView.ScaleType.FIT_XY);
            imgButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.add_photo));
            //添加图片的点击事件监听
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rewardInfo();
                }
            });
            ftg_img.addView(imgButton);
        }
    }

    /**
     * 选择图片
     */
    private void rewardInfo() {
        final ReplacePhotoPopupWindow popupWindow = new ReplacePhotoPopupWindow(this);
        //设置Popupwindow显示位置（从底部弹出）
        popupWindow.showAtLocation(rlParentOrderEvaluation, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        rlParentOrderEvaluation.setAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlParentOrderEvaluation.setAlpha(1f);
            }
        });
        //从相册选取图片
        popupWindow.getTvAlbum().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                PhotoUtil.selectPictureFromAlbum(OrderEvaluationActivity.this);
            }
        });
        //拍照
        popupWindow.getTvShoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                PhotoUtil.photograph(OrderEvaluationActivity.this);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 800 && resultCode == RESULT_OK && data != null) {
            rvPhotos.setVisibility(View.VISIBLE);
//            pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            paths.clear();
            paths.addAll(pathList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 完成
     */
    @OnClick(R.id.btOrderEvaluationDone)
    public void orderEvaluationDone() {
        orderEvaluate();
    }

    /**
     * 评价已完成订单
     */
    private void orderEvaluate() {
        List<String> newPhotos = new ArrayList<String>();
        if (paths != null && paths.size() > 0) {
            for (String s : paths) {
                newPhotos.add("data:image/jpeg;base64," + HelperUtil.bitmapToString(ImageUtils.getImageBitmap(s)));
            }
        }
        mSVProgressHUD.showWithStatus("加载中...");
        OrderEvaluateParam orderEvaluateParam = new OrderEvaluateParam(
                orderNumber,
                etOrderEvaluation.getText().toString(),
                leave,
                newPhotos
        );
        presenter = new PresenterImpl<Object>(OrderEvaluationActivity.this);
        presenter.onCreate();
        presenter.orderevaluate(orderEvaluateParam);
        presenter.attachView(orderEvaluateView);
    }

    private ClientBaseView<Object> orderEvaluateView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() == 1) {
//                Intent intent = new Intent(OrderEvaluationActivity.this, EmployerActivity.class);
//                intent.putExtra("aid", 1);
//                startActivity(intent);
                OrderEvaluationActivity.this.finish();
            } else {
                Toast.makeText(OrderEvaluationActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "orderEvaluateView==" + result);
        }
    };

    @OnClick(R.id.ivWhiteStar1)
    public void whiteStar1() {
        leave = 1;
        ivWhiteStar1.setImageResource(R.mipmap.star);
        ivWhiteStar2.setImageResource(R.mipmap.white_star);
        ivWhiteStar3.setImageResource(R.mipmap.white_star);
        ivWhiteStar4.setImageResource(R.mipmap.white_star);
        ivWhiteStar5.setImageResource(R.mipmap.white_star);
    }

    @OnClick(R.id.ivWhiteStar2)
    public void whiteStar2() {
        leave = 2;
        ivWhiteStar1.setImageResource(R.mipmap.star);
        ivWhiteStar2.setImageResource(R.mipmap.star);
        ivWhiteStar3.setImageResource(R.mipmap.white_star);
        ivWhiteStar4.setImageResource(R.mipmap.white_star);
        ivWhiteStar5.setImageResource(R.mipmap.white_star);
    }

    @OnClick(R.id.ivWhiteStar3)
    public void whiteStar3() {
        leave = 3;
        ivWhiteStar1.setImageResource(R.mipmap.star);
        ivWhiteStar2.setImageResource(R.mipmap.star);
        ivWhiteStar3.setImageResource(R.mipmap.star);
        ivWhiteStar4.setImageResource(R.mipmap.white_star);
        ivWhiteStar5.setImageResource(R.mipmap.white_star);
    }

    @OnClick(R.id.ivWhiteStar4)
    public void whiteStar4() {
        leave = 4;
        ivWhiteStar1.setImageResource(R.mipmap.star);
        ivWhiteStar2.setImageResource(R.mipmap.star);
        ivWhiteStar3.setImageResource(R.mipmap.star);
        ivWhiteStar4.setImageResource(R.mipmap.star);
        ivWhiteStar5.setImageResource(R.mipmap.white_star);
    }

    @OnClick(R.id.ivWhiteStar5)
    public void whiteStar5() {
        leave = 5;
        ivWhiteStar1.setImageResource(R.mipmap.star);
        ivWhiteStar2.setImageResource(R.mipmap.star);
        ivWhiteStar3.setImageResource(R.mipmap.star);
        ivWhiteStar4.setImageResource(R.mipmap.star);
        ivWhiteStar5.setImageResource(R.mipmap.star);
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
                OrderEvaluationActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
