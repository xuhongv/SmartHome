package com.xuhong.smarthome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.util.TypedValue;

/**
 * 项目名： SeekBarColorPicker-master
 * 包名： com.csdn.seekbarcolorpicker_master
 * 文件名字： SeekBarColorPicker
 * 创建时间：2017/10/31 20:27
 * 项目名： Xuhong
 * 描述： 七彩色圆环采集器
 */

public class SeekBarColorPicker extends View {

    //圆环的画笔
    private Paint paintCircleRing;

    //最里面的圆的画笔，默认是绿色
    private Paint paintInnerColor;

    //圆形选择器
    private Paint paintSelecter;

    //渐变色环参数：红、紫、蓝、绿、黄、橙、红
    private final int[] mCircleColors = new int[]{0xFFFF0000, 0xFFFF00FF,
            0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};
    //宽度
    private int width;
    //高度
    private int height;

    //圆环的宽
    private int barWidth = 8;

    //内圆的半径
    private float innerRadius;
    //外圆的半径
    private float outerRadius;

    //圆的坐标
    private float circleX, circleY;

    private float markPointX, markPointY;

    //默认角度
    private int angle = 0;

    //开启机智云的rgb灯模式,默认不开启
    private boolean isGizwitLight = false;

    public SeekBarColorPicker(Context context) {
        super(context);
        init();
    }

    public SeekBarColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeekBarColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        // 渐变色环参数
        paintCircleRing = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircleRing.setAntiAlias(true);
        paintCircleRing.setStyle(Paint.Style.STROKE);
        paintCircleRing.setStrokeWidth(30);

        //内圆参数
        paintInnerColor = new Paint();
        paintInnerColor.setColor(Color.GREEN);
        paintInnerColor.setAntiAlias(true);
        paintInnerColor.setStrokeWidth(5);

        //选择器
        paintSelecter = new Paint();
        paintSelecter.setColor(Color.WHITE);
        paintSelecter.setAntiAlias(true);
        paintSelecter.setStrokeWidth(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        barWidth = dp2px(getContext(), 10);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //选择最小
        int size = (width > height) ? height : width;
        //确定圆的x坐标中心点
        circleX = width / 2;
        //确定圆的Y坐标中心点
        circleY = height / 2;
        //分辨率适配:获取圆环的宽度 圆环的半径 内部圆的半径
        if (width <= 480) {
            paintCircleRing.setStrokeWidth(40);
            outerRadius = size / 2 - dp2px(getContext(), 40);
            innerRadius = outerRadius - barWidth;
        } else if (width > 480 && width <= 720) {
            paintCircleRing.setStrokeWidth(45);
            outerRadius = size / 2 - dp2px(getContext(), 60);
            innerRadius = outerRadius - barWidth;
        } else if (width > 720 && width <= 1080) {
            paintCircleRing.setStrokeWidth(80);
            outerRadius = size / 2 - dp2px(getContext(), 60);
            innerRadius = outerRadius - barWidth;
        } else {
            paintCircleRing.setStrokeWidth(100);
            outerRadius = size / 2 - dp2px(getContext(), 70);
            innerRadius = outerRadius - barWidth;
        }
        //circleX 渲染中心点x坐标，circleY渲染中心y点坐标；mCircleColors为渐变颜色内容
        Shader s = new SweepGradient(circleX, circleY, mCircleColors, null);
        paintCircleRing.setShader(s);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画色环
        canvas.drawCircle(circleX, circleY, outerRadius, paintCircleRing);
        //画最里面的圆，默认是绿色
        canvas.drawCircle(circleX, circleY, innerRadius / 3, paintInnerColor);

        setInitMarkToXY(getAngle());

        float frameRadius = 0;
        if (width <= 480) {
            frameRadius = outerRadius - innerRadius + dp2px(getContext(), 7) / 2;
        } else if (width > 480 && width <= 720) {
            frameRadius = outerRadius - innerRadius + dp2px(getContext(), 10) / 2;
        } else if (width > 720 && width <= 1080) {
            frameRadius = outerRadius - innerRadius + dp2px(getContext(), 7) / 2;
        } else {
            frameRadius = outerRadius - innerRadius + dp2px(getContext(), 8) / 2;
        }

        canvas.drawCircle(markPointX, markPointY, frameRadius, paintSelecter);

        super.onDraw(canvas);
    }

    private void setInitMarkToXY(int angle) {
        markPointX = (float) (circleX + outerRadius
                * Math.sin(angle * Math.PI / 180));
        markPointY = (float) (circleY - outerRadius
                * Math.cos(angle * Math.PI / 180));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float markRange = dp2px(getContext(), 60);
        float x = event.getX();
        float y = event.getY();
        boolean up = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < markPointX + markRange && x > markPointX - markRange
                        && y > markPointY - markRange && y < markPointY + markRange) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_UP:
                up = true;
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                up = true;
                moved(x, y, up);
                break;
        }
        return true;
    }


    private boolean isMarkPointRange(float x, float y) {
        float range = dp2px(getContext(), 60);
        return x > (markPointX - range) && x < (markPointX + range) && y > (markPointY - range) && y < (markPointY + range);
    }

    private void moved(float x, float y, boolean up) {

        //判断触摸点是否在圆环内
        if (!isMarkPointRange(x, y)) {
            return;
        }

        float distance = (float) Math.sqrt(Math.pow((x - circleX), 2)
                + Math.pow((y - circleY), 2));

        if (distance < outerRadius + 100 && distance > innerRadius - 100 && !up) {

            markPointX = (float) (circleX + outerRadius * Math.cos(Math.atan2(x - circleX, circleY - y) - (Math.PI / 2)));
            markPointY = (float) (circleY + outerRadius * Math.sin(Math.atan2(x - circleX, circleY - y) - (Math.PI / 2)));

            float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x - circleX, circleY - y)) + 360.0)) % 360.0);

            // 注意：为负数要加360°
            if (degrees < 0) {
                degrees += 2 * Math.PI;
            }

            //改变内部圆的颜色
            int CircleColor = interpCircleColor(mCircleColors, degrees);

            if (isGizwitLight) {

                int data_LED_R = Color.red(CircleColor);
                int data_LED_G = Color.green(CircleColor);
                int data_LED_B = Color.blue(CircleColor);

                if (data_LED_R == 255) {
                    data_LED_R = 254;
                }
                if (data_LED_G == 255) {
                    data_LED_G = 254;
                }
                if (data_LED_B == 255) {
                    data_LED_B = 254;
                }
                CircleColor = Color.argb(255, data_LED_R, data_LED_G, data_LED_B);
            }

            paintInnerColor.setColor(CircleColor);

            //角度四舍五入
            this.angle = Math.round(degrees);
            invalidate();

        } else {
            if (mSeekBarColorPickerChangeListener != null) {
                String toHexString = Integer.toHexString(paintInnerColor.getColor());
                mSeekBarColorPickerChangeListener.onProgressChange(this, paintInnerColor.getColor(), "#" + toHexString.substring(2, toHexString.length()));
            }
            invalidate();
        }

    }

    //获取圆环上颜色
    private int interpCircleColor(int colors[], float degree) {
        degree -= 90;

        if (degree < 0)
            degree += 360;

        float p = degree * (colors.length - 1) / 360;
        int i = (int) p;
        p -= i;

        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);
        return Color.argb(a, r, g, b);
    }

    private float fromColor2Degree(int color) {

        float degree = 0;
        int diff = 360 / (mCircleColors.length - 1);

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int[] mColor = {b, g, r};

        // 把最大的，置0xFF，最小的，置0
        int min = findMin(b, g, r);
        int max = findMax(b, g, r);

        int temp = (0xff << (max * 8)) + (0xff << (8 * 3));

        if (max == min) {//证明RGB相等；
            return 90;// 九十度
        }

        int mid = 3 - max - min;
        int start = 0;
        int end = 0;
        for (int i = 0; i < mCircleColors.length - 2; i++) {
            if (mCircleColors[i] - temp == 0)
                start = i;
            if (mCircleColors[i] - temp == (0xff << (mid * 8)))
                end = i;
        }
        float percent = (float) mColor[mid] / (float) 0xff;
        int degreeDiff = (int) (percent * diff);

        if (start < end) {
            degree = start * diff;
            degree += degreeDiff;
        } else {
            degree = start * diff;
            degree -= degreeDiff;
        }

        degree += 90;

        if (degree > 360)
            degree -= 360;
        return degree;
    }

    private int colorFilter(int color) {
        int result = 0;

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int[] mColor = {b, g, r};

        // 把最大的，置0xFF，最小的，置0
        int min = findMin(b, g, r);
        int max = findMax(b, g, r);

        if (mColor[min] != 0 || mColor[max] != 0xff)
            return result;

        result = Color.argb(255, mColor[2], mColor[1], mColor[0]);

        return result;
    }

    private int findMin(int one, int two, int three) {
        if (one < two && one < three) {
            return 0;
        } else if (two < three) {
            return 1;
        } else {
            return 2;
        }
    }

    private int findMax(int one, int two, int three) {
        if (one > two && one > three) {
            return 0;
        } else if (two > three) {
            return 1;
        } else {
            return 2;
        }
    }

    private int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private int getAngle() {
        return angle;
    }


    public interface SeekBarColorPickerChangeListener {
        void onProgressChange(SeekBarColorPicker seekBarColorPicker, int color, String htmlColor);
    }

    public void setSeekBarColorPickerChangeListener(SeekBarColorPickerChangeListener mSeekBarColorPickerChangeListener) {
        this.mSeekBarColorPickerChangeListener = mSeekBarColorPickerChangeListener;
    }

    private SeekBarColorPickerChangeListener mSeekBarColorPickerChangeListener;

    /**
     * 通过int设置color
     *
     * @param color 整型
     */
    public void setColorByInt(int color) {


        if (isGizwitLight) {

            int data_LED_R = Color.red(color);
            int data_LED_G = Color.green(color);
            int data_LED_B = Color.blue(color);

            if (data_LED_R == 254) {
                data_LED_R = 255;
            }
            if (data_LED_G == 254) {
                data_LED_G = 255;
            }
            if (data_LED_B == 254) {
                data_LED_B = 255;
            }
            color = Color.argb(255, data_LED_R, data_LED_G, data_LED_B);
        }

        int colorFilter = colorFilter(color);
        // 颜色格式不符合，不做响应
        if (colorFilter == 0) {
            return;
        }
        // set内圈的颜色
        paintInnerColor.setColor(colorFilter);
        // set外圈的角度
        float degree = fromColor2Degree(colorFilter);
        this.angle = Math.round(degree);

    }

    /**
     * html的十六进制方式转换rgb
     *
     * @param htmlRGB 比如#eee03e
     */
    public void setColorByhtmlRGB(String htmlRGB) {

        if (htmlRGB.indexOf("#") != 0) {
            return;
        }

        if (htmlRGB.length() != 7) {
            return;
        }

        int red = Integer.parseInt(htmlRGB.substring(1, 3), 16);
        int green = Integer.parseInt(htmlRGB.substring(3, 5), 16);
        int blue = Integer.parseInt(htmlRGB.substring(5, 7), 16);

        int result = Color.argb(255, red, green, blue);

        if (isGizwitLight) {

            int data_LED_R = Color.red(result);
            int data_LED_G = Color.green(result);
            int data_LED_B = Color.blue(result);

            if (data_LED_R == 254) {
                data_LED_R = 255;
            }
            if (data_LED_G == 254) {
                data_LED_G = 255;
            }
            if (data_LED_B == 254) {
                data_LED_B = 255;
            }
            result = Color.argb(255, data_LED_R, data_LED_G, data_LED_B);
        }

        // set内圈的颜色
        paintInnerColor.setColor(result);

        // set外圈的角度
        float degree = fromColor2Degree(result);
        this.angle = Math.round(degree);
    }


    public boolean isGizwitLight() {
        return isGizwitLight;
    }

    public void setGizwitLight(boolean gizwitLight) {
        isGizwitLight = gizwitLight;
    }

}