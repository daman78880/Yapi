package com.yapi.views.group_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yapi.R
import com.yapi.databinding.FragmentGroupInfoBinding


class GroupInfoFragment : Fragment() {
    private lateinit var binding: FragmentGroupInfoBinding
    private val vModel:ViewModelGroupInfo by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGroupInfoBinding.inflate(LayoutInflater.from(requireContext()))
        binding.model=vModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.apply {
            val adapter = AdapterTabLayoutGroupInfo(requireActivity(), 3)
            viewPagerGroupInfo.adapter = adapter
            TabLayoutMediator(binding.tabLayoutGroupInfo, binding.viewPagerGroupInfo) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "General"
                    }
                    1 -> {
                        tab.text = "Members"
                    }
                    2 -> {
                        tab.text ="Files"
                    }
                }
            }.attach()
        }

    }

}