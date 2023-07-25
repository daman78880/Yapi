package com.yapi.views.add_people_email_confirmation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import com.yapi.views.add_people_email.Invitaion

class AdapterEmailConfirmation(val context: Context, val invitationList:ArrayList<Invitaion>):RecyclerView.Adapter<AdapterEmailConfirmation.MyViewHolder>() {
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val txtTitleEmailEvEmailConfirmList:AppCompatTextView=itemView.findViewById(R.id.txtTitleEmailEvEmailConfirmList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.rv_email_confirmation_list,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.txtTitleEmailEvEmailConfirmList.text=invitationList[position].user_email
        //    "status": "pending",
    }

    override fun getItemCount(): Int {
        return invitationList.size
    }
}