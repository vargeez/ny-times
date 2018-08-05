package com.news.android.nytimes.manager;

import android.util.Log;

import com.news.android.nytimes.listener.ResponseListener;
import com.news.android.nytimes.model.NewsModel;
import com.news.android.nytimes.network.APIClient;
import com.news.android.nytimes.network.APIInterface;
import com.news.android.nytimes.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppManager {

    private final String TAG = "AppManager";
    APIInterface apiInterface;

    public AppManager() {
        apiInterface = APIClient.getClilent().create(APIInterface.class);
    }

    public void callApi(final String requestTag, final ResponseListener responseListener) {
        Call apiCall = null;
        switch (requestTag) {
            case Constants.API_MOST_VIEWED:
                apiCall = apiInterface.getMostViewed();
                break;
        }
        apiCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                responseListener.onSuccess(Constants.API_MOST_VIEWED, response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
}
