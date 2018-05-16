package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.BindPhoneResult;
import com.tch.kuwanx.result.VerifyCodeResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 绑定手机号
 */
public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.pbShow)
    ProgressBar pbShow;
    @BindView(R.id.viewSecond)
    View viewSecond;
    @BindView(R.id.llBindPhoneFirst)
    LinearLayout llBindPhoneFirst;
    @BindView(R.id.llBindPhoneSecond)
    LinearLayout llBindPhoneSecond;
    @BindView(R.id.llBindPhoneThird)
    LinearLayout llBindPhoneThird;
    @BindView(R.id.viewThird)
    View viewThird;
    @BindView(R.id.etInputNewPhone)
    EditText etInputNewPhone;
    @BindView(R.id.etNewPhoneCode)
    EditText etNewPhoneCode;
    @BindView(R.id.btGetNewPhoneCode)
    Button btGetNewPhoneCode;
    @BindView(R.id.tvNewPhoneNumber)
    TextView tvNewPhoneNumber;
    @BindView(R.id.tvBindPhoneOld)
    TextView tvBindPhoneOld;

    private Timer timer;
    private TimerTask task;
    private int index = 0;
    private Animation animation;
    private int codeIndex = 60;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("修改绑定手机号");
        if (getIntent().getStringExtra("phone") != null) {
            phone = getIntent().getStringExtra("phone");
            tvBindPhoneOld.setText(phone);
        }
    }

    /**
     * 下一步
     */
    @OnClick(R.id.btBindPhoneNext)
    public void bindPhoneNext() {
        if (!TextUtils.isEmpty(etInputNewPhone.getText().toString())
                && Utils.isPhone(etInputNewPhone.getText().toString())) {
            firstViewGoneAnim();
            initHalfAnimation(0);
            tvNewPhoneNumber.setText(etInputNewPhone.getText().toString());
        } else {
            Toasty.warning(BindPhoneActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 确定
     */
    @OnClick(R.id.btBindPhoneFix)
    public void bindPhoneFix() {
        if (!TextUtils.isEmpty(etNewPhoneCode.getText().toString())) {
            bindNewPhoneHttp();
        } else {
            Toasty.warning(BindPhoneActivity.this, "请输入正确的验证码！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.btGetNewPhoneCode)
    public void getNewPhoneCode() {
        if (btGetNewPhoneCode.getText().toString().equals("获取验证码")) {
            codeHttp(tvNewPhoneNumber.getText().toString());
        }
    }

    /**
     * 读秒方法
     */
    private void getCodeChange() {
        btGetNewPhoneCode.setClickable(false);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                codeIndex--;
                handler.sendEmptyMessage(4);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    /**
     * 注册：获取验证码
     */
    private void codeHttp(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("type", "1");
        String params = EncryptionUtil.getParameter(BindPhoneActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/getVerifyCode.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_bindPhoneCode")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(BindPhoneActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        VerifyCodeResult verifyCodeResult = (VerifyCodeResult) GsonUtil.json2Object(response, VerifyCodeResult.class);
                        if (verifyCodeResult != null
                                && verifyCodeResult.getRet().equals("1")) {
                            getCodeChange();
                        } else {
                            Toasty.warning(BindPhoneActivity.this, verifyCodeResult.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 绑定成功
     */
    @OnClick(R.id.btBindSuccess)
    public void bindSuccess() {
        Intent intent = new Intent();
        intent.putExtra("phone", etInputNewPhone.getText().toString());
        BindPhoneActivity.this.setResult(0, intent);
        BindPhoneActivity.this.finish();
    }

    /**
     * 绑定新手机号
     */
    private void bindNewPhoneHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(BindPhoneActivity.this));
        map.put("phone", etInputNewPhone.getText().toString());
        map.put("vcode", etNewPhoneCode.getText().toString());
        String params = EncryptionUtil.getParameter(BindPhoneActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/bindNewPhone.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_bindNewPhone")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(BindPhoneActivity.this, "绑定失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        BindPhoneResult bindPhoneResult =
                                (BindPhoneResult) GsonUtil.json2Object(response, BindPhoneResult.class);
                        if (bindPhoneResult != null &&
                                bindPhoneResult.getRet().equals("1")) {
                            secondViewGoneAnim();
                            initHalfAnimation(2);
                        } else if (bindPhoneResult.getRet().equals("2")) {
                            Toasty.warning(BindPhoneActivity.this, "绑定失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 第一个布局隐藏动画
     */
    private void firstViewGoneAnim() {
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(4000);//设置动画持续时间
        llBindPhoneFirst.startAnimation(AnimationUtils.makeOutAnimation(BindPhoneActivity.this, false));
        llBindPhoneFirst.setVisibility(View.GONE);
        llBindPhoneSecond.startAnimation(AnimationUtils.makeInAnimation(BindPhoneActivity.this, false));
        llBindPhoneSecond.setVisibility(View.VISIBLE);
    }

    /**
     * 第一个布局显示动画
     */
    private void firstViewVisibleAnim() {
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(4000);//设置动画持续时间
        llBindPhoneFirst.startAnimation(AnimationUtils.makeInAnimation(BindPhoneActivity.this, true));
        llBindPhoneFirst.setVisibility(View.VISIBLE);
        llBindPhoneSecond.startAnimation(AnimationUtils.makeOutAnimation(BindPhoneActivity.this, true));
        llBindPhoneSecond.setVisibility(View.GONE);
    }

    /**
     * 第二个布局隐藏动画
     */
    private void secondViewGoneAnim() {
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(4000);//设置动画持续时间
        llBindPhoneSecond.startAnimation(AnimationUtils.makeOutAnimation(BindPhoneActivity.this, false));
        llBindPhoneSecond.setVisibility(View.GONE);
        llBindPhoneThird.startAnimation(AnimationUtils.makeInAnimation(BindPhoneActivity.this, false));
        llBindPhoneThird.setVisibility(View.VISIBLE);
    }

    /**
     * 第二个布局显示动画
     */
    private void secondViewVisibleAnim() {
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(4000);//设置动画持续时间
        llBindPhoneSecond.startAnimation(AnimationUtils.makeInAnimation(BindPhoneActivity.this, true));
        llBindPhoneSecond.setVisibility(View.VISIBLE);
        llBindPhoneThird.startAnimation(AnimationUtils.makeOutAnimation(BindPhoneActivity.this, true));
        llBindPhoneThird.setVisibility(View.GONE);
    }

    /**
     * 设置顶部动画
     */
    private void initHalfAnimation(final int what) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = what;
                message.obj = index++;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 5);
    }

    /**
     * 设置顶部动画
     */
    private void initHalfOutAnimation(final int what) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = what;
                message.obj = index--;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 5);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int halfToRightIndex = (int) msg.obj;
                    if (halfToRightIndex < 50) {
                        pbShow.setProgress(halfToRightIndex);
                    } else if (halfToRightIndex == 50) {
                        viewSecond.setBackgroundResource(R.drawable.oval_submit_button);
                        timer.cancel();
                        task.cancel();
                        handler.removeMessages(0);
                    }
                    break;
                case 1:
                    int halfToLeftIndex = (int) msg.obj;
                    if (halfToLeftIndex > 0) {
                        pbShow.setProgress(halfToLeftIndex);
                    } else if (halfToLeftIndex == 0) {
                        viewSecond.setBackgroundResource(R.drawable.oval_filter_item);
                        timer.cancel();
                        task.cancel();
                        handler.removeMessages(1);
                    }
                    break;
                case 2:
                    int secondToRightIndex = (int) msg.obj;
                    if (secondToRightIndex < 100) {
                        pbShow.setProgress(secondToRightIndex);
                    } else if (secondToRightIndex == 100) {
                        viewThird.setBackgroundResource(R.drawable.oval_submit_button);
                        timer.cancel();
                        task.cancel();
                        handler.removeMessages(2);
                    }
                    break;
                case 3:
                    int secondToLeftIndex = (int) msg.obj;
                    if (secondToLeftIndex > 50) {
                        pbShow.setProgress(secondToLeftIndex);
                    } else if (secondToLeftIndex == 50) {
                        viewThird.setBackgroundResource(R.drawable.oval_filter_item);
                        timer.cancel();
                        task.cancel();
                        handler.removeMessages(3);
                    }
                    break;
                case 4:
                    btGetNewPhoneCode.setText(codeIndex + "秒");
                    if (codeIndex < 0) {
                        codeIndex = 60;
                        btGetNewPhoneCode.setText("获取验证码");
                        btGetNewPhoneCode.setClickable(true);
                        timer.cancel();
                        task.cancel();
                        handler.removeMessages(4);
                    }
                    break;
            }
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void bindPhoneTitleBack() {
//        backView();
        BindPhoneActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
//            backView();
            Intent intent = new Intent();
            intent.putExtra("phone", etInputNewPhone.getText().toString());
            BindPhoneActivity.this.setResult(0, intent);
            BindPhoneActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backView() {
        if (llBindPhoneFirst.getVisibility() == View.VISIBLE) {
            BindPhoneActivity.this.finish();
        } else if (llBindPhoneSecond.getVisibility() == View.VISIBLE) {
            firstViewVisibleAnim();
            initHalfOutAnimation(1);
        } else if (llBindPhoneThird.getVisibility() == View.VISIBLE) {
            secondViewVisibleAnim();
            initHalfOutAnimation(3);
        }
    }
}
