package com.buyk.crocompany.buyk_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.OnClick;

public class PrivateInformationUsingAgreeWebView extends Activity{
    public WebView mWebView;
    public WebSettings mWebsetting;
    String agreePrivateInfo = "http://115.68.226.54:8080/terms/personal-data/";
    TextView title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agree_web_view_activity);
        title = (TextView)this.findViewById(R.id.agreeViewTitle);
        mWebView = (WebView) this.findViewById(R.id.webView);
        title.setText("BuyK 개인정보취급방침");
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebsetting = mWebView.getSettings();
        mWebsetting.setJavaScriptEnabled(true);

        mWebView.loadUrl(agreePrivateInfo);
    }

    @OnClick(R.id.backToLogin)
    public void goBackToLogin(View view) {
        this.finish();
    }

}
