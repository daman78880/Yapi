package com.yapi.views.chat.chatGroupInfo

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yapi.common.Constants
import com.yapi.databinding.ChatGroupInfoFragmentBinding
import com.yapi.views.chat.chatUserInfo.RVLinksAdapter
import com.yapi.views.chat.chatUserInfo.RVPhotoMediaAdapter
import com.yapi.views.menu_screen.GroupData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatGroupInfoFragment():Fragment() {
    private lateinit var rvLinkAdapter: RVLinksAdapter
    private lateinit var mediaAdapter: RVPhotoMediaAdapter
    private lateinit var dataBinding: ChatGroupInfoFragmentBinding
    private var screenWidth: Int?=0
    val viewModel: ChatGroupInfoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = ChatGroupInfoFragmentBinding.inflate(inflater)
        dataBinding.vModel=viewModel
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        viewModel.screenWidth=screenWidth
        viewModel.screenHeight=displayMetrics.heightPixels

      //  bundle.putSerializable("all_data_information", allGroupData)
        if(Constants.API_CALL_DEMO) {
            var userType = requireArguments().getString("userType")

            if (userType == Constants.GROUPS_KEY) {
                var team_id = requireArguments().getString("team_id")
                viewModel.fetchGroupOrTeamData(team_id!!)
            }
        }else
        {
            viewModel.screenDataVisibility.set(true)
        }
     /*   if(userType==Constants.GROUPS_KEY) {
            var groupData = requireArguments().getSerializable("all_data_information") as GroupData
            viewModel.groupPhoto=groupData.image_url
            if(groupData.image_url!!.isNotEmpty())
            {
                viewModel.groupPhotoVisibility=true
            }else
            {
                viewModel.groupPhotoVisibility=true
            }
        }*/

        initUI()
        return dataBinding.root
    }

    private fun initUI() {
        setPhotoAdapterMethod()
        setLinkAdapterMethod()
        viewModel.setAdapterData()
        viewModel.setTemplateAdapterData()
    }

    fun setPhotoAdapterMethod()
    {
        var finalPerPhoto=screenWidth!!.toFloat()/3.58f
        var photoList=ArrayList<String>()
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        photoList.add("AA")
        mediaAdapter= RVPhotoMediaAdapter(requireActivity(),photoList,finalPerPhoto!!.toInt(),6)
        dataBinding.rvMediaView.layoutManager= GridLayoutManager(requireActivity(),3)
        dataBinding.rvMediaView.adapter=mediaAdapter
    }

    fun setLinkAdapterMethod()
    {
        rvLinkAdapter= RVLinksAdapter(requireActivity())
        dataBinding.rvLinksView.layoutManager= LinearLayoutManager(requireActivity())
        dataBinding.rvLinksView.adapter=rvLinkAdapter
    }
}