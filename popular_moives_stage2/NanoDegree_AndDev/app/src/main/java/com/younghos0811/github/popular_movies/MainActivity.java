package com.younghos0811.github.popular_movies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

import com.younghos0811.github.popular_movies.adaptor.MovieAdaptor;
import com.younghos0811.github.popular_movies.data.Movie;
import com.younghos0811.github.popular_movies.viewmodel.MainViewModel;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MovieAdaptor.MovieAdapterOnClickHandler {

    final private static String TAG = MainActivity.class.getSimpleName();

    @BindString(R.string.selected_menu_item_id)
    String SELECTED_MENU_ITEM_ID;

    @BindView(R.id.recyclerview_movie)
    RecyclerView mRecyclerView;

    @BindView(R.id.main_progressbar)
    ProgressBar mProgressbar;

    @BindView(R.id.tv_error_message_display)
    TextView mErroTv;

    private MainViewModel viewModel;
    private MovieAdaptor mMovieAdaptor;
    private int mMenuSelected = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bindRecyclerView();
        setupViewModel();

        if(savedInstanceState == null) {
            /** If Create Activity, Go Load Movie **/
            loadData();
        }
        else {
            /** If Configuration Change, Restore Selected Menu **/
            if(savedInstanceState.containsKey(SELECTED_MENU_ITEM_ID)) {
                mMenuSelected = savedInstanceState.getInt(SELECTED_MENU_ITEM_ID);
            }
        }
    }


    /**
     * @Method
     * Set RecyclerView (Set Recycler View & Create Adaptor & Set LoadMore)
     */
    private void bindRecyclerView() {
//        GridLayoutManager layoutManager
//                = new GridLayoutManager(this, 2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns());

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdaptor = new MovieAdaptor(new ArrayList<>() , mRecyclerView ,MainActivity.this,MainActivity.this);
        mRecyclerView.setAdapter(mMovieAdaptor);

        mMovieAdaptor.setOnLoadMoreListener(() -> {
            /** By doing add null , so the adapter will check view_type and show progress bar at bottom **/
            if(!(mMenuSelected == R.id.action_favorite_sort)) {
                mMovieAdaptor.addMovieListNull();
                loadData();
                mMovieAdaptor.setFavorite(false);
            }
            else {
                mMovieAdaptor.setFavorite(true);
            }
        });
    }

    /**
     * @Method
     * Get Number of GridLayout Columns
     *
     * @return
     */
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the item
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }


    /**
     * @Method
     * Init ViewModel & Set Observing Live Data (Popular or Top_Rate Movie - in Network , My Favorite Movie - In Room)
     */
    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        /** Observing to Popular or Top_rate Movie - In Network **/
        viewModel.getMovieList().observe(this, movieList -> {
            if(mMovieAdaptor.getMovieList().size() == 0) {
                showRecycleView();
            }
            mMovieAdaptor.setMovieList(movieList);
            mMovieAdaptor.setLoaded();
            mMovieAdaptor.notifyDataSetChanged();
        });

        /** Observing to Popular or Top_rate Movie - In Room **/
        viewModel.getFavoriteMovieList().observe(this , movieList -> {
            if(mMenuSelected == R.id.action_favorite_sort) {
                if(mMovieAdaptor.getMovieList().size() == 0 ) {
                    showRecycleView();
                }
                viewModel.setFavoriteListAtMovieList();
            }
        });

        /** Observing Network Error **/
        viewModel.getError().observe(this, errorData -> {
            Log.e(TAG, errorData);
            showErroMessage();
        });
    }


    /**
     * @Method
     * Call Load MovieList to Network using Retrofit (Popluar or Top_Rate)
     */
    private void loadData() {
        viewModel.loadMovie();
    }


    /**
     * @Method
     * Load My Favorite Movies in Room (Actually, Favorite Movie Live Data Set Notify)
     */
    private void loadFavoriteData() {
        viewModel.setFavoriteListAtMovieList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_main, menu);

        if(mMenuSelected != -1) {
            MenuItem menuItem = (MenuItem)menu.findItem(mMenuSelected);
            menuItem.setChecked(true);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            /** refresh MainActivity **/
            initMainActivity();
            return true;
        }

        if (id == R.id.oss_menu) {
            /** Navigate License Page **/
            Context context = this;
            Class DestinationActivity = OssActivity.class;
            Intent intent = new Intent(context,DestinationActivity);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * @Method
     * When click sort type item, Perform action
     *
     * @param item : clicked menu item
     */
    // To handle menu text color group items click event
    public void onSortGroupItemClick(MenuItem item){
        if(item.getItemId() == R.id.action_popular_sort){
            item.setChecked(true);
            mMenuSelected = item.getItemId();
            viewModel.setSortType(getResources().getString(R.string.sort_option_popular));
            mMovieAdaptor.setFavorite(false);
            initMainActivity();

        }else if (item.getItemId() == R.id.action_toprate_sort) {
            item.setChecked(true);
            mMenuSelected = item.getItemId();
            viewModel.setSortType(getResources().getString(R.string.sort_option_topRate));
            mMovieAdaptor.setFavorite(false);
            initMainActivity();
        }else if (item.getItemId() == R.id.action_favorite_sort) {
            item.setChecked(true);
            mMenuSelected = item.getItemId();
            viewModel.setSortType(getResources().getString(R.string.sort_option_topRate));
            mMovieAdaptor.setFavorite(true);
            initMainActivity();
        }

    }


    /**
     * @Method
     * Initialize MainPage (If Click Refresh or Change Sort Type Button , Call this Method)
     */
    private void initMainActivity() {
        mMovieAdaptor.initAdaptor();
        showProgressBar();
        viewModel.initData();

        if(mMenuSelected == R.id.action_favorite_sort) {
            loadFavoriteData();
        }
        else {
            loadData();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /** Save selected sort_type ID **/
        outState.putInt(SELECTED_MENU_ITEM_ID , mMenuSelected);
        super.onSaveInstanceState(outState);
    }


    /**
     * @Method
     * When Click Viewholder , Navigate Detail Movie
     *
     * @param movie : Clicked Movie Item
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailMovieActivity.class;
        Intent intentToStartDetailActivity = new Intent(context , destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT , movie);
        startActivity(intentToStartDetailActivity);
    }


    private void showRecycleView() {
        mProgressbar.setVisibility(View.INVISIBLE);
        mErroTv.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showProgressBar() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressbar.setVisibility(View.VISIBLE);
        mErroTv.setVisibility(View.INVISIBLE);
    }


    private void showErroMessage() {
        mErroTv.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressbar.setVisibility(View.INVISIBLE);

    }
}
