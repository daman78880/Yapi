package com.yapi.views.workspaces.workspacesList

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.lifecycle.HiltViewModel

class WorkspaceViewModel():ViewModel() {

    var screenWidth:Int?=0
    var checkBoxValue=ObservableBoolean(false)
    var workspaceName=ObservableField("")
    var databaseName=ObservableField("")
    var tokenName=ObservableField("")
    var secretValue=ObservableField("")
    var checkValidationData=MutableLiveData<SignInErrorData>()
    var hideEditWorkspaceDialog=MutableLiveData<Boolean>()
    var showAddWorkspaceDialog=MutableLiveData<Boolean>()

    var showBackButton=ObservableBoolean(false)
    fun onClick(view:View)
    {
        when(view.id)
        {
            R.id.txtWorkspaceBack->{
            if(!checkDeviceType())
            {
                view.findNavController().navigateUp()
            }
            }
            R.id.btnSaveWorkspace->{
                //for save Workspace
                if(checkValidation())
                {
                    hideEditWorkspaceDialog.value=true
                }
            }
            R.id.linearAddWorkspaces->{
                showAddWorkspaceDialog.value=true
            }
        }
    }

    fun checkValidation():Boolean
    {
        if(workspaceName.get().toString().trim().length==0)
        {
            checkValidationData.value= SignInErrorData(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_workspace_name_text),1)
            return false
        }else
            if(databaseName.get().toString().trim().length==0)
            {
                checkValidationData.value= SignInErrorData(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_database_name_text),2)
                return false
            }else
                if(tokenName.get().toString().trim().length==0)
                {
                    checkValidationData.value= SignInErrorData(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_token_text),3)
                    return false
                }
                else
                    if(secretValue.get().toString().trim().length==0)
                    {
                        checkValidationData.value= SignInErrorData(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_secret_text),4)
                        return false
                    }else
                    {
                        checkValidationData.value= SignInErrorData("",-1)

                    }
        return true
    }
}