package com.example.regent.moviegalleriapart_2.utils.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.regent.moviegalleriapart_2.model.Result;
import com.example.regent.moviegalleriapart_2.utils.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private final LiveData<List<Result>> movieResult;

    public MainViewModel(@NonNull Application application){
        super(application);

        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the Database");
        movieResult = appDatabase.mTaskDao().loadAllFavourites();
    }

    public LiveData<List<Result>> getMovieResult() {
        return movieResult;
    }
}
