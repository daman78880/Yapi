package com.yapi.views.signup_code

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.yapi.R
import com.yapi.databinding.FragmentSignUpCodeBinding
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpCodeFragment : Fragment() {
    private lateinit var binding:FragmentSignUpCodeBinding
    val viewModel:SignupCodeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSignUpCodeBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.vModel=viewModel
        var email=arguments?.getString("email")
        viewModel.email=email
        binding.txtTempTitleDescriptionSignUpCode.setText(Html.fromHtml(requireActivity().getString(R.string.code_first_part)+"<font color=\"#3d3d3d\"><b>"+email+"</b></font>"+requireActivity().getString(R.string.code_last_part)))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        addErrorObserverMethod()
    }

    private fun addErrorObserverMethod() {
        viewModel.errorData.observe(requireActivity(),Observer{
            var data=it as SignInErrorData
            if(data!=null)
            {
             binding.txtErrorMessageTitle!!.setText(data.message)
            }else
            {
                binding.txtErrorMessageTitle!!.setText("")
            }
        })
    }

    private fun init() {
        onClick()
    }

    private fun onClick(){
       // binding.otpViewSignUpCodee as Fram
        binding.apply{
//            pin?.layoutParams?.width = resources.getDimensionPixelSize(R.dimen.three);
//            pin?.layoutParams?.height = resources.getDimensionPixelSize(R.dimen.threePlus);
          /* btnSignUpCode.setOnClickListener {
               findNavController().navigate(R.id.action_signUpCodeFragment_to_signupTeam)
           }*/
        }
    }
}