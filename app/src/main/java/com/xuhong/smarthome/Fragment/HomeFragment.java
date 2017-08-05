package com.xuhong.smarthome.Fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.SearchNewsShowActivity;
import com.xuhong.smarthome.adapter.mPagerAdapter;
import com.xuhong.smarthome.adapter.mRecyclerViewCardAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private BGABanner mBanner;
    private LinearLayout llSearch;
    private TextView tvSearch;
    private LinearLayout mSearchLayout;
    private ScrollView mScrollView;
    private boolean isExpand = false;
    private Toolbar toolbar;
    private RecyclerView mRecycleView_NewsIndex;


    private mRecyclerViewCardAdapter adapter ;



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



        //新闻索引卡片
        List<String> list =new ArrayList<>();
        list.add("热点");
        list.add("军事");
        list.add("爱情");
        list.add("人生");
        list.add("国际");
        list.add("国际");
        list.add("国际");
        list.add("国际");
        list.add("国际");
        list.add("国际");
        list.add("国际");
        list.add("国际");
        list.add("国际");
        list.add("国际");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter =new mRecyclerViewCardAdapter(getActivity(),list);

        mRecycleView_NewsIndex= (RecyclerView) view.findViewById(R.id.mRecycleView_NewsIndex);
        mRecycleView_NewsIndex.setLayoutManager(linearLayoutManager);
        mRecycleView_NewsIndex.setAdapter(adapter);
        adapter.setOnItemClickListener(new mRecyclerViewCardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });


    }


    @Override
    protected void initData() {
        mBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        .placeholder(R.mipmap.holder)
                        .error(R.mipmap.holder)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
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
        tvSearch.setText("搜索你想要的人或事");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    private void reduce() {
        //设置收缩状态时的布局
        tvSearch.setText("搜索");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
        LayoutParams.width = dip2px(80);
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mSearchLayout.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mSearchLayout);
    }

    void beginDelayedTransition(ViewGroup view) {
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
                startActivity(new Intent(getActivity(), SearchNewsShowActivity.class));
                break;
        }
    }


}
