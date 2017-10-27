package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.BaseActivity;
import com.xuhong.smarthome.utils.L;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseDevicesControlActivity extends BaseActivity {


    //设备不可控弹窗
    private AlertDialog dialog;

    //设备
    public GizWifiDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvName = (TextView) toolbar.findViewById(R.id.tvName);
        ImmersionBar.setTitleBar(this, toolbar);

        Intent intent = getIntent();
        mDevice = intent.getParcelableExtra("GizWifiDevice");
        mDevice.setListener(gizWifiDeviceListener);
        tvName.setText(Objects.equals(mDevice.getAlias(), "") || mDevice.getAlias() == null ? mDevice.getProductName() : mDevice.getAlias());
        getStatusOfDevice();
    }

    public void showAlerDialog(GizWifiDeviceNetStatus netStatus) {

        String mTitle = null;

        if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
            return;
        }

        if (netStatus == GizWifiDeviceNetStatus.GizDeviceOffline) {
            mTitle = "设备已离线！";
        }

        if (netStatus == GizWifiDeviceNetStatus.GizDeviceUnavailable) {
            mTitle = "设备不可控！";
        }


        final View view = getLayoutInflater().inflate(R.layout.dialog_alert, null);

        new AlertDialog.Builder(this)
                .setCancelable(false)//屏幕外点击无效
                .setView(view)
                .show();

//        RotateAnimation rotateAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setDuration(300);
//        rotateAnimation.setInterpolator(new BounceInterpolator());
//        view.startAnimation(rotateAnimation);


//        ProgressDialog dialog =new ProgressDialog(this);
//        dialog.setView(view);
//        dialog.setMessage("设备已经离线");
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.show();


    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    protected abstract void bindView();

    private GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            BaseDevicesControlActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        /** 用于获取设备状态 */
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                   java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
            BaseDevicesControlActivity.this.didReceiveData(result, device, dataMap, sn);
        }

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                       java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
            BaseDevicesControlActivity.this.didGetHardwareInfo(result, device, hardwareInfo);
        }

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
            BaseDevicesControlActivity.this.didSetCustomInfo(result, device);
        }

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            BaseDevicesControlActivity.this.didUpdateNetStatus(device, netStatus);
        }


    };


    /**
     * 设备订阅回调
     *
     * @param result       错误码
     * @param device       被订阅设备
     * @param isSubscribed 订阅状态
     */
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        L.e("设备订阅回调:" + result);
    }

    /**
     * 设备状态回调
     *
     * @param result  错误码
     * @param device  当前设备
     * @param dataMap 当前设备状态
     * @param sn      命令序号
     */
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                  java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
        L.e("设备状态回调:" + result);
    }

    /**
     * 设备硬件信息回调
     *
     * @param result       错误码
     * @param device       当前设备
     * @param hardwareInfo 当前设备硬件信息
     */
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                      java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
    }

    /**
     * 修改设备信息回调
     *
     * @param result 错误码
     * @param device 当前设备
     */
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
    }

    /**
     * 设备状态变化回调
     */
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
        L.e(" 设备状态变化回调:" + netStatus);
    }


    /**
     * 发送指令,下发单个数据点的命令可以用这个方法
     *
     * @param key   数据点对应的标识名
     * @param value 需要改变的值
     */
    protected void sendCommand(String key, Object value) {
        if (value == null) {
            return;
        }
        int sn = 5;
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>();
        hashMap.put(key, value);
        mDevice.write(hashMap, sn);
    }


    protected void sendRgbCmd(String keyR, Object valueR, String keyG, Object valueG, String keyB, Object valueB) {
        if (valueR == null || valueG == null || valueB == null) {
            return;
        }
        int sn = 5;
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>();
        hashMap.put(keyR, valueR);
        hashMap.put(keyG, valueG);
        hashMap.put(keyB, valueB);
        mDevice.write(hashMap, sn);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出页面，取消设备订阅
        mDevice.setSubscribe(false);
        mDevice.setListener(null);
    }
    /**
     * Description:页面加载后弹出等待框，等待设备可被控制状态回调，如果一直不可被控，等待一段时间后自动退出界面
     */
    private void getStatusOfDevice() {
        // 设备是否可控
        if (isDeviceCanBeControlled()) {
            // 可控则查询当前设备状态
            mDevice.getDeviceStatus();
        } else {
            // 显示等待栏
            //progressDialog.show();
            if (mDevice.isLAN()) {
                // 小循环10s未连接上设备自动退出
                //  mHandler.postDelayed(mRunnable, 10000);
            } else {
                // 大循环20s未连接上设备自动退出
                //  mHandler.postDelayed(mRunnable, 20000);
            }
        }
    }

    private boolean isDeviceCanBeControlled() {
        return mDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled;
    }


}
