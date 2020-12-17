package com.kmtlibs.adapter.base.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

/**
 * 扩展方法，用于获取View
 *
 * @ layoutResId int
 * @receiver ViewGroup parent
 * @return View
 */
public class AdapterUtils {
    public static View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
    }
}