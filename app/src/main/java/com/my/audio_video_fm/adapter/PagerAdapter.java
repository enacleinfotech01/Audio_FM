package com.my.audio_video_fm.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.my.audio_video_fm.fragment.RedeemVoucherFragment;
import com.my.audio_video_fm.fragment.StudentOfferFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a different fragment based on position
        switch (position) {
            case 0:
                return new RedeemVoucherFragment();
            case 1:
                return new StudentOfferFragment();
            default:
                return new RedeemVoucherFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of pages
    }
}

