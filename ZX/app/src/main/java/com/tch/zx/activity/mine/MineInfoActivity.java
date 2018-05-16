package com.tch.zx.activity.mine;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.contacts.AddLabelActivity;
import com.tch.zx.activity.login_register.TradeActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.fragment.mine.MineMainFragment;
import com.tch.zx.http.NetworkImgAsyncTask;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.UploadPicResultBean;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.DynamicPhotoPopupWindow;
import com.tch.zx.wheelview.model.AddressDtailsEntity;
import com.tch.zx.wheelview.model.AddressModel;
import com.tch.zx.wheelview.utils.JsonUtil;
import com.tch.zx.wheelview.utils.Utils;
import com.tch.zx.wheelview.view.ChooseAddressWheel;
import com.tch.zx.wheelview.view.ChooseSexWheel;
import com.tch.zx.wheelview.view.listener.OnAddressChangeListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息
 */
public class MineInfoActivity extends BaseActivity implements OnAddressChangeListener {

    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 显示dialog的父布局
     */
    @BindView(R.id.ll_parent_dynamic_mine_info)
    LinearLayout ll_parent_dynamic_mine_info;
    /**
     * 显示地区信息文本框
     */
    @BindView(R.id.tv_address_mine_info)
    TextView tv_address_mine_info;
    /**
     * 性别展示文本框
     */
    @BindView(R.id.tv_sex_mine_info)
    TextView tv_sex_mine_info;
    /**
     * 头像图片
     */
    @BindView(R.id.civ_mine_info_photo)
    CircleImageView civ_mine_info_photo;
    /**
     * 姓名
     */
    @BindView(R.id.tvInfoName)
    TextView tvInfoName;
    /**
     * 个签
     */
    @BindView(R.id.tvInfoIntroduce)
    TextView tvInfoIntroduce;
    /**
     * 公司名称
     */
    @BindView(R.id.tvInfoCompanyName)
    TextView tvInfoCompanyName;
    /**
     * 职位
     */
    @BindView(R.id.tvInfoPosition)
    TextView tvInfoPosition;
    /**
     * 行业
     */
    @BindView(R.id.tvInfoCategoryName)
    TextView tvInfoCategoryName;

    /**
     * 设置popupwindow的布局参数
     */
    private WindowManager.LayoutParams params;
    /**
     * 跳转Intent
     */
    private Intent intent;
    /**
     * 展示地区轮滑效果
     */
    private ChooseAddressWheel chooseAddressWheel = null;
    private ChooseSexWheel chooseSexWheel = null;
    /**
     * 数据库使用
     */
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_mine_info);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        paths = new ArrayList<String>();
        tv_title_top_all.setText("个人信息");
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        userBean = userBeanDao.loadAll().get(0);
        if (userBean != null) {
            tv_sex_mine_info.setText(userBean.getAppUserSex());
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    civ_mine_info_photo.setImageBitmap(resource);
                }
            };
            Glide.with(MineInfoActivity.this).asBitmap().load(userBean.getAppUserPic()).into(target);
            tvInfoName.setText(userBean.getAppUserName());
            tv_address_mine_info.setText(userBean.getAdrProvince() + " " + userBean.getAdrCity() + " " + userBean.getAdrCounty());
            tvInfoIntroduce.setText(userBean.getAppUserIntroduce());
            tvInfoCompanyName.setText(userBean.getCompanyName());
            tvInfoPosition.setText(userBean.getCompanyPosition());
            tvInfoCategoryName.setText(userBean.getCategoryName() + " " + userBean.getStypeName());
        }

        initSexInfo();
        initAddressInfo();
    }

    /**
     * 设置性别选项
     */
    private void initSexInfo() {
        List<String> list = new ArrayList<String>();
        list.add("男");
        list.add("女");
        chooseSexWheel = new ChooseSexWheel(this);
        chooseSexWheel.bindData(list);
        chooseSexWheel.setOnAddressChangeListener(new OnAddressChangeListener() {
            @Override
            public void onAddressChange(String province, String city, String district) {
                tv_sex_mine_info.setText(city);
            }
        });
    }


    /**
     * 选取头像
     */
    @OnClick(R.id.rl_photo_image_mine)
    public void selectPhotoMine() {
        rewardInfo();
    }

    /**
     * 修改名字
     */
    @OnClick(R.id.rl_name_mine_info)
    public void updateName() {
        intent = new Intent(this, AddLabelActivity.class);
        startActivity(intent);
    }

    /**
     * 选择性别
     */
    @OnClick(R.id.rl_sex_mine_info)
    public void editSex() {
        Utils.hideKeyBoard(this);
        chooseSexWheel.show(ll_parent_dynamic_mine_info);
    }

    /**
     * 选择地区
     */
    @OnClick(R.id.rl_address_mine_info)
    public void editAddress(View view) {
        Utils.hideKeyBoard(this);
        chooseAddressWheel.show(ll_parent_dynamic_mine_info);
    }

    /**
     * 编辑简介
     */
    @OnClick(R.id.rl_introduction_editing)
    public void introdutionEditing() {
        intent = new Intent(this, IntroductionEditingActivity.class);
        startActivity(intent);
    }

    /**
     * 编辑公司
     */
    @OnClick(R.id.rl_edit_company)
    public void editrlCompany() {
        intent = new Intent(this, AddLabelActivity.class);
        startActivity(intent);
    }

    /**
     * 编辑职位
     */
    @OnClick(R.id.rl_edit_office)
    public void editOffice() {
        intent = new Intent(this, AddLabelActivity.class);
        startActivity(intent);
    }

    /**
     * 编辑行业
     */
    @OnClick(R.id.rl_edit_industry)
    public void editIndustry() {
        intent = new Intent(this, TradeActivity.class);
        startActivityForResult(intent, 72);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnMineInfo() {
        MineInfoActivity.this.finish();
    }

    /**
     * 选择图片
     */
    private void rewardInfo() {
        final DynamicPhotoPopupWindow rewardPopupWindow = new DynamicPhotoPopupWindow(this);
        //设置Popupwindow显示位置（从底部弹出）
        rewardPopupWindow.showAtLocation(ll_parent_dynamic_mine_info, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                startActivityForResult(intent1, 70);
                rewardPopupWindow.dismiss();
            }
        });

        rewardPopupWindow.getTv_popup_choose_photo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewardPopupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 71);
                rewardPopupWindow.dismiss();
            }
        });
    }

    /**
     * 加载地区信息
     */
    private void initAddressInfo() {
        initWheel();
        initData();
    }

    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);
    }

    private void initData() {
        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) return;
            tv_address_mine_info.setText(data.Province + " " + data.City + " " + data.Area);
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
            }
        }
    }

    /**
     * 选择地址
     *
     * @param province
     * @param city
     * @param district
     */
    @Override
    public void onAddressChange(String province, String city, String district) {
        tv_address_mine_info.setText(province + " " + city + " " + district);
    }

    private List<String> paths;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 72:
                    Log.e("TAG", data.getStringExtra("trade"));
                    tvInfoCategoryName.setText(data.getStringExtra("trade"));
                    break;
                case 70:
                    //通过这种方法取出的拍摄会默认压缩，因为如果相机的像素比较高拍摄出来的图会比较高清，
                    //如果图太大会造成内存溢出（OOM），因此此种方法会默认给图片尽心压缩
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    civ_mine_info_photo.setImageBitmap(bitmap);
                    File f = HelperUtil.compressImage(bitmap);
                    paths.add(f.getPath());
                    sendPhoto();
                    break;
                case 71:
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        paths.add(path);
                        Bitmap bm = BitmapFactory.decodeFile(path);
                        civ_mine_info_photo.setImageBitmap(bm);
                        sendPhoto();
                    }
                    break;
            }
        }
    }

    private Map<String, String> paramMap;

    private void sendPhoto() {
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
                        Toast.makeText(MineInfoActivity.this, "头像上传成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MineInfoActivity.this, "头像上传失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        }
    });

}
