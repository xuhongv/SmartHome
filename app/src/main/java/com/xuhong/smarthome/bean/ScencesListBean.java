package com.xuhong.smarthome.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/10 0010.
 */

public class ScencesListBean {

    private String title;
    private Drawable drawable;
    private boolean isShowGo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public boolean isShowGo() {
        return isShowGo;
    }

    public void setShowGo(boolean showGo) {
        isShowGo = showGo;
    }
}
