package com.example.regent.moviegalleriapart_2.utils.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.regent.moviegalleriapart_2.model.Result;
import com.example.regent.moviegalleriapart_2.utils.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private final LiveData<List<Result>> movieResult;
    private AppDatabase appDatabase;

    public MainViewModel(@NonNull Application application){
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the Database");
        movieResult = appDatabase.mTaskDao().loadAllFavourites();
    }

    public LiveData<List<Result>> getMovieResult() {
        return movieResult;
    }

    public void deleteItem(Result result){
        new deleteAsyncTask(appDatabase).execute(result);
    }

    private static class deleteAsyncTask extends AsyncTask<Result, Void, Void>{
        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Result... results) {
            db.mTaskDao().deleteFavourite(results[0]);
            return null;
        }
    }
}
