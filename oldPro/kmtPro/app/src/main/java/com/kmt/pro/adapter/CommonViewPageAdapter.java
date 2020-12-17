package com.kmt.pro.adapter;


import com.kmt.pro.base.BaseFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Create by JFZ
 * date: 2020-05-21 16:59
 **/
public class CommonViewPageAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;

    public CommonViewPageAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
