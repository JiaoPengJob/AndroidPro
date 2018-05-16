package com.tch.kuwanx.ui.release;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.PublishPostResult;
import com.tch.kuwanx.result.UserCdsResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.mine.article.ArticleActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import es.dmoral.toasty.Toasty;

/**
 * 发布置换
 */
public class ReleaseActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvReleaseAddArticlePhoto)
    RecyclerView rvReleaseAddArticlePhoto;
    @BindView(R.id.rgReleaseRes)
    RadioGroup rgReleaseRes;
    @BindView(R.id.rbXBox)
    RadioButton rbXBox;
    @BindView(R.id.rbPs)
    RadioButton rbPs;
    @BindView(R.id.rbSwitch)
    RadioButton rbSwitch;
    @BindView(R.id.rgReleaseDealType)
    RadioGroup rgReleaseDealType;
    @BindView(R.id.rbReleaseInExchange)
    RadioButton rbReleaseInExchange;
    @BindView(R.id.rbReleaseNoChangeBack)
    RadioButton rbReleaseNoChangeBack;
    @BindView(R.id.ibMailing)
    ImageButton ibMailing;
    @BindView(R.id.ibFlashing)
    ImageButton ibFlashing;
    @BindView(R.id.ibFaceCross)
    ImageButton ibFaceCross;
    @BindView(R.id.tvReleaseDateStart)
    TextView tvReleaseDateStart;
    @BindView(R.id.tvReleaseDateEnd)
    TextView tvReleaseDateEnd;
    @BindView(R.id.etReleaseDescribeRes)
    EditText etReleaseDescribeRes;

    private Intent intent;
    private CommonAdapter releaseAddArticleAdapter;
    private Time time = new Time();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        time.setToNow();
        tvReleaseDateStart.setText(time.year + "-" + (time.month + 1) + "-" + time.monthDay);
        tvReleaseDateEnd.setText(time.year + "-" + (time.month + 1) + "-" + time.monthDay);
        tvTitleContent.setText("发布置换");
        btTitleFeatures.setVisibility(View.VISIBLE);
        btTitleFeatures.setText("发送");
        initReleaseAddArticlePhoto();
        releaseResChoose();
        initEditInputListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initEditInputListener() {
        etReleaseDescribeRes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etReleaseDescribeRes.getLineCount() >= 5) {
                    etReleaseDescribeRes.setLines(5);
                }
            }
        });

        etReleaseDescribeRes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
                if ((view.getId() == R.id.etReleaseDescribeRes && Utils.canVerticalScroll(etReleaseDescribeRes))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
    }

    private String resType = "1", dealType = "1", deliveryMethod = "邮寄";

    private void releaseResChoose() {
        rgReleaseRes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbXBox.getId() == i) {
                    resType = "1";
                } else if (rbPs.getId() == i) {
                    resType = "2";
                } else if (rbSwitch.getId() == i) {
                    resType = "3";
                }
            }
        });
        rgReleaseDealType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbReleaseInExchange.getId() == i) {
                    dealType = "1";
                } else if (rbReleaseNoChangeBack.getId() == i) {
                    dealType = "2";
                }
            }
        });
    }

    private String startDateSel, endDateSel;
    private boolean isStartDate = true;

    /**
     * 开始时间
     */
    @OnClick(R.id.tvReleaseDateStart)
    public void releaseDateStart() {
        isStartDate = true;
        onYearMonthDayPicker("开始时间");
    }

    /**
     * 结束时间
     */
    @OnClick(R.id.tvReleaseDateEnd)
    public void releaseDateEnd() {
        isStartDate = false;
        onYearMonthDayPicker("结束时间");
    }

    /**
     * 弹出时间选择轮播组件
     *
     * @param title
     */
    public void onYearMonthDayPicker(String title) {
        DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTextColor(Color.parseColor("#FFDA44"));
        picker.setTextSize(15);
        picker.setLabel("-", "-", "");
        picker.setLabelTextColor(Color.parseColor("#333333"));
        picker.setTopLineColor(Color.parseColor("#DBDBDB"));
        picker.setTopPadding(10);
        picker.setRangeEnd(time.year + 10, time.month + 1, time.monthDay);
        picker.setRangeStart(time.year, time.month + 1, time.monthDay);
        picker.setSelectedItem(time.year, time.month + 1, time.monthDay);
        picker.setDividerColor(Color.parseColor("#DBDBDB"));
        picker.setContentPadding(10, 10);
        picker.setResetWhileWheel(true);
        picker.setCancelTextColor(Color.parseColor("#444444"));
        picker.setSubmitTextColor(Color.parseColor("#444444"));
        picker.setSubmitTextSize(13);
        picker.setCancelTextSize(13);
        picker.setTitleText(title);
        picker.setTitleTextColor(Color.parseColor("#FFDA44"));
        picker.setTitleTextSize(13);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                if (isStartDate) {
                    startDateSel = year + "-" + month + "-" + day;
                    tvReleaseDateStart.setText(startDateSel);
                } else {
                    endDateSel = year + "-" + month + "-" + day;
                    tvReleaseDateEnd.setText(endDateSel);
                }

            }
        });
        picker.show();
    }

    /**
     * 邮寄
     */
    @OnClick(R.id.ibMailing)
    public void mailing() {
        deliveryMethod = "邮寄";
        ibMailing.setImageResource(R.drawable.switch_sel);
        ibFlashing.setImageResource(R.drawable.switch_unsel);
        ibFaceCross.setImageResource(R.drawable.switch_unsel);
    }

    /**
     * 闪送
     */
    @OnClick(R.id.ibFlashing)
    public void flashing() {
        deliveryMethod = "闪送";
        ibMailing.setImageResource(R.drawable.switch_unsel);
        ibFlashing.setImageResource(R.drawable.switch_sel);
        ibFaceCross.setImageResource(R.drawable.switch_unsel);
    }

    /**
     * 面交
     */
    @OnClick(R.id.ibFaceCross)
    public void faceCross() {
        deliveryMethod = "面交";
        ibMailing.setImageResource(R.drawable.switch_unsel);
        ibFlashing.setImageResource(R.drawable.switch_unsel);
        ibFaceCross.setImageResource(R.drawable.switch_sel);
    }

    /**
     * 动态加载图片选择
     */
    private void initReleaseAddArticlePhoto() {
        List<String> resList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            resList.add(Utils.getDrawableResPath(ReleaseActivity.this, R.drawable.add_white));
        }
        rvReleaseAddArticlePhoto.setLayoutManager(new GridLayoutManager(this, 3));
        rvReleaseAddArticlePhoto.setAdapter(releaseAddArticleAdapter = new CommonAdapter<String>(this,
                R.layout.item_release_res, resList) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                if (item.equals(Utils.getDrawableResPath(ReleaseActivity.this, R.drawable.add_white))) {
                    ((ImageView) holder.getView(R.id.ivReleaseResPhoto)).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else {
                    ((ImageView) holder.getView(R.id.ivReleaseResPhoto)).setScaleType(ImageView.ScaleType.FIT_XY);
                }
                Glide.with(ReleaseActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivReleaseResPhoto));
            }
        });
        releaseAddArticleAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                getUserCdsHttp();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CustomPopWindow permutationPop;
    @BindView(R.id.releaseParentView)
    LinearLayout releaseParentView;
    private ViewPager vpReleasePer;
    private List<View> views = new ArrayList<View>();
    private CommonAdapter perAdapter;
    private List<UserCdsResult.ResultBean> allPers = new ArrayList<>();

    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_release_permutation, null);
        vpReleasePer = (ViewPager) view.findViewById(R.id.vpResPer);
        initViewPager();
        permutationPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, 540)
                .enableOutsideTouchableDissmiss(true)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.8f)
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(releaseParentView, Gravity.BOTTOM, 0, 0);
    }

    private void initViewPager() {
        views.clear();
        for (int i = 0; i < (int) Math.ceil(((float) allPers.size()) / 6); i++) {
            views.add(LayoutInflater.from(this).inflate(R.layout.fragment_res, null));
        }
        if (allPers.size() % 6 == 0) {
            views.add(LayoutInflater.from(this).inflate(R.layout.fragment_res, null));
        }
        vpReleasePer.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = views.get(position);
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(views.get(position));
                return views.get(position);
            }
        });
        vpReleasePer.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView rvReleasePer = (RecyclerView) views.get(0).findViewById(R.id.rvReleasePer);
                setReleasePerData(rvReleasePer, 0);
            }
        });
        vpReleasePer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RecyclerView rvReleasePer = (RecyclerView) views.get(position).findViewById(R.id.rvReleasePer);
                setReleasePerData(rvReleasePer, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setReleasePerData(RecyclerView rvReleasePer, final int positionSel) {
        final List<UserCdsResult.ResultBean> perList = new ArrayList<>();
        perList.clear();
        int length = 6 * (positionSel + 1);
        if (positionSel == views.size() - 1) {
            length = allPers.size();
        }
        int index = (6 * (positionSel + 1)) - 6;
        for (int i = index; i < length; i++) {
            perList.add(allPers.get(i));
        }
        if (positionSel == views.size() - 1) {
            perList.add(new UserCdsResult.ResultBean(Utils.getDrawableResPath(this, R.drawable.add_release), "", ""));
        }

        rvReleasePer.setLayoutManager(new GridLayoutManager(this, 3));
        rvReleasePer.setAdapter(perAdapter = new CommonAdapter<UserCdsResult.ResultBean>(this,
                R.layout.item_release_per, perList) {
            @Override
            protected void convert(ViewHolder holder, UserCdsResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(ReleaseActivity.this)
                            .load(item.getHeadpic())
                            .into((ImageView) holder.getView(R.id.ivReleasePerPhoto));
                } else {
                    holder.setImageResource(R.id.ivReleasePerPhoto, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvReleasePer, item.getName());

                if (positionSel == views.size() - 1
                        && position == perList.size() - 1) {
                    holder.getView(R.id.tvReleasePer).setVisibility(View.INVISIBLE);
                    holder.setOnClickListener(R.id.ivReleasePerPhoto, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (permutationPop != null) {
                                permutationPop.dissmiss();
                            }
                            Intent intent = new Intent(ReleaseActivity.this, ArticleActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        perAdapter.notifyDataSetChanged();
        perAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                ((UserCdsResult.ResultBean) perAdapter.getDatas().get(position)).getId();
                if (permutationPop != null) {
                    permutationPop.dissmiss();
                }

                cdsId = ((UserCdsResult.ResultBean) perAdapter.getDatas().get(0)).getId();

                releaseAddArticleAdapter.getDatas().clear();
                releaseAddArticleAdapter.getDatas().add(((UserCdsResult.ResultBean) perAdapter.getDatas().get(position)).getHeadpic());
                releaseAddArticleAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private String cdsId = "";

    @BindView(R.id.etReleaseCanPayPrice)
    EditText etReleaseCanPayPrice;
    @BindView(R.id.etReleasePayAddress)
    EditText etReleasePayAddress;
    @BindView(R.id.etReleaseWantPayPrice)
    EditText etReleaseWantPayPrice;
    @BindView(R.id.etReleaseCdsNameFirst)
    EditText etReleaseCdsNameFirst;
    @BindView(R.id.etReleaseCdsNameSecond)
    EditText etReleaseCdsNameSecond;
    @BindView(R.id.etReleaseCdsNameThird)
    EditText etReleaseCdsNameThird;

    private List<String> cdsNames = new ArrayList<>();

    /**
     * 发送
     */
    @OnClick(R.id.btTitleFeatures)
    public void titleFeaturesSend() {
        String date = tvReleaseDateStart.getText().toString() + "~" + tvReleaseDateEnd.getText().toString();

        cdsNames.clear();
        if (!TextUtils.isEmpty(etReleaseCdsNameFirst.getText().toString())) {
            cdsNames.add(etReleaseCdsNameFirst.getText().toString());
        }
        if (!TextUtils.isEmpty(etReleaseCdsNameSecond.getText().toString())) {
            cdsNames.add(etReleaseCdsNameSecond.getText().toString());
        }
        if (!TextUtils.isEmpty(etReleaseCdsNameThird.getText().toString())) {
            cdsNames.add(etReleaseCdsNameThird.getText().toString());
        }

        if (!TextUtils.isEmpty(etReleaseDescribeRes.getText().toString())
                && !TextUtils.isEmpty(etReleaseCanPayPrice.getText().toString())
                && !TextUtils.isEmpty(etReleasePayAddress.getText().toString())
                && !TextUtils.isEmpty(etReleaseWantPayPrice.getText().toString())
                && cdsNames.size() > 0
                && !TextUtils.isEmpty(cdsId)) {
            publishPostHttp(date, Utils.join(cdsNames, "|"));
        } else {
            Toasty.warning(ReleaseActivity.this, "请完善发布信息！", Toast.LENGTH_SHORT, false).show();
        }

    }

    /**
     * 发布置换
     */
    private void publishPostHttp(String date, String cdsNames) {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ReleaseActivity.this));
        map.put("my_cds_id", cdsId);
        map.put("swap_type", dealType);
        map.put("swap_cycle", date);
        map.put("delivery_mode", deliveryMethod);
        map.put("pay_deposit", etReleaseCanPayPrice.getText().toString());
        map.put("cds_address", etReleasePayAddress.getText().toString());
        map.put("swap_deposit", etReleaseWantPayPrice.getText().toString());
        map.put("swap_cds", cdsNames);
        map.put("my_cds_description", etReleaseDescribeRes.getText().toString());
        String params = EncryptionUtil.getParameter(ReleaseActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/publishPost.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_publishPost")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ReleaseActivity.this, "发布中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ReleaseActivity.this, "发布失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        PublishPostResult publishPostResult =
                                (PublishPostResult) GsonUtil.json2Object(response, PublishPostResult.class);
                        if (publishPostResult != null
                                && publishPostResult.getRet().equals("1")) {
                            Toasty.warning(ReleaseActivity.this, "发布成功！", Toast.LENGTH_SHORT, false).show();
                            ReleaseActivity.this.finish();
                        } else {
                            Toasty.warning(ReleaseActivity.this, "发布失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void releaseTitleBack() {
        back();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        ReleaseActivity.this.finish();
    }

    /**
     * 获取用户物品列表
     */
    private void getUserCdsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(ReleaseActivity.this));
        String params = EncryptionUtil.getParameter(ReleaseActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserCds.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_article_getUserCds")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ReleaseActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ReleaseActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        UserCdsResult userCdsResult =
                                (UserCdsResult) GsonUtil.json2Object(response, UserCdsResult.class);
                        if (userCdsResult != null
                                && userCdsResult.getRet().equals("1")) {
                            if (userCdsResult.getResult() != null
                                    && userCdsResult.getResult().size() > 0) {
                                allPers.clear();
                                allPers.addAll(userCdsResult.getResult());
                                showDialog();
                            }
                        } else {

                        }
                    }
                });
    }
}
