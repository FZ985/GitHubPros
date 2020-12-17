package com.example.ktx3.base;

import androidx.viewbinding.ViewBinding;

public interface BaseInit<VB> {

    VB getBinding();

    void init();
}