package com.younghos0811.github.popular_movies.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.younghos0811.github.popular_movies.R;
import com.younghos0811.github.popular_movies.data.Video;

import java.util.List;


public class VideoAdaptor extends RecyclerView.Adapter {

    private Context mContext;
    private List<Video> mVideoList;
    private VideoAdapterOnClickHandler mClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onVideoClick(String video);
    }

    public VideoAdaptor(Context context , List<Video> videoList , VideoAdapterOnClickHandler clickHandler) {
        mVideoList = videoList;
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        boolean shouldAttachToParentImmediately = false;
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        vh = new VideoAdapterViewHolder(view);

        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round);

        Video thisVideo = mVideoList.get(position);
        Glide.with(mContext)
                .load(thisVideo.getImageUrl())
                .apply(options)
                .into(((VideoAdapterViewHolder)viewHolder).mVideoImageView);

        ((VideoAdapterViewHolder) viewHolder).utubeUrl = thisVideo.getWatchUrl();
        ((VideoAdapterViewHolder) viewHolder).mVideoNameView.setText(thisVideo.getName());

    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public List<Video> getVideowList() {
        return mVideoList;
    }

    public void setVideoList(List<Video> videoList) {
        mVideoList.addAll(videoList);
        notifyDataSetChanged();
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mVideoImageView;
        TextView mVideoNameView;
        String utubeUrl;

        public VideoAdapterViewHolder(View view) {
            super(view);
            mVideoImageView = (ImageView)view.findViewById(R.id.video_imageview);
            mVideoNameView = (TextView)view.findViewById(R.id.video_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO: WHY IMAGE VIEW Clck ??
            mClickHandler.onVideoClick(utubeUrl);
        }
    }
}
