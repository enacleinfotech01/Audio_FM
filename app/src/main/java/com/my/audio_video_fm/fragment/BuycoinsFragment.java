package com.my.audio_video_fm.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.SharedViewModel;

public class BuycoinsFragment extends Fragment {
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buycoins, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        TextView textView = view.findViewById(R.id.yourTextViewId);
        String retrievedText = textView.getText().toString();  // or any other value you need

        // Save to SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_text", retrievedText);
        editor.apply();

        // Update ViewModel
        sharedViewModel.setText(retrievedText);

        return view;
    }
}
