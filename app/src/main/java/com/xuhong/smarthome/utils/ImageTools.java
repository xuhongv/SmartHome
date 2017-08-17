package com.xuhong.smarthome.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/** 
 * Tools for handler picture 
 *  
 * @author Ryan.Tang 
 *  
 */  
public final class ImageTools {  
  
    /** 
     * Transfer drawable to bitmap 把drawable转换为bitmap
     *  
     * @param drawable 
     * @return 
     */  
    public static Bitmap drawableToBitmap(Drawable drawable) {  
        int w = drawable.getIntrinsicWidth();  
        int h = drawable.getIntrinsicHeight();  
  
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, w, h);  
        drawable.draw(canvas);  
        return bitmap;  
    }  
  
    /** 
     * Bitmap to drawable 
     *  
     * @param bitmap 
     * @return 
     */  
    public static Drawable bitmapToDrawable(Bitmap bitmap) {  
        return new BitmapDrawable(bitmap);  
    }  
  
    /** 
     * Input stream to bitmap 
     *  
     * @param inputStream 
     * @return 
     * @throws Exception 
     */  
    public static Bitmap inputStreamToBitmap(InputStream inputStream)  
            throws Exception {  
        return BitmapFactory.decodeStream(inputStream);  
    }  
  
    /** 
     * Byte transfer to bitmap 
     *  
     * @param byteArray 
     * @return 
     */  
    public static Bitmap byteToBitmap(byte[] byteArray) {  
        if (byteArray.length != 0) {  
            return BitmapFactory  
                    .decodeByteArray(byteArray, 0, byteArray.length);  
        } else {  
            return null;  
        }  
    }  
  
    /** 
     * Byte transfer to drawable 
     *  
     * @param byteArray 
     * @return 
     */  
    public static Drawable byteToDrawable(byte[] byteArray) {  
        ByteArrayInputStream ins = null;  
        if (byteArray != null) {  
            ins = new ByteArrayInputStream(byteArray);  
        }  
        return Drawable.createFromStream(ins, null);  
    }  
  
    /** 
     * Bitmap transfer to bytes 
     *  
     * @return
     */  
    public static byte[] bitmapToBytes(Bitmap bm) {  
        byte[] bytes = null;  
        if (bm != null) {  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
            bytes = baos.toByteArray();  
        }  
        return bytes;  
    }  
  
    /** 
     * Drawable transfer to bytes 
     *  
     * @param drawable 
     * @return 
     */  
    public static byte[] drawableToBytes(Drawable drawable) {  
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;  
        Bitmap bitmap = bitmapDrawable.getBitmap();  
        byte[] bytes = bitmapToBytes(bitmap);  
        ;  
        return bytes;  
    }  
  
    /** 
     * Base64 to byte[] 
//   */  
//  public static byte[] base64ToBytes(String base64) throws IOException {  
//      byte[] bytes = Base64.decode(base64);  
//      return bytes;  
//  }  
//  
//  /**  
//   * Byte[] to base64  
//   */  
//  public static String bytesTobase64(byte[] bytes) {  
//      String base64 = Base64.encode(bytes);  
//      return base64;  
//  }  
  
    /** 
     * Create reflection images 
     *  
     * @param bitmap 
     * @return 
     */  
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {  
        final int reflectionGap = 4;  
        int w = bitmap.getWidth();  
        int h = bitmap.getHeight();  
  
        Matrix matrix = new Matrix();  
        matrix.preScale(1, -1);  
  
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,  
                h / 2, matrix, false);  
  
        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),  
                Config.ARGB_8888);  
  
        Canvas canvas = new Canvas(bitmapWithReflection);  
        canvas.drawBitmap(bitmap, 0, 0, null);  
        Paint deafalutPaint = new Paint();  
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);  
  
        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);  
  
        Paint paint = new Paint();  
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,  
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,  
                0x00ffffff, TileMode.CLAMP);  
        paint.setShader(shader);  
        // Set the Transfer mode to be porter duff and destination in  
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
        // Draw a rectangle using the paint with our linear gradient  
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()  
                + reflectionGap, paint);  
  
        return bitmapWithReflection;  
    }  
  




    public static boolean copyFileUsingFileChannels(File source, File dest,boolean shouldDelete){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                if(shouldDelete){
                    //如果需要删除源文件
                    if (source.exists()){
                        source.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static Bitmap decodeUriAsBitmap(Uri uri, Context context) {
        Bitmap bitmap = null;
        try {
            // 先通过getContentResolver方法获得一个ContentResolver实例，
            // 调用openInputStream(Uri)方法获得uri关联的数据流stream
            // 把上一步获得的数据流解析成为bitmap
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}  