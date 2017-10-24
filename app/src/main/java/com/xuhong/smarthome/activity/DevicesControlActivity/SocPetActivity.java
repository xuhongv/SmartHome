package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.graphics.Color;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.xuhong.smarthome.R;
import com.xuhong.smarthome.view.ColorCircularSeekBar;
import com.xuhong.smarthome.view.ColorTempCircularSeekBar;
import com.xuhong.smarthome.view.TemperatureView;

public class SocPetActivity extends BaseDevicesControlActivity {


    /**
     * 色彩进度条
     */
    private ColorCircularSeekBar circularSeekBar;

    /**
     * 色温进度条
     */
    private ColorTempCircularSeekBar colorTempCircularSeekBar;


    //湿度
    private cn.fanrunqi.waveprogress.WaveProgressView WaveLoadingView;


    //温度
    private TemperatureView temperatureView;


    //ui
    private Switch swRight;


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
        colorTempCircularSeekBar = (ColorTempCircularSeekBar) findViewById(R.id.csbSeekbar);//色温
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
        WaveLoadingView = (cn.fanrunqi.waveprogress.WaveProgressView) findViewById(R.id.WaveLoadingView);
        WaveLoadingView.setCurrent(28, "28%/良好");


        //温度
        temperatureView = (TemperatureView) findViewById(R.id.temperatureView);
        temperatureView.setProgress(26);


        //rgb灯
        swRight= (Switch) findViewById(R.id.swRight);
        swRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
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

//        color_num_r = Color.red(color);
//        color_num_g = Color.green(color);
//        color_num_b = Color.blue(color);
    }

    /* 色温*/
    public void cColorTemp(int color_num) {
//        sendRgbCmd("Temperature_R", Color.red(color_num),
//                "Temperature_G", Color.green(color_num), "Temperature_B", Color.blue(color_num));
//        color_num_temp_r = Color.red(color_num);
//        color_num_temp_g = Color.green(color_num);
//        color_num_temp_b = Color.blue(color_num);
    }


}
