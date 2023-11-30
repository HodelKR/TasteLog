package com.example.tastelog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentStateAdapter {
    List<Fragment> fragments;
    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new ArrayList<>();
        fragments.add(new RightHomeFragment());
        fragments.add(new LeftHomeFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        // Return the total number of fragments
        return fragments.size();
    }
}
