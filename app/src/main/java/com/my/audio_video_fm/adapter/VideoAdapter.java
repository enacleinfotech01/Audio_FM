package com.my.audio_video_fm.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.audio_video_fm.R;
import com.my.audio_video_fm.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videoList;

    public VideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        TextView title;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webview);
            title = itemView.findViewById(R.id.title);

            // Configure WebView settings
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);  // Enable DOM storage
        }

        public void setVideoData(Video video) {
            title.setText(video.getTitle());

            // Construct the YouTube embed URL
            String videoId = getVideoIdFromUrl(video.getVideourl());
            String videoUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&mute=1";

            // Construct the HTML code to embed the YouTube video
            String html = "<html><body style='margin:0px;padding:0px;'><iframe width='100%' height='100%' src='" + videoUrl + "' frameborder='0' allowfullscreen></iframe></body></html>";

            // Load the HTML content into the WebView
            webView.setWebViewClient(new WebViewClient());
            webView.loadData(html, "text/html", "utf-8");
        }

        private String getVideoIdFromUrl(String url) {
            // Extract video ID from URL
            String videoId = "";
            try {
                Uri uri = Uri.parse(url);
                videoId = uri.getQueryParameter("v");
                if (videoId == null) {
                    // Handle YouTube Shorts or other URL formats
                    videoId = uri.getLastPathSegment();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videoId;
        }
    }
}
