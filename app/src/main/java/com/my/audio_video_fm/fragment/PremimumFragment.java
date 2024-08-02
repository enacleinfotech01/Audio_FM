package com.my.audio_video_fm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.my.audio_video_fm.R; // Replace with the new fragment class

public class PremimumFragment extends Fragment {
ImageView close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premimum, container, false);
close=view.findViewById(R.id.close);
        // Set up the redeem button click listener
        TextView redeemTextView = view.findViewById(R.id.redem);
        redeemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with your new fragment
                Fragment newFragment = new RedeemFragment();

                // Pass data if needed
                Bundle args = new Bundle();
                args.putString("some_key", "some_value");
                newFragment.setArguments(args);

                // Perform the fragment transaction
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                fragmentTransaction.addToBackStack(null); // Add to back stack if you want to navigate back
                fragmentTransaction.commit();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity
                requireActivity().finish();
                // Optional: add a transition animation
               requireActivity().overridePendingTransition(0, R.anim.slide_out_down);
            }
        });
        Button startButton = view.findViewById(R.id.start_button);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply fade in animation
                v.startAnimation(fadeIn);
                Fragment newFragment = new FreeTrailFragment();

                // Pass data if needed
                Bundle args = new Bundle();
                args.putString("some_key", "some_value");
                newFragment.setArguments(args);

                // Perform the fragment transaction
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                fragmentTransaction.addToBackStack(null); // Add to back stack if you want to navigate back
                fragmentTransaction.commit();
                // Optionally apply blur effect (not directly supported, requires custom implementation)
                // For demonstration, apply fade out after fade in
                v.postDelayed(() -> v.startAnimation(fadeOut), fadeIn.getDuration());
            }
        });

        return view;
    }

}
