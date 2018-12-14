package com.younghos0811.github.popular_movies.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.younghos0811.github.popular_movies.R;
import com.younghos0811.github.popular_movies.data.Review;

import java.util.List;

public class ReviewAdaptor extends RecyclerView.Adapter {

    private Context mContext;
    private List<Review> mReviewList;
    private ReviewOnClickHandler mClickHandler;

    public interface ReviewOnClickHandler {
        void onReviewClick(String reviewUrl);
    }

    public ReviewAdaptor(Context context , List<Review> reviewList , ReviewAdaptor.ReviewOnClickHandler clickHandler) {
        mReviewList = reviewList;
        mContext = context;
        mClickHandler = clickHandler;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        boolean shouldAttachToParentImmediately = false;
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        vh = new ReviewAdapterViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Review review = mReviewList.get(position);
        String title = "- " + review.getAuthor() + " -";

        ReviewAdapterViewHolder reviewViewholder = (ReviewAdapterViewHolder)viewHolder;
        reviewViewholder.mReviewAuthorView.setText(title);
        reviewViewholder.mReviewContent.setText(review.getContent());
        reviewViewholder.reviewUrl =review.getUrl();
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public List<Review> getReviewList() {
        return mReviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        mReviewList.addAll(reviewList);
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mReviewAuthorView;
        TextView mReviewContent;
        String reviewUrl;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthorView = (TextView)view.findViewById(R.id.review_autor);
            mReviewContent = (TextView)view.findViewById(R.id.review_content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onReviewClick(reviewUrl);
        }
    }
}
