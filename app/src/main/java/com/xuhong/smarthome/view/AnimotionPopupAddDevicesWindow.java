package com.xuhong.smarthome.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.xuhong.smarthome.R;

/**
 * 项目名： SmartHome
 * 包名： com.xuhong.smarthome.view
 * 文件名字： AnimotionPopupAddDevicesWindow
 * 创建时间：2017/7/23 9:57
 * 项目名： Xuhong
 * 描述： TODO
 */

public class AnimotionPopupAddDevicesWindow extends PopupWindow implements View.OnClickListener {

    private View mMenuView;

    private OnPopWindowClickListener listener;

    private Activity activity;

    private ImageView iv_add, iv_push_resale_faker, iv_push_photo_faker, iv_push_resale, iv_push_photo;

    private float defheightTvPhoto, defwightTvPhoto;

    private float heightTvPhoto, wightTvPhoto;

    private int heightTvTaobao, wightTvTaobao;

    private float defheightTvTaobao, defwightTvTaobao;


    public AnimotionPopupAddDevicesWindow(Activity activity, OnPopWindowClickListener listener) {
        this.activity = activity;
        initView(activity, listener);
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

    private void initView(Activity activity, OnPopWindowClickListener listener) {
        //设置按钮监听
        this.listener = listener;
        initViewSetting(activity);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        startAnimation();
    }

    private void initViewSetting(Activity context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_add_devices, null);


        iv_add = (ImageView) mMenuView.findViewById(R.id.dd5);
        iv_add.setOnClickListener(this);

        iv_push_photo = (ImageView) mMenuView.findViewById(R.id.iv_push_photo);
        iv_push_photo.setOnClickListener(this);

        iv_push_resale = (ImageView) mMenuView.findViewById(R.id.iv_push_resale);
        iv_push_resale.setOnClickListener(this);

        iv_push_resale_faker = (ImageView) mMenuView.findViewById(R.id.iv_push_resale_faker);
        iv_push_photo_faker = (ImageView) mMenuView.findViewById(R.id.iv_push_photo_faker);


    }

    @Override
    public void onClick(View view) {
        listener.onPopWindowClickListener(view);
        if (view.getId() == R.id.dd5) {
            exitAnimation();
        }

    }

    //接口
    public interface OnPopWindowClickListener {
        void onPopWindowClickListener(View view);

    }

    //进去界面的动画
    private void startAnimation() {
        //最下面的添加按钮旋转按钮动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 45, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setInterpolator(new BounceInterpolator());
        rotateAnimation.setFillAfter(true);
        iv_add.startAnimation(rotateAnimation);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //默认相册图片
                int location[] = new int[2];
                iv_push_photo_faker.getLocationOnScreen(location);
                defwightTvPhoto = location[0];
                defheightTvPhoto = location[1];

                //之后的淘宝图片
                int location1[] = new int[2];
                iv_push_resale.getLocationOnScreen(location1);
                wightTvTaobao = location1[0];
                heightTvTaobao = location1[1];


                //之后的相册图片
                int location2[] = new int[2];
                iv_push_photo.getLocationOnScreen(location2);
                wightTvPhoto = location2[0];
                heightTvPhoto = location2[1];

                //默认淘宝的图片位置
                int location3[] = new int[2];
                iv_push_resale_faker.getLocationOnScreen(location3);
                defwightTvTaobao = location3[0];
                defheightTvTaobao = location3[1];

                TranslateAnimation sa = new TranslateAnimation(0, wightTvPhoto - defwightTvPhoto, 0, heightTvPhoto - defheightTvPhoto);
                sa.setDuration(300);
                sa.setInterpolator(new BounceInterpolator());
                iv_push_photo_faker.startAnimation(sa);
                sa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv_push_photo_faker.setVisibility(View.GONE);
                        iv_push_resale_faker.setVisibility(View.VISIBLE);
                        iv_push_photo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                TranslateAnimation sa1 = new TranslateAnimation(0, wightTvTaobao - defwightTvTaobao, 0, heightTvTaobao - defheightTvTaobao);
                sa1.setDuration(600);
                  sa1.setInterpolator(new BounceInterpolator());
                iv_push_resale_faker.startAnimation(sa1);
                sa1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv_push_resale_faker.setVisibility(View.GONE);
                        iv_push_resale.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    ///出去的动画
    private void exitAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setInterpolator(new BounceInterpolator());
        iv_add.startAnimation(rotateAnimation);


        TranslateAnimation sa = new TranslateAnimation(0, defwightTvPhoto - wightTvPhoto,  0 ,defheightTvPhoto - heightTvPhoto);
        sa.setDuration(400);
        sa.setInterpolator(new BounceInterpolator());
        sa.setFillAfter(true);
        iv_push_photo.startAnimation(sa);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_push_photo.setVisibility(View.GONE);
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        TranslateAnimation sa2 = new TranslateAnimation(0, wightTvPhoto - defwightTvPhoto,  0 ,defheightTvPhoto - heightTvPhoto);
        sa2.setDuration(600);
        sa2.setInterpolator(new BounceInterpolator());
        iv_push_resale.startAnimation(sa2);
        sa2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_push_resale.setVisibility(View.GONE);
                iv_push_resale_faker.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


}
