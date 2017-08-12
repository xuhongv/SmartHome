package com.xuhong.smarthome;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/8/12 0012.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //bomb默认初始化
        Bmob.initialize(this, "Your Application ID");


    }
}
