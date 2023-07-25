package com.yapi.views.search_result

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import com.yapi.common.ChatRoundRectCornerImageView
import com.yapi.common.RoundRectCornerImageView
import com.yapi.common.checkDeviceType
import com.yapi.databinding.FragmentSearchBinding

class AdapterSearch(
    val context: Context,
    val list: ArrayList<PojoSearchScreenData>,
    val  clickListener: ClickListener
) :
    RecyclerView.Adapter<AdapterSearch.MyViewHolder>() {
    class MyViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleName: AppCompatTextView = itemView.findViewById(R.id.txtTempTitleRvSearchLayout)
        val titleSubName: AppCompatTextView =
            itemView.findViewById(R.id.txtViewAllTitleRvSearchLayout)
        val layoutFileName: ConstraintLayout =
            itemView.findViewById(R.id.layoutFileNameRvSearchLayout)
        val layoutMsg: ConstraintLayout = itemView.findViewById(R.id.layoutMessageRvSearchLayout)
        val layoutImageFileName: ConstraintLayout =
            itemView.findViewById(R.id.layoutIconRvSearchLayout)
        val imageFileName: RoundRectCornerImageView =
            itemView.findViewById(R.id.imgImageRvSearchLayout)
        val drawableImageFileName: AppCompatImageView =
            itemView.findViewById(R.id.imgImageDrawableRvSearchLayout)
        val firstCharacterFileName: AppCompatTextView =
            itemView.findViewById(R.id.txtFirstCharacterRvSearchLayout)
        val titleFileName: AppCompatTextView = itemView.findViewById(R.id.txtTitleRvSearchLayout)
        val subTitleFileName: AppCompatTextView =
            itemView.findViewById(R.id.txtSubTitleRvSearchLayout)

        val groupIconMessage: AppCompatImageView =
            itemView.findViewById(R.id.imgGroupIconRvSearchLayout)
        val groupNameMessage: AppCompatTextView =
            itemView.findViewById(R.id.txtGroupNameRvSearchLayout)
        val groupTimeMessage: AppCompatTextView =
            itemView.findViewById(R.id.txtGroupTimeRvSearchLayout)
        val userImageMessage: ChatRoundRectCornerImageView =
            itemView.findViewById(R.id.imgUserImageRvSearchLayout)
        val messageCardLayout: RelativeLayout =
            itemView.findViewById(R.id.cardViewTextMessageRvSearchLayout)
        val userMsg: AppCompatTextView = itemView.findViewById(R.id.txtMessageRvSearchLayout)
        val msgDetailLayout: LinearLayout =
            itemView.findViewById(R.id.linearUserDetailRvSearchLayout)
        val userNameMessage: AppCompatTextView =
            itemView.findViewById(R.id.txtUserNameRvSearchLayout)
        val userMsgTimeMessage: AppCompatTextView =
            itemView.findViewById(R.id.txtMessageTimeRvSearchLayout)
        val emojiMessage: AppCompatImageView =
            itemView.findViewById(R.id.imgEmojiIconRvSearchLayout)
        val moreOptionMessage: AppCompatImageView =
            itemView.findViewById(R.id.imgMoreChatRvSearchLayout)

        val viewLine: View = itemView.findViewById(R.id.viewLineRvSearchLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_search_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (list[position].statusTitle == null) {
            holder.titleName.visibility = View.GONE
            holder.titleSubName.visibility = View.GONE
        } else {
            holder.titleName.visibility = View.VISIBLE
            holder.titleSubName.visibility = View.VISIBLE
            holder.titleName.text = list[position].statusTitle
            holder.titleSubName.text = list[position].statusSubTitle
        }
        holder.layoutImageFileName.setBackgroundResource(android.R.color.transparent)
        if (list[position].status == 0) {
            holder.layoutFileName.visibility = View.VISIBLE
            holder.layoutMsg.visibility = View.GONE

            holder.imageFileName.visibility = View.VISIBLE
            holder.drawableImageFileName.visibility = View.GONE
            holder.firstCharacterFileName.visibility = View.GONE

            holder.imageFileName.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.demo_photo))
            holder.titleFileName.text = list[position].nameOrFileName
            if (list[position].daginationOrFileSize == null) {
                holder.subTitleFileName.visibility = View.GONE
            } else {
                holder.subTitleFileName.visibility = View.VISIBLE
                holder.subTitleFileName.text = list[position].daginationOrFileSize
            }
        } else if (list[position].status == 1) {
            holder.layoutFileName.visibility = View.VISIBLE
            holder.drawableImageFileName.visibility = View.VISIBLE
            holder.layoutMsg.visibility = View.GONE
            holder.imageFileName.visibility = View.GONE
            holder.firstCharacterFileName.visibility = View.GONE
            holder.layoutImageFileName.setBackgroundResource(R.drawable.round_lite_gray)
            holder.drawableImageFileName.setImageDrawable(list[position].imageOrDrawable)
            holder.titleFileName.text = list[position].nameOrFileName
            if (list[position].daginationOrFileSize == null) {
                holder.subTitleFileName.visibility = View.GONE
            } else {
                holder.subTitleFileName.visibility = View.VISIBLE
                holder.subTitleFileName.text = list[position].daginationOrFileSize
            }
        } else if (list[position].status == 2) {
            holder.layoutFileName.visibility = View.VISIBLE
            holder.layoutMsg.visibility = View.GONE

            holder.imageFileName.visibility = View.GONE
            holder.drawableImageFileName.visibility = View.GONE
            holder.firstCharacterFileName.visibility = View.VISIBLE
            holder.layoutImageFileName.setBackgroundResource(R.drawable.round_lite_gray)

            holder.firstCharacterFileName.text = list[position].nameOrFileName?.get(0).toString()
            holder.titleFileName.text = list[position].nameOrFileName
            if (list[position].daginationOrFileSize == null) {
                holder.subTitleFileName.visibility = View.GONE
            } else {
                holder.subTitleFileName.visibility = View.VISIBLE
                holder.subTitleFileName.text = list[position].daginationOrFileSize
            }
        } else {
            holder.layoutFileName.visibility = View.GONE
            holder.layoutMsg.visibility = View.VISIBLE
            holder.userImageMessage.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.demo_photo))
            holder.groupIconMessage.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.group_icon))
            holder.groupNameMessage.text = list[position].groupName
            holder.groupTimeMessage.text = list[position].groupCreatedTime
            holder.userNameMessage.text = list[position].nameOrFileName
            holder.userMsgTimeMessage.text = list[position].msgTime
            holder.userMsg.text = list[position].msg
        }
        if (position < list.size - 1) {
            if (list[position].statusTitle == list[position + 1].statusTitle || list[position].statusTitle == null) {
                holder.viewLine.visibility = View.GONE
//                    holder.titleName.visibility=View.GONE
//                    holder.titleSubName.visibility=View.GONE
            } else {
                holder.viewLine.visibility = View.VISIBLE
            }
        } else {
            if (list[position].statusTitle != null)
                holder.viewLine.visibility = View.VISIBLE
        }
        if (position > 0) {
            if (list[position - 1].statusTitle == list[position].statusTitle) {
                holder.titleName.visibility = View.GONE
                holder.titleSubName.visibility = View.GONE
            } else {
                holder.titleName.visibility = View.VISIBLE
                holder.titleSubName.visibility = View.VISIBLE
            }
        }
        if (position == list.size - 1) {
            if (list[position].endViewLine == true) {
                holder.viewLine.visibility = View.VISIBLE
            }
        }
        holder.itemView.setOnClickListener {
            if(checkDeviceType())
            {
                clickListener.onClickLisener(position)
            }else
            {
                if (it.findNavController().currentDestination?.id == R.id.searchFragment)
                    it.findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment)
            }

        }
        holder.itemView.setOnLongClickListener {
            if(checkDeviceType())
            {
            }else
            {
            if (it.findNavController().currentDestination?.id == R.id.searchFragment) {
                val bundel = Bundle()
                bundel.putBoolean("empty", true)
                it.findNavController()
                    .navigate(R.id.action_searchFragment_to_searchResultFragment, bundel)
            }
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

} interface ClickListener
{
    fun onClickLisener(position: Int)
}
