package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.UpdateImg;
import com.tch.kuwanx.result.UpdateNameResult;
import com.tch.kuwanx.result.UserCdsResult;
import com.tch.kuwanx.result.UserInfoResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.mine.article.ArticleActivity;
import com.tch.kuwanx.ui.mine.article.ArticleInfoActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 个人资料
 */
public class PersonalInformationActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvPersonalResList)
    RecyclerView rvPersonalResList;
    @BindView(R.id.tvPersonalNickname)
    TextView tvPersonalNickname;
    @BindView(R.id.tvPersonalPhone)
    TextView tvPersonalPhone;
    @BindView(R.id.llPersonalInfoParent)
    LinearLayout llPersonalInfoParent;
    @BindView(R.id.ivPersonalAvatar)
    ImageView ivPersonalAvatar;

    private CommonAdapter personalResAdapter;
    private Intent intent;
    private UserInfoResult.ResultBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("个人资料");
        btTitleFeatures.setText("保存");
        btTitleFeatures.setVisibility(View.VISIBLE);
        initPersonalResList();
        if (getIntent().getSerializableExtra("user") != null) {
            user = (UserInfoResult.ResultBean) getIntent().getSerializableExtra("user");
            initInfo();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserCdsHttp();
    }

    @BindView(R.id.tvPersonalCertification)
    TextView tvPersonalCertification;
    @BindView(R.id.tvPayPwd)
    TextView tvPayPwd;

    /**
     * 加载信息
     */
    private void initInfo() {
        if (TextUtils.isEmpty(user.getHeadpic())) {
            ivPersonalAvatar.setImageResource(R.drawable.default_head);
        } else {
            Glide.with(PersonalInformationActivity.this)
                    .load(user.getHeadpic())
                    .apply(bitmapTransform(new CropCircleTransformation()))
                    .into(ivPersonalAvatar);
        }
        tvPersonalNickname.setText(user.getNickname());
        tvPersonalPhone.setText(user.getAccount());
        if (user.getAuthedstatus().equals("1")) {
            tvPersonalCertification.setText("已认证");
        } else {
            tvPersonalCertification.setText("马上认证");
        }
        if (!TextUtils.isEmpty(user.getPaypassword())) {
            tvPayPwd.setText("已设置");
        } else {
            tvPayPwd.setText("未设置");
        }
    }

    /**
     * 加载物品列表数据
     */
    private void initPersonalResList() {
        rvPersonalResList.setLayoutManager(new GridLayoutManager(this, 4));
        rvPersonalResList.setAdapter(personalResAdapter = new CommonAdapter<UserCdsResult.ResultBean>(this,
                R.layout.item_personal_res, new ArrayList<UserCdsResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, UserCdsResult.ResultBean item, int position) {
                Glide.with(PersonalInformationActivity.this)
                        .load(item.getHeadpic())
                        .into(((ImageView) holder.getView(R.id.ivPersonalResPhoto)));
                holder.setText(R.id.tvPersonalResName, item.getName());
            }
        });
        personalResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(PersonalInformationActivity.this, ArticleInfoActivity.class);
                intent.putExtra("id", ((List<UserCdsResult.ResultBean>) personalResAdapter.getDatas()).get(position).getId());
                intent.putExtra("id", "PersonalInformationActivity");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 获取用户物品列表
     */
    private void getUserCdsHttp() {
        Map<String, Object> map = new HashMap<>();
//        map.put("userid", DaoUtils.getUserId(PersonalInformationActivity.this));
        map.put("userid", "be8c68e7aa754021b3b03c4bdca80e78");
        String params = EncryptionUtil.getParameter(PersonalInformationActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserCds.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getUserCds")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        PersonalInformationActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(PersonalInformationActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        UserCdsResult userCdsResult =
                                (UserCdsResult) GsonUtil.json2Object(response, UserCdsResult.class);
                        if (userCdsResult != null
                                && userCdsResult.getRet().equals("1")) {
                            personalResAdapter.getDatas().clear();
                            personalResAdapter.getDatas().addAll(userCdsResult.getResult());
                            personalResAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 保存
     */
    @OnClick(R.id.btTitleFeatures)
    public void keeping() {
        updateUserAvatarHttp();
    }

    /**
     * 昵称
     */
    @OnClick(R.id.rlPersonalName)
    public void rlPersonalName() {
        intent = new Intent(PersonalInformationActivity.this, UpdateNameActivity.class);
        intent.putExtra("nickname", tvPersonalNickname.getText().toString());
        startActivityForResult(intent, 0);
    }

    /**
     * 绑定手机号
     */
    @OnClick(R.id.rlPersonalPhone)
    public void personalPhone() {
        intent = new Intent(PersonalInformationActivity.this, BindPhoneActivity.class);
        intent.putExtra("phone", tvPersonalPhone.getText().toString());
        startActivityForResult(intent, 1);
    }

    /**
     * 认证身份
     */
    @OnClick(R.id.rlAuthentication)
    public void authentication() {
        if (user.getAuthedstatus().equals("2")) {
            intent = new Intent(PersonalInformationActivity.this, AuthenticationActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 支付密码
     */
    @OnClick(R.id.rlPayPwd)
    public void payPwd() {
        intent = new Intent(PersonalInformationActivity.this, PayPwdActivity.class);
        intent.putExtra("payPwdType", tvPayPwd.getText().toString());
        startActivity(intent);
    }

    /**
     * 我的地址
     */
    @OnClick(R.id.rlShipAddress)
    public void shipAddress() {
        intent = new Intent(PersonalInformationActivity.this, ShipAddressActivity.class);
        intent.putExtra("activity", "PersonalInformationActivity");
        startActivity(intent);
    }

    /**
     * 物品管理
     */
    @OnClick(R.id.rlArticleManger)
    public void articleManger() {
        intent = new Intent(PersonalInformationActivity.this, ArticleActivity.class);
        startActivity(intent);
    }

    private CustomPopWindow mAvatarPop;
    private Button btTakeAvatar, btChooseAvatar, btExitAvatar;

    /**
     * 选择头像
     */
    @OnClick(R.id.rlPersonalAvatar)
    public void personalAvatar() {
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
                .showAtLocation(llPersonalInfoParent, Gravity.BOTTOM, 0, 0);
    }

    private void takeAvatar() {
        Album.camera(this) // 相机功能。
                .image() // 拍照。
                .requestCode(10)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        if (requestCode == 10) {
                            Logger.wtf("选取的头像：" + result);
                            updateImg(result);
                            Glide.with(PersonalInformationActivity.this)
                                    .load(result)
                                    .apply(bitmapTransform(new CropCircleTransformation()))
                                    .into(ivPersonalAvatar);
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

    private void selectPhotos() {
        Album.image(PersonalInformationActivity.this) // 选择图片。
                .multipleChoice()
                .requestCode(100)
                .camera(false)
                .columnCount(4)
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
                            for (AlbumFile albumFile : result) {
                                Logger.wtf("选取的头像：" + albumFile.getPath());
                                updateImg(albumFile.getPath());
                                Glide.with(PersonalInformationActivity.this)
                                        .load(albumFile.getPath())
                                        .apply(bitmapTransform(new CropCircleTransformation()))
                                        .into(ivPersonalAvatar);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                tvPersonalNickname.setText(data.getStringExtra("nickname"));
                break;
            case 1:
                tvPersonalPhone.setText(data.getStringExtra("phone"));
                break;
        }
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
        File file = new File(path);
        EasyHttp.post(HttpUtils.URI_CENTER + "pic/upload.jhtml")
                .params("userId", DaoUtils.getUserId(PersonalInformationActivity.this))
                .params("avatar", file, file.getName(), listener)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .cacheKey(this.getClass().getSimpleName() + "_uploadAvatar")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        PersonalInformationActivity.this, "上传中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(PersonalInformationActivity.this, "上传头像失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        Logger.e("上传头像成功");
                        UpdateImg updateImg =
                                (UpdateImg) GsonUtil.json2Object(response, UpdateImg.class);
                        if (updateImg != null &&
                                updateImg.getRet().equals("1")) {
                            newAvatarPath = updateImg.getErrorstring();
                        }
                    }
                });
    }

    private String newAvatarPath = "";

    /**
     * 修改头像
     */
    private void updateUserAvatarHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(PersonalInformationActivity.this));
        if (!TextUtils.isEmpty(newAvatarPath)) {
            map.put("headpic", newAvatarPath);
        }
        map.put("nickname", tvPersonalNickname.getText().toString());
        String params = EncryptionUtil.getParameter(PersonalInformationActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/updateUser.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .cacheTime(2)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("修改头像 == " + e);
                    }

                    @Override
                    public void onSuccess(String response) {
                        UpdateNameResult updateNameResult =
                                (UpdateNameResult) GsonUtil.json2Object(response, UpdateNameResult.class);
                        if (updateNameResult != null
                                && updateNameResult.getRet().equals("1")) {
                            DaoUtils.refreshUserHeadpic(PersonalInformationActivity.this, updateNameResult.getResult().getHeadpic());
                            DaoUtils.refreshUserName(PersonalInformationActivity.this, updateNameResult.getResult().getNickname());
                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                                @Override
                                public UserInfo getUserInfo(String userId) {
                                    return new UserInfo(
                                            DaoUtils.getUserId(PersonalInformationActivity.this),
                                            DaoUtils.getUser(PersonalInformationActivity.this).getNickname(),
                                            Uri.parse(DaoUtils.getUser(PersonalInformationActivity.this).getHeadpic())
                                    );//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                                }
                            }, true);
//                            RongIM.getInstance().setCurrentUserInfo(new UserInfo(
//                                    DaoUtils.getUserId(PersonalInformationActivity.this),
//                                    DaoUtils.getUser(PersonalInformationActivity.this).getNickname(),
//                                    Uri.parse(DaoUtils.getUser(PersonalInformationActivity.this).getHeadpic())));
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(DaoUtils.getUserId(PersonalInformationActivity.this),
                                    DaoUtils.getUser(PersonalInformationActivity.this).getNickname(),
                                    Uri.parse(DaoUtils.getUser(PersonalInformationActivity.this).getHeadpic())));
                            Toasty.warning(PersonalInformationActivity.this, "保存成功！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void personalInfoTitleBack() {
        PersonalInformationActivity.this.finish();
    }
}
