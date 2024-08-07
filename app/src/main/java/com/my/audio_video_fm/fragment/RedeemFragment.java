package com.my.audio_video_fm.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.PagerAdapter;


public class RedeemFragment extends Fragment {
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redeem, container, false);

        ImageView back = view.findViewById(R.id.back);
        TabLayout tabLayout = view.findViewById(R.id.tab_layoutredem);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager_redeem);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Redeem Voucher");
                            break;
                        case 1:
                            tab.setText("Student Offer");
                            break;
                    }
                }).attach();

        return view;
    }
}
