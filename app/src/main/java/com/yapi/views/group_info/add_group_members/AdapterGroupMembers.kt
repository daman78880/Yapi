package com.yapi.views.group_info.add_group_members

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.yapi.R

class AdapterGroupMembers(val context: Context,val list:ArrayList<PojoGroupMembers>):RecyclerView.Adapter<AdapterGroupMembers.MyViewHolder>() {
class MyViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView){
    val img=itemView.findViewById<RoundedImageView>(R.id.imgPictureRvMemberListGroup)
    val name=itemView.findViewById<AppCompatTextView>(R.id.txtUserNameRvMemberListGroup)
    val designation=itemView.findViewById<AppCompatTextView>(R.id.txtUserDesignationRvMemberListGroup)
    val txtUserPositionRvMenerListCreateGroup: AppCompatTextView =itemView.findViewById<AppCompatTextView>(R.id.txtUserPositionRvMenerListCreateGroup)
    val layouttxtUserPositionRvMenerListCreateGroup=itemView.findViewById<ConstraintLayout>(R.id.layoutUserPositionRvMemberListGroup)
    val layoutRemoveRvMenerListCreateGroup=itemView.findViewById<ConstraintLayout>(R.id.layoutUserRemoveRvMemberListGroup)
    val imgMoreOption=itemView.findViewById<AppCompatImageView>(R.id.imgEditRvMemberListGroup)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.rv_member_list_group,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text=list[position].name
        holder.designation.text=list[position].designation
        holder.txtUserPositionRvMenerListCreateGroup.setText("Manager")
        holder.layouttxtUserPositionRvMenerListCreateGroup.setOnClickListener {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
        }
        holder.layoutRemoveRvMenerListCreateGroup.setOnClickListener {
            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()
        }
        if (context.resources.getBoolean(R.bool.isTab)) {
            holder.layoutRemoveRvMenerListCreateGroup.visibility=View.VISIBLE
            holder.imgMoreOption.visibility=View.GONE
        }
        else{
            holder.layoutRemoveRvMenerListCreateGroup.visibility=View.GONE
            holder.imgMoreOption.visibility=View.VISIBLE
        }

      /*  if(position==0 || position==2)
        {
            holder.txtUserPositionRvMenerListCreateGroup.setText("Owner")
        }else
        {*/
       // }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}