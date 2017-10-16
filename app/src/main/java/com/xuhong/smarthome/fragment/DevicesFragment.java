package com.xuhong.smarthome.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.base.BaseDialog;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.ConfigActivity.AirLinkAddDevicesActivity;
import com.xuhong.smarthome.activity.DevicesControlActivity.SmartSocketActivity;
import com.xuhong.smarthome.adapter.DevicesListAdapter;
import com.xuhong.smarthome.utils.SharePreUtils;
import com.xuhong.smarthome.utils.SoftInputUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.xudaojie.qrcodelib.CaptureActivity;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class DevicesFragment extends BaseFragment {


    private Toolbar toolbarl;

    private RecyclerView rVMyDevices;
    private DividerItemDecoration dividerItemDecoration;

    private DevicesListAdapter adapter;

    private boolean isFirstBind = false;
    //扫描回调码
    public static final int REQUEST_QR_CODE = 105;
    //扫描之后返回的参数
    private String product_key;
    private String did;
    private String passcode;


    //设备列表
    private List<GizWifiDevice> deviceslist = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private String uid;
    private String token;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImmersionBar.setTitleBar(getActivity(), toolbarl);
        toolbarl.inflateMenu(R.menu.menu_devices_add);
        toolbarl.setOverflowIcon(getActivity().getResources().getDrawable(R.drawable.ic_toolbar_devices_add));
        toolbarl.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //扫描添加设备
                    case R.id.menu_id_scanAddDevices:
                        Intent i = new Intent(getActivity(), CaptureActivity.class);
                        startActivityForResult(i, REQUEST_QR_CODE);
                        break;
                    //手动添加设备
                    case R.id.menu_id_handAddDevices:
                        startActivity(new Intent(getActivity(), AirLinkAddDevicesActivity.class));
                        break;
                    default:
                        break;
                }
                return false;
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

        uid = SharePreUtils.getString(getActivity(), "_uid", null);
        token = SharePreUtils.getString(getActivity(), "_token", null);

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

        //设备点击事件
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

        //设备长按弹窗
        adapter.setOnItemLongClickListener(new DevicesListAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(GizWifiDevice mGizWifiDevice) {
                showDevicesInfDialog(mGizWifiDevice);
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


    private String getParamFomeUrl(String url, String param) {
        String product_key = "";
        int startindex = url.indexOf(param + "=");
        startindex += (param.length() + 1);
        String subString = url.substring(startindex);
        int endindex = subString.indexOf("&");
        if (endindex == -1) {
            product_key = subString;
        } else {
            product_key = subString.substring(0, endindex);
        }
        return product_key;
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
            Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
        }

        /** 用于设备绑定 */
        public void didBindDevice(GizWifiErrorCode result, java.lang.String did) {
            Toast.makeText(getActivity(), "恭喜，绑定成功！", Toast.LENGTH_SHORT).show();
        }

        /** 用于设备绑定（旧） */
        public void didBindDevice(int error, String errorMessage, String did) {
            Toast.makeText(getActivity(), "恭喜，绑定成功！", Toast.LENGTH_SHORT).show();
        }

        /** 用于绑定推送 */
        public void didChannelIDBind(GizWifiErrorCode result) {
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_QR_CODE && data != null) {
            String text = data.getStringExtra("result");
            if (text.contains("product_key=") && text.contains("did=") && text.contains("passcode=")) {
                product_key = getParamFomeUrl(text, "product_key");
                did = getParamFomeUrl(text, "did");
                passcode = getParamFomeUrl(text, "passcode");
                startBindDevices(did, passcode);
                // L.e("product_key:" + product_key + ",did:" + did + ",passcode:" + passcode);
            } else {
                Toast.makeText(getActivity(), "请确认是我们的产品二维码哦亲~", Toast.LENGTH_SHORT).show();
            }


        }
    }


    /**
     * 扫描二维码后开始绑定设备
     *
     * @param did      设备的did
     * @param passcode 设备的passcode
     */
    private void startBindDevices(String did, String passcode) {
        if (uid != null && token != null) {
            GizWifiSDK.sharedInstance().bindDevice(
                    uid
                    , token
                    , did
                    , passcode
                    , null);
        }
    }


    private void showDevicesInfDialog(final GizWifiDevice mGizWifiDevice) {

        final String[] stringItems = {"解绑此设备", "重命名设备", "查看设备信息"};
        final ActionSheetDialog sheetDialog = new ActionSheetDialog(getActivity(), stringItems, null);
        sheetDialog.isTitleShow(false).show();
        sheetDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        new CustomBaseDialog(getActivity(), mGizWifiDevice).show();
                        break;
                    case 1:
                        renameDevicesDialog(mGizWifiDevice);
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
                sheetDialog.dismiss();
            }
        });
    }

    //删除弹窗振动
    private class CustomBaseDialog extends BaseDialog<CustomBaseDialog> {

        private GizWifiDevice mGizWifiDevice;
        private TextView mTvCancel, mTvSure, dialog_title, delete_content;

        CustomBaseDialog(Context context, GizWifiDevice mGizWifiDevice) {
            super(context);
            this.mGizWifiDevice = mGizWifiDevice;
        }

        @Override
        public View onCreateView() {
            widthScale(0.85f);
            showAnim(new Swing());
            View view = View.inflate(getActivity(), R.layout.dialog_delete, null);
            mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            mTvSure = (TextView) view.findViewById(R.id.tv_exit);
            dialog_title = (TextView) view.findViewById(R.id.dialog_title);
            delete_content = (TextView) view.findViewById(R.id.delete_content);

            dialog_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);

            if (mGizWifiDevice.getAlias().isEmpty()) {
                delete_content.setText("确定要解绑此设备“" + mGizWifiDevice.getProductName() + "”吗？");
            } else {
                delete_content.setText("确定要解绑此设备“" + mGizWifiDevice.getAlias() + "”吗？");
            }

            view.setBackgroundDrawable(
                    CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));
            return view;
        }


        @Override
        public void setUiBeforShow() {
            mTvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            mTvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GizWifiSDK.sharedInstance().unbindDevice(uid, token, mGizWifiDevice.getDid());
                    dismiss();
                }
            });
        }
    }

    //重命名
    private void renameDevicesDialog(final GizWifiDevice mGizWifiDevice) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_rename, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .show();

        final EditText rename_et = (EditText) dialog.findViewById(R.id.rename_et);
        SoftInputUtils.showSoftInput(getActivity());

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
                    Toast.makeText(getActivity(), "输入不能为空!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                mGizWifiDevice.setCustomInfo(null, name);
                hideKeyBoard();
                dialog.dismiss();

            }
        });
    }

    private void hideKeyBoard() {
        // 隐藏键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 设备绑定事件监听
     */
    private GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        @Override
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {

            if (GizWifiDeviceNetStatus.GizDeviceOffline == device.getNetStatus()) {
                return;
            }

            if (isFirstBind && GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
                Intent intent = new Intent(getActivity(), SmartSocketActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", device);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };
}


