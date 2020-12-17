package com.kmt.pro.base;


import android.content.Intent;

import com.kmt.pro.permission.PermissionTool;
import com.kmtlibs.app.base.SuperInitActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseSwipActivity extends SuperInitActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionTool.get().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionTool.get().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PermissionTool.get().destry();
    }
}
