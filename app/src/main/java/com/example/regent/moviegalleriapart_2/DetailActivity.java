package com.example.regent.moviegalleriapart_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.regent.moviegalleriapart_2.model.Result;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_POSITION = "extra_movie";

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private RatingBar movieRating;
    private ImageView movieImage;
    private TextView movieReleaseDate, movieTitle, movieOverview;
    Result result;
    private double numberRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieRating = findViewById(R.id.movie_rating);
        movieImage = findViewById(R.id.movie_picture_image_view);
        movieReleaseDate = findViewById(R.id.release_date_text_view);
        movieTitle = findViewById(R.id.movie_name_text_view);
        movieOverview = findViewById(R.id.movie_overview_text_view);

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
                .override(250, 250)
                .into(movieImage);

    }
}
