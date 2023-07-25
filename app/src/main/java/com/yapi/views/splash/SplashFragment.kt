package com.yapi.views.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yapi.R
import com.yapi.common.Constants
import com.yapi.common.MyMessageEvent
import com.yapi.common.checkDeviceType
import com.yapi.databinding.SplashLayoutBinding
import com.yapi.pref.PreferenceFile
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment  : Fragment() {

    private lateinit var binding: SplashLayoutBinding

    @Inject
    lateinit var preferenceFile: PreferenceFile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = SplashLayoutBinding.inflate(inflater)
        var userId = preferenceFile!!.fetchStringValue(Constants.LOGIN_USER_ID)
       var USER_PROFILE_CREATED= preferenceFile.fetchBooleanValue(Constants.USER_PROFILE_CREATED)
        Handler(Looper.myLooper()!!).postDelayed(object : Runnable {
            override fun run() {
                if (userId == "") {
                    findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                } else {
                    if(USER_PROFILE_CREATED)
                    {
                        if(checkDeviceType()){
                            EventBus.getDefault().post(MyMessageEvent(1, Constants.MENU_KEY)) //post event
                        }else{
                            findNavController().navigate(R.id.action_splashFragment_to_menuFragment)
                        }
                    }else
                    {
                        findNavController().navigate(R.id.action_splashFragment_to_signupTeam)
                    }
                }
            }
        }, 2000)
        return binding.root
    }
}