package com.xuhong.smarthome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;


public class StepsView extends View {
    /**
     * 默认距离边缘的距离
     */
    public static final int DEFAULT_PADDING = 20;
    /**
     * 未完成的步骤的颜色
     */
    public static final int COLOR_BAR_UNDONE = 0XFFdddde1;

    public void setmDoneColor(int mDoneColor) {
        this.mDoneColor = mDoneColor;
    }

    /**
     * 完成步骤的颜色
     */
    public static final int COLOR_BAR_DONE = 0XFF94c540;
    /**
     * 未完成步骤的标题颜色
     */
    public static final int COLOR_TITLE_BAR_UNDONE = 0XFFceced0;
    /**
     * 完成步骤的标题颜色
     */
    public static final int COLOR_TITLE_BAR_DONE = 0XFF828282;
    /**
     * 默认线条高度
     */
    public static final int DEFAULT_LINE_HEIGHT = 5;
    /**
     * 默认圆的半径
     */
    public static final int DEFAULT_CIRCLE_RADIUS = 50;

    private int mUnDoneColor = COLOR_BAR_UNDONE;
    private int mDoneColor = COLOR_BAR_DONE;
    private int titleUnDoneCloor = COLOR_TITLE_BAR_UNDONE;
    private int titleDoneCloor = COLOR_TITLE_BAR_DONE;
    private float mLineHeight = DEFAULT_LINE_HEIGHT;
    private float mRadius = DEFAULT_CIRCLE_RADIUS;

    private float mCenterY = 0.0f;
    private float mLeftX = 0.0f;
    private float mLeftY = 0.0f;
    private float mRightX = 0.0f;
    private float mRightY = 0.0f;
    private float mDistance = 0.0f;
    private int position = 1;

    private String titles[];


    public StepsView(Context context) {
        super(context);
    }

    public StepsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getDefaultWidth();
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        int height = 120;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算位置
        mCenterY = this.getHeight() / 2;
        mLeftX = this.getLeft() + getPaddingLeft() + dp2px(30);
        mLeftY = mCenterY - mLineHeight / 2;
        mRightX = this.getRight() - getPaddingRight();
        mRightY = mCenterY + mLineHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();

        /*****************初始化圆画笔***************/
        Paint mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        /*****************初始化圆画笔***************/

        /*****************初始化线条画笔***************/
        Paint mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        /*****************初始化线条画笔***************/

        /*****************初始化圆内数字画笔***************/
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(45.0f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
        textPaint.setColor(Color.WHITE);
        /*****************初始化圆内数字画笔***************/

        /*****************初始化圆标题画笔***************/
        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        titlePaint.setTextSize(30);
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
        titlePaint.setColor(mUnDoneColor);
        /*****************初始化圆标题画笔***************/

        mDistance = (getWidth() - 2 * mLeftX) / (titles.length - 1);//计算每个圆之间的相距距离

        /*****************根据标题个数开始画图***************/
        for (int i = 0; i < titles.length; i++) {
            String str = (i + 1) + "";//声明步骤数

            textPaint.getTextBounds(str, 0, str.length(), rect);//获取步骤数文字相关宽高属性

            /*****************根据当前所在步骤数（position）来对标题进行颜色改变，突出当前所在步骤***************/
            if (i == position - 1) {
                titlePaint.setColor(titleDoneCloor);
            } else {
                titlePaint.setColor(titleUnDoneCloor);
            }
            /*****************根据当前所在步骤数（position）来对标题进行颜色改变，突出当前所在步骤***************/

            /*****************根据当前所在步骤数（position）来进行已完成步骤与未完成步骤的颜色设置***************/
            if (i < position) {//判断当前所画步骤是否小于当前用户所在步骤
                //小于，证明本次所画步骤为已完成步骤
                mCirclePaint.setColor(mDoneColor);
                if (position - 1 == i) {
                    mLinePaint.setColor(mUnDoneColor);
                } else {
                    mLinePaint.setColor(mDoneColor);
                }
            } else {//大于，证明本次所画步骤为未完成步骤
                mCirclePaint.setColor(mUnDoneColor);
                mLinePaint.setColor(mUnDoneColor);
            }
            /*****************根据当前所在步骤数（position）来进行已完成步骤与未完成步骤的颜色设置***************/

            /*****************画两个步骤圆之间的连接线***************/
            if (i < titles.length - 1) {
                canvas.drawRect(mLeftX + i * mDistance + rect.width(), mLeftY, mLeftX + (i + 1) * mDistance, mRightY, mLinePaint);
            }

            if (position < titles.length) {
                mLinePaint.setColor(mDoneColor);
                canvas.drawRect(mLeftX + (position - 1) * mDistance + rect.width(), mLeftY, mLeftX + (2 * position - 1) * mDistance / 2, mRightY, mLinePaint);
            }
            /*****************画两个步骤圆之间的连接线***************/

            canvas.drawCircle(mLeftX + i * mDistance, mLeftY, mRadius, mCirclePaint);//开始画圆
            canvas.drawText(str, mLeftX + i * mDistance - rect.width() / 2, mLeftY + rect.height() / 2, textPaint);//开始写圆内步骤数


            titlePaint.getTextBounds(titles[i], 0, titles[i].length(), rect);//获取标题文字相关宽高属性
            /*****************计算标题坐标并画出***************/
            if (i == 0) {
                canvas.drawText(titles[i], mLeftX - rect.width() / 2, mLeftY + 90, titlePaint);
            } else if (i == titles.length - 1) {
                canvas.drawText(titles[i], getWidth() - mLeftX - rect.width() / 2, mLeftY + 90, titlePaint);
            } else {
                canvas.drawText(titles[i], mLeftX + i * mDistance - rect.width() / 2, mLeftY + 90, titlePaint);
            }
            /*****************计算标题坐标并画出***************/


        }
        /*****************根据标题个数开始画图***************/

    }

    /**
     * 下一步
     */
    public void next() {
        if (position < titles.length) {
            position++;
            invalidate();
        }
    }

    /**
     * 上一步
     */
    public void back() {
        if (position > 1) {
            position--;
            invalidate();
        }
    }

    /**
     * 重置
     */
    public void reset() {
        if (position != 1) {
            position = 1;
            invalidate();
        }
    }

    public void setTitle(String[] titles) {
        this.titles = titles;
    }

    /**
     * 得到默认的StepBar的宽度
     *
     * @return
     */
    private int getDefaultWidth() {
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        return screenWidth - 2 * dp2px(DEFAULT_PADDING);
    }

    /**
     * dp单位和px单位转换
     *
     * @param dp
     * @return
     */
    public int dp2px(int dp) {
        return (int) (this.getContext().getResources().getDisplayMetrics().density * dp + 0.5);
    }

    public void setmUnDoneColor(int mUnDoneColor) {
        this.mUnDoneColor = mUnDoneColor;
    }


}