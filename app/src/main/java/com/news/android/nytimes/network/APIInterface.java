package com.news.android.nytimes.network;

import com.news.android.nytimes.model.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface<T> {
    @GET("mostviewed/all-sections/7.json?api-key=9545b3467e8e4b23a69a9387d66641ca")
    Call<NewsModel> getMostViewed();
}
