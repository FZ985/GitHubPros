package com.example.ktx3

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.ktx3.base.BaseActivity
import com.example.ktx3.databinding.ActivityMainBinding
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun init() {
        binding.mainTv.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23
                && (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA
                    ), 100
                )
                return@setOnClickListener
            }
            Matisse.from(this@MainActivity)
                .choose(MimeType.ofAll())
                .countable(true)
                .capture(true)
                .captureStrategy(CaptureStrategy(true, "com.example.ktx3.fileprovider"))
                .theme(R.style.ddd)
                .spanCount(4)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .showPreview(false) // Default is `true`
                .forResult(100)
        }
    }

}