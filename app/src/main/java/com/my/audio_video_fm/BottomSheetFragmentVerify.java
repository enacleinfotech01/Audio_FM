package com.my.audio_video_fm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static android.app.Activity.RESULT_OK;

public class BottomSheetFragmentVerify extends BottomSheetDialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final long MAX_IMAGE_SIZE = 300 * 1024; // 300 KB
    private ImageView ivStudentId;
    private ImageView ivDelete;
    private EditText etName;
    private Uri selectedImageUri;
    private TextView tvWarning;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_verify, container, false);

        etName = view.findViewById(R.id.et_name);
        ivStudentId = view.findViewById(R.id.iv_student_id);
        Button btnVerify = view.findViewById(R.id.btn_verify);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ivDelete = view.findViewById(R.id.delete);
        tvWarning = view.findViewById(R.id.warning); // Initialize TextView

        // Verify button click listener
        btnVerify.setOnClickListener(v -> performVerification());

        // Close button click listener
        ivClose.setOnClickListener(v -> dismiss());

        // Student ID Image click listener (for upload functionality)
        ivStudentId.setOnClickListener(v -> openGallery());

        // Delete image button click listener
        ivDelete.setOnClickListener(v -> {
            ivStudentId.setImageURI(null); // Remove the image
            selectedImageUri = null;
            tvWarning.setText(""); // Clear the warning message
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Validate the image file
            if (isValidImage(selectedImageUri)) {
                ivStudentId.setImageURI(selectedImageUri); // Display the selected image
                tvWarning.setText(""); // Clear the warning message
            } else {
                tvWarning.setText("Please select a valid image file (PNG, JPG, JPEG, under 300KB).");
                selectedImageUri = null; // Clear the invalid URI
                ivStudentId.setImageURI(null); // Clear the image view
            }
        }
    }

    @SuppressLint("LongLogTag")
    private boolean isValidImage(Uri uri) {
        if (uri == null) return false;

        // Check file size and type
        try {
            // Get file size
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                long fileSize = cursor.getLong(sizeIndex);
                cursor.close();

                // Validate file size and type
                if (fileSize > MAX_IMAGE_SIZE) {
                    return false;
                }

                // Validate file extension
                String fileName = getFileNameFromUri(uri);
                if (fileName != null) {
                    String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                    return extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg");
                }
            }
        } catch (Exception e) {
            Log.e("BottomSheetFragmentVerify", "Error validating image", e);
        }
        return false;
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return result != null ? result : "Unknown file";
    }

    private void performVerification() {
        String name = etName.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name cannot be empty");
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform your verification logic here
        String fileName = getFileNameFromUri(selectedImageUri);
        Log.d("Verification", "Name: " + name + ", File Name: " + fileName);

        // Show the result in a new BottomSheet
        BottomSheetFragmentVerificationResult resultSheet = new BottomSheetFragmentVerificationResult("Verification successful!");
        resultSheet.show(getFragmentManager(), resultSheet.getTag());

        // Optionally dismiss the fragment or show a success message
        dismiss();
    }
}
