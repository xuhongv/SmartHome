package com.xuhong.smarthome.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author 写文件的工具类
 */
public class FileUtils {

    public static final String DOWNLOAD_DIR = "download";
    public static final String CACHE_DIR = "cache";
    public static final String ICON_DIR = "icon";
    public static final String IMAGE_LOADER_DIR = "image";
    public static final String MAP_CACHE_DIR = "map";

    public static final String APP_STORAGE_ROOT = "AndroidNAdaption";

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
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
    public static String getAppDir(Context context,String name) {
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

    /**
     * 获取SD下当前APP的目录
     */
    public static String getAppExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(APP_STORAGE_ROOT);
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * 获取应用的cache目录
     */
    public static String getCachePath(Context context) {
        File f = context.getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }

    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 产生图片的路径，带文件夹和文件名，文件名为当前毫秒数
     */
    public static String generateImgePath(Context context) {
        return getAppDir(context,ICON_DIR) + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }



    /**
     * 按质量压缩bm
     * @param bm
     * @param quality 压缩率
     * @return
     */
    public static String saveBitmapByQuality(Bitmap bm,int quality,Context context) {
        String croppath="";
        try {
            File f = new File(FileUtils.generateImgePath(context));
            //得到相机图片存到本地的图片
            croppath=f.getPath();
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG,quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return croppath;
    }
}
