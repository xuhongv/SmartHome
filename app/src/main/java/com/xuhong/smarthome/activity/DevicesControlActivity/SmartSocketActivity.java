package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.BaseActivity;
import com.xuhong.smarthome.utils.L;

import java.util.concurrent.ConcurrentHashMap;

public class SmartSocketActivity extends BaseDevicesControlActivity {

    //智能插头设备
    private GizWifiDevice mDevice;

    //数据点
    private static final String FLAG_ONOFF = "OnOff";
    private static final String FLAG_TIME_ON_MINUTE = "Time_On_Minute";
    private static final String FLAG_Time_OFF_MINUTE = "Time_Off_Minute";
    private static final String FLAG_IS_TIME_ONOFF = "Time_OnOff";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_socket);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvName = (TextView) findViewById(R.id.tvName);

        Intent intent = getIntent();
        mDevice = intent.getParcelableExtra("GizWifiDevice");
        mDevice.setListener(gizWifiDeviceListener);
        tvName.setText(mDevice.getAlias() == null ? mDevice.getProductName() : mDevice.getAlias());
    }

    @Override
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        L.e("设备界面回调：" + device.getMacAddress());
    }

    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
        L.e("设备界面回调数据：" + dataMap.toString());
    }

    @Override
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
        L.e("设备界面回调数据 netStatus：" + netStatus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDevice.setListener(null);
        mDevice.setSubscribe(false);
    }
}
