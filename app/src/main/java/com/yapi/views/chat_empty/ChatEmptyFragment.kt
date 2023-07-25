package com.yapi.views.chat_empty

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.yapi.R
import com.yapi.common.Constants
import com.yapi.common.GroupEvent
import com.yapi.databinding.FragmentChatEmptyBinding
import com.yapi.views.create_group.CreateGroupFragment
import com.yapi.views.menu_screen.MenuFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChatEmptyFragment : Fragment() {
    private lateinit var binding: FragmentChatEmptyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatEmptyBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        if (requireActivity().resources.getBoolean(R.bool.isTab)) {
            System.out.println("phone========tablet")
            binding.layoutToolBarChatEmpty.visibility = View.GONE

        } else {
            binding.layoutToolBarChatEmpty.visibility = View.VISIBLE
        }



        binding.apply {
            imgTempDrawableChatEmpty.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.chatEmptyFragment) {
                    findNavController().navigate(R.id.action_chatEmptyFragment_to_menuFragment)
                }
            }
            imgDemoIconEmptyChat.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.chatEmptyFragment) {

                    findNavController().navigate(R.id.action_chatEmptyFragment_to_menuFragment)
                }
            }
            txtTempChatEmpty.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.chatEmptyFragment) {

                    findNavController().navigate(R.id.action_chatEmptyFragment_to_menuFragment)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: GroupEvent?) {
        // Do something
        Log.e("gsegegsgsgs===",System.currentTimeMillis().toString())

        if(event!!.screenName== Constants.CREATEGOUP_KEY)
        {
            CreateGroupFragment.newInstanceCreateGroup("").showNow(requireActivity().supportFragmentManager," SimpleDialog.TAG")
        }
    }
}