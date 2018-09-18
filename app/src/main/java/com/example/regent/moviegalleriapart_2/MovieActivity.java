package com.example.regent.moviegalleriapart_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.regent.moviegalleriapart_2.model.Result;
import com.example.regent.moviegalleriapart_2.model.TopRatedMovies;
import com.example.regent.moviegalleriapart_2.presenter.MovieApi;
import com.example.regent.moviegalleriapart_2.presenter.MovieService;
import com.example.regent.moviegalleriapart_2.utils.MovieAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int TOTAL_PAGE = 5;
    private int currentPage = PAGE_START;

    MovieAdapter movieAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    private MovieService movieService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        recyclerView = findViewById(R.id.recycler_view);

        movieAdapter = new MovieAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);

        progressBar = findViewById(R.id.progress_bar);


        movieService = MovieApi.getRetrofit(this).create(MovieService.class);
        loadFirstPage();
    }

    private void loadFirstPage(){
        Log.i(TAG, "loadFirstPage: ");

        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                // Got data here, send to adapter

                List<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                movieAdapter.addAll(results);

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
}
