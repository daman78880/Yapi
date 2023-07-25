package com.yapi.views.search

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard

class SearchViewModel():ViewModel() {

var dissmissDialogPopupData=MutableLiveData<Boolean>()
    var cancelshowField=ObservableBoolean(false)
    fun onClick(view:View){
        when(view.id)
        {
           R.id.layoutSearch, R.id.layoutCreateSearch->{
                MainActivity.activity?.get()?.hideKeyboard()
            }
            R.id.imgCancelSearch,R.id.ivOutsideCloseSearch->{
                if(checkDeviceType()){
                    dissmissDialogPopupData.value=true
                }else
                {
                    view.findNavController().navigateUp()
                }
            }
        }
    }
}