package com.my.audio_video_fm.bottomsheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.SharedViewModel;
public class EpisodeBottomSheet extends BottomSheetDialogFragment {
    private SharedViewModel sharedViewModel;
    private RadioGroup radioGroup;
    private static final String ARG_ID = "ID";
    private int episodeId;

    public static EpisodeBottomSheet newInstance(int episodeId) {
        EpisodeBottomSheet fragment = new EpisodeBottomSheet();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, episodeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            episodeId = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_bottom_sheet, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        radioGroup = view.findViewById(R.id.your_radio_group_id);

        final TextView textView = view.findViewById(R.id.text);
        final TextView required = view.findViewById(R.id.coinrequired);
        final TextView episode = view.findViewById(R.id.episode);
        final Button btn = view.findViewById(R.id.btn);

        // Optionally observe LiveData for updates
        sharedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String text) {
                Log.d("EpisodeBottomSheet", "Observed Text: " + text);
                textView.setText(text);
                Toast.makeText(getContext(), "Data updated: " + text, Toast.LENGTH_SHORT).show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint({"NonConstantResourceId", "MissingInflatedId"})
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Find RadioButton instances
                RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
                RadioButton radioButton2 = view.findViewById(R.id.radioButton2);

                // Enable both RadioButtons initially
                radioButton1.setClickable(true);
                radioButton2.setClickable(true);

                // Handle RadioButton selection
                if (checkedId == R.id.radioButton1) {
                    // Update UI for radioButton1 selection
                    String retrievedText = textView.getText().toString();
                    required.setText(retrievedText);
                    episode.setText("Unlock Episode");
                    btn.setText("Unlock Episode 1");
                    btn.setEnabled(true);  // Enable the button

                    // Uncheck and disable radioButton2
                    radioButton2.setClickable(false);
                } else if (checkedId == R.id.radioButton2) {
                    // Update UI for radioButton2 selection
                    String retrievedText1 = ((TextView) view.findViewById(R.id.othertext)).getText().toString();
                    required.setText(retrievedText1);
                    episode.setText("Unlock Episodes");
                    btn.setText("Unlock Episodes 11 - 16");
                    btn.setEnabled(true);  // Enable the button

                    // Uncheck and disable radioButton1
                    radioButton1.setClickable(false);
                } else {
                    // No RadioButton is selected
                    btn.setEnabled(false);  // Disable the button
                }
            }
        });



        return view;
    }
}
