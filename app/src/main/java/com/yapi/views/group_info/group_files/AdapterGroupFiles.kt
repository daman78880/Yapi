package com.yapi.views.group_info.group_files

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.yapi.R

class AdapterGroupFiles(val context: Context, val list:ArrayList<PojoGroupFiles>):RecyclerView.Adapter<AdapterGroupFiles.MyViewHolder>() {
class MyViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView){
    val fileName=itemView.findViewById<AppCompatTextView>(R.id.txtFileNameRvGroupFiles)
    val fileSize=itemView.findViewById<AppCompatTextView>(R.id.txtFileSizeRvGroupFiles)
    val fileSharedBy=itemView.findViewById<AppCompatTextView>(R.id.txtFileInfoRvGroupFiles)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.rv_group_files_list,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.fileSharedBy.text=list[position].fileSharedBy
        holder.fileName.text= Html.fromHtml(list[position].fileName+"  <font color='#b8b8b8'>"+list[position].fileSize+"Mb</font>")
        holder.fileName.requestLayout()


       // holder.fileName.viewTreeObserver.addOnGlobalLayoutListener {

            Log.e("ghdghsgdesdsdsd===",holder.fileName.lineCount.toString())
            if(holder.fileName.getLineCount()>1)
            {
                holder.fileName.text=""
                holder.fileName.text= list[position].fileName
                holder.fileSize.visibility=View.VISIBLE
                holder.fileSize.text=list[position].fileSize+"Mb"
            }else
            {
                //holder.fileName.text=""
                holder.fileSize.visibility=View.GONE
                //holder.fileName.text= Html.fromHtml(list[position].fileName+"  <font color='#b8b8b8'>"+list[position].fileSize+"Mb</font>")
            }
      //  }


      /*  textview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                textView.getLayout().getLineCount()
            }
        });
*/





    }

    override fun getItemCount(): Int {
        return list.size
    }
}