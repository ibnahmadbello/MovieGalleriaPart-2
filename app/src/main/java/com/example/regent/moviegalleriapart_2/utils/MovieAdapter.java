package com.example.regent.moviegalleriapart_2.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.regent.moviegalleriapart_2.R;
import com.example.regent.moviegalleriapart_2.model.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = MovieAdapter.class.getSimpleName();

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private RecyclerViewClickListener clickListener;

    private List<Result> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private MovieAdapterCallback movieAdapterCallback;

    public MovieAdapter(Context context, List<Result> movieResults, RecyclerViewClickListener clickListener){
        this.context = context;
        this.movieAdapterCallback = (MovieAdapterCallback) context;
        this.clickListener = clickListener;
        this.movieResults = movieResults;
    }

    public interface RecyclerViewClickListener{
        void onItemClick(int clickedItemPosition);
    }

    public List<Result> getMovieResults(){
        return movieResults;
    }

    public void setMovieResults(List<Result> movieResults){
        this.movieResults = movieResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM:
                viewHolder = getViewHolder(parent, layoutInflater);
                break;
            case LOADING:
                View view = layoutInflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(view);
                Log.i(TAG, "View is created");
                break;
        }

        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater){
        RecyclerView.ViewHolder viewHolder;
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Result result = movieResults.get(position);

        switch (getItemViewType(position)){
            case ITEM:
                MovieViewHolder viewHolder = (MovieViewHolder) holder;
                viewHolder.bindMovieItem(result);
                break;
            case LOADING:
                // do nothing
                break;
        }
//        holder.bindMovieItem(result);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "Movie size is: " + movieResults.size());
        return movieResults == null? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView cardView;
        private TextView movieName;
        private ImageView movieImage;
        private Result result;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.movie_text_view);
            movieImage = itemView.findViewById(R.id.movie_image_view);
            cardView = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(this);
        }

        public void bindMovieItem(Result resultItem){
            result = resultItem;
            movieName.setText(result.getTitle());
            loadImage(result.getPosterPath())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(movieImage);
            /*Picasso.with(context)
                    .load(IMAGE_BASE_URL + result.getPosterPath())
                    .into(movieImage);*/
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onItemClick(clickedPosition);
            Result singleMovie = movieResults.get(clickedPosition);
            Log.i(TAG, singleMovie.toString());
        }
    }

    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath){
        return Glide
                .with(context)
                .load(IMAGE_BASE_URL + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public void add(Result result){
        movieResults.add(result);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Result> movieResults){
        for (Result result : movieResults){
            add(result);
        }
    }

    public void remove(Result result){
        int position = movieResults.indexOf(result);
        if (position > -1){
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear(){
        isLoadingAdded = false;
        while (getItemCount() > 0){
            remove(getItem(0));
        }
    }

    public boolean isEmpty(){
        return getItemCount() == 0;
    }

    public void addLoadingFooter(){
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter(){
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Result result = getItem(position);

        if (result != null){
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position){
        return movieResults.get(position);
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder{
        public LoadingViewHolder(View view){
            super (view);
        }
    }
}
