package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.popupWindow.WorkAreaPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工人/个人资料/地址管理
 */
public class WorkerAreaMangerActivity extends BaseActivtiy implements PopupInterface {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.parentWorkerAreaManager)
    RelativeLayout parentWorkerAreaManager;
    @BindView(R.id.tvWorkerAreaShow)
    TextView tvWorkerAreaShow;

    private PresenterImpl<Object> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_area_manger);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("区域管理");
    }

    /**
     * 选择地址
     */
    @OnClick(R.id.rlWorkerAreaSel)
    public void workerAreaSel() {
        WorkAreaPopupWindow areaPopupWindow = new WorkAreaPopupWindow(this, this);
        //设置Popupwindow显示位置（从底部弹出）
        areaPopupWindow.showAtLocation(parentWorkerAreaManager, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        parentWorkerAreaManager.setAlpha(0.8f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                parentWorkerAreaManager.setAlpha(1f);
            }
        });
    }

    @Override
    public void getResult(int index, String result) {
        tvWorkerAreaShow.setText(result);
    }

    /**
     * 确定
     */
    @OnClick(R.id.btWorkerAreaMangerDetermine)
    public void workerAreaMangerDetermine() {
        if (!tvWorkerAreaShow.getText().toString().equals("省份-城市-区县")) {
            String cityStr = tvWorkerAreaShow.getText().toString();
            String province = cityStr.substring(0, cityStr.indexOf("-"));
            String city = cityStr.substring(cityStr.indexOf("-") + 1, cityStr.lastIndexOf("-"));
            String area = cityStr.substring(cityStr.lastIndexOf("-") + 1, cityStr.length());

            Intent intent = new Intent();
            intent.putExtra("workerAreaMag", tvWorkerAreaShow.getText().toString());
            setResult(RESULT_OK, intent);
            WorkerAreaMangerActivity.this.finish();
        }
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatWorkerAreaManager() {
        WorkerAreaMangerActivity.this.finish();
    }
}
