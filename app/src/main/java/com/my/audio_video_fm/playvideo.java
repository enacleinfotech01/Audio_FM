package com.my.audio_video_fm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.my.audio_video_fm.fragment.PlayFragment;

public class playvideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);

        // Get the video ID and image URL from the Intent extras
       String videoId = getIntent().getStringExtra("VIDEO_ID");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        if (videoId != null && imageUrl != null) {
            // Create a new instance of PlayFragment with the video ID and image URL
            PlayFragment playFragment = PlayFragment.newInstance(videoId, imageUrl);

            // Load the PlayFragment into the container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, playFragment)
                    .commit();
        } else {
            Toast.makeText(this, "No media item provided", Toast.LENGTH_SHORT).show();
        }
    }
}
