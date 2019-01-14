package com.example.regent.moviegalleriapart_2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.regent.moviegalleriapart_2.model.FavouriteEntry;
import com.example.regent.moviegalleriapart_2.utils.FavouriteAdapter;
import com.example.regent.moviegalleriapart_2.utils.viewModel.FavouriteListViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements View.OnLongClickListener{

    private FavouriteListViewModel viewModel;
    private FavouriteAdapter favouriteAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        setTitle(getString(R.string.favourites_movie));

        recyclerView = findViewById(R.id.favourite_recycler_view);
        favouriteAdapter = new FavouriteAdapter(this, new ArrayList<FavouriteEntry>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(favouriteAdapter);

        viewModel = ViewModelProviders.of(this).get(FavouriteListViewModel.class);
        viewModel.getFavouriteEntryLiveData().observe(FavouritesActivity.this, new Observer<List<FavouriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteEntry> favouriteEntryList) {
                favouriteAdapter.addItems(favouriteEntryList);
            }
        });

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
    public boolean onLongClick(View view) {
        FavouriteEntry favouriteEntry = (FavouriteEntry) view.getTag();
        viewModel.deleteItem(favouriteEntry);
        return true;
    }
}
