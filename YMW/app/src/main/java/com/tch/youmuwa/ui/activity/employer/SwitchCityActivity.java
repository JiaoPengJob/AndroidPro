package com.tch.youmuwa.ui.activity.employer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovejjfg.powerrecycle.AdapterLoader;
import com.lovejjfg.powerrecycle.PowerAdapter;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.City;
import com.tch.youmuwa.bean.Citys;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.view.IndexBar;
import com.tch.youmuwa.util.HelperUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择城市
 */
public class SwitchCityActivity extends BaseActivtiy {

    @BindView(R.id.rvCity)
    RecyclerView rvCity;
    @BindView(R.id.ibCity)
    IndexBar ibCity;

    private List<City> cities = new ArrayList<City>();
    private List<String> letters = new ArrayList<>();
    private Intent intent;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_city);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        if (getIntent().getStringExtra("city") != null) {
            city = getIntent().getStringExtra("city");
        }
        initIndexBar();
    }

    /**
     * 加载索引栏
     */
    private void initIndexBar() {
        fillNameAndSort();
        IndexAdapter indexAdapter = new IndexAdapter();
        indexAdapter.setList(cities);
        rvCity.setAdapter(indexAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCity.setLayoutManager(linearLayoutManager);
        rvCity.setHasFixedSize(false);
        ibCity.setLetters(letters);
        ibCity.setOnLetterChangeListener(new IndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(int position, String letter) {
                if ("#".equals(letter)) {
                    rvCity.scrollToPosition(0);
                    return;
                }
                for (int i = 0; i < cities.size(); i++) {
                    City city = cities.get(i);
                    String pinyin = city.getPinyin();
                    String firstPinyin = String.valueOf(TextUtils.isEmpty(pinyin) ? city.getName().charAt(0) : pinyin.charAt(0));
                    if (letter.compareToIgnoreCase(firstPinyin) == 0) {
                        linearLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
        indexAdapter.setOnItemClickListener(new AdapterLoader.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                intent = new Intent();
                intent.putExtra("cityName", cities.get(position).getName());
                SwitchCityActivity.this.setResult(10, intent);
                SwitchCityActivity.this.finish();
            }
        });
    }

    // 填充拼音, 排序
    private void fillNameAndSort() {
        letters.add("#");
        for (int i = 0; i < Citys.NAMES.length; i++) {
            City city = new City(this, Citys.NAMES[i]);
            cities.add(city);
            if (HelperUtil.isDigital(city.getName())) {
                if (!letters.contains("#")) {
                    letters.add("#");
                }
                continue;
            }
            String pinyin = city.getPinyin();
            String letter;
            if (!TextUtils.isEmpty(pinyin)) {
                letter = pinyin.substring(0, 1).toUpperCase();
            } else {
                letter = city.getName().substring(0, 1).toUpperCase();
            }
            if (!letters.contains(letter)) {
                letters.add(letter);
            }
        }
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o1.getPinyin().compareTo(o2.getPinyin());
            }
        });
        Collections.sort(letters,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    static class IndexAdapter extends PowerAdapter<City> {

        @Override
        public RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
            return new IndexHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_switch_city, parent, false));
        }

        @Override
        public void onViewHolderBind(RecyclerView.ViewHolder holder, int position) {
            ((IndexHolder) holder).onBind(list, position);
        }
    }

    static class IndexHolder extends RecyclerView.ViewHolder {

        private final TextView mTvPinyin;
        private final TextView mTvName;

        public IndexHolder(View itemView) {
            super(itemView);
            mTvPinyin = (TextView) itemView.findViewById(R.id.tv_pinyin);
            mTvName = (TextView) itemView.findViewById(R.id.tv_city_name);
        }

        public void onBind(List<City> list, int position) {
            City person = list.get(position);
            String pinyin = person.getPinyin();
            String name = person.getName();
            String mFirstPinyin = String.valueOf(TextUtils.isEmpty(pinyin) ? name.charAt(0) : pinyin.charAt(0));
            String mPreFirstPinyin;
            if (position == 0) {
                mPreFirstPinyin = "-";
            } else {
                City city = list.get(position - 1);
                String prePinyin = city.getPinyin();
                String preName = city.getName();
                mPreFirstPinyin = String.valueOf(TextUtils.isEmpty(prePinyin) ? preName.charAt(0) : prePinyin.charAt(0));
            }
            if (HelperUtil.isDigital(mFirstPinyin)) {
                mFirstPinyin = "#";
            }
            if (HelperUtil.isDigital(mPreFirstPinyin)) {
                mPreFirstPinyin = "#";
            }
            mTvPinyin.setVisibility(mFirstPinyin.compareToIgnoreCase(mPreFirstPinyin) != 0 ? View.VISIBLE
                    : View.GONE);
            mTvPinyin.setText(String.valueOf(mFirstPinyin.toUpperCase()));
            mTvName.setText(name);
        }
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibCityRetreat)
    public void cityRetreat() {
        intent = new Intent();
        intent.putExtra("cityName", city);
        SwitchCityActivity.this.setResult(11, intent);
        SwitchCityActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent();
            intent.putExtra("cityName", city);
            SwitchCityActivity.this.setResult(11, intent);
            SwitchCityActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
