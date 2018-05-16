package com.tch.youmuwa.ui.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.CompleteParam;
import com.tch.youmuwa.bean.parameters.GetCodeParam;
import com.tch.youmuwa.bean.parameters.RePasswordParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.WorkerTypeResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.myinterface.WorkerTypeInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.ui.activity.login.WorkerPerfectDataActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerMainActivity;
import com.tch.youmuwa.ui.popupWindow.EmployerAreaPopupWindow;
import com.tch.youmuwa.ui.popupWindow.EmployerTypePopupWindow;
import com.tch.youmuwa.ui.popupWindow.EmployerWorkYearPopupWindow;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善资料
 */
public class PerfectDataActivity extends BaseActivtiy implements PopupInterface, WorkerTypeInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.tvGetPPwdVerificationCode)
    TextView tvGetPPwdVerificationCode;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.parentPerfectData)
    LinearLayout parentPerfectData;
    @BindView(R.id.tvPDWorkerType)
    TextView tvPDWorkerType;
    @BindView(R.id.tvPDWorkerYear)
    TextView tvPDWorkerYear;
    @BindView(R.id.tvPDWorkerArea)
    TextView tvPDWorkerArea;
    @BindView(R.id.llPerfectDataPwdParent)
    LinearLayout llPerfectDataPwdParent;
    @BindView(R.id.etPerfectName)
    EditText etPerfectName;
    @BindView(R.id.etPerfectCode)
    EditText etPerfectCode;
    @BindView(R.id.ivPerfectDataPwdEye)
    ImageView ivPerfectDataPwdEye;
    @BindView(R.id.etPerfectDataPwd)
    EditText etPerfectDataPwd;
    @BindView(R.id.etRePerfectDataPwd)
    EditText etRePerfectDataPwd;
    @BindView(R.id.ivRePerfectDataPwdEye)
    ImageView ivRePerfectDataPwdEye;
    @BindView(R.id.etPerfectAge)
    EditText etPerfectAge;
    @BindView(R.id.etPCode)
    EditText etPCode;
    /**
     * 设置的参数
     */
    private Timer timer;
    private TimerTask timerTask;
    private int index = 60;//计时器
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库
    private PresenterImpl<Object> presenter;//接口
    private int workerType = 1;//记录选择的工种的id
    private String province = "", city = "", area = "";//地区
    private String workerAge = "0";//工龄
    private SVProgressHUD mSVProgressHUD;//加载显示
    private boolean isPwdClose = true;//密码是否隐藏
    private boolean isRePwdClose = true;//确认密码是否隐藏
    private double latitude = 0, longitude = 0;
    private MyLocationListener myListener;
    private LocationClient mLocationClient = null;
    private List<UserInfo> userInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_data);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("完善资料");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        userInfos = userInfoDao.loadAll();
        if (SharedPrefsUtil.getValue(PerfectDataActivity.this, "ifPwdLogin", true)) {
            llPerfectDataPwdParent.setVisibility(View.GONE);
        } else {
            llPerfectDataPwdParent.setVisibility(View.VISIBLE);
        }

        etPerfectCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int after_length = editable.length();
                if (after_length == 15 || after_length == 18) {
                    if (HelperUtil.isLegalId(editable.toString())) {
                        etPerfectAge.setText(String.valueOf(HelperUtil.getAgeByIDNumber(editable.toString())));
                    } else {
                        Toast.makeText(PerfectDataActivity.this, "身份证号不合法！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @OnClick(R.id.parentPerfectData)
    public void hideInput() {
        HelperUtil.hideInput(PerfectDataActivity.this, parentPerfectData);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvGetPPwdVerificationCode)
    public void getPPwdVerificationCode() {
        if (tvGetPPwdVerificationCode.getText().toString().equals("获取验证码")) {
            getCode();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tvGetPPwdVerificationCode.setText(index + "秒");
                if (index < 0) {
                    index = 60;
                    tvGetPPwdVerificationCode.setText("获取验证码");
                    cancelTimer();
                }
            } else if (msg.what == 1) {
                if (!TextUtils.isEmpty(etPerfectAge.getText().toString())
                        && !TextUtils.isEmpty(etPerfectAge.getText().toString())
                        && !TextUtils.isEmpty(etPerfectName.getText().toString())
                        && !TextUtils.isEmpty(etPerfectCode.getText().toString())
                        && !tvPDWorkerType.getText().toString().equals("请选择您的工种")
                        && !tvPDWorkerArea.getText().toString().equals("省份-城市-区县")
                        ) {
                    clientComplete();
                } else {
                    Toast.makeText(PerfectDataActivity.this, "资料未完善", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 获取验证码
     */
    private void getCode() {
        CacheDataManager.clearAllCache(PerfectDataActivity.this);
        GetCodeParam getCodeParam = new GetCodeParam("", 3);
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.attachView(getCodeView);
        presenter.getsms(getCodeParam);
    }

    private ClientBaseView<Object> getCodeView = new ClientBaseView<Object>() {

        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
//            Toast.makeText(PerfectDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                etPCode.setFocusable(true);
                etPCode.setFocusableInTouchMode(true);
                etPCode.requestFocus();
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        index--;
                        handler.sendEmptyMessage(0);
                    }
                };
                timer.schedule(timerTask, 0, 1000);
            } else {
                Toast.makeText(PerfectDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "getCodeView:--" + result);
        }
    };

    /**
     * 清空计时器
     */
    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        handler.removeMessages(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    /**
     * 工种
     */
    @OnClick(R.id.rlPDTypes)
    public void pDTypes() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentPerfectData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentPerfectData.getWindowToken(), 0); //强制隐藏键盘
        }
        EmployerTypePopupWindow popupWindow = new EmployerTypePopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        popupWindow.showAtLocation(parentPerfectData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        parentPerfectData.setAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentPerfectData.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(WorkerTypeResult.DataBean type) {
        tvPDWorkerType.setText(type.getName());
        workerType = type.getId();
    }

    /**
     * 工龄
     */
    @OnClick(R.id.rlPDWorkYear)
    public void rlPDWorkYear() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentPerfectData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentPerfectData.getWindowToken(), 0); //强制隐藏键盘
        }
        EmployerWorkYearPopupWindow yearPopupWindow = new EmployerWorkYearPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        yearPopupWindow.showAtLocation(parentPerfectData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        parentPerfectData.setAlpha(0.8f);
        yearPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentPerfectData.setAlpha(1f);
            }
        });
    }

    /**
     * 区域
     */
    @OnClick(R.id.rlPDWorkArea)
    public void rlPDWorkArea() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentPerfectData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentPerfectData.getWindowToken(), 0); //强制隐藏键盘
        }
        EmployerAreaPopupWindow areaPopupWindow = new EmployerAreaPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        areaPopupWindow.showAtLocation(parentPerfectData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        parentPerfectData.setAlpha(0.8f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentPerfectData.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(int index, String result) {
        if (index == 1) {
            tvPDWorkerYear.setText(result);
            workerAge = result;
        } else if (index == 2) {
            tvPDWorkerArea.setText(result);
            String cityStr = tvPDWorkerArea.getText().toString();
            province = cityStr.substring(0, cityStr.indexOf("-"));
            city = cityStr.substring(cityStr.indexOf("-") + 1, cityStr.lastIndexOf("-"));
            area = cityStr.substring(cityStr.lastIndexOf("-") + 1, cityStr.length());
        }
    }

    /**
     * 确定
     */
    @OnClick(R.id.btPerfectDataDone)
    public void perfectDataDone() {
        initLocation();
        if (longitude != 0 && latitude != 0) {
            if (SharedPrefsUtil.getValue(PerfectDataActivity.this, "ifPwdLogin", true)) {
                if (!TextUtils.isEmpty(etPerfectAge.getText().toString())
                        && !TextUtils.isEmpty(etPerfectAge.getText().toString())
                        && !TextUtils.isEmpty(etPerfectName.getText().toString())
                        && !TextUtils.isEmpty(etPerfectCode.getText().toString())
                        && !tvPDWorkerType.getText().toString().equals("请选择您的工种")
                        && !tvPDWorkerArea.getText().toString().equals("省份-城市-区县")
                        ) {
                    clientComplete();
                } else {
                    Toast.makeText(PerfectDataActivity.this, "资料未完善", Toast.LENGTH_SHORT).show();
                }
            } else {
                clientRePassword();
            }

        }
    }

    /**
     * 重置密码
     */
    private void clientRePassword() {
        CacheDataManager.clearAllCache(PerfectDataActivity.this);
        RePasswordParam rePasswordParam = new RePasswordParam(
                Integer.parseInt(etPCode.getText().toString()),
                etPerfectDataPwd.getText().toString(),
                etRePerfectDataPwd.getText().toString()
        );
        presenter = new PresenterImpl<Object>(PerfectDataActivity.this);
        presenter.onCreate();
        presenter.repassword(rePasswordParam);
        presenter.attachView(rePwdView);
    }

    private ClientBaseView<Object> rePwdView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                handler.sendEmptyMessage(1);
            } else {
                Toast.makeText(PerfectDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "rePwdView==" + result);
        }
    };

    /**
     * 完善信息
     */
    private void clientComplete() {
        CacheDataManager.clearAllCache(PerfectDataActivity.this);
        mSVProgressHUD.showWithStatus("加载中...");
        CompleteParam completeParam = new CompleteParam(
                etPerfectName.getText().toString(),
                etPerfectCode.getText().toString(),
                Integer.parseInt(etPerfectAge.getText().toString()),
                workerType,
                workerAge,
                province,
                city,
                area,
                String.valueOf(longitude + "," + latitude)
        );
        presenter = new PresenterImpl<Object>(PerfectDataActivity.this);
        presenter.onCreate();
        presenter.complete(completeParam);
        presenter.attachView(completeView);
    }

    private ClientBaseView<Object> completeView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() == 1) {
                switchRoles();
            } else {
                Toast.makeText(PerfectDataActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "completeView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatPerfectData() {
//        Intent intent = new Intent(PerfectDataActivity.this, EmployerActivity.class);
//        intent.putExtra("aid", 3);
//        startActivity(intent);
        PerfectDataActivity.this.finish();
    }

    /**
     * 密码是否可见
     */
    @OnClick(R.id.ivPerfectDataPwdEye)
    public void perfectDataPwdEye() {
        if (isPwdClose) {
            isPwdClose = false;
            ivPerfectDataPwdEye.setImageResource(R.mipmap.pwd_open);
            etPerfectDataPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isPwdClose = true;
            ivPerfectDataPwdEye.setImageResource(R.mipmap.pwd_close);
            etPerfectDataPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 确认密码是否可见
     */
    @OnClick(R.id.ivRePerfectDataPwdEye)
    public void rePerfectDataPwdEye() {
        if (isRePwdClose) {
            isRePwdClose = false;
            ivRePerfectDataPwdEye.setImageResource(R.mipmap.pwd_open);
            etRePerfectDataPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            isRePwdClose = true;
            ivRePerfectDataPwdEye.setImageResource(R.mipmap.pwd_close);
            etRePerfectDataPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * 添加定位
     */
    private void initLocation() {
        myListener = new MyLocationListener();
        mLocationClient = new LocationClient(PerfectDataActivity.this);
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");//必加
        option.setScanSpan(0);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 切换角色
     */
    private void switchRoles() {
        CacheDataManager.clearAllCache(PerfectDataActivity.this);
        presenter = new PresenterImpl<Object>(PerfectDataActivity.this);
        presenter.onCreate();
        presenter.switchroles(2);
        presenter.attachView(SwitchRolesView);
    }

    private ClientBaseView<Object> SwitchRolesView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                SharedPrefsUtil.putValue(PerfectDataActivity.this, "isEmployer", false);
                Intent intent = new Intent(PerfectDataActivity.this, WorkerMainActivity.class);
                startActivity(intent);
                PerfectDataActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SwitchRolesView==" + result);
        }
    };

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
                Intent intent = new Intent(PerfectDataActivity.this, EmployerActivity.class);
                intent.putExtra("aid", 3);
                startActivity(intent);
                PerfectDataActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
