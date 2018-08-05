package com.news.android.nytimes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsModel implements Serializable{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("num_results")
    @Expose
    private Integer numberResults;
    @SerializedName("results")
    @Expose
    private List<NewsResultModel> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getNumberResults() {
        return numberResults;
    }

    public void setNumberResults(Integer numberResults) {
        this.numberResults = numberResults;
    }

    public List<NewsResultModel> getResults() {
        return results;
    }

    public void setResults(List<NewsResultModel> results) {
        this.results = results;
    }
}
