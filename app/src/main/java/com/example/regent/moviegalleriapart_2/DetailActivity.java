package com.example.regent.moviegalleriapart_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.regent.moviegalleriapart_2.model.Result;
import com.example.regent.moviegalleriapart_2.model.Review.Review;
import com.example.regent.moviegalleriapart_2.presenter.MovieApi;
import com.example.regent.moviegalleriapart_2.presenter.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_POSITION = "extra_movie";

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private RatingBar movieRating;
    private ImageView movieImage;
    private TextView movieReleaseDate, movieTitle, movieOverview, movieFavourite, movieReview;
    Result result;
    private double numberRating;

    private MovieService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieRating = findViewById(R.id.movie_rating);
        movieImage = findViewById(R.id.movie_picture_image_view);
        movieReleaseDate = findViewById(R.id.release_date_text_view);
        movieTitle = findViewById(R.id.movie_name_text_view);
        movieOverview = findViewById(R.id.movie_overview_text_view);
        movieFavourite = findViewById(R.id.movie_favourite_text_view);
        movieReview = findViewById(R.id.movie_review_text_view);

        movieService = MovieApi.getRetrofit(this).create(MovieService.class);

        movieFavourite.setOnClickListener(this);
        movieReview.setOnClickListener(this);

        result = (Result) getIntent().getSerializableExtra(MovieActivity.EXTRA);

        setupMovie();
    }

    private void setupMovie(){
        movieOverview.setText(result.getOverview());
        movieTitle.setText(result.getTitle());
        movieReleaseDate.setText(result.getReleaseDate());
        numberRating = result.getPopularity();
        movieRating.setRating((float) numberRating);
        movieRating.setIsIndicator(true);
        movieRating.setStepSize(1.0f);
        movieRating.setMax(10);
        movieRating.setNumStars(10);

        Glide.with(this)
                .load(IMAGE_BASE_URL + result.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .override(500, 500)
                .into(movieImage);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.movie_favourite_text_view:
                break;
            case R.id.movie_review_text_view:
                handleReview();
                break;
        }
    }

    private void handleReview(){
        callMovieReview().enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                List<com.example.regent.moviegalleriapart_2.model.Review.Result> results = fetchResults(response);
                for (com.example.regent.moviegalleriapart_2.model.Review.Result str : results){
                    Log.e(TAG, String.valueOf(str));
                }
//                Log.i(TAG, url);
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.d(TAG, "Error message is: " + t.getMessage());
            }
        });
    }

    /**
     * Performs a Retrofit call to the Review link
     */
    private Call<Review> callMovieReview(){
        int movie_id = result.getId();
        String review = "reviews";
        return movieService.getReview(movie_id, getString(R.string.api_key), "en_US", 1);
    }

    private List<com.example.regent.moviegalleriapart_2.model.Review.Result> fetchResults(Response<Review> reviewResponse){
        Review review = reviewResponse.body();
        Log.i(TAG, review.getResults().toString());
        return review.getResults();
    }
}
