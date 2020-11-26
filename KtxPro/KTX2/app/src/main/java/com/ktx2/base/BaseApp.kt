package com.ktx2.base

import android.app.Application

class BaseApp : Application() {
    companion object {
        private var app: BaseApp? = null

        @JvmStatic
        fun getInstance() = app!!
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}