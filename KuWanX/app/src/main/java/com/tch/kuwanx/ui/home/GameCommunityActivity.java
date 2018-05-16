package com.tch.kuwanx.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.GameCommunityListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
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

/**
 * 游戏社区列表页
 */
public class GameCommunityActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvGameCommunity)
    RecyclerView rvGameCommunity;
    @BindView(R.id.refreshGameCommunity)
    SmartRefreshLayout refreshGameCommunity;

    private CommonAdapter gameCommunityAdapter;
    private Intent intent;
    private boolean isMore = false;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_community);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("游戏社区");
        setGameCommunityData();
        refreshGameCommunity.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameCommunityHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        pageIndex += 1;
        gameCommunityHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        pageIndex = 1;
        gameCommunityHttp();
    }

    /**
     * 获取游戏社区列表数据
     */
    private void gameCommunityHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("pageSize", String.valueOf(pageSize));
        map.put("pageIndex", String.valueOf(pageIndex));
        String params = EncryptionUtil.getParameter(GameCommunityActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/getGameCommunityList.jhtml")
                .params("data", params)
                .accessToken(false)//本次请求是否追加token
                .timeStamp(false)//本次请求是否携带时间戳
                .sign(false)//本次请求是否需要签名
                .syncRequest(false)//是否是同步请求，默认异步请求。true:同步请求
                .cacheKey(this.getClass().getSimpleName())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("获取游戏社区列表数据 == " + e);
                        if (refreshGameCommunity != null) {
                            refreshGameCommunity.finishLoadmore();
                            refreshGameCommunity.finishRefresh();
                        }
                    }

                    @Override
                    public void onSuccess(String response) {
                        gameCommunityList = (GameCommunityListResult)
                                GsonUtil.json2Object(response, GameCommunityListResult.class);
                        if (gameCommunityList != null
                                && gameCommunityList.getRet().equals("1")) {
                            if (isMore) {
                                gameCommunityAdapter.getDatas().addAll(gameCommunityList.getResult());
                                gameCommunityAdapter.notifyDataSetChanged();
                            } else {
                                gameCommunityAdapter.getDatas().clear();
                                gameCommunityAdapter.getDatas().addAll(gameCommunityList.getResult());
                                gameCommunityAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(GameCommunityActivity.this, gameCommunityList.getMsg(), Toast.LENGTH_SHORT, false).show();
                        }
                        if (refreshGameCommunity != null) {
                            refreshGameCommunity.finishLoadmore();
                            refreshGameCommunity.finishRefresh();
                        }
                    }
                });
    }

    private GameCommunityListResult gameCommunityList;
    private RecyclerView rvGameComItemImg;

    /**
     * 加载游戏社区列表数据
     */
    private void setGameCommunityData() {
        rvGameCommunity.setLayoutManager(new LinearLayoutManager(this));
        rvGameCommunity.setAdapter(gameCommunityAdapter = new CommonAdapter<GameCommunityListResult.ResultBean>(this,
                R.layout.item_game_community, new ArrayList<GameCommunityListResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, GameCommunityListResult.ResultBean item, int position) {
                //内容
                holder.setText(R.id.tvGameComItemContent, item.getTitle());
                holder.setText(R.id.tvGameComItemLoc, item.getModule());
                try {
                    holder.setText(R.id.tvGameComItemTime, Utils.longToString(Long.parseLong(item.getCreate_time()), "yyyy-MM-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (item.getImgList() != null
                        && item.getImgList().size() == 1) {
                    holder.setVisible(R.id.ivGameComItemImg, true);
                    holder.setVisible(R.id.rvGameComItemImg, false);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            Utils.getScreenWidth(GameCommunityActivity.this),
                            Utils.getScreenWidth(GameCommunityActivity.this) / 3
                    );
                    lp.setMargins(5, 5, 5, 5);
                    ImageView ivPhoto = (ImageView) holder.getView(R.id.ivGameComItemImg);
                    ivPhoto.setLayoutParams(lp);
                    if (!TextUtils.isEmpty(item.getImgList().get(0).getImg())) {
                        Glide.with(GameCommunityActivity.this)
                                .load(item.getImgList().get(0).getImg())
                                .into(ivPhoto);
                    } else {
                        holder.setImageResource(R.id.ivGameComItemImg, R.drawable.placeholder);
                    }
                } else if (item.getImgList() != null
                        && item.getImgList().size() > 1) {
                    holder.setVisible(R.id.ivGameComItemImg, false);
                    holder.setVisible(R.id.rvGameComItemImg, true);
                    rvGameComItemImg = (RecyclerView) holder.getView(R.id.rvGameComItemImg);
                    initHomeChangeImgs(rvGameComItemImg, item.getImgList());
                } else if (item.getImgList() != null
                        && item.getImgList().size() < 1) {
                    holder.setVisible(R.id.ivGameComItemImg, false);
                    holder.setVisible(R.id.rvGameComItemImg, false);
                } else {
                    holder.setVisible(R.id.ivGameComItemImg, false);
                    holder.setVisible(R.id.rvGameComItemImg, false);
                }
            }
        });
        gameCommunityAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(GameCommunityActivity.this, GameCommunityDetailsActivity.class);
                intent.putExtra("showType", "showAll");
                intent.putExtra("communityId", gameCommunityList.getResult().get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 加载显示的图片
     *
     * @param view
     */
    private void initHomeChangeImgs(RecyclerView view, List<GameCommunityListResult.ResultBean.ImgListBean> list) {
        final ArrayList<String> itemImgs = new ArrayList<String>();
        CommonAdapter homeChangeImgsAdapter;
        for (GameCommunityListResult.ResultBean.ImgListBean ib : list) {
            itemImgs.add(ib.getImg());
        }
        view.setLayoutManager(new GridLayoutManager(GameCommunityActivity.this, 3));
        view.setAdapter(homeChangeImgsAdapter = new CommonAdapter<GameCommunityListResult.ResultBean.ImgListBean>(GameCommunityActivity.this,
                R.layout.img,
                new ArrayList<GameCommunityListResult.ResultBean.ImgListBean>()) {
            @Override
            protected void convert(ViewHolder holder, GameCommunityListResult.ResultBean.ImgListBean item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(GameCommunityActivity.this) / 3 - 20,
                        Utils.getScreenWidth(GameCommunityActivity.this) / 3 - 20
                );
                lp.setMargins(5, 5, 5, 5);
                ImageView ivPhoto = (ImageView) holder.getView(R.id.ivPhoto);
                ivPhoto.setLayoutParams(lp);
                if (!TextUtils.isEmpty(item.getImg())) {
                    Glide.with(GameCommunityActivity.this)
                            .load(item.getImg())
                            .into(ivPhoto);
                } else {
                    holder.setImageResource(R.id.ivPhoto, R.drawable.placeholder);
                }
            }
        });
        homeChangeImgsAdapter.getDatas().clear();
        homeChangeImgsAdapter.getDatas().addAll(list);
        homeChangeImgsAdapter.notifyDataSetChanged();
        homeChangeImgsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showGallery(itemImgs, position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 展示大图画廊
     */
    private void showGallery(ArrayList<String> list, int position) {
        Album.gallery(this)
                .requestCode(100) // 请求码，会在listener中返回。
                .checkedList(list) // 要浏览的图片列表：ArrayList<String>。
                .currentPosition(position)
                .navigationAlpha(250) // Android5.0+的虚拟导航栏的透明度。
                .checkable(false) // 是否有浏览时的选择功能。
                .widget(
                        Widget.newDarkBuilder(GameCommunityActivity.this)
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void GameComsTitleBack() {
        GameCommunityActivity.this.finish();
    }
}
