package com.yapi.views.group_info

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.R

class ViewModelGroupInfo:ViewModel() {
    fun onClick(view:View){
        when(view.id){
            R.id.imgCancelGroupInfo->{
               // Toast.makeText(view.context, "Clicked", Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack()
            }
        }
    }
}