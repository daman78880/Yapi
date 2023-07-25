package com.yapi.views.signupTeam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yapi.R
import com.yapi.common.RoundRectCornerImageView
import com.yapi.databinding.RvUsersAdapterBinding

class RVUsersAdapter(
    private var context: Context,
    private var invitationList: ArrayList<ViewInvitationData>,
    private var clickListener: TeamClickListener
) :
    RecyclerView.Adapter<RVUsersAdapter.MyViewHolder>() {
    private lateinit var binding: RvUsersAdapterBinding

    fun getInvitationList():ArrayList<ViewInvitationData>
    {
       return invitationList
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFirstCharacter = itemView.findViewById<TextView>(R.id.tvFirstCharacter)
        var tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        var tvMembersCount = itemView.findViewById<TextView>(R.id.tvMembersCount)
        var ivGroupPhoto = itemView.findViewById<RoundRectCornerImageView>(R.id.ivGroupPhoto)
        var joinNowBTN = itemView.findViewById<AppCompatButton>(R.id.joinNowBTN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //  binding = RvUsersAdapterBinding.inflate(LayoutInflater.from(context))
        var view = LayoutInflater.from(context).inflate(R.layout.rv_users_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvUserName.text = invitationList[position].name
        if(invitationList[position].memberCount.toInt()>1)
        {
            holder.tvMembersCount.text = (invitationList[position].memberCount.toInt()).toString() + " members"
        }else
        {
            holder.tvMembersCount.text = (invitationList[position].memberCount.toInt()).toString() + " member"
        }

        if(!(invitationList[position].image_url.equals(""))){
            Glide.with(context)
                .load(invitationList[position].image_url)
                .into(binding.ivGroupPhoto)
            holder.tvFirstCharacter.visibility=View.GONE
            holder.ivGroupPhoto.visibility=View.VISIBLE
        }else
        {
            holder.tvFirstCharacter.setText(invitationList[position].name.substring(0,1).toUpperCase())
            holder.tvFirstCharacter.visibility=View.VISIBLE
            holder.ivGroupPhoto.visibility=View.GONE
        }
        holder.joinNowBTN.setOnClickListener {
            clickListener.onClickJoin(position,invitationList[position]._id)
        }
    }

    override fun getItemCount(): Int {
        return invitationList.size
    }
}

interface TeamClickListener
{
    fun onClickJoin(position: Int,teamId:String)
}