package com.yapi.views.deleteGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yapi.databinding.DeleteGroupPopupBinding

class DeleteGroupFragment:Fragment() {

    private lateinit var dataBinding: DeleteGroupPopupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding=DeleteGroupPopupBinding.inflate(LayoutInflater.from(requireActivity()))
        initUI()
        return dataBinding.root
    }

    private fun initUI() {

    }
}