package com.xuhong.smarthome;

import android.content.Context;
import android.util.Log;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizEventType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.xuhong.smarthome.activity.BaseActivity;
import com.xuhong.smarthome.constant.Constant;
import com.xuhong.smarthome.utils.SharePreUtils;

import java.util.List;

import cn.bmob.v3.Bmob;


public class Application extends android.app.Application {


    private Context mContext;


    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // 上下文
        mContext = getApplicationContext();
        //bomb初始化
        Bmob.initialize(this, Constant.BMOB_APPLICATION_ID);

        GizWifiSDK.sharedInstance().setListener(listener);
        //注册机智云SDK
        GizWifiSDK.sharedInstance().startWithAppID(this, "ea46989a46044d79ac43b1558f0bd101");
    }

    private GizWifiSDKListener listener = new GizWifiSDKListener() {
        @Override
        public void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID, String eventMessage) {
            if (eventType == GizEventType.GizEventSDK) {
                // SDK的事件通知
                Log.i("==w", "匿名登录");
                //匿名登录
                GizWifiSDK.sharedInstance().userLoginAnonymous();
            }
        }

        @Override
        public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
            super.didUserLogin(result, uid, token);
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                Log.i("==w", "匿名登录成功：uid:" + uid + ",token:" + token);
                SharePreUtils.putString(getContext(), "_uid", uid);
                SharePreUtils.putString(getContext(), "_token", token);
            }
        }
    };


}
