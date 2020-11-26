package com.ktx2

import android.view.View
import com.ktx2.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_main
    override fun initView() {
//        main_bottombar.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.page_1 -> {
//                    val badge = main_bottombar.getOrCreateBadge(it.itemId)
//                    badge.isVisible = true
//                    true
//                }
//                R.id.page_2 -> {
//                    val badge = main_bottombar.getOrCreateBadge(it.itemId)
//                    badge.isVisible = true
//                    badge.number = 80
//                    true
//                }
//                R.id.page_3 -> {
//                    val badge = main_bottombar.getOrCreateBadge(it.itemId)
//                    badge.isVisible = true
//                    badge.number = 200
//                    true
//                }
//
//                R.id.page_4 -> {
//                    val badge = main_bottombar.getOrCreateBadge(it.itemId)
//                    badge.isVisible = true
//                    badge.number = 1000
//                    true
//                }
//                else -> false
//            }
//        }
//        main_bottombar.setOnNavigationItemReselectedListener {
//            when (it.itemId) {
//                R.id.page_1, R.id.page_2, R.id.page_3, R.id.page_4 -> {
//                    Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    override fun initData() {
        initBottomNavigation(false)
    }

    private fun initBottomNavigation(isCenter: Boolean) {
        main_bottombar.menu.clear()
        var menu = main_bottombar.menu
        menu.add(0, 1, 1, "page")
        val findItem = menu.findItem(1)
        findItem.setIcon(android.R.drawable.star_big_on)

    }

    var changeIndex = 0;
    fun jump(view: View) {
        if (changeIndex == 0) {
            changeIndex = 1
            initBottomNavigation(true)
        } else {
            changeIndex = 0
            initBottomNavigation(false)
        }

//        startActivity(Intent(MainActivity@ this, MainActivity::class.java))
    }
}