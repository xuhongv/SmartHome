package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.view.ColorCircularSeekBar;
import com.xuhong.smarthome.view.InfraredView;
import com.xuhong.smarthome.view.MotorControlView;
import com.xuhong.smarthome.view.TemperatureView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SocPetActivity extends BaseDevicesControlActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    //色彩
    private ColorCircularSeekBar circularSeekBar;

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
    private RelativeLayout rlMotorAdd, rlMotorReduce;

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
    private static final String KEY_HUMIDITY = "Humidity";
    private static final String KEY_MOTOR = "Motor_Speed";
    private static final String KEY_INFRARED = "Infrared";
    private static final String KEY_LED_COLOR = "LED_Color";
    private static final String KEY_TEMPERTURE = "Temperature";


    //数据点临时存储的数值
    private boolean tempSwitch = false;
    private int tempLightRed = 0;
    private int tempLightGreen = 0;
    private int tempLightBlue = 0;
    private int tempTemperture = 0;
    private int tempHumidity = 0;
    private boolean tempIsInfrared = false;
    private int tempMotorSpeed = 0;


    //Code
    private static final int CODE_HANDLER_UI = 105;


    @SuppressLint("HandlerLeak")
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_HANDLER_UI:
                    updataUI();
                    break;
            }

        }
    };
    private List<Integer> integerList;

    //更新UI
    private void updataUI() {


        if (tempLightRed == 254) {
            tempLightRed = 255;

        }
        if (tempLightGreen == 254) {
            tempLightGreen = 255;

        }
        if (tempLightBlue == 254) {
            tempLightBlue = 255;
        }


        circularSeekBar.setInnerColor(Color.argb(255, tempLightRed, tempLightGreen, tempLightBlue));
        mSbRed.setProgress(tempLightRed);
        mSbGreen.setProgress(tempLightGreen);
        mSbBlue.setProgress(tempLightBlue);
        mSwLight.setChecked(tempSwitch);

        mTemperatureView.setProgress(tempTemperture + 40);
        mTvTemper.setText("当前温度：" + tempTemperture + "°");

        //湿度
        if (tempHumidity < 50) {
            mWaveLoadingView.setCurrent(tempHumidity, "干燥！");
            mWaveLoadingView.setMaxProgress(100);
            mWaveLoadingView.setWaveColor("#F08B88");
        } else if (tempHumidity > 60) {
            mWaveLoadingView.setCurrent(tempHumidity, "舒适！");
            mWaveLoadingView.setWaveColor("#45C01A");
        } else {
            mWaveLoadingView.setCurrent(tempHumidity, "潮湿！");
            mWaveLoadingView.setWaveColor("#5be4ef");
        }

        mTvHumidness.setText("当前湿度：" + tempHumidity);

        //红外
        if (!tempIsInfrared) {
            mMInfraredView.setShowText("努力感应...");
        } else {
            mMInfraredView.setShowText("感应到了...");
        }

        //马达
        mMotorView.setValue(tempMotorSpeed, null, 500);


    }


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

        //拖动条
        mSbRed = (SeekBar) findViewById(R.id.sbRed);
        mSbRed.setOnSeekBarChangeListener(this);
        mSbGreen = (SeekBar) findViewById(R.id.sbGreen);
        mSbGreen.setOnSeekBarChangeListener(this);
        mSbBlue = (SeekBar) findViewById(R.id.sbBlue);
        mSbBlue.setOnSeekBarChangeListener(this);
        mSbRed.setMax(254);
        mSbGreen.setMax(254);
        mSbBlue.setMax(254);


        //湿度
        mWaveLoadingView = (cn.fanrunqi.waveprogress.WaveProgressView) findViewById(R.id.WaveLoadingView);
        mWaveLoadingView.setCurrent(0, "未知");


        //温度
        mTemperatureView = (TemperatureView) findViewById(R.id.temperatureView);

        //rgb灯
        mBtRed = (Button) findViewById(R.id.btYellow);
        mBtRed.setOnClickListener(this);
        mBtGreen = (Button) findViewById(R.id.btPurple);
        mBtGreen.setOnClickListener(this);
        mBtBlue = (Button) findViewById(R.id.btPink);
        mBtBlue.setOnClickListener(this);


        mSwLight = (Switch) findViewById(R.id.swLight);
        mSwLight.setOnClickListener(this);

        //马达
        integerList = new ArrayList<>();
        for (int i = -5; i < 6; i++) {
            integerList.add(i);
        }

        mMotorView = (MotorControlView) findViewById(R.id.mMotorView);
        mMotorView.setValueList(integerList);
        mMotorView.setValue(5, null, 500);


        mBtMotoReduce = (Button) findViewById(R.id.btMotoReduce);
        mBtMotorAdd = (Button) findViewById(R.id.btMotorAdd);
        mBtMotoReduce.setOnClickListener(this);
        mBtMotorAdd.setOnClickListener(this);

        rlMotorAdd = (RelativeLayout) findViewById(R.id.rlMotorAdd);
        rlMotorAdd.setOnClickListener(this);
        rlMotorReduce = (RelativeLayout) findViewById(R.id.rlMotorReduce);
        rlMotorReduce.setOnClickListener(this);


        //红外
        mMInfraredView = (InfraredView) findViewById(R.id.mInfraredView);


        //文字栏
        mTvHumidness = (TextView) findViewById(R.id.tvHumidness);
        mTvTemper = (TextView) findViewById(R.id.tvTemper);


    }

    /* 色彩*/
    public void cColor(int color) {

        tempLightRed = Color.red(color);
        tempLightGreen = Color.green(color);
        tempLightBlue = Color.blue(color);

        if (tempLightRed == 255) {
            tempLightRed = 254;
        }
        if (tempLightGreen == 255) {
            tempLightGreen = 254;
        }
        if (tempLightBlue == 255) {
            tempLightBlue = 254;
        }
        sendRgbCmd(KEY_LIGHT_R, tempLightRed, KEY_LIGHT_G, tempLightGreen, KEY_LIGHT_B, tempLightBlue);
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

                if (dataKey.equals(KEY_TEMPERTURE)) {
                    tempTemperture = (Integer) map.get(dataKey);
                }

                if (dataKey.equals(KEY_HUMIDITY)) {
                    tempHumidity = (Integer) map.get(dataKey);
                }

                if (dataKey.equals(KEY_INFRARED)) {
                    tempIsInfrared = (boolean) map.get(dataKey);
                }

                if (dataKey.equals(KEY_MOTOR)) {
                    tempMotorSpeed = (Integer) map.get(dataKey);
                }
            }


            mHandler.sendEmptyMessage(CODE_HANDLER_UI);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btYellow:
                sendCommand(KEY_LED_COLOR, 1);
                break;
            case R.id.btPink:
                sendCommand(KEY_LED_COLOR, 3);
                break;
            case R.id.btPurple:
                sendCommand(KEY_LED_COLOR, 2);
                break;
            case R.id.btMotoReduce:
            case R.id.rlMotorReduce:
                sendCommand(KEY_MOTOR, tempMotorSpeed-1);
                break;
            case R.id.btMotorAdd:
            case R.id.rlMotorAdd:
                sendCommand(KEY_MOTOR, tempMotorSpeed+1);
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.sbRed:
                sendCommand(KEY_LIGHT_R, seekBar.getProgress());
                break;
            case R.id.sbGreen:
                sendCommand(KEY_LIGHT_G, seekBar.getProgress());
                break;
            case R.id.sbBlue:
                sendCommand(KEY_LIGHT_B, seekBar.getProgress());
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
}
