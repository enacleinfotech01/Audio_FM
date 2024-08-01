package com.my.audio_video_fm.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.my.audio_video_fm.fragment.ActivitiesFragment;
import com.my.audio_video_fm.fragment.MyListFragment;

public class ViewPagerAdapterProfile extends FragmentStateAdapter {
    public ViewPagerAdapterProfile(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyListFragment();
            case 1:
                return new ActivitiesFragment();
            default:
                return new MyListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
