package com.yapi.views.chat.chatUserInfo

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
import com.yapi.databinding.ChatUserInfoFragmentBinding

class ChatUserInfoFragment : Fragment() {
    private lateinit var rvLinkAdapter: RVLinksAdapter
    private var screenWidth: Int? = 0
    private lateinit var mediaAdapter: RVPhotoMediaAdapter
    private lateinit var dataBinding: ChatUserInfoFragmentBinding
    val viewModel: ChatUserInfoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dataBinding = ChatUserInfoFragmentBinding.inflate(inflater)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

            dataBinding.vModel = viewModel

        viewModel.userType.set(requireArguments().getString("userType"))

        if (viewModel.userType.get().equals(Constants.CUSTOMERS_KEY)) {
            viewModel.userInformation.set(true)
        } else {
            viewModel.userInformation.set(false)
        }

        initUI()
        return dataBinding.root
    }


    private fun initUI() {
        setPhotoAdapterMethod()
        setLinkAdapterMethod()
        viewModel.setAdapterData()
        viewModel.setTemplateAdapterData()
    }

    fun setPhotoAdapterMethod() {
//        var finalPerPhoto=screenWidth!!.toFloat()/3.61f
//        var finalPerPhoto=screenWidth!!.toFloat()/3.51f
        val finalPerPhoto = screenWidth!!.toFloat() / 3.58f
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
        mediaAdapter = RVPhotoMediaAdapter(requireActivity(),photoList, finalPerPhoto.toInt(),6)
        dataBinding.rvMediaView.layoutManager = GridLayoutManager(requireActivity(), 3)
        dataBinding.rvMediaView.adapter = mediaAdapter
    }

    fun setLinkAdapterMethod() {
        rvLinkAdapter = RVLinksAdapter(requireActivity())
        dataBinding.rvLinksView.layoutManager = LinearLayoutManager(requireActivity())
        dataBinding.rvLinksView.adapter = rvLinkAdapter
    }
}