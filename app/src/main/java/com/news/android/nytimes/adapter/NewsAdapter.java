package com.news.android.nytimes.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.news.android.nytimes.R;
import com.news.android.nytimes.activity.NewsDetailActivity;
import com.news.android.nytimes.model.NewsResultModel;
import com.news.android.nytimes.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> implements Filterable {

    private final String TAG = "NewsAdapter";
    private Context context;
    private List<NewsResultModel> mNewsList, mFilteredNewsList;
    private FilterNews mFilterNews;

    public NewsAdapter(Context context, List<NewsResultModel> mNewsList) {
        this.context = context;
        this.mNewsList = mNewsList;
        this.mFilteredNewsList = mNewsList;
        mFilterNews = new FilterNews(this);
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_news, viewGroup, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder newsHolder, final int i) {
        newsHolder.tvTitle.setText(mFilteredNewsList.get(i).getTitle());
        newsHolder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(Constants.KEY_NEWS_TITLE, mFilteredNewsList.get(i).getTitle());
                intent.putExtra(Constants.KEY_NEWS_DETAIL_URL, mFilteredNewsList.get(i).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredNewsList.size();
    }

    public void notifiyList(List<NewsResultModel> mNewsList) {
        this.mNewsList = mNewsList;
        this.mFilteredNewsList = mNewsList;
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilterNews;
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        private CardView cvParent;
        private TextView tvTitle;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            cvParent = (CardView) itemView.findViewById(R.id.cv_parent);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    private class FilterNews extends Filter {

        private NewsAdapter mNewsAdapter;

        public FilterNews(NewsAdapter mNewsAdapter) {
            this.mNewsAdapter = mNewsAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();
            if (charString.isEmpty()) {
                //mFilteredNewsList = new ArrayList<NewsResultModel>();
            } else {
                ArrayList<NewsResultModel> filteredList = new ArrayList<>();
                for (NewsResultModel androidVersion : mNewsList) {

                    if (androidVersion.getTitle().toLowerCase().contains(charString)) {
                        filteredList.add(androidVersion);
                    }
                }
                mFilteredNewsList = filteredList;

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = mFilteredNewsList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilteredNewsList = (ArrayList<NewsResultModel>) filterResults.values;
            this.mNewsAdapter.notifyDataSetChanged();
        }
    }
}
