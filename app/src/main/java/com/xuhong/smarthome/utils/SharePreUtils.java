package com.xuhong.smarthome.utils;


import android.content.Context;
import android.content.SharedPreferences;
/**
 * 项目名： XuHongColseTo
 * 包名： com.xuhong.xuhongcolseto.Utils
 * 文件名字： SharePreUtils
 * 创建时间：2017/6/19 16:08
 * 项目名： Xuhong
 * 描述： sharePrefences封装类
 */
public class SharePreUtils {
    private static final String SP_NAME = "config";
    public static void putInt(Context mContext, String key, int valus) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, valus);
        editor.apply();
    }
    public static void putString(Context mContext, String key, String valus) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, valus);
        editor.apply();
    }
    public static String getString(Context mContext, String key, String defValus) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValus);
    }
    public static void putBoolean(Context mContext, String key, boolean valus) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, valus);
        editor.apply();
    }
    public static boolean getBoolean(Context mContext, String key, boolean defValus) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValus);
    }
    public static int getInt(Context mContext, String key, int defValus) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValus);
    }
}