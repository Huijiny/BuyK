package com.buyk.crocompany.buyk_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.OnClick;

public class AgreeWebViewActivity extends AppCompatActivity {

    public WebView mWebView;
    public WebSettings mWebsetting;
    String usingRule = "http://115.68.226.54:8080/terms/provision/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agree_web_view_activity);

        mWebView = (WebView)this.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebsetting = mWebView.getSettings();
        mWebsetting.setJavaScriptEnabled(true);

        mWebView.loadUrl(usingRule);
    }
    @OnClick(R.id.backToLogin)
    public void goBackToLogin(View view){
        this.finish();
    }
    }
