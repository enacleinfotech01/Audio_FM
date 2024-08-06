package com.my.audio_video_fm.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.my.audio_video_fm.R;
import com.my.audio_video_fm.fragment.PremimumFragment;

public class Fragment_container extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        // Retrieve intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String imageUrl = intent.getStringExtra("image_url");
            String title = intent.getStringExtra("title");
            int id = intent.getIntExtra("ID", -1); // Default to -1 if not found

            if (id == 1) {
                // Start a new activity for ID 1
                Intent newIntent = new Intent(this, EpisodeActivity.class);
                newIntent.putExtra("image_url", imageUrl);
                newIntent.putExtra("title", title);
                startActivity(newIntent);
                finish(); // Optionally close this activity
            } else {
                // Show fragment for other IDs
                Fragment fragment;

                if (id == 2) {
                    fragment = new PremimumFragment(); // Example fragment
                } else {
                    fragment = new PremimumFragment(); // Default fragment
                }

                // Pass data to the fragment
                Bundle args = new Bundle();
                args.putString("image_url", imageUrl);
                args.putString("title", title);
                fragment.setArguments(args);

                // Begin fragment transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        }
    }
}
