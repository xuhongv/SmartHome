package com.xuhong.smarthome.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.L;

public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private String webUrl;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initData();
        initView();
    }

    private void initData() {


        Intent intent = this.getIntent();
        webUrl = intent.getStringExtra("_webUrl");
        String webTitle = intent.getStringExtra("_webTitle");
        L.e("_webUrl" + webUrl);
        //设置标题

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, toolbar);
        toolbar.setTitle(webTitle);
        toolbar.inflateMenu(R.menu.menu_news_detail);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share_sina:
                        break;
                    case R.id.menu_shareweichatCircle:
                        break;
                    case R.id.menu_share_weichatfriend:
                        break;
                    case R.id.menu_shareQQ:
                        break;
                    case R.id.menu_shareCollection:
                        break;

                }
                return true;
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWebView = (WebView) findViewById(R.id.mWebView);
        //支持JS
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
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


}
