package com.yapi.views.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R

class RVchatAdapter(
    private var context: Context, private var clickListener: MessageClickListener,
    private var arraylist: ArrayList<String>,
) :
    RecyclerView.Adapter<RVchatAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_more_chat = view.findViewById<ImageView>(R.id.iv_more_chat)
        var constraintsTimeLayout = view.findViewById<ConstraintLayout>(R.id.constraintsTimeLayout)
        var rvPhotoVW = view.findViewById<RecyclerView>(R.id.rvPhotoVW)

        var cardViewLeftTextMessage =
            view.findViewById<RelativeLayout>(R.id.cardViewLeftTextMessage)
        var cardViewMediaLeft = view.findViewById<RelativeLayout>(R.id.cardViewMediaLeft)
        var cardViewAudioLeft = view.findViewById<RelativeLayout>(R.id.cardViewAudioLeft)
        var tvViewAllPhotos = view.findViewById<AppCompatTextView>(R.id.tvViewAllPhotos)
        var cardViewFilesLeft = view.findViewById<RelativeLayout>(R.id.cardViewFilesLeft)

    }

    fun getChatList(): ArrayList<String> {
        return arraylist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View? = null
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.left_chat_layout, parent, false)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.right_chat_layout, parent, false)
        }
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.iv_more_chat.setOnClickListener {
           var userType=0
            if(position%2==0)
            {
                userType=0
            }else
            {
                userType=1
            }
            clickListener.onMesssageListener(position, holder.iv_more_chat,userType)
        }

        if (position == 0 || position == 4) {
            holder.constraintsTimeLayout.visibility = View.VISIBLE
        } else {
            holder.constraintsTimeLayout.visibility = View.GONE
        }

        holder.cardViewLeftTextMessage.visibility = View.GONE
        holder.cardViewMediaLeft.visibility = View.GONE
        holder.cardViewAudioLeft.visibility = View.GONE
        holder.cardViewFilesLeft.visibility=View.GONE

        if (arraylist[position].toString() == "AA") {
            holder.cardViewLeftTextMessage.visibility = View.VISIBLE
        } else
            if (arraylist[position].toString() == "BB") {
                holder.cardViewMediaLeft.visibility = View.VISIBLE
            } else
                if (arraylist[position].toString() == "CC") {
                    holder.cardViewMediaLeft.visibility = View.VISIBLE
                } else
                    if (arraylist[position].toString() == "DD") {
                        holder.cardViewLeftTextMessage.visibility = View.VISIBLE
                    } else
                        if (arraylist[position].toString() == "EE") {
                            holder.cardViewAudioLeft.visibility = View.VISIBLE
                        } else
                            if (arraylist[position].toString() == "FF") {
                                holder.cardViewAudioLeft.visibility = View.VISIBLE
                            } else
                            if(arraylist[position].toString()=="GG")
                            {
                                holder.cardViewFilesLeft.visibility=View.VISIBLE
                            }else
                            {
                                holder.cardViewLeftTextMessage.visibility = View.VISIBLE
                            }


        var photoArrayList=ArrayList<String>()
        photoArrayList.clear()
        photoArrayList.add("AA")
        photoArrayList.add("BB")
        photoArrayList.add("CC")
        photoArrayList.add("DD")
        photoArrayList.add("EE")
        if(photoArrayList.size>4)
        {
            holder.tvViewAllPhotos.visibility=View.VISIBLE
        }else
        {
            holder.tvViewAllPhotos.visibility=View.GONE
        }

        var adapter = RVChatPhotoAdapter(context,photoArrayList)
        holder.rvPhotoVW.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvPhotoVW.adapter = adapter

    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            1
        } else {
            2
        }
    }
}

interface MessageClickListener {
    fun onMesssageListener(position: Int, ivMoreImageView: ImageView,userType:Int)
}