package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.WorkerTypeResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.WorkerTypeInterface;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 完善信息/工种选择
 */

public class EmployerTypePopupWindow extends PopupWindow {

    private Context context;
    private View view;
    private RecyclerView rvWorkerTypes;
    private int nPosition = 0;
    private CommonAdapter adapter;
    private WorkerTypeInterface pi;
    private PresenterImpl<Object> presenter;

    public EmployerTypePopupWindow(Context context, WorkerTypeInterface pi) {
        this.context = context;
        this.pi = pi;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_employer_worker_type, null);
        initView();
        successView();

    }

    private void initView() {
        rvWorkerTypes = (RecyclerView) view.findViewById(R.id.rvWorkerTypes);
        clientGetworkertype();
    }

    private void successView() {
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
        this.setAnimationStyle(R.style.anim_popup);
    }

    /**
     * 加载数据
     */
    private void setListData(final List<WorkerTypeResult.DataBean> types) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        rvWorkerTypes.setLayoutManager(gridLayoutManager);
        adapter = new CommonAdapter<WorkerTypeResult.DataBean>(context, R.layout.item_worker_type, types) {
            @Override
            protected void convert(ViewHolder viewHolder, WorkerTypeResult.DataBean item, int cPosition) {
                viewHolder.setText(R.id.tvWorkType, item.getName());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(HelperUtil.getScreenWidth(context) / 6,
                        HelperUtil.getScreenHeight(context) / 20);
                TextView tvWorkType = (TextView) viewHolder.getView(R.id.tvWorkType);
                tvWorkType.setLayoutParams(lp);
                if (nPosition == cPosition) {
                    viewHolder.setTextColor(R.id.tvWorkType, Color.parseColor("#FFFFFF"));
                    viewHolder.setBackgroundRes(R.id.tvWorkType, R.drawable.oval_guide_button);
                } else {
                    viewHolder.setTextColor(R.id.tvWorkType, Color.parseColor("#444444"));
                    viewHolder.setBackgroundRes(R.id.tvWorkType, R.drawable.oval_worker_type);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                nPosition = position;
                pi.getResult(types.get(position));
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvWorkerTypes.setAdapter(adapter);
    }

    /**
     * 获取工种
     */
    private void clientGetworkertype() {
        presenter = new PresenterImpl<Object>(context);
        presenter.onCreate();
        presenter.getworkertype();
        presenter.attachView(workerTypeView);
    }

    private ClientBaseView<Object> workerTypeView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                WorkerTypeResult type = (WorkerTypeResult) GsonUtil.parseJson(baseBean, WorkerTypeResult.class);
                setListData(type.getData());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerTypeView==" + result);
        }
    };

}
