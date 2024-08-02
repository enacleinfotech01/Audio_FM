package com.my.audio_video_fm.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.my.audio_video_fm.R;


public class MainActivity extends AppCompatActivity {

    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnev);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                navController.navigate(R.id.homeFragment);
            } else if (itemId == R.id.navigation_audio) {
                navController.navigate(R.id.premimumFragment);
            } else if (itemId == R.id.navigation_video) {
                navController.navigate(R.id.videoFragment);
            } else if (itemId == R.id.navigation_search) {
                navController.navigate(R.id.searchFragment);
            } else if (itemId == R.id.navigation_personal) {
                navController.navigate(R.id.mySpaceFragment);
            } else {
                return false;
            }
            return true;
        });
        onSupportNavigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView)).getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}