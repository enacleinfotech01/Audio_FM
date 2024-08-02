package com.my.audio_video_fm.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.my.audio_video_fm.fragment.AnalyticsFragment;
import com.my.audio_video_fm.fragment.MyAudioFragment;

public class ViewPagerAdapterStudio extends FragmentStateAdapter {

    public ViewPagerAdapterStudio(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RecordFragment();
            case 1:
                return new MyAudioFragment();
            case 2:
                return new AnalyticsFragment();
            default:
                return new RecordFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}
