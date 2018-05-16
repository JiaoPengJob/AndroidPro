package com.tch.kuwanx.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.SearchFriendsResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.ui.friend.FriendProfileActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
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
 * 搜索好友，添加好友
 */
public class AddNewFriendActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.ibTitleFeatures)
    ImageButton ibTitleFeatures;
    @BindView(R.id.etAddNewFriend)
    EditText etAddNewFriend;
    @BindView(R.id.rvAddNewFriends)
    RecyclerView rvAddNewFriends;
    @BindView(R.id.refreshAddNewFriend)
    SmartRefreshLayout refreshAddNewFriend;

    private CommonAdapter searchFriendAdapter;
    private boolean isMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("添加好友");
        ibTitleFeatures.setImageResource(R.drawable.store_search);
        ibTitleFeatures.setVisibility(View.VISIBLE);
        initSearchFriends();
        refreshAddNewFriend.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        if (!TextUtils.isEmpty(etAddNewFriend.getText().toString())) {
            searchFriendHttp();
        } else {
            if (refreshAddNewFriend != null) {
                refreshAddNewFriend.finishLoadmore();
            }
            Toasty.warning(AddNewFriendActivity.this, "请输入手机号！", Toast.LENGTH_SHORT, false).show();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        if (!TextUtils.isEmpty(etAddNewFriend.getText().toString())) {
            searchFriendHttp();
        } else {
            if (refreshAddNewFriend != null) {
                refreshAddNewFriend.finishRefresh();
            }
            Toasty.warning(AddNewFriendActivity.this, "请输入手机号！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 加载搜索的好友列表
     */
    private void initSearchFriends() {
        rvAddNewFriends.setLayoutManager(new LinearLayoutManager(AddNewFriendActivity.this));
        rvAddNewFriends.setAdapter(searchFriendAdapter = new CommonAdapter<SearchFriendsResult.ResultBean>(AddNewFriendActivity.this,
                R.layout.item_search_friends, new ArrayList<SearchFriendsResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, SearchFriendsResult.ResultBean item, int position) {
                if (!TextUtils.isEmpty(item.getHeadpic())) {
                    Glide.with(AddNewFriendActivity.this)
                            .load(item.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivSearchFriendsImg));
                } else {
                    holder.setImageResource(R.id.ivSearchFriendsImg, R.mipmap.app_icon);
                }

                holder.setText(R.id.tvSearchFriendsName, item.getNickname());
            }
        });
        searchFriendAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(AddNewFriendActivity.this, FriendProfileActivity.class);
                intent.putExtra("friendId",
                        ((SearchFriendsResult.ResultBean) searchFriendAdapter.getDatas().get(position)).getUserId());
                intent.putExtra("activity", "AddNewFriend");
                intent.putExtra("addType", "1");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 搜索好友
     */
    @OnClick(R.id.ibTitleFeatures)
    public void addNewFriendTitleFeatures() {
        if (!TextUtils.isEmpty(etAddNewFriend.getText().toString())) {
            searchFriendHttp();
        }
    }

    /**
     * 搜索好友
     */
    private void searchFriendHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(AddNewFriendActivity.this));
        map.put("phone", etAddNewFriend.getText().toString());
        String params = EncryptionUtil.getParameter(AddNewFriendActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/searchFriend.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        AddNewFriendActivity.this, "搜索中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshAddNewFriend != null) {
                            refreshAddNewFriend.finishLoadmore();
                            refreshAddNewFriend.finishRefresh();
                        }
                        Toasty.warning(AddNewFriendActivity.this, "搜索失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        SearchFriendsResult searchFriendsResult =
                                (SearchFriendsResult) GsonUtil.json2Object(response, SearchFriendsResult.class);
                        if (searchFriendsResult != null
                                && searchFriendsResult.getRet().equals("1")) {
                            if (isMore) {

                            } else {
                                searchFriendAdapter.getDatas().clear();
                                searchFriendAdapter.getDatas().addAll(searchFriendsResult.getResult());
                                searchFriendAdapter.notifyDataSetChanged();
                            }
                        }
                        if (refreshAddNewFriend != null) {
                            refreshAddNewFriend.finishLoadmore();
                            refreshAddNewFriend.finishRefresh();
                        }
                    }
                });
    }


    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void addNewFriendTitleBack() {
        AddNewFriendActivity.this.finish();
    }

}
