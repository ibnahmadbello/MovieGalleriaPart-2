package com.example.regent.moviegalleriapart_2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.regent.moviegalleriapart_2.model.Popular;
import com.example.regent.moviegalleriapart_2.model.Result;
import com.example.regent.moviegalleriapart_2.model.TopRatedMovies;
import com.example.regent.moviegalleriapart_2.presenter.MovieApi;
import com.example.regent.moviegalleriapart_2.presenter.MovieService;
import com.example.regent.moviegalleriapart_2.utils.AppDatabase;
import com.example.regent.moviegalleriapart_2.utils.MovieAdapter;
import com.example.regent.moviegalleriapart_2.utils.MovieAdapterCallback;
import com.example.regent.moviegalleriapart_2.utils.PageScrollListener;
import com.example.regent.moviegalleriapart_2.utils.QueryPreferences;
import com.example.regent.moviegalleriapart_2.utils.viewModel.MainViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity implements MovieAdapterCallback, MovieAdapter.RecyclerViewClickListener{

    private static final String TAG = MovieActivity.class.getSimpleName();
    public static String EXTRA = "Movie_Extra";

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int TOTAL_PAGE = 5;
    private int currentPage = PAGE_START;

    MovieAdapter movieAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    private List<Result> movieItems = new ArrayList<>();

    private MovieService movieService;

    private AppDatabase mAppDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        recyclerView = findViewById(R.id.recycler_view);

        movieAdapter = new MovieAdapter(this, movieItems, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.addOnScrollListener(new PageScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                if (QueryPreferences.getStoredQuery(MovieActivity.this).equals("top_rated")){
                    loadNextPage();
                } else if (QueryPreferences.getStoredQuery(MovieActivity.this).equals("popular")){
                    loadNextPopularPage();
                }

                /*// mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);*/
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGE;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        progressBar = findViewById(R.id.progress_bar);


        movieService = MovieApi.getRetrofit(this).create(MovieService.class);
        loadFirstPage();

        /*mAppDatabase = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();*/

    }

    /*private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovieResult().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                movieAdapter.setMovieResults(results);
            }
        });
    }*/

    private void loadFirstPage(){
        Log.i(TAG, "loadFirstPage: ");
        QueryPreferences.setStoredQuery(this, "top_rated");
        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                // Got data here, send to adapter

                List<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
//                movieItems.clear();
                movieAdapter.clear();
                movieAdapter.addAll(results);
//                movieAdapter.notifyDataSetChanged();

                setTitle(getString(R.string.top_rated));

                if (currentPage <= TOTAL_PAGE) movieAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for pagination
     * @return
     */
    private Call<TopRatedMovies> callTopRatedMoviesApi(){
        return movieService.getTopRatedMovies(getString(R.string.api_key), "en_US", currentPage);
    }

    private List<Result> fetchResults(Response<TopRatedMovies> topRatedMoviesResponse){
        TopRatedMovies topRatedMovies = topRatedMoviesResponse.body();
        return topRatedMovies.getResults();
    }

    @Override
    public void retryPageLoad() {

    }

    @Override
    public void onItemClick(int clickedItemPosition) {
        Intent intent = new Intent(this, DetailActivity.class);
        Result testMovie = movieItems.get(clickedItemPosition);
        intent.putExtra(EXTRA, testMovie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_popular_movie:
                String popular_query = "popular";
                if (QueryPreferences.getStoredQuery(this).equals(popular_query)){
                    Toast.makeText(this, "Already showing Popular Movies.", Toast.LENGTH_SHORT).show();
                } else {
                    currentPage = 1;
                    loadPopularMovie();
                }
                break;
            case R.id.action_top_rated_movie:
                String top_rated_query = "top_rated";
                if (QueryPreferences.getStoredQuery(this).equals(top_rated_query)){
                    Toast.makeText(this, "Already showing Top Rated Movies.", Toast.LENGTH_SHORT).show();
                } else {
                    currentPage = 1;
                    loadFirstPage();
                }
                break;
            case R.id.action_show_favourite:
                startActivity(new Intent(this, FavouritesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Performs a Retrofit call to the popular movies API.
     * @return
     */
    private Call<Popular> callPopularMoviesApi(){
        return movieService.getPopularMovies(getString(R.string.api_key), "en_US", currentPage);
    }

    private List<Result> fetchPopular(Response<Popular> popularResponse){
        Popular popularMovies = popularResponse.body();
        return popularMovies.getResults();
    }

    private void loadPopularMovie() {
        QueryPreferences.setStoredQuery(this, "popular");
        callPopularMoviesApi().enqueue(new Callback<Popular>() {
            @Override
            public void onResponse(Call<Popular> call, Response<Popular> response) {
                List<Result> popularResult = fetchPopular(response);
                progressBar.setVisibility(View.GONE);
//                movieItems.clear();
                movieAdapter.clear();
                movieAdapter.addAll(popularResult);
//                movieAdapter.notifyDataSetChanged();
                setTitle(getString(R.string.most_popular));

                if (currentPage <= TOTAL_PAGE) movieAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Popular> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPage(){
        Log.i(TAG, "loadFirstPage: ");
        QueryPreferences.setStoredQuery(this, "top_rated");
        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                // Got data here, send to adapter

                movieAdapter.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                movieAdapter.addAll(results);

                setTitle(getString(R.string.top_rated));

                if (currentPage <= TOTAL_PAGE) movieAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPopularPage() {
        QueryPreferences.setStoredQuery(this, "popular");
        callPopularMoviesApi().enqueue(new Callback<Popular>() {
            @Override
            public void onResponse(Call<Popular> call, Response<Popular> response) {

                movieAdapter.removeLoadingFooter();
                isLoading = false;
                List<Result> popularResult = fetchPopular(response);
                progressBar.setVisibility(View.GONE);
//                movieItems.clear();
//                movieAdapter.clear();
                movieAdapter.addAll(popularResult);
//                movieAdapter.notifyDataSetChanged();
                setTitle(getString(R.string.most_popular));

                if (currentPage <= TOTAL_PAGE) movieAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Popular> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
