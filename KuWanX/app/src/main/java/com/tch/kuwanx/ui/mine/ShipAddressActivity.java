package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.DeleteUserAddressResult;
import com.tch.kuwanx.result.UserAddressResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
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
 * 管理收货地址
 */
public class ShipAddressActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvShipAddress)
    RecyclerView rvShipAddress;
    @BindView(R.id.refreshShipAddress)
    SmartRefreshLayout refreshShipAddress;

    private CommonAdapter shipAddressAdapter;
    private String activity;
    private UserAddressResult.ResultBean defaultAddress;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("管理收货地址");
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            if (activity.equals("ConfirmOrderActivity")) {
                defaultAddress = (UserAddressResult.ResultBean) getIntent().getSerializableExtra("address");
            }
        }
        initShipAddress();
        refreshShipAddress.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddressHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        getUserAddressHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        getUserAddressHttp();
    }

    /**
     * 加载地址列表
     */
    private void initShipAddress() {
        rvShipAddress.setLayoutManager(new LinearLayoutManager(this));
        rvShipAddress.setAdapter(shipAddressAdapter = new CommonAdapter<UserAddressResult.ResultBean>(this,
                R.layout.item_ship_address, new ArrayList<UserAddressResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, final UserAddressResult.ResultBean item, int position) {
                holder.setText(R.id.tvAddressUserName, item.getName());
                holder.setText(R.id.tvAddressUserPhone, item.getPhone());
                holder.setText(R.id.tvAddressUserArea, item.getDetail());
                if (item.getIddefault().equals("2")) {
                    holder.setImageResource(R.id.ivIsDefault, R.drawable.default_sel);
                    holder.setOnClickListener(R.id.llAddressDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toasty.warning(ShipAddressActivity.this, "默认地址不可删除！", Toast.LENGTH_SHORT, false).show();
                        }
                    });
                } else {
                    holder.setImageResource(R.id.ivIsDefault, R.drawable.oval_unselect);
                    holder.setOnClickListener(R.id.llAddressDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteUserAddressHttp(item.getId());
                        }
                    });
                }
            }
        });
        shipAddressAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (activity.equals("ConfirmOrderActivity")) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address", ((UserAddressResult.ResultBean) shipAddressAdapter.getDatas().get(position)));
                    intent.putExtras(bundle);
                    setResult(10, intent);
                    ShipAddressActivity.this.finish();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 新建地址
     */
    @OnClick(R.id.btNewAddress)
    public void newAddress() {
        Intent intent = new Intent(ShipAddressActivity.this, AddAddressActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void shipAddressBack() {
        if (activity.equals("ConfirmOrderActivity")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("address", defaultAddress);
            intent.putExtras(bundle);
            setResult(10, intent);
            ShipAddressActivity.this.finish();
        } else {
            ShipAddressActivity.this.finish();
        }
    }

    /**
     * 获取用户地址
     */
    private void getUserAddressHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(ShipAddressActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(ShipAddressActivity.this, map);
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
                        if (refreshShipAddress != null) {
                            refreshShipAddress.finishLoadmore();
                            refreshShipAddress.finishRefresh();
                        }
                        Toasty.warning(ShipAddressActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshShipAddress != null) {
                            refreshShipAddress.finishLoadmore();
                            refreshShipAddress.finishRefresh();
                        }

                        UserAddressResult userAddressResult =
                                (UserAddressResult) GsonUtil.json2Object(response, UserAddressResult.class);
                        if (userAddressResult != null
                                && userAddressResult.getRet().equals("1")) {
                            if (isMore) {
                                shipAddressAdapter.getDatas().addAll(userAddressResult.getResult());
                                shipAddressAdapter.notifyDataSetChanged();
                            } else {
                                shipAddressAdapter.getDatas().clear();
                                shipAddressAdapter.getDatas().addAll(userAddressResult.getResult());
                                shipAddressAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(ShipAddressActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 删除地址
     */
    private void deleteUserAddressHttp(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        String params = EncryptionUtil.getParameter(ShipAddressActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/deleteUserAddress.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_deleteUserAddress")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Toasty.warning(ShipAddressActivity.this, "删除失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        DeleteUserAddressResult deleteUserAddressResult =
                                (DeleteUserAddressResult) GsonUtil.json2Object(response, DeleteUserAddressResult.class);
                        if (deleteUserAddressResult != null
                                && deleteUserAddressResult.getRet().equals("1")) {
                            Toasty.warning(ShipAddressActivity.this, "删除成功！", Toast.LENGTH_SHORT, false).show();
                            getUserAddressHttp();
                        } else {
                            Toasty.warning(ShipAddressActivity.this, "删除失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
