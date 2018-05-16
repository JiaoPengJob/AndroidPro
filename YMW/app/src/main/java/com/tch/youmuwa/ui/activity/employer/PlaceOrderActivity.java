package com.tch.youmuwa.ui.activity.employer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.DateItem;
import com.tch.youmuwa.bean.parameters.SendRequireToWorkerParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GotoPriceResult;
import com.tch.youmuwa.bean.result.ProjectAddsResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.CalenddarPopupInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.popupWindow.CalendarPopupWindow;
import com.tch.youmuwa.ui.popupWindow.CalendarWindow;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 下单/指定工人
 */
public class PlaceOrderActivity extends BaseActivtiy implements CalenddarPopupInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ivPointWork)
    ImageView ivPointWork;
    @BindView(R.id.ivContractorWork)
    ImageView ivContractorWork;
    @BindView(R.id.rlDayWage)
    RelativeLayout rlDayWage;
    @BindView(R.id.viewDayWage)
    View viewDayWage;
    @BindView(R.id.tvWorkArea)
    TextView tvWorkArea;
    @BindView(R.id.parentPlaceOrder)
    LinearLayout parentPlaceOrder;
    @BindView(R.id.tvWorkerTime)
    TextView tvWorkerTime;
    @BindView(R.id.etRecruitmentTitle)
    EditText etRecruitmentTitle;
    @BindView(R.id.etConstructionDescription)
    EditText etConstructionDescription;
    @BindView(R.id.etContactPerson)
    EditText etContactPerson;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.rlPlaceHomeFee)
    RelativeLayout rlPlaceHomeFee;
    @BindView(R.id.viewPlaceHomeFee)
    View viewPlaceHomeFee;
    @BindView(R.id.rlPlaceOrderWorkerType)
    RelativeLayout rlPlaceOrderWorkerType;
    @BindView(R.id.viewPlaceOrderWorkerType)
    View viewPlaceOrderWorkerType;
    @BindView(R.id.tvPlaceWorkerType)
    TextView tvPlaceWorkerType;
    @BindView(R.id.tvGotoDoorPrice)
    TextView tvGotoDoorPrice;
    /**
     * 设置的参数
     */
    private Intent intent;//跳转
    private ProjectAddsResult.MsgListBean addsResult;//地址
    private PresenterImpl<Object> presenter;//接口
    private int workType = 1;//1:点工或2:包工
    private int workerId;//工人id
    private String startTime = "", endTime = "";//开始和结束时间
    private String isSpecify = "";//是否是指定工人
    private int areaIndex = 0;//选中的地址脚标
    private SVProgressHUD mSVProgressHUD;//加载显示
    private String price = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("下单");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        workerId = getIntent().getIntExtra("workerId", 0);
        if (getIntent().getStringExtra("isSpecify") != null) {
            isSpecify = getIntent().getStringExtra("isSpecify");
            if (!isSpecify.equals("")) {
                rlPlaceOrderWorkerType.setVisibility(View.VISIBLE);
                viewPlaceOrderWorkerType.setVisibility(View.VISIBLE);
                tvPlaceWorkerType.setText(isSpecify);
            } else {
                rlPlaceOrderWorkerType.setVisibility(View.GONE);
                viewPlaceOrderWorkerType.setVisibility(View.GONE);
            }
        }

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                if (temp.length() == 2) {
                    if (temp.indexOf("0") == 0) {
                        if (!temp.substring(1).equals(".")) {
                            etPrice.setText("0");
                            etPrice.setSelection(etPrice.length());
                        }
                    }
                }
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    editable.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    @OnClick(R.id.parentPlaceOrder)
    public void hideInput() {
        HelperUtil.hideInput(PlaceOrderActivity.this, parentPlaceOrder);
    }

    /**
     * 点工
     */
    @OnClick({R.id.ivPointWork, R.id.tvPointWork})
    public void pointWork() {
        workType = 1;
        ivPointWork.setImageResource(R.mipmap.user_type_select);
        ivContractorWork.setImageResource(R.mipmap.user_type_not_select);
        rlDayWage.setVisibility(View.VISIBLE);
        viewDayWage.setVisibility(View.VISIBLE);
        rlPlaceHomeFee.setVisibility(View.GONE);
        viewPlaceHomeFee.setVisibility(View.GONE);
    }

    /**
     * 包工
     */
    @OnClick({R.id.ivContractorWork, R.id.tvContractorWork})
    public void contractorWork() {
        workType = 2;
        ivPointWork.setImageResource(R.mipmap.user_type_not_select);
        ivContractorWork.setImageResource(R.mipmap.user_type_select);
        rlDayWage.setVisibility(View.GONE);
        viewDayWage.setVisibility(View.GONE);
        rlPlaceHomeFee.setVisibility(View.VISIBLE);
        viewPlaceHomeFee.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getsettings();
        }
    };

    /**
     * 获取上门费
     */
    private void getsettings() {
        presenter = new PresenterImpl<Object>(PlaceOrderActivity.this);
        presenter.onCreate();
        presenter.getsettings();
        presenter.attachView(gotoDoorView);
    }

    private ClientBaseView<Object> gotoDoorView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                GotoPriceResult gotoPrice = (GotoPriceResult) GsonUtil.parseJson(baseBean.getData(), GotoPriceResult.class);
                tvGotoDoorPrice.setText(gotoPrice.getGoto_price());
            } else {
                Toast.makeText(PlaceOrderActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "gotoDoorView--" + result);
        }
    };

    /**
     * 施工地点
     */
    @OnClick(R.id.llAreaManager)
    public void areaManager() {
        intent = new Intent(this, AreaManagerActivity.class);
        intent.putExtra("activity", "PlaceOrderActivity");
        startActivityForResult(intent, 40);
    }

    /**
     * 立即招用
     */
    @OnClick(R.id.tvImmediatelyRecruitment)
    public void immediatelyRecruitment() {
        if (rlDayWage.getVisibility() == View.VISIBLE) {
            price = etPrice.getText().toString();
        } else {
            price = "";
        }
        if (addsResult != null) {
            clientSendRequire();
        }

    }

    /**
     * 下需求单
     */
    private void clientSendRequire() {
        mSVProgressHUD.showWithStatus("下单中...");
        SendRequireToWorkerParam sendRequireToWorkerParam = new SendRequireToWorkerParam(
                etRecruitmentTitle.getText().toString(),
                workType,
                workerId,
                addsResult.getId(),
                startTime,
                endTime,
                etConstructionDescription.getText().toString(),
                etContactPerson.getText().toString(),
                etPhone.getText().toString(),
                price
        );
        presenter = new PresenterImpl<Object>(PlaceOrderActivity.this);
        presenter.onCreate();
        presenter.sendrequiretoworker(sendRequireToWorkerParam);
        presenter.attachView(requiretoWorkerView);
    }

    private ClientBaseView<Object> requiretoWorkerView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
//                Intent intent = new Intent(PlaceOrderActivity.this, EmployerActivity.class);
//                intent.putExtra("aid", 2);
//                startActivity(intent);
                PlaceOrderActivity.this.finish();
            } else {
                Toast.makeText(PlaceOrderActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "requireView==" + result);
        }
    };

    /**
     * 时间
     */
    @OnClick(R.id.llWorkerTime)
    public void workerTime() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentPlaceOrder, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentPlaceOrder.getWindowToken(), 0); //强制隐藏键盘
        }
        CalendarWindow areaPopupWindow = new CalendarWindow(this, this);
        //设置Popupwindow显示位置（从顶部弹出）
        areaPopupWindow.showAtLocation(parentPlaceOrder, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 40);
        parentPlaceOrder.setAlpha(0.8f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentPlaceOrder.setAlpha(1f);
            }
        });
    }

    /**
     * 日历数据回调
     *
     * @param list
     */
    @Override
    public void getResult(List<String> list) {
        //对时间进行排序
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Date date1 = HelperUtil.simpleDate(lhs);
                Date date2 = HelperUtil.simpleDate(rhs);
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.after(date2)) {
                    return 1;
                }
                return -1;
            }
        });
        tvWorkerTime.setText(list.get(0) + "-" + list.get(list.size() - 1));
        startTime = list.get(0);
        endTime = list.get(list.size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 40) {
                areaIndex = data.getIntExtra("areaIndex", 0);
                addsResult = (ProjectAddsResult.MsgListBean) data.getSerializableExtra("addsResult");
                tvWorkArea.setText(addsResult.getAddr_province() + addsResult.getAddr_city() + addsResult.getAddr_area() + addsResult.getAddr_detail());
            }
        }
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatPlaceOrder() {
        PlaceOrderActivity.this.finish();
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
                PlaceOrderActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
