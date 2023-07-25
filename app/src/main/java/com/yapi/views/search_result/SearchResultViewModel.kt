package com.yapi.views.search_result

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard

class SearchResultViewModel():ViewModel() {

    var dissmissDialogPopupData= MutableLiveData<Boolean>()
    var cancelshowField= ObservableBoolean(false)

    fun onClick(view: View)
    {
        when(view.id)
        {
            R.id.layoutCreateSearch->{
                MainActivity.activity?.get()?.hideKeyboard()
            }
            R.id.ivOutsideCloseSearchResult,R.id.imgCancelSearch->{
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