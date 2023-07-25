package com.yapi.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yapi.MainActivity

object BindingAdapter {

    @BindingAdapter(value = ["setRecyclerAdapter"], requireAll = false)
    @JvmStatic
    fun setRecyclerAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
    ) {
        recyclerView.adapter = adapter
    }

    @BindingAdapter(value = ["setRoundRectImage"], requireAll = false)
    @JvmStatic
    fun setRoundRectImage(
        recyclerView: RoundRectCornerImageView,
       value:String,
    ) {
        if(!(value.equals(""))){
        Glide.with(MainActivity.activity!!.get()!!)
            .load(value)
            .into(recyclerView)
    }}
}