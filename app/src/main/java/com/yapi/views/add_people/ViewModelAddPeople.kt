package com.yapi.views.add_people

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard

class ViewModelAddPeople : ViewModel() {
    var addPeopleEmailScreenOpenData= MutableLiveData<String>()
    var dismissDialogData=MutableLiveData<Boolean>()
    var teamId:String?=""

    fun onClick(view: View) {
        when (view.id) {
            R.id.imgCancelAddPeople,R.id.btnBack,R.id.ivOutsideCloseAddPeople -> {
                if(checkDeviceType()){
                    dismissDialogData.value=true
                }else
                {
                view.findNavController().popBackStack()
            }
            }
            R.id.layoutSendEmailInvitationAddPeople->{
                if(checkDeviceType()){
                    addPeopleEmailScreenOpenData.value=teamId
                }else
                {
                    var bundle = Bundle()
                    bundle.putString("team_id", teamId)
                    view.findNavController().navigate(R.id.action_addPeopleFragment_to_addPeopleEmailFragment,bundle)
                }
            }
            R.id.btnNext->{
                if(checkDeviceType()){
                    addPeopleEmailScreenOpenData.value=teamId
                }else
                {
                    var bundle = Bundle()
                    bundle.putString("team_id", teamId)
                    view.findNavController().navigate(R.id.action_addPeopleFragment_to_addPeopleEmailFragment,bundle)
                }
            }
            R.id.topConstraintsAddPeople->{
                MainActivity.activity!!.get()!!.hideKeyboard()
            }
        }
    }
}