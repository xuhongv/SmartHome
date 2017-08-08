package com.xuhong.smarthome.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/*
 * 项目名：iot_android-TCL
 * 包名：com.tcl.iot.util
 * 创建时间：2017/4/20  下午 5:27
 * 创建者  xuhong
 * 描述：Picasso封装
 */
public class PicassoUtils {

    //加载相机或相册的图片
    public static void loadImageViewFromURl(Context mContent, String url, ImageView imageView){
        Picasso.with(mContent).load(url).into(imageView);
    }
    //加载本地的图片
    public static void loadImageViewFromLocal(Context mContent, int resouce, ImageView imageView){
        Picasso.with(mContent).load(resouce).into(imageView);
    }
}
