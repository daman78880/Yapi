package com.yapi.recycleradapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.yapi.BR
import com.yapi.R


class RecyclerAdapter<T : AbstractModel>(@LayoutRes val layoutId: Int) :
    RecyclerView.Adapter<RecyclerAdapter.VH<T>>() {

    private val animatedPosition: HashSet<Int> by lazy { HashSet() }
    private val items by lazy { mutableListOf<T>() }
    private var inflater: LayoutInflater? = null
    private var onItemClick: OnItemClick? = null
    private var isAnimation = true

    fun setAnimations(boolean: Boolean) {
        isAnimation = boolean
    }

    fun getAllItems() = items

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
        Log.e("dataList" ,items.toString() )
    }

    fun setOnItemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<T> {
        val layoutInflater = inflater ?: LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH<T>, position: Int) {

//        holder.itemView.animation =
//           AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_anim)
        try {
            val model = items[position]
            model.adapterPosition = position
            onItemClick?.let { model.onItemClick = it }
            holder.bind(model)
            //if (isAnimation)
            // Turning off animations to make the app more smooth
            // setAnimation(holder, position)
           // holder.setIsRecyclable(false)
        } catch (e: Exception) {

        }
    }

    private fun setAnimation(holder: RecyclerView.ViewHolder, position: Int) {
        if (this.animatedPosition.contains(Integer.valueOf(position))) {
            holder.itemView.clearAnimation()
            return
        }

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_slide_from_bottom))
        this.animatedPosition.add(Integer.valueOf(position))
    }

    class VH<T : AbstractModel>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: T) {
            binding.setVariable(BR.model, model)
            binding.executePendingBindings()
        }
    }

    fun interface OnItemClick {
        fun onClick(view: View, position: Int, type: String)
    }

}


















