package com.app.api29;

import android.content.Context;
import android.content.SharedPreferences;

import api29.libs.app.base.BaseSharedPreferences;

/**
 * Create by JFZ
 * date: 2020-05-15 16:44
 **/
public class User extends BaseSharedPreferences {
    @Override
    public SharedPreferences getSharedPreference() {
        Context context= null;
        return context.getSharedPreferences("sp_user",Context.MODE_PRIVATE);
    }

    public void userName(String name){
        putString("name",name);
    }
}
