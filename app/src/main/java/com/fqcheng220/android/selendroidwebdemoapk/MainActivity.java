package com.fqcheng220.android.selendroidwebdemoapk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private WebView mWebView;
    private String mWebUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.view_web);
        mWebView.setWebContentsDebuggingEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false
        /**
         * http://www.qonline.cc网站必须开启dom storage，否则显示空白
         */
        mWebView.getSettings().setDomStorageEnabled(true);//开启本地DOM存储
        /**
         * 不加这个的话
         * 高版本系统webview加载url跳转到系统浏览器
         */
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                android.util.Log.d(TAG, "shouldOverrideUrlLoading " + url);
                //                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                android.util.Log.d(TAG, "onPageStarted " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                android.util.Log.d(TAG, "onPageFinished " + url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //                super.onReceivedSslError(view, handler, error);
                android.util.Log.e(TAG, "onReceivedSslError");
                handler.proceed();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                android.util.Log.d(TAG, "onJsAlert " + ",url=" + url + ",message=" + message + ","
                        + "result=" + result);
                /**
                 * Android WebView 不能弹出alert的对话框
                 * 必须返回super.onJsAlert(view, url, message, result)
                 */
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d(TAG, "onConsoleMessage " + ",consoleMessage=" + consoleMessage);
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                android.util.Log.d(TAG, "onJsConfirm " + ",url=" + url + ",message=" + message +
                        "," + "result=" + result);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                android.util.Log.d(TAG, "onJsPrompt " + ",url=" + url + ",message=" + message +
                        "," + "result=" + result);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message,
                                            JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }

            @Override
            public boolean onJsTimeout() {
                return super.onJsTimeout();
            }
        });
        mWebView.loadUrl(mWebUrl);
    }

    private void parseIntent() {
        mWebUrl = "file:///android_asset/test.html";
        Intent intent = getIntent();
        if (intent != null) {
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                mWebUrl = intent.getDataString();
            }
        }
    }
}