package com.xuhong.smarthome.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/8/16 0016.
 */

public class L {

    private static boolean isLog = true;

    private static final String TAG = "smarthomeLog";

    public static void i(String tag, String msg) {
        if (isLog) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isLog) {
            Log.i(TAG, msg);
        }
    }



    public static void d(String tag, String msg) {
        if (isLog) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (isLog) {
            Log.d(TAG, msg);
        }
    }







    public static void e(String tag, String msg) {
        if (isLog) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isLog) {
            Log.e(TAG, msg);
        }
    }

}
