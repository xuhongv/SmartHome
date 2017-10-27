package com.xuhong.smarthome.activity.ConfigActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gyf.barlibrary.ImmersionBar;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.romainpiel.shimmer.ShimmerTextView;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.BaseActivity;
import com.xuhong.smarthome.adapter.mPagerAdapter;
import com.xuhong.smarthome.constant.Constant;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.NetStatusUtil;
import com.xuhong.smarthome.utils.SharePreUtils;
import com.xuhong.smarthome.utils.ToastUtils;
import com.xuhong.smarthome.view.GifView;
import com.xuhong.smarthome.view.NoScollViewPager;
import com.xuhong.smarthome.view.RippleView;
import com.xuhong.smarthome.view.StepsView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.xuhong.smarthome.constant.Constant.SoftAP_Start;

public class AirLinkAddDevicesActivity extends BaseActivity implements View.OnClickListener {


    private StepsView stepsView;
    private ShimmerTextView shimmer_tv;
    private ShimmerButton btnNext, btnNextReady;
    private Shimmer shimmer;
    private CheckBox mCheckBox;
    private TextView tv_Message, tv_Progress, tvShow;
    private RippleView mRippleView;

    private EditText etSSID, etPsw;
    private String workSSID, workSSIDPsw;

    private NoScollViewPager mViewPager;
    private List<View> viewList;

    private mPagerAdapter mPagerAdapter;

    private List<GizWifiGAgentType> modeList, modeDataList;

    //是否接收wifiSDk的监听回报
    private boolean isRecieveWifiEvent = false;


    private static final int HANDLER_CODE_PROGRESS = 102;
    private static final int HANDLER_CODE_SUCCESSFUL = 103;
    private static final int HANDLER_CODE_FAILED = 104;
    private static final int REQUEST_CODE = 105;
    private static final int REQUEST_SUCCEED_CODE = 106;
    private int Flag = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_CODE_PROGRESS:
                    if (Flag < 100) {
                        Flag++;
                        tv_Progress.setText(Flag + "%");
                        if (Flag == 30) {
                            tvShow.setText("正在尝试与设备连接....");
                        }
                        mHandler.sendEmptyMessageDelayed(HANDLER_CODE_PROGRESS, 600);
                    } else {
                        mRippleView.stopRippleAnimation();
                        tvShow.setText("连接失败~");
                    }
                    break;


                //airlink配置失败回调
                case HANDLER_CODE_FAILED:

                    ToastUtils.showToast(AirLinkAddDevicesActivity.this, msg.obj.toString());

                    new SweetAlertDialog(AirLinkAddDevicesActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("温馨提示")
                            .setContentText("配置失败，尝试手动配置吧？")
                            .setConfirmText("好的")
                            .setCancelText("不了")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    ToastUtils.showToast(AirLinkAddDevicesActivity.this, "抱歉，softAP模式配网暂未加入！");
                                    finish();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                }
                            })
                            .show();
                    break;

                case HANDLER_CODE_SUCCESSFUL:

                    new SweetAlertDialog(AirLinkAddDevicesActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("温馨提示")
                            .setContentText("恭喜，配置成功！")
                            .setConfirmText("OK")

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })

                            .show();


                    break;

                case REQUEST_SUCCEED_CODE:
                    showWifiListDialog();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arilink_add_devices);
        initView();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
    }

    private void initData() {

        if (NetStatusUtil.isWifiConnected(this)) {
            //获取当前已经连接的Wi-Fi
            workSSID = NetStatusUtil.getCurentWifiSSID(this);
        } else {
            workSSID = "请先连接到Wi-Fi网络!";
            etSSID.setFocusable(false);
        }
        etSSID.setText(workSSID);

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

        //主页面view
        stepsView = (StepsView) findViewById(R.id.stepsView);
        stepsView.setTitle(new String[]{"输入wifi密码", "确认设备状态", "搜索设备", "配网成功"});
        stepsView.setmUnDoneColor(getResources().getColor(R.color.black5));
        stepsView.setmDoneColor(getResources().getColor(R.color.yellow0));

        shimmer_tv = (ShimmerTextView) findViewById(R.id.shimmer_tv);

        mViewPager = (NoScollViewPager) findViewById(R.id.mViewPager);
        viewList = new ArrayList<>();


        View View1 = getLayoutInflater().inflate(R.layout.viewpager_add_devices_one, null);
        View View2 = getLayoutInflater().inflate(R.layout.viewpager_add_devices_two, null);
        View View3 = getLayoutInflater().inflate(R.layout.viewpager_add_devices_three, null);
        View View4 = getLayoutInflater().inflate(R.layout.viewpager_add_devices_four, null);
        viewList.add(View1);
        viewList.add(View2);
        viewList.add(View3);
        viewList.add(View4);

        mPagerAdapter = new mPagerAdapter(viewList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPagingEnabled(false);


        //第一个view

        btnNext = (ShimmerButton) View1.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        shimmer = new Shimmer();
        shimmer.start(shimmer_tv);
        shimmer.start(btnNext);

        etSSID = (EditText) View1.findViewById(R.id.etSSID);
        etPsw = (EditText) View1.findViewById(R.id.etPsw);


        if (SharePreUtils.getString(AirLinkAddDevicesActivity.this, Constant.WIFI_PW, null) != null) {

            if (SharePreUtils.getString(AirLinkAddDevicesActivity.this, Constant.WIFI_NAME, null) != null) {

                if (!SharePreUtils.getString(AirLinkAddDevicesActivity.this, Constant.WIFI_NAME, null).equals(NetStatusUtil.getCurentWifiSSID(this))) {
                    etPsw.setText("");
                }

            }
            etPsw.setText(SharePreUtils.getString(AirLinkAddDevicesActivity.this, Constant.WIFI_PW, null));
        }


        //密码显示操作
        CheckBox cbLaws = (CheckBox) View1.findViewById(R.id.cbLaws);
        cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
            }
        });

        View1.findViewById(R.id.imgWiFiList).setOnClickListener(this);

        //第二个view
        GifView gifView = (GifView) View2.findViewById(R.id.GifView);
        gifView.setMovieResource(R.drawable.airlink);

        btnNextReady = (ShimmerButton) View2.findViewById(R.id.btnNextReady);
        btnNextReady.setClickable(false);
        btnNextReady.setBackground(getResources().getDrawable(R.drawable.img_bg_black_shape));


        mCheckBox = (CheckBox) View2.findViewById(R.id.checkBox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    btnNextReady.setBackground(getResources().getDrawable(R.drawable.img_bg_shape));
                    btnNextReady.setClickable(true);
                    btnNextReady.setOnClickListener(AirLinkAddDevicesActivity.this);
                } else {
                    btnNextReady.setClickable(false);
                    btnNextReady.setBackground(getResources().getDrawable(R.drawable.img_bg_black_shape));
                }
            }
        });

        tv_Message = (TextView) View2.findViewById(R.id.tv_Message);
        tv_Message.setOnClickListener(this);

        //第三个View
        tv_Progress = (TextView) View3.findViewById(R.id.tvShowProgress);
        tvShow = (TextView) View3.findViewById(R.id.tvShow);

        mRippleView = (RippleView) View3.findViewById(R.id.mRippleView);
        mRippleView.setAnimationProgressListener(new RippleView.AnimationListener() {
            @Override
            public void startAnimation() {
                mHandler.sendEmptyMessage(HANDLER_CODE_PROGRESS);
                startAirlink();

            }
            @Override
            public void EndAnimation() {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //下一步
            case R.id.btnNext:

                if (workSSID.contains("请先连接到Wi-Fi网络!")) {
                    ToastUtils.showToast(AirLinkAddDevicesActivity.this, "请先连接到Wi-Fi网络，再点击下一步！");
                    break;
                }

                workSSIDPsw = etPsw.getText().toString();
                workSSID = etSSID.getText().toString().intern();
                L.e("etSSID:" + workSSID);
                SharePreUtils.putString(AirLinkAddDevicesActivity.this, Constant.WIFI_PW, workSSIDPsw);
                SharePreUtils.putString(AirLinkAddDevicesActivity.this, Constant.WIFI_NAME, workSSID);


                if (etPsw.getText().toString().isEmpty()) {
                    new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("温馨提示")
                            .setContentText("您确认该Wf-Fi的密码为空!")
                            .setConfirmText("确定")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    mViewPager.setCurrentItem(1, true);
                                    stepsView.next();
                                }
                            })
                            .show();
                    return;
                }
                mViewPager.setCurrentItem(1, true);
                stepsView.next();

                break;


            case R.id.tv_Message:
                if (mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                    btnNextReady.setClickable(false);
                    btnNextReady.setBackground(getResources().getDrawable(R.drawable.img_bg_black_shape));

                } else {
                    mCheckBox.setChecked(true);
                    btnNextReady.setBackground(getResources().getDrawable(R.drawable.img_bg_shape));
                    btnNextReady.setClickable(true);
                    btnNextReady.setOnClickListener(AirLinkAddDevicesActivity.this);
                }

                break;
            case R.id.btnNextReady:

                mViewPager.setCurrentItem(2, true);
                stepsView.next();
                mRippleView.startRippleAnimation();

                break;

            //显示wifi名字列表
            case R.id.imgWiFiList:
                checkPermission();
                break;
        }
    }

    // 拦截系统的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("警告")
                    .setContentText("您确认放弃配置吗!")
                    .setConfirmText("放弃")
                    .setCancelText("不了")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            finish();
                        }
                    })
                    .show();
            return true;
        }
        return false;
    }


    private void startAirlink() {

       //L.e("startAirlink start!");

        isRecieveWifiEvent = true;

        modeDataList = new ArrayList<>();
        modeList = new ArrayList<>();

        modeDataList.add(GizWifiGAgentType.GizGAgentESP);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP);
        modeDataList.add(GizWifiGAgentType.GizGAgentHF);
        modeDataList.add(GizWifiGAgentType.GizGAgentRTK);
        modeDataList.add(GizWifiGAgentType.GizGAgentWM);
        modeDataList.add(GizWifiGAgentType.GizGAgentQCA);
        modeDataList.add(GizWifiGAgentType.GizGAgentTI);
        modeDataList.add(GizWifiGAgentType.GizGAgentFSK);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP3);
        modeDataList.add(GizWifiGAgentType.GizGAgentBL);
        modeDataList.add(GizWifiGAgentType.GizGAgentAtmelEE);
        modeDataList.add(GizWifiGAgentType.GizGAgentOther);


        //这里我仅仅用ESp8266模块,所以固定为第一个
        modeList.add(modeDataList.get(0));

        L.e("配置的etSSID:" + workSSID);
        L.e("配置的etPas:" + workSSIDPsw);

        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
        //开始配置
        GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw,
                GizWifiConfigureMode.GizWifiAirLink, null, 60, modeList);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GizWifiSDK.sharedInstance().setListener(null);
        isRecieveWifiEvent = false;

    }

    private void showWifiListDialog() {


        List<ScanResult> rsList = NetStatusUtil.getCurrentWifiScanResult(this);
        List<String> localList = new ArrayList<>();
        List<ScanResult> resultsList = new ArrayList<>();

        for (ScanResult sss : rsList) {
            if (!sss.SSID.contains(SoftAP_Start)) {
                if (!localList.toString().contains(sss.SSID)) {
                    localList.add(sss.SSID);
                    resultsList.add(sss);
                }
            }
        }

        final ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();

        for (int i = 0; i < resultsList.size(); i++) {
            mMenuItems.add(new DialogMenuItem(resultsList.get(i).SSID, R.drawable.wifi_icon));
        }

        final NormalListDialog dialog = new NormalListDialog(AirLinkAddDevicesActivity.this, mMenuItems);

        dialog.title("请选择WI-Fi名字")//
                .titleTextSize_SP(14)//
                .titleBgColor(getResources().getColor(R.color.yellow0))//
                .itemPressColor(getResources().getColor(R.color.yellow0))//
                .itemTextColor(getResources().getColor(R.color.black0))//
                .itemTextSize(12)//
                .cornerRadius(0)//
                .widthScale(0.8f)//
                .show(R.style.myDialogAnim);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSSID.setText(mMenuItems.get(position).mOperName);
                dialog.dismiss();
            }
        });
    }

    private void checkPermission() {
        //是否大于6.0版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否已经授权
            int Code_ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int Code_ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            //授权结果判断
            if (Code_ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED && Code_ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            } else {
                mHandler.sendEmptyMessage(REQUEST_SUCCEED_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                List<String> deniedPermission = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    String permission = permissions[i];
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermission.add(permission);
                    }
                }
                L.e("deniedPermission:" + deniedPermission);
                if (deniedPermission.isEmpty()) {
                    mHandler.sendEmptyMessage(105);
                } else {
                    mHandler.sendEmptyMessage(105);
                }

            }
        }
    }

    /**
     * 配网模式回调
     */
    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {


        // 设备配置回调
        @Override
        public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
            super.didDiscovered(result, deviceList);

            L.e("gizWifiSDKListener new !!!!" + result.getResult());

            if (GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING == result) {
                return;
            }

            if (mRippleView != null) {
                mRippleView.stopRippleAnimation();
            }

            Message message = mHandler.obtainMessage();

            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                message.what = HANDLER_CODE_SUCCESSFUL;
            } else {
                message.what = HANDLER_CODE_FAILED;
                message.obj = toastError(result);
            }

            mHandler.sendMessage(message);
        }

    };
}
