package com.xuhong.smarthome.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;

import com.xuhong.smarthome.R;

public class TemperatureView extends View {

    private float mWidth;
    private float mHeight;
    private float mMin;
    private Paint[] mPaints;
    private int mBackgroundColor;
    private int mThermometerColor;
    private RectF oval;
    private Scroller mScroller;
    private String[] mNumber = {"80", "60", "40", "20", "0", "-20", "-40"};
    private float mProgress;
    private int mValueColor = Color.RED;
    private float mValueSize = 36;

    public TemperatureView(Context context) {
        this(context, null);
    }

    public TemperatureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray mTypedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TemperatureView, defStyleAttr, 0);
        mBackgroundColor = mTypedArray.getColor(R.styleable.TemperatureView_backgroundColor, Color.GRAY);
        mThermometerColor = mTypedArray.getColor(R.styleable.TemperatureView_thermometerColor, Color.RED);
        mValueColor = mTypedArray.getColor(R.styleable.TemperatureView_valueColor, Color.GRAY);
        mValueSize = mTypedArray.getDimension(R.styleable.TemperatureView_valueSize, 36);
        mPaints = new Paint[2];
        for (int i = 0; i < mPaints.length; i++) {
            mPaints[i] = new Paint();
        }
        oval = new RectF();
        AccelerateInterpolator localAccelerateInterpolator = new AccelerateInterpolator();
        this.mScroller = new Scroller(context, localAccelerateInterpolator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        mMin = mHeight / 10;
        oval.set(mWidth / 2 - mMin * 0.75f, mMin * 8, mWidth / 2 + mMin * 0.75f,  mMin * 9.5f);

        // 绘制背景
        mPaints[0].setColor(mBackgroundColor);
        mPaints[0].setAntiAlias(true);
        mPaints[0].setStrokeCap(Paint.Cap.ROUND);
        mPaints[0].setStrokeWidth(mMin / 2);
        canvas.drawLine(mWidth / 2, mMin * 0.5f, mWidth/ 2, mMin * 8.5f, mPaints[0]);
        // 绘制底部圆
        mPaints[0].setColor(mThermometerColor);
        canvas.drawArc(oval, 0, 360, false, mPaints[0]);

        // 绘制刻度
        mPaints[1].setColor(mValueColor);
        mPaints[1].setStrokeWidth(mMin / 16);
        mPaints[1].setAntiAlias(true);
        mPaints[1].setTextSize(mValueSize);
        mPaints[1].setTextAlign(Paint.Align.CENTER);
        canvas.drawText("℃", mWidth / 2 + mMin * 0.75f, mMin * 0.75f, mPaints[1]);

        Rect rect = new Rect();
        mPaints[1].getTextBounds(mNumber[6], 0, mNumber[6].length(), rect);
        for (int i = 1; i < 8; i++) {
            canvas.drawLine(mWidth / 2 + mMin / 4, mMin * (i + 0.5f), mWidth / 2 + mMin / 2, mMin * (i + 0.5f), mPaints[1]);
            canvas.drawText(mNumber[i-1], mWidth / 2 + mMin / 2 + rect.width() / 3 * 2, mMin * (i + 0.5f) + rect.height() / 2, mPaints[1]);
        }
        // 进度条
        if (mProgress != 0f) {
            mPaints[0].setColor(mThermometerColor);
            mPaints[0].setStrokeCap(Paint.Cap.BUTT);
            canvas.drawLine(mWidth / 2, mMin * 8.5f, mWidth/ 2, mMin * (7.5f - 7 * mProgress / 140), mPaints[0]);
        }

    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void start(float progress) {
        setProgress(progress + 40);
        AnimatorSet animation = new AnimatorSet();
        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this, "progress", 0f, mProgress);
        progressAnimation.setDuration(3000);// 动画执行时间
        /*
         * AccelerateInterpolator　　　　　                  加速，开始时慢中间加速
         * DecelerateInterpolator　　　 　　                 减速，开始时快然后减速
         * AccelerateDecelerateInterolator　                     先加速后减速，开始结束时慢，中间加速
         * AnticipateInterpolator　　　　　　                 反向 ，先向相反方向改变一段再加速播放
         * AnticipateOvershootInterpolator　                 反向加超越，先向相反方向改变，再加速播放，会超出目的值然后缓慢移动至目的值
         * BounceInterpolator　　　　　　　                        跳跃，快到目的值时值会跳跃，如目的值100，后面的值可能依次为85，77，70，80，90，100
         * CycleIinterpolator　　　　　　　　                   循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2 *
         * mCycles * Math.PI * input) LinearInterpolator　　　 线性，线性均匀改变
         * OvershottInterpolator　　　　　　                  超越，最后超出目的值然后缓慢改变到目的值
         * TimeInterpolator　　　　　　　　　                        一个接口，允许你自定义interpolator，以上几个都是实现了这个接口
         */
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.playTogether(progressAnimation);//动画同时执行,可以做多个动画
        animation.start();
    }
}
