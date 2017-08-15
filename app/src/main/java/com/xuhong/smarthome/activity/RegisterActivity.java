package com.xuhong.smarthome.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.MainActivity;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.utils.PhotoSelectUtils;
import com.xuhong.smarthome.utils.PicassoUtils;
import com.xuhong.smarthome.utils.RegexUtils;
import com.xuhong.smarthome.utils.ToastUtils;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.mPopupWindow;

import java.io.File;

import cn.bmob.v3.BmobUser;
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


    private EditText register_et_name, register_et_email, register_et_password, register_et_password_again;

    //选取相册
    private PhotoSelectUtils photoSelectUtils;
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

        //拍照回调
        photoSelectUtils = new PhotoSelectUtils(this, new PhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                mFile = outputFile;
                Log.e("==w", "成功");
                ivCameraBg.setAlpha(1f);
                Picasso.with(RegisterActivity.this).load(outputFile).transform(new CircleTransform()).into(ivCameraBg);
                ivNull.setVisibility(View.INVISIBLE);
            }
        }, 1, 1, 350, 350);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.llCamera:
                mPopupWindow mPopupWindow = new mPopupWindow(RegisterActivity.this, new mPopupWindow.OnPopWindowClickListener() {
                    @Override
                    public void onPopWindowClickListener(View view) {
                        switch (view.getId()) {
                            case R.id.btnTakePhoto:
                                // 3、调用拍照方法
                                PermissionGen.with(RegisterActivity.this)
                                        .addRequestCode(PhotoSelectUtils.REQ_TAKE_PHOTO)
                                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        ).request();
                                break;
                            case R.id.btnAlum:
                                PermissionGen.needPermission(RegisterActivity.this,
                                        PhotoSelectUtils.REQ_SELECT_PHOTO,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                );
                                break;
                        }
                    }
                });
                mPopupWindow.show();
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

                    if (mFile != null) {
                        final BmobFile bmobFile = new BmobFile(mFile);
                        bmobFile.uploadblock(new UploadFileListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                    photoFileURL = bmobFile.getFileUrl();
                                    Log.e("==w", "相片：" + photoFileURL);
                                    User user = new User();
                                    user.setUsername(register_et_name.getText().toString());
                                    user.setEmail(register_et_email.getText().toString());
                                    user.setPassword(register_et_password.getText().toString());
                                    user.setNick(photoFileURL);
                                    user.setText("xuhong");
                                    user.signUp(new SaveListener<User>() {
                                        @Override
                                        public void done(User user, BmobException e) {
                                            if (e == null) {
                                                ToastUtils.showPhotoToast(RegisterActivity.this, R.drawable.ic_warning, "注册成功！");

                                            } else {
                                                Log.e("==w", e + "");
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
                    }




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

    @PermissionSuccess(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        photoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        photoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        photoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {

        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + RegisterActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }


}
