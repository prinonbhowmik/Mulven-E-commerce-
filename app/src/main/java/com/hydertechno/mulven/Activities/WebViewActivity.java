package com.hydertechno.mulven.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hydertechno.mulven.Interface.WebAppInterface;
import com.hydertechno.mulven.Interface.WebMsgInterface;
import com.hydertechno.mulven.R;

import es.dmoral.toasty.Toasty;

public class WebViewActivity extends AppCompatActivity implements WebMsgInterface {
    private WebView webView;
    private RelativeLayout loadingProgress;
    private String Url;

    private TextView webPageTitle;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webPageTitle = findViewById(R.id.webPageTitle);
        webView = findViewById(R.id.webView);
        loadingProgress = findViewById(R.id.loadingProgress);

        Intent intent = getIntent();

        boolean isTerms = intent.getBooleanExtra("isTerms", false);
        Url =intent.getStringExtra("url");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("MyApplication", consoleMessage.message() + " -- From line " +
                        consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
//                if (consoleMessage.message().contains("TypeError")) {
//                    onMessage("Error");
//                }
                return true;
            }
        });
        webView.loadUrl(Url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        if (isTerms) {
            webPageTitle.setText("Terms And Conditions");
        } else {
            webPageTitle.setText("Payment");
        }
    }

    public void WebViewBack(View view) {
        finish();
    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else
            finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }

    @Override
    public void onMessage(String msg) {
        Intent intent = new Intent();
        intent.putExtra("result", msg);
        setResult(Activity.RESULT_OK, intent);
        WebViewActivity.this.finish();
    }
}