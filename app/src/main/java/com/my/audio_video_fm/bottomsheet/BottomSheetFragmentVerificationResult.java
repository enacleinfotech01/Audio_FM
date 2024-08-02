    package com.my.audio_video_fm.bottomsheet;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;

    import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
    import com.my.audio_video_fm.R;
    import com.my.audio_video_fm.fragment.FreeTrailFragment;

    public class BottomSheetFragmentVerificationResult extends BottomSheetDialogFragment {

        private Button start_button_bottom_sheet;
        private String verificationMessage;

        public BottomSheetFragmentVerificationResult(String message) {
            this.verificationMessage = message;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet_layout_verification_result, container, false);

            TextView tvMessage = view.findViewById(R.id.tv_message);
            ImageView btnClose = view.findViewById(R.id.btn_close);
            start_button_bottom_sheet = view.findViewById(R.id.start_button_veryfi);

            // Set verification message
            tvMessage.setText(verificationMessage);

            start_button_bottom_sheet.setOnClickListener(v -> {
                // Create a new instance of FreeTrailFragment
                FreeTrailFragment freeTrailFragment = new FreeTrailFragment();

                // Get the FragmentManager from the parent activity or fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Begin the fragment transaction
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace the current fragment with the new one
                transaction.replace(R.id.fragment_container, freeTrailFragment); // R.id.fragment_container should be the ID of your container

                // Add the transaction to the back stack (optional)
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            });


            btnClose.setOnClickListener(v -> dismiss());

            return view;
        }
    }
