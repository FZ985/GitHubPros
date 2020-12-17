package com.example.ktx3

import com.example.ktx3.base.BaseActivity
import com.example.ktx3.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun init() {
        binding.mainTv
    }

}