package com.xuhong.smarthome.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xuhong.smarthome.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 项目名： SmartHome-master
 * 包名名： com.xuhong.smarthome.view
 * 创建者: xuhong  GitHub-> https://github.com/xuhongv
 * 创建时间: 2017/11/9.
 * 描述：TOMO
 */

public class BubbleView extends RelativeLayout {
    private List<Drawable> drawableList = new ArrayList();
    private int viewWidth = this.dp2pix(16);
    private int viewHeight = this.dp2pix(16);
    private int maxHeartNum = 8;
    private int minHeartNum = 2;
    private int riseDuration = 4000;
    private int bottomPadding = 200;
    private int originsOffset = 60;
    private float maxScale = 1.0F;
    private float minScale = 1.0F;
    private int innerDelay = 200;

    public BubbleView(Context context) {
        super(context);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BubbleView setDefaultDrawableList() {
        List<Drawable> drawableList = new ArrayList();
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_indigo_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_cyan_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_blue_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_light_blue_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_lime_a200_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_pink_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_purple_900_24dp));
        drawableList.add(this.getResources().getDrawable(R.drawable.ic_favorite_red_900_24dp));
        this.setDrawableList(drawableList);
        return this;
    }

    public BubbleView setDrawableList(List<Drawable> drawableList) {
        this.drawableList = drawableList;
        return this;
    }

    public BubbleView setRiseDuration(int riseDuration) {
        this.riseDuration = riseDuration;
        return this;
    }

    public BubbleView setBottomPadding(int px) {
        this.bottomPadding = px;
        return this;
    }

    public BubbleView setOriginsOffset(int px) {
        this.originsOffset = px;
        return this;
    }

    public BubbleView setScaleAnimation(float maxScale, float minScale) {
        this.maxScale = maxScale;
        this.minScale = minScale;
        return this;
    }

    public BubbleView setAnimationDelay(int delay) {
        this.innerDelay = delay;
        return this;
    }

    public void setMaxHeartNum(int maxHeartNum) {
        this.maxHeartNum = maxHeartNum;
    }

    public void setMinHeartNum(int minHeartNum) {
        this.minHeartNum = minHeartNum;
    }

    public BubbleView setItemViewWH(int viewWidth, int viewHeight) {
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
        return this;
    }

    public BubbleView setGiftBoxImaeg(Drawable drawable, int positionX, int positionY) {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(drawable);
        LayoutParams layoutParams = new LayoutParams(imageView.getWidth(), imageView.getHeight());
        this.addView(imageView, layoutParams);
        imageView.setX((float)positionX);
        imageView.setY((float)positionY);
        return this;
    }

    public void startAnimation(final int rankWidth, final int rankHeight) {
        Observable.timer((long)this.innerDelay, TimeUnit.MILLISECONDS).repeat((long)((int)(Math.random() * (double)(this.maxHeartNum - this.minHeartNum)) + this.minHeartNum)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            public void call(Long aLong) {
                BubbleView.this.bubbleAnimation(rankWidth, rankHeight);
            }
        });
    }

    public void startAnimation(final int rankWidth, final int rankHeight, int count) {
        Observable.timer((long)this.innerDelay, TimeUnit.MILLISECONDS).repeat((long)count).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            public void call(Long aLong) {
                BubbleView.this.bubbleAnimation(rankWidth, rankHeight);
            }
        });
    }

    public void startAnimation(final int rankWidth, final int rankHeight, int delay, int count) {
        Observable.timer((long)delay, TimeUnit.MILLISECONDS).repeat((long)count).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            public void call(Long aLong) {
               BubbleView.this.bubbleAnimation(rankWidth, rankHeight);
            }
        });
    }

    private void bubbleAnimation(int rankWidth, int rankHeight) {
        rankHeight -= this.bottomPadding;
        int seed = (int)(Math.random() * 3.0D);
        switch(seed) {
            case 0:
                rankWidth -= this.originsOffset;
                break;
            case 1:
                rankWidth += this.originsOffset;
                break;
            case 2:
                rankHeight -= this.originsOffset;
        }

        LayoutParams layoutParams = new LayoutParams(this.viewWidth, this.viewHeight);
        int heartDrawableIndex = (int)((double)this.drawableList.size() * Math.random());
        ImageView tempImageView = new ImageView(this.getContext());
        tempImageView.setImageDrawable((Drawable)this.drawableList.get(heartDrawableIndex));
        this.addView(tempImageView, layoutParams);
        ObjectAnimator riseAlphaAnimator = ObjectAnimator.ofFloat(tempImageView, "alpha", new float[]{1.0F, 0.0F});
        riseAlphaAnimator.setDuration((long)this.riseDuration);
        ObjectAnimator riseScaleXAnimator = ObjectAnimator.ofFloat(tempImageView, "scaleX", new float[]{this.minScale, this.maxScale});
        riseScaleXAnimator.setDuration((long)this.riseDuration);
        ObjectAnimator riseScaleYAnimator = ObjectAnimator.ofFloat(tempImageView, "scaleY", new float[]{this.minScale, this.maxScale});
        riseScaleYAnimator.setDuration((long)this.riseDuration);
        ValueAnimator valueAnimator = this.getBesselAnimator(tempImageView, rankWidth, rankHeight);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(valueAnimator).with(riseAlphaAnimator).with(riseScaleXAnimator).with(riseScaleYAnimator);
        animatorSet.start();
    }

    private ValueAnimator getBesselAnimator(final ImageView imageView, int rankWidth, int rankHeight) {
        float[] point0 = new float[]{(float)(rankWidth / 2), (float)rankHeight};
        float[] point1 = new float[]{(float)((double)rankWidth * 0.1D) + (float)(Math.random() * (double)rankWidth * 0.8D), (float)((double)rankHeight - Math.random() * (double)rankHeight * 0.5D)};
        float[] point2 = new float[]{(float)(Math.random() * (double)rankWidth), (float)(Math.random() * (double)((float)rankHeight - point1[1]))};
        float[] point3 = new float[]{(float)(Math.random() * (double)rankWidth), 0.0F};
        BubbleView.BesselEvaluator besselEvaluator = new BubbleView.BesselEvaluator(point1, point2);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(besselEvaluator, new Object[]{point0, point3});
        valueAnimator.setDuration((long)this.riseDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float[] currentPosition = new float[2];
                currentPosition = (float[])((float[])animation.getAnimatedValue());
                imageView.setTranslationX(currentPosition[0]);
                imageView.setTranslationY(currentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
               BubbleView.this.removeView(imageView);
                imageView.setImageDrawable((Drawable)null);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        return valueAnimator;
    }

    private int dp2pix(int dp) {
        float scale = this.getResources().getDisplayMetrics().density;
        int pix = (int)((float)dp * scale + 0.5F);
        return pix;
    }

    private int pix2dp(int pix) {
        float scale = this.getResources().getDisplayMetrics().density;
        int dp = (int)((float)pix / scale + 0.5F);
        return dp;
    }

    public class BesselEvaluator implements TypeEvaluator<float[]> {
        private float[] point1 = new float[2];
        private float[] point2 = new float[2];

        public BesselEvaluator(float[] point1, float[] point2) {
            this.point1 = point1;
            this.point2 = point2;
        }

        public float[] evaluate(float fraction, float[] point0, float[] point3) {
            float[] currentPosition = new float[]{point0[0] * (1.0F - fraction) * (1.0F - fraction) * (1.0F - fraction) + this.point1[0] * 3.0F * fraction * (1.0F - fraction) * (1.0F - fraction) + this.point2[0] * 3.0F * (1.0F - fraction) * fraction * fraction + point3[0] * fraction * fraction * fraction, point0[1] * (1.0F - fraction) * (1.0F - fraction) * (1.0F - fraction) + this.point1[1] * 3.0F * fraction * (1.0F - fraction) * (1.0F - fraction) + this.point2[1] * 3.0F * (1.0F - fraction) * fraction * fraction + point3[1] * fraction * fraction * fraction};
            return currentPosition;
        }
    }
}
