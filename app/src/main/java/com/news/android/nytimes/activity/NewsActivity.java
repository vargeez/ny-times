package com.news.android.nytimes.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.news.android.nytimes.util.CommonMethods;
import com.news.android.nytimes.R;
import com.news.android.nytimes.adapter.NewsAdapter;
import com.news.android.nytimes.listener.DialogClickListener;
import com.news.android.nytimes.listener.ResponseListener;
import com.news.android.nytimes.manager.AppManager;
import com.news.android.nytimes.model.NewsModel;
import com.news.android.nytimes.model.NewsResultModel;
import com.news.android.nytimes.util.ConnectionDetector;
import com.news.android.nytimes.util.Constants;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class NewsActivity extends AppCompatActivity implements ResponseListener, DialogClickListener {

    private final String TAG = "News";
    private List<NewsResultModel> mNews, mNewsList, mFilterableNews;
    private int visibleThreshold = 10;
    private NewsAdapter mNewsAdapter;
    private int lastVisibleItem;
    private CircularProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_launcher));

        mNews = new ArrayList<>();
        mNewsList = new ArrayList<>();
        mFilterableNews = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_news);
        progressBar = (CircularProgressBar) findViewById(R.id.progress_bar);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mNewsAdapter = new NewsAdapter(this, mNews);
        recyclerView.setAdapter(mNewsAdapter);

        callApi();

        //scroll listener for lazy loading
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if ((lastVisibleItem % (visibleThreshold - 1)) == 0 && mNews.size() != 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    loadData();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        setupSearch(menuItem);
        return true;
    }

    @Override
    public void onSuccess(String requestTag, Object response) {
        progressBar.setVisibility(View.GONE);
        switch (requestTag) {
            case Constants.API_MOST_VIEWED:
                NewsModel newsModel = (NewsModel) response;
                mNews.addAll(newsModel.getResults());
                mNewsList = (getNewsList(mNews, visibleThreshold));
                mNewsAdapter.notifiyList(mNews);
                break;
        }
    }

    @Override
    public void onFailure(Throwable error) {
        progressBar.setVisibility(View.GONE);
        CommonMethods.showDialog(this, Constants.TAG_NETWORK_ERROR, getResources().getString(R.string.info_network_error),
                error.getMessage().toString(), getResources().getString(R.string.info_okay), this);
    }

    private void callApi() {
        if (new ConnectionDetector(this).isConnectedToInternet()) {
            progressBar.setVisibility(View.VISIBLE);
            new AppManager().callApi(Constants.API_MOST_VIEWED, this);
        } else {
            CommonMethods.showDialog(this, Constants.TAG_NO_INTERNET, getResources().getString(R.string.info_no_internet),
                    getResources().getString(R.string.info_check_connection), getResources().getString(R.string.info_okay), this);
        }
    }

    //notify recycler view adapter
    private void loadData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                List<NewsResultModel> newsList = getNewsList(mNews, visibleThreshold);
                progressBar.setVisibility(View.GONE);
                mNewsAdapter.notifiyList(newsList);
            }
        });

    }

    //function to add items in lazy loading list
    private List<NewsResultModel> getNewsList(List<NewsResultModel> mNews, int offset) {
        List<NewsResultModel> newsList = new ArrayList<>();
        if (mNews.size() >= visibleThreshold) {
            newsList.addAll(mNews.subList(0, offset));
        } else {
            newsList.addAll(mNews);
        }
        mNewsList.addAll(newsList);
        mNews.removeAll(newsList);
        return mNewsList;
    }

    //search bar functionality
    private void setupSearch(MenuItem menuItem) {
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mNewsAdapter.getFilter().filter(s);
                return true;
            }
        });
    }


    //alert dialog handling
    @Override
    public void onPositiveClick(String requestTag, MaterialDialog dialog) {
        switch (requestTag) {
            case Constants.TAG_NO_INTERNET:
                dialog.dismiss();
                break;
            case Constants.TAG_NETWORK_ERROR:
                dialog.dismiss();
                break;
        }
    }
}
