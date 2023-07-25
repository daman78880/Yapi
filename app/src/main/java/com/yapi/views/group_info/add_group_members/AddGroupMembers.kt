package com.yapi.views.group_info.add_group_members

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yapi.R
import com.yapi.databinding.FragmentAddGroupMembersBinding

class AddGroupMembers : Fragment() {
    private lateinit var adapterPagination: AdapterNumberPaginationGroupMember
    private lateinit var binding:FragmentAddGroupMembersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentAddGroupMembersBinding.inflate(LayoutInflater.from(requireContext()))
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvGroupMembers.setNestedScrollingEnabled(true);

        val list=ArrayList<PojoGroupMembers>()
        for(i in 0 until 20){
            list.add(PojoGroupMembers("Name${i+1}","Sales ${i+4}"))
        }
        binding.apply {
            rvGroupMembers.adapter=AdapterGroupMembers(requireContext(),list)
            var paginationList= ArrayList<Int>()
            for(i in 0 until list.size){
                paginationList.add(i+1)
            }
            adapterPagination=AdapterNumberPaginationGroupMember(requireContext(),paginationList,0,object :AdapterNumberPaginationGroupMember.Click{
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(selectedPosition: Int) {
                    Log.i("asfjkasnf","onClick selectedPosition is $selectedPosition")
                    adapterPagination.selectedPage=selectedPosition
                    adapterPagination.temp=1
                    adapterPagination.visibleCount=0
                    adapterPagination.notifyDataSetChanged()
                }
            })
            rvNumberPaginationGroupMembers.adapter=adapterPagination
        }
    }
}