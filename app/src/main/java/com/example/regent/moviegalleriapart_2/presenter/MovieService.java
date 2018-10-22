package com.example.regent.moviegalleriapart_2.presenter;

import com.example.regent.moviegalleriapart_2.model.Popular;
import com.example.regent.moviegalleriapart_2.model.Review.Result;
import com.example.regent.moviegalleriapart_2.model.TopRatedMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/top_rated")
    Call<TopRatedMovies> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );

    @GET("movie/popular")
    Call<Popular> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );

    @GET("movie/{movie_id}/reviews")
    Call<Result> getReview(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );
}
