package com.yapi.views.menu_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R

class AdapterCustomerList(val context: Context, val list:ArrayList<PojoCustomerList>,val click: AdapterCustomerList.Click,
val userType:String):RecyclerView.Adapter<AdapterCustomerList.MyViewHolder>() {
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val img:com.makeramen.roundedimageview.RoundedImageView=view.findViewById(R.id.imgProfilePicRvCustomerList)
        val onlineStatus:com.makeramen.roundedimageview.RoundedImageView=view.findViewById(R.id.onlineStatusRvCustomerList)
        val userName:AppCompatTextView=view.findViewById(R.id.txtUserNameRvCustomerList)
        val unSeenMsgCount:AppCompatTextView=view.findViewById(R.id.txtCountRvCustomerList)
        val selectedLine:View=view.findViewById(R.id.selectedLineRvCustomersList)
        val constraintsLayoutStart:ConstraintLayout=view.findViewById(R.id.constraintsLayoutStart)
        val layoutAddNewTeamsMenuAdapter:ConstraintLayout=view.findViewById(R.id.layoutAddNewTeamsMenuAdapter)
        val imgAddNewTeamsMenu:AppCompatTextView=view.findViewById(R.id.imgAddNewTeamsMenu)
    }
    fun getListt():ArrayList<PojoCustomerList>
    {
        return list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.rv_customers_list_menu,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.img.setImageResource(R.drawable.demo_photo)
        holder.userName.text=list[position].name
        if(list[position].onlineStatus) {
            holder.onlineStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.onlineStatusColor))
        }else{
            holder.onlineStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.offlineStatusColor))
        }
        if(list[position].unSeenMsgCount>0)
        {
            holder.unSeenMsgCount.text = list[position].unSeenMsgCount.toString()
            holder.unSeenMsgCount.visibility=View.VISIBLE
            holder.userName.setTextColor(ContextCompat.getColor(context,R.color.darkGrey))
        }
        else
        {
            holder.unSeenMsgCount.visibility=View.INVISIBLE
            holder.userName.setTextColor(ContextCompat.getColor(context,R.color.medium_grey_color))
        }
        if(list[position].selectedStatus){
            holder.selectedLine.visibility=View.VISIBLE
            holder.userName.setTextColor(ContextCompat.getColor(context,R.color.blueColor))
        }
        else {
            holder.selectedLine.visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            if(list[position].unSeenMsgCount!=-1) {
                click.onSeletect(holder.adapterPosition,userType)
            }
        }

        if(list[position].unSeenMsgCount!=-1)
        {
            holder.imgAddNewTeamsMenu.setText("")
            holder.constraintsLayoutStart.visibility=View.VISIBLE
            holder.layoutAddNewTeamsMenuAdapter.visibility=View.GONE
        }else
        {
            holder.imgAddNewTeamsMenu.setText(list[position].name)
            holder.constraintsLayoutStart.visibility=View.GONE
            holder.layoutAddNewTeamsMenuAdapter.visibility=View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click{
        fun onSeletect(position: Int,userType:String)
    }
}