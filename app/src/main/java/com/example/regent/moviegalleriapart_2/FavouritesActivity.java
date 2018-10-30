package com.example.regent.moviegalleriapart_2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.regent.moviegalleriapart_2.model.Result;
import com.example.regent.moviegalleriapart_2.utils.AppDatabase;
import com.example.regent.moviegalleriapart_2.utils.viewModel.MainViewModel;

import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    private AppDatabase mAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        setTitle(getString(R.string.favourites_movie));

        mAppDatabase = AppDatabase.getInstance(getApplicationContext());
//        setupViewModel();
    }

    /*private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovieResult().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel")
            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_remove_from_favourite){

        }
        return super.onOptionsItemSelected(item);
    }
}
