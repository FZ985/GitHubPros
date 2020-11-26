package com.ktx2.base

import android.content.SharedPreferences

abstract class BaseSharedPreference {

    fun putInt(key: String, value: Int): BaseSharedPreference {
        edit().putInt(key, value).commit()
        return this
    }

    fun putString(key: String, value: String): BaseSharedPreference {
        edit().putString(key, value).commit()
        return this
    }

    fun putBool(key: String, value: Boolean): BaseSharedPreference {
        edit().putBoolean(key, value).commit()
        return this;
    }

    fun putFloat(key: String, value: Float): BaseSharedPreference {
        edit().putFloat(key, value).commit()
        return this
    }

    fun putLong(key: String, value: Long): BaseSharedPreference {
        edit().putLong(key, value).commit()
        return this
    }

    fun getInt(key: String, defVal: Int): Int {
        return getSharedPreference().getInt(key, defVal)
    }

    fun getString(key: String, defVal: String): String {
        return getSharedPreference().getString(key, defVal)!!
    }

    fun getBool(key: String, defVal: Boolean): Boolean {
        return getSharedPreference().getBoolean(key, defVal)
    }

    fun getFloat(key: String, defVal: Float): Float {
        return getSharedPreference().getFloat(key, defVal)
    }

    fun getLong(key: String, defVal: Long): Long {
        return getSharedPreference().getLong(key, defVal)
    }

    fun remove(key: String) {
        edit().remove(key).commit()
    }

    fun clear() {
        edit().clear()
    }

    private fun edit(): SharedPreferences.Editor {
        return getSharedPreference().edit()
    }

    abstract fun getSharedPreference(): SharedPreferences
}