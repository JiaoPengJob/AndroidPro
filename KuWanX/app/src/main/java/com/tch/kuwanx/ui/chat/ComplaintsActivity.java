package com.tch.kuwanx.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.ComplaintsResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 投诉
 */
public class ComplaintsActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.ibComplaintsOne)
    ImageButton ibComplaintsOne;
    @BindView(R.id.ibComplaintsSecond)
    ImageButton ibComplaintsSecond;
    @BindView(R.id.ibComplaintsThree)
    ImageButton ibComplaintsThree;

    private int complaintsReason = 1;
    private String complaintsStr = "发布不适当内容对我造成骚扰";
    private String targetId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("通讯录");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("targetId"))) {
            targetId = getIntent().getStringExtra("targetId");
        }
    }

    /**
     * 确认投诉
     */
    @OnClick(R.id.ibComplaints)
    public void submitComplaints() {
        complaintsHttp();
    }

    /**
     * 选择单一原因
     */
    @OnClick(R.id.ibComplaintsOne)
    public void complaintsSwitchOne() {
        complaintsReason = 1;
        complaintsStr = "发布不适当内容对我造成骚扰";
        ibComplaintsOne.setImageResource(R.drawable.switch_sel);
        ibComplaintsSecond.setImageResource(R.drawable.switch_unsel);
        ibComplaintsThree.setImageResource(R.drawable.switch_unsel);
    }

    @OnClick(R.id.ibComplaintsSecond)
    public void complaintsSwitchSecond() {
        complaintsReason = 2;
        complaintsStr = "存在欺诈骗钱行为";
        ibComplaintsOne.setImageResource(R.drawable.switch_unsel);
        ibComplaintsSecond.setImageResource(R.drawable.switch_sel);
        ibComplaintsThree.setImageResource(R.drawable.switch_unsel);
    }

    @OnClick(R.id.ibComplaintsThree)
    public void complaintsSwitchThree() {
        complaintsReason = 3;
        complaintsStr = "发布仿冒品信息";
        ibComplaintsOne.setImageResource(R.drawable.switch_unsel);
        ibComplaintsSecond.setImageResource(R.drawable.switch_unsel);
        ibComplaintsThree.setImageResource(R.drawable.switch_sel);
    }

    /**
     * 投诉
     */
    private void complaintsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", DaoUtils.getUserId(ComplaintsActivity.this));
        map.put("complaint_user_id", targetId);
        map.put("complaint_content", complaintsStr);
        String params = EncryptionUtil.getParameter(ComplaintsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/addComplaint.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addComplaint")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ComplaintsActivity.this, "投诉中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ComplaintsActivity.this, "投诉失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        ComplaintsResult complaintsResult =
                                (ComplaintsResult) GsonUtil.json2Object(response, ComplaintsResult.class);
                        if (complaintsResult != null
                                && complaintsResult.getRet().equals("1")) {
                            Toasty.warning(ComplaintsActivity.this, "投诉成功！", Toast.LENGTH_SHORT, false).show();
                            ComplaintsActivity.this.finish();
                        }
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void complaintsBack() {
        ComplaintsActivity.this.finish();
    }
}
