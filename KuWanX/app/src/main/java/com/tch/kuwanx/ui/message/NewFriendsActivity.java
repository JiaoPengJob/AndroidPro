package com.tch.kuwanx.ui.message;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AcceptFriendRequestResult;
import com.tch.kuwanx.result.ClearNewFriendsResult;
import com.tch.kuwanx.result.NewFriendsResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.friend.FriendProfileActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 新朋友
 */
public class NewFriendsActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvNewFriends)
    RecyclerView rvNewFriends;
    @BindView(R.id.refreshNewFriends)
    SmartRefreshLayout refreshNewFriends;

    private CommonAdapter newFriendsAdapter;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("新朋友");
        btTitleFeatures.setText("清除");
        btTitleFeatures.setVisibility(View.VISIBLE);
        initNewFriendsData();
        refreshNewFriends.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        newFriendsHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        newFriendsHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        newFriendsHttp();
    }

    private void initNewFriendsData() {
        rvNewFriends.setLayoutManager(new LinearLayoutManager(this));
        rvNewFriends.setAdapter(newFriendsAdapter = new CommonAdapter<NewFriendsResult.ResultBean>(this,
                R.layout.item_new_friends, new ArrayList<NewFriendsResult.ResultBean>()) {
            @Override
            protected void convert(final ViewHolder holder, final NewFriendsResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(NewFriendsActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivNewFriendItemAvatar));
                } else {
                    holder.setImageResource(R.id.ivNewFriendItemAvatar, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvNewFriendItemName, item.getNickname());
                holder.setText(R.id.tvNewFriendItemContent, item.getVerification_message());

                if (item.getApply_status().equals("0")) {
                    //未添加
                    holder.setText(R.id.btNewFriendsAccept, "接受");
                    holder.setBackgroundRes(R.id.btNewFriendsAccept, R.drawable.oval_filter_item_sel);
                } else {
                    //已添加
                    holder.setText(R.id.btNewFriendsAccept, "已接受");
                    holder.setBackgroundColor(R.id.btNewFriendsAccept, Color.parseColor("#00000000"));
                }

                holder.setOnClickListener(R.id.btNewFriendsAccept, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getApply_status().equals("0")) {
                            //未添加
                            acceptFriendRequestHttp(item.getApp_user_id());
                        }
                    }
                });
            }
        });
        newFriendsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(NewFriendsActivity.this, FriendProfileActivity.class);
                intent.putExtra("friendId", ((NewFriendsResult.ResultBean) newFriendsAdapter.getDatas().get(position)).getApp_user_id());
                intent.putExtra("addType", ((NewFriendsResult.ResultBean) newFriendsAdapter.getDatas().get(position)).getApply_status());
                intent.putExtra("activity", "AddNewFriend");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 清除
     */
    @OnClick(R.id.btTitleFeatures)
    public void clearNewFriends() {
        showDialog();
    }

    private Dialog dialog;
    private Button btNewFriendsDefine, btNewFriendsCancel;

    private void showDialog() {
        dialog = new Dialog(this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_clear_new_friends, null);
        btNewFriendsDefine = (Button) layout.findViewById(R.id.btNewFriendsDefine);
        btNewFriendsCancel = (Button) layout.findViewById(R.id.btNewFriendsCancel);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (Utils.getScreenWidth(this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        btNewFriendsDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定
//                newFriendsAdapter.getDatas().clear();
//                newFriendsAdapter.notifyDataSetChanged();
                if (dialog != null) {
                    dialog.cancel();
                }
                clearNewFriendsHttp();
            }
        });
        btNewFriendsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                if (dialog != null) {
                    dialog.cancel();
                }
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void NewFriendTitleBack() {
        NewFriendsActivity.this.finish();
    }

    /**
     * 新朋友列表
     */
    private void newFriendsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(NewFriendsActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(NewFriendsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/newFriends.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_newFriends")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (refreshNewFriends != null) {
                            refreshNewFriends.finishLoadmore();
                            refreshNewFriends.finishRefresh();
                        }
                        Toasty.warning(NewFriendsActivity.this, "获取新朋友失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshNewFriends != null) {
                            refreshNewFriends.finishLoadmore();
                            refreshNewFriends.finishRefresh();
                        }

                        NewFriendsResult newFriendsResult =
                                (NewFriendsResult) GsonUtil.json2Object(response, NewFriendsResult.class);
                        if (newFriendsResult != null
                                && newFriendsResult.getRet().equals("1")) {
                            if (isMore) {
                                newFriendsAdapter.getDatas().addAll(newFriendsResult.getResult());
                                newFriendsAdapter.notifyDataSetChanged();
                            } else {
                                newFriendsAdapter.getDatas().clear();
                                newFriendsAdapter.getDatas().addAll(newFriendsResult.getResult());
                                newFriendsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(NewFriendsActivity.this, "获取新朋友失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 接收好友请求
     *
     * @param id
     */
    private void acceptFriendRequestHttp(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(NewFriendsActivity.this));
        map.put("friends_app_user_id", id);
        String params = EncryptionUtil.getParameter(NewFriendsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/acceptFriendRequest.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_acceptFriendRequest")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        NewFriendsActivity.this, "添加中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(NewFriendsActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AcceptFriendRequestResult acceptFriendRequestResult =
                                (AcceptFriendRequestResult) GsonUtil.json2Object(response, AcceptFriendRequestResult.class);
                        if (acceptFriendRequestResult != null
                                && acceptFriendRequestResult.getRet().equals("1")) {
                            newFriendsHttp();
                        } else {
                            Toasty.warning(NewFriendsActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 清空新朋友请求列表
     */
    private void clearNewFriendsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(NewFriendsActivity.this));
        String params = EncryptionUtil.getParameter(NewFriendsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/clearNewFriends.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_clearNewFriends")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        NewFriendsActivity.this, "清空中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(NewFriendsActivity.this, "清空失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        ClearNewFriendsResult clearNewFriendsResult =
                                (ClearNewFriendsResult) GsonUtil.json2Object(response, ClearNewFriendsResult.class);
                        if (clearNewFriendsResult != null
                                && clearNewFriendsResult.getRet().equals("1")) {
                            newFriendsHttp();
                        }
                    }
                });
    }

}
