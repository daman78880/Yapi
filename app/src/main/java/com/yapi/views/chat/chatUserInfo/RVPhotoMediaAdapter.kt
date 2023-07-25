package com.yapi.views.chat.chatUserInfo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import com.yapi.common.RoundRectCornerImageView

class RVPhotoMediaAdapter(
    private var context: Context, private var photoList: ArrayList<String>,
    private var finalPerPhoto: Int, private var maxValue: Int,
) : RecyclerView.Adapter<RVPhotoMediaAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivPhotoImageView: RoundRectCornerImageView =
            view.findViewById<RoundRectCornerImageView>(R.id.ivPhotoImageView)
        var ivPhotoAlpha = view.findViewById<RoundRectCornerImageView>(R.id.ivPhotoAlpha)
        var tvPhotoPendingValue = view.findViewById<AppCompatTextView>(R.id.tvPhotoPendingValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.rv_photo_media_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.ivPhotoImageView.layoutParams.width = finalPerPhoto
        holder.ivPhotoImageView.layoutParams.height = finalPerPhoto
        holder.ivPhotoImageView.requestLayout()

        if (photoList.size > maxValue) {
            if (position == maxValue - 1) {
                holder.ivPhotoAlpha.visibility = View.VISIBLE
                holder.tvPhotoPendingValue.visibility = View.VISIBLE
                holder.tvPhotoPendingValue.setText((photoList.size-maxValue+1).toString()+" +")
            } else {
                holder.ivPhotoAlpha.visibility = View.GONE
                holder.tvPhotoPendingValue.visibility = View.GONE
            }
        } else {
            holder.ivPhotoAlpha.visibility = View.GONE
            holder.tvPhotoPendingValue.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        if (photoList.size > maxValue) {
            return maxValue
        } else {
            return photoList.size
        }
    }
}