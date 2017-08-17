package com.xuhong.smarthome;

import android.content.Context;

import com.xuhong.smarthome.constant.Constant;
import cn.bmob.v3.Bmob;


public class Application extends android.app.Application {


    private  Context mContext;


    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 上下文
        mContext = getApplicationContext();
        //bomb初始化
        Bmob.initialize(this, Constant.BMOB_APPLICATION_ID);


    }
}
