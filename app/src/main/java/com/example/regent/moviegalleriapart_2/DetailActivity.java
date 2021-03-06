package com.example.regent.moviegalleriapart_2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.regent.moviegalleriapart_2.model.FavouriteEntry;
import com.example.regent.moviegalleriapart_2.model.Result;
import com.example.regent.moviegalleriapart_2.model.Review.Review;
import com.example.regent.moviegalleriapart_2.model.Video.Video;
import com.example.regent.moviegalleriapart_2.presenter.MovieApi;
import com.example.regent.moviegalleriapart_2.presenter.MovieService;
import com.example.regent.moviegalleriapart_2.utils.AddFavouriteViewModel;
import com.example.regent.moviegalleriapart_2.utils.FavouritePreference;
import com.example.regent.moviegalleriapart_2.utils.VideoAdapter;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, VideoAdapter.RecyclerViewClickListener{

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_POSITION = "extra_movie";

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private RatingBar movieRating;
    private ImageView movieImage;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private TextView movieReleaseDate, movieTitle, movieOverview, movieReview;
    Result result;
    private double numberRating;
    String url;
    private static final String API_KEY = "";

    List<com.example.regent.moviegalleriapart_2.model.Review.Result> mResultList = new ArrayList<>();

    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<com.example.regent.moviegalleriapart_2.model.Video.Result> resultList = new ArrayList<>();

    private MovieService movieService;

    private AddFavouriteViewModel mAddFavouriteViewModel;
    private boolean mInitialState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieRating = findViewById(R.id.movie_rating);
        movieImage = findViewById(R.id.movie_picture_image_view);
        movieReleaseDate = findViewById(R.id.release_date_text_view);
        movieTitle = findViewById(R.id.movie_name_text_view);
        movieOverview = findViewById(R.id.movie_overview_text_view);
        movieReview = findViewById(R.id.movie_review_text_view);
        recyclerView = findViewById(R.id.video_recycler_view);
        mToolbar = findViewById(R.id.detail_toolbar);
        mFloatingActionButton = findViewById(R.id.fab);

        movieService = MovieApi.getRetrofit(this).create(MovieService.class);
        mAddFavouriteViewModel = ViewModelProviders.of(this).get(AddFavouriteViewModel.class);

//        movieFavourite.setOnClickListener(this);
        movieReview.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);

        result = (Result) getIntent().getSerializableExtra(MovieActivity.EXTRA);



        setupMovie();

        handleVideoView();

        setupVideoRecyclerView();
    }

    private void setupMovie(){
        movieOverview.setText(result.getOverview());
        movieTitle.setText(result.getTitle());
        mToolbar.setTitle(result.getTitle());
        movieReleaseDate.setText(result.getReleaseDate());
        numberRating = result.getVoteAverage();
        Log.i(TAG, "popularity " + numberRating);
        movieRating.setRating((float) numberRating);
        movieRating.setIsIndicator(true);
        movieRating.setStepSize(2);
//        movieRating.setMax(5);
//        movieRating.setNumStars(10);
        if (FavouritePreference.getPrefFavouriteQuery(this)==(result.getId())){
            mFloatingActionButton.setSelected(!mInitialState);
        }

        Glide.with(this)
                .load(IMAGE_BASE_URL + result.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .override(500, 500)
                .into(movieImage);

        setTitle(result.getTitle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.movie_favourite_text_view:
                boolean initialState = movieFavourite.isSelected();
                movieFavourite.setSelected(!initialState);
                break;*/
            case R.id.fab:
                mInitialState = mFloatingActionButton.isSelected();
                mFloatingActionButton.setSelected(!mInitialState);
                mAddFavouriteViewModel.addFavourite(new FavouriteEntry(result.getId(), result.getTitle(), result.getOverview(), result.getPosterPath()));
                FavouritePreference.setPrefFavouriteQuery(this, result.getId());
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
                for (com.example.regent.moviegalleriapart_2.model.Review.Result result1 : results){
                    mResultList.add(result1);
                }
                Log.i(TAG, "The size is: " + mResultList.size());
                getUrl();
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
        return movieService.getReview(movie_id, getString(R.string.api_key), "en_US");
    }

    private List<com.example.regent.moviegalleriapart_2.model.Review.Result> fetchResults(Response<Review> reviewResponse){
        Review review = reviewResponse.body();
//        Log.i(TAG, review.getResults().toString());
        assert review != null;
        return review.getResults();
    }

    private void getUrl(){
        for (com.example.regent.moviegalleriapart_2.model.Review.Result result : mResultList){
            url = result.getUrl();
        }
        if (url != null) {
            Log.i(TAG, url);
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(intent);
        } else {
            return;
        }
    }

    private void handleVideoView(){
        callVideoApi().enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                List<com.example.regent.moviegalleriapart_2.model.Video.Result> results = fetchVideos(response);
                resultList.addAll(results);
                videoAdapter.notifyDataSetChanged();
                Log.i(TAG, "The size is: " + resultList.size());
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.d(TAG, "Error message is: " + t.getMessage());
            }
        });
    }

    private Call<Video> callVideoApi(){
        int movie_id = result.getId();
        Log.i(TAG, "The movie_id is: " + movie_id);
        return movieService.getVideo(movie_id, getString(R.string.api_key), "en_US", 1);
    }

    private List<com.example.regent.moviegalleriapart_2.model.Video.Result> fetchVideos(Response<Video> videoResponse){
        Video video = videoResponse.body();
        return video.getResults();
    }

    private void setupVideoRecyclerView(){
        videoAdapter = new VideoAdapter(this, resultList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(videoAdapter);
    }

    @Override
    public void onItemClicked(int clickedItemPosition) {
        com.example.regent.moviegalleriapart_2.model.Video.Result singleVideo = resultList.get(clickedItemPosition);
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this).equals(YouTubeInitializationResult.SUCCESS)){
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, API_KEY, singleVideo.getKey());
            startActivity(intent);
        } else {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + singleVideo.getKey()));
            startActivity(webIntent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeAsUp)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
