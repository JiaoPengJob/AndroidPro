package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.AddressBean;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.WorkerAreasResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.util.CacheDataManager;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelListView;

/**
 * 施工区域选择/工人
 */

public class WorkerPlacePopupWindow extends PopupWindow {

    @BindView(R.id.rvWorkerPlaces)
    RecyclerView rvWorkerPlaces;

    private Context context;
    private View view;
    private PopupInterface pi;
    private CommonAdapter adapter;
    private int index = 0;
    private PresenterImpl<Object> presenter;
    private List<WorkerAreasResult> list;

    public WorkerPlacePopupWindow(Context context, PopupInterface pi) {
        this.context = context;
        this.pi = pi;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_worker_places, null);
        ButterKnife.bind(this, view);
//        initView();
        CacheDataManager.clearAllCache(context);
        list = new ArrayList<WorkerAreasResult>();
        getWorkerArea();
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFF000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.AnimTop);
    }

    private void getWorkerArea() {
        presenter = new PresenterImpl<Object>(context);
        presenter.onCreate();
        presenter.getworkerarea();
        presenter.attachView(workerAreaView);
    }

    private ClientBaseView<Object> workerAreaView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                Type listType = new TypeToken<LinkedList<WorkerAreasResult>>() {
                }.getType();
                Gson gson = new Gson();
                list = gson.fromJson(baseBean.getData().toString(), listType);
                list.add(0, new WorkerAreasResult("全市"));
                initView();
            } else {
                Log.e("TAG", "工人端获取区域接口--" + baseBean.getMsg().toString());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerAreaView--" + result);
        }
    };

    private void initView() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, HelperUtil.getScreenHeight(context) / 3);
        rvWorkerPlaces.setLayoutParams(lp);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvWorkerPlaces.setLayoutManager(layoutManager);
        rvWorkerPlaces.setAdapter(adapter = new CommonAdapter<WorkerAreasResult>(context, R.layout.item_worker_places, list) {
            @Override
            protected void convert(ViewHolder holder, WorkerAreasResult item, int position) {
                holder.setText(R.id.tvPlace, item.getArea());
                if (index == position) {
                    holder.setTextColor(R.id.tvPlace, Color.parseColor("#FFFFFF"));
                    holder.setBackgroundRes(R.id.tvPlace, R.drawable.oval_worker_type_select);
                } else {
                    holder.setTextColor(R.id.tvPlace, Color.parseColor("#444444"));
                    holder.setBackgroundRes(R.id.tvPlace, R.color.white);
                }
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                index = position;
                pi.getResult(1, list.get(position).getArea());
                adapter.notifyDataSetChanged();
                WorkerPlacePopupWindow.this.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

}
