package com.my.audio_video_fm.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.Room.AppDatabase;
import com.my.audio_video_fm.Room.User;
import com.my.audio_video_fm.Room.UserDao;
import com.my.audio_video_fm.bottomsheet.AgeBottomSheet;
import com.my.audio_video_fm.bottomsheet.ImageSelectionBottomSheet;
import com.my.audio_video_fm.bottomsheet.OccupationBottomSheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_Profile extends Fragment implements AgeBottomSheet.OnAgeSelectedListener, OccupationBottomSheet.OnOccupationSelectedListener, ImageSelectionBottomSheet.OnImageSelectionListener {
    private EditText ageEditText, occupationEditText;
    private TextView selectedTextView;
    private String imageUri = null; // Initialize to null or a default value

    private ImageButton changeImageButton;
    private AppCompatButton saveButton;
    private String selectedGender = "";
    private UserDao userDao;
    private CircleImageView profileImage;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private RadioGroup radioGroupGender;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit__profile, container, false);

        // Initialize UI components
        ageEditText = view.findViewById(R.id.ageEditText);
        occupationEditText = view.findViewById(R.id.occupationEditText);
        profileImage = view.findViewById(R.id.profile_image);
        saveButton = view.findViewById(R.id.saveButton);
        radioGroupGender = view.findViewById(R.id.radioGroupGender); // Initialize RadioGroup

        EditText nameEditText =view.findViewById(R.id.nameEditText);
        EditText bioEditText =view.findViewById(R.id.bioEditText);
        // Set click listeners
        ageEditText.setOnClickListener(v -> {
            AgeBottomSheet ageBottomSheet = new AgeBottomSheet();
            ageBottomSheet.show(getChildFragmentManager(), ageBottomSheet.getTag());
        });

        view.findViewById(R.id.change_image_button).setOnClickListener(v -> {
            ImageSelectionBottomSheet bottomSheet = new ImageSelectionBottomSheet();
            bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
        });

        occupationEditText.setOnClickListener(v -> {
            OccupationBottomSheet occupationBottomSheet = new OccupationBottomSheet();
            occupationBottomSheet.show(getChildFragmentManager(), occupationBottomSheet.getTag());
        });

        if (imageUri != null) {
            Uri uri = Uri.parse(imageUri);
            profileImage.setImageURI(uri);
        }
        // Set RadioGroup listener
        radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_male) {
                selectedGender = "Male";
            } else if (checkedId == R.id.rb_female) {
                selectedGender = "Female";
            } else if (checkedId == R.id.rb_other) {
                selectedGender = "Other";
            } else {
                selectedGender = "";
            }
        });
        AppDatabase db = AppDatabase.getDatabase(getContext());
        userDao = db.userDao();

            saveButton.setOnClickListener(v -> {
                String name = nameEditText.getText().toString().trim();
                String bio = bioEditText.getText().toString().trim();
                String ageString = ageEditText.getText().toString().trim();
                String occupation = occupationEditText.getText().toString().trim();
                 // Set this based on the user's selection
                String imageUri = this.imageUri; // Replace with actual URI





            User user = new User(0, imageUri, name, bio, ageString, selectedGender, occupation);

            // Insert user data into Room database
            new Thread(() -> {
                userDao.insertUser(user);
            }).start();
        });



        return view;
    }



    @Override
    public void onAgeSelected(String ageRange) {
        ageEditText.setText(ageRange);
    }


    @Override
    public void onOccupationSelected(String occupation) {
        occupationEditText.setText(occupation);
    }

        @Override
        public void onImageSelected(int requestCode) {
            switch (requestCode) {
                case REQUEST_IMAGE_PICK:
                    openGallery();
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    openCamera();
                    break;
            }
        }

        private void openGallery() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        }

        private void openCamera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);
                this.imageUri = imageUri.toString(); // Convert URI to String
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profileImage.setImageBitmap(imageBitmap);
                this.imageUri = saveBitmapToFile(imageBitmap); // Save Bitmap and get URI
            }
        }
    }
    private String saveBitmapToFile(Bitmap bitmap) {
        File filesDir = getContext().getFilesDir();
        File imageFile = new File(filesDir, "captured_image.jpg");

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compress bitmap to JPEG
            return Uri.fromFile(imageFile).toString(); // Return URI as string
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle error
        }
    }


}
