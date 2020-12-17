package com.kmt.pro.permission;

import android.content.Intent;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Create by JFZ
 * date: 2020-06-29 15:33
 **/
public abstract class PermissionCallImpl implements PermissionTool.PermissionCall {
    @Override
    public void onPermissionNo(List<String> faildPermissionList, List<String> donotAsk) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }
}
