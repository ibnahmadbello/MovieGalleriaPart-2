package com.example.regent.moviegalleriapart_2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourite")
public class FavouriteEntry {

    @PrimaryKey
    private int id;
    private String movieTitle;
    private String movieOverView;
    private String moviePosterPath;

    public FavouriteEntry(int id, String movieTitle, String movieOverView, String moviePosterPath){
        this.id = id;
        this.movieTitle = movieTitle;
        this.movieOverView = movieOverView;
        this.moviePosterPath = moviePosterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieOverView() {
        return movieOverView;
    }

    public void setMovieOverView(String movieOverView) {
        this.movieOverView = movieOverView;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }



}
