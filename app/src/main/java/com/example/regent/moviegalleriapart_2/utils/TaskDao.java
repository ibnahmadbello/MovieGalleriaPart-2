package com.example.regent.moviegalleriapart_2.utils;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.regent.moviegalleriapart_2.model.Result;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM favourites")
    LiveData<List<Result>> loadAllFavourites();

    @Insert(onConflict = REPLACE)
    void insertFavourite(Result result);

    @Delete
    void deleteFavourite(Result result);

    @Query("SELECT * FROM favourites WHERE id = :id")
    LiveData<Result> loadFavouriteById(int id);

}
