package com.tch.kuwanx.ui.looks;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.Utils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 置换信息查看
 */
public class DisplacementInforActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etDisplaceEdit)
    EditText etDisplaceEdit;
    @BindView(R.id.rvDisplacements)
    RecyclerView rvDisplacements;
    @BindView(R.id.rlDisplaceParent)
    RelativeLayout rlDisplaceParent;
    @BindView(R.id.tvDisPostWay)
    TextView tvDisPostWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displacement_infor);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("置换信息查看");
        initDisplacements();
        etDisplaceEdit.addTextChangedListener(new TextWatcher() {
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
                            etDisplaceEdit.setText("0");
                            etDisplaceEdit.setSelection(etDisplaceEdit.length());
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

    private CommonAdapter disAdapter;

    private void initDisplacements() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add("http://g.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=03cfbe5096ef76c6d087f32fa826d1cc/7acb0a46f21fbe098d71af656d600c338744ad90.jpg");
        }
        rvDisplacements.setLayoutManager(new GridLayoutManager(this, 3));
        rvDisplacements.setAdapter(disAdapter = new CommonAdapter<String>(this, R.layout.item_img_post, list) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        (Utils.getScreenWidth(DisplacementInforActivity.this) - 50) / 3,
                        (Utils.getScreenWidth(DisplacementInforActivity.this) - 50) / 3
                );
                lp.setMargins(5, 5, 5, 5);
                holder.getView(R.id.ivPostImg).setLayoutParams(lp);
                Glide.with(DisplacementInforActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPostImg));
            }
        });
        disAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CustomPopWindow mAvatarPop;
    private Button btExitEms, btExitFlash, btExitFace,btExitDis;

    /**
     * 邮寄方式
     */
    @OnClick(R.id.rlDisPostWay)
    public void disPostWay() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_dis, null);
        btExitEms = (Button) view.findViewById(R.id.btExitEms);
        btExitFlash = (Button) view.findViewById(R.id.btExitFlash);
        btExitFace = (Button) view.findViewById(R.id.btExitFace);
        btExitDis = (Button) view.findViewById(R.id.btExitDis);
        btExitEms.setOnClickListener(new DisClickListener());
        btExitFlash.setOnClickListener(new DisClickListener());
        btExitFace.setOnClickListener(new DisClickListener());
        btExitDis.setOnClickListener(new DisClickListener());
        mAvatarPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(rlDisplaceParent, Gravity.BOTTOM, 0, 0);
    }

    private class DisClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mAvatarPop != null) {
                mAvatarPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.btExitEms:
                    tvDisPostWay.setText("邮寄");
                    break;
                case R.id.btExitFlash:
                    tvDisPostWay.setText("闪送");
                    break;
                case R.id.btExitFace:
                    tvDisPostWay.setText("面交");
                    break;
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void displacementInforBack() {
        DisplacementInforActivity.this.finish();
    }
}
