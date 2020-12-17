package com.kmtlibs.app.base;

import android.content.SharedPreferences;

/**
 * Create by JFZ
 * date: 2020-05-15 16:40
 **/
public abstract class BaseSharedPreferences {

    public void putBool(String key, boolean value) {
        getSharedPreference().edit().putBoolean(key, value).commit();
    }

    public boolean getBool(String key, boolean defValue) {
        return getSharedPreference().getBoolean(key, defValue);
    }

    public void putString(String key, String value) {
        getSharedPreference().edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return getSharedPreference().getString(key, defValue);
    }

    public void putInt(String key, int value) {
        getSharedPreference().edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return getSharedPreference().getInt(key, defValue);
    }

    public void putLong(String key, long value) {
        getSharedPreference().edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return getSharedPreference().getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        getSharedPreference().edit().putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return getSharedPreference().getFloat(key, defValue);
    }

    public void clear() {
        getSharedPreference().edit().clear().commit();
    }
    public abstract SharedPreferences getSharedPreference();
}
