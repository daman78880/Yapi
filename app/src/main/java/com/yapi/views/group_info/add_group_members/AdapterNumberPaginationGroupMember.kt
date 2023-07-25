package com.yapi.views.group_info.add_group_members

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import kotlinx.coroutines.handleCoroutineException

class AdapterNumberPaginationGroupMember(
    val context: Context,
    val list: ArrayList<Int>,
    var selectedPage: Int = 0,
    val click: Click,
) : RecyclerView.Adapter<AdapterNumberPaginationGroupMember.MyViewHolder>() {
    var temp = 1
    var visibleCount = 0
    var highlightTxt = 1

    class
    MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val arrowLayout: ConstraintLayout = itemView.findViewById(R.id.layoutArrowNumberPagination)
        val arrowImg: AppCompatImageView = itemView.findViewById(R.id.imgArrowNumberPagination)

        val dotLayout: ConstraintLayout = itemView.findViewById(R.id.layoutImgDotNumberPagination)
        val dotImg: AppCompatImageView = itemView.findViewById(R.id.imgDotNumberPagination)

        val txtLayout: ConstraintLayout = itemView.findViewById(R.id.layoutTxtNumberPagination)
        val txt: AppCompatTextView = itemView.findViewById(R.id.txtNumberPagination)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.number_pagination, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position >= selectedPage) {
            if (position == list.size - 1) {
                Log.i("asdjfnhaskjdfn", "visible count $visibleCount")
                if (visibleCount != 2) {
                    holder.arrowLayout.visibility = View.VISIBLE
                } else {
                    holder.arrowImg.visibility = View.GONE
                }
                holder.txtLayout.visibility = View.VISIBLE
                holder.dotLayout.visibility = View.VISIBLE
                holder.arrowImg.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.chat_more_icon))
                holder.arrowImg.rotation = 90f
                holder.dotImg.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_baseline_keyboard_arrow_right_24))
                if(highlightTxt==3){
                    holder.txt.setTextColor(ContextCompat.getColor(context,
                        R.color.white))
                    holder.txtLayout.setBackgroundResource(R.drawable.btn_drawable_blue)

                }else{

                    holder.txt.setTextColor(ContextCompat.getColor(context,
                        R.color.drawableLiteGreyColor))
                    holder.txtLayout.setBackgroundColor(ContextCompat.getColor(context,
                        android.R.color.transparent))
                }
                holder.dotImg.rotation = 0f
                holder.txt.text = list.size.toString()
            } else if (temp <= 3) {
                visibleCount += 1
                temp += 1
                if (temp == 2) {
                    holder.arrowLayout.visibility = View.VISIBLE
                    holder.txtLayout.visibility = View.VISIBLE
                    holder.dotLayout.visibility = View.GONE
                    holder.arrowImg.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_baseline_keyboard_arrow_left_24))
                    holder.arrowImg.rotation = 0f
                    holder.dotImg.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.chat_more_icon))
                    if(temp-1==highlightTxt) {
                        holder.txtLayout.setBackgroundResource(R.drawable.btn_drawable_blue)
                        holder.txt.setTextColor(ContextCompat.getColor(context,
                            R.color.white))
                    }else{
                        holder.txtLayout.setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.transparent))
                        holder.txt.setTextColor(ContextCompat.getColor(context,
                            R.color.drawableLiteGreyColor))
                    }
                    holder.dotImg.rotation = 90f
                    holder.txt.text = list[position].toString()


                } else if (temp == 3) {
                    holder.arrowLayout.visibility = View.GONE
                    holder.txtLayout.visibility = View.VISIBLE
                    holder.dotLayout.visibility = View.GONE
                    if(temp-1==highlightTxt) {
                        holder.txtLayout.setBackgroundResource(R.drawable.btn_drawable_blue)
                        holder.txt.setTextColor(ContextCompat.getColor(context,
                            R.color.white))
                    }else{
                        holder.txtLayout.setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.transparent))
                        holder.txt.setTextColor(ContextCompat.getColor(context,
                            R.color.drawableLiteGreyColor))
                    }
                    holder.txt.text = list[position].toString()
                } else {
                    holder.arrowLayout.visibility = View.GONE
                    holder.txtLayout.visibility = View.VISIBLE
                    holder.dotLayout.visibility = View.GONE
                    if(temp-1==highlightTxt) {
                        holder.txtLayout.setBackgroundResource(R.drawable.btn_drawable_blue)
                        holder.txt.setTextColor(ContextCompat.getColor(context,
                            R.color.white))
                    }else{
                        holder.txtLayout.setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.transparent))
                        holder.txt.setTextColor(ContextCompat.getColor(context,
                            R.color.drawableLiteGreyColor))
                    }
                    holder.txtLayout.setBackgroundColor(ContextCompat.getColor(context,
                        android.R.color.transparent))
                    holder.txt.setTextColor(ContextCompat.getColor(context,
                        R.color.drawableLiteGreyColor))
                    holder.txt.text = list[position].toString()
                }
            } else {
                holder.arrowLayout.visibility = View.GONE
                holder.txtLayout.visibility = View.GONE
                holder.dotLayout.visibility = View.GONE
            }
        } else {
            holder.arrowLayout.visibility = View.GONE
            holder.txtLayout.visibility = View.GONE
            holder.dotLayout.visibility = View.GONE
        }
        holder.dotLayout.setOnClickListener {

            if(selectedPage==list.size-3 && highlightTxt == 2 ){
                Log.i("asjfsnadjf","inside if condition")
                highlightTxt=3
                click.onClick(selectedPage)
            }else{
                if (highlightTxt < 2) {
                    highlightTxt = 2
                    click.onClick(selectedPage )
                }
                else if(highlightTxt==2 && selectedPage == list.size-4){
                    highlightTxt=2
                    click.onClick(selectedPage+1 )
                }
                else {
                    if(selectedPage==list.size-3 && highlightTxt==3) {
                    }else{
                        click.onClick(selectedPage + 2)
                        highlightTxt = 1
                    }
                }
            }
        }
        holder.arrowLayout.setOnClickListener {
            if(selectedPage==list.size-3 && highlightTxt == 3 ){
                highlightTxt=2
                click.onClick(selectedPage)
            }else{
            if (selectedPage >= 1) {
                Log.i("sadkkfnas","if")
//                highlightTxt-=1
                if (highlightTxt >1) {
                    highlightTxt = 1
                    click.onClick(selectedPage )
                }
                else {
                    highlightTxt = 1
                click.onClick(selectedPage - 1)
                }
            }else{
                Log.i("sadkkfnas","else")
                if (highlightTxt >1) {
                    highlightTxt = 1
                    click.onClick(selectedPage )
                }
            }
            }
        }
        holder.txtLayout.setOnClickListener {
            Log.i("asdhfbasd","position $position")
            if(position==0){
               Log.i("asdhfbasd","position 0 page $selectedPage")
                highlightTxt=1
                click.onClick(selectedPage)
            }else if(position==1){
               var page=selectedPage
               Log.i("asdhfbasd","position 1 page $page")
               highlightTxt=2
               click.onClick(page)
            }else {
               var page=selectedPage+2
               Log.i("asdhfbasd","position else page $page")
               highlightTxt=1
               click.onClick(page)
            }
//            if (selectedPage - 1 != position && selectedPage) {
//                val check = list.size - position
//                if (check >= 3) {
//                    if (highlightTxt < 2) {
//
//                    }
//                    click.onClick(list[position] - 1)
//                }
//            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onClick(selectedPosition: Int)
    }
}