package com.my.audio_video_fm.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;

public class AgeBottomSheet extends BottomSheetDialogFragment {

    // Define an interface to communicate with the parent fragment
    public interface OnAgeSelectedListener {
        void onAgeSelected(String ageRange);
    }

    private OnAgeSelectedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Ensure that the parent fragment implements the listener
            listener = (OnAgeSelectedListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString() + " must implement OnAgeSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_age, container, false);

        // Set click listeners for each age range TextView
        view.findViewById(R.id.age_15_20).setOnClickListener(v -> selectAge("15-20 years"));
        view.findViewById(R.id.age_21_25).setOnClickListener(v -> selectAge("21-25 years"));
        view.findViewById(R.id.age_26_30).setOnClickListener(v -> selectAge("26-30 years"));
        view.findViewById(R.id.age_31_35).setOnClickListener(v -> selectAge("31-35 years"));
        view.findViewById(R.id.age_36_40).setOnClickListener(v -> selectAge("36-40 years"));
        view.findViewById(R.id.age_41_45).setOnClickListener(v -> selectAge("41-45 years"));
        view.findViewById(R.id.age_46_50).setOnClickListener(v -> selectAge("46-50 years"));
        view.findViewById(R.id.age_51_55).setOnClickListener(v -> selectAge("51-55 years"));
        view.findViewById(R.id.age_55_60).setOnClickListener(v -> selectAge("55-60 years"));
        view.findViewById(R.id.older).setOnClickListener(v -> selectAge("Older"));

        // Handle close button
        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());

        return view;
    }

    private void selectAge(String ageRange) {
        listener.onAgeSelected(ageRange);  // Pass the selected age back to the parent fragment
        dismiss();
    }
    private void sendAgeToParentFragment(String ageRange) {
        if (listener != null) {
            listener.onAgeSelected(ageRange);
        }
        dismiss();  // Close the bottom sheet
    }
}
