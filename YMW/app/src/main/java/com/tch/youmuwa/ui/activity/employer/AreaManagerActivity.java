package com.tch.youmuwa.ui.activity.employer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.DefaultedProjectAddressParam;
import com.tch.youmuwa.bean.parameters.DeleteAreaParam;
import com.tch.youmuwa.bean.parameters.ProjectAddrsParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.ProjectAddsResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.view.RecyclerViewDecoration;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地址管理页面
 */
public class AreaManagerActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.rvAreaManager)
    RecyclerView rvAreaManager;
    @BindView(R.id.refreshAreaManager)
    SmartRefreshLayout refreshAreaManager;
    /**
     * 设置的参数
     */
    private Intent intent;//跳转
    private PresenterImpl<Object> presenter;//接口
    private CommonAdapter adapter;//适配器
    private int selIndex = 0;//默认选中的地址
    private ProjectAddsResult.MsgListBean addsResult;//地址返回结果
    private String activity;//页面标识
    private Dialog dialog;//弹出框
    private TextView tvDismissAreaDelete, tvDetermineAreaDelete;//弹出框按钮
    private int areaId = 0;//地址id
    private SVProgressHUD mSVProgressHUD;//加载显示
    private List<ProjectAddsResult.MsgListBean> addsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_manager);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clientAddresses();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        addsList = new ArrayList<ProjectAddsResult.MsgListBean>();
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
        }
        selIndex = getIntent().getIntExtra("areaIndex", 0);
        clientAddresses();
        refreshAreaManager.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                clientAddresses();
            }
        });
    }

    /**
     * 默认列表数据
     */
    private void defaultListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAreaManager.setLayoutManager(layoutManager);
        rvAreaManager.addItemDecoration(new RecyclerViewDecoration(this, "#F0F0F0", 1, false));
        adapter = new CommonAdapter<ProjectAddsResult.MsgListBean>(this, R.layout.item_area_manager, addsList) {
            @Override
            protected void convert(ViewHolder holder, ProjectAddsResult.MsgListBean adds, final int position) {
                holder.setText(R.id.tvAddressInfo, adds.getAddr_province() + adds.getAddr_city() + adds.getAddr_area() + adds.getAddr_detail());
                if (activity.equals("MineDataActivity")) {
                    holder.setOnClickListener(R.id.tvSetDefaultAddress, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            areaId = addsList.get(position).getId();
                            showDeleteDialog();
                        }
                    });
                } else {
                    holder.setVisible(R.id.tvSetDefaultAddress, false);
                    if (selIndex == position) {
                        holder.setVisible(R.id.ivIfSelect, true);
                    } else {
                        holder.setVisible(R.id.ivIfSelect, false);
                    }
                }
            }
        };
        if (adapter != null) {
            rvAreaManager.setAdapter(adapter);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    selIndex = position;
                    adapter.notifyDataSetChanged();
                    if (activity.equals("MineDataActivity")) {
                        intent = new Intent(AreaManagerActivity.this, AddAreaActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("address", addsList.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("addsResult", addsList.get(position));
                        intent.putExtra("areaIndex", position);
                        setResult(RESULT_OK, intent);
                        AreaManagerActivity.this.finish();
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }

    /**
     * 添加地址
     */
    @OnClick(R.id.ibAddAreaManager)
    public void addAreaManager() {
        intent = new Intent(this, AddAreaActivity.class);
        startActivity(intent);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatAreaManager() {
        this.finish();
    }

    /**
     * 获取施工地址列表
     */
    private void clientAddresses() {
        mSVProgressHUD.showWithStatus("加载中...");
        ProjectAddrsParam projectAddrsParam = new ProjectAddrsParam();
        presenter = new PresenterImpl<Object>(AreaManagerActivity.this);
        presenter.onCreate();
        presenter.getprojectaddrs(projectAddrsParam);
        presenter.attachView(projectAddsView);
    }

    private ClientBaseView<Object> projectAddsView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshAreaManager.isRefreshing()) {
                refreshAreaManager.finishRefresh();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(AreaManagerActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                ProjectAddsResult projectAddsResult = (ProjectAddsResult) GsonUtil.parseJson(baseBean.getData(), ProjectAddsResult.class);
                addsList = projectAddsResult.getMsg_list();
                defaultListData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshAreaManager.isRefreshing()) {
                refreshAreaManager.finishRefresh();
            }
            Log.e("Error", "projectAddsView==" + result);
        }
    };

    /**
     * 显示确认删除的弹窗
     */
    private void showDeleteDialog() {
        dialog = new Dialog(this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_area_delete, null);
        tvDismissAreaDelete = (TextView) layout.findViewById(R.id.tvDismissAreaDelete);
        tvDetermineAreaDelete = (TextView) layout.findViewById(R.id.tvDetermineAreaDelete);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvDismissAreaDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //确认
        tvDetermineAreaDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArea();
                dialog.cancel();
            }
        });
    }

    /**
     * 删除地址
     */
    private void deleteArea() {
        mSVProgressHUD.showWithStatus("加载中...");
        DeleteAreaParam deleteAreaParam = new DeleteAreaParam(
                areaId
        );
        presenter = new PresenterImpl<Object>(AreaManagerActivity.this);
        presenter.onCreate();
        presenter.removeprojectaddress(deleteAreaParam);
        presenter.attachView(deleteAreaView);
    }

    private ClientBaseView<Object> deleteAreaView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(AreaManagerActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                addsList.clear();
                adapter.notifyDataSetChanged();
                clientAddresses();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "deleteAreaView==" + result);
        }
    };

    /**
     * 监听后退物理按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bl = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
                if (presenter != null) {
                    presenter.onStop();
                }
                bl = false;
            } else {
                AreaManagerActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }

}
