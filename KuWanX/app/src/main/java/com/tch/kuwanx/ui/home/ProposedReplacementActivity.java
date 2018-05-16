package com.tch.kuwanx.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
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
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.message.KwMessage;
import com.tch.kuwanx.module.SampleExtensionModule;
import com.tch.kuwanx.result.AddSwapDetailResult;
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
import es.dmoral.toasty.Toasty;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 提出置换
 */
public class ProposedReplacementActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvProposedRepRes)
    RecyclerView rvProposedRepRes;
    @BindView(R.id.etProposedRepEdit)
    EditText etProposedRepEdit;

    private CommonAdapter proposedRepResAdapter;
    private String cdsId = "";
    private String postId = "", otherId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_replacement);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("post_id"))) {
            postId = getIntent().getStringExtra("post_id");
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra("otherId"))) {
            otherId = getIntent().getStringExtra("otherId");
        }

        tvTitleContent.setText("提出置换");
        btTitleFeatures.setVisibility(View.VISIBLE);
        btTitleFeatures.setText("发送");
        initReleaseAddArticlePhoto();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 动态加载图片选择
     */
    private void initReleaseAddArticlePhoto() {
        List<String> resList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            resList.add(Utils.getDrawableResPath(ProposedReplacementActivity.this, R.drawable.add_white));
        }
        rvProposedRepRes.setLayoutManager(new GridLayoutManager(this, 3));
        rvProposedRepRes.setAdapter(proposedRepResAdapter = new CommonAdapter<String>(this,
                R.layout.item_release_res, resList) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                if (item.equals(Utils.getDrawableResPath(ProposedReplacementActivity.this, R.drawable.add_white))) {
                    ((ImageView) holder.getView(R.id.ivReleaseResPhoto)).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else {
                    ((ImageView) holder.getView(R.id.ivReleaseResPhoto)).setScaleType(ImageView.ScaleType.FIT_XY);
                }
                Glide.with(ProposedReplacementActivity.this)
                        .load(item)
                        .into((ImageView) holder.getView(R.id.ivReleaseResPhoto));
            }
        });
        proposedRepResAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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

    /**
     * 获取用户物品列表
     */
    private void getUserCdsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", DaoUtils.getUserId(ProposedReplacementActivity.this));
        String params = EncryptionUtil.getParameter(ProposedReplacementActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getUserCds.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_article_getUserCds")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ProposedReplacementActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ProposedReplacementActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
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

    private CustomPopWindow permutationPop;
    @BindView(R.id.llProposedRepParent)
    LinearLayout llProposedRepParent;
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
                .showAtLocation(llProposedRepParent, Gravity.BOTTOM, 0, 0);
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
                    Glide.with(ProposedReplacementActivity.this)
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
                            Intent intent = new Intent(ProposedReplacementActivity.this, ArticleActivity.class);
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
                if (permutationPop != null) {
                    permutationPop.dissmiss();
                }

                cdsId = ((UserCdsResult.ResultBean) perAdapter.getDatas().get(0)).getId();

                proposedRepResAdapter.getDatas().clear();
                proposedRepResAdapter.getDatas().add(((UserCdsResult.ResultBean) perAdapter.getDatas().get(position)).getHeadpic());
                proposedRepResAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    void proposedReplacementActivityBack() {
        ProposedReplacementActivity.this.finish();
    }

    /**
     * 提出置换
     */
    @OnClick(R.id.btTitleFeatures)
    void outChange() {
        if (!TextUtils.isEmpty(cdsId)
                && !TextUtils.isEmpty(etProposedRepEdit.getText().toString())) {
            addSwapDetailHttp();
        } else {
            Toasty.warning(ProposedReplacementActivity.this, "发布的信息不完整！", Toast.LENGTH_SHORT, false).show();
        }

        //如果成功，则跳转到聊天页面
//        initSendMsg();
    }

    /**
     * 发送消息
     */
    private void initSendMsg(AddSwapDetailResult.ResultBean params) {
        sendKwMsg(params);
        setMyExtensionModule();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("targetId", otherId)
                .appendQueryParameter("title", params.getNickname())
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Bundle bundle = new Bundle();
        bundle.putString("name", params.getNickname());
        if (params != null) {
            bundle.putString("img", params.getPost_headpic());
        }
        intent.putExtras(bundle);
        startActivity(intent);
        ProposedReplacementActivity.this.finish();
    }

    /**
     * 发送自定义消息
     */
    private void sendKwMsg(AddSwapDetailResult.ResultBean params) {
        setMyExtensionModule();

        KwMessage kwMessage = new KwMessage();
        kwMessage.setState("已发布");
        kwMessage.setMoney(params.getSwap_deposit());
        kwMessage.setTitle(params.getName());
        kwMessage.setPost_type(params.getSwap_mode());
        kwMessage.setImg_url(params.getHeadpic());
        kwMessage.setMessage_id(params.getPublish_time());
        kwMessage.setContent(params.getDetail());
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,
                "9b87af9ba3d640f19a3033a1a7fa3f3b", kwMessage, null, null,
                new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        Logger.wtf("消息成功存到本地数据库的回调");
                    }

                    @Override
                    public void onSuccess(Message message) {
                        Logger.wtf("消息发送成功的回调");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        Logger.wtf("消息发送失败的回调");
                    }
                });
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new SampleExtensionModule());
            }
        }
    }

    /**
     * 提出置换
     */
    private void addSwapDetailHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ProposedReplacementActivity.this));
        map.put("post_id", postId);
        map.put("swap_good", cdsId);
        map.put("swap_deposit", etProposedRepEdit.getText().toString());
        map.put("swap_mode", tvProposedRepPostWay.getText().toString());
        String params = EncryptionUtil.getParameter(ProposedReplacementActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/addSwapDetail.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_addSwapDetail")
                .cacheTime(2)
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ProposedReplacementActivity.this, "发送中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(ProposedReplacementActivity.this, "发送失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        AddSwapDetailResult addSwapDetailResult =
                                (AddSwapDetailResult) GsonUtil.json2Object(response, AddSwapDetailResult.class);
                        if (addSwapDetailResult != null
                                && addSwapDetailResult.getRet().equals("1")) {
                            initSendMsg(addSwapDetailResult.getResult());
                        } else {
                            Toasty.warning(ProposedReplacementActivity.this, "发送失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    private CustomPopWindow mAvatarPop;
    private Button btExitEms, btExitFlash, btExitFace, btExitDis;
    @BindView(R.id.tvProposedRepPostWay)
    TextView tvProposedRepPostWay;

    /**
     * 邮寄方式
     */
    @OnClick(R.id.rlProposedRepPostWay)
    public void updateResPostWay() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_dis, null);
        btExitEms = (Button) view.findViewById(R.id.btExitEms);
        btExitFlash = (Button) view.findViewById(R.id.btExitFlash);
        btExitFace = (Button) view.findViewById(R.id.btExitFace);
        btExitDis = (Button) view.findViewById(R.id.btExitDis);
        btExitEms.setOnClickListener(new DisClickListener());
        btExitFlash.setOnClickListener(new DisClickListener());
        btExitFace.setOnClickListener(new DisClickListener());
        btExitDis.setOnClickListener(new DisClickListener());
        mAvatarPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(llProposedRepParent, Gravity.BOTTOM, 0, 0);
    }

    private class DisClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mAvatarPop != null) {
                mAvatarPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.btExitEms:
                    tvProposedRepPostWay.setText("邮寄");
                    break;
                case R.id.btExitFlash:
                    tvProposedRepPostWay.setText("闪送");
                    break;
                case R.id.btExitFace:
                    tvProposedRepPostWay.setText("面交");
                    break;
            }
        }
    }
}
