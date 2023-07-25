package com.yapi.views.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.yapi.R
import com.yapi.common.changeBackgroundForError
import com.yapi.databinding.FragmentSignUpBinding
import com.yapi.views.add_people_email.CheckEmailResponse
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding:FragmentSignUpBinding
    val vModel:SignupViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSignUpBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.vModel=vModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        showErrorUIObserver()
        checkEmailErrorObserver()
    }

    private fun init() {
        binding.apply {

        }
        onClick()
    }
    private fun onClick(){
        binding.apply {

        }
    }

    fun showErrorUIObserver()
    {
        vModel.errorData.observe(requireActivity(), Observer {
            var data=it as SignInErrorData
            showErrorMethod(data.message)
        })
    }

    //For check Email Observer
    private fun checkEmailErrorObserver() {
        vModel.checkEmailResponseData.observe(requireActivity(), Observer {
            var data=it as CheckEmailResponse
            if(data.status==200)
            {
                showErrorMethod(data.message)
            }
        })
    }

    fun showErrorMethod(message:String)
    {
        if(message!=null && message.isNotEmpty())
        {
            binding.txtErrorEmailSignup!!.setText(message)
            changeBackgroundForError(binding.layoutEmailSignUp,requireActivity().resources.getColor(R.color.error_box_color),
                requireActivity().resources.getColor(R.color.error_border_color))
        }else
        {
            binding.txtErrorEmailSignup!!.setText("")
            changeBackgroundForError(binding.layoutEmailSignUp,requireActivity().resources.getColor(R.color.white),
                requireActivity().resources.getColor(R.color.liteGrey))
        }
    }
}