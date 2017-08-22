package com.xuhong.smarthome.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.romainpiel.shimmer.ShimmerViewHelper;
import com.xuhong.smarthome.MainActivity;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.constant.Constant;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity {


    private String lasterVersion = "1.00.00";


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101) {
                Intent intent = new Intent();
                intent.putExtra("isLastVersion", isLasterVersion());
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Shimmer shimmer = new Shimmer();
        ShimmerTextView shimmer_login = (ShimmerTextView) findViewById(R.id.shimmer_login);
        shimmer.start(shimmer_login);
        getCurrentVersion();
    }

    //获取最新版本号
    private void getCurrentVersion() {

        OkHttpUtils.getInstance().sendCommon(Constant.GET_APP_VERSION, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessageDelayed(101, 3000);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        lasterVersion = jsonObject.getString("appVersion");
                        mHandler.sendEmptyMessageDelayed(101, 3000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessageDelayed(101, 3000);
                    }
                } else {
                    mHandler.sendEmptyMessageDelayed(101, 3000);
                }
            }
        });


    }

    //获取当前app版本
    private String getcurrentAppName() {
        try {
            String versionName = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
            return versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //是否为最新版本
    private boolean isLasterVersion() {
        long lastetVersionCode = versionInf(lasterVersion);
        long currentVersionCode = versionInf(getcurrentAppName());

        L.i("最新版本：" + lastetVersionCode);
        L.i("当前版本：" + currentVersionCode);
        return (lastetVersionCode - currentVersionCode) >= 0;
    }

    private long versionInf(String versionInf) {
        String[] verNums = versionInf.split("\\.");
        int mVersionNum1 = Integer.parseInt(verNums[0]);
        int mVersionNum2 = Integer.parseInt(verNums[1]);
        int mVersionNum3 = Integer.parseInt(verNums[2]);
        return mVersionNum1 * 1000 * 1000 + mVersionNum2 * 1000 + mVersionNum3;
    }
}
