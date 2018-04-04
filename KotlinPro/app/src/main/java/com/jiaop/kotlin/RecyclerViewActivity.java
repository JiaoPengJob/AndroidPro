package com.jiaop.kotlin;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiaop.libs.base.JPBaseActivity;
import com.jiaop.libs.utils.JPActivityUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecyclerViewActivity extends JPBaseActivity {

    @BindView(R.id.rvListShow)
    RecyclerView rvListShow;
    @BindView(R.id.llParent)
    LinearLayout llParent;

    private CommonAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooter;

    @Override
    protected int layoutId() {
        return R.layout.activity_recycler_view;
    }

    private View headView;

    @Override
    protected void initView() {
        rvListShow.setLayoutManager(new LinearLayoutManager(this));
//        rvListShow.setAdapter(mAdapter = new CommonAdapter<String>(this, R.layout.item_show, new ArrayList<String>()) {
//            @Override
//            protected void convert(ViewHolder holder, String item, int position) {
//                holder.setText(R.id.tvItemText, item);
//            }
//        });
        mAdapter = new CommonAdapter<String>(this, R.layout.item_show, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.tvItemText, item);
            }
        };
        //当设置了头布局或者底布局后，应使用footer刷新
        mHeaderAndFooter = new HeaderAndFooterWrapper(mAdapter);
        mHeaderAndFooter.addHeaderView(headView = LayoutInflater.from(this).inflate(R.layout.head_view, rvListShow, false));
        rvListShow.setAdapter(mHeaderAndFooter);
        mHeaderAndFooter.notifyDataSetChanged();
        initHeadView();
    }

    private Button btHeadClick;

    private void initHeadView() {
        btHeadClick = (Button) headView.findViewById(R.id.btHeadClick);
        btHeadClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Activity> activities = JPActivityUtil.getInstance().getAllActivity();
                Toast.makeText(RecyclerViewActivity.this, "Head -- View -- Click" + activities.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<String> mList = new ArrayList<>();

    @Override
    protected void initWiFiData() {
        mList.clear();
        for (int i = 0; i < 20; i++) {
            mList.add("WiFi --" + i);
        }
        mAdapter.getDatas().clear();
        mAdapter.getDatas().addAll(mList);
//        mAdapter.notifyDataSetChanged();
        mHeaderAndFooter.notifyDataSetChanged();
    }

    @Override
    protected void initNetData() {
        mList.clear();
        for (int i = 0; i < 20; i++) {
            mList.add("Net --" + i);
        }
        mAdapter.getDatas().clear();
        mAdapter.getDatas().addAll(mList);
//        mAdapter.notifyDataSetChanged();
        mHeaderAndFooter.notifyDataSetChanged();
    }

    @Override
    protected void initOfflineData() {
        mList.clear();
        for (int i = 0; i < 20; i++) {
            mList.add("Offline --" + i);
        }
        mAdapter.getDatas().clear();
        mAdapter.getDatas().addAll(mList);
//        mAdapter.notifyDataSetChanged();
        mHeaderAndFooter.notifyDataSetChanged();
    }

    @Override
    protected int statusBarColor() {
        return R.color.btColor;
    }


}
