package com.yapi.views.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R

class RVEditMessageAdapter(
    private var context: Context,
    private var editMessageList: ArrayList<EditMessageData>,private var clickListener:ClickMessage
) : RecyclerView.Adapter<RVEditMessageAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivEditIcon = itemView.findViewById<ImageView>(R.id.ivEditIcon)
        var tvEditMessage = itemView.findViewById<AppCompatTextView>(R.id.tvEditMessage)
        var constraintsPinConverrsation = itemView.findViewById<ConstraintLayout>(R.id.constraintsPinConverrsation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.rv_edit_message_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.ivEditIcon.setImageResource(editMessageList[position].image)
        holder.tvEditMessage.text = editMessageList[position].name

        if(position==editMessageList.size-1)
        {
            holder.tvEditMessage.setTextColor(context.resources.getColor(R.color.darkRed))
        }else
        {
            holder.tvEditMessage.setTextColor(context.resources.getColor(R.color.darkGrey))
        }

        holder.constraintsPinConverrsation.setOnClickListener {
            clickListener.onClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return editMessageList.size
    }
}

interface ClickMessage{
    fun onClickListener(position: Int)
}
