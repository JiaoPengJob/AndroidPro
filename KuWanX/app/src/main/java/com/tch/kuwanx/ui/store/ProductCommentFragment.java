package com.tch.kuwanx.ui.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.GoodCommentsResult;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Jiaop on 2017/10/24.
 * 商品评论
 */
public class ProductCommentFragment extends Fragment implements OnRefreshLoadmoreListener {

    @BindView(R.id.rvProductComment)
    RecyclerView rvProductComment;
    @BindView(R.id.refreshProductCommentF)
    SmartRefreshLayout refreshProductCommentF;

    private View viewRoot;
    private CommonAdapter productCommentAdapter;
    private String goodId;
    private boolean isMore = false;
    private int index = 1, size = 10;

    public static ProductCommentFragment getInstance(String goodId) {
        ProductCommentFragment sf = new ProductCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("goodId", goodId);
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_product_comments, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        if (getArguments().getString("goodId") != null) {
            goodId = getArguments().getString("goodId");
        }
        initProductCommentData();
        refreshProductCommentF.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        goodCommentsHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        goodCommentsHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        goodCommentsHttp();
    }

    private RecyclerView rvProComImgs;

    private void initProductCommentData() {
        rvProductComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductComment.setAdapter(productCommentAdapter = new CommonAdapter<GoodCommentsResult.ResultBean>(getActivity(),
                R.layout.item_product_comments, new ArrayList<GoodCommentsResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, GoodCommentsResult.ResultBean item, int position) {
                rvProComImgs = (RecyclerView) holder.getView(R.id.rvProComImgs);
                initHomeChangeImgs(rvProComImgs);

                Glide.with(getActivity())
                        .load("http://img17.3lian.com/d/file/201701/16/779db6efe9d4520e07e8bfb8b9e55175.jpg")
                        .apply(bitmapTransform(new CropCircleTransformation()))
                        .into((ImageView) holder.getView(R.id.ivProComItemPhoto));
                holder.setText(R.id.tvProComItemName, item.getNickname());
                try {
                    holder.setText(R.id.tvProComItemDate, Utils.longToString(Long.parseLong(item.getComm_createtime()), "yyyy-MM-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.setText(R.id.tvProComItemNorm, "规格");
                holder.setText(R.id.tvProComItemContent, item.getComm_content());
            }
        });
    }

    /**
     * 加载显示的图片
     *
     * @param view
     */
    private void initHomeChangeImgs(RecyclerView view) {
        final ArrayList<String> itemImgs = new ArrayList<String>();
        CommonAdapter homeChangeImgsAdapter;
        for (int i = 0; i < 2; i++) {
            itemImgs.add("http://img2.imgtn.bdimg.com/it/u=4215897016,3222246668&fm=27&gp=0.jpg");
        }
        view.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        view.setAdapter(homeChangeImgsAdapter = new CommonAdapter<String>(getActivity(), R.layout.img, itemImgs) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(getActivity()) / 3,
                        Utils.getScreenWidth(getActivity()) / 3
                );
                lp.setMargins(0, 0, 0, 0);
                ImageView ivPhoto = (ImageView) holder.getView(R.id.ivPhoto);
                ivPhoto.setLayoutParams(lp);
                Glide.with(getActivity())
                        .load(item)
                        .into(ivPhoto);
            }
        });
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
                .requestCode(100)
                .checkedList(list)
                .currentPosition(position)
                .navigationAlpha(250)
                .checkable(false)
                .widget(
                        Widget.newDarkBuilder(getActivity())
                                .title("预览")
                                .build()
                )
                .start();
    }

    /**
     * 商品的评价列表
     */
    private void goodCommentsHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("good_id", goodId);
//        map.put("good_id", "ea0a10ba182f4983a5968b5909486a0d");
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(getActivity(), map);
        EasyHttp.post(HttpUtils.URI_CENTER + "good/getGoodComments.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getGoodComments")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        getActivity(), "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshProductCommentF != null) {
                            refreshProductCommentF.finishLoadmore();
                            refreshProductCommentF.finishRefresh();
                        }

                        Toasty.warning(getActivity(), "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshProductCommentF != null) {
                            refreshProductCommentF.finishLoadmore();
                            refreshProductCommentF.finishRefresh();
                        }

                        GoodCommentsResult goodCommentsResult =
                                (GoodCommentsResult) GsonUtil.json2Object(response, GoodCommentsResult.class);
                        if (goodCommentsResult != null
                                && goodCommentsResult.getRet().equals("1")) {
                            if (isMore) {
                                productCommentAdapter.getDatas().addAll(goodCommentsResult.getResult());
                                productCommentAdapter.notifyDataSetChanged();
                            } else {
                                productCommentAdapter.getDatas().clear();
                                productCommentAdapter.getDatas().addAll(goodCommentsResult.getResult());
                                productCommentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(getActivity(), "获取失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
