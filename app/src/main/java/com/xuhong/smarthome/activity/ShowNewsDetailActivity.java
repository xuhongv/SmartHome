package com.xuhong.smarthome.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.xuhong.smarthome.R;

public class ShowNewsDetailActivity extends AppCompatActivity {

    private WebView mWebView;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_detail);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = this.getIntent();
        webUrl = intent.getStringExtra("_webUrl");


    }


    private void initView() {
        mWebView = (WebView) findViewById(R.id.mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(webUrl);
    }
}
