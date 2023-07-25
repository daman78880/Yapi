package com.yapi.views.group_info.group_files

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yapi.R
import com.yapi.databinding.FragmentGroupFilesBinding

class GroupFilesFragment : Fragment() {
    private lateinit var binding:FragmentGroupFilesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentGroupFilesBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val list=ArrayList<PojoGroupFiles>()
        //for(i in 1 until 10){
        for(i in 1..10){
            list.add(PojoGroupFiles("Fileeehdfhhfe$i",(i+5).toString(),"Sared by $i"))
        }
        binding.apply {
            rvFileGroupFiles.adapter=AdapterGroupFiles(requireContext(),list)
        }
    }
}