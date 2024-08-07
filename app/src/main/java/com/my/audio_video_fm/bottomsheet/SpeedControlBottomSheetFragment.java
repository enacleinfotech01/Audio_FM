package com.my.audio_video_fm.bottomsheet;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.my.audio_video_fm.R;

public class SpeedControlBottomSheetFragment extends BottomSheetDialogFragment {
    TextView textView;
Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SeekBar  speedSeekBar = view.findViewById(R.id.speedSeekBar);
      TextView speedTextView = view.findViewById(R.id.speedTextView);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speed = 0.5f + (progress / 10.0f); // Adjust speed range as needed
                speedTextView.setText(String.format("%.1f", speed) + "x Speed");
                // Apply the speed to MediaPlayer or other components as needed
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle touch start if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle touch stop if needed
            }
        });

    }
}
