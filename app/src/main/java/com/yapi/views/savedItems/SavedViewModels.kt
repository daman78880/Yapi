package com.yapi.views.savedItems

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.R
import com.yapi.common.checkDeviceType

class SavedViewModels : ViewModel() {

    var backArrowVisible = ObservableBoolean(false)
var screenWidth:Int?=0
var screenHeight:Int?=0
    fun onClick(view: View) {
        when (view.id) {
            R.id.ivSavedItemsBack -> {
                if (!checkDeviceType()) {
                    view.findNavController().navigateUp()
                }
            }
        }
    }
}