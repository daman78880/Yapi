package com.yapi.views.leaveGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yapi.databinding.DeleteGroupPopupBinding
import com.yapi.databinding.LeaveModulePopupBinding

class LeaveGroupFragment:Fragment() {

    private lateinit var dataBinding: LeaveModulePopupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding= LeaveModulePopupBinding.inflate(LayoutInflater.from(requireActivity()))
        initUI()
        return dataBinding.root
    }

    private fun initUI() {

    }
}