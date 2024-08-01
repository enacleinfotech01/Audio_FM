package com.my.audio_video_fm.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.my.audio_video_fm.fragment.AnalyticsFragment;
import com.my.audio_video_fm.fragment.MyAudioFragment;
import com.my.audio_video_fm.fragment.MyListFragment;
import com.my.audio_video_fm.fragment.RecordFragment;

public class ViewPagerAdapterStudio  extends FragmentStateAdapter {

    public ViewPagerAdapterStudio(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new RecordFragment();
            case 2:
                return new MyAudioFragment();
            default:
                return new AnalyticsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}
