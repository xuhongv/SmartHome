package com.xuhong.smarthome.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 项目名： SmartHome
 * 包名： com.xuhong.smarthome.view
 * 文件名字： SpaceItemDecoration
 * 创建时间：2017/8/7 16:11
 * 项目名： Xuhong
 * 描述： recyclerview的各个子项的距离
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
