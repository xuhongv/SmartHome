package com.xuhong.smarthome.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.utils.FileUtils;
import com.xuhong.smarthome.utils.PhotoSelectUtils;
import com.xuhong.smarthome.utils.RegexUtils;
import com.xuhong.smarthome.utils.TakePictureManager;
import com.xuhong.smarthome.utils.ToastUtils;
import com.xuhong.smarthome.view.AnimotionPopupWindow;
import com.xuhong.smarthome.view.CircleTransform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout llCamera;

    private Button btn_register;


    //透明度
    private float BgAlpha = 0.5f;
    private float ButtonAlpha = 0.7f;

    //拍照
    private TakePictureManager takePictureManager;


    private EditText register_et_name, register_et_email, register_et_password, register_et_password_again;
    private ImageView ivCameraBg, ivNull;

    private File mFile;

    private String photoFileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
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

        register_et_name = (EditText) findViewById(R.id.register_et_name);
        register_et_email = (EditText) findViewById(R.id.register_et_email);
        register_et_password = (EditText) findViewById(R.id.register_et_password);
        register_et_password_again = (EditText) findViewById(R.id.register_et_password_again);


        RelativeLayout rlUserName = (RelativeLayout) findViewById(R.id.rlUserName);
        rlUserName.setAlpha(BgAlpha);
        RelativeLayout rlUserPassword = (RelativeLayout) findViewById(R.id.rlUserPassword);
        rlUserPassword.setAlpha(BgAlpha);
        RelativeLayout rlUserPasswordComfir = (RelativeLayout) findViewById(R.id.rlUserPasswordComfir);
        rlUserPasswordComfir.setAlpha(BgAlpha);
        RelativeLayout rlUserEmail = (RelativeLayout) findViewById(R.id.rlUserEmail);
        rlUserEmail.setAlpha(BgAlpha);
        ivCameraBg = (ImageView) findViewById(R.id.ivCameraBg);
        ivNull = (ImageView) findViewById(R.id.ivNull);
        ivCameraBg.setAlpha(BgAlpha);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_register.setAlpha(ButtonAlpha);

        llCamera = (FrameLayout) findViewById(R.id.llCamera);
        llCamera.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCamera:
                List<String> list = new ArrayList<>();
                list.add("相册");
                list.add("相机");
                AnimotionPopupWindow mAnimotionPopupWindow = new AnimotionPopupWindow(RegisterActivity.this, list);
                mAnimotionPopupWindow.show();
                mAnimotionPopupWindow.setAnimotionPopupWindowOnClickListener(new AnimotionPopupWindow.AnimotionPopupWindowOnClickListener() {
                    @Override
                    public void onPopWindowClickListener(int position) {
                        switch (position) {
                            case 0:
                                takePictureManager = new TakePictureManager(RegisterActivity.this);
                                //开启裁剪 比例 1:3 宽高 350 350  (默认不裁剪)
                                takePictureManager.setTailor(1, 1, 350, 350);
                                //拍照方式
                                takePictureManager.startTakeWayByAlbum();
                                //回调
                                takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                                    @Override
                                    public void successful(boolean isTailor, File outFile, Uri filePath) {
                                        mFile = outFile;
                                        ivCameraBg.setAlpha(1f);
                                        Picasso.with(RegisterActivity.this).load(outFile).transform(new CircleTransform()).into(ivCameraBg);
                                        ivNull.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void failed(int errorCode, List<String> deniedPermissions) {

                                    }
                                });
                                break;
                            case 1:

                                takePictureManager = new TakePictureManager(RegisterActivity.this);
                                //开启裁剪 比例 1:3 宽高 350 350  (默认不裁剪)
                                takePictureManager.setTailor(1, 1, 350, 350);
                                //拍照方式
                                takePictureManager.startTakeWayByCarema();
                                //回调
                                takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                                    @Override
                                    public void successful(boolean isTailor, File outFile, Uri filePath) {
                                        mFile = outFile;
                                        ivCameraBg.setAlpha(1f);
                                        Picasso.with(RegisterActivity.this).load(outFile).transform(new CircleTransform()).into(ivCameraBg);
                                        ivNull.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void failed(int errorCode, List<String> deniedPermissions) {

                                    }
                                });
                                break;
                        }
                    }
                });
                break;


            case R.id.btn_register:
                if (!register_et_name.getText().toString().isEmpty()
                        && !register_et_email.getText().toString().isEmpty()
                        && !register_et_password.getText().toString().isEmpty()
                        && !register_et_password_again.getText().toString().isEmpty()) {

                    if (!register_et_password.getText().toString().equals(register_et_password_again.getText().toString())) {
                        ToastUtils.showPhotoToast(RegisterActivity.this, R.drawable.ic_warning, "两次输入密码不一致哦！");
                        return;
                    }

                    if (!RegexUtils.isEmail(register_et_email.getText().toString())) {
                        ToastUtils.showPhotoToast(RegisterActivity.this, R.drawable.ic_warning, "您输入的邮箱地址有误！");
                        return;
                    }

                    if (mFile == null) {
                        ToastUtils.showPhotoToast(RegisterActivity.this, R.drawable.ic_warning, "请上传您的头像！");
                        return;
                    }


                    final BmobFile bmobFile = new BmobFile(mFile);
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                photoFileURL = bmobFile.getFileUrl();
                                User user = new User();
                                user.setUsername(register_et_name.getText().toString());
                                user.setEmail(register_et_email.getText().toString());
                                user.setPassword(register_et_password.getText().toString());
                                user.setNick(photoFileURL);
                                user.signUp(new SaveListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        if (e == null) {
                                            ToastUtils.showPhotoToast(RegisterActivity.this, R.drawable.ic_warning, "注册成功！");
                                            finish();
                                        } else {
                                            switch (e.getErrorCode()) {
                                                case 202:
                                                    ToastUtils.showToast(RegisterActivity.this, "注册失败！该名字已注册，请尝试换个名字！");
                                                    break;
                                                case 203:
                                                    ToastUtils.showToast(RegisterActivity.this, "注册失败！该邮箱已注册，请尝试换个邮箱！");
                                                    break;
                                                case 9610:
                                                    ToastUtils.showToast(RegisterActivity.this, "请检查您的网络状态！");
                                                    break;

                                            }
                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }
                    });


                } else {
                    Snackbar.make(llCamera, "请输入完整的信息！", Snackbar.LENGTH_LONG)
                            .setAction("知道了！", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                }


                break;
        }
    }

    //把本地的onActivityResult()方法回调绑定到对象
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

    //onRequestPermissionsResult()方法权限回调绑定到对象
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
