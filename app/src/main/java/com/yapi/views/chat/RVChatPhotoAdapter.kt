package com.yapi.views.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import com.yapi.common.RoundRectCornerImageView

class RVChatPhotoAdapter(private var context: Context,private val photoArrayList: ArrayList<String>) :
    RecyclerView.Adapter<RVChatPhotoAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhotoView = itemView.findViewById<RoundRectCornerImageView>(R.id.ivPhotoView)
        val relVideoView = itemView.findViewById<RelativeLayout>(R.id.relVideoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_photo_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(photoArrayList.size>3) {
            if (position == 3) {
                holder.ivPhotoView.visibility = View.GONE
                holder.relVideoView.visibility = View.VISIBLE
            } else {
                holder.ivPhotoView.visibility = View.VISIBLE
                holder.relVideoView.visibility = View.GONE
            }
        }
        else
        {
            holder.ivPhotoView.visibility = View.VISIBLE
            holder.relVideoView.visibility = View.GONE
            val params: ConstraintLayout.LayoutParams = holder.ivPhotoView.getLayoutParams() as ConstraintLayout.LayoutParams
            if(photoArrayList.size==1)
            {
                params.width = context.resources.getDimension(com.intuit.sdp.R.dimen._70sdp).toInt()
                params.height = context.resources.getDimension(com.intuit.sdp.R.dimen._70sdp).toInt()
            }else  if(photoArrayList.size==2)
            {
                params.width = context.resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
                params.height = context.resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
            }else
            {
                params.width = context.resources.getDimension(com.intuit.sdp.R.dimen._35sdp).toInt()
                params.height = context.resources.getDimension(com.intuit.sdp.R.dimen._35sdp).toInt()
            }

                holder.ivPhotoView.setLayoutParams(params)

        }
    }

    override fun getItemCount(): Int {
        if(photoArrayList.size>4)
        {
            return 4
        }else
        {
            return photoArrayList.size
        }
    }
}