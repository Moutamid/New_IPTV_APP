package com.ixidev.mobile.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ixidev.mobile.R;

import java.util.ArrayList;

public class GalleryVideoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_video);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display videos
        videoAdapter = new VideoAdapter(getAllVideos());
        recyclerView.setAdapter(videoAdapter);

    }

    private ArrayList<VideoModel> getAllVideos() {
        ArrayList<VideoModel> videoList = new ArrayList<>();
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
        };

        Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id_index = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                int path_index = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                int name_index = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                int duration_index = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);

                int id = cursor.getInt(id_index);
                String path = cursor.getString(path_index);
                String name = cursor.getString(name_index);
                long duration = cursor.getLong(duration_index);

                VideoModel video = new VideoModel(id, path, name, duration);
                videoList.add(video);
            }
            cursor.close();
        }

        return videoList;
    }

    private static class VideoModel {
        int id;
        String path;
        String name;
        long duration;

        VideoModel(int id, String path, String name, long duration) {
            this.id = id;
            this.path = path;
            this.name = name;
            this.duration = duration;
        }
    }

    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
        private ArrayList<VideoModel> videos;

        VideoAdapter(ArrayList<VideoModel> videos) {
            this.videos = videos;
        }

        @NonNull
        @Override
        public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
            VideoModel video = videos.get(position);
            holder.videoName.setText(video.name);
            holder.videoDuration.setText("Duration: " + formatMilliseconds(video.duration));

            // Load video thumbnail using Glide (you'll need to add Glide to your dependencies)
            Glide.with(GalleryVideoActivity.this)
                    .load(video.path)
                    .into(holder.videoThumbnail);
            
            holder.itemView.setOnClickListener(v -> playVideo(holder.itemView.getContext(), video.path));
            
        }

        public String formatMilliseconds(long milliseconds) {
            // Calculate seconds and minutes
            long totalSeconds = milliseconds / 1000;
            long seconds = totalSeconds % 60;
            long minutes = (totalSeconds / 60) % 60;
            long hours = totalSeconds / 3600;

            String hoursText = hours > 0 ? hours + " hour" + (hours > 1 ? "s" : "") + " " : "";
            String minutesText = minutes > 0 ? minutes + " minute" + (minutes > 1 ? "s" : "") + " " : "";
            String secondsText = seconds + " second" + (seconds > 1 ? "s" : "");

            String formattedTime = hoursText + minutesText + secondsText;
            return formattedTime.trim();
        }

        private void playVideo(Context context, String videoPath) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(videoPath), "video/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Check if there is a video player app available to handle the intent
            PackageManager packageManager = context.getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                context.startActivity(intent);
            } else {
                // Handle the case where no video player app is available
                Toast.makeText(context, "No video player app found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        class VideoViewHolder extends RecyclerView.ViewHolder {
            ImageView videoThumbnail;
            TextView videoName;
            TextView videoDuration;

            VideoViewHolder(@NonNull View itemView) {
                super(itemView);
                videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
                videoName = itemView.findViewById(R.id.videoName);
                videoDuration = itemView.findViewById(R.id.videoDuration);
            }
        }
    }

}