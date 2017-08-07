package com.xuhong.smarthome.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by admin on 2017/8/7.
 */

public class OkHttpUtils {

    //联网工具类对象，主要是联网进行一些操作
    public static OkHttpUtils instance;

    //1 创建一个OkHttp对象
    private static OkHttpClient client;

    public OkHttpUtils() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        Log.d("==w", "verify: 设置默认验证通过");
                        return true;
                    }
                })
                .build();
    }

    //网络请求的单例模式 一般在有3个线程以上时应用...
    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    //通用的根据url发送json数据 get请求
    public void sendCommon(String jsonUrl, Callback callback) {
        //2 创建一个网络请求
        Request request = new Request.Builder()
                //json数据的网址
                .url(jsonUrl)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    //提交注册所需的参数给服务器
    public void postRegister(String jsonUrl, String username, String userpwd, String userpwd2, String deviceID, String referrer, Callback callback) {
        Log.d("==w", "注册 提交账号密码: username:" + username);

        RequestBody requestBodyPost = new FormBody.Builder()
                .add("username", username)//用户名
                .add("password", userpwd)//密码
                .add("password2", userpwd2)//确认密码
                .add("device", deviceID)//设备号
                .add("rec_user", referrer)//推荐人信息
                .build();

        Request requestPost = new Request.Builder()
                .url(jsonUrl)
                .post(requestBodyPost)
                .build();
        client.newCall(requestPost).enqueue(callback);
    }

}
