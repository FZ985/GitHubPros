package com.example.ktx3;

import com.example.ktx3.base.BaseActivity;
import com.example.ktx3.databinding.ActivityMainBinding;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-17 10:44
 */
public class HomeActivity extends BaseActivity<ActivityMainBinding> {
    @Override
    public ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void init() {
        binding.mainTv.setText("dddd");
    }
}