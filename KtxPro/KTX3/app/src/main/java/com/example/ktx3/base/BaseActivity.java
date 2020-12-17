package com.example.ktx3.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-17 10:32
 */
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity implements BaseInit<VB> {
    protected VB binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getBinding();
        setContentView(binding.getRoot());
        init();
    }
}