package com.younghos0811.github.popular_movies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.younghos0811.github.popular_movies.adaptor.ReviewAdaptor;
import com.younghos0811.github.popular_movies.adaptor.VideoAdaptor;
import com.younghos0811.github.popular_movies.data.Movie;
import com.younghos0811.github.popular_movies.data.database.MovieDatabase;
import com.younghos0811.github.popular_movies.viewmodel.DetailViewModel;
import com.younghos0811.github.popular_movies.viewmodel.DetailViewModelFactory;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DetailMovieActivity extends AppCompatActivity implements VideoAdaptor.VideoAdapterOnClickHandler, ReviewAdaptor.ReviewOnClickHandler {

    final private static String TAG = DetailMovieActivity.class.getSimpleName();

    @BindView(R.id.movie_toolbar_image_view)
    ImageView mToolbarImgView;

    @BindView(R.id.detail_image_view)
    ImageView mImgView;

    @BindView(R.id.tv_tile)
    TextView mTitleView;

    @BindView(R.id.tv_release)
    TextView mReleaseView;

    @BindView(R.id.tv_rate)
    TextView mRateView;

    @BindView(R.id.tv_over_view)
    TextView mOverView;

    @BindView(R.id.recyclerview_video)
    RecyclerView mVideoRecyclerView;

    @BindView(R.id.recyclerview_review)
    RecyclerView mReviewRecyclerView;

    @BindView(R.id.favoriteBtn)
    FloatingActionButton mFavoriteMovieBtn;

    private VideoAdaptor mVideoAdaptor;
    private ReviewAdaptor mReviewAdaptor;
    private DetailViewModel viewModel;
    private MovieDatabase mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mDb = MovieDatabase.getInstance(getApplicationContext());
        setRecyclerViews();
        Movie movie = null;

        /** If First Init, so Send Movie Data to ViewModel **/
//        if(savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
                if(movie != null) {
                    setUpMovieUI(movie);
                }
            }
//        }

        /** set up ViewModel **/
        setupViewModel(movie);
    }


    /**
     * @Method
     * Set up ViewModel & Set Observing Live Data (Review , Video , Favorite Movie check)
     *
     * @param movie
     */
    private void setupViewModel(Movie movie) {
        DetailViewModelFactory factory = new DetailViewModelFactory(mDb,movie,BuildConfig.API_KEY);
        viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        viewModel.getReviewList().observe(this, reviewList -> {
            if(mReviewAdaptor.getReviewList().size() == 0) {
                mReviewAdaptor.setReviewList(reviewList);
            }
        });

        viewModel.getVideoList().observe(this, videoList -> {
            if(mVideoAdaptor.getVideowList().size() == 0) {
                mVideoAdaptor.setVideoList(videoList);
            }
        });

        viewModel.getFavoriteMovie().observe(this, favoriteMovie -> {
            if(favoriteMovie == null) {
                mFavoriteMovieBtn.setActivated(false);
            }
            else {
                mFavoriteMovieBtn.setActivated(true);
            }
        });
    }


    /**
     * @Method
     * Set RecyclerView & Create Adaptor (Review , Video)
     */
    private void setRecyclerViews() {
        /** Video RecyclerView **/
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideoRecyclerView.setHasFixedSize(true);
        mVideoRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mVideoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mVideoAdaptor = new VideoAdaptor( DetailMovieActivity.this , new ArrayList<>() , DetailMovieActivity.this);
        mVideoRecyclerView.setAdapter(mVideoAdaptor);

        /** Review RecyclerView **/
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mReviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReviewAdaptor = new ReviewAdaptor( DetailMovieActivity.this , new ArrayList<>() , DetailMovieActivity.this);
        mReviewRecyclerView.setAdapter(mReviewAdaptor);
    }


    /**
     * @Method
     * Bind UI Data throug Movie
     *
     * @param movie : Movie Information From MainActivity
     */
    private void setUpMovieUI(Movie movie) {
        String title = getResources().getString(R.string.movie_title) +" : \n " + movie.getTitle();
        String release = getResources().getString(R.string.movie_date)+ " : \n " + movie.getReleaseDay();
        String rate = getResources().getString(R.string.movie_rate) + " : \n " + movie.getRate() + " / 10.0";
        String overView =  movie.getOverView();

        getSupportActionBar().setTitle(movie.getTitle());
        mTitleView.setText(title);
        mReleaseView.setText(release);
        mRateView.setText(rate);
        mOverView.setText(overView);

        Glide.with(this)
                .load(movie.getFullPath())
                .into(mToolbarImgView);

        Glide.with(this)
                .load(movie.getFullPath())
                .into(mImgView);
    }


    /**
     * @Method
     * When click video item , Navigate UTUBE Video
     *
     * @param videoUrl
     */
    @Override
    public void onVideoClick(String videoUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(videoUrl))
                .setPackage(getResources().getString(R.string.utube_package_name));
        startActivity(intent);
    }


    /**
     * @Method
     * When click Review item , Review Site
     *
     * @param reviewUrl
     */
    @Override
    public void onReviewClick(String reviewUrl) {
        /** When click Review item , Navigate Review Site  **/
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(reviewUrl));
        startActivity(intent);
    }


    /**
     * @Method
     * When Click Favorite Buttn , Perform Insert or Delete
     *
     * @param view
     */
    @OnClick(R.id.favoriteBtn)
    void changeFavorite(View view) {
        if(view.isActivated()) {
            viewModel.deleteFavorite();
        }
        else {
            viewModel.insertFavorite();
        }
    }
}
