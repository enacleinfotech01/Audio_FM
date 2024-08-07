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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.my.audio_video_fm.R;
import com.my.audio_video_fm.fragment.RedeemFragment;

public class PremimumFragment extends Fragment {

    private ImageView close;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premimum, container, false);

        close = view.findViewById(R.id.close);

        TextView redeemTextView = view.findViewById(R.id.redem);
        redeemTextView.setOnClickListener(v -> {
            Fragment redeemFragment = new RedeemFragment();
            FragmentManager fragmentManager = getParentFragmentManager(); // Use getParentFragmentManager() if inside a nested fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentcontainer, redeemFragment); // Ensure this ID is correct
            fragmentTransaction.addToBackStack(null); // Optional
            fragmentTransaction.commit();

        });


        close.setOnClickListener(v -> {
            requireActivity().finish();
           // requireActivity().overridePendingTransition(0, R.anim.slide_out_down);
        });

        Button startButton = view.findViewById(R.id.start_button);
        Animation fadeIn = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out);

        startButton.setOnClickListener(v -> {
            v.startAnimation(fadeIn);

            Fragment FreeTrailFragment = new FreeTrailFragment();
            FragmentManager fragmentManager = getParentFragmentManager(); // Use getParentFragmentManager() if inside a nested fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentcontainer, FreeTrailFragment); // Ensure this ID is correct
            fragmentTransaction.addToBackStack(null); // Optional
            fragmentTransaction.commit();

            v.postDelayed(() -> v.startAnimation(fadeOut), fadeIn.getDuration());
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
                // Uncomment the line below if you want to override the transition animation
                // requireActivity().overridePendingTransition(0, R.anim.slide_out_down);
            }
        });

        return view;
    }
}
