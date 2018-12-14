package com.younghos0811.github.popular_movies.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.younghos0811.github.popular_movies.R;
import com.younghos0811.github.popular_movies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdaptor extends RecyclerView.Adapter {

    private Context mContext;
    private final int VIEW_MOVIE = 1;
    private final int VIEW_PROG = 0;
    private List<Movie> mMovieList;

    private int visibleThreshold = 4;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isNoneData = true;
    private boolean isFavorite = false;

    //click event handler
    private final MovieAdapterOnClickHandler mMovieClickHandler;

    /** click event Interface **/
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public MovieAdaptor(final List<Movie> movieList, RecyclerView recyclerView ,MovieAdapterOnClickHandler clickHandler ,Context context) {
        mMovieList = movieList;
        mContext = context;
        mMovieClickHandler = clickHandler;

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = gridLayoutManager.getItemCount();
                            lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                            if (!loading && !isNoneData && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                loading = true;
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                            }
                        }
                    });

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(loading && (position == mMovieList.size()-1) && !isFavorite) {
                        return 2;
                    }
                    else {
                        return 1;
                    }
                }
            });

        }
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList = movieList;
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    @Override
    public int getItemViewType(int position) {
        return mMovieList.get(position) != null ? VIEW_MOVIE : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        boolean shouldAttachToParentImmediately = false;

        if (viewType == VIEW_MOVIE) {
            //create movie item view holder
            int layoutIdForListItem = R.layout.movie_list_item;
            LayoutInflater inflater = LayoutInflater.from(mContext);

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            vh = new MovieAdapterViewHolder(view);
        } else {
            //create progressbar item view holder
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, shouldAttachToParentImmediately);
            vh = new ProgressViewHolder(v);
        }

        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MovieAdapterViewHolder) {

            RequestOptions options = new RequestOptions()
                    .centerCrop();

            Movie movie= (Movie)mMovieList.get(position);

            Glide.with(mContext)
                    .load(movie.getFullPath())
                    .apply(options)
                    .into(((MovieAdapterViewHolder) viewHolder).mMovieImageView);

            ((MovieAdapterViewHolder) viewHolder).movie = movie;


        } else {
            ((ProgressViewHolder)viewHolder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void addMovieListNull() {
        mMovieList.add(null);
        notifyItemInserted(mMovieList.size() - 1);
    }

    public void setLoaded() {
        loading = false;
        isNoneData = false;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void initAdaptor() {
        if(loading) {
            Log.i("adaptor test" , "loading .....");
        }
        loading = false;
        isNoneData = true;
        mMovieList = new ArrayList<>();
        notifyDataSetChanged();
    }










    /**
     * Cache of the children views for a Movie list item.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Movie movie;
        ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView)view.findViewById(R.id.movie_imageview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mMovieClickHandler.onClick(movie);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}
