package com.my.audio_video_fm.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;

public class LanguageBottomSheetDialog extends BottomSheetDialogFragment {

    private TextView radioEnglish, radioSpanish, radioFrench, radioGerman, radioChinese,radio_south,radio_bengli,radio_hindi;
    private Button buttonCancel;
    private Button buttonSave;

    private OnLanguageSelectedListener listener;
    private TextView selectedTextView = null;

    public interface OnLanguageSelectedListener {
        void onLanguageSelected(String language);
    }

    public void setOnLanguageSelectedListener(OnLanguageSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_languages, container, false);

        radioEnglish = view.findViewById(R.id.radio_english);
        radioSpanish = view.findViewById(R.id.radio_spanish);
        radioFrench = view.findViewById(R.id.radio_french);
        radioGerman = view.findViewById(R.id.radio_german);
        radioChinese = view.findViewById(R.id.radio_chinese);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonSave = view.findViewById(R.id.button_save);

        radio_hindi=view.findViewById(R.id.radio_hindi);
        radio_south=view.findViewById(R.id.radio_south);
        radio_bengli=view.findViewById(R.id.radio_bengli);
        View.OnClickListener radioClickListener = v -> {
            if (selectedTextView != null) {
                selectedTextView.setBackgroundResource(R.drawable.radio_button_border_unselected);
            }
            selectedTextView = (TextView) v;
            selectedTextView.setBackgroundResource(R.drawable.radio_button_border_selected);
        };

        radioEnglish.setOnClickListener(radioClickListener);
        radioSpanish.setOnClickListener(radioClickListener);
        radioFrench.setOnClickListener(radioClickListener);
        radioGerman.setOnClickListener(radioClickListener);
        radioChinese.setOnClickListener(radioClickListener);
        radio_south.setOnClickListener(radioClickListener);
        radio_hindi.setOnClickListener(radioClickListener);
        radio_bengli.setOnClickListener(radioClickListener);

        buttonCancel.setOnClickListener(v -> dismiss());

        buttonSave.setOnClickListener(v -> {
            if (listener != null && selectedTextView != null) {
                listener.onLanguageSelected(selectedTextView.getText().toString());
            }
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }
}
