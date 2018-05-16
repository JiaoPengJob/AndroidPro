package com.tch.youmuwa.ui.activity.worker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.ContractorDisParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实际工资/包工
 */
public class ActualWageActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etActualPrice)
    EditText etActualPrice;

    private PresenterImpl<Object> presenter;
    private int id;
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_wage);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("辞退工资");
        id = getIntent().getIntExtra("id", 0);
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(ActualWageActivity.this);
        etActualPrice.addTextChangedListener(new TextWatcher() {
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
                            etActualPrice.setText("0");
                            etActualPrice.setSelection(etActualPrice.length());
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

    /**
     * 提交
     */
    @OnClick(R.id.btActualSubmit)
    public void actualSubmit() {
        mSVProgressHUD.showWithStatus("加载中...");
        ContractorDisParam contractorDisParam = new ContractorDisParam(
                id,
                etActualPrice.getText().toString()
        );
        presenter = new PresenterImpl<Object>(ActualWageActivity.this);
        presenter.onCreate();
        presenter.dismiss2(contractorDisParam);
        presenter.attachView(actualView);
    }

    private ClientBaseView<Object> actualView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(ActualWageActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                ActualWageActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "actualView--" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatActualWage() {
        ActualWageActivity.this.finish();
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
                ActualWageActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
