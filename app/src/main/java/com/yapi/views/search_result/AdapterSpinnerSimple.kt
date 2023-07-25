package com.yapi.views.search_result

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import com.yapi.R

class AdapterSpinnerSimple(val context:Context,val list:ArrayList<String>): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.rv_spinner_simple, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = list[position]
        return view
    }
    private class ItemHolder(row: View?) {
        val label: AppCompatTextView
        val view: View
        init {
            label = row?.findViewById(R.id.txtSpinnerSimple) as AppCompatTextView
            view = row.findViewById(R.id.viewSpinnerSimple) as View
        }
    }
}