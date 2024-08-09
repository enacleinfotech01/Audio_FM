package com.my.audio_video_fm.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;

public class OccupationBottomSheet extends BottomSheetDialogFragment {

    public interface OnOccupationSelectedListener {
        void onOccupationSelected(String occupation);
    }

    private OnOccupationSelectedListener listener;

    public void setOnOccupationSelectedListener(OnOccupationSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_occupation, container, false);

        // Handle occupation selection and pass the selected occupation back to the listener
        view.findViewById(R.id.occupation_student).setOnClickListener(v -> {
            if (listener != null) listener.onOccupationSelected("Engineer");
            dismiss();
        });
        // More occupation options...
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
