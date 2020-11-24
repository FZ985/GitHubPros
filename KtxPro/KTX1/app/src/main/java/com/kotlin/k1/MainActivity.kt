package com.kotlin.k1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.kotlin.k1.adapter.Adapter1
//import com.kotlin.k1.adapter2.Adapter2
import com.kotlin.k1.adapter2.Adapter3
import com.kotlin.k1.adapter2.SortBean
import com.kotlin.k1.chinese2pinyin.PinyinComparator
import com.kotlin.k1.sticky.StickyRecyclerHeadersDecoration
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_recycle.layoutManager = LinearLayoutManager(this)
//        adapter1()
//        adapter2()
        adapter3()
    }

    private fun adapter1() {
        val adapter = Adapter1(Utils.items.toMutableList())
        main_recycle.adapter = adapter
        adapter.loadMoreModule.setOnLoadMoreListener {
            main_recycle.postDelayed({
                adapter.addData(Utils.items.toMutableList())
                adapter.loadMoreModule.loadMoreComplete()
            }, 300)
        }
    }

    private fun adapter2() {
//        val datas = mutableListOf<SortBean>()
//        for (sortname in Utils.items) {
//            datas.add(SortBean(sortname))
//        }
//        Collections.sort(datas, PinyinComparator())
//        val adapter2 = Adapter2(datas)
//        main_recycle.adapter = adapter2
//        val headDecor = StickyRecyclerHeadersDecoration(adapter2)
//        main_recycle.addItemDecoration(headDecor)
//        adapter2.registerAdapterDataObserver(object : AdapterDataObserver() {
//            override fun onChanged() {
//                headDecor.invalidateHeaders()
//            }
//        })
    }

    private fun adapter3() {
        val datas = mutableListOf<SortBean>()
        for (sortname in Utils.items) {
            datas.add(SortBean(sortname))
        }
        Collections.sort(datas, PinyinComparator())
        val adapter3 = Adapter3(datas)
        main_recycle.adapter = adapter3
        val headDecor = StickyRecyclerHeadersDecoration(adapter3)
        main_recycle.addItemDecoration(headDecor)
        adapter3.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                headDecor.invalidateHeaders()
            }
        })
//        headDecor.invalidateHeaders()
    }

}