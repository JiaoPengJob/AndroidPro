package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.PointFinishedParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.HelperUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实际工资/点工
 */
public class PointWageActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etContractorSureDay)
    EditText etContractorSureDay;
    @BindView(R.id.tvContractorDayPrice)
    TextView tvContractorDayPrice;
    @BindView(R.id.tvContractorPriceCount)
    TextView tvContractorPriceCount;

    private int id;
    private double price;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_wage);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("辞退工资");
        id = getIntent().getIntExtra("id", 0);
        price = getIntent().getDoubleExtra("price", 0);
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(PointWageActivity.this);
        tvContractorDayPrice.setText(String.valueOf(price));
        etContractorSureDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && !charSequence.toString().equals("")) {
                    Message message = new Message();
                    message.obj = charSequence.toString();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                double num = Double.parseDouble(msg.obj.toString());
                tvContractorPriceCount.setText(String.valueOf(price * num));
            }
        }
    };

    /**
     * 提交
     */
    @OnClick(R.id.btContractorSubmit)
    public void contractorSubmit() {
        if (!TextUtils.isEmpty(etContractorSureDay.getText().toString())) {
            mSVProgressHUD.showWithStatus("加载中...");
            PointFinishedParam pointFinished = new PointFinishedParam(
                    id,
                    Integer.parseInt(etContractorSureDay.getText().toString())
            );
            presenter = new PresenterImpl<Object>(PointWageActivity.this);
            presenter.onCreate();
            presenter.dismiss1(pointFinished);
            presenter.attachView(achievePointView);
        } else {
            Toast.makeText(PointWageActivity.this, "天数不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private ClientBaseView<Object> achievePointView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                Intent intent = new Intent(PointWageActivity.this, WorkerMainActivity.class);
                intent.putExtra("aid", 1);
                startActivity(intent);
                PointWageActivity.this.finish();
            } else {
                Toast.makeText(PointWageActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "achievePointView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatContractorWage() {
        PointWageActivity.this.finish();
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
                PointWageActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
