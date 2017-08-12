package com.xuhong.smarthome.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public abstract class BaseFragment extends Fragment {


    protected ImmersionBar mImmersionBar;

    protected Activity mActivity;

    protected View mRootView;

    private boolean isImmersionBarEnabled = true;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsPrepare;

    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsImmersion;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        initView(mRootView);
        return mRootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        if (isLazyLoad()) {
            mIsPrepare = true;
            mIsImmersion = true;
            onLazyLoad();
        } else {

            if (isImmersionBarEnabled)
                initImmersionBar();
        }

    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected void setImmersionBarEnabled() {
        isImmersionBarEnabled = false;
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisible() {
        onLazyLoad();
    }

    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
        }
        if (mIsVisible && mIsImmersion && isImmersionBarEnabled) {
            initImmersionBar();
        }
    }


    /**
     * 设置布局id
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();



    /**
     * view与数据绑定
     */
    protected abstract void initView(View view);



    /**
     * 初始化数据
     */
    protected void initData() {

    }


    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.navigationBarWithKitkatEnable(false).init();
    }


}
