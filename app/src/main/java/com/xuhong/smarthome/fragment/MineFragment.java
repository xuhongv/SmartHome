package com.xuhong.smarthome.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.AlterUserInfActivity;
import com.xuhong.smarthome.activity.LoginActivity;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.TakePictureManager;
import com.xuhong.smarthome.utils.ToastUtils;
import com.xuhong.smarthome.view.AnimotionPopupWindow;
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.PullScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;


public class MineFragment extends BaseFragment implements View.OnClickListener {


    private ImageView ivHeaderBg;
    private ImageView ivmeIcon;
    private PullScrollView pullView;
    private TextView tvName;
    private TextView mTvDevices;
    private TextView mTvShareDevices;
    private TextView mTvDevicesLog;
    private com.lqr.optionitemview.OptionItemView mOVUserInf;
    private com.lqr.optionitemview.OptionItemView mOVCarText;
    private com.lqr.optionitemview.OptionItemView mOVDayHappy;
    private com.lqr.optionitemview.OptionItemView mOVAbout;
    private com.lqr.optionitemview.OptionItemView OVVegetable;
    //上传图片用到
    private TakePictureManager takePictureManager;
    //拍照完图片保存的路径

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {

        mTvDevices = (TextView) view.findViewById(R.id.tvDevices);
        mTvShareDevices = (TextView) view.findViewById(R.id.tvShareDevices);
        mTvDevicesLog = (TextView) view.findViewById(R.id.tvDevicesLog);
        mOVUserInf = (com.lqr.optionitemview.OptionItemView) view.findViewById(R.id.OVUserInf);
        mOVUserInf.setOnClickListener(this);
        mOVCarText = (com.lqr.optionitemview.OptionItemView) view.findViewById(R.id.OVCarText);
        mOVCarText.setOnClickListener(this);
        mOVDayHappy = (com.lqr.optionitemview.OptionItemView) view.findViewById(R.id.OVDayHappy);
        mOVDayHappy.setOnClickListener(this);
        mOVAbout = (com.lqr.optionitemview.OptionItemView) view.findViewById(R.id.OVAbout);
        OVVegetable = (com.lqr.optionitemview.OptionItemView) view.findViewById(R.id.OVVegetable);
        mOVAbout.setOnClickListener(this);
        OVVegetable.setOnClickListener(this);


        ivHeaderBg = (ImageView) view.findViewById(R.id.ivHeaderBg);
        ivmeIcon = (ImageView) view.findViewById(R.id.ivIcon);
        tvName = (TextView) view.findViewById(R.id.tvName);
        ivmeIcon.setOnClickListener(this);
        pullView = (PullScrollView) view.findViewById(R.id.pullView);
        pullView.setZoomView(ivHeaderBg);


        getUserInf();

    }


    @Override
    public void onResume() {
        super.onResume();
        getUserInf();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ivIcon:
                //判断是否已经登录
                if (BmobUser.getCurrentUser(User.class) == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    List<String> list = new ArrayList<>();
                    list.add("更改头像");
                    list.add("切换账号");
                    list.add("退出登录");
                    AnimotionPopupWindow popupWindow = new AnimotionPopupWindow(getActivity(), list);
                    popupWindow.show();
                    popupWindow.setAnimotionPopupWindowOnClickListener(new AnimotionPopupWindow.AnimotionPopupWindowOnClickListener() {
                        @Override
                        public void onPopWindowClickListener(int position) {
                            switch (position) {
                                //更改头像
                                case 0:
                                    changeMyIcon();
                                    break;
                                //切换账号
                                case 1:
                                    startActivity(new Intent(getActivity(), AlterUserInfActivity.class));
                                    break;
                                //退出登录
                                case 2:
                                    User.logOut();
                                    ToastUtils.showToast(getActivity(), "退出成功！");
                                    getUserInf();
                                    break;

                            }
                        }
                    });
                }
                break;

            case R.id.OVUserInf:
               startActivity(new Intent(getActivity(),AlterUserInfActivity.class));
                break;
            case R.id.OVCarText:
                break;
            case R.id.OVDayHappy:

                break;

            case R.id.OVVegetable:
                break;
        }
    }

    /**
     * 获取当前用户信息
     */
    private void getUserInf() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        if (userInfo != null) {
            String nick = userInfo.getNick();
            Picasso.with(getActivity())
                    .load(nick)
                    .transform(new BlurTransformation(getActivity()))
                    .fit()
                    .into(ivHeaderBg);

            Picasso.with(getActivity())
                    .load(nick)
                    .transform(new CircleTransform())
                    .into(ivmeIcon);

            tvName.setText(userInfo.getUsername());

        } else {
            Picasso.with(getActivity())
                    .load(R.mipmap.ic_mine_header_bg)
                    .transform(new BlurTransformation(getActivity()))
                    .fit()
                    .into(ivHeaderBg);

            Picasso.with(getActivity())
                    .load(R.mipmap.ic_unget)
                    .transform(new CircleTransform())
                    .into(ivmeIcon);

            tvName.setText("欢迎使用智能管家");
        }
    }


    private void changeMyIcon() {

        List<String> list = new ArrayList<>();
        list.add("拍照");
        list.add("相册");

        AnimotionPopupWindow animotionPopupWindow = new AnimotionPopupWindow(getActivity(), list);
        animotionPopupWindow.show();
        animotionPopupWindow.setAnimotionPopupWindowOnClickListener(new AnimotionPopupWindow.AnimotionPopupWindowOnClickListener() {
            @Override
            public void onPopWindowClickListener(int position) {
                switch (position) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openAlbun();
                        break;
                }
            }
        });

    }

    private void openAlbun() {
        takePictureManager = new TakePictureManager(this);
        takePictureManager.setTailor(1, 1, 350, 350);
        takePictureManager.startTakeWayByAlbum();
        takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {
                updataMyicon(outFile);
            }

            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {

            }

        });
    }

    private void openCamera() {

        takePictureManager = new TakePictureManager(this);
        takePictureManager.setTailor(1, 1, 350, 350);
        takePictureManager.startTakeWayByCarema();
        takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {
                updataMyicon(outFile);
            }

            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {

            }
        });

    }


    private void updataMyicon(final File outFile) {
        final User userInfo = BmobUser.getCurrentUser(User.class);
        //删除当前文件
        BmobFile file = new BmobFile();
        file.setUrl(userInfo.getNick());//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        file.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    final BmobFile bmobFile = new BmobFile(outFile);
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                userInfo.setNick(bmobFile.getFileUrl());
                                userInfo.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            ToastUtils.showToast(getActivity(), "更新用户信息成功:");
                                            getUserInf();
                                        }
                                    }
                                });

                            } else {
                                ToastUtils.showToast(getActivity(), "上传文件失败：" + e.getMessage());
                            }

                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }
                    });
                } else {
                    ToastUtils.showToast(getActivity(), "失败：" + e.getErrorCode() + "," + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

}