package com.example.regent.moviegalleriapart_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.regent.moviegalleriapart_2.utils.Utils;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = ReviewActivity.class.getSimpleName();

    private String url;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        url = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        // if no string is passed, close the activity
        if (TextUtils.isEmpty(url)){
            finish();
        }

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        initWebView();

        webView.loadUrl(url);
    }

    private void initWebView(){
        progressBar.setMax(100);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browser, menu);

        if (Utils.isBookmarked(this, webView.getUrl())){
            Utils.tintMenuIcon(getApplicationContext(), menu.getItem(0), R.color.colorAccent);
        } else {
            Utils.tintMenuIcon(getApplicationContext(), menu.getItem(0), android.R.color.white);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // menu item 0 - index is bookmark icon

        // enable - disable the toolbar navigation icons
        if (!webView.canGoBack()){
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).getIcon().setAlpha(130);
        } else {
            menu.getItem(1).setEnabled(true);
            menu.getItem(1).getIcon().setAlpha(255);
        }

        if (!webView.canGoForward()){
            menu.getItem(2).setEnabled(false);
            menu.getItem(2).getIcon().setAlpha(130);
        } else {
            menu.getItem(2).setEnabled(true);
            menu.getItem(2).getIcon().setAlpha(255);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        if (item.getItemId() == R.id.action_bookmark){
            // bookmark / unbookmark the url
            Utils.bookmarkUrl(this, webView.getUrl());

            String msg = Utils.isBookmarked(this, webView.getUrl()) ?
                    webView.getTitle() + "is Bookmarked" : webView.getTitle() + "is removed!";
            Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            snackbar.show();

            // refresh the toolbar icons, so that bookmark icon color changes
            // depending on the bookmark status
            invalidateOptionsMenu();
        }

        if (item.getItemId() == R.id.action_back){
            back();
        }

        if (item.getItemId() == R.id.action_forward){
            forward();
        }

        return super.onOptionsItemSelected(item);
    }

    // backward the browser navigation
    private void back(){
        if (webView.canGoBack()){
            webView.goBack();
        }
    }

    // forward the browser navigation
    private void forward(){
        if (webView.canGoForward()){
            webView.goForward();
        }
    }

    private class MyWebChromeClient extends WebChromeClient{
        Context context;

        public MyWebChromeClient(Context context){
            super();
            this.context = context;
        }
    }*/

}
