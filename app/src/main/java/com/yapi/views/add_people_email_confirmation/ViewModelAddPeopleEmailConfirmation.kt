package com.yapi.views.add_people_email_confirmation

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.R
import com.yapi.common.checkDeviceType

class ViewModelAddPeopleEmailConfirmation:ViewModel(){
    var invitedPersonCount=ObservableField("")
    var dismissDialogData=MutableLiveData<Boolean>()
    fun onClick(view:View){
        when(view.id){
            R.id.imgCancelAddPeopleEmailConf,R.id.ivOutsideCloseAddEmailConfirmation->{
                if(checkDeviceType()){
                    dismissDialogData.value=true
                }else
                {
                    if (view.findNavController().currentDestination?.id == R.id.addPeopleEmailConfirmationFragment) {
                        view.findNavController().popBackStack()
                    }
                }
            }
            R.id.btnDoneGroupAddPeopleEmailConfirm->{
                if(checkDeviceType()){
                    dismissDialogData.value=true
                }else
                {
                    if (view.findNavController().currentDestination?.id == R.id.addPeopleEmailConfirmationFragment) {
                        view.findNavController().popBackStack()
                    }
                }
            }
        }
    }
}