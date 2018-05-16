package com.tch.kuwanx.ui.chat;

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
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.looks.DisplacementInforActivity;
import com.tch.kuwanx.ui.mine.article.ArticleActivity;
import com.tch.kuwanx.ui.release.ReleaseActivity;
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
 * 提出置换
 */
public class ProposedExActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvProposedHis)
    RecyclerView rvProposedHis;
    @BindView(R.id.etProposedHisEdit)
    EditText etProposedHisEdit;
    @BindView(R.id.etProposedYourEdit)
    EditText etProposedYourEdit;
    @BindView(R.id.rvProposedYours)
    RecyclerView rvProposedYours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_ex);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("提出置换");
        btTitleFeatures.setText("提交");
        btTitleFeatures.setVisibility(View.VISIBLE);
        initEditStyle();
        initProposedHis();
        initProposedYours();
    }

    /**
     * 设置输入框样式
     */
    private void initEditStyle() {
        etProposedHisEdit.addTextChangedListener(new TextWatcher() {
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
                            etProposedHisEdit.setText("0");
                            etProposedHisEdit.setSelection(etProposedHisEdit.length());
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
        etProposedYourEdit.addTextChangedListener(new TextWatcher() {
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
                            etProposedYourEdit.setText("0");
                            etProposedYourEdit.setSelection(etProposedYourEdit.length());
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

    private CommonAdapter proposedHisAdapter;

    /**
     * 加载他提供的物品
     */
    private void initProposedHis() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add("http://g.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=03cfbe5096ef76c6d087f32fa826d1cc/7acb0a46f21fbe098d71af656d600c338744ad90.jpg");
        }
        rvProposedHis.setLayoutManager(new GridLayoutManager(this, 3));
        rvProposedHis.setAdapter(proposedHisAdapter = new CommonAdapter<String>(this, R.layout.item_img_post, list) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        (Utils.getScreenWidth(ProposedExActivity.this) - 50) / 3,
                        (Utils.getScreenWidth(ProposedExActivity.this) - 50) / 3
                );
                lp.setMargins(5, 5, 5, 5);
                holder.getView(R.id.ivPostImg).setLayoutParams(lp);
                Glide.with(ProposedExActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivPostImg));
            }
        });
        proposedHisAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CommonAdapter proposedYoursAdapter;

    /**
     * 加载你能提供的物品
     */
    private void initProposedYours() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(Utils.getDrawableResPath(ProposedExActivity.this, R.drawable.add_white));
        }
        rvProposedYours.setLayoutManager(new GridLayoutManager(this, 3));
        rvProposedYours.setAdapter(proposedYoursAdapter = new CommonAdapter<String>(this, R.layout.item_release_res, list) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        (Utils.getScreenWidth(ProposedExActivity.this) - 50) / 3,
                        (Utils.getScreenWidth(ProposedExActivity.this) - 50) / 3
                );
                lp.setMargins(5, 5, 5, 5);
                holder.getView(R.id.ivReleaseResPhoto).setLayoutParams(lp);
                Glide.with(ProposedExActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivReleaseResPhoto));
            }
        });
        proposedYoursAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showDialog();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CustomPopWindow proposedPop;
    @BindView(R.id.llProposedParent)
    LinearLayout llProposedParent;
    private ViewPager vpProposed;
    private List<View> views = new ArrayList<View>();
    private CommonAdapter proposedAdapter;
    private List<String> allProposed = new ArrayList<String>();

    private void showDialog() {
        allProposed.clear();
        for (int i = 0; i < 15; i++) {
            allProposed.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509635716369&di=508e24e0e20b9e0ca4eaaaea98488368&imgtype=0&src=http%3A%2F%2Fwww.taopic.com%2Fuploads%2Fallimg%2F140104%2F234972-140104010A490.jpg");
        }
        View view = LayoutInflater.from(this).inflate(R.layout.pop_release_permutation, null);
        vpProposed = (ViewPager) view.findViewById(R.id.vpResPer);
        initViewPager();
        proposedPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, 540)
                .enableOutsideTouchableDissmiss(true)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.8f)
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(llProposedParent, Gravity.BOTTOM, 0, 0);
    }

    private void initViewPager() {
        views.clear();
        for (int i = 0; i < (int) Math.ceil(((float) allProposed.size()) / 6); i++) {
            views.add(LayoutInflater.from(this).inflate(R.layout.fragment_res, null));
        }
        if (allProposed.size() % 6 == 0) {
            views.add(LayoutInflater.from(this).inflate(R.layout.fragment_res, null));
        }
        vpProposed.setAdapter(new PagerAdapter() {
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
        vpProposed.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView rvReleasePer = (RecyclerView) views.get(0).findViewById(R.id.rvReleasePer);
                setReleasePerData(rvReleasePer, 0);
            }
        });
        vpProposed.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            length = allProposed.size();
        }
        int index = (6 * (positionSel + 1)) - 6;
        for (int i = index; i < length; i++) {
            perList.add(allProposed.get(i));
        }
        if (positionSel == views.size() - 1) {
            perList.add(Utils.getDrawableResPath(ProposedExActivity.this, R.drawable.add_release));
        }

        rvReleasePer.setLayoutManager(new GridLayoutManager(ProposedExActivity.this, 3));
        rvReleasePer.setAdapter(proposedAdapter = new CommonAdapter<String>(ProposedExActivity.this, R.layout.item_release_per, perList) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                Glide.with(ProposedExActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivReleasePerPhoto));
                if (positionSel == views.size() - 1
                        && position == perList.size() - 1) {
                    holder.getView(R.id.tvReleasePer).setVisibility(View.INVISIBLE);
                    holder.setOnClickListener(R.id.ivReleasePerPhoto, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (proposedPop != null) {
                                proposedPop.dissmiss();
                            }
                            Intent intent = new Intent(ProposedExActivity.this, ArticleActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        proposedAdapter.notifyDataSetChanged();
        proposedAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 提交
     */
    @OnClick(R.id.btTitleFeatures)
    public void proposedSubmit() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void proposedExBack() {
        ProposedExActivity.this.finish();
    }
}
