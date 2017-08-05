package com.xuhong.smarthome.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.SearchNewsShowActivity;

import java.util.Arrays;

import cn.bingoogolapple.bgabanner.BGABanner;


public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private BGABanner mBanner;
    private ViewPager mViewPager_fragmentHome;
    private LinearLayout llSearch;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        mBanner = (BGABanner) view.findViewById(R.id.banner_main_depth);
        mViewPager_fragmentHome = (ViewPager) view.findViewById(R.id.mViewPager_fragmentHome);
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);
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
