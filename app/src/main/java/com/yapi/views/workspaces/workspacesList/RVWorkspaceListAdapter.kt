package com.yapi.views.workspaces.workspacesList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import com.yapi.common.checkDeviceType

class RVWorkspaceListAdapter(
    private var context: Context,
    private var workspaceList: ArrayList<WorkSpaceData>,
    private var clickListener: WorkspaceClickListener,
) :
    RecyclerView.Adapter<RVWorkspaceListAdapter.MyWorkspaceViewHolder>() {

    class MyWorkspaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivPlusIconWork = itemView.findViewById<AppCompatImageView>(R.id.ivPlusIconWork)
        var constarintDetails = itemView.findViewById<ConstraintLayout>(R.id.constarintDetails)
        var constraintsUsers = itemView.findViewById<ConstraintLayout>(R.id.constraintsUsers)
        var constarintsTopList = itemView.findViewById<ConstraintLayout>(R.id.constarintsTopList)
        var viewBottomLine = itemView.findViewById<View>(R.id.viewBottomLine)
        var tvEditWork = itemView.findViewById<AppCompatTextView>(R.id.tvEditWork)
        var tvNameValue = itemView.findViewById<AppCompatTextView>(R.id.tvNameValue)
        var tvOpenWork = itemView.findViewById<AppCompatTextView>(R.id.tvOpenWork)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWorkspaceViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.rv_workspace_list_adapter, parent, false)
        return MyWorkspaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyWorkspaceViewHolder, position: Int) {

        holder.tvNameValue.text = workspaceList[position].name
        if (workspaceList[position].isCheck!!) {
            holder.constarintDetails.visibility = View.VISIBLE
            holder.constraintsUsers.setBackgroundResource(R.drawable.user_list_box_background)
            holder.ivPlusIconWork.setImageResource(R.drawable.minus_user_list)
            holder.viewBottomLine.visibility = View.GONE
        } else {
            holder.ivPlusIconWork.setImageResource(R.drawable.add_user_button)
            holder.constarintDetails.visibility = View.GONE
            holder.constraintsUsers.setBackgroundColor(context.getColor(R.color.white))
            if (position == workspaceList.size - 1) {
                holder.viewBottomLine.visibility = View.GONE
            } else {
                holder.viewBottomLine.visibility = View.VISIBLE
            }
            holder.constarintsTopList.setBackgroundResource(0)
        }

        if (checkDeviceType()) {
            holder.viewBottomLine.visibility = View.GONE
        } else {
            if (position != workspaceList.size - 1) {
                if (workspaceList[position + 1].isCheck!!) {
                    holder.viewBottomLine.visibility = View.GONE
                } else {
                    // holder.viewBottomLine.visibility = View.VISIBLE
                }
            }
        }

        holder.ivPlusIconWork.setOnClickListener {
            workspaceList[position].isCheck = !(workspaceList[position].isCheck!!)
            notifyDataSetChanged()
        }

        holder.tvEditWork.setOnClickListener {
            clickListener.onEditClickMethod(position)
        }

        holder.tvOpenWork.setOnClickListener {
            //for open workspace
            clickListener.onOpenClickMethod(position)
        }
    }

    override fun getItemCount(): Int {
        return workspaceList.size
    }
}

interface WorkspaceClickListener {
    fun onEditClickMethod(position: Int)
    fun onOpenClickMethod(position: Int)
}