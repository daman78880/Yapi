package com.yapi.views.workspaces.openWorkspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yapi.MainActivity
import com.yapi.common.checkDeviceType
import com.yapi.databinding.OpenWorkspaceLayoutBinding
import com.yapi.views.workspaces.workspacesList.WorkSpaceData

class OpenWorkspaceFragment : DialogFragment() {

    private var rvRegionsAdapter: RVRegionsAdapter?=null
    private var dataBinding: OpenWorkspaceLayoutBinding? = null

    val viewModel: OpenWorkspaceViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dataBinding = OpenWorkspaceLayoutBinding.inflate(inflater)
        dataBinding!!.vModel = viewModel
        viewModel.regionsAvailabile.set(true)
        viewModel.rvOpenWorkspaceList=dataBinding!!.rvOpenWorkspaceList
        initUI()
        setRegionAdapter(1)
        return dataBinding!!.root
    }

    //For UI Initialization
    private fun initUI() {
        if (checkDeviceType()) {
            viewModel.showBackButton.set(false)
        } else {
            viewModel.showBackButton.set(true)
        }
      //  setRegionAdapter()

        viewModel.tabClickObserver.observe(requireActivity(), Observer {
            var tabType=it as Int
            if(tabType>0) {
                setRegionAdapter(tabType)
            }
            })
    }

    fun setRegionAdapter(tabType:Int)
    {
        var openWorkspaceList=ArrayList<WorkSpaceData>()
        openWorkspaceList.clear()
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))
        openWorkspaceList.add(WorkSpaceData("AA",false))

      //  var tabType=2
        rvRegionsAdapter=RVRegionsAdapter(requireActivity(),openWorkspaceList,tabType)
        dataBinding!!.rvOpenWorkspaceList!!.layoutManager=
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        dataBinding!!.rvOpenWorkspaceList!!.adapter=rvRegionsAdapter
    }
}