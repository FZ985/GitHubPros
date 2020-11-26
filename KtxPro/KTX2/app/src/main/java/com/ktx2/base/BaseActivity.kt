package com.ktx2.base

import android.app.Activity
import android.os.Bundle

abstract class BaseActivity : BaseInitActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(layoutId())
            initView()
            styleStatus(this)
            initData()
        } catch (e: Exception) {
            println("base init error ${e.message}")
        }

    }

    fun styleStatus(activity: Activity) {}




}