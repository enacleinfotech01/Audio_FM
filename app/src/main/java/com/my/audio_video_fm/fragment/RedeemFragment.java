package com.my.audio_video_fm.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.adapter.PagerAdapter;


public class RedeemFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_redeem, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layoutredem);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager_redeem);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), requireActivity().getLifecycle());
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