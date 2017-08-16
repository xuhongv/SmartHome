package com.xuhong.smarthome.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.L;

/**
 * 项目名： SmartHome-master
 * 包名： com.xuhong.smarthome.view
 * 文件名字： SelectPopupWindow
 * 创建时间：2017/8/15 20:59
 * 项目名： Xuhong
 * 描述： TODO
 */

public class SelectPopupWindow extends PopupWindow implements View.OnClickListener {

    public static final int TYPE_MINE_PHOTO = 0;
    public static final int TYPE_SELECT_PIC = 1;

    private Activity activity;

    private OnPopWindowClickListener listener;

    private View view;

    private LayoutAnimationController mLac;


    public SelectPopupWindow(Activity activity, OnPopWindowClickListener listener, int type) {
        this.activity = activity;
        this.listener = listener;

        if (type == TYPE_MINE_PHOTO) {
            initMinePicView();
        }

        if (type == TYPE_SELECT_PIC) {
            initSelectorView();
        }
        initView();
    }

    private void initView() {
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.dialog_style);
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
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 6f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(350);
        animation.setStartOffset(150);

        mLac = new LayoutAnimationController(animation, 0.12f);
        mLac.setInterpolator(new DecelerateInterpolator());

    }


    private void initSelectorView() {

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_takephoto, null);



        Button btnTakePhoto = (Button) view.findViewById(R.id.btnTakePhoto);
        Button btnAlum = (Button) view.findViewById(R.id.btnAlum);
        Button btncancel = (Button) view.findViewById(R.id.btncancel);

        btnTakePhoto.setOnClickListener(this);
        btnAlum.setOnClickListener(this);
        btncancel.setOnClickListener(this);
    }

    private void initMinePicView() {

        Button mBtnAlterPic;
        Button mBtnAlterInf;
        Button mBtnSwitch;
        Button mBtnExit;
        Button mBtncancel;

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_mine_pic, null);

        mBtnAlterPic = (Button) view.findViewById(R.id.btnAlterPic);
        mBtnAlterInf = (Button) view.findViewById(R.id.btnAlterInf);
        mBtnSwitch = (Button) view.findViewById(R.id.btnSwitch);
        mBtnExit = (Button) view.findViewById(R.id.btnExit);
        mBtncancel = (Button) view.findViewById(R.id.btncancel);

        mBtnAlterPic.setOnClickListener(this);
        mBtnAlterInf.setOnClickListener(this);
        mBtnSwitch.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
        mBtncancel.setOnClickListener(this);
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

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.pop_layout);
        ll.setLayoutAnimation(mLac);
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
