package com.yapi.views.savedItems

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import com.yapi.common.ChatRoundRectCornerImageView
import com.yapi.views.chat.MessageClickListener
import com.yapi.views.chat.RVChatPhotoAdapter

class RVSavedItemsAdapter(
    private var context: Context,
    private var savedIetmsArrayList: ArrayList<String>,
    private var clickListener: MessageClickListener,
) : RecyclerView.Adapter<RVSavedItemsAdapter.MySavedItemsViewHolder>() {

    class MySavedItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var constraintsTimeLayout =
            itemView.findViewById<ConstraintLayout>(R.id.constraintsTimeLayout)

        var iv_more_chat = itemView.findViewById<ImageView>(R.id.iv_more_chat)
        var rvPhotoVW = itemView.findViewById<RecyclerView>(R.id.rvPhotoVW)

        var cardViewLeftTextMessage =
            itemView.findViewById<RelativeLayout>(R.id.cardViewLeftTextMessage)
        var cardViewMediaLeft = itemView.findViewById<RelativeLayout>(R.id.cardViewMediaLeft)
        var cardViewAudioLeft = itemView.findViewById<RelativeLayout>(R.id.cardViewAudioLeft)
        var tvViewAllPhotos = itemView.findViewById<AppCompatTextView>(R.id.tvViewAllPhotos)
        var cardViewFilesLeft = itemView.findViewById<RelativeLayout>(R.id.cardViewFilesLeft)
        var topChatConstraints = itemView.findViewById<ConstraintLayout>(R.id.topChatConstraints)
        var ivLeftUserPhoto = itemView.findViewById<ChatRoundRectCornerImageView>(R.id.ivLeftUserPhoto)
        var linearLeftUserDetail = itemView.findViewById<LinearLayout>(R.id.linearLeftUserDetail)
        var cardLeftChatText = itemView.findViewById<RelativeLayout>(R.id.cardLeftChatText)
        var tvTypeMessage = itemView.findViewById<AppCompatTextView>(R.id.tvTypeMessage)
        var tvLeftMessage = itemView.findViewById<AppCompatTextView>(R.id.tvLeftMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySavedItemsViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.left_chat_layout, parent, false)
        return MySavedItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MySavedItemsViewHolder, position: Int) {
        holder.topChatConstraints.setBackgroundResource(R.drawable.saved_items_back)
        holder.constraintsTimeLayout.visibility = View.GONE
        holder.tvTypeMessage.visibility = View.VISIBLE

        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        var marginValue=context.resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt()
        var otherMarginValue=context.resources.getDimension(com.intuit.sdp.R.dimen._5sdp).toInt()

        params.setMargins(marginValue, otherMarginValue, marginValue, otherMarginValue)
        holder.topChatConstraints.setLayoutParams(params)

        val constraintSet = ConstraintSet()
        constraintSet.clone(holder.topChatConstraints)

        constraintSet.connect(holder.ivLeftUserPhoto.getId(),
            ConstraintSet.TOP,
            holder.cardLeftChatText.getId(),
            ConstraintSet.TOP,
            0)
        var paddingStartValue=context.resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt()

        holder.topChatConstraints.setPadding(paddingStartValue,paddingStartValue,0,paddingStartValue)

        holder.tvLeftMessage.setPadding(0,0,paddingStartValue,0)
        constraintSet.applyTo(holder.topChatConstraints)
    /*    var photoSize=context.resources.getDimension(com.intuit.sdp.R.dimen._35sdp).toInt()

        val params22 = ConstraintLayout.LayoutParams(
            photoSize,
            photoSize
        )
        params22.setMargins(marginValue, 0, 0, 0)
        holder.ivLeftUserPhoto.setLayoutParams(params22)*/
      /*  val constraintSet = ConstraintSet()
        constraintSet.clone(holder.topChatConstraints)
      *//*  constraintSet.connect(R.id.imageView,
            ConstraintSet.RIGHT,
            R.id.ivLeftUserPhoto,
            ConstraintSet.RIGHT,
            0)*//*
        constraintSet.connect(R.id.ivLeftUserPhoto,
            ConstraintSet.TOP,
            R.id.cardLeftChatText,
            ConstraintSet.TOP,
            80)
        constraintSet.applyTo(holder.topChatConstraints)*/

     //   holder.ivLeftUserPhoto.setLayoutParams(params22)


        holder.iv_more_chat.setOnClickListener {
            clickListener.onMesssageListener(position, holder.iv_more_chat,1)
        }

        holder.cardViewLeftTextMessage.visibility = View.GONE
        holder.cardViewMediaLeft.visibility = View.GONE
        holder.cardViewAudioLeft.visibility = View.GONE
        holder.cardViewFilesLeft.visibility=View.GONE

        holder.cardViewLeftTextMessage.setBackgroundColor(Color.TRANSPARENT);


        if (savedIetmsArrayList[position].toString() == "AA") {
            holder.cardViewLeftTextMessage.visibility = View.VISIBLE
        } else
            if (savedIetmsArrayList[position].toString() == "BB") {
                holder.cardViewMediaLeft.visibility = View.VISIBLE
            } else
                if (savedIetmsArrayList[position].toString() == "CC") {
                    holder.cardViewMediaLeft.visibility = View.VISIBLE
                } else
                    if (savedIetmsArrayList[position].toString() == "DD") {
                        holder.cardViewLeftTextMessage.visibility = View.VISIBLE
                    } else
                        if (savedIetmsArrayList[position].toString() == "EE") {
                            holder.cardViewAudioLeft.visibility = View.VISIBLE
                        } else
                            if (savedIetmsArrayList[position].toString() == "FF") {
                                holder.cardViewAudioLeft.visibility = View.VISIBLE
                            } else
                                if(savedIetmsArrayList[position].toString()=="GG")
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
      /*  if(savedIetmsArrayList.size>4){
            return 4
        } else
        {
            return savedIetmsArrayList.size
        }*/
        return savedIetmsArrayList.size
    }
}