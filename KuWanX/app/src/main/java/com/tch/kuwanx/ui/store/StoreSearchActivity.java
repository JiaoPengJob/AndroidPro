package com.tch.kuwanx.ui.store;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商城搜索
 */
public class StoreSearchActivity extends BaseActivity {

    @BindView(R.id.etStoreSearchInput)
    EditText etStoreSearchInput;
    @BindView(R.id.ivStoreComplex)
    ImageView ivStoreComplex;
    @BindView(R.id.ivStoreSales)
    ImageView ivStoreSales;
    @BindView(R.id.ivStorePrice)
    ImageView ivStorePrice;
    @BindView(R.id.llStoreMenuView)
    LinearLayout llStoreMenuView;
    @BindView(R.id.rvStoreSearch)
    RecyclerView rvStoreSearch;
    @BindView(R.id.llStoreBlank)
    LinearLayout llStoreBlank;

    //是否降序,默认降序
    private boolean salesDescending = true, priceDescending = true;
    private CommonAdapter storeSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initSearchData();
    }

    /**
     * 加载商城搜索列表
     */
    private void initSearchData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("");
        }
        rvStoreSearch.setLayoutManager(new LinearLayoutManager(this));
        rvStoreSearch.setAdapter(storeSearchAdapter = new CommonAdapter<String>(this, R.layout.item_store_search, list) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                Glide.with(StoreSearchActivity.this)
                        .load("http://p2.gexing.com/G1/M00/06/CD/rBACFFK1PfPCqeBHAAAdy2VAyaA244_200x200_3.jpg?recache=20131108")
                        .into((ImageView) holder.getView(R.id.ivStoreSearchItemImg));
                holder.setText(R.id.tvStoreSearchItemTitle, "标题");

                holder.setText(R.id.tvStoreSearchItemTitle, "标题");
                holder.setText(R.id.tvStoreSearchItemNorm, "规格");
                holder.setText(R.id.tvStoreSearchItemNum, "X1");
                holder.setText(R.id.tvStoreSearchItemNewPrice, "¥6.66");
                holder.setText(R.id.tvStoreSearchItemOldPrice, "¥8.88");
                holder.setText(R.id.tvStoreSearchItemComNum, "88" + "条评价");
                holder.setText(R.id.tvStoreSearchItemGood, "66" + "好评");
                holder.setOnClickListener(R.id.ivStoreSearchItemBuy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StoreSearchActivity.this, ConfirmOrderActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        storeSearchAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(StoreSearchActivity.this, ProductDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 综合
     */
    @OnClick(R.id.llStoreComplex)
    public void storeComplex() {

    }

    /**
     * 销量
     */
    @OnClick(R.id.llStoreSales)
    public void storeSales() {
        if (salesDescending) {
            ivStoreSales.setImageResource(R.drawable.down_icon);
            salesDescending = false;
        } else {
            ivStoreComplex.setImageResource(R.drawable.up);
            salesDescending = true;
        }
    }

    /**
     * 价格
     */
    @OnClick(R.id.llStorePrice)
    public void storePrice() {
        if (priceDescending) {
            ivStorePrice.setImageResource(R.drawable.down_icon);
            priceDescending = false;
        } else {
            ivStoreComplex.setImageResource(R.drawable.up);
            priceDescending = true;
        }
    }

    private TextView tvFilterCity, tvFilterFaceCross, tvFilterFlashing,
            tvFilterOne, tvFilterHundred, tvFilterTwoHundred, tvFilterFiveHundred,
            tvFilterThousand, tvFilterTwoThousand, tvFilterFiveThousand, tvFilterMillion;
    private EditText etFilterLowestPrice, etFilterHighestPrice;
    private Button btFilterReset, btFilterConfirm;
    private View viewFilterLowestPrice, viewFilterHighestPrice;
    private CustomPopWindow mFilterPop;
    private int filterType = 0, least = 0, lowestPrice = 0, highestPrice = 0;

    /**
     * 筛选
     */
    @OnClick(R.id.llStoreSelect)
    public void storeSelect() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_filter, null);
        tvFilterCity = (TextView) view.findViewById(R.id.tvFilterCity);
        tvFilterFaceCross = (TextView) view.findViewById(R.id.tvFilterFaceCross);
        tvFilterFlashing = (TextView) view.findViewById(R.id.tvFilterFlashing);
        tvFilterOne = (TextView) view.findViewById(R.id.tvFilterOne);
        tvFilterHundred = (TextView) view.findViewById(R.id.tvFilterHundred);
        tvFilterTwoHundred = (TextView) view.findViewById(R.id.tvFilterTwoHundred);
        tvFilterFiveHundred = (TextView) view.findViewById(R.id.tvFilterFiveHundred);
        tvFilterThousand = (TextView) view.findViewById(R.id.tvFilterThousand);
        tvFilterTwoThousand = (TextView) view.findViewById(R.id.tvFilterTwoThousand);
        tvFilterFiveThousand = (TextView) view.findViewById(R.id.tvFilterFiveThousand);
        tvFilterMillion = (TextView) view.findViewById(R.id.tvFilterMillion);
        etFilterLowestPrice = (EditText) view.findViewById(R.id.etFilterLowestPrice);
        etFilterHighestPrice = (EditText) view.findViewById(R.id.etFilterHighestPrice);
        btFilterReset = (Button) view.findViewById(R.id.btFilterReset);
        btFilterConfirm = (Button) view.findViewById(R.id.btFilterConfirm);
        viewFilterLowestPrice = view.findViewById(R.id.viewFilterLowestPrice);
        viewFilterHighestPrice = view.findViewById(R.id.viewFilterHighestPrice);

        tvFilterCity.setOnClickListener(new FilterClick());
        tvFilterFaceCross.setOnClickListener(new FilterClick());
        tvFilterFlashing.setOnClickListener(new FilterClick());
        tvFilterOne.setOnClickListener(new FilterClick());
        tvFilterHundred.setOnClickListener(new FilterClick());
        tvFilterTwoHundred.setOnClickListener(new FilterClick());
        tvFilterFiveHundred.setOnClickListener(new FilterClick());
        tvFilterThousand.setOnClickListener(new FilterClick());
        tvFilterTwoThousand.setOnClickListener(new FilterClick());
        tvFilterFiveThousand.setOnClickListener(new FilterClick());
        tvFilterMillion.setOnClickListener(new FilterClick());
        btFilterReset.setOnClickListener(new FilterClick());
        btFilterConfirm.setOnClickListener(new FilterClick());
        viewFilterLowestPrice.setOnClickListener(new FilterClick());
        viewFilterHighestPrice.setOnClickListener(new FilterClick());

        if (filterType == 0) {
            filterCitySel();
        } else if (filterType == 1) {
            filterFaceCrossSel();
        } else if (filterType == 2) {
            filterFlashingSel();
        }

        if (lowestPrice != 0) {
            etFilterLowestPrice.setText(String.valueOf(lowestPrice));
        } else if (highestPrice != 0) {
            etFilterHighestPrice.setText(String.valueOf(highestPrice));
        } else {
            if (least == 1) {
                filterOneSel();
            } else if (least == 100) {
                filterHundredSel();
            } else if (least == 200) {
                filterTwoHundredSel();
            } else if (least == 500) {
                filterFiveHundredSel();
            } else if (least == 1000) {
                filterThousandSel();
            } else if (least == 2000) {
                filterTwoThousandSel();
            } else if (least == 5000) {
                filterFiveThousandSel();
            } else if (least == 10000) {
                filterMillionSel();
            }
        }

        mFilterPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .create()
                .showAsDropDown(llStoreMenuView, 0, 5);
    }

    /**
     * 重置
     */
    public void filterReset() {
        least = 0;
        filterType = 0;
        lowestPrice = 0;
        highestPrice = 0;
        clearStyle();
    }

    private void clearStyle() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterCity.setTextColor(Color.parseColor("#919191"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#919191"));
        tvFilterFlashing.setTextColor(Color.parseColor("#919191"));

        clearBtnStyle();

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void clearBtnStyle() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));
    }

    /**
     * 确认
     */
    public void filterConfirm() {
        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            lowestPrice = Integer.parseInt(etFilterLowestPrice.getText().toString());
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            highestPrice = Integer.parseInt(etFilterHighestPrice.getText().toString());
        }
        mFilterPop.dissmiss();
    }

    private class FilterClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (Utils.isSHowKeyboard(StoreSearchActivity.this, mFilterPop.getPopupWindow().getContentView())) {
                Utils.hindKeyBoard(StoreSearchActivity.this, mFilterPop.getPopupWindow().getContentView());
            }
            switch (view.getId()) {
                case R.id.tvFilterCity:
                    filterType = 0;
                    filterCitySel();
                    break;
                case R.id.tvFilterFaceCross:
                    filterType = 1;
                    filterFaceCrossSel();
                    break;
                case R.id.tvFilterFlashing:
                    filterType = 2;
                    filterFlashingSel();
                    break;
                case R.id.tvFilterOne:
                    least = 1;
                    filterOneSel();
                    break;
                case R.id.tvFilterHundred:
                    least = 100;
                    filterHundredSel();
                    break;
                case R.id.tvFilterTwoHundred:
                    least = 200;
                    filterTwoHundredSel();
                    break;
                case R.id.tvFilterFiveHundred:
                    least = 500;
                    filterFiveHundredSel();
                    break;
                case R.id.tvFilterThousand:
                    least = 1000;
                    filterThousandSel();
                    break;
                case R.id.tvFilterTwoThousand:
                    least = 2000;
                    filterTwoThousandSel();
                    break;
                case R.id.tvFilterFiveThousand:
                    least = 5000;
                    filterFiveThousandSel();
                    break;
                case R.id.tvFilterMillion:
                    least = 10000;
                    filterMillionSel();
                    break;
                case R.id.btFilterReset:
                    filterReset();
                    break;
                case R.id.btFilterConfirm:
                    filterConfirm();
                    break;
                case R.id.viewFilterLowestPrice:
                    least = 0;
                    etFilterLowestPrice.setFocusable(true);
                    etFilterLowestPrice.setFocusableInTouchMode(true);
                    etFilterLowestPrice.requestFocus();
                    clearBtnStyle();
                    popupInputMethodWindow();
                    break;
                case R.id.viewFilterHighestPrice:
                    least = 0;
                    etFilterHighestPrice.setFocusable(true);
                    etFilterHighestPrice.setFocusableInTouchMode(true);
                    etFilterHighestPrice.requestFocus();
                    clearBtnStyle();
                    popupInputMethodWindow();
                    break;
            }
        }
    }

    private Handler handler = new Handler();
    private InputMethodManager imm;

    private void popupInputMethodWindow() {
        mFilterPop.getPopupWindow().setOutsideTouchable(false);
        //软键盘不会挡着popupwindow
        mFilterPop.getPopupWindow().setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mFilterPop.getPopupWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    private void filterCitySel() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterCity.setTextColor(Color.parseColor("#333333"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#919191"));
        tvFilterFlashing.setTextColor(Color.parseColor("#919191"));
    }

    private void filterFaceCrossSel() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterCity.setTextColor(Color.parseColor("#919191"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#333333"));
        tvFilterFlashing.setTextColor(Color.parseColor("#919191"));
    }

    private void filterFlashingSel() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterCity.setTextColor(Color.parseColor("#919191"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#919191"));
        tvFilterFlashing.setTextColor(Color.parseColor("#333333"));
    }

    private void filterOneSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#333333"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }

    }

    private void filterHundredSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#333333"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterTwoHundredSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#333333"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterFiveHundredSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#333333"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterThousandSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#333333"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterTwoThousandSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#333333"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterFiveThousandSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#333333"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterMillionSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#333333"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    @OnClick(R.id.etStoreSearchInput)
    public void storeSearchInput() {
        etStoreSearchInput.setFocusable(true);
        etStoreSearchInput.setFocusableInTouchMode(true);
        etStoreSearchInput.requestFocus();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibStoreSearchTitleBack)
    public void storeSearchTitleBack() {
        this.finish();
    }


}
