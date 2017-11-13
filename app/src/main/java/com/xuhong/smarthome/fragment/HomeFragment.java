package com.xuhong.smarthome.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.SearchNewsActivity;
import com.xuhong.smarthome.activity.WebViewActivity;
import com.xuhong.smarthome.adapter.SpaceItemDecoration;
import com.xuhong.smarthome.adapter.mRecyclerViewCardAdapter;
import com.xuhong.smarthome.adapter.mRecyclerViewNewListAdapter;
import com.xuhong.smarthome.bean.HomeNewListBean;
import com.xuhong.smarthome.bean.HomeNewsChannelBean;
import com.xuhong.smarthome.bean.HomeNewsListItemBean;
import com.xuhong.smarthome.constant.Constant;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.OkHttpUtils;
import com.xuhong.smarthome.utils.ParseJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private BGABanner mBanner;
    private LinearLayout llSearch;
    private TextView tvSearch;
    private LinearLayout mSearchLayout;
    private ScrollView mScrollView;
    private boolean isExpand = false;
    private Toolbar toolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    //recyclerview
    private RecyclerView mRecycleView_NewsIndex, mRecycleView_NewsLists;

    //适配器
    private mRecyclerViewCardAdapter adapter;
    private mRecyclerViewNewListAdapter adapterNewsList;

    //bean
    private List<HomeNewsListItemBean> homeNewsListItemBeanList;

    //存储网址的链接URL和标题
    private List<String> urlList;
    private List<String> titleList;
    private List<String> picList;

    //新闻索引频道
    private String newsChannel;
    private String newsList;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //新闻索引
            if (msg.what == 101) {
                list.clear();
                HomeNewsChannelBean newsChannelBean = ParseJson.getHomeNewsChannelBean(newsChannel, HomeNewsChannelBean.class);
                for (int i = 0; i < newsChannelBean.getResult().size(); i++) {
                    list.add(newsChannelBean.getResult().get(i));
                }
                adapter.notifyDataSetChanged();
            }

            //新闻列表
            if (msg.what == 102) {
                HomeNewListBean newListBean = ParseJson.getHomeNewsListBean(newsList, HomeNewListBean.class);
                urlList = new ArrayList<>();
                titleList=new ArrayList<>();
                picList=new ArrayList<>();

                for (int i = 0; i < newListBean.getResult().getList().size(); i++) {
                    homeNewsListItemBeanList.add(new HomeNewsListItemBean(newListBean.getResult().getList().get(i).getTitle()
                            , newListBean.getResult().getList().get(i).getTime()
                            , newListBean.getResult().getList().get(i).getPic()
                            , newListBean.getResult().getList().get(i).getSrc()));
                    urlList.add(newListBean.getResult().getList().get(i).getUrl());
                    titleList.add(newListBean.getResult().getList().get(i).getTitle());
                    picList.add(newListBean.getResult().getList().get(i).getPic());
                }

                adapterNewsList.notifyDataSetChanged();
            }


        }
    };
    private List<String> list = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {


        mBanner = (BGABanner) view.findViewById(R.id.banner_main_depth);
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tvSearch = (TextView) view.findViewById(R.id.tv_search);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollView);
        mSearchLayout = (LinearLayout) view.findViewById(R.id.llSearch);

        String[] stringArray = getResources().getStringArray(R.array.news_index);
        Collections.addAll(list, stringArray);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new mRecyclerViewCardAdapter(getActivity(), list);

        mRecycleView_NewsIndex = (RecyclerView) view.findViewById(R.id.mRecycleView_NewsIndex);
        mRecycleView_NewsIndex.setLayoutManager(linearLayoutManager);
        mRecycleView_NewsIndex.addItemDecoration(new SpaceItemDecoration(20));
        mRecycleView_NewsIndex.setAdapter(adapter);
        adapter.setOnItemClickListener(new mRecyclerViewCardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView_NewsLists = (RecyclerView) view.findViewById(R.id.mRecycleView_NewsLists);
        homeNewsListItemBeanList = new ArrayList<>();
        adapterNewsList = new mRecyclerViewNewListAdapter(getActivity(), homeNewsListItemBeanList);
        mRecycleView_NewsLists.setLayoutManager(linearLayoutManager2);
        mRecycleView_NewsLists.setAdapter(adapterNewsList);
        //解决了嵌套在recyclerview手滑卡顿的问题
        mRecycleView_NewsLists.setHasFixedSize(true);
        mRecycleView_NewsLists.setNestedScrollingEnabled(false);
        //设置分割线
        mRecycleView_NewsLists.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));


        adapterNewsList.setOnItemClickListener(new mRecyclerViewNewListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("_webUrl", urlList.get(position));
                intent.putExtra("_webTitle", titleList.get(position));
                intent.putExtra("_picTitle", picList.get(position));
                intent.setClass(getActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 下拉刷新
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_blue_bright, R.color.colorPrimaryDark,
                android.R.color.holo_orange_dark, android.R.color.holo_red_dark, android.R.color.holo_purple);

        // 手动调用,通知系统去测量
        mSwipeRefreshLayout.measure(0, 0);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 3000);
            }
        });



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //getNewsChannel();
    }

    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 3000);

        //getNewsChannel();
        getNewsList();
        mBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                if (getActivity()!=null){
                    Glide.with(getActivity())
                          .load(model)
                          .placeholder(R.mipmap.holder)
                          .error(R.mipmap.holder)
                          .centerCrop()
                          .dontAnimate()
                          .into(itemView);
                }
            }
        });

        mBanner.setData(Arrays.asList("http://app.chinagtop.com/app/img/nvc/1.png", "http://app.chinagtop.com/app/img/nvc/2.png", "http://app.chinagtop.com/app/img/nvc/3.png"), Arrays.asList("新版厨具", "提示文字2", "提示文字3"));

        //设置toolbar初始透明度为0
        toolbar.getBackground().mutate().setAlpha(0);


        //scrollview滚动状态监听
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //改变toolbar的透明度
                changeToolbarAlpha();
                //滚动距离>=大图高度-toolbar高度 即toolbar完全盖住大图的时候 且不是伸展状态 进行伸展操作
                if (mScrollView.getScrollY() >= mBanner.getHeight() - toolbar.getHeight() && !isExpand) {
                    expand();
                    isExpand = true;
                }
                //滚动距离<=0时 即滚动到顶部时  且当前伸展状态 进行收缩操作
                else if (mScrollView.getScrollY() <= 0 && isExpand) {
                    reduce();
                    isExpand = false;
                }
                mSwipeRefreshLayout.setEnabled(mScrollView.getScrollY() == 0);
            }
        });


    }

    private void getNewsList() {

        OkHttpUtils.getInstance().getMyNewsList("头条", 0, 40, Constant.JUSU_APPKEY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    newsList = response.body().string();
                    mHandler.sendEmptyMessage(102);
                }
            }
        });
    }

    private void getNewsChannel() {

        OkHttpUtils.getInstance().sendCommon(Constant.URL_GET_NEWS_CHANNEL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    newsChannel = response.body().string();
                    mHandler.sendEmptyMessage(101);
                }

            }
        });
    }

    private void changeToolbarAlpha() {
        int scrollY = mScrollView.getScrollY();
        //快速下拉会引起瞬间scrollY<0
        if (scrollY < 0) {
            toolbar.getBackground().mutate().setAlpha(0);
            return;
        }
        //计算当前透明度比率
        float radio = Math.min(1, scrollY / (mBanner.getHeight() - toolbar.getHeight() * 1f));
        //设置透明度
        toolbar.getBackground().mutate().setAlpha((int) (radio * 0xFF));
    }

    private void expand() {


        //设置伸展状态时的布局
        tvSearch.setText("搜索新闻");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(20), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    private void reduce() {
        //设置收缩状态时的布局
        tvSearch.setText("搜索新闻");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = dip2px(100);
        LayoutParams.setMargins(dip2px(10), dip2px(20), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    private void beginDelayedTransition(ViewGroup view) {
        TransitionSet mSet = new AutoTransition();
        mSet.setDuration(300);
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSearch:
                startActivity(new Intent(getActivity(), SearchNewsActivity.class));
                break;
        }
    }
}
