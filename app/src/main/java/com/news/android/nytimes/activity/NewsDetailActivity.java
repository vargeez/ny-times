package com.news.android.nytimes.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.news.android.nytimes.R;
import com.news.android.nytimes.util.Constants;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class NewsDetailActivity extends AppCompatActivity {

    private final String TAG = "NewsDetail";

    private String detailedNewsUrl = "";
    private CircularProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().hasExtra(Constants.KEY_NEWS_TITLE)) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.KEY_NEWS_TITLE));
            getIntent().removeExtra(Constants.KEY_NEWS_TITLE);
        }
        if (getIntent().hasExtra(Constants.KEY_NEWS_DETAIL_URL)) {
            detailedNewsUrl = getIntent().getStringExtra(Constants.KEY_NEWS_DETAIL_URL);
            getIntent().removeExtra(Constants.KEY_NEWS_DETAIL_URL);
        }

        WebView webViewNewsDetail = (WebView) findViewById(R.id.web_view_news_detail);
        progressBar = (CircularProgressBar) findViewById(R.id.progress_bar);

        webViewNewsDetail.setWebViewClient(new NewsWebViewClient());
        webViewNewsDetail.loadUrl(detailedNewsUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class NewsWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG, "" + url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

}
