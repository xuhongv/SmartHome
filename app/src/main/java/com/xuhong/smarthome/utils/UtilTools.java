package com.xuhong.smarthome.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;



/**
 * 项目名： UtilTools
 * 包名： com.xuhong.smarthome.utils
 * 文件名字： UtilTools
 * 创建时间：2017/8/16 14:45
 * 作者： Xuhong
 * 描述： UtilToolsg工具类
 */

public class UtilTools {

    //设置字体
    public static void setFont(Context mContext, TextView textview) {
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(), "fonts/FONT.TTF");
        textview.setTypeface(fontType);
    }

}
