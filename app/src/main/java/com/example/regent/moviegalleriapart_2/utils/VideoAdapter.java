package com.example.regent.moviegalleriapart_2.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.regent.moviegalleriapart_2.R;
import com.example.regent.moviegalleriapart_2.model.Video.Result;

import java.net.URI;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static final String TAG = VideoAdapter.class.getSimpleName();

    private static final String BASE_VIDEO_URL = "https://youtube.com/watch";

    private RecyclerViewClickListener clickListener;

    private List<Result> videoResult;
    private Context context;

    public VideoAdapter(Context context, List<Result> videoResult, RecyclerViewClickListener clickListener){
        this.context = context;
        this.videoResult = videoResult;
        this.clickListener = clickListener;
    }

    public interface RecyclerViewClickListener{
        void onItemClicked(int clickedItemPosition);
    }

    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.each_video_item, parent, false);
        Log.i(TAG, "View is created.");
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoViewHolder holder, int position) {
        Result result = videoResult.get(position);
        holder.bindVideoItem(result);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "The array list size is: " + videoResult.size());
        return videoResult == null ? 0 : videoResult.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView videoView;
        private Result result;

        public VideoViewHolder(View itemView){
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            itemView.setOnClickListener(this);
        }

        public void bindVideoItem(Result resultItem){
            result = resultItem;
            String key = result.getKey();
            Uri uri = Uri.parse(BASE_VIDEO_URL + key);
            videoView.setText(key);
        }



        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onItemClicked(clickedPosition);
            Result singleVideo = videoResult.get(clickedPosition);
            Log.i(TAG, singleVideo.getName());
        }
    }
}
