package com.example.regent.moviegalleriapart_2.utils;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.regent.moviegalleriapart_2.model.FavouriteEntry;
import com.example.regent.moviegalleriapart_2.model.Result;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM favourite")
    LiveData<List<FavouriteEntry>> loadAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavourite(FavouriteEntry favouriteEntry);

    @Delete
    void deleteFavourite(FavouriteEntry favouriteEntry);

    @Query("SELECT * FROM favourite WHERE id = :id")
    LiveData<FavouriteEntry> loadFavouriteById(int id);

}
