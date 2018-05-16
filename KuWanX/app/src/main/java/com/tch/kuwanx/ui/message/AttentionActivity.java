package com.tch.kuwanx.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AddInterestResult;
import com.tch.kuwanx.result.GetInterestListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.listener.SwipeSwitchListener;
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
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 关注
 */
public class AttentionActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.rlAttentionTitle)
    RelativeLayout rlAttentionTitle;
    @BindView(R.id.viewAttentionDark)
    View viewAttentionDark;
    @BindView(R.id.rvAttention)
    RecyclerView rvAttention;
    @BindView(R.id.ibAttentionAddMenu)
    ImageButton ibAttentionAddMenu;
    @BindView(R.id.refreshAttention)
    SmartRefreshLayout refreshAttention;

    private Intent intent;
    private CommonAdapter attentionAdapter;
    private int att;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        att = getIntent().getIntExtra("att", 0);
        if (att == 0) {
            ibAttentionAddMenu.setVisibility(View.VISIBLE);
        } else if (att == 1) {
            ibAttentionAddMenu.setVisibility(View.GONE);
        }
        initAttentionData();
        refreshAttention.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInterestListHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        getInterestListHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        getInterestListHttp();
    }

    private List<SwipeHorizontalMenuLayout> smls = new ArrayList<>();

    private void initAttentionData() {
        smls.clear();
        rvAttention.setLayoutManager(new LinearLayoutManager(this));
        rvAttention.setAdapter(attentionAdapter = new CommonAdapter<GetInterestListResult.ResultBean>(this,
                R.layout.item_attention_parent, new ArrayList<GetInterestListResult.ResultBean>()) {
            @Override
            protected void convert(final ViewHolder holder, final GetInterestListResult.ResultBean item, final int position) {
                smls.add(((SwipeHorizontalMenuLayout) holder.getView(R.id.swipeAttention)));
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(AttentionActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivContactFriendPhoto));
                } else {
                    holder.setImageResource(R.id.ivContactFriendPhoto, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvContactName, item.getNickname());
                holder.setOnClickListener(R.id.tvUnSubscribe, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //取消关注
                        ((SwipeHorizontalMenuLayout) holder.getView(R.id.swipeAttention)).smoothCloseMenu();
                        attentionAdapter.notifyDataSetChanged();
                        cancleInterestHttp(item.getId());
                    }
                });

                ((SwipeHorizontalMenuLayout) holder.getView(R.id.swipeAttention)).setSwipeListener(new SwipeSwitchListener() {
                    @Override
                    public void beginMenuClosed(SwipeMenuLayout swipeMenuLayout) {

                    }

                    @Override
                    public void beginMenuOpened(SwipeMenuLayout swipeMenuLayout) {

                    }

                    @Override
                    public void endMenuClosed(SwipeMenuLayout swipeMenuLayout) {

                    }

                    @Override
                    public void endMenuOpened(SwipeMenuLayout swipeMenuLayout) {
                        for (SwipeHorizontalMenuLayout sml : smls) {
                            if (sml.isMenuOpen()) {
                                sml.smoothCloseMenu();
                            }
                        }
                        ((SwipeHorizontalMenuLayout) holder.getView(R.id.swipeAttention)).smoothOpenMenu(position);
                    }
                });
            }
        });
        attentionAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
     * 菜单按钮
     */
    @OnClick(R.id.ibAttentionAddMenu)
    public void attentionAddMenu() {
        showMsgAddMenu();
    }

    private CustomPopWindow attentionMenuPop;
    private LinearLayout llSponsorAttentionChat, llAddFriends, llInviteFriends;

    private void showMsgAddMenu() {
        View view = LayoutInflater.from(AttentionActivity.this).inflate(R.layout.pop_msg_menu, null);
        llSponsorAttentionChat = (LinearLayout) view.findViewById(R.id.llSponsorGroupChat);
        llAddFriends = (LinearLayout) view.findViewById(R.id.llAddFriends);
        llInviteFriends = (LinearLayout) view.findViewById(R.id.llInviteFriends);
        llSponsorAttentionChat.setOnClickListener(new AttentionActivity.MenuClickListener());
        llAddFriends.setOnClickListener(new AttentionActivity.MenuClickListener());
        llInviteFriends.setOnClickListener(new AttentionActivity.MenuClickListener());
        attentionMenuPop = new CustomPopWindow.PopupWindowBuilder(AttentionActivity.this)
                .setView(view)
                .size(280, 280)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewAttentionDark.setVisibility(View.GONE);
                    }
                })
                .create()
                .showAsDropDown(rlAttentionTitle, 0, 2, Gravity.RIGHT);
        viewAttentionDark.setVisibility(View.VISIBLE);
    }

    private class MenuClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (attentionMenuPop != null) {
                attentionMenuPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llSponsorGroupChat:
                    intent = new Intent(AttentionActivity.this, ChooseContactActivity.class);
                    startActivity(intent);
                    break;
                case R.id.llAddFriends:
                    intent = new Intent(AttentionActivity.this, AddNewFriendActivity.class);
                    startActivity(intent);
                    break;
                case R.id.llInviteFriends:

                    break;
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibAttentionBack)
    public void attentionBack() {
        AttentionActivity.this.finish();
    }

    /**
     * 获取关注列表
     */
    private void getInterestListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", DaoUtils.getUserId(AttentionActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(AttentionActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/getInterestList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getInterestList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        AttentionActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshAttention != null) {
                            refreshAttention.finishLoadmore();
                            refreshAttention.finishRefresh();
                        }
                        Toasty.warning(AttentionActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshAttention != null) {
                            refreshAttention.finishLoadmore();
                            refreshAttention.finishRefresh();
                        }

                        GetInterestListResult getInterestListResult =
                                (GetInterestListResult) GsonUtil.json2Object(response, GetInterestListResult.class);
                        if (getInterestListResult != null
                                && getInterestListResult.getRet().equals("1")) {
                            if (isMore) {
                                attentionAdapter.getDatas().addAll(getInterestListResult.getResult());
                                attentionAdapter.notifyDataSetChanged();
                            } else {
                                attentionAdapter.getDatas().clear();
                                attentionAdapter.getDatas().addAll(getInterestListResult.getResult());
                                attentionAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    /**
     * 取消关注
     */
    private void cancleInterestHttp(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", DaoUtils.getUserId(AttentionActivity.this));
        map.put("interest_user_id", id);
        String params = EncryptionUtil.getParameter(AttentionActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/cancleInterest.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_cancleInterest")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        AttentionActivity.this, "取消关注中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(AttentionActivity.this, "取消关注失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddInterestResult addInterestResult =
                                (AddInterestResult) GsonUtil.json2Object(response, AddInterestResult.class);
                        if (addInterestResult != null
                                && addInterestResult.getRet().equals("1")) {
                            getInterestListHttp();
                        } else {
                            Toasty.warning(AttentionActivity.this, "取消关注失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

}
