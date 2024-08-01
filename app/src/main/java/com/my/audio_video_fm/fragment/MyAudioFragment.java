package com.my.audio_video_fm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.my.audio_video_fm.R;

public class MyAudioFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_audio, container, false);
        TextView filterTextView = view.findViewById(R.id.filter_text_view);
        filterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
        TextView sortByTextView = view.findViewById(R.id.sortby);
        sortByTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet1();
            }
        });
        return view;
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View sheetView = LayoutInflater.from(requireActivity()).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
    private void showBottomSheet1() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View sheetView = LayoutInflater.from(requireActivity()).inflate(R.layout.bottom_sheet_sort_layout, null);
        bottomSheetDialog.setContentView(sheetView);

        // Optionally, handle radio button selections here
        RadioGroup radioGroup = sheetView.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = sheetView.findViewById(checkedId);
                // Handle selection
                // For example, you can show a Toast or update some data
                Toast.makeText(requireActivity(), "Selected: " + selectedRadioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show();
    }
}