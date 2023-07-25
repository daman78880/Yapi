package com.yapi.views.group_info

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yapi.views.group_info.add_group_members.AddGroupMembers
import com.yapi.views.group_info.group_files.GroupFilesFragment
import com.yapi.views.group_info.group_general.GroupGeneral
import com.yapi.views.menu_screen.MenuFragment

class AdapterTabLayoutGroupInfo(val fm: FragmentActivity, val totalCount: Int) :
    FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0->{
                GroupGeneral()
            }
            1 -> {
                AddGroupMembers()
            }
            2 ->{
                GroupFilesFragment()
            }
            else -> {
                GroupGeneral()
            }
        }
    }
}