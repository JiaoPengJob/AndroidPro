package com.tch.zx.activity.message;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.contacts.CommentDynamicAdapter;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.SearchFriendResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendsActivity<T> extends BaseActivity {

    /**
     * 输入框
     */
    @BindView(R.id.et_add_friend_phone)
    EditText et_add_friend_phone;
    /**
     * 清除的×
     */
    @BindView(R.id.iv_clear_input)
    ImageView iv_clear_input;
    /**
     * 用户不存在
     */
    @BindView(R.id.tv_none_user)
    TextView tv_none_user;
    /**
     * 搜索
     */
    @BindView(R.id.tv_search_friend)
    TextView tv_search_friend;
    @BindView(R.id.rvSearchFriend)
    RecyclerView rvSearchFriend;

    private BasePresenter<Object> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add_friends);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initViewListener();
    }

    /**
     * 设置组件监听事件
     */
    private void initViewListener() {
        //输入框内容变化监听
        et_add_friend_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    iv_clear_input.setVisibility(View.VISIBLE);
                    tv_none_user.setVisibility(View.GONE);
                } else {
                    iv_clear_input.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 搜索
     */
    @OnClick(R.id.tv_search_friend)
    public void searchData() {
        if (!TextUtils.isEmpty(et_add_friend_phone.getText().toString()) && HelperUtil.isMobileNO(et_add_friend_phone.getText().toString())) {
            queryFriendByPhone();
            //数据不为空
//            if (true) {
//                ll_user_info.setVisibility(View.VISIBLE);
//                tv_none_user.setVisibility(View.GONE);
//                ll_user_info.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(AddFriendsActivity.this, FriendInfoActivity.class);
//                        startActivity(intent);
//                    }
//                });
//            } else {
//                ll_user_info.setVisibility(View.GONE);
//                tv_none_user.setVisibility(View.VISIBLE);
//            }
        } else {
            Toast.makeText(AddFriendsActivity.this, "手机号有误!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 搜索好友
     */
    private void queryFriendByPhone() {
        presenter = new BasePresenter<Object>(AddFriendsActivity.this);
        presenter.onCreate();
        presenter.attachView(queryFriendByPhoneView);

        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("userName", et_add_friend_phone.getText().toString());
        map.put("userName", "17319311613");

        String data = HelperUtil.getParameter(map);
        presenter.queryFriendByPhone(data);
    }

    private BaseView<Object> queryFriendByPhoneView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                SearchFriendResult searchFriendResult = GsonUtil.parseJson(baseResultBean.getResult(), SearchFriendResult.class);
                setSearchResult(searchFriendResult.getResponseObject());
            }

        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "queryFriendByPhoneView接口错误" + result);
        }
    };

    private CircleImageView civSearchItemPhoto;
    private CommonAdapter searchFriendAdapter;

    /**
     * 加载列表
     */
    private void setSearchResult(final List<SearchFriendResult.ResponseObjectBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddFriendsActivity.this);
        rvSearchFriend.setLayoutManager(layoutManager);
        //设置分割线
        rvSearchFriend.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 5, true));
        rvSearchFriend.setAdapter(searchFriendAdapter = new CommonAdapter<SearchFriendResult.ResponseObjectBean>(AddFriendsActivity.this, R.layout.item_search_friend, list) {
            @Override
            public void convert(ViewHolder holder, SearchFriendResult.ResponseObjectBean item, int position) {
                civSearchItemPhoto = (CircleImageView) holder.getView(R.id.civSearchItemPhoto);
                Glide.with(AddFriendsActivity.this)
                        .load(item.getUser_picture())
                        .into(civSearchItemPhoto);
                holder.setText(R.id.tvSearchItemName, item.getName());
                holder.setText(R.id.tvSearchItemInfo, item.getCompany_name() + "   " + item.getCompany_position());
            }
        });
        searchFriendAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(AddFriendsActivity.this, FriendInfoActivity.class);
                intent.putExtra("userId", list.get(position).getApp_user_id());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 清除输入框
     */
    @OnClick(R.id.iv_clear_input)
    public void clearInput() {
        et_add_friend_phone.setText("");
        iv_clear_input.setVisibility(View.GONE);
    }

    /**
     * 后退
     */
    @OnClick(R.id.ll_together_msg_title_return)
    public void returnBack() {
        AddFriendsActivity.this.finish();
    }
}
