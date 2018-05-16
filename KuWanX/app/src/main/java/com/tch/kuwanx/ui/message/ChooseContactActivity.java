package com.tch.kuwanx.ui.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.ContactBean;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AddGroupMemberResult;
import com.tch.kuwanx.result.FriendsListResult;
import com.tch.kuwanx.ui.BaseActivity;
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
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 发起群聊
 */
public class ChooseContactActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvContactList)
    RecyclerView rvContactList;
    @BindView(R.id.refreshChooseContact)
    SmartRefreshLayout refreshChooseContact;

    private CommonAdapter contactAdapter;
    private List<String> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private String activity = "", groupId, groupName;
    private boolean isMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contact);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            groupId = getIntent().getStringExtra("groupId");
            groupName = getIntent().getStringExtra("groupName");
            if (activity.equals("GroupManager")) {
                btTitleFeatures.setText("邀请");
            } else {
                btTitleFeatures.setText("创建");
            }
        } else {
            btTitleFeatures.setText("创建");
        }
        tvTitleContent.setText("选择联系人");
        btTitleFeatures.setVisibility(View.VISIBLE);
        initChooseContactList();
        refreshChooseContact.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFriendsListHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        getFriendsListHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        getFriendsListHttp();
    }

    /**
     * ContactBean,包含是否选中标识
     */
    private void initChooseContactList() {
        rvContactList.setLayoutManager(new LinearLayoutManager(this));
        rvContactList.setAdapter(contactAdapter = new CommonAdapter<ContactBean>(this,
                R.layout.item_choose_contact, new ArrayList<ContactBean>()) {
            @Override
            protected void convert(ViewHolder holder, final ContactBean item, final int position) {
                FriendsListResult.ResultBean bean = (FriendsListResult.ResultBean) item.getBean();
                if (!TextUtils.isEmpty(bean.getHeadpic())) {
                    Glide.with(ChooseContactActivity.this)
                            .load(bean.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivChooseContactAvatar));
                } else {
                    holder.setImageResource(R.id.ivChooseContactAvatar, R.mipmap.app_icon);
                }
                holder.setText(R.id.ivChooseContactName, bean.getNickname());

                if (item.isSelect()) {
                    holder.setImageResource(R.id.ibChooseContactSel, R.drawable.select);
                } else {
                    holder.setImageResource(R.id.ibChooseContactSel, R.drawable.unselect);
                }
                holder.setOnClickListener(R.id.ibChooseContactSel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.isSelect()) {
                            ((List<ContactBean>) contactAdapter.getDatas()).get(position).setSelect(false);
                            contactAdapter.notifyDataSetChanged();
                        } else {
                            ((List<ContactBean>) contactAdapter.getDatas()).get(position).setSelect(true);
                            contactAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        contactAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (((List<ContactBean>) contactAdapter.getDatas()).get(position).isSelect()) {
                    ((List<ContactBean>) contactAdapter.getDatas()).get(position).setSelect(false);
                    contactAdapter.notifyDataSetChanged();
                } else {
                    ((List<ContactBean>) contactAdapter.getDatas()).get(position).setSelect(true);
                    contactAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 创建
     */
    @OnClick(R.id.btTitleFeatures)
    public void buildGroupChat() {
        if (activity.equals("GroupManager")) {
            ids.clear();
            names.clear();
            for (ContactBean cb : (List<ContactBean>) contactAdapter.getDatas()) {
                if (cb.isSelect()) {
                    ids.add(((FriendsListResult.ResultBean) cb.getBean()).getFriends_app_user_id());
                    names.add(((FriendsListResult.ResultBean) cb.getBean()).getNickname());
                }
            }
            addGroupMemberHttp(Utils.join(ids, ","), Utils.join(names, ","));
        } else {
            showAffirm();
        }
    }

    private CustomPopWindow affirmPop;
    @BindView(R.id.viewChooseDark)
    View viewChooseDark;
    @BindView(R.id.rlChooseContact)
    RelativeLayout rlChooseContact;
    private Button btChoosePopAffirm, btChoosePopCancel;

    /**
     * 确认是否创建
     */
    private void showAffirm() {
        View view = LayoutInflater.from(ChooseContactActivity.this).
                inflate(R.layout.pop_choose, null);
        btChoosePopAffirm = (Button) view.findViewById(R.id.btChoosePopAffirm);
        btChoosePopCancel = (Button) view.findViewById(R.id.btChoosePopCancel);

        btChoosePopAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定
                if (affirmPop != null) {
                    affirmPop.dissmiss();
                }
                ids.clear();
                names.clear();
                ids.add(DaoUtils.getUserId(ChooseContactActivity.this));
                names.add(DaoUtils.getUser(ChooseContactActivity.this).getNickname());
                for (ContactBean cb : (List<ContactBean>) contactAdapter.getDatas()) {
                    if (cb.isSelect()) {
                        ids.add(((FriendsListResult.ResultBean) cb.getBean()).getFriends_app_user_id());
                        names.add(((FriendsListResult.ResultBean) cb.getBean()).getNickname());
                    }
                }
                createGroupHttp(Utils.join(ids, ","), Utils.join(names, ","));
            }
        });

        btChoosePopCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                if (affirmPop != null) {
                    affirmPop.dissmiss();
                }
            }
        });

        affirmPop = new CustomPopWindow.PopupWindowBuilder(ChooseContactActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewChooseDark.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(rlChooseContact, Gravity.CENTER, 0, -20);
        viewChooseDark.setVisibility(View.VISIBLE);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void chooseContactTitleBack() {
        ChooseContactActivity.this.finish();
    }

    /**
     * 获取好友列表
     */
    private void getFriendsListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(ChooseContactActivity.this));
        String params = EncryptionUtil.getParameter(ChooseContactActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/getFriendsList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getFriendsList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ChooseContactActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshChooseContact != null) {
                            refreshChooseContact.finishLoadmore();
                            refreshChooseContact.finishRefresh();
                        }
                        Toasty.warning(ChooseContactActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshChooseContact != null) {
                            refreshChooseContact.finishLoadmore();
                            refreshChooseContact.finishRefresh();
                        }

                        FriendsListResult friendsListResult =
                                (FriendsListResult) GsonUtil.json2Object(response, FriendsListResult.class);
                        if (friendsListResult != null
                                && friendsListResult.getRet().equals("1")) {
                            if (isMore) {

                            } else {
                                contactAdapter.getDatas().clear();
                                for (FriendsListResult.ResultBean item : friendsListResult.getResult()) {
                                    ((List<ContactBean>) contactAdapter.getDatas()).add(
                                            new ContactBean(false, item)
                                    );
                                }
                                contactAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    /**
     * 创建群
     */
    private void createGroupHttp(String ids, String names) {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(ChooseContactActivity.this));
        map.put("group_nickname", "群聊");
        map.put("group_member", ids);
        map.put("member_nickname", names);
        String params = EncryptionUtil.getParameter(ChooseContactActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/createGroup.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_createGroup")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ChooseContactActivity.this, "创建中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ChooseContactActivity.this, "创建失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {

                    }
                });
    }

    /**
     * 添加群组成员
     */
    private void addGroupMemberHttp(String ids, String names) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("group_nickname", groupName);
        map.put("member_app_user_id", ids);
        map.put("member_nickname", names);
        String params = EncryptionUtil.getParameter(ChooseContactActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/addGroupMember.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName())
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ChooseContactActivity.this, "添加中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ChooseContactActivity.this, "添加失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddGroupMemberResult addGroupMemberResult =
                                (AddGroupMemberResult) GsonUtil.json2Object(response, AddGroupMemberResult.class);
                        if (addGroupMemberResult != null
                                && addGroupMemberResult.getRet().equals("1")) {
                            Toasty.warning(ChooseContactActivity.this, "添加成功！", Toast.LENGTH_SHORT, false).show();
                            ChooseContactActivity.this.finish();
                        }
                    }
                });
    }

}
