package com.xuhong.smarthome.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * 项目名：TakePictureManager
 * 包名：com.xuhong.takepictureandroidn_master
 * 文件名：TakePictureManager
 * 创建时间：2017/8/18 12:08
 * 创建者： 徐宏  http://blog.csdn.net/xh870189248
 * 描述：只为适配Android N 拍照和本地相册上传封装而生
 */

public class TakePictureManager {

    private Activity mActivity;
    private Fragment mFragment;

    //默认不开启裁剪
    private boolean isTailor = false;

    //裁剪宽高的比例,默认是是 1 ：1
    private int aspectX = 1;
    private int aspectY = 1;

    //裁剪图片的宽高,默认是是 1 ：1
    private int outputX = 350;
    private int outputY = 350;

    //拿到未裁剪相片的回调码（拍照后）
    private static final int CODE_ORIGINAL_PHOTO_CAMERA = 101;

    //拿到未裁剪相片的回调码（选择本地图库后）
    private static final int CODE_ORIGINAL_PHOTO_ALBUM = 102;

    //拿到已裁剪相片的回调码
    private static final int CODE_TAILOR_PHOTO = 103;

    //布尔值，true：在mActivity进行操作 ；false :Fragment操作
    private boolean isActicity;

    //上下文
    private Context mContext;

    //activity
    private Activity tempActivity;

    //FileProvider的主机名：一般是包名+".fileprovider"
    private String FILE_PROVIDER_AUTHORITY;

    //临时存储相片地址
    private String imgPath;

    //最终得到的Url
    private Uri outputUri;

    //是否压缩图片 默认开启压缩图片的
    private boolean isCompressor = true;

    //图片回调接口
    private takePictureCallBackListener takeCallBacklistener;

    //内部权限接口，学习于郭神
    private PermissionListener permissionListener;


    public TakePictureManager(Activity mActivity) {
        this.mActivity = mActivity;
        tempActivity = mActivity;
        isActicity = true;
        mContext = mActivity;
        FILE_PROVIDER_AUTHORITY = mActivity.getPackageName() + ".fileprovider";
    }

    public TakePictureManager(Fragment mFragment) {
        this.mFragment = mFragment;
        isActicity = false;
        mContext = mFragment.getActivity();
        tempActivity = mFragment.getActivity();
        FILE_PROVIDER_AUTHORITY = mFragment.getActivity().getPackageName() + ".fileprovider";
    }


    //开始拍照
    public void startTakeWayByCarema() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0或6.0以上，则要申请运行时权限，这里需要申请拍照和写入SD卡的权限
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}
                    , new PermissionListener() {
                        @Override
                        public void onGranted() {
                            startOpencamera();
                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {
                            if (takeCallBacklistener != null) {
                                takeCallBacklistener.failed(1, deniedPermissions);
                            }
                        }
                    });
            return;
        }
        startOpencamera();
    }

    //开始从图库获取
    public void startTakeWayByAlbum() {

        imgPath = generateImgePath(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0或6.0以上，则要申请运行时权限，这里需要申请拍照和写入SD卡的权限
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted() {
                    startAlbum();
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                    if (takeCallBacklistener != null) {
                        takeCallBacklistener.failed(1, deniedPermissions);
                    }
                }
            });

            return;
        }
        startAlbum();
    }

    private void startAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        if (isActicity) {
            mActivity.startActivityForResult(intent, CODE_ORIGINAL_PHOTO_ALBUM);
        } else {
            mFragment.startActivityForResult(intent, CODE_ORIGINAL_PHOTO_ALBUM);
        }
    }


    /**
     * 对外接口，是否裁剪
     *
     * @param aspectX 要裁剪的宽比例
     * @param aspectY 要裁剪的高比例
     * @param outputX 要裁剪图片的宽
     * @param outputY 要裁剪图片的高
     */

    public void setTailor(int aspectX, int aspectY, int outputX, int outputY) {
        isTailor = true;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.outputX = outputX;
        this.outputY = outputY;
    }

    /**
     * 裁剪方法
     *
     * @param srcFile
     * @param output
     */

    private void statZoom(File srcFile, File output) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(mContext, srcFile), "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        if (isActicity) {
            mActivity.startActivityForResult(intent, CODE_TAILOR_PHOTO);
        } else {
            mFragment.startActivityForResult(intent, CODE_TAILOR_PHOTO);
        }

    }


    /**
     * 获取到的相片回调方法，
     * 必须要在当前的Activity或Fragment中的onActivityResult下调用！
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void attachToActivityForResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != -1) {
            return;
        }

        Bitmap bm = null;
        File temFile = null;
        File srcFile = null;
        File outPutFile = null;

        switch (requestCode) {
            //拍照后得到的图片
            case CODE_ORIGINAL_PHOTO_CAMERA:

                srcFile = new File(imgPath);
                outPutFile = new File(generateImgePath(mContext));
                outputUri = Uri.fromFile(outPutFile);
                Uri imageContentUri = getImageContentUri(mContext, srcFile);

                if (isTailor) {
                    statZoom(srcFile, outPutFile);
                } else {
                    if (takeCallBacklistener != null) {
                        takeCallBacklistener.successful(false, srcFile, imageContentUri);
                    }
                }

                break;

            //选择相册后得到的图片
            case CODE_ORIGINAL_PHOTO_ALBUM:
                if (data != null) {
                    Uri sourceUri = data.getData();
                    String pickPath = getPath(mContext, sourceUri);
                    srcFile = new File(pickPath);
                    temFile = srcFile;

                    if (isTailor) {
                        //裁剪之后的文件和ur
                        outPutFile = new File(generateImgePath(mContext));
                        outputUri = Uri.fromFile(outPutFile);
                        //发起裁剪请求
                        statZoom(srcFile, outPutFile);
                    } else {

                        outputUri = Uri.fromFile(srcFile);
                        //如果选择返回一个压缩后的图片
                        if (isCompressor) {
                            temFile = outputIamge(mContext, compressImage(decodeUriAsBitmap(outputUri), 100));
                            outputUri = Uri.fromFile(temFile);
                        }

                        if (takeCallBacklistener != null) {
                            takeCallBacklistener.successful(true, temFile, outputUri);
                        }
                    }

                } else {
                    if (takeCallBacklistener != null) {
                        takeCallBacklistener.failed(0, null);
                    }
                }
                break;

            //裁剪后的图片：
            case CODE_TAILOR_PHOTO:
                //拿到图片之后，用户可能会舍弃，所以先判断
                if (data != null) {
                    if (outputUri != null) {
                        //如果是拍照的,删除临时文件
                        temFile = new File(imgPath);

                        if (temFile.exists()) {
                            temFile.delete();
                        }

                        //返回一个压缩后的图片
                        if (isCompressor) {
                            temFile = outputIamge(mContext, compressImage(decodeUriAsBitmap(outputUri), 100));
                            outputUri = Uri.fromFile(temFile);
                        }

                        if (takeCallBacklistener != null) {
                            takeCallBacklistener.successful(true, temFile, outputUri);
                        }

                    }
                } else {
                    if (takeCallBacklistener != null) {
                        takeCallBacklistener.failed(0, null);
                    }
                }

                break;
        }
    }


    //打开相机
    private void startOpencamera() {
        imgPath = generateImgePath(mContext);
        File imgFile = new File(imgPath);
        Uri imgUri = null;

        //判断当前手机版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imgUri = FileProvider.getUriForFile(mContext, FILE_PROVIDER_AUTHORITY, imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

        //判断当前在哪启动
        if (isActicity) {
            mActivity.startActivityForResult(intent, CODE_ORIGINAL_PHOTO_CAMERA);
        } else {
            mFragment.startActivityForResult(intent, CODE_ORIGINAL_PHOTO_CAMERA);
        }
    }


    //权限回调
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                    //被拒绝权限
                    if (deniedPermissions.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermissions);
                        if (takeCallBacklistener != null) {
                            takeCallBacklistener.failed(1, deniedPermissions);
                        }
                    }
                }
                break;
        }

    }

    /**
     * 申请运行时权限
     */
    private void requestRuntimePermission(String[] permissions, PermissionListener listener) {

        permissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        //此处兼容了无法在fragment回调监听事件
        if (!permissionList.isEmpty()) {
            if (isActicity) {
                ActivityCompat.requestPermissions((Activity) mContext, permissionList.toArray(new String[permissionList.size()]), 1);
            } else {
                mFragment.requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
            }


            if (takeCallBacklistener != null) {
                takeCallBacklistener.failed(1, permissionList);
            }
        } else {
            permissionListener.onGranted();
        }
    }


    public void setTakePictureCallBackListener(takePictureCallBackListener takeCallBacklistener) {
        this.takeCallBacklistener = takeCallBacklistener;
    }


    //得到图片回调接口（内部）
    public interface takePictureCallBackListener {
        /**
         * 成功回调
         *
         * @param isTailor 是否开启了裁剪
         * @param outFile
         * @param filePath
         */
        void successful(boolean isTailor, File outFile, Uri filePath);

        /**
         * 失败回调
         *
         * @param errorCode         错误码  0：图片发生错误  1：被拒绝的权限
         * @param deniedPermissions 被拒绝的权限
         */
        void failed(int errorCode, List<String> deniedPermissions);

    }


    private interface PermissionListener {

        void onGranted();

        void onDenied(List<String> deniedPermissions);
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------以下是文件操作相关------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------------------------------*/


    private static final String ICON_DIR = "icon";
    private static final String APP_STORAGE_ROOT = "AndroidNAdaption";

    //判断SD卡是否挂载

    private static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取app在外置SD卡的路径
     *
     * @param name
     * @return
     */
    private static String getAppDir(Context context, String name) {
        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(getAppExternalStoragePath());
        } else {
            sb.append(getCachePath(context));
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    //获取SD下当前APP的目录

    private static String getAppExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(APP_STORAGE_ROOT);
        sb.append(File.separator);
        return sb.toString();
    }

    //获取应用的cache目录
    private static String getCachePath(Context context) {
        File f = context.getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }

    //创建文件夹
    private static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    //产生图片的路径，带文件夹和文件名，文件名为当前毫秒数

    private static String generateImgePath(Context context) {
        return getAppDir(context, ICON_DIR) + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }

    //裁剪根据文件路径获取uri

    private static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
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
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 根据uri返回bitmap
     *
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            // 先通过getContentResolver方法获得一个ContentResolver实例，
            // 调用openInputStream(Uri)方法获得uri关联的数据流stream
            // 把上一步获得的数据流解析成为bitmap
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 返回一张压缩后的图片
     *
     * @param image 原图片
     * @param size  裁剪之后的大小
     * @return
     */
    private static Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //在自定义目录创建图片
    private static File outputIamge(Context context, Bitmap bitmap) {

        File outputIamge = new File(generateImgePath(context));

        //创建
        try {
            outputIamge.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fOut = null;

        try {
            fOut = new FileOutputStream(outputIamge);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputIamge;
    }


    /*******************
     * -适配小米手机
     ************************/
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;

    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;

    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}