package com.my.audio_video_fm.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.my.audio_video_fm.fragment.BuycoinsFragment;
import com.my.audio_video_fm.fragment.EarnCoinFragment;


public class ViewPagerAdapterStore extends FragmentStateAdapter {

    public ViewPagerAdapterStore(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BuycoinsFragment();
            case 1:
                return new EarnCoinFragment();
            default:
                return new BuycoinsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }

    {
    }
}
