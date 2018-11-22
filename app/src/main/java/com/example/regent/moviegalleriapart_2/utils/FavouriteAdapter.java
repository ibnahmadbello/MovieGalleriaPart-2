package com.example.regent.moviegalleriapart_2.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.regent.moviegalleriapart_2.R;
import com.example.regent.moviegalleriapart_2.model.FavouriteEntry;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.RecyclerViewHolder> {

    private List<FavouriteEntry> mFavouriteEntryList;
    private View.OnLongClickListener mLongClickListener;

    public FavouriteAdapter(List<FavouriteEntry> favouriteEntryList, View.OnLongClickListener longClickListener){
        this.mFavouriteEntryList = favouriteEntryList;
        this.mLongClickListener = longClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        FavouriteEntry favouriteEntry = mFavouriteEntryList.get(position);

        holder.titleTextView.setText(favouriteEntry.getMovieTitle());
        holder.overViewTextView.setText(favouriteEntry.getMovieOverView());

        holder.itemView.setTag(favouriteEntry);
        holder.itemView.setOnLongClickListener(mLongClickListener);
    }

    @Override
    public int getItemCount() {
        return mFavouriteEntryList == null ? 0 : mFavouriteEntryList.size();
    }

    public void addItems(List<FavouriteEntry> favouriteEntryList){
        this.mFavouriteEntryList = favouriteEntryList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView;
        private TextView overViewTextView;

        RecyclerViewHolder(View view){
            super(view);

            titleTextView = view.findViewById(R.id.favourite_movie_title);
            overViewTextView = view.findViewById(R.id.favourite_movie_overview);
        }
    }
}
