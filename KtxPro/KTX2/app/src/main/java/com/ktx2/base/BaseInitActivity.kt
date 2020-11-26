package com.ktx2.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open abstract class BaseInitActivity : AppCompatActivity(), BaseInitCall {
    var mContext: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

    override fun onDestroy() {
        super.onDestroy()

        mContext = null
    }
}