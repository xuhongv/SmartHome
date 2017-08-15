package com.xuhong.smarthome.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.xuhong.smarthome.R;

/**
 * 项目名： SmartHome-master
 * 包名： com.xuhong.smarthome.view
 * 文件名字： mPopupWindow
 * 创建时间：2017/8/15 20:59
 * 项目名： Xuhong
 * 描述： TODO
 */

public class mPopupWindow extends PopupWindow implements View.OnClickListener {


    private Activity activity;

    private OnPopWindowClickListener listener;

    private View view;


    public mPopupWindow(Activity activity, OnPopWindowClickListener listener) {
        this.activity = activity;
        initView(activity, listener);
    }

    private void initView(Activity activity, OnPopWindowClickListener listener) {


        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_takephoto, null);
        Button btnTakePhoto = (Button) view.findViewById(R.id.btnTakePhoto);
        Button btnAlum = (Button) view.findViewById(R.id.btnAlum);
        Button btncancel = (Button) view.findViewById(R.id.btncancel);

        btnTakePhoto.setOnClickListener(this);
        btnAlum.setOnClickListener(this);
        btncancel.setOnClickListener(this);

        //设置按钮监听
        this.listener = listener;
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dialog_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    public void show() {
        Rect rect = new Rect();
          /*
           * getWindow().getDecorView()得到的View是Window中的最顶层View，可以从Window中获取到该View，
           * 然后该View有个getWindowVisibleDisplayFrame()方法可以获取到程序显示的区域，
           * 包括标题栏，但不包括状态栏。
           */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = activity.getWindow().getDecorView().getHeight();
        this.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onClick(View v) {
        listener.onPopWindowClickListener(v);
        dismiss();
    }

    public interface OnPopWindowClickListener {
        void onPopWindowClickListener(View view);
    }
}
