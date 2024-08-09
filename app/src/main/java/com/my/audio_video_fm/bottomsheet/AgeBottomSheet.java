package com.my.audio_video_fm.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;

public class AgeBottomSheet extends BottomSheetDialogFragment {

    public interface OnAgeSelectedListener {
        void onAgeSelected(String age);
    }

    private OnAgeSelectedListener listener;

    public void setOnAgeSelectedListener(OnAgeSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_age, container, false);
view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dismiss();
    }
});
        // Handle age selection and pass the selected age back to the listener
        view.findViewById(R.id.age_21_25).setOnClickListener(v -> {
            if (listener != null) listener.onAgeSelected("20-30");
            dismiss();
        });
        // More age options...

        return view;
    }
}
