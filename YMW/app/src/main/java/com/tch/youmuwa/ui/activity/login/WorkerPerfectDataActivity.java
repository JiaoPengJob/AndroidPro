package com.tch.youmuwa.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.CompleteParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.WorkerLoginResult;
import com.tch.youmuwa.bean.result.WorkerTypeResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.listener.BaiduLocationListener;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.myinterface.WorkerTypeInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.MainActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerMainActivity;
import com.tch.youmuwa.ui.popupWindow.WorkAreaPopupWindow;
import com.tch.youmuwa.ui.popupWindow.WorkTypePopupWindow;
import com.tch.youmuwa.ui.popupWindow.WorkYearPopupWindow;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善资料
 */
public class WorkerPerfectDataActivity extends BaseActivtiy implements PopupInterface, WorkerTypeInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.parentWorkerPerfectData)
    LinearLayout parentWorkerPerfectData;
    @BindView(R.id.tvWorkerType)
    TextView tvWorkerType;
    @BindView(R.id.tvWorkerYear)
    TextView tvWorkerYear;
    @BindView(R.id.tvWorkerArea)
    TextView tvWorkerArea;
    @BindView(R.id.ivWorkerTermsSelect)
    ImageView ivWorkerTermsSelect;
    @BindView(R.id.etPerfectRName)
    EditText etPerfectRName;
    @BindView(R.id.etPerfectRCode)
    EditText etPerfectRCode;
    @BindView(R.id.etWPerfectRAge)
    EditText etWPerfectRAge;
    /**
     * 设置的参数
     */
    private boolean isTermsSelect = true;//是否同意条款
    private Intent intent;//跳转
    private PresenterImpl<Object> presenter;//接口
    private String province = "", city = "", area = "";//地区
    private String workerAge = "0";//工龄
    private int workerType = -1;//工种id
    private SVProgressHUD mSVProgressHUD;//加载显示
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener;
    private double latitude, longitude;
    private WorkerLoginResult workerLoginResult;
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;//数据库
    private String pwd, phone;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_perfect_data);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        title.setText("完善资料");
        tvWorkerYear.setText(HelperUtil.getCurrentTime("yyyy") + "-" + (Integer.parseInt(HelperUtil.getCurrentTime("yyyy")) - 1) + "年");
        if (getIntent().getSerializableExtra("workerLogin") != null) {
            workerLoginResult = (WorkerLoginResult) getIntent().getSerializableExtra("workerLogin");
        }
        if (getIntent().getStringExtra("phone") != null) {
            phone = getIntent().getStringExtra("phone");
        }
        if (getIntent().getStringExtra("pwd") != null) {
            pwd = getIntent().getStringExtra("pwd");
        }
        type = getIntent().getIntExtra("type", 0);

        location();

        etPerfectRCode.addTextChangedListener(new TextWatcher() {
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
                        Message msg = new Message();
                        msg.obj = editable.toString();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } else {
                        Toast.makeText(WorkerPerfectDataActivity.this, "身份证号不合法！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @OnClick(R.id.parentWorkerPerfectData)
    public void hideInput() {
        HelperUtil.hideInput(WorkerPerfectDataActivity.this, parentWorkerPerfectData);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            etWPerfectRAge.setText(String.valueOf(HelperUtil.getAgeByIDNumber(msg.obj.toString())));
        }
    };

    /**
     * 选择工种
     */
    @OnClick(R.id.rlTypes)
    public void typeSelect() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentWorkerPerfectData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentWorkerPerfectData.getWindowToken(), 0); //强制隐藏键盘
        }
        WorkTypePopupWindow popupWindow = new WorkTypePopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        popupWindow.showAtLocation(parentWorkerPerfectData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        parentWorkerPerfectData.setAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentWorkerPerfectData.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(WorkerTypeResult.DataBean type) {
        tvWorkerType.setText(type.getName());
        workerType = type.getId();
    }

    /**
     * 选择工龄
     */
    @OnClick(R.id.rlWorkYear)
    public void workYear() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentWorkerPerfectData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentWorkerPerfectData.getWindowToken(), 0); //强制隐藏键盘
        }
        WorkYearPopupWindow yearPopupWindow = new WorkYearPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        yearPopupWindow.showAtLocation(parentWorkerPerfectData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        parentWorkerPerfectData.setAlpha(0.8f);
        yearPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentWorkerPerfectData.setAlpha(1f);
            }
        });
    }

    /**
     * 选择施工区域
     */
    @OnClick(R.id.rlWorkArea)
    public void workArea() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentWorkerPerfectData, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentWorkerPerfectData.getWindowToken(), 0); //强制隐藏键盘
        }
        WorkAreaPopupWindow areaPopupWindow = new WorkAreaPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        areaPopupWindow.showAtLocation(parentWorkerPerfectData, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        parentWorkerPerfectData.setAlpha(0.8f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentWorkerPerfectData.setAlpha(1f);
            }
        });
    }

    /**
     * 是否同意服务条款
     */
    @OnClick(R.id.ivWorkerTermsSelect)
    public void workerTermsSelect() {
        if (isTermsSelect) {
            isTermsSelect = false;
            ivWorkerTermsSelect.setImageResource(R.mipmap.terms_service_not_select);
        } else {
            isTermsSelect = true;
            ivWorkerTermsSelect.setImageResource(R.mipmap.worker_terms_service_select);
        }
    }

    /**
     * 注册
     */
    @OnClick(R.id.btWorkerRegister)
    public void workerRegister() {
        if (isTermsSelect) {
            if (longitude != 0 && latitude != 0) {
                if (!TextUtils.isEmpty(etWPerfectRAge.getText().toString())
                        && !TextUtils.isEmpty(etPerfectRName.getText().toString())
                        && !TextUtils.isEmpty(etPerfectRCode.getText().toString())
                        && !etWPerfectRAge.getText().toString().equals("0")
                        && !tvWorkerType.getText().toString().equals("请选择您的工种")
                        && !tvWorkerArea.getText().toString().equals("省份-城市-区县")
                        ) {
                    clientComplete();
                } else {
                    Toast.makeText(WorkerPerfectDataActivity.this, "资料未完善", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(WorkerPerfectDataActivity.this, "正在进行定位...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(WorkerPerfectDataActivity.this, "您还未同意游木蛙服务条款!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 服务条款
     */
    @OnClick(R.id.tvWorkerTermsServiceContent)
    public void workerTermsServiceContent() {
        intent = new Intent(WorkerPerfectDataActivity.this, TermsServiceContentActivity.class);
        intent.putExtra("activity", "RegisterActivity");
        startActivity(intent);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreat() {
        this.finish();
    }

    @Override
    public void getResult(int index, String result) {
        if (index == 1) {
            tvWorkerYear.setText(result);
            workerAge = result;
        } else if (index == 2) {
            tvWorkerArea.setText(result);
            String cityStr = tvWorkerArea.getText().toString();
            province = cityStr.substring(0, cityStr.indexOf("-"));
            city = cityStr.substring(cityStr.indexOf("-") + 1, cityStr.lastIndexOf("-"));
            area = cityStr.substring(cityStr.lastIndexOf("-") + 1, cityStr.length());
        }
    }

    /**
     * 链接服务器
     */
    private void clientComplete() {
        mSVProgressHUD.showWithStatus("加载中...");
        CompleteParam completeParam = new CompleteParam(
                etPerfectRName.getText().toString(),
                etPerfectRCode.getText().toString(),
                Integer.parseInt(etWPerfectRAge.getText().toString()),
                workerType,
                workerAge,
                province,
                city,
                area,
                String.valueOf(longitude + "," + latitude)
        );
        presenter = new PresenterImpl<Object>(WorkerPerfectDataActivity.this);
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
                if (mLocationClient != null) {
                    mLocationClient.stop();
                }
                setUserDaoInfo(phone, pwd, 2, 1);
                Intent intent = new Intent(WorkerPerfectDataActivity.this, WorkerMainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(WorkerPerfectDataActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
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

    private void location() {
        myListener = new MyLocationListenner();
        mLocationClient = new LocationClient(WorkerPerfectDataActivity.this);
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

    public class MyLocationListenner implements BDLocationListener {

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
                WorkerPerfectDataActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }

    /**
     * 保存用户信息
     */
    private void setUserDaoInfo(String phone, String pwd, int type, int result) {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.deleteAll();
        UserInfo userinfo = new UserInfo();
        userinfo.setPhone(phone);
        userinfo.setPwd(pwd);
        userinfo.setType(type);
        userinfo.setResult(result);
        userInfoDao.insert(userinfo);
    }
}
