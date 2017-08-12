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

    /**
     *
     * 获取指定类型的新闻列表
     *
     * @param channel
     * @param start
     * @param num
     * @param appkey
     * @param callback
     */

    public void getMyNewsList(String channel, int start, int num, String appkey, Callback callback) {

        RequestBody requestBodyPost = new FormBody.Builder()
                .add("channel", channel)//要获取的新闻频道
                .add("num",  Integer.toString(num))//每次请求的数目
                .add("start", Integer.toString(start))//开始条目
                .add("appkey", appkey)//appkey
                .build();

        Request requestPost = new Request.Builder()
                .url("http://api.jisuapi.com/news/get")
                .post(requestBodyPost)
                .build();
        client.newCall(requestPost).enqueue(callback);
    }

    /**
     *
     * 获取关键字的新闻列表
     *
     * @param key
     * @param appkey
     * @param callback
     */

    public void getKeyList(String key, String appkey, Callback callback) {

        RequestBody requestBodyPost = new FormBody.Builder()
                .add("keyword", key)//要获取的新闻频道
                .add("appkey", appkey)//appkey
                .build();

        Request requestPost = new Request.Builder()
                .url("http://api.jisuapi.com/news/search")
                .post(requestBodyPost)
                .build();
        client.newCall(requestPost).enqueue(callback);
    }

}
