package com.tch.youmuwa.ui.activity.employer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.DateItem;
import com.tch.youmuwa.bean.WorkerTypeBean;
import com.tch.youmuwa.bean.parameters.SendRequireParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GotoPriceResult;
import com.tch.youmuwa.bean.result.ProjectAddsResult;
import com.tch.youmuwa.bean.result.WorkerTypeResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.CalenddarPopupInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.popupWindow.CalendarPopupWindow;
import com.tch.youmuwa.ui.popupWindow.CalendarWindow;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 需求/发布需求/公共
 */
public class ReleaseOrderActivity extends BaseActivtiy implements CalenddarPopupInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.ivPointWork)
    ImageView ivPointWork;
    @BindView(R.id.rlDayWage)
    RelativeLayout rlDayWage;
    @BindView(R.id.viewDayWage)
    View viewDayWage;
    @BindView(R.id.ivContractorWork)
    ImageView ivContractorWork;
    @BindView(R.id.rvNeedWorkType)
    RecyclerView rvNeedWorkType;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvReleaseAreaShow)
    TextView tvReleaseAreaShow;
    @BindView(R.id.parentRelease)
    LinearLayout parentRelease;
    @BindView(R.id.tvReleaseTime)
    TextView tvReleaseTime;
    @BindView(R.id.etRequireTitle)
    EditText etRequireTitle;
    @BindView(R.id.etRequireDescription)
    EditText etRequireDescription;
    @BindView(R.id.etRequireName)
    EditText etRequireName;
    @BindView(R.id.etRequirePhone)
    EditText etRequirePhone;
    @BindView(R.id.etRequirePrice)
    EditText etRequirePrice;
    @BindView(R.id.rlReleaseHomeFee)
    RelativeLayout rlReleaseHomeFee;
    @BindView(R.id.viewReleaseHomeFee)
    View viewReleaseHomeFee;
    @BindView(R.id.tvReleaseGotoPrice)
    TextView tvReleaseGotoPrice;
    @BindView(R.id.svParentRelease)
    ScrollView svParentRelease;
    /**
     * 设置的参数
     */
    private CommonAdapter adapter;//适配器
    private int nPosition = -1;//需求工种选中的脚标
    private Intent intent;//跳转
    private ProjectAddsResult.MsgListBean addsResult;//地址
    private String startTime = "", endTime = "";//开始时间和结束时间
    private int type = 1;//点工(1)或包工(2)
    private PresenterImpl<Object> presenter;//接口
    private List<WorkerTypeBean> types;//选中的工种集合
    private int areaIndex = 0;//选中的地址脚标
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_order);
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
        etRequirePrice.addTextChangedListener(new TextWatcher() {
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
                            etRequirePrice.setText("0");
                            etRequirePrice.setSelection(etRequirePrice.length());
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
        initListData();
    }

    @OnClick(R.id.parentRelease)
    public void hideInput() {
        HelperUtil.hideInput(ReleaseOrderActivity.this, parentRelease);
    }

    /**
     * 加载列表信息
     */
    private void initListData() {
        clientGetworkertype();
    }

    private void setListData(List<WorkerTypeResult.DataBean> list) {
        types = new ArrayList<WorkerTypeBean>();
        for (WorkerTypeResult.DataBean item : list) {
            types.add(new WorkerTypeBean(item.getId(), item.getName(), false));
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ReleaseOrderActivity.this, 4);
        rvNeedWorkType.setLayoutManager(gridLayoutManager);
        adapter = new CommonAdapter<WorkerTypeBean>(ReleaseOrderActivity.this, R.layout.item_worker_type, types) {
            @Override
            protected void convert(ViewHolder viewHolder, WorkerTypeBean item, int cPosition) {
                viewHolder.setText(R.id.tvWorkType, item.getTypeName());
                TextView tvWorkType = (TextView) viewHolder.getView(R.id.tvWorkType);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(HelperUtil.getScreenWidth(ReleaseOrderActivity.this) / 6, HelperUtil.getScreenHeight(ReleaseOrderActivity.this) / 18);
                tvWorkType.setLayoutParams(lp);
                if (cPosition == nPosition) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                    } else {
                        item.setSelected(true);
                    }
                }
                if (item.isSelected()) {
                    viewHolder.setTextColor(R.id.tvWorkType, Color.parseColor("#FFFFFF"));
                    viewHolder.setBackgroundRes(R.id.tvWorkType, R.drawable.oval_guide_button);
                } else {
                    viewHolder.setTextColor(R.id.tvWorkType, Color.parseColor("#444444"));
                    viewHolder.setBackgroundRes(R.id.tvWorkType, R.drawable.oval_default_type);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                nPosition = position;
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvNeedWorkType.setAdapter(adapter);
    }

    /**
     * 点工
     */
    @OnClick({R.id.ivPointWork, R.id.tvPointWork})
    public void pointWork() {
        type = 1;
        ivPointWork.setImageResource(R.mipmap.user_type_select);
        ivContractorWork.setImageResource(R.mipmap.user_type_not_select);
        rlDayWage.setVisibility(View.VISIBLE);
        viewDayWage.setVisibility(View.VISIBLE);
        rlReleaseHomeFee.setVisibility(View.GONE);
        viewReleaseHomeFee.setVisibility(View.GONE);
    }

    /**
     * 包工
     */
    @OnClick({R.id.ivContractorWork, R.id.tvContractorWork})
    public void contractorWork() {
        type = 2;
        ivPointWork.setImageResource(R.mipmap.user_type_not_select);
        ivContractorWork.setImageResource(R.mipmap.user_type_select);
        rlDayWage.setVisibility(View.GONE);
        viewDayWage.setVisibility(View.GONE);
        rlReleaseHomeFee.setVisibility(View.VISIBLE);
        viewReleaseHomeFee.setVisibility(View.VISIBLE);
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
        presenter = new PresenterImpl<Object>(ReleaseOrderActivity.this);
        presenter.onCreate();
        presenter.getsettings();
        presenter.attachView(gotoDoorView);
    }

    private ClientBaseView<Object> gotoDoorView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                GotoPriceResult gotoPrice = (GotoPriceResult) GsonUtil.parseJson(baseBean.getData(), GotoPriceResult.class);
                tvReleaseGotoPrice.setText(gotoPrice.getGoto_price());
            } else {
                Toast.makeText(ReleaseOrderActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "gotoDoorView--" + result);
        }
    };

    /**
     * 地址
     */
    @OnClick(R.id.llReleaseAreaManager)
    public void releaseAreaManager() {
        intent = new Intent(this, AreaManagerActivity.class);
        intent.putExtra("activity", "ReleaseOrderActivity");
        intent.putExtra("areaIndex", areaIndex);
        startActivityForResult(intent, 41);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 41) {
                areaIndex = data.getIntExtra("areaIndex", 0);
                addsResult = (ProjectAddsResult.MsgListBean) data.getSerializableExtra("addsResult");
                tvReleaseAreaShow.setText(addsResult.getAddr_province() + addsResult.getAddr_city() + addsResult.getAddr_area() + addsResult.getAddr_detail());
            }
        }
    }

    /**
     * 时间
     */
    @OnClick(R.id.llReleaseTime)
    public void releaseTime() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(parentRelease, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(parentRelease.getWindowToken(), 0); //强制隐藏键盘
        }
        CalendarWindow areaPopupWindow = new CalendarWindow(this, this);
        //设置Popupwindow显示位置（从顶部弹出）
        areaPopupWindow.showAtLocation(parentRelease, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 40);
        parentRelease.setAlpha(0.8f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentRelease.setAlpha(1f);
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
        tvReleaseTime.setText(list.get(0) + "-" + list.get(list.size() - 1));
        startTime = list.get(0);
        endTime = list.get(list.size() - 1);
    }

    /**
     * 立即招用
     */
    @OnClick(R.id.tvImmediatelyRecruitment)
    public void immediatelyRecruitment() {
        List<Integer> list = new ArrayList<Integer>();
        for (WorkerTypeBean type : types) {
            if (type.isSelected()) {
                list.add(type.getId());
            }
        }
        if (addsResult != null) {
            clientSendRequire(list);
        }
    }

    /**
     * 链接服务器
     */
    private void clientSendRequire(List<Integer> list) {
        mSVProgressHUD.showWithStatus("加载中...");
        SendRequireParam sendRequireParam = new SendRequireParam(
                etRequireTitle.getText().toString(),
                type,
                addsResult.getId(),
                startTime,
                endTime,
                etRequireDescription.getText().toString(),
                etRequireName.getText().toString(),
                etRequirePhone.getText().toString(),
                list,
                etRequirePrice.getText().toString()
        );

        presenter = new PresenterImpl<Object>(ReleaseOrderActivity.this);
        presenter.onCreate();
        presenter.sendrequire(sendRequireParam);
        presenter.attachView(requireView);
    }

    private ClientBaseView<Object> requireView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
//                Intent intent = new Intent(ReleaseOrderActivity.this, EmployerActivity.class);
//                intent.putExtra("aid", 2);
//                startActivity(intent);
                ReleaseOrderActivity.this.finish();
            } else {
                Toast.makeText(ReleaseOrderActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
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
     * 获取工种
     */
    private void clientGetworkertype() {
        presenter = new PresenterImpl<Object>(ReleaseOrderActivity.this);
        presenter.onCreate();
        presenter.getworkertype();
        presenter.attachView(workerTypeView);
    }

    private ClientBaseView<Object> workerTypeView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                WorkerTypeResult type = (WorkerTypeResult) GsonUtil.parseJson(baseBean, WorkerTypeResult.class);
                setListData(type.getData());
            } else {
                Toast.makeText(ReleaseOrderActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerTypeView==" + result);
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ibRetreat)
    public void retreatReleaseOrder() {
        ReleaseOrderActivity.this.finish();
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
                ReleaseOrderActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
