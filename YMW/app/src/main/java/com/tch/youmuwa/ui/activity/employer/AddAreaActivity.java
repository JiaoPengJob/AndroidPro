package com.tch.youmuwa.ui.activity.employer;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.AddProjectAddressParam;
import com.tch.youmuwa.bean.parameters.UpdatetProjecAddressParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.ProjectAddsResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.popupWindow.WorkAreaPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加地址
 */
public class AddAreaActivity extends BaseActivtiy implements PopupInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.llAddArea)
    LinearLayout llAddArea;
    @BindView(R.id.tvAreaChoose)
    TextView tvAreaChoose;
    @BindView(R.id.etDetails)
    EditText etDetails;
    /**
     * 设置的参数
     */
    private PresenterImpl<Object> presenter;//接口
    private ProjectAddsResult.MsgListBean address;//接收地址信息
    private boolean isUpdate = false;//是否为更新地址
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getSerializableExtra("address") != null) {
            isUpdate = true;
            address = (ProjectAddsResult.MsgListBean) getIntent().getSerializableExtra("address");
            tvAreaChoose.setText(address.getAddr_province() + "-" + address.getAddr_city() + "-" + address.getAddr_area());
            etDetails.setText(address.getAddr_detail());
        }
        etDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDetails.setFocusable(true);
                etDetails.setFocusableInTouchMode(true);
            }
        });
    }

    /**
     * 选择地区
     */
    @OnClick(R.id.llAreaChoose)
    public void areaChoose() {
        WorkAreaPopupWindow areaPopupWindow = new WorkAreaPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        areaPopupWindow.showAtLocation(llAddArea, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        llAddArea.setAlpha(0.8f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llAddArea.setAlpha(1f);
            }
        });
    }

    /**
     * 保存
     */
    @OnClick(R.id.btAddArea)
    public void addArea() {
        if (!TextUtils.isEmpty(tvAreaChoose.getText().toString())
                && !tvAreaChoose.getText().toString().equals("省份-城市-区县")) {
            String cityStr = tvAreaChoose.getText().toString();
            String province = cityStr.substring(0, cityStr.indexOf("-"));
            String city = cityStr.substring(cityStr.indexOf("-") + 1, cityStr.lastIndexOf("-"));
            String area = cityStr.substring(cityStr.lastIndexOf("-") + 1, cityStr.length());
            if (isUpdate) {
                updateAddress(address.getId(), province, city, area, etDetails.getText().toString());
            } else {
                clientAddress(province, city, area, etDetails.getText().toString());
            }
        } else {
            Toast.makeText(AddAreaActivity.this, "请选择城市区域！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 访问添加地址接口
     */
    private void clientAddress(String province, String city, String area, String detail) {
        mSVProgressHUD.showWithStatus("加载中...");
        AddProjectAddressParam addressParam = new AddProjectAddressParam(
                province,
                city,
                area,
                detail
        );
        presenter = new PresenterImpl<Object>(AddAreaActivity.this);
        presenter.onCreate();
        presenter.addprojectaddress(addressParam);
        presenter.attachView(addressView);
    }

    private ClientBaseView<Object> addressView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            mSVProgressHUD.dismiss();
            if (baseBean.getState() != 1) {
                Toast.makeText(AddAreaActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                AddAreaActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            mSVProgressHUD.dismiss();
            Log.e("Error", "addressView==" + result);
        }
    };

    /**
     * 更新施工地址
     */
    private void updateAddress(int id, String province, String city, String area, String detail) {
        mSVProgressHUD.showWithStatus("加载中...");
        UpdatetProjecAddressParam addressParam = new UpdatetProjecAddressParam(
                id,
                province,
                city,
                area,
                detail
        );
        presenter = new PresenterImpl<Object>(AddAreaActivity.this);
        presenter.onCreate();
        presenter.updateprojectaddress(addressParam);
        presenter.attachView(updateAddressView);
    }

    private ClientBaseView<Object> updateAddressView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(AddAreaActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                AddAreaActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "updateAddressView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatAddArea() {
        this.finish();
    }

    /**
     * 接口回调
     *
     * @param index
     * @param result
     */
    @Override
    public void getResult(int index, String result) {
        tvAreaChoose.setText(result);
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
                AddAreaActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
