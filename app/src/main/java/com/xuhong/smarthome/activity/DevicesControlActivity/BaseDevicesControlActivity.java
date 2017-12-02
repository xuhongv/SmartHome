package com.xuhong.smarthome.activity.DevicesControlActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gyf.barlibrary.ImmersionBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.BaseActivity;
import com.xuhong.smarthome.activity.ConfigActivity.AirLinkAddDevicesActivity;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.SoftInputUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.github.xudaojie.qrcodelib.CaptureActivity;


public abstract class BaseDevicesControlActivity extends BaseActivity {


    //设备不可控弹窗
    private AlertDialog dialog;

    //设备
    public GizWifiDevice mDevice;
    private TextView tvName;

    private SweetAlertDialog pDialog;


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
        tvName = (TextView) toolbar.findViewById(R.id.tvName);

        toolbar.inflateMenu(R.menu.menu_devices_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //设备重命名
                    case R.id.menu_Devices_Rename:
                        showRenameDialog(mDevice);
                        break;
                    //设备详情
                    case R.id.menu_Devices_Details:
                        showDialogDevicesInf(mDevice);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        ImmersionBar.setTitleBar(this, toolbar);

        Intent intent = getIntent();
        mDevice = intent.getParcelableExtra("GizWifiDevice");
        mDevice.setListener(gizWifiDeviceListener);
        tvName.setText(Objects.equals(mDevice.getAlias(), "") || mDevice.getAlias() == null ? mDevice.getProductName() : mDevice.getAlias());
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("同步设备状态中...");
        pDialog.setCancelable(false);
        pDialog.show();


        getStatusOfDevice();
    }


    //重命名
    private void showRenameDialog(final GizWifiDevice mGizWifiDevice) {

        final View view = getLayoutInflater().inflate(R.layout.dialog_rename, null);
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(BaseDevicesControlActivity.this)
                .setView(view)
                .show();

        final EditText rename_et = (EditText) dialog.findViewById(R.id.rename_et);
        SoftInputUtils.showSoftInput(BaseDevicesControlActivity.this);

        dialog.findViewById(R.id.tv_cancel_rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果用户没有输入文字直接点击取消就关闭
                if (rename_et.getText().toString().isEmpty()) {
                    dialog.dismiss();
                    return;
                }
                dialog.dismiss();
                hideKeyBoard();
            }
        });

        dialog.findViewById(R.id.tv_sure_rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = rename_et.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(BaseDevicesControlActivity.this, "输入不能为空!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                mGizWifiDevice.setCustomInfo(null, name);
                hideKeyBoard();
                dialog.dismiss();

            }
        });
    }


    //获取设备信息
    private void showDialogDevicesInf(GizWifiDevice mGizWifiDevice) {

        BaseAnimatorSet mBasIn = new BounceTopEnter();
        BaseAnimatorSet mBasOut = new SlideBottomExit();

        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.btnNum(1)
                .content("ProductKey:" + mGizWifiDevice.getProductKey()
                        + "\n MacAddress:" + mGizWifiDevice.getMacAddress()
                        + "\n ProductName:" + mGizWifiDevice.getProductName()
                        + "\n IPAddress:" + mGizWifiDevice.getIPAddress()
                        + "\n NetStatus:" + mGizWifiDevice.getNetStatus()
                )
                .btnText("确定")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        });
    }


    private void hideKeyBoard() {
        // 隐藏键盘
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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


        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);

        sweetAlertDialog.setTitleText("温馨提示");
        sweetAlertDialog.setContentText(mTitle);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                finish();
            }
        });
        sweetAlertDialog.show();


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

            if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
                pDialog.dismissWithAnimation();
            }
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
        L.e("设备订阅回调 didSetSubscribe result:" + result);
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
        L.e("设备状态回调 didReceiveData result: " + result);
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            pDialog.dismissWithAnimation();
        } else {
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
        }
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
        L.e(" 修改设备信息回调:" + result);
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            tvName.setText(Objects.equals(mDevice.getAlias(), "") || mDevice.getAlias() == null ? mDevice.getProductName() : mDevice.getAlias());
        }
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
        L.d("发送Rgb数据：" + hashMap.toString());
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
            pDialog.dismissWithAnimation();

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
