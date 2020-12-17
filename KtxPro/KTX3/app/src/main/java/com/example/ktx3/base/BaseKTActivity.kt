package com.example.ktx3.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseKTActivity : AppCompatActivity(), BaseKTInit {
    //    lateinit var binding: ViewBinding
    lateinit var mContext: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        init()
    }

    abstract fun init()

}