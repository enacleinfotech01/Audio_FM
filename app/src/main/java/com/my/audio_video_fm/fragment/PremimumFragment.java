package com.my.audio_video_fm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.my.audio_video_fm.R;

public class PremimumFragment extends Fragment {

    private ImageView close;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premimum, container, false);
        close = view.findViewById(R.id.close);

        TextView redeemTextView = view.findViewById(R.id.redem);
        redeemTextView.setOnClickListener(v -> {
            // Navigate to RedeemFragment using NavController
            NavController navController = NavHostFragment.findNavController(PremimumFragment.this);
            navController.navigate(R.id.action_premimumFragment_to_redeemFragment);
        });

        close.setOnClickListener(v -> {
            requireActivity().finish();
            requireActivity().overridePendingTransition(0, R.anim.slide_out_down);
        });

        Button startButton = view.findViewById(R.id.start_button);
        Animation fadeIn = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out);

        startButton.setOnClickListener(v -> {
            v.startAnimation(fadeIn);
            NavController navController = NavHostFragment.findNavController(PremimumFragment.this);
            navController.navigate(R.id.action_premimumFragment_to_redeemFragment);

            v.postDelayed(() -> v.startAnimation(fadeOut), fadeIn.getDuration());
        });

        return view;
    }

}
