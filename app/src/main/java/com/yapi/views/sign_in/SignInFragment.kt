package com.yapi.views.sign_in

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yapi.R
import com.yapi.common.changeBackgroundForError
import com.yapi.databinding.FragmentSignInBinding
import com.yapi.databinding.FragmentSignUpBinding
import com.yapi.views.add_people_email.CheckEmailResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding:FragmentSignInBinding
     val viewModel:SignInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSignInBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.vModel=viewModel
        showErrorUIObserver()
        checkEmailErrorObserver()
        return binding.root
    }

    //For check Email Observer
    private fun checkEmailErrorObserver() {
        viewModel.checkEmailResponseData.observe(requireActivity(), Observer {
            var data=it as CheckEmailResponse
            if(data.status==400)
            {
                showErrorMethod(data.message)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.apply {

        }
        onClick()
    }
    private fun onClick(){
     /*   binding.apply {
            btnSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment2)
            }
        }*/
    }

    fun showErrorUIObserver()
    {
        viewModel.errorData.observe(requireActivity(), Observer {
            val data=it as SignInErrorData
            showErrorMethod(data.message)
        })
    }

    fun showErrorMethod(message:String)
    {
        if(message!=null && message.isNotEmpty())
        {
            binding.txtErrorEmailSignIn.setText(message)
            changeBackgroundForError(binding.layoutEmailSignIn,requireActivity().resources.getColor(R.color.error_box_color),
                requireActivity().resources.getColor(R.color.error_border_color))
        }else
        {
            binding.txtErrorEmailSignIn.setText("")
            changeBackgroundForError(binding.layoutEmailSignIn,requireActivity().resources.getColor(R.color.white),
                requireActivity().resources.getColor(R.color.liteGrey))
        }
    }

/*   fun changeBackgroundForError(layoutEmailSignIn:ConstraintLayout,boxColor:Int,borderColor:Int)
    {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        var finalWidth2 = requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._1sdp)
        drawable.setStroke(finalWidth2.toInt(), borderColor)
        drawable.cornerRadius = requireActivity().resources.getDimension(R.dimen.roundDrawableCommon)
        drawable.setColor(boxColor)
        layoutEmailSignIn!!.setBackgroundDrawable(drawable)
    }*/
}