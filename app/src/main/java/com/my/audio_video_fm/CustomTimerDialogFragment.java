// CustomTimerDialogFragment.java
package com.my.audio_video_fm;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomTimerDialogFragment extends DialogFragment {

    public interface CustomTimerListener {
        void onCustomTimerSet(int hours, int minutes);
    }

    private CustomTimerListener listener;

    public void setCustomTimerListener(CustomTimerListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom, container, false);

        EditText inputHours = view.findViewById(R.id.input_hours);
        EditText inputMinutes = view.findViewById(R.id.input_minutes);
        Button cancelButton = view.findViewById(R.id.btn_cancel);
        Button setButton = view.findViewById(R.id.btn_set);

        cancelButton.setOnClickListener(v -> dismiss());

        setButton.setOnClickListener(v -> {
            int hours = Integer.parseInt(inputHours.getText().toString());
            int minutes = Integer.parseInt(inputMinutes.getText().toString());
            if (listener != null) {
                listener.onCustomTimerSet(hours, minutes);
            }
            dismiss();
        });

        return view;
    }
}
