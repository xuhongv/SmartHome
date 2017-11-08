package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.view.SeekBarColorPicker;

import java.util.concurrent.ConcurrentHashMap;

public class SmartLightActivity extends BaseDevicesControlActivity implements View.OnClickListener {


    //数据点
    private static final String KEY_LED_COLOR = "LED_Color";
    private static final String KEY_LIGHT_R = "LED_R";
    private static final String KEY_LIGHT_B = "LED_B";
    private static final String KEY_LIGHT_G = "LED_G";


    private static final String DATA_TIME_ON_MINUTE = "timesOpen";
    private static final String DATA_TIME_OFF_MINUTE = "timesOff";


    private static final int CODE_HANDLER_UPADATA_UI = 102;


    //数据点临时存储的数值
    private boolean tempSwitch = false;
    private int tempLightRed = 0;
    private int tempLightGreen = 0;
    private int tempLightBlue = 0;


    //ui
    private com.xuhong.smarthome.view.SeekBarColorPicker mMSeekbarColorPicker;
    private CheckBox mCbSwitch;
    private CheckBox mCbBlue;
    private CheckBox mCbPurple;
    private CheckBox mCbPink;
    private CheckBox mCbTimer;
    private CheckBox mCbThree;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_HANDLER_UPADATA_UI) {
                mMSeekbarColorPicker.setColorByInt(Color.argb(255, tempLightRed, tempLightGreen, tempLightBlue));

                if (tempLightRed == 0 && tempLightGreen == 0 && tempLightBlue == 0) {
                    mCbSwitch.setChecked(true);
                } else {
                    mCbSwitch.setChecked(false);
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

        //色环
        mMSeekbarColorPicker = (com.xuhong.smarthome.view.SeekBarColorPicker) findViewById(R.id.mSeekbarColorPicker);
        mMSeekbarColorPicker.setGizwitLight(true);
        mMSeekbarColorPicker.setSeekBarColorPickerChangeListener(new SeekBarColorPicker.SeekBarColorPickerChangeListener() {
            @Override
            public void onProgressChange(SeekBarColorPicker seekBarColorPicker, int color, String htmlColor) {
                cColor(color);
            }
        });


        mCbSwitch = (CheckBox) findViewById(R.id.cbSwitch);
        mCbSwitch.setOnClickListener(this);

        mCbBlue = (CheckBox) findViewById(R.id.cbYellow);
        mCbBlue.setOnClickListener(this);
        mCbPurple = (CheckBox) findViewById(R.id.cbPurple);
        mCbPurple.setOnClickListener(this);
        mCbPink = (CheckBox) findViewById(R.id.cbPink);
        mCbPink.setOnClickListener(this);

        mCbTimer = (CheckBox) findViewById(R.id.cbTimer);
        mCbTimer.setOnClickListener(this);
        mCbThree = (CheckBox) findViewById(R.id.cbThree);
        mCbThree.setOnClickListener(this);

    }


    public void cColor(int color) {
        tempLightRed = Color.red(color);
        tempLightGreen = Color.green(color);
        tempLightBlue = Color.blue(color);
        sendRgbCmd(KEY_LIGHT_R, tempLightRed, KEY_LIGHT_G, tempLightGreen, KEY_LIGHT_B, tempLightBlue);
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

                if (dataKey.equals(KEY_LIGHT_B)) {
                    tempLightBlue = (Integer) map.get(dataKey);
                }

                if (dataKey.equals(KEY_LIGHT_G)) {
                    tempLightGreen = (Integer) map.get(dataKey);
                }

                if (dataKey.equals(KEY_LIGHT_R)) {
                    tempLightRed = (Integer) map.get(dataKey);
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
                    sendRgbCmd(KEY_LIGHT_R, 0, KEY_LIGHT_G, 0, KEY_LIGHT_B, 0);
                } else {
                    sendRgbCmd(KEY_LIGHT_R, 155, KEY_LIGHT_G, 155, KEY_LIGHT_B, 155);
                }
                break;
            case R.id.cbPurple:
                sendCommand(KEY_LED_COLOR, 2);
                break;
            case R.id.cbYellow:
                sendCommand(KEY_LED_COLOR, 1);
                break;
            case R.id.cbPink:
                sendCommand(KEY_LED_COLOR, 3);
                break;
        }

    }

    public void sendCommand(String key1, Object value1) {
        if (value1 == null) {
            return;
        }
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>();
        hashMap.put(key1, value1);
        mDevice.write(hashMap, 0);
    }

}
