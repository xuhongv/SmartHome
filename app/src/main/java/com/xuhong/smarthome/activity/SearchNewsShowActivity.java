package com.xuhong.smarthome.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.adapter.mRecyclerViewNewListAdapter;
import com.xuhong.smarthome.bean.HomeNewListBean;
import com.xuhong.smarthome.bean.HomeNewsListItemBean;
import com.xuhong.smarthome.constant.Constant;
import com.xuhong.smarthome.utils.OkHttpUtils;
import com.xuhong.smarthome.utils.ParseJson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchNewsShowActivity extends BaseActivity {


    private String newsList;

    //存储网址的链接URL和标题
    private List<String> urlList;
    private List<String> titleList;

    //输入的关键字
    private String tempContext;


    //bean
    private List<HomeNewsListItemBean> homeNewsListItemBeanList;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 103) {
                Log.e("==w", newsList.toString());
                HomeNewListBean newListBean = ParseJson.getHomeNewsListBean(newsList, HomeNewListBean.class);
                urlList = new ArrayList<>();
                titleList = new ArrayList<>();

                for (int i = 0; i < newListBean.getResult().getList().size(); i++) {
                    homeNewsListItemBeanList.add(new HomeNewsListItemBean(newListBean.getResult().getList().get(i).getTitle()
                            , newListBean.getResult().getList().get(i).getTime()
                            , newListBean.getResult().getList().get(i).getPic()
                            , newListBean.getResult().getList().get(i).getSrc()));
                    urlList.add(newListBean.getResult().getList().get(i).getUrl());
                    titleList.add(newListBean.getResult().getList().get(i).getTitle());
                }
                adapterNewsList.notifyDataSetChanged();
            }
        }
    };
    private mRecyclerViewNewListAdapter adapterNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news_show);
        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, toolbar);

        SearchView mSearchView = (SearchView) findViewById(R.id.mSearchView);
        mSearchView.setQueryHint("搜索新闻、人物");
        //展开后提交按钮 mSearchView.setSubmitButtonEnabled(true);
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) mSearchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        //强制隐藏下划线
        if (mSearchView != null) {
            try {
                //--拿到字节码
                Class<?> argClass = mSearchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                getNewsList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tempContext = s;
                return false;
            }
        });

        //RecyclerView
        homeNewsListItemBeanList = new ArrayList<>();
        RecyclerView mRecycleView_Search = (RecyclerView) findViewById(R.id.mRecycleView_Search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapterNewsList = new mRecyclerViewNewListAdapter(this, homeNewsListItemBeanList);
        mRecycleView_Search.setLayoutManager(linearLayoutManager);
        mRecycleView_Search.setAdapter(adapterNewsList);
        mRecycleView_Search.addItemDecoration(new DividerItemDecoration(
                SearchNewsShowActivity.this, DividerItemDecoration.VERTICAL));

        adapterNewsList.setOnItemClickListener(new mRecyclerViewNewListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("_webUrl", urlList.get(position));
                intent.putExtra("_webTitle", titleList.get(position));
                intent.setClass(SearchNewsShowActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });


        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("==w", "tempContext:" + tempContext);
                if (tempContext != null &&  !tempContext.isEmpty()) {
                    getNewsList(tempContext);
                } else {
                    Toast.makeText(SearchNewsShowActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void getNewsList(String searchContent) {
        OkHttpUtils.getInstance().getKeyList(searchContent, Constant.JUSU_APPKEY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    newsList = response.body().string();
                    mHandler.sendEmptyMessage(103);
                }
            }
        });
    }


}
