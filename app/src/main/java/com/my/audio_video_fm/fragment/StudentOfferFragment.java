package com.my.audio_video_fm.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.my.audio_video_fm.bottomsheet.BottomSheetFragmentVerify;
import com.my.audio_video_fm.R;


public class StudentOfferFragment extends Fragment {

Button verify;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_student_offer, container, false);

        verify=view.findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
    return view;
    }
    private void showBottomSheet() {
        BottomSheetFragmentVerify bottomSheet = new BottomSheetFragmentVerify();
        bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
    }}