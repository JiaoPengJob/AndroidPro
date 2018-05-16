package com.tch.kuwanx.ui.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.CartsItem;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.DeleteShoppingCartResult;
import com.tch.kuwanx.result.ShoppingCartListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 购物车
 */
public class ShoppingCartActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvShoppingCart)
    RecyclerView rvShoppingCart;
    @BindView(R.id.rlShopCartsParent)
    RelativeLayout rlShopCartsParent;
    @BindView(R.id.ibShopCartsAllSel)
    ImageButton ibShopCartsAllSel;
    @BindView(R.id.refreshShoppingCart)
    SmartRefreshLayout refreshShoppingCart;
    @BindView(R.id.tvShoppingCartAllPrice)
    TextView tvShoppingCartAllPrice;

    private CommonAdapter shoppingCartAdapter;
    private boolean isMore = false;
    private int index = 1, size = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("购物车");
        initShoppingCart();
        refreshShoppingCart.setOnLoadmoreListener(this);
        refreshShoppingCart.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShoppingCartListHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        getShoppingCartListHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        getShoppingCartListHttp();
    }

    private boolean isAll = false;

    /**
     * 全选
     */
    @OnClick(R.id.ibShopCartsAllSel)
    public void shopCartsAllSel() {
        if (isAll) {
            isAll = false;
            ibShopCartsAllSel.setImageResource(R.drawable.oval_unselect);
            for (CartsItem cartsItem : list) {
                cartsItem.setWillPay(false);
            }
            tvShoppingCartAllPrice.setText("0");
        } else {
            isAll = true;
            ibShopCartsAllSel.setImageResource(R.drawable.select);
            for (CartsItem cartsItem : list) {
                cartsItem.setWillPay(true);
                int count = Integer.parseInt(((ShoppingCartListResult.ResultBean) cartsItem.getItem()).getGood_count());
                int price = Integer.parseInt(((ShoppingCartListResult.ResultBean) cartsItem.getItem()).getCurrent_price());
                int all = 0;
                tvShoppingCartAllPrice.setText(String.valueOf(all + count * price));
            }
        }
        shoppingCartAdapter.notifyDataSetChanged();
    }

    private List<CartsItem> list = new ArrayList<>();

    private void initShoppingCart() {
        rvShoppingCart.setLayoutManager(new LinearLayoutManager(this));
        rvShoppingCart.setAdapter(shoppingCartAdapter = new CommonAdapter<CartsItem>(this,
                R.layout.item_carts_childs, new ArrayList<CartsItem>()) {
            @Override
            protected void convert(final ViewHolder holder, final CartsItem item, final int position) {
                final ShoppingCartListResult.ResultBean scr = (ShoppingCartListResult.ResultBean) item.getItem();
                holder.setText(R.id.tvCartsItemCdName, scr.getGood_name());
                holder.setText(R.id.tvCartsItemCdPrice, "￥" + scr.getCurrent_price());
                holder.setText(R.id.tvCartsItemCdNumber, "X" + scr.getGood_count());
                Glide.with(ShoppingCartActivity.this)
                        .load(scr.getGood_cover())
                        .into((ImageView) holder.getView(R.id.ivCartsItemCd));

                holder.setText(R.id.tvCartsItemType, "酷玩商城");

//                if (scr.getGood_type_id().equals("10")) {
//                    holder.setText(R.id.tvCartsItemType, "游戏设备");
//                } else if (scr.getGood_type_id().equals("20")) {
//                    holder.setText(R.id.tvCartsItemType, "游戏光盘");
//                } else if (scr.getGood_type_id().equals("30")) {
//                    holder.setText(R.id.tvCartsItemType, "游戏周边");
//                }

                if (item.isWillPay()) {
                    holder.setImageResource(R.id.ibCartsItemTypeSel, R.drawable.select);
                    holder.setImageResource(R.id.ibCartsItemChildSel, R.drawable.select);
                } else {
                    holder.setImageResource(R.id.ibCartsItemTypeSel, R.drawable.oval_unselect);
                    holder.setImageResource(R.id.ibCartsItemChildSel, R.drawable.oval_unselect);
                    isAll = false;
                    ibShopCartsAllSel.setImageResource(R.drawable.oval_unselect);
                }

                if (item.isIfEdit()) {
                    holder.setText(R.id.btCartsItemEdit, "完成");
//                    holder.getView(R.id.llCartsItemBefore).startAnimation(AnimationUtils.makeOutAnimation(ShoppingCartActivity.this, false));
                    holder.getView(R.id.llCartsItemBefore).setVisibility(View.GONE);
//                    holder.getView(R.id.rlCartsItemAfter).startAnimation(AnimationUtils.makeInAnimation(ShoppingCartActivity.this, false));
                    holder.getView(R.id.rlCartsItemAfter).setVisibility(View.VISIBLE);
                } else {
                    holder.setText(R.id.btCartsItemEdit, "编辑");
//                    holder.getView(R.id.rlCartsItemAfter).startAnimation(AnimationUtils.makeOutAnimation(ShoppingCartActivity.this, true));
                    holder.getView(R.id.rlCartsItemAfter).setVisibility(View.GONE);
//                    holder.getView(R.id.llCartsItemBefore).startAnimation(AnimationUtils.makeInAnimation(ShoppingCartActivity.this, true));
                    holder.getView(R.id.llCartsItemBefore).setVisibility(View.VISIBLE);
                    EditText etCartsItemNum = (EditText) holder.getView(R.id.etCartsItemNum);
                    holder.setText(R.id.tvCartsItemCdNumber, "X" + etCartsItemNum.getText().toString());
                    scr.setGood_count(etCartsItemNum.getText().toString());

                    int count = Integer.parseInt(scr.getGood_count());
                    int price = Integer.parseInt(scr.getCurrent_price());
                    int all = 0;
                    tvShoppingCartAllPrice.setText(String.valueOf(all + count * price));
                }

                //减号
                holder.setOnClickListener(R.id.ibCartsItemMinus, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText etCartsItemNum = (EditText) holder.getView(R.id.etCartsItemNum);
                        etCartsItemNum.setText(String.valueOf(Integer.parseInt(etCartsItemNum.getText().toString()) - 1));
                    }
                });

                //加号
                holder.setOnClickListener(R.id.ibCartsItemAdd, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText etCartsItemNum = (EditText) holder.getView(R.id.etCartsItemNum);
                        etCartsItemNum.setText(String.valueOf(Integer.parseInt(etCartsItemNum.getText().toString()) + 1));
                    }
                });

                //选择
                holder.setOnClickListener(R.id.ibCartsItemTypeSel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.isWillPay()) {
                            item.setWillPay(false);
                        } else {
                            item.setWillPay(true);
                            for (CartsItem cartsItem : list) {
                                if (cartsItem.isWillPay()) {
                                    isAll = true;
                                }
                            }
                            if (isAll) {
                                ibShopCartsAllSel.setImageResource(R.drawable.select);
                            }
                            int count = Integer.parseInt(scr.getGood_count());
                            int price = Integer.parseInt(scr.getCurrent_price());
                            int all = Integer.parseInt(tvShoppingCartAllPrice.getText().toString());
                            tvShoppingCartAllPrice.setText(String.valueOf(all + count * price));
                        }
                        notifyDataSetChanged();
                    }
                });
                holder.setOnClickListener(R.id.ibCartsItemChildSel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.isWillPay()) {
                            item.setWillPay(false);
                        } else {
                            item.setWillPay(true);
                            for (CartsItem cartsItem : list) {
                                if (cartsItem.isWillPay()) {
                                    isAll = true;
                                }
                            }
                            if (isAll) {
                                ibShopCartsAllSel.setImageResource(R.drawable.select);
                            }
                            int count = Integer.parseInt(scr.getGood_count());
                            int price = Integer.parseInt(scr.getCurrent_price());
                            int all = 0;
                            tvShoppingCartAllPrice.setText(String.valueOf(all + count * price));
                        }
                        notifyDataSetChanged();
                    }
                });

                //编辑
                holder.setOnClickListener(R.id.btCartsItemEdit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((Button) holder.getView(R.id.btCartsItemEdit)).getText().toString().equals("编辑")) {
                            item.setIfEdit(true);
                        } else if (((Button) holder.getView(R.id.btCartsItemEdit)).getText().toString().equals("完成")) {
                            item.setIfEdit(false);
                        }
                        notifyDataSetChanged();
                    }
                });
                //删除
                holder.setOnClickListener(R.id.btCartsItemDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        list.remove(position);
//                        shoppingCartAdapter.notifyDataSetChanged();
                        deleteShoppingCartHttp(scr.getCart_id());
                    }
                });
                /**
                 * 选择规格
                 */
                holder.setOnClickListener(R.id.tvCartsItemNorm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText etCartsItemNum = (EditText) holder.getView(R.id.etCartsItemNum);
                        showNormPop(scr, Integer.parseInt(etCartsItemNum.getText().toString()), etCartsItemNum);
                    }
                });
            }
        });
        shoppingCartAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
     * 购物车删除
     */
    private void deleteShoppingCartHttp(String cartId) {
        Map<String, Object> map = new HashMap<>();
        map.put("cart_id", cartId);
        String params = EncryptionUtil.getParameter(ShoppingCartActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/deleteShoppingCart.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_deleteShoppingCart")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ShoppingCartActivity.this, "删除中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ShoppingCartActivity.this, "删除失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        DeleteShoppingCartResult deleteShoppingCartResult =
                                (DeleteShoppingCartResult) GsonUtil.json2Object(response, DeleteShoppingCartResult.class);
                        if (deleteShoppingCartResult != null
                                && deleteShoppingCartResult.getRet().equals("1")) {
                            getShoppingCartListHttp();
                        } else {
                            Toasty.warning(ShoppingCartActivity.this, "删除失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private CustomPopWindow mNormPop;
    private TagFlowLayout flowLayout;
    private List<String> flowList;
    private TextView tvTabShow, tvArticleName, tvArticleNorm;
    private RoundedImageView rivArticlePhoto;
    private ImageButton ibArticlePlus, ibArticleMinus;
    private EditText etArticleNum;
    private Button btArticleBuy;
    private int articleNum = 1;

    public void showNormPop(ShoppingCartListResult.ResultBean scr, int number, final EditText et) {
        articleNum = number;
        flowList = new ArrayList<>();
        View view = LayoutInflater.from(ShoppingCartActivity.this).inflate(R.layout.pop_norm, null);
        rivArticlePhoto = (RoundedImageView) view.findViewById(R.id.rivArticlePhoto);
        tvArticleName = (TextView) view.findViewById(R.id.tvArticleName);
        tvArticleNorm = (TextView) view.findViewById(R.id.tvArticleNorm);
        ibArticlePlus = (ImageButton) view.findViewById(R.id.ibArticlePlus);
        ibArticleMinus = (ImageButton) view.findViewById(R.id.ibArticleMinus);
        etArticleNum = (EditText) view.findViewById(R.id.etArticleNum);
        btArticleBuy = (Button) view.findViewById(R.id.btArticleBuy);
        flowLayout = (TagFlowLayout) view.findViewById(R.id.flowLayout);
        ibArticlePlus.setOnClickListener(new NormClickListener());
        ibArticleMinus.setOnClickListener(new NormClickListener());
        btArticleBuy.setOnClickListener(new NormClickListener());
        etArticleNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    etArticleNum.setText("1");
                } else {
                    articleNum = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Glide.with(ShoppingCartActivity.this)
                .load(scr.getGood_cover())
                .into(rivArticlePhoto);
        etArticleNum.setText(String.valueOf(articleNum));
        tvArticleNorm.setText(scr.getSpec());
        initFlowLayoutData();
        mNormPop = new CustomPopWindow.PopupWindowBuilder(ShoppingCartActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setAnimationStyle(R.style.pop_anim)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Logger.e("articleNum == " + articleNum);
                        et.setText(String.valueOf(articleNum));
//                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                })
                .create()
                .showAtLocation(rlShopCartsParent, Gravity.BOTTOM, 0, 0);
    }

    private class NormClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ibArticlePlus:
                    etArticleNum.setText(String.valueOf(++articleNum));
                    break;
                case R.id.ibArticleMinus:
                    if (articleNum > 1) {
                        etArticleNum.setText(String.valueOf(--articleNum));
                    }
                    break;
                case R.id.btArticleBuy:
//                    tvNorm.setText(normSelect);
                    if (mNormPop != null) {
                        mNormPop.dissmiss();
                    }
                    break;
            }
        }
    }

    private String normSelect;

    /**
     * 加载流式布局
     */
    private void initFlowLayoutData() {
        flowList.add("字大大大");
        flowList.add("字大大撒多");
        flowList.add("字大");
        flowList.add("字大大");
        flowList.add("字大大");
        flowList.add("字大大撒多撒多撒");
        flowLayout.setAdapter(new TagAdapter<String>(flowList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                tvTabShow = (TextView) LayoutInflater.from(ShoppingCartActivity.this).inflate(R.layout.tv, flowLayout, false);
                tvTabShow.setText(s);
                return tvTabShow;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                ((TextView) view).setTextColor(Color.parseColor("#949393"));
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                ((TextView) view).setTextColor(Color.parseColor("#333333"));
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Logger.wtf("setOnTagClickListener选中项：" + flowList.get(position));
                normSelect = flowList.get(position);
                return true;
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void shoppingCartBack() {
        ShoppingCartActivity.this.finish();
    }

    /**
     * 用户购物车列表
     */
    private void getShoppingCartListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ShoppingCartActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(ShoppingCartActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getShoppingCartList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getShoppingCartList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ShoppingCartActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshShoppingCart != null) {
                            refreshShoppingCart.finishRefresh();
                        }
                        Toasty.warning(ShoppingCartActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshShoppingCart != null) {
                            refreshShoppingCart.finishRefresh();
                        }

                        ShoppingCartListResult shoppingCartListResult =
                                (ShoppingCartListResult) GsonUtil.json2Object(response, ShoppingCartListResult.class);
                        if (shoppingCartListResult != null
                                && shoppingCartListResult.getRet().equals("1")) {
                            if (isMore) {
                                list.clear();
                                for (ShoppingCartListResult.ResultBean scr : shoppingCartListResult.getResult()) {
                                    list.add(new CartsItem(false, false, scr));
                                }
                                shoppingCartAdapter.getDatas().addAll(list);
                                shoppingCartAdapter.notifyDataSetChanged();
                            } else {
                                list.clear();
                                for (ShoppingCartListResult.ResultBean scr : shoppingCartListResult.getResult()) {
                                    list.add(new CartsItem(false, false, scr));
                                }
                                shoppingCartAdapter.getDatas().clear();
                                shoppingCartAdapter.getDatas().addAll(list);
                                shoppingCartAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(ShoppingCartActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private List<String> goodIds = new ArrayList<>();
    private List<String> nums = new ArrayList<>();

    /**
     * 购物车结算
     */
    @OnClick(R.id.btSettlement)
    void cartSettlement() {
//        CartsItem   shoppingCartAdapter
        for (CartsItem cartsItem : (List<CartsItem>) shoppingCartAdapter.getDatas()) {
            if (cartsItem.isWillPay()) {
                goodIds.add(((ShoppingCartListResult.ResultBean) cartsItem.getItem()).getGood_id());
                nums.add(((ShoppingCartListResult.ResultBean) cartsItem.getItem()).getGood_count());
            }
        }
        String ids = Utils.join(goodIds, ",");
        String numbers = Utils.join(nums, ",");
        Intent intent = new Intent(ShoppingCartActivity.this, ShopCartSettlementActivity.class);
        intent.putExtra("goodIds", ids);
        intent.putExtra("nums", numbers);
        startActivity(intent);
    }


}
