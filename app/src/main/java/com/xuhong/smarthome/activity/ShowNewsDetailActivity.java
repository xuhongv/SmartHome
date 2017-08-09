package com.xuhong.smarthome.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.xuhong.smarthome.R;

import java.lang.reflect.Method;

public class ShowNewsDetailActivity extends AppCompatActivity {

    private WebView mWebView;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_detail);

        //显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //取消阴影
        getSupportActionBar().setElevation(0);


        initData();
        initView();
    }

    private void initData() {

        Intent intent = this.getIntent();
        webUrl = intent.getStringExtra("_webUrl");
        String webTitle = intent.getStringExtra("_webTitle");
        //设置标题
        setTitle(webTitle);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWebView = (WebView) findViewById(R.id.mWebView);
        WebSettings webSettings = mWebView.getSettings();
        //支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        //接口回调
        mWebView.setWebChromeClient(new WebViewClient());
        mWebView.loadUrl(webUrl);
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(webUrl);
                //我接受这个事件
                return true;
            }
        });
    }


    public class WebViewClient extends WebChromeClient {

        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {

            }
            super.onProgressChanged(view, newProgress);
        }
    }


    //自定义菜单显示加载
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //自定义菜单和返回键的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //如果点击了返回键，则关闭当前Activity
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //让菜单显示图片
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }


}
