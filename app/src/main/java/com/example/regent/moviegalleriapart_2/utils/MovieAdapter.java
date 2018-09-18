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
import android.widget.ProgressBar;
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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public static final String TAG = MovieAdapter.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private List<Result> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private MovieAdapterCallback movieAdapterCallback;

    public MovieAdapter(Context context){
        this.context = context;
//        this.movieAdapterCallback = (MovieAdapterCallback) context;
        movieResults = new ArrayList<>();
    }

    public List<Result> getMovieResults(){
        return movieResults;
    }

    public void setMovieResults(List<Result> movieResults){
        this.movieResults = movieResults;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
        Log.i(TAG, "View is created");
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Result result = movieResults.get(position);
        holder.bindMovieItem(result);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "Movie size is: " + movieResults.size());
        return movieResults == null? 0 : movieResults.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView movieName;
        private ImageView movieImage;
        private Result result;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.movie_text_view);
            movieImage = itemView.findViewById(R.id.movie_image_view);
            cardView = itemView.findViewById(R.id.card_view);
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



    }

    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath){
        return Glide
                .with(context)
                .load(IMAGE_BASE_URL + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade();
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

    public void addLoadingFooter(){
        isLoadingAdded = true;
//        add(new Result());
    }
}
