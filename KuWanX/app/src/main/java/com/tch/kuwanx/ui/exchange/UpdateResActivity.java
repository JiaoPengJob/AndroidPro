package com.tch.kuwanx.ui.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.KwMsg;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.mine.article.ArticleActivity;
import com.tch.kuwanx.utils.Utils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改
 */
public class UpdateResActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvUpdateRes)
    RecyclerView rvUpdateRes;
    @BindView(R.id.etUpdateResEdit)
    EditText etUpdateResEdit;

    private KwMsg kwMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_res);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("修改置换");
        btTitleFeatures.setText("发送");
        btTitleFeatures.setVisibility(View.VISIBLE);

        if (getIntent().getSerializableExtra("kwMsg") != null) {
            kwMsg = (KwMsg) getIntent().getSerializableExtra("kwMsg");
        }

        initUpdateResPhoto();
        initEditStyle();
    }

    private void initEditStyle() {
        etUpdateResEdit.addTextChangedListener(new TextWatcher() {
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
                            etUpdateResEdit.setText("0");
                            etUpdateResEdit.setSelection(etUpdateResEdit.length());
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

    private CommonAdapter updateResAdapter;

    /**
     * 设置提供的物品
     */
    private void initUpdateResPhoto() {
        rvUpdateRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvUpdateRes.setAdapter(updateResAdapter = new CommonAdapter<String>(this, R.layout.item_release_res, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        (Utils.getScreenWidth(UpdateResActivity.this) - 50) / 3,
                        (Utils.getScreenWidth(UpdateResActivity.this) - 50) / 3
                );
                lp.setMargins(5, 5, 5, 5);
                holder.getView(R.id.ivReleaseResPhoto).setLayoutParams(lp);
                Glide.with(UpdateResActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivReleaseResPhoto));
            }
        });
        updateResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showDialog();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < 6; i++) {
            updateResAdapter.getDatas().add(Utils.getDrawableResPath(UpdateResActivity.this, R.drawable.add_white));
        }
        updateResAdapter.notifyDataSetChanged();
    }

    private CustomPopWindow permutationPop;
    private ViewPager vpReleasePer;
    private List<View> views = new ArrayList<View>();
    private CommonAdapter perAdapter;
    private List<String> allPers = new ArrayList<String>();

    private void showDialog() {
        allPers.clear();
        for (int i = 0; i < 15; i++) {
            allPers.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509635716369&di=508e24e0e20b9e0ca4eaaaea98488368&imgtype=0&src=http%3A%2F%2Fwww.taopic.com%2Fuploads%2Fallimg%2F140104%2F234972-140104010A490.jpg");
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pop_release_permutation, null);
        vpReleasePer = (ViewPager) view.findViewById(R.id.vpResPer);
        initViewPager();
        permutationPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, 540)
                .enableOutsideTouchableDissmiss(true)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.8f)
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(llUpdateRes, Gravity.BOTTOM, 0, 0);
    }

    private void initViewPager() {
        views.clear();
        for (int i = 0; i < (int) Math.ceil(((float) allPers.size()) / 6); i++) {
            views.add(LayoutInflater.from(this).inflate(R.layout.fragment_res, null));
        }
        if (allPers.size() % 6 == 0) {
            views.add(LayoutInflater.from(this).inflate(R.layout.fragment_res, null));
        }
        vpReleasePer.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = views.get(position);
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(views.get(position));
                return views.get(position);
            }
        });
        vpReleasePer.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView rvReleasePer = (RecyclerView) views.get(0).findViewById(R.id.rvReleasePer);
                setReleasePerData(rvReleasePer, 0);
            }
        });
        vpReleasePer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RecyclerView rvReleasePer = (RecyclerView) views.get(position).findViewById(R.id.rvReleasePer);
                setReleasePerData(rvReleasePer, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setReleasePerData(RecyclerView rvReleasePer, final int positionSel) {
        final List<String> perList = new ArrayList<>();
        perList.clear();
        int length = 6 * (positionSel + 1);
        if (positionSel == views.size() - 1) {
            length = allPers.size();
        }
        int index = (6 * (positionSel + 1)) - 6;
        for (int i = index; i < length; i++) {
            perList.add(allPers.get(i));
        }
        if (positionSel == views.size() - 1) {
            perList.add(Utils.getDrawableResPath(this, R.drawable.add_release));
        }

        rvReleasePer.setLayoutManager(new GridLayoutManager(this, 3));
        rvReleasePer.setAdapter(perAdapter = new CommonAdapter<String>(this, R.layout.item_release_per, perList) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                Glide.with(UpdateResActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivReleasePerPhoto));
                if (positionSel == views.size() - 1
                        && position == perList.size() - 1) {
                    holder.getView(R.id.tvReleasePer).setVisibility(View.INVISIBLE);
                    holder.setOnClickListener(R.id.ivReleasePerPhoto, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (permutationPop != null) {
                                permutationPop.dissmiss();
                            }
                            Intent intent = new Intent(UpdateResActivity.this, ArticleActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        perAdapter.notifyDataSetChanged();
        perAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
    private Button btExitEms, btExitFlash, btExitFace, btExitDis;
    @BindView(R.id.tvUpdateResPostWay)
    TextView tvUpdateResPostWay;
    @BindView(R.id.llUpdateRes)
    LinearLayout llUpdateRes;

    /**
     * 邮寄方式
     */
    @OnClick(R.id.tvUpdateResPostWay)
    public void updateResPostWay() {
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
                .showAtLocation(llUpdateRes, Gravity.BOTTOM, 0, 0);
    }

    private class DisClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mAvatarPop != null) {
                mAvatarPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.btExitEms:
                    tvUpdateResPostWay.setText("邮寄");
                    break;
                case R.id.btExitFlash:
                    tvUpdateResPostWay.setText("闪送");
                    break;
                case R.id.btExitFace:
                    tvUpdateResPostWay.setText("面交");
                    break;
            }
        }
    }

    /**
     * 发送
     */
    @OnClick(R.id.btTitleFeatures)
    public void sendUpdateRes() {
        UpdateResActivity.this.finish();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void updateResBack() {
        UpdateResActivity.this.finish();
    }
}
