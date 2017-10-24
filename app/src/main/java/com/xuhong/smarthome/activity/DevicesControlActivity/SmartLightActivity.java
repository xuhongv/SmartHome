package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.L;

import java.util.concurrent.ConcurrentHashMap;

public class SmartLightActivity extends BaseDevicesControlActivity implements View.OnClickListener {


    //数据点
    private static final String FLAG_ONOFF = "OnOff";
    private static final String FLAG_TIME_ON_MINUTE = "Time_On_Minute";
    private static final String FLAG_Time_OFF_MINUTE = "Time_Off_Minute";
    private static final String FLAG_IS_TIME_ONOFF = "Time_OnOff";

    private static final int CODE_HANDLER_UPADATA_UI = 102;


    //临时存储
    private boolean isOpenoff;
    //ui
    private CheckBox mCbStatus;
    private LinearLayout mLlIndex;
    private CheckBox mCbSwitch;
    private TextView mTvSwitch;
    private TextView tvStatus;
    private ImageView mIvTimer;
    private TextView mTvShareDevices;
    private ImageView mIvCountDowm;
    private TextView mTvDevicesLog;



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_HANDLER_UPADATA_UI) {
                if (isOpenoff) {
                    mCbStatus.setChecked(true);
                    mCbSwitch.setChecked(true);
                    tvStatus.setText("插头已经关闭");
                    tvStatus.setTextColor(getResources().getColor(R.color.red0));
                } else {
                    mCbStatus.setChecked(false);
                    mCbSwitch.setChecked(false);
                    tvStatus.setText("插头已经开启");
                    tvStatus.setTextColor(getResources().getColor(R.color.green2));
                }


            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smart_light;
    }

    @Override
    protected void bindView() {

        //把状态显示图片设置为点击没反应
        mCbStatus = (CheckBox) findViewById(R.id.cbStatus);
        mCbStatus.setClickable(false);

        //开关按钮
        mCbSwitch = (CheckBox) findViewById(R.id.cbSwitch);
        mCbSwitch.setOnClickListener(this);


        mLlIndex = (LinearLayout) findViewById(R.id.llIndex);

        mTvSwitch = (TextView) findViewById(R.id.tvSwitch);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        mIvTimer = (ImageView) findViewById(R.id.ivTimer);
        mTvShareDevices = (TextView) findViewById(R.id.tvShareDevices);
        mIvCountDowm = (ImageView) findViewById(R.id.ivCountDowm);
        mTvDevicesLog = (TextView) findViewById(R.id.tvDevicesLog);

    }


    @Override
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        L.e("设备界面回调：" + device.getMacAddress());

    }

    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
        L.e("设备界面回调数据：" + dataMap.toString());
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS && dataMap.get("data") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            for (String dataKey : map.keySet()) {
                if (dataKey.equals(FLAG_ONOFF)) {
                    isOpenoff = (Boolean) map.get(dataKey);
                }
            }
            mHandler.sendEmptyMessage(CODE_HANDLER_UPADATA_UI);
        }
    }

    @Override
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
        L.e("设备界面回调数据 netStatus：" + netStatus);
        showAlerDialog(netStatus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDevice.setListener(null);
        mDevice.setSubscribe(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbSwitch:
                if (mCbSwitch.isChecked()) {
                    sendCommand(FLAG_ONOFF, true);
                } else {
                    sendCommand(FLAG_ONOFF, false);
                }
                break;
        }
    }

    public void sendCommand(String key1, Object value1) {
        if (value1 == null) {
            return;
        }
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key1, value1);
        mDevice.write(hashMap, 0);
    }




}
