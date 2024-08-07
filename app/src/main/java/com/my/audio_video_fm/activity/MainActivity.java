package com.my.audio_video_fm.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.fragment.HomeFragment;
import com.my.audio_video_fm.fragment.PremimumFragment;
import com.my.audio_video_fm.fragment.RedeemFragment;
import com.my.audio_video_fm.fragment.RedeemVoucherFragment;
import com.my.audio_video_fm.fragment.VideoFragment;
import com.my.audio_video_fm.fragment.SearchFragment;
import com.my.audio_video_fm.fragment.MySpaceFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FragmentManager
        fragmentManager = getSupportFragmentManager();

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnev);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_audio) {
                selectedFragment = new RedeemFragment();
            } else if (itemId == R.id.navigation_video) {
                selectedFragment = new VideoFragment();
            } else if (itemId == R.id.navigation_search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.navigation_personal) {
                selectedFragment = new MySpaceFragment();
            }

            if (selectedFragment != null) {
                // Replace the current fragment with the selected one
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainerView, selectedFragment);
                transaction.addToBackStack(null); // Optional: Add to back stack for navigation
                transaction.commit();
                return true;
            }
            return false;
        });

        // Load the default fragment (home)
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, new HomeFragment())
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }
        return super.onSupportNavigateUp();
    }
}
