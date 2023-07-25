package com.yapi.views.chat.chatUserInfo

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.R
import com.yapi.common.Constants
import com.yapi.common.MyMessageEvent
import com.yapi.common.checkDeviceType
import com.yapi.recycleradapter.RecyclerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import org.greenrobot.eventbus.EventBus

class ChatUserInfoViewModel():ViewModel() {
    val rvFilesAdapter by lazy { RecyclerAdapter<TempModel>(R.layout.rv_file_adapter_layout) }
    val rvTemplatesAdapter by lazy { RecyclerAdapter<TempModel>(R.layout.rvtemplate_layout) }

    var userType=ObservableField("")
    var userInformation=ObservableBoolean(false)

    var dismissDataObserver=MutableLiveData<Boolean>()

    fun onClick(view: View)
    {
        when(view.id)
        {
            R.id.ivBackUserInfo->{
                if(checkDeviceType()){
                    EventBus.getDefault().post(MyMessageEvent(11,Constants.USER_PROFILE_BACK)) //post event
                }else
                {
                    view.findNavController().navigateUp()
                }
            }
        }
    }


    fun setAdapterData()
    {
        var fileList=ArrayList<TempModel>()
        fileList.add(TempModel("AAA"))
        fileList.add(TempModel("AAA"))
        fileList.add(TempModel("AAA"))
        rvFilesAdapter.addItems(fileList)
        rvFilesAdapter.notifyDataSetChanged()
    }

    fun setTemplateAdapterData()
    {
        var fileList=ArrayList<TempModel>()
        fileList.add(TempModel("AAA"))
        fileList.add(TempModel("AAA"))
        fileList.add(TempModel("AAA"))
        rvTemplatesAdapter.addItems(fileList)
        rvTemplatesAdapter.notifyDataSetChanged()
    }
}