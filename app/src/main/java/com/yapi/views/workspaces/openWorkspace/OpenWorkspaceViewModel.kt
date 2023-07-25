package com.yapi.views.workspaces.openWorkspace

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.views.workspaces.workspacesList.WorkSpaceData

class OpenWorkspaceViewModel : ViewModel() {

    var showBackButton = ObservableBoolean(false)

    var regionsAvailabile = ObservableBoolean(false)
    var bussinesUnitsAvailabile = ObservableBoolean(false)
    var jobTypeAvailabile = ObservableBoolean(false)
    var usersAvailabile = ObservableBoolean(false)
var rvOpenWorkspaceList:RecyclerView?=null
    fun onClick(view: View) {
        when (view.id) {
            R.id.constarintsRegions -> {
                resetallDataTabs(1)
            }
            R.id.constarintsBusiness -> {
                resetallDataTabs(2)
            }
            R.id.constarintsJobTypes -> {
                resetallDataTabs(3)
            }

            R.id.constarintsUsers -> {
                resetallDataTabs(4)
            }
            R.id.txtOpenWorkspaceBack->{

                if(!checkDeviceType()){
                    view.findNavController().navigateUp()
                }

            }
        }
    }

    var tabClickObserver=MutableLiveData<Int>()

    fun resetallDataTabs(tabType:Int)
    {
        regionsAvailabile.set(false)
        bussinesUnitsAvailabile.set(false)
        jobTypeAvailabile.set(false)
        usersAvailabile.set(false)
        if(tabType==1)
        {
            regionsAvailabile.set(true)
            tabClickObserver.value=1
        }else
            if(tabType==2)
            {
                bussinesUnitsAvailabile.set(true)
                tabClickObserver.value=2
            }else
                if(tabType==3)
                {
                    jobTypeAvailabile.set(true)
                    tabClickObserver.value=3
                }else
                    if(tabType==4)
                    {
                        usersAvailabile.set(true)
                        tabClickObserver.value=4
                    }
      //  return tabClickObserver
    }


}