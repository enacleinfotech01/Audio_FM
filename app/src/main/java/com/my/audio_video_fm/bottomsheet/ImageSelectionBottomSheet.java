package com.my.audio_video_fm.bottomsheet;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;

public class ImageSelectionBottomSheet extends BottomSheetDialogFragment {

    public interface OnImageSelectionListener {
        void onImageSelected(int requestCode);
    }

    private OnImageSelectionListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnImageSelectionListener) {
            listener = (OnImageSelectionListener) getParentFragment();
        } else if (context instanceof OnImageSelectionListener) {
            listener = (OnImageSelectionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnImageSelectionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_image, container, false);

        // Find views
        TextView selectFromGallery = view.findViewById(R.id.select_from_gallery);
        TextView captureFromCamera = view.findViewById(R.id.capture_from_camera);

        // Set click listeners
        selectFromGallery.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageSelected(REQUEST_IMAGE_PICK);
            }
            dismiss();
        });

        captureFromCamera.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageSelected(REQUEST_IMAGE_CAPTURE);
            }
            dismiss();
        });

        return view;
    }

    public static final int REQUEST_IMAGE_PICK = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
}
