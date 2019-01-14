package com.example.regent.moviegalleriapart_2.utils.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.regent.moviegalleriapart_2.model.FavouriteEntry;
import com.example.regent.moviegalleriapart_2.utils.AppDatabase;

import java.util.List;

public class FavouriteListViewModel extends AndroidViewModel {


    private LiveData<List<FavouriteEntry>> mFavouriteEntryLiveData;
    private AppDatabase mAppDatabase;

    public FavouriteListViewModel(Application application){
        super(application);

        mAppDatabase = AppDatabase.getInstance(this.getApplication());
        mFavouriteEntryLiveData = mAppDatabase.mTaskDao().loadAllFavourites();
        }

    public LiveData<List<FavouriteEntry>> getFavouriteEntryLiveData(){
        return mFavouriteEntryLiveData;
    }

    public void deleteItem(FavouriteEntry favouriteEntry){
        new deleteAsyncTask(mAppDatabase).execute(favouriteEntry);
    }

    private static class deleteAsyncTask extends AsyncTask<FavouriteEntry, Void, Void>{
        private AppDatabase mDatabase;

        deleteAsyncTask(AppDatabase database){
            mDatabase = database;
        }

        @Override
        protected Void doInBackground(FavouriteEntry... favouriteEntries) {
            mDatabase.mTaskDao().deleteFavourite(favouriteEntries[0]);
            return null;
        }
    }



}
