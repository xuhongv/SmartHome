package com.xuhong.smarthome.activity.DevicesControlActivity;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.xuhong.smarthome.activity.BaseActivity;


public abstract class BaseDevicesControlActivity extends BaseActivity {


   GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

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
    }


}
