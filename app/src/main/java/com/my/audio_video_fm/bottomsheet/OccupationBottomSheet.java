package com.my.audio_video_fm.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;
// OccupationBottomSheet.java
public class OccupationBottomSheet extends BottomSheetDialogFragment {

    public interface OnOccupationSelectedListener {
        void onOccupationSelected(String occupation);
    }

    private OnOccupationSelectedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnOccupationSelectedListener) {
            listener = (OnOccupationSelectedListener) context;
        } else if (getParentFragment() instanceof OnOccupationSelectedListener) {
            listener = (OnOccupationSelectedListener) getParentFragment();
        } else {
            throw new ClassCastException(context.toString() + " must implement OnOccupationSelectedListener");
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_occupation, container, false);

        // Find views
        TextView occupationStudent = view.findViewById(R.id.occupation_student);
        TextView occupationSelfEmployed = view.findViewById(R.id.occupation_self_employed);
        TextView occupationGovtEmployee = view.findViewById(R.id.occupation_govt_employee);
        TextView occupationSalaried = view.findViewById(R.id.occupation_salaried);

        // Set click listeners
        occupationStudent.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOccupationSelected("Student");
            }
            dismiss();
        });

        occupationSelfEmployed.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOccupationSelected("Self-Employed");
            }
            dismiss();
        });

        occupationGovtEmployee.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOccupationSelected("Govt.Employee");
            }
            dismiss();
        });

        occupationSalaried.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOccupationSelected("Salaried");
            }
            dismiss();
        });

        // Add more occupations as needed

        return view;
    }
}
