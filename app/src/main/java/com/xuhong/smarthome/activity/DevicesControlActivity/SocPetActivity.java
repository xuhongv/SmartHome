package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.graphics.Color;
import android.os.Message;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.view.ColorCircularSeekBar;
import com.xuhong.smarthome.view.ColorTempCircularSeekBar;
import com.xuhong.smarthome.view.MotorControlView;
import com.xuhong.smarthome.view.TemperatureView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

public class SocPetActivity extends BaseDevicesControlActivity {


    //色彩进度条
    private ColorCircularSeekBar circularSeekBar;

    //色温进度条
    private ColorTempCircularSeekBar colorTempCircularSeekBar;

    //湿度
    private cn.fanrunqi.waveprogress.WaveProgressView mWaveLoadingView;
    private TextView mTvHumidness;

    //温度
    private TemperatureView mTemperatureView;
    private TextView mTvTemper;

    //马达
    private MotorControlView mMotorView;
    private Button mBtMotoReduce;
    private Button mBtMotorAdd;

    //灯的开关
    private Switch mSwLight;

    //红绿蓝亮度调节
    private SeekBar mSbRed;
    private SeekBar mSbGreen;
    private SeekBar mSbBlue;

    //按钮红绿蓝开关
    private Button mBtRed;
    private Button mBtGreen;
    private Button mBtBlue;

    //红外
    private com.xuhong.smarthome.view.InfraredView mMInfraredView;


    //数据点相关
    private static final String KEY_SWITCH = "LED_OnOff";
    private static final String KEY_LIGHT_R = "LED_R";
    private static final String KEY_LIGHT_B = "LED_B";
    private static final String KEY_LIGHT_G = "LED_G";


    //数据点临时存储的数值
    private boolean tempSwitch = false;
    private int tempLightRed = 0;
    private int tempLightGreen = 0;
    private int tempLightBlue = 0;


    //Code
    private static final int CODE_HANDLER_UI = 105;


    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CODE_HANDLER_UI:
                    updateColor();
                    updateColortemp();
                    break;
            }

        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_soc_pet;
    }

    @Override
    protected void bindView() {

        //色彩的进度条
        circularSeekBar = (ColorCircularSeekBar) findViewById(R.id.csbSeekbar2);
        circularSeekBar.postInvalidateDelayed(2000);
        circularSeekBar.setMaxProgress(100);
        circularSeekBar.setProgress(30);
        circularSeekBar.setMProgress(0);
        circularSeekBar.postInvalidateDelayed(100);
        circularSeekBar.setSeekBarChangeListener(new ColorCircularSeekBar.OnSeekChangeListener() {
            @Override
            public void onProgressChange(ColorCircularSeekBar view, int color) {
                cColor(color);
            }

        });
        //色温的进度条
        colorTempCircularSeekBar = (ColorTempCircularSeekBar) findViewById(R.id.csbSeekbar);
        colorTempCircularSeekBar.postInvalidateDelayed(2000);
        colorTempCircularSeekBar.setMaxProgress(100);
        colorTempCircularSeekBar.setProgress(30);
        colorTempCircularSeekBar.setMProgress(0);
        colorTempCircularSeekBar.postInvalidateDelayed(100);
        colorTempCircularSeekBar.setSeekBarChangeListener(new ColorTempCircularSeekBar.OnSeekChangeListener() {

            @Override
            public void onProgressChange(ColorTempCircularSeekBar view, int color) {
                cColorTemp(color);
            }

        });


        //湿度
        mWaveLoadingView = (cn.fanrunqi.waveprogress.WaveProgressView) findViewById(R.id.WaveLoadingView);
        mWaveLoadingView.setCurrent(28, "28%/良好");


        //温度
        mTemperatureView = (TemperatureView) findViewById(R.id.temperatureView);

        //rgb灯
        mSwLight = (Switch) findViewById(R.id.swLight);
        mSwLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        //马达
        List<Integer> integerList = new ArrayList<>();
        for (int i = -5; i < 6; i++) {
            integerList.add(i);
        }

        mMotorView = (MotorControlView) findViewById(R.id.mMotorView);
        mMotorView.setValueList(integerList);
        mMotorView.setValue(5, null, 500);


    }

    /* 色彩*/
    public void cColor(int color) {

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        if (red == 255) {
            red = 254;
        }
        if (green == 255) {
            green = 254;
        }
        if (blue == 255) {
            blue = 254;
        }


        sendRgbCmd("LED_R", red,
                "LED_G", green, "LED_B", blue);

//        data_R = Color.red(color);
//        color_num_g = Color.green(color);
//        color_num_b = Color.blue(color);
    }

    /* 色温*/
    public void cColorTemp(int color_num) {
        sendRgbCmd("Temperature_R", Color.red(color_num), "Temperature_G", Color.green(color_num), "Temperature_B", Color.blue(color_num));

//        color_num_temp_r = Color.red(color_num);
//        color_num_temp_g = Color.green(color_num);
//        color_num_temp_b = Color.blue(color_num);
    }

    /*
     * 更新色彩
	 */
    private void updateColor() {
        circularSeekBar.setInnerColor(Color.argb(255, tempLightRed, tempLightGreen, tempLightBlue));

    }

    /*
     * 更新色温
     */
    private void updateColortemp() {
        colorTempCircularSeekBar.setInnerColor(Color.argb(255, tempLightRed, tempLightGreen, tempLightBlue));

    }


    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
        super.didReceiveData(result, device, dataMap, sn);
        L.e("收到的数据：" + dataMap);

        if (dataMap.get("data") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            for (String dataKey : map.keySet()) {
                if (dataKey.equals(KEY_SWITCH)) {
                    tempSwitch = (Boolean) map.get(dataKey);
                }

                if (dataKey.equals(KEY_LIGHT_R)) {
                    tempLightRed = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_LIGHT_G)) {
                    tempLightGreen = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_LIGHT_B)) {
                    tempLightBlue = (Integer) map.get(dataKey);
                }

            }
        }

    }
}
