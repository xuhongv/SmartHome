package com.xuhong.smarthome;

import com.xuhong.smarthome.constant.Constant;
import cn.bmob.v3.Bmob;


public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //bomb初始化
        Bmob.initialize(this, Constant.BMOB_APPLICATION_ID);


    }
}
