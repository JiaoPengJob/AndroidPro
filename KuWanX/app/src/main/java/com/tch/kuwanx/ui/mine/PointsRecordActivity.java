package com.tch.kuwanx.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gavin.com.library.PowerfulStickyDecoration;
import com.gavin.com.library.listener.PowerGroupListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.Income;
import com.tch.kuwanx.bean.Points;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.UserIntegralResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 积分记录
 */
public class PointsRecordActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvPointRecord)
    RecyclerView rvPointRecord;
    @BindView(R.id.refreshPointsRecord)
    SmartRefreshLayout refreshPointsRecord;

    private CommonAdapter pointRecordAdapter;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_record);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("积分记录");
        initPointRecord();
        refreshPointsRecord.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserIntegralHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        getUserIntegralHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        getUserIntegralHttp();
    }

    private List<Points> list = new ArrayList<>();

    private void initPointRecord() {
        rvPointRecord.setLayoutManager(new LinearLayoutManager(this));
        rvPointRecord.setAdapter(pointRecordAdapter = new CommonAdapter<Points>(this,
                R.layout.item_point_record, list) {
            @Override
            protected void convert(ViewHolder holder, Points item, int position) {
                UserIntegralResult.ResultBean result = (UserIntegralResult.ResultBean) item.getItem();
                Glide.with(PointsRecordActivity.this)
                        .load("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1516688548&di=2b651281b08d776b0c0d1efb9a0ac819&src=http://att2.citysbs.com/hangzhou/2016/08/08/17/middle_992x662-172100_v2_20221470648060391_8a9f824024805fb060b2ef41fb353e03.jpg")
                        .apply(bitmapTransform(new CropCircleTransformation()))
                        .into((ImageView) holder.getView(R.id.ivPointRecordItem));
                holder.setText(R.id.tvPointResTime, result.getCreate_time());
                //目前用来源代替物品名称
                if (result.getIntegral_sorce().equals("1")) {
                    holder.setText(R.id.tvPointResName, "置换物品");
                } else {
                    holder.setText(R.id.tvPointResName, "购买商品");
                }

                holder.setText(R.id.tvPointResCount, result.getIntegral_value());
                if (result.getIntegral_value().indexOf("+") != -1) {
                    holder.setTextColor(R.id.tvPointResCount, Color.parseColor("#F67171"));
                } else {
                    holder.setTextColor(R.id.tvPointResCount, Color.parseColor("#A5A4A4"));
                }

            }
        });

        PowerGroupListener listener = new PowerGroupListener() {
            @Override
            public String getGroupName(int position) {
                return list.get(position).getDate();
            }

            @Override
            public View getGroupView(int position) {
                //获取自定定义的组View
                View view = getLayoutInflater().inflate(R.layout.item_group, null, false);
                TextView tvPointsDate = (TextView) view.findViewById(R.id.tvPointsDate);
                TextView tvPointsPlus = (TextView) view.findViewById(R.id.tvPointsPlus);
                TextView tvPointsLess = (TextView) view.findViewById(R.id.tvPointsLess);
                tvPointsDate.setText(list.get(position).getDate());
                tvPointsPlus.setText("增加" + incomes.get(position).getPlus());
                tvPointsLess.setText("减少" + incomes.get(position).getLess());
                return view;
            }
        };
        PowerfulStickyDecoration decoration = PowerfulStickyDecoration.Builder
                .init(listener)
                .setGroupHeight(120)       //设置高度
                .isAlignLeft(true)                                 //靠右边显示   默认左边
                .setGroupBackground(Color.parseColor("#F2F2F2"))    //设置背景   默认透明
                .setDivideColor(Color.parseColor("#F2F2F2"))        //分割线颜色
                .setDivideHeight(1)       //分割线高度
                .build();
        rvPointRecord.addItemDecoration(decoration);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void pointsRecordBack() {
        PointsRecordActivity.this.finish();
    }

    /**
     * 获取积分记录
     */
    private void getUserIntegralHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(PointsRecordActivity.this));
//        map.put("appuser_id", "be8c68e7aa754021b3b03c4bdca80e78");
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(PointsRecordActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserIntegral.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getUserIntegral")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        PointsRecordActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshPointsRecord != null) {
                            refreshPointsRecord.finishLoadmore();
                            refreshPointsRecord.finishRefresh();
                        }
                        Toasty.warning(PointsRecordActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshPointsRecord != null) {
                            refreshPointsRecord.finishLoadmore();
                            refreshPointsRecord.finishRefresh();
                        }

                        UserIntegralResult userIntegralResult =
                                (UserIntegralResult) GsonUtil.json2Object(response, UserIntegralResult.class);
                        if (userIntegralResult != null
                                && userIntegralResult.getRet().equals("1")) {
                            if (isMore) {
                                for (UserIntegralResult.ResultBean uir : userIntegralResult.getResult()) {
                                    try {
                                        list.add(new Points(Utils.dateToString(
                                                Utils.stringToDate(
                                                        Utils.time2DayString(uir.getCreate_time()), "yyyy-MM"), "yyyy-MM"), uir));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                pointRecordAdapter.notifyDataSetChanged();
                                incomes = Utils.getIncome(list);
                            } else {
                                list.clear();
                                for (UserIntegralResult.ResultBean uir : userIntegralResult.getResult()) {
                                    try {
                                        list.add(new Points(Utils.dateToString(
                                                Utils.stringToDate(
                                                        Utils.time2DayString(uir.getCreate_time()), "yyyy-MM"), "yyyy-MM"), uir));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                pointRecordAdapter.notifyDataSetChanged();
                                incomes = Utils.getIncome(list);
                            }
                        }
                    }
                });
    }

    private List<Income> incomes;
}
