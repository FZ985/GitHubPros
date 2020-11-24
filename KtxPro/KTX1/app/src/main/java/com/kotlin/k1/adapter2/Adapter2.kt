//package com.kotlin.k1.adapter2
//
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.chad.library.adapter.base.viewholder.BaseViewHolder
//import com.kotlin.k1.R
//import com.kotlin.k1.sticky.BaseStickyHeaderAdapter
//import com.kotlin.k1.sticky.callback.StickyRecyclerHeadersAdapter
//
//class Adapter2(data: MutableList<SortBean>?) :
//    BaseStickyHeaderAdapter<SortBean, BaseViewHolder>(R.layout.item_list, data),
//    StickyRecyclerHeadersAdapter<BaseViewHolder> {
//
//    override fun convert(holder: BaseViewHolder, item: SortBean) {
//        holder.setText(R.id.item_tv, item.displayInfo)
//    }
//
//    override fun getHeaderId(position: Int): Long {
//        println(
//            "position--headId:" + position + "," + getItem(position).fullName[0] + ",long:" + getItem(
//                position
//            ).fullName[0].toLong()
//        )
//        return getItem(position).fullName[0].toLong()
//    }
//
//    override fun onCreateHeaderViewHolder(parent: ViewGroup): BaseViewHolder {
//        return BaseViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.head_sort, parent, false)
//        )
//    }
//
//    override fun onBindHeaderViewHolder(holder: BaseViewHolder, position: Int) {
//        println("bindviewholder:${getItem(position).letter},==null：${holder == null}")
//        holder.setBackgroundColor(R.id.head_tv, Color.BLACK)
//        holder.setText(R.id.head_tv, getItem(position).letter)
//    }
//}