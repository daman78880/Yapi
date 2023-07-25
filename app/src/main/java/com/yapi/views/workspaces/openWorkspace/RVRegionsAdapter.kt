package com.yapi.views.workspaces.openWorkspace

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.views.workspaces.workspacesList.WorkSpaceData

class RVRegionsAdapter(
    private val context: Context,
    private val openWorkspaceList: ArrayList<WorkSpaceData>,private val tabType:Int
) :
    RecyclerView.Adapter<RVRegionsAdapter.MyRegionsViewModel>() {

    class MyRegionsViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivPlusIconRegion = itemView.findViewById<AppCompatImageView>(R.id.ivPlusIconRegion)
        var constarintRegionsDetails = itemView.findViewById<ConstraintLayout>(R.id.constarintRegionsDetails)
        var constraintsRegions = itemView.findViewById<ConstraintLayout>(R.id.constraintsRegions)
        var viewBottomLine = itemView.findViewById<View>(R.id.viewBottomLine)
        var constarintsTopList = itemView.findViewById<LinearLayout>(R.id.constarintsTopList)
        var constraintsBusinessUnit = itemView.findViewById<ConstraintLayout>(R.id.constraintsBusinessUnit)
        var linearFirstRowBusiness = itemView.findViewById<LinearLayout>(R.id.linearFirstRowBusiness)
        var ivPlusIconBusiness = itemView.findViewById<AppCompatImageView>(R.id.ivPlusIconBusiness)
        var constarintBusinessDetails = itemView.findViewById<ConstraintLayout>(R.id.constarintBusinessDetails)


        var constraintsJobTypes = itemView.findViewById<ConstraintLayout>(R.id.constraintsJobTypes)
        var topConstraintsJobTypes = itemView.findViewById<ConstraintLayout>(R.id.topConstraintsJobTypes)
        var ivPlusIconJobTypes = itemView.findViewById<AppCompatImageView>(R.id.ivPlusIconJobTypes)
        var constarintJobTypesDetails = itemView.findViewById<ConstraintLayout>(R.id.constarintJobTypesDetails)


        var constraintsUsers = itemView.findViewById<ConstraintLayout>(R.id.constraintsUsers)
        var ivPlusIconUsers = itemView.findViewById<AppCompatImageView>(R.id.ivPlusIconUsers)
        var constarintUsersDetails = itemView.findViewById<ConstraintLayout>(R.id.constarintUsersDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRegionsViewModel {
        var view = LayoutInflater.from(context).inflate(R.layout.rv_regions_adapter, parent, false)
        return MyRegionsViewModel(view)
    }

    override fun onBindViewHolder(holder: MyRegionsViewModel, position: Int) {

        if(tabType==1) {
            holder.constraintsRegions.visibility=View.VISIBLE
            holder.constraintsBusinessUnit.visibility=View.GONE
            holder.constraintsJobTypes.visibility=View.GONE
            holder.constraintsUsers.visibility=View.GONE

            if (openWorkspaceList[position].isCheck!!) {
                holder.constarintRegionsDetails.visibility = View.VISIBLE

                holder.ivPlusIconRegion.setImageResource(R.drawable.minus_user_list)
                holder.viewBottomLine.visibility = View.GONE
                holder.constraintsRegions.setBackgroundResource(R.drawable.user_list_box_background)
            } else {
                holder.constarintRegionsDetails.visibility = View.GONE

                holder.ivPlusIconRegion.setImageResource(R.drawable.add_user_button)
                holder.constraintsRegions.setBackgroundColor(context.getColor(R.color.white))
                if (position == openWorkspaceList.size - 1) {
                    holder.viewBottomLine.visibility = View.GONE
                } else {
                    holder.viewBottomLine.visibility = View.VISIBLE
                }
                holder.constarintsTopList.setBackgroundResource(0)
            }
            holder.ivPlusIconRegion.setOnClickListener {
                openWorkspaceList[position].isCheck = !openWorkspaceList[position].isCheck!!
                notifyDataSetChanged()
            }
        }else
            if(tabType==2)
            {
                holder.constraintsRegions.visibility=View.GONE
               holder.constraintsBusinessUnit.visibility=View.VISIBLE
               holder.constraintsJobTypes.visibility=View.GONE
               holder.constraintsUsers.visibility=View.GONE

                if(openWorkspaceList[position].isCheck!!)
                {
                    holder.constarintBusinessDetails.visibility = View.VISIBLE

                    holder.ivPlusIconBusiness.setImageResource(R.drawable.minus_user_list)
                    holder.viewBottomLine.visibility = View.GONE
                    holder.constraintsBusinessUnit.setBackgroundResource(R.drawable.user_list_box_background)
                }else
                {   holder.constarintBusinessDetails.visibility = View.GONE

                    holder.ivPlusIconBusiness.setImageResource(R.drawable.add_user_button)

                    holder.constraintsBusinessUnit.setBackgroundColor(context.getColor(R.color.white))

                    if (position == openWorkspaceList.size - 1) {
                        holder.viewBottomLine.visibility = View.GONE
                    } else {
                        holder.viewBottomLine.visibility = View.VISIBLE
                    }
                    holder.constarintsTopList.setBackgroundResource(0)
                }

                holder.ivPlusIconBusiness.setOnClickListener {
                    openWorkspaceList[position].isCheck = !openWorkspaceList[position].isCheck!!
                    notifyDataSetChanged()
                }
            }else  if(tabType==3){
                holder.constraintsRegions.visibility=View.GONE
                holder.constraintsBusinessUnit.visibility=View.GONE
                holder.constraintsJobTypes.visibility=View.VISIBLE
                holder.constraintsUsers.visibility=View.GONE

                if(openWorkspaceList[position].isCheck!!)
                {
                    holder.ivPlusIconJobTypes.setImageResource(R.drawable.minus_user_list)
                    holder.constarintJobTypesDetails.visibility = View.VISIBLE
                    holder.viewBottomLine.visibility = View.GONE
                    holder.constraintsJobTypes.setBackgroundResource(R.drawable.user_list_box_background)
                }else
                {
                    holder.ivPlusIconJobTypes.setImageResource(R.drawable.add_user_button)
                    holder.constarintJobTypesDetails.visibility = View.GONE
                    holder.constraintsJobTypes.setBackgroundColor(context.getColor(R.color.white))

                    if (position == openWorkspaceList.size - 1) {
                        holder.viewBottomLine.visibility = View.GONE
                    } else {
                        holder.viewBottomLine.visibility = View.VISIBLE
                    }
                    holder.constarintsTopList.setBackgroundResource(0)
                }


                holder.ivPlusIconJobTypes.setOnClickListener {
                    openWorkspaceList[position].isCheck = !openWorkspaceList[position].isCheck!!
                    notifyDataSetChanged()
                }
            }else
                if(tabType==4){
                    holder.constraintsRegions.visibility=View.GONE
                    holder.constraintsBusinessUnit.visibility=View.GONE
                    holder.constraintsJobTypes.visibility=View.GONE
                    holder.constraintsUsers.visibility=View.VISIBLE

                    if(openWorkspaceList[position].isCheck!!)
                    {
                        holder.ivPlusIconUsers.setImageResource(R.drawable.minus_user_list)
                        holder.constarintUsersDetails.visibility = View.VISIBLE

                        holder.viewBottomLine.visibility = View.GONE
                        holder.constraintsUsers.setBackgroundResource(R.drawable.user_list_box_background)
                    }else
                    {
                        holder.ivPlusIconUsers.setImageResource(R.drawable.add_user_button)
                        holder.constarintUsersDetails.visibility = View.GONE

                        holder.constraintsUsers.setBackgroundColor(context.getColor(R.color.white))

                        if (position == openWorkspaceList.size - 1) {
                            holder.viewBottomLine.visibility = View.GONE
                        } else {
                            holder.viewBottomLine.visibility = View.VISIBLE
                        }
                        holder.constarintsTopList.setBackgroundResource(0)
                    }


                    holder.ivPlusIconUsers.setOnClickListener {
                        openWorkspaceList[position].isCheck = !openWorkspaceList[position].isCheck!!
                        notifyDataSetChanged()
                    }
                }

        if (checkDeviceType()) {
            holder.viewBottomLine.visibility = View.GONE
        } else {
            if (position != openWorkspaceList.size - 1) {
                if (openWorkspaceList[position + 1].isCheck!!) {
                    holder.viewBottomLine.visibility = View.GONE
                } else {
                    // holder.viewBottomLine.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return openWorkspaceList.size
    }
}