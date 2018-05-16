package com.tch.kuwanx.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.adapter.TabAdapter;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AddGoodCollectResult;
import com.tch.kuwanx.result.AddShoppingCartResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.CustomViewPager;
import com.tch.kuwanx.view.MyIndicator;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 商品详情页
 */
public class ProductDetailsActivity extends BaseActivity implements MyIndicator.onTabClickListener {

    @BindView(R.id.productIndicator)
    MyIndicator productIndicator;
    @BindView(R.id.productViewPager)
    CustomViewPager productViewPager;
    @BindView(R.id.rlProductDetailsParent)
    RelativeLayout rlProductDetailsParent;

    private String[] mTitles = {"商品", "详情", "评价"};
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Intent intent;
    private String goodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        initView();
    }

    private ProductFragment productFragment;

    private void initView() {
        if (getIntent().getStringExtra("goodId") != null) {
            goodId = getIntent().getStringExtra("goodId");
        }
        productIndicator.setTabTitles(mTitles);
        productIndicator.setOnTabClickListener(this);
        fragments.add(productFragment = ProductFragment.getInstance(goodId));
        fragments.add(DetailsFragment.getInstance(goodId));
        fragments.add(ProductCommentFragment.getInstance(goodId));
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), fragments);
        productViewPager.setAdapter(adapter);
        productViewPager.setPagingEnabled(false);
    }

    @Override
    public void onTabClick(String s, int i) {
        productViewPager.setCurrentItem(i);
    }

    /**
     * 购物车
     */
    @OnClick(R.id.ibShopCart)
    public void shopCart() {
        intent = new Intent(ProductDetailsActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    /**
     * 消息
     */
    @OnClick(R.id.ibProductMsg)
    public void productMsg() {

    }

    /**
     * 客服
     */
    @OnClick(R.id.llCustomerService)
    public void customerService() {

    }

    @BindView(R.id.ivProCollection)
    ImageView ivProCollection;
    @BindView(R.id.tvProCollection)
    TextView tvProCollection;
    private int isCollection = 1;

    /**
     * 收藏
     */
    @OnClick(R.id.llCollection)
    public void productCollection() {
        if (isCollection == 1) {
            AddGoodCollectHttp();
        } else {
            cancleGoodCollectHttp();
        }
    }

    /**
     * 商品添加收藏
     */
    private void AddGoodCollectHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ProductDetailsActivity.this));
        map.put("good_id", goodId);
        String params = EncryptionUtil.getParameter(ProductDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/AddGoodCollect.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addGoodCollect")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ProductDetailsActivity.this, "收藏中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ProductDetailsActivity.this, "收藏失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddGoodCollectResult addGoodCollectResult =
                                (AddGoodCollectResult) GsonUtil.json2Object(response, AddGoodCollectResult.class);
                        if (addGoodCollectResult != null
                                && addGoodCollectResult.getRet().equals("1")) {
                            isCollection = 2;
                            ivProCollection.setImageResource(R.drawable.collection_sel);
                            tvProCollection.setText("已收藏");
                        }
                    }
                });
    }

    /**
     * 商品取消收藏
     */
    private void cancleGoodCollectHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ProductDetailsActivity.this));
        map.put("good_id", goodId);
        String params = EncryptionUtil.getParameter(ProductDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/cancleGoodCollect.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_cancleGoodCollect")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ProductDetailsActivity.this, "取消收藏中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ProductDetailsActivity.this, "取消收藏失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddGoodCollectResult addGoodCollectResult =
                                (AddGoodCollectResult) GsonUtil.json2Object(response, AddGoodCollectResult.class);
                        if (addGoodCollectResult != null
                                && addGoodCollectResult.getRet().equals("1")) {
                            isCollection = 1;
                            ivProCollection.setImageResource(R.drawable.collection);
                            tvProCollection.setText("收藏");
                        }
                    }
                });
    }

    /**
     * 加入购物车
     */
    @OnClick(R.id.btJoinShopCart)
    public void joinShopCart() {
        addShoppingCartHttp();
    }

    /**
     * 加入购物车
     */
    private void addShoppingCartHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ProductDetailsActivity.this));
        map.put("good_id", goodId);
        map.put("good_count", "1");
        String params = EncryptionUtil.getParameter(ProductDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/addShoppingCart.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addShoppingCart")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ProductDetailsActivity.this, "添加中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ProductDetailsActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddShoppingCartResult addShoppingCartResult =
                                (AddShoppingCartResult) GsonUtil.json2Object(response, AddShoppingCartResult.class);
                        if (addShoppingCartResult != null
                                && addShoppingCartResult.getRet().equals("1")) {
                            Toasty.warning(ProductDetailsActivity.this, "添加成功！", Toast.LENGTH_SHORT, false).show();
                        } else {
                            Toasty.warning(ProductDetailsActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 立即购买
     */
    @OnClick(R.id.btBuyNow)
    public void buyNow() {
        if (productFragment.getTvNorm().getText().toString().equals("规格")) {
            productFragment.showNormPop();
        } else {
            intent = new Intent(ProductDetailsActivity.this, ConfirmOrderActivity.class);
            intent.putExtra("goodId", goodId);
            intent.putExtra("number", productFragment.getArticleNum());
            startActivity(intent);
        }
    }

    public RelativeLayout getRlProductDetailsParent() {
        return rlProductDetailsParent;
    }

    public MyIndicator getProductIndicator() {
        return productIndicator;
    }

    public CustomViewPager getProductViewPager() {
        return productViewPager;
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibProDetBack)
    public void proDetBack() {
        ProductDetailsActivity.this.finish();
    }
}
