package com.tch.kuwanx.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.IndexBean;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AddFriendNoteResult;
import com.tch.kuwanx.result.DeleteFriendsResult;
import com.tch.kuwanx.result.FriendsListResult;
import com.tch.kuwanx.result.NewFriendsResult;
import com.tch.kuwanx.ui.friend.FriendProfileActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.tch.kuwanx.view.IndexBar;
import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.listener.SwipeSwitchListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Field;
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
 * Created by Jiaop on 2017/10/24.
 * 通讯录（子页）
 */
public class AddressBookFragment extends Fragment implements OnRefreshLoadmoreListener {

    @BindView(R.id.rvContacts)
    RecyclerView rvContacts;
    @BindView(R.id.indexBar)
    IndexBar indexBar;
    @BindView(R.id.tvSideBarHint)
    TextView tvSideBarHint;
    @BindView(R.id.llMsgAddressBook)
    LinearLayout llMsgAddressBook;
    @BindView(R.id.refreshMsgAddressBook)
    SmartRefreshLayout refreshMsgAddressBook;

    private View viewRoot;
    private CommonAdapter contactsAdapter;
    private LinearLayoutManager contactLayoutManager;
    private Intent intent;
    private boolean isMore = false;

    public static AddressBookFragment getInstance() {
        AddressBookFragment sf = new AddressBookFragment();
        return sf;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_msg_address_book, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        initContacts();
        refreshMsgAddressBook.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFriendsListHttp();
        newFriendsHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        getFriendsListHttp();
        newFriendsHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        getFriendsListHttp();
        newFriendsHttp();
    }

    private List<SwipeHorizontalMenuLayout> smls = new ArrayList<>();

    private void initContacts() {
        rvContacts.setLayoutManager(contactLayoutManager = new LinearLayoutManager(getActivity()));
        rvContacts.setAdapter(contactsAdapter = new CommonAdapter<IndexBean>(getActivity(),
                R.layout.item_contacts_parent, new ArrayList<IndexBean>()) {
            @Override
            protected void convert(final ViewHolder holder, final IndexBean item, final int position) {
                smls.add(((SwipeHorizontalMenuLayout) holder.getView(R.id.smlContacts)));
                holder.setText(R.id.tvContactName, item.getName());
                final FriendsListResult.ResultBean bean = (FriendsListResult.ResultBean) item.getItem();
                if (!TextUtils.isEmpty(bean.getHeadpic())) {
                    Glide.with(getActivity())
                            .load(bean.getHeadpic())
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into((ImageView) holder.getView(R.id.ivContactFriendPhoto));
                } else {
                    holder.setImageResource(R.id.ivContactFriendPhoto, R.mipmap.app_icon);
                }
                //备注
                holder.setOnClickListener(R.id.tvContactRemark, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((SwipeHorizontalMenuLayout) holder.getView(R.id.smlContacts)).smoothCloseMenu();
                        showUpdateNikeName(bean.getFriends_app_user_id());
                    }
                });
                //删除
                holder.setOnClickListener(R.id.tvContactDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((SwipeHorizontalMenuLayout) holder.getView(R.id.smlContacts)).smoothCloseMenu();
                        deleteFriendsHttp(bean.getFriends_app_user_id());
                    }
                });

                ((SwipeHorizontalMenuLayout) holder.getView(R.id.smlContacts)).setSwipeListener(new SwipeSwitchListener() {
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
                        ((SwipeHorizontalMenuLayout) holder.getView(R.id.smlContacts)).smoothOpenMenu(position);
                    }
                });
            }
        });
        contactsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
                intent.putExtra("friendId",
                        ((FriendsListResult.ResultBean) ((IndexBean) contactsAdapter.getDatas().get(position)).getItem()).getFriends_app_user_id());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private CustomPopWindow groupMineNamePop;
    private EditText etMineName;
    private Button btMineNamePopCancel, btMineNamePopDefine;

    /**
     * 弹出修改备注
     */
    private void showUpdateNikeName(final String id) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_update_nike_name, null);
        etMineName = (EditText) view.findViewById(R.id.etMineName);
        btMineNamePopCancel = (Button) view.findViewById(R.id.btMineNamePopCancel);
        btMineNamePopDefine = (Button) view.findViewById(R.id.btMineNamePopDefine);
        btMineNamePopCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                if (groupMineNamePop != null) {
                    groupMineNamePop.dissmiss();
                }
            }
        });
        btMineNamePopDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认
                if (groupMineNamePop != null) {
                    groupMineNamePop.dissmiss();
                }
                if (!TextUtils.isEmpty(etMineName.getText().toString())) {
                    addFriendNoteHttp(id, etMineName.getText().toString());
                } else {
                    Toasty.warning(getActivity(), "备注不能为空！", Toast.LENGTH_SHORT, false).show();
                }
            }
        });
        groupMineNamePop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(view)
                .size(Utils.getScreenWidth(getActivity()) - 40, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFocusable(true)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.6f)
                .create()
                .showAtLocation(llMsgAddressBook, Gravity.CENTER, 0, 0);
    }

    /**
     * 新朋友
     */
    @OnClick(R.id.llNewFriends)
    public void newFriends() {
        intent = new Intent(getActivity(), NewFriendsActivity.class);
        startActivity(intent);
    }

    /**
     * 关注
     */
    @OnClick(R.id.llAttention)
    public void attention() {
        intent = new Intent(getActivity(), AttentionActivity.class);
        intent.putExtra("att", 0);
        startActivity(intent);
    }

    /**
     * 群聊
     */
    @OnClick(R.id.llGroupChat)
    public void groupChat() {
        intent = new Intent(getActivity(), GroupChatListActivity.class);
        intent.putExtra("activity", "AddressBook");
        startActivity(intent);
    }

    /**
     * 获取好友列表
     */
    private void getFriendsListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(getActivity()));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/getFriendsList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getFriendsList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        getActivity(), "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshMsgAddressBook != null) {
                            refreshMsgAddressBook.finishLoadmore();
                            refreshMsgAddressBook.finishRefresh();
                        }
                        Toasty.warning(getActivity(), "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshMsgAddressBook != null) {
                            refreshMsgAddressBook.finishLoadmore();
                            refreshMsgAddressBook.finishRefresh();
                        }

                        FriendsListResult friendsListResult =
                                (FriendsListResult) GsonUtil.json2Object(response, FriendsListResult.class);
                        if (friendsListResult != null
                                && friendsListResult.getRet().equals("1")) {
                            if (isMore) {

                            } else {
                                contactsAdapter.getDatas().clear();
                                for (FriendsListResult.ResultBean item : friendsListResult.getResult()) {
                                    ((List<IndexBean>) contactsAdapter.getDatas()).add(new IndexBean(item.getNickname(), item)
                                    );
                                }
                            }

                            //设置索引栏
                            indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                                    .setNeedRealIndex(true)//设置需要真实的索引
                                    .setmLayoutManager(contactLayoutManager)//设置RecyclerView的LayoutManager
                                    .setmSourceDatas(((List<IndexBean>) contactsAdapter.getDatas()));//设置数据源

                            contactsAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    /**
     * 新朋友列表
     */
    private void newFriendsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(getActivity()));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/newFriends.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_newFriends_")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (refreshMsgAddressBook != null) {
                            refreshMsgAddressBook.finishLoadmore();
                            refreshMsgAddressBook.finishRefresh();
                        }
                        Logger.e("获取新朋友列表数量失败！");
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshMsgAddressBook != null) {
                            refreshMsgAddressBook.finishLoadmore();
                            refreshMsgAddressBook.finishRefresh();
                        }

                        NewFriendsResult newFriendsResult =
                                (NewFriendsResult) GsonUtil.json2Object(response, NewFriendsResult.class);
                        if (newFriendsResult != null
                                && newFriendsResult.getRet().equals("1")) {
                            if (newFriendsResult.getResult().size() > 0) {
                                List<NewFriendsResult.ResultBean> list = new ArrayList<>();
                                list.clear();
                                for (NewFriendsResult.ResultBean item : newFriendsResult.getResult()) {
                                    if (item.getApply_status().equals("0")) {
                                        list.add(item);
                                    }
                                }
                                if (list.size() > 0) {
                                    tvMsgAddressBookNewFs.setVisibility(View.VISIBLE);
                                    if (list.size() > 99) {
                                        tvMsgAddressBookNewFs.setText("99+");
//                                        ((HomeActivity) getActivity()).updateMsgCount(3, 99);
                                    } else {
                                        tvMsgAddressBookNewFs.setText(String.valueOf(list.size()));
//                                        ((HomeActivity) getActivity()).updateMsgCount(3, list.size());
                                    }
                                } else {
                                    tvMsgAddressBookNewFs.setVisibility(View.GONE);
                                }
                            } else {
                                tvMsgAddressBookNewFs.setVisibility(View.GONE);
                            }
                        } else {
                            tvMsgAddressBookNewFs.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @BindView(R.id.tvMsgAddressBookNewFs)
    TextView tvMsgAddressBookNewFs;

    /**
     * 删除通信录好友
     */
    private void deleteFriendsHttp(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(getActivity()));
        map.put("friends_app_user_id", id);
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/deleteFriends.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_deleteFriends")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        getActivity(), "删除中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(getActivity(), "删除失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        DeleteFriendsResult deleteFriendsResult =
                                (DeleteFriendsResult) GsonUtil.json2Object(response, DeleteFriendsResult.class);
                        if (deleteFriendsResult != null
                                && deleteFriendsResult.getRet().equals("1")) {
                            Toasty.warning(getActivity(), "删除成功！", Toast.LENGTH_SHORT, false).show();
                            getFriendsListHttp();
                        }
                    }
                });
    }

    /**
     * 添加好友备注
     */
    private void addFriendNoteHttp(String id, String text) {
        Map<String, Object> map = new HashMap<>();
        map.put("app_user_id", DaoUtils.getUserId(getActivity()));
        map.put("friends_app_user_id", id);
        map.put("friend_note", text);
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "msg/addFriendNote.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addFriendNote")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        getActivity(), "修改备注中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(getActivity(), "修改备注失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddFriendNoteResult addFriendNoteResult =
                                (AddFriendNoteResult) GsonUtil.json2Object(response, AddFriendNoteResult.class);
                        if (addFriendNoteResult != null
                                && addFriendNoteResult.getRet().equals("1")) {
                            Toasty.warning(getActivity(), "修改备注成功！", Toast.LENGTH_SHORT, false).show();
                            getFriendsListHttp();
                        }
                    }
                });
    }

}
