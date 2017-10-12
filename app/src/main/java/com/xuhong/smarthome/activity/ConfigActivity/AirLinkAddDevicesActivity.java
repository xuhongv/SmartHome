package com.xuhong.smarthome.activity.ConfigActivity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.romainpiel.shimmer.ShimmerTextView;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.BaseActivity;
import com.xuhong.smarthome.adapter.mPagerAdapter;
import com.xuhong.smarthome.constant.Constant;
import com.xuhong.smarthome.utils.NetStatusUtil;
import com.xuhong.smarthome.utils.SharePreUtils;
import com.xuhong.smarthome.utils.ToastUtils;
import com.xuhong.smarthome.view.GifView;
import com.xuhong.smarthome.view.NoScollViewPager;
import com.xuhong.smarthome.view.StepsView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AirLinkAddDevicesActivity extends BaseActivity implements View.OnClickListener {

    private StepsView stepsView;
    private ShimmerTextView shimmer_tv;
    private ShimmerButton btnNext;
    private Shimmer shimmer;


    private EditText etSSID, etPsw;
    private String currentSSID;

    private NoScollViewPager mViewPager;
    private List<View> viewList;

    private mPagerAdapter mPagerAdapter;


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
    }

    private void initData() {

        if (NetStatusUtil.isWifiConnected(this)) {
            //获取当前已经连接的Wi-Fi
            currentSSID = NetStatusUtil.getCurentWifiSSID(this);
        } else {
            currentSSID = "请先连接到Wi-Fi网络!";
            etSSID.setFocusable(false);
        }

        String savaWifiSSid = SharePreUtils.getString(this, Constant.WIFI_NAME, null);

        //判断是否
        if (savaWifiSSid != null && savaWifiSSid.equals(currentSSID)) {


        }
        etSSID.setText(currentSSID);

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
        viewList.add(View1);
        viewList.add(View2);
        viewList.add(View3);

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

        //第二个view
        GifView gifView = (GifView) View2.findViewById(R.id.GifView);
        gifView.setMovieResource(R.drawable.airlink);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //下一步
            case R.id.btnNext:

                if (currentSSID.contains("请先连接到Wi-Fi网络!")) {
                    ToastUtils.showToast(AirLinkAddDevicesActivity.this, "请先连接到Wi-Fi网络，再点击下一步！");
                    break;
                }

                if (etPsw.getText().toString().isEmpty()) {

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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

                }
                SharePreUtils.putString(AirLinkAddDevicesActivity.this, Constant.WIFI_NAME, currentSSID);

                break;
        }
    }

    // 拦截系统的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("警告")
                    .setContentText("您确认放弃配置吗!")
                    .setConfirmText("不了")
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
}
