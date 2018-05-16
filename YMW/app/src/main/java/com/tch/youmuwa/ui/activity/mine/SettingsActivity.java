package com.tch.youmuwa.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.AppShareResult;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.ShareInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.login.TermsServiceContentActivity;
import com.tch.youmuwa.ui.popupWindow.SharePopupWindow;
import com.tch.youmuwa.ui.popupWindow.ShareWorkerPopupWindow;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.MarketUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivtiy implements ShareInterface {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.parentSettings)
    LinearLayout parentSettings;

    private Intent intent;
    private String shareTitle, shareUrl, shareDescription, imageurl;
    private String fragmentType;
    private PresenterImpl<Object> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("设置");
        if (getIntent().getStringExtra("fragmentType") != null) {
            fragmentType = getIntent().getStringExtra("fragmentType");
        }
    }

    /**
     * 意见反馈
     */
    @OnClick(R.id.rlFeedback)
    public void feedback() {
        intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
        intent.putExtra("fragmentType", fragmentType);
        startActivity(intent);
    }

    /**
     * 用户协议
     */
    @OnClick(R.id.rlUserAgreement)
    public void userAgreement() {
        intent = new Intent(SettingsActivity.this, TermsServiceContentActivity.class);
        intent.putExtra("activity", "GSettingsActivity");
        startActivity(intent);
    }

    /**
     * 分享
     */
    @OnClick(R.id.rlSettingShare)
    public void settingShare() {
        getAppShare();
        if (fragmentType.equals("worker")) {
            ShareWorkerPopupWindow shareWorkerPopupWindow = new ShareWorkerPopupWindow(this, this);
            //设置Popupwindow显示位置（从顶部弹出）
            shareWorkerPopupWindow.showAtLocation(parentSettings, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            parentSettings.setAlpha(0.8f);
            shareWorkerPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    parentSettings.setAlpha(1f);
                }
            });
        } else {
            SharePopupWindow areaPopupWindow = new SharePopupWindow(this, this);
            //设置Popupwindow显示位置（从顶部弹出）
            areaPopupWindow.showAtLocation(parentSettings, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            parentSettings.setAlpha(0.8f);
            areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    parentSettings.setAlpha(1f);
                }
            });
        }
    }

    /**
     * 分享点击回调
     *
     * @param platform
     */
    @Override
    public void getShareResult(SHARE_MEDIA platform) {
        uMengShare(platform);
    }

    private void uMengShare(SHARE_MEDIA platform) {
        if (shareTitle != null && shareUrl != null && shareDescription != null &&
                imageurl != null) {
            UMWeb web = new UMWeb(shareUrl);
            web.setTitle(shareTitle);//标题
            UMImage image = new UMImage(SettingsActivity.this, imageurl);//网络图片
            web.setThumb(image);//缩略图
            web.setDescription(shareDescription);//描述
            new ShareAction(this)
                    .setPlatform(platform)//传入平台
                    .withMedia(web)
                    .setCallback(shareListener)//回调监听器
                    .share();
        }
    }

    /**
     * 分享的回调
     */
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(SettingsActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SettingsActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SettingsActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    /**
     * QQ与新浪微博的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatSettings() {
        SettingsActivity.this.finish();
    }

    /**
     * 关于我们
     */
    @OnClick(R.id.rlSettingAboutUs)
    public void settingAboutUs() {
        intent = new Intent(SettingsActivity.this, TermsServiceContentActivity.class);
        intent.putExtra("activity", "AboutUs");
        startActivity(intent);
    }

    /**
     * 评价我们
     */
    @OnClick(R.id.rlEvaluationUs)
    public void evaluationUs() {
        MarketUtils.launchAppDetail(SettingsActivity.this, "com.tch.youmuwa", "");
    }

    /**
     * app分享接口
     */
    private void getAppShare() {
        presenter = new PresenterImpl<Object>(SettingsActivity.this);
        presenter.onCreate();
        presenter.getshareapp();
        presenter.attachView(appShareView);
    }

    private ClientBaseView<Object> appShareView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                AppShareResult as = (AppShareResult) GsonUtil.parseJson(baseBean.getData(), AppShareResult.class);
                shareTitle = as.getTitle();
                shareUrl = as.getUrl();
                shareDescription = as.getDescription();
                imageurl = as.getImage();
            } else {
                Toast.makeText(SettingsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "appShareView--" + result);
        }
    };

}
