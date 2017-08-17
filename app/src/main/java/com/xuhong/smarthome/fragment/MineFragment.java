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

import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.AlterUserInfActivity;
import com.xuhong.smarthome.activity.LoginActivity;
import com.xuhong.smarthome.activity.RegisterActivity;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.listener.PermissionListener;
import com.xuhong.smarthome.utils.FileUtils;
import com.xuhong.smarthome.utils.ImageTools;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.PhotoSelectUtils;
import com.xuhong.smarthome.utils.ToastUtils;
import com.xuhong.smarthome.view.AnimotionPopupWindow;
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.PullScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static android.app.Activity.RESULT_OK;


public class MineFragment extends BaseFragment implements View.OnClickListener {


    private ImageView ivHeaderBg;
    private ImageView ivmeIcon;
    private PullScrollView pullView;

    private TextView tvName;

    //临时的上传图片的文件
    private File photoFile;


    //上传图片用到
    private String FILE_PROVIDER_AUTHORITY;
    private static final int REQ_TAKE_PHOTO = 100;
    private static final int REQ_ALBUM = 101;
    private static final int REQ_ZOOM = 102;
    private PermissionListener permissionListener;
    private Uri outputUri;
    private String imgPath = FileUtils.generateImgePath(getActivity());
    //拍照完图片保存的路径

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {

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
                    list.add("修改名字");
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
                                //修改名字
                                case 1:

                                    break;
                                //切换账号
                                case 2:
                                    startActivity(new Intent(getActivity(), AlterUserInfActivity.class));
                                    break;
                                //退出登录
                                case 3:
                                    User.logOut();
                                    ToastUtils.showToast(getActivity(), "退出成功！");
                                    getUserInf();
                                    break;
                            }
                        }
                    });
                }
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
        FILE_PROVIDER_AUTHORITY = getActivity().getPackageName() + ".fileprovider";

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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            //如果是6.0或6.0以上，则要申请运行时权限，这里需要申请拍照和写入SD卡的权限
                            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    openCamera();
                                }

                                @Override
                                public void onDenied(List<String> deniedPermissions) {
                                    L.e("拍照权限被拒绝了");
                                }
                            });
                            return;
                        }

                        openCamera();

                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, REQ_ALBUM);
                        break;
                }
            }
        });

    }

    private void openCamera() {

        // 指定调用相机拍照后照片的储存路径
        L.i("拍照完图片的路径为：" + imgPath);
        File imgFile = new File(imgPath);
        Uri imgUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0或以上
            imgUri = FileProvider.getUriForFile(getActivity(), FILE_PROVIDER_AUTHORITY, imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQ_TAKE_PHOTO);

    }


    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        permissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:

                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
                case RESULT_OK://调用图片选择处理成功
                    Bitmap bm = null;
                    File temFile = null;
                    File srcFile = null;
                    File outPutFile = null;
                    switch (requestCode) {
                        case REQ_TAKE_PHOTO:
                            //拍照后在这里回调
                            srcFile = new File(imgPath);
                            outPutFile = new File(FileUtils.generateImgePath(getActivity()));
                            outputUri = Uri.fromFile(outPutFile);
                            startPhotoZoom(srcFile, outPutFile, REQ_ZOOM);
                            break;

                        case REQ_ALBUM:// 选择相册中的图片
                            if (data != null) {
                                Uri sourceUri = data.getData();
                                String[] proj = {MediaStore.Images.Media.DATA};

                                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                                Cursor cursor = getActivity().managedQuery(sourceUri, proj, null, null, null);
                                // 按我个人理解 这个是获得用户选择的图片的索引值
                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                                cursor.moveToFirst();
                                // 最后根据索引值获取图片路径
                                String imgPath = cursor.getString(column_index);

                                srcFile = new File(imgPath);
                                outPutFile = new File(FileUtils.generateImgePath(getActivity()));
                                outputUri = Uri.fromFile(outPutFile);
                                //发起裁剪请求
                                startPhotoZoom(srcFile, outPutFile, REQ_ZOOM);
                            }
                            break;

                        case REQ_ZOOM://裁剪后回调
                            if (data != null) {
                                if (outputUri != null) {
                                    bm = ImageTools.decodeUriAsBitmap(outputUri, getActivity());
                                    //如果是拍照的,删除临时文件
                                    temFile = new File(imgPath);
                                    if (temFile.exists()) {
                                        temFile.delete();
                                    }
                                    String scaleImgPath = FileUtils.saveBitmapByQuality(bm, 80, getActivity());//进行压缩
                                    L.i("压缩后图片的路径为：" + scaleImgPath);

                                }
                            } else {
                                L.e("选择图片发生错误，图片可能已经移位或删除");
                            }
                            break;
                    }
                    break;
            }
        }


    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 发起剪裁图片的请求
     *
     * @param srcFile     原文件的File
     * @param output      输出文件的File
     * @param requestCode 请求码
     */
    public void startPhotoZoom(File srcFile, File output, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(srcFile), "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, requestCode);

    }


}