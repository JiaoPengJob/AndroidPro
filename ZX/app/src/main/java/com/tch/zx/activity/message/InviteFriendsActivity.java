package com.tch.zx.activity.message;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.bean.PeoplePhoneInfo;
import com.tch.zx.service.FindNewFriendsService;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.util.WXUtil;
import com.tch.zx.view.PhoneNumPopupWindow;
import com.tch.zx.view.RewardPopupWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 邀请好友
 */
public class InviteFriendsActivity extends BaseActivity {
    /**
     * 头标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 父布局
     */
    @BindView(R.id.ll_parent_invite)
    LinearLayout ll_parent_invite;
    /**
     * 跳转
     */
    private Intent intent;
    /**
     * 设置popupwindow的布局参数
     */
    private WindowManager.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);

        intiView();

    }

    /**
     * 初始化页面
     */
    private void intiView() {
        tv_title_top_all.setText("邀请好友");
    }

    /**
     * 手机联系人
     */
    @OnClick(R.id.rl_intent_phone_book)
    public void intentPhoneBook() {
        intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    /**
     * Activity回调函数
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //如果当前版本大于等于Android 6.0，且该权限未被授予，则申请授权
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    //申请授权，第一个参数为要申请用户授权的权限；第二个参数为requestCode 必须大于等于0，主要用于回调的时候检测，匹配特定的onRequestPermissionsResult。
                    //可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 40);

                } else {
                    //如果该版本低于6.0，或者该权限已被授予，它则可以继续读取联系人。
                    //处理返回的data,获取选择的联系人信息
                    Uri uri = data.getData();
                    PeoplePhoneInfo info = HelperUtil.getPhoneContacts(this, uri);
                    Log.d("TAG", "姓名=" + info.getName());
                    for (String num : info.getPhone()) {
                        Log.d("TAG", "手机号=" + num);
                    }
                    showInfo(info.getPhone());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 显示选中的手机号
     */
    private void showInfo(List<String> list) {
        PhoneNumPopupWindow rewardPopupWindow = new PhoneNumPopupWindow(this, list);
        //设置Popupwindow显示位置（从底部弹出）
        rewardPopupWindow.showAtLocation(ll_parent_invite, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 40) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户成功授予权限
            } else {
                Toast.makeText(this, "你拒绝了此应用对读取联系人权限的申请！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 微信联系人
     */
    @OnClick(R.id.rl_intent_wechat_friends)
    public void intentWechatfriends() {
//        WXUtil.shareWeb();
    }

    /**
     * 导入通讯录
     */
    @OnClick(R.id.rl_intent_im_book)
    public void intentImBook() {
        intent = new Intent(this, FindNewFriendsService.class);
        startService(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void backInviteFriends() {
        this.finish();
    }

}
