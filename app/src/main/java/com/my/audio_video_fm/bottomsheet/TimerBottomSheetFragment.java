package com.my.audio_video_fm.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.dialog.CustomTimerDialogFragment;
import com.my.audio_video_fm.R;

public class TimerBottomSheetFragment extends BottomSheetDialogFragment {

    public interface TimerSelectionListener {
        void onTimerSelected(String timerOption);
        void onCustomTimerSelected(int hours, int minutes);
    }

    private TimerSelectionListener listener;

    public void setTimerSelectionListener(TimerSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom, container, false);

        TextView title = view.findViewById(R.id.dialog_title);
        TextView description = view.findViewById(R.id.dialog_description);
        RadioGroup timerOptions = view.findViewById(R.id.timer_options);
        Button closeButton = view.findViewById(R.id.btn_close);
        Button selectButton = view.findViewById(R.id.btn_timer_off); // Renamed for clarity

        closeButton.setOnClickListener(v -> dismiss());

        selectButton.setOnClickListener(v -> {
            int selectedId = timerOptions.getCheckedRadioButtonId();
            RadioButton selectedOption = view.findViewById(selectedId);
            if (selectedOption != null) {
                String selectedText = selectedOption.getText().toString();
                if (listener != null) {
                    if ("Time Off".equals(selectedText)) {
                        listener.onTimerSelected("Time Off");
                    } else if (selectedId == R.id.timer_custom) {
                        CustomTimerDialogFragment customTimerDialog = new CustomTimerDialogFragment();
                        customTimerDialog.setCustomTimerListener((hours, minutes) -> {
                            if (listener != null) {
                                listener.onCustomTimerSelected(hours, minutes);
                            }
                        });
                        customTimerDialog.show(getParentFragmentManager(), "CustomTimerDialog");
                    } else {
                        listener.onTimerSelected(selectedText);
                    }
                }
            }
            dismiss();
        });

        return view;
    }
}
