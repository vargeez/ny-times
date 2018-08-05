package com.news.android.nytimes.listener;

import retrofit2.Response;

public interface ResponseListener {
    public void onSuccess(String requestTag, Object response);
    public void onFailure(Throwable error);
}
