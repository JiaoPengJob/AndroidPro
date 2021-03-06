package com.tch.zx.activity.line;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.SearchedEXAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.SearchHistoryBean;
import com.tch.zx.dao.green.SearchHistoryBeanDao;
import com.tch.zx.http.bean.result.SearchInfoResultBean;
import com.tch.zx.http.presenter.SearchInfoPresenter;
import com.tch.zx.http.view.SearchInfoView;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.FlowTagGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;

/**
 * 搜索页面
 */
public class SearchMainActivity extends BaseActivity {

    /**
     * 搜索框
     */
    @BindView(R.id.sv_search_main)
    SearchView sv_search_main;
    /**
     * 搜索历史列表
     */
    @BindView(R.id.ftg_history)
    FlowTagGroup ftg_history;
    /**
     * 清空搜索历史
     */
    @BindView(R.id.tv_clear_history)
    TextView tv_clear_history;
    /**
     * 搜索内容展示的列表
     */
    @BindView(R.id.elv_searched)
    ExpandableListView elv_searched;
    /**
     * 开始搜索按钮
     */
    @BindView(R.id.tv_search_begin)
    TextView tv_search_begin;
    /**
     * 未搜索到内容提示
     */
    @BindView(R.id.tv_search_unsearch_info)
    TextView tv_search_unsearch_info;
    /**
     * 搜索历史
     */
    @BindView(R.id.sv_history)
    ScrollView sv_history;
    @BindView(R.id.llSearchParent)
    LinearLayout llSearchParent;
    /**
     * 搜索内容适配器
     */
    SearchedEXAdapter searchedEXAdapter;
    /**
     * 搜索框输入完成的内容
     */
    private String searchViewText;
    /**
     * 搜索结果集合
     */
    private List<String> searchDatas;
    /**
     * 搜索历史数据dao
     */
    private SearchHistoryBeanDao searchHistoryBeanDao;
    /**
     * 数据库session
     */
    private DaoSession daoSession;
    /**
     * 搜索数据集合
     */
    private List<SearchHistoryBean> searchHistoryBeanList;

    private SearchInfoPresenter searchInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_search_main);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //获取搜索历史的数据dao
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        searchHistoryBeanDao = daoSession.getSearchHistoryBeanDao();

        initSearchView();
        setGridViewData();
    }

    /**
     * 加载SearchView组件事件
     */
    private void initSearchView() {
        int id = sv_search_main.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv_search_main.findViewById(id);
        textView.setTextColor(Color.parseColor("#666666"));//字体颜色
        textView.setTextSize(16);//字体、提示字体大小
        textView.setHintTextColor(Color.GRAY);//提示字体颜色**
        //取消搜索输入框的下划线
        Class<?> c = sv_search_main.getClass();
        try {
            //通过反射，获得类对象的一个属性对象
            Field f = c.getDeclaredField("mSearchPlate");
            //设置此私有属性是可访问的
            f.setAccessible(true);
            //获得属性的值
            View v = (View) f.get(sv_search_main);
            //设置此view的背景
            v.setBackgroundResource(R.drawable.shape_search);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置搜索文本监听
        sv_search_main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索框中搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewText = newText;
                //如果搜索框里内容清空了,则展示搜索历史
                if (newText == null || newText.equals("")) {
                    setGridViewData();
                }
                return false;
            }
        });
    }

    /**
     * 加载搜索历史数据
     */
    private void setGridViewData() {
        //清空历史列表中所有子布局,重新加载
        ftg_history.removeAllViews();

        //如果搜索数据列表显示,则隐藏
        if (elv_searched.getVisibility() == View.VISIBLE) {
            elv_searched.setVisibility(View.GONE);
        }
        //如果提示信息显示,则隐藏
        if (tv_search_unsearch_info.getVisibility() == View.VISIBLE) {
            tv_search_unsearch_info.setVisibility(View.GONE);
        }
        //显示未搜索到内容的提示
        sv_history.setVisibility(View.VISIBLE);

        //从数据库中查询所有历史记录
        searchHistoryBeanList = daoSession.getSearchHistoryBeanDao().loadAll();

        for (int i = 0; i < searchHistoryBeanList.size(); i++) {
            for (int j = searchHistoryBeanList.size() - 1; j > i; j--) {
                if (searchHistoryBeanList.get(i).getSearchHistory().equals(searchHistoryBeanList.get(j).getSearchHistory())) {
                    searchHistoryBeanList.remove(j);
                }
            }
        }

        if (searchHistoryBeanList != null) {
            if (searchHistoryBeanList.size() == 0) {
                tv_clear_history.setVisibility(View.GONE);
            } else {
                tv_clear_history.setVisibility(View.VISIBLE);
                //设置历史列表显示
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(lp);
                mlp.setMargins(10, 10, 10, 10);
                for (int i = 0; i < searchHistoryBeanList.size(); i++) {
                    final TextView tv_item_search_history = new TextView(this);
                    tv_item_search_history.setLayoutParams(mlp);
                    tv_item_search_history.setBackground(this.getResources().getDrawable(R.drawable.shape_item_search_history));
                    tv_item_search_history.setPadding(30, 20, 30, 20);
                    tv_item_search_history.setTextColor(Color.parseColor("#666666"));
                    tv_item_search_history.setTextSize(16f);
                    tv_item_search_history.setText(searchHistoryBeanList.get(i).getSearchHistory().toString());
                    tv_item_search_history.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sv_search_main.setQuery(tv_item_search_history.getText().toString(), false);
                        }
                    });
                    ftg_history.addView(tv_item_search_history);
                }
            }
        } else {
            tv_clear_history.setVisibility(View.GONE);
        }
    }

    /**
     * 清空所有搜索历史
     */
    @OnClick(R.id.tv_clear_history)
    public void clearAllHistoryOnClick() {
        try {
            searchHistoryBeanDao.deleteAll();
            //清空历史列表中所有子布局,重新加载
            ftg_history.removeAllViews();
            tv_clear_history.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置搜索出来的数据
     */
    private void setExData(List<SearchInfoResultBean.ResultBean.ResponseObjectBean> list) {

        List<String> gList = new ArrayList<String>();
        for (SearchInfoResultBean.ResultBean.ResponseObjectBean searchResult : list) {
            if (searchResult.getType().equals("1")) {
                gList.add("直播");
            } else if (searchResult.getType().equals("2")) {
                gList.add("小课");
            } else if (searchResult.getType().equals("3")) {
                gList.add("专栏");
            } else if (searchResult.getType().equals("4")) {
                gList.add("线下");
            }
        }
        //去除重复
        HashSet<String> hs = new HashSet<String>(gList);
        gList = new ArrayList<String>();
        Iterator<String> iterator = hs.iterator();
        while (iterator.hasNext()) {
            gList.add(iterator.next());
        }

        List<SearchInfoResultBean.ResultBean.ResponseObjectBean> lives = new ArrayList<SearchInfoResultBean.ResultBean.ResponseObjectBean>();
        List<SearchInfoResultBean.ResultBean.ResponseObjectBean> classes = new ArrayList<SearchInfoResultBean.ResultBean.ResponseObjectBean>();
        List<SearchInfoResultBean.ResultBean.ResponseObjectBean> columns = new ArrayList<SearchInfoResultBean.ResultBean.ResponseObjectBean>();
        List<SearchInfoResultBean.ResultBean.ResponseObjectBean> offlines = new ArrayList<SearchInfoResultBean.ResultBean.ResponseObjectBean>();
        List<List<SearchInfoResultBean.ResultBean.ResponseObjectBean>> iList = new ArrayList<List<SearchInfoResultBean.ResultBean.ResponseObjectBean>>();

        for (SearchInfoResultBean.ResultBean.ResponseObjectBean search : list) {
            if (search.getType().equals("1")) {
                lives.add(search);
            } else if (search.getType().equals("2")) {
                classes.add(search);
            } else if (search.getType().equals("3")) {
                columns.add(search);
            } else if (search.getType().equals("4")) {
                offlines.add(search);
            }
        }

        for (String s : gList) {
            if (s.equals("直播")) {
                iList.add(lives);
            } else if (s.equals("小课")) {
                iList.add(classes);
            } else if (s.equals("专栏")) {
                iList.add(columns);
            } else if (s.equals("线下")) {
                iList.add(offlines);
            }
        }

        searchedEXAdapter = new SearchedEXAdapter(this, gList, iList);
        elv_searched.setGroupIndicator(null);
        elv_searched.setAdapter(searchedEXAdapter);
        for (int i = 0; i < gList.size(); i++) {
            elv_searched.expandGroup(i);
        }

        elv_searched.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        elv_searched.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                return false;
            }
        });

    }

    /**
     * 开始搜索点击事件
     */
    @OnClick(R.id.tv_search_begin)
    public void searchBeginOnClick() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(llSearchParent, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(llSearchParent.getWindowToken(), 0);
        //判断搜索框输入内容是否为空
        if (searchViewText != null && !searchViewText.equals("")) {
            //每点击一次搜索,将搜索信息存放进数据库中
            try {
                SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
                searchHistoryBean.setSearchHistory(searchViewText);
                searchHistoryBeanDao.insert(searchHistoryBean);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //进行搜索
            searchInfo(searchViewText);

        } else {
            Toast.makeText(this, "您还未输入任何内容!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 访问服务器
     */
    private void searchInfo(String text) {
        searchInfoPresenter = new SearchInfoPresenter(this);
        searchInfoPresenter.onCreate();
        searchInfoPresenter.attachView(searchInfoView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", text);
        String data = HelperUtil.getParameter(map);
        searchInfoPresenter.searchInfo(data);
    }

    private SearchInfoView searchInfoView = new SearchInfoView() {
        @Override
        public void onSuccess(SearchInfoResultBean searchInfoResultBean) {
            if (searchInfoResultBean.getResult().getResponseObject() != null
                    && searchInfoResultBean.getResult().getResponseObject().size() > 0) {
                //如果搜索历史列表显示,则隐藏
                if (sv_history.getVisibility() == View.VISIBLE) {
                    sv_history.setVisibility(View.GONE);
                }
                //如果提示信息显示,则隐藏
                if (tv_search_unsearch_info.getVisibility() == View.VISIBLE) {
                    tv_search_unsearch_info.setVisibility(View.GONE);
                }
                //显示未搜索到内容的提示
                elv_searched.setVisibility(View.VISIBLE);
                setExData(searchInfoResultBean.getResult().getResponseObject());
            } else {
                //如果搜索历史列表显示,则隐藏
                if (sv_history.getVisibility() == View.VISIBLE) {
                    sv_history.setVisibility(View.GONE);
                }
                //如果搜索结果展示中,则隐藏
                if (elv_searched.getVisibility() == View.VISIBLE) {
                    elv_searched.setVisibility(View.GONE);
                }
                //显示未搜索到内容的提示
                tv_search_unsearch_info.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SearchInfoView:==" + result);
        }
    };

}
