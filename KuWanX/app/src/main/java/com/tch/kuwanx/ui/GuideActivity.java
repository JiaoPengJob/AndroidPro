package com.tch.kuwanx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.login.LoginActivity;
import com.tch.kuwanx.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity {

    @BindView(R.id.vpGuide)
    ViewPager vpGuide;

    private List<View> views = new ArrayList<View>();
    private List<String> guides = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        guides.add(Utils.getDrawableResPath(GuideActivity.this, R.drawable.guides_one));
        guides.add(Utils.getDrawableResPath(GuideActivity.this, R.drawable.guides_two));
        guides.add(Utils.getDrawableResPath(GuideActivity.this, R.drawable.guides_three));

        for (int i = 0; i < guides.size(); i++) {
            views.add(getLayoutInflater().from(GuideActivity.this).inflate(R.layout.guide_show, null));
        }
        vpGuide.setAdapter(new PagerAdapter() {
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
        vpGuide.post(new Runnable() {
            @Override
            public void run() {
                ImageView ivGuide = (ImageView) views.get(0).findViewById(R.id.ivGuide);
                Glide.with(GuideActivity.this)
                        .load(guides.get(0))
                        .into(ivGuide);
            }
        });
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageView ivGuide = (ImageView) views.get(position).findViewById(R.id.ivGuide);
                Button btIntoApp = (Button) views.get(position).findViewById(R.id.btIntoApp);
                if (position == views.size() - 1) {
                    btIntoApp.setVisibility(View.VISIBLE);
                } else {
                    btIntoApp.setVisibility(View.GONE);
                }
                Glide.with(GuideActivity.this)
                        .load(guides.get(position))
                        .into(ivGuide);
                btIntoApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initSkip();
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 跳过
     */
    @OnClick(R.id.btSkip)
    public void skip() {
        initSkip();
    }

    private void initSkip() {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        GuideActivity.this.finish();
    }
}
