package com.xuhong.smarthome.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;

import com.gizwits.gizwifisdk.api.WifiAutoConnectManager;
import com.xuhong.smarthome.constant.Constant;

/**
 * Created by xuhong on 2017/10/12.
 */

public class WifiChangeReciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String wifiname = SharePreUtils.getString(context, Constant.WIFI_NAME, "");
        String wifipass = SharePreUtils.getString(context, Constant.WIFI_PW, "");
        String connectWifiSsid = NetStatusUtil.getConnectWifiSsid(context);

        if (connectWifiSsid != null && connectWifiSsid.contains(Constant.SoftAP_Start)) {
        } else {

            if (connectWifiSsid.contains(wifiname)) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiAutoConnectManager manager = new WifiAutoConnectManager(wifiManager);
            manager.connect(wifiname, wifipass, WifiAutoConnectManager.getCipherType(context, wifiname));
        }
    }
}
