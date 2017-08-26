package com.xuhong.smarthome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.DevicesControlActivity.SmartSocketActivity;
import com.xuhong.smarthome.adapter.DevicesListAdapter;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class DevicesFragment extends BaseFragment {


    private Toolbar toolbarl;

    private RecyclerView rVMyDevices;
    private DividerItemDecoration dividerItemDecoration;

    private DevicesListAdapter adapter;

    private boolean isFirstBind = false;



    //设备列表
    private List<GizWifiDevice> deviceslist = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), toolbarl);
        toolbarl.inflateMenu(R.menu.menu_devices_add);
        toolbarl.setOverflowIcon(getActivity().getResources().getDrawable(R.drawable.ic_toolbar_devices_add));
        toolbarl.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_devices;
    }

    @Override
    protected void initView(View view) {
        toolbarl = (Toolbar) view.findViewById(R.id.toolbar);
        rVMyDevices = (RecyclerView) view.findViewById(R.id.rVMyDevices);
    }

    @Override
    protected void initData() {
        dividerItemDecoration = new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL);
        deviceslist = GizWifiSDK.sharedInstance().getDeviceList();
        for (GizWifiDevice gizWifiDevice : deviceslist) {
            isFirstBind = false;
            String productKey = gizWifiDevice.getProductKey();
            if (productKey.equals("71b4ebd7f42d4985992734a9d82acda8")) {
                gizWifiDevice.setSubscribe("b67d3b74b7f34e5b8acb0f468f7870cd", true);
            } else {
                gizWifiDevice.setSubscribe(true);
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new DevicesListAdapter(getActivity(), deviceslist);
        rVMyDevices.setLayoutManager(linearLayoutManager);
        rVMyDevices.addItemDecoration(dividerItemDecoration);
        rVMyDevices.setAdapter(adapter);
        adapter.setOnItemClickListener(new DevicesListAdapter.OnItemClickListener() {
            @Override
            public void onClick(GizWifiDevice mGizWifiDevice) {
                isFirstBind = true;
                mGizWifiDevice.setListener(gizWifiDeviceListener);
                String productKey = mGizWifiDevice.getProductKey();
                if (productKey.equals("71b4ebd7f42d4985992734a9d82acda8")) {
                    mGizWifiDevice.setSubscribe("b67d3b74b7f34e5b8acb0f468f7870cd", true);
                } else {
                    mGizWifiDevice.setSubscribe(true);
                }


            }
        });
        adapter.setOnItemLongClickListener(new DevicesListAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(GizWifiDevice mGizWifiDevice) {

            }
        });
    }

    private void didDevicesCloudBack(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
        deviceslist.clear();
        deviceslist.addAll(deviceList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次返回都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
    }
    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        /** 用于设备列表 */
        @Override
        public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
            didDevicesCloudBack(result, deviceList);
        }

        /** 用于用户匿名登录 */
        public void didUserLogin(GizWifiErrorCode result, java.lang.String uid, java.lang.String token) {
        }

        /** 用于设备解绑 */
        public void didUnbindDevice(GizWifiErrorCode result, java.lang.String did) {
        }

        /** 用于设备绑定 */
        public void didBindDevice(GizWifiErrorCode result, java.lang.String did) {
        }

        /** 用于设备绑定（旧） */
        public void didBindDevice(int error, String errorMessage, String did) {
        }

        /** 用于绑定推送 */
        public void didChannelIDBind(GizWifiErrorCode result) {
        }

    };

    private GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {
        @Override
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
//            L.e("isFirstBind:"+isFirstBind);
//            L.e("isFirstBind:"+device.getNetStatus());
//            L.e("isFirstBind:"+GizWifiErrorCode.GIZ_SDK_SUCCESS);
//
            if (GizWifiDeviceNetStatus.GizDeviceOffline == device.getNetStatus()){
                return;
            }

            if (isFirstBind  && GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
                Intent intent = new Intent(getActivity(), SmartSocketActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", device);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };
}


