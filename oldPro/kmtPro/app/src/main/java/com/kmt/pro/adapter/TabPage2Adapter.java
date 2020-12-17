package com.kmt.pro.adapter;

import com.kmt.pro.base.BaseFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Create by JFZ
 * date: 2020-07-28 14:50
 **/
public class TabPage2Adapter extends FragmentStateAdapter {
    private List<BaseFragment> frags;

    public TabPage2Adapter(@NonNull FragmentActivity fragmentActivity, List<BaseFragment> frags) {
        super(fragmentActivity);
        this.frags = frags;
    }

    public TabPage2Adapter(@NonNull Fragment fragment, List<BaseFragment> frags) {
        super(fragment);
        this.frags = frags;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return frags.get(position);
    }

    @Override
    public int getItemCount() {
        return frags == null ? 0 : frags.size();
    }
}
