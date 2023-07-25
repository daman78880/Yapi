package com.yapi.views.menu_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R

class AdapterSettingList(val context: Context,var list:ArrayList<PojoSettingList>,val click:Click):RecyclerView.Adapter<AdapterSettingList.MyViewHolder>(){
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val selectedView:View=view.findViewById(R.id.selectedLineRvSettingList)
        val title:AppCompatTextView=view.findViewById(R.id.txtTitleSettingList)
    }
    fun getListt():ArrayList<PojoSettingList>
    {
        return list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.rv_setting_list_menu,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(list[position].selectedStatus){
            holder.selectedView.visibility=View.VISIBLE
        }else{
            holder.selectedView.visibility=View.INVISIBLE
        }
        holder.title.text=list[position].title
        holder.itemView.setOnClickListener {
            click.onSelected(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click{
        fun onSelected(position: Int)
    }
}