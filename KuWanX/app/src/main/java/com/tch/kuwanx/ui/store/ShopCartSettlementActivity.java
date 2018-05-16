package com.tch.kuwanx.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.ShoppingCartAccountResult;
import com.tch.kuwanx.result.UserAddressResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.mine.ShipAddressActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 购物车结算
 */
public class ShopCartSettlementActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvSettlementCds)
    RecyclerView rvSettlementCds;
    @BindView(R.id.tvSettlementSun)
    TextView tvSettlementSun;

    private int size = 10, index = 1;
    private String goodIds = "", nums = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart_settlement);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("确认订单");
        tvSettlementSun.setText("0");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("goodIds"))) {
            goodIds = getIntent().getStringExtra("goodIds");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("nums"))) {
            nums = getIntent().getStringExtra("nums");
        }
        getUserAddressHttp();
        showList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingCartAccountHttp();
    }

    private CommonAdapter settlementAdapter;
    private int[] articleNums = new int[1];
    private String[] priceCount = new String[1];

    /**
     * 显示列表
     */
    private void showList() {
        rvSettlementCds.setLayoutManager(new LinearLayoutManager(ShopCartSettlementActivity.this));
        rvSettlementCds.setAdapter(settlementAdapter = new CommonAdapter<ShoppingCartAccountResult.ResultBean.GoodListBean>(ShopCartSettlementActivity.this,
                R.layout.item_order_sub, new ArrayList<ShoppingCartAccountResult.ResultBean.GoodListBean>()) {
            @Override
            protected void convert(final ViewHolder holder, ShoppingCartAccountResult.ResultBean.GoodListBean item, final int position) {

                switch (item.getGood_type_id()) {
                    case "10":
                        holder.setText(R.id.tvSettlementItemType, "游戏设备");
                        break;
                    case "20":
                        holder.setText(R.id.tvSettlementItemType, "游戏光盘");
                        break;
                    case "30":
                        holder.setText(R.id.tvSettlementItemType, "游戏周边");
                        break;
                }

                if (!TextUtils.isEmpty(item.getGood_cover())) {
                    Glide.with(ShopCartSettlementActivity.this)
                            .load(item.getGood_cover())
                            .into((ImageView) holder.getView(R.id.ivSettlementItemCdImg));
                } else {
                    holder.setImageResource(R.id.ivSettlementItemCdImg, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvSettlementItemCdName, item.getGood_name());
                holder.setText(R.id.tvSettlementItemCdContent, item.getGood_intr());
                holder.setText(R.id.tvSettlementItemCdPrice, item.getCurrent_price());
                holder.setText(R.id.etSettlementItemNum, item.getSale_num());//购买数量
                int count = Integer.parseInt(item.getSale_num());
                int price = Integer.parseInt(item.getCurrent_price());
                holder.setText(R.id.tvSettlementItemSelCount, String.valueOf(count * price));
                tvSettlementSun.setText(String.valueOf(
                        Float.parseFloat(
                                ((TextView) holder.getView(R.id.tvSettlementItemSelCount)).getText().toString())
                                + Float.parseFloat(tvSettlementSun.getText().toString())));

                //减
                holder.setOnClickListener(R.id.ibSettlementItemMinus, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (articleNums[position] > 1) {
                            holder.setText(R.id.etSettlementItemNum, String.valueOf(--articleNums[position]));
                            holder.setText(R.id.tvSettlementItemSelCount,
                                    String.valueOf(Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString()) * articleNums[position]));
                            priceCount[position] = String.valueOf(Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString()) * articleNums[position]);
                            tvSettlementSun.setText(String.valueOf(
                                    Float.parseFloat(tvSettlementSun.getText().toString()) -
                                            Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString())));
                        }
                    }
                });
                //加
                holder.setOnClickListener(R.id.ibSettlementItemPlus, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.setText(R.id.etSettlementItemNum, String.valueOf(++articleNums[position]));
                        holder.setText(R.id.tvSettlementItemSelCount,
                                String.valueOf(Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString()) * articleNums[position]));
                        priceCount[position] = String.valueOf(Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString()) * articleNums[position]);
                        tvSettlementSun.setText(String.valueOf(
                                Float.parseFloat(tvSettlementSun.getText().toString()) +
                                        Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString())));
                    }
                });
                //输入数量
                ((EditText) holder.getView(R.id.etSettlementItemNum)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString().equals("")) {
                            holder.setText(R.id.etSettlementItemNum, "1");
                        } else {
                            articleNums[position] = Integer.parseInt(charSequence.toString());
                        }
                        holder.setText(R.id.tvSettlementItemCount, "共" + ((EditText) holder.getView(R.id.etSettlementItemNum)).getText().toString() + "件商品");
                        holder.setText(R.id.tvSettlementItemSelCount, String.valueOf(Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString()) * articleNums[position]));
                        priceCount[position] = String.valueOf(Float.parseFloat(((TextView) holder.getView(R.id.tvSettlementItemCdPrice)).getText().toString()) * articleNums[position]);
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });
        settlementAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tvSettlementSun.setText("0");
                    for (int j = 0; j < priceCount.length; j++) {
                        tvSettlementSun.setText(String.valueOf(
                                Float.parseFloat(tvSettlementSun.getText().toString()) +
                                        Float.parseFloat(priceCount[j])));
                    }
                    break;
            }
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibTitleBack)
    void ibTitleBack() {
        ShopCartSettlementActivity.this.finish();
    }

    /**
     * 选择地址
     */
    @OnClick(R.id.rlSettlementAddress)
    void settlementAddress() {
        Intent intent = new Intent(ShopCartSettlementActivity.this, ShipAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("activity", "ConfirmOrderActivity");
        bundle.putSerializable("address", defaultAddress);
        intent.putExtras(bundle);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                UserAddressResult.ResultBean rb = (UserAddressResult.ResultBean) data.getSerializableExtra("address");
                tvSettlementName.setText(rb.getName());
                tvSettlementPhone.setText(rb.getPhone());
                tvSettlementAddress.setText(rb.getDetail());
                break;
        }
    }

    @BindView(R.id.tvSettlementName)
    TextView tvSettlementName;
    @BindView(R.id.tvSettlementPhone)
    TextView tvSettlementPhone;
    @BindView(R.id.tvSettlementAddress)
    TextView tvSettlementAddress;

    /**
     * 获取用户地址
     */
    private void getUserAddressHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(ShopCartSettlementActivity.this));
        String params = EncryptionUtil.getParameter(ShopCartSettlementActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserAddress.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getUserAddress")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("获取用户地址 -- 失败 == " + e);
                    }

                    @Override
                    public void onSuccess(String response) {
                        UserAddressResult userAddressResult =
                                (UserAddressResult) GsonUtil.json2Object(response, UserAddressResult.class);
                        for (UserAddressResult.ResultBean item : userAddressResult.getResult()) {
                            if (item.getIddefault().equals("2")) {
                                defaultAddress = item;
                                tvSettlementName.setText(item.getName());
                                tvSettlementPhone.setText(item.getPhone());
                                tvSettlementAddress.setText(item.getDetail());
                            }
                        }
                    }
                });
    }

    private UserAddressResult.ResultBean defaultAddress;

    /**
     * 购物车结算
     */
    private void shoppingCartAccountHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ShopCartSettlementActivity.this));
        map.put("good_id", goodIds);
        map.put("good_count", nums);
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(ShopCartSettlementActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/shoppingCartAccount.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_shoppingCartAccount")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ShopCartSettlementActivity.this, "结算中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ShopCartSettlementActivity.this, "结算失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        ShoppingCartAccountResult shoppingCartAccount =
                                (ShoppingCartAccountResult) GsonUtil.json2Object(response, ShoppingCartAccountResult.class);
                        if (shoppingCartAccount != null
                                && shoppingCartAccount.getRet().equals("1")) {
                            settlementAdapter.getDatas().clear();
                            settlementAdapter.getDatas().addAll(shoppingCartAccount.getResult().getGoodList());

                            articleNums = new int[shoppingCartAccount.getResult().getGoodList().size()];
                            for (int i = 0; i < shoppingCartAccount.getResult().getGoodList().size(); i++) {
                                articleNums[i] = Integer.parseInt(shoppingCartAccount.getResult().getGoodList().get(i).getSale_num());
                            }

                            //需要先添加所有小计
                            priceCount = new String[shoppingCartAccount.getResult().getGoodList().size()];

                            settlementAdapter.notifyDataSetChanged();
                        } else {
                            Toasty.warning(ShopCartSettlementActivity.this, "结算失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

}
