package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.AddressBean;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelListView;

/**
 * 施工区域选择
 */

public class WorkAreaPopupWindow extends PopupWindow {

    private Context context;
    private View view;
    private WheelListView wheelProvince, wheelCity, wheelCounty;
    private List<String> provinces, citys, countys;
    private AddressBean address;
    private LineConfig config;
    private Button btCancel, btDetermine;
    private ImageView ivIcon;
    private TextView tvIcon;
    private PopupInterface pi;
    private int pIndex = 0, cIndex = 0, aIndex = 0;

    public WorkAreaPopupWindow(Context context, PopupInterface pi) {
        this.context = context;
        this.pi = pi;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_worker_area, null);
        initView();

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

    private void initView() {
        wheelProvince = (WheelListView) view.findViewById(R.id.wheelProvince);
        wheelCity = (WheelListView) view.findViewById(R.id.wheelCity);
        wheelCounty = (WheelListView) view.findViewById(R.id.wheelCounty);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        btDetermine = (Button) view.findViewById(R.id.btDetermine);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        tvIcon = (TextView) view.findViewById(R.id.tvIcon);


        if (SharedPrefsUtil.getValue(context, "isEmployer", true)) {
            ivIcon.setImageResource(R.mipmap.employee_location_icon);
            tvIcon.setText("地址添加");
            tvIcon.setTextColor(Color.parseColor("#31D09A"));
        } else {
            ivIcon.setImageResource(R.mipmap.area_icon);
            tvIcon.setText("施工区域");
            tvIcon.setTextColor(Color.parseColor("#FBC83F"));
        }

        provinces = new ArrayList<String>();
        citys = new ArrayList<String>();
        countys = new ArrayList<String>();

        address = GsonUtil.parseJson(HelperUtil.readAssert(context, "address.txt"), AddressBean.class);

        config = new LineConfig();
        config.setColor(Color.parseColor("#000000"));//线颜色
        config.setAlpha(50);//线透明度
        config.setRatio(1f);//线比率
        config.setThick(ConvertUtils.toPx(context, 2));//线粗
        config.setShadowVisible(false);

        for (AddressBean.ProvinceBean provinceBean : address.getProvince()) {
            provinces.add(provinceBean.getName());
        }
        citys.add(address.getProvince().get(0).getCity().get(0).getName());
        countys.add(address.getProvince().get(0).getCity().get(0).getArea().get(0).getName());

        wheelProvince.setItems(provinces);
        wheelProvince.setLineConfig(config);
        wheelProvince.setSelectedTextColor(Color.parseColor("#000000"));
        wheelProvince.setTextSize(18);

        wheelProvince.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(boolean b, final int i, String s) {
                pIndex = i;
                pi.getResult(2, provinces.get(pIndex) + "-" + citys.get(cIndex) + "-" + countys.get(aIndex));
                citys = new ArrayList<String>();
                for (AddressBean.ProvinceBean.CityBean cityBean : address.getProvince().get(i).getCity()) {
                    citys.add(cityBean.getName());
                }
                wheelCity.setItems(citys);
                wheelCity.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
                    @Override
                    public void onItemSelected(boolean b, int index, String s) {
                        cIndex = index;
                        pi.getResult(2, provinces.get(pIndex) + "-" + citys.get(cIndex) + "-" + countys.get(aIndex));
                        countys = new ArrayList<String>();
                        for (AddressBean.ProvinceBean.CityBean.AreaBean areaBean : address.getProvince().get(i).getCity().get(index).getArea()) {
                            countys.add(areaBean.getName());
                        }
                        wheelCounty.setItems(countys);
                        wheelCounty.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
                            @Override
                            public void onItemSelected(boolean b, int i, String s) {
                                aIndex = i;
                                pi.getResult(2, provinces.get(pIndex) + "-" + citys.get(cIndex) + "-" + countys.get(aIndex));
                            }
                        });
                    }
                });
            }
        });

        wheelCity.setItems(citys);
        wheelCity.setLineConfig(config);
        wheelCity.setSelectedTextColor(Color.parseColor("#000000"));
        wheelCity.setTextSize(18);

        wheelCounty.setItems(countys);
        wheelCounty.setLineConfig(config);
        wheelCounty.setSelectedTextColor(Color.parseColor("#000000"));
        wheelCounty.setTextSize(18);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
