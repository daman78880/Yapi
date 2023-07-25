package com.yapi.views.create_team.third_step_create_team

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.yapi.R
import com.yapi.common.*
import com.yapi.databinding.SecondStepCreateTeamBinding
import com.yapi.databinding.ThirdStepCreateLayoutBinding
import com.yapi.views.add_people_email.CheckEmailResponse
import com.yapi.views.create_team.second_step_create_team.SecondStepViewModel
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdStepCreateFragment : Fragment() {
    private lateinit var dataBinding: ThirdStepCreateLayoutBinding
    private val viewModel: ThirdStepViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dataBinding = ThirdStepCreateLayoutBinding.inflate(LayoutInflater.from(requireActivity()))
        dataBinding.vModel=viewModel
        viewModel.chipGroupAddPeopleEmail = dataBinding.chipGroupAddPeopleEmail
        initUI()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        viewModel.screenWidth=width

        if(Constants.API_CALL_DEMO) {
            var team_id = requireArguments().getString("team_id")
            viewModel.teamId = team_id
        }
        return dataBinding.root
    }

    private fun initUI() {
        showErrorUIObserver()

        dataBinding.apply {
            etMemberEmail!!.doOnTextChanged { text, start, before, count ->
                if (text?.isNotEmpty() == true) {
                    layoutAddPeopleAddPeopleEmail!!.visibility = View.VISIBLE
                    txtAddPeopleAddPeopleEmail!!.text = text
                    txtUserNameAddPeopleEmail!!.text = text[0].toString()
                } else {
                    layoutAddPeopleAddPeopleEmail!!.visibility = View.GONE
                }
            }
            layoutAddPeopleAddPeopleEmail!!.setOnClickListener {
                if (etMemberEmail!!.text?.isNotEmpty() == true) {
                    val msg = requireActivity().isEmailValid(etMemberEmail.text.toString())
                    if (msg.isEmpty()) {

                        var alreadyExistEmail=false
                        if(chipGroupAddPeopleEmail?.childCount!!>0) {
                            for (i in 0 until chipGroupAddPeopleEmail?.childCount!!) {
                                val chipView = chipGroupAddPeopleEmail?.getChildAt(i) as Chip
                                val title = chipView.text.toString()
                                if (title.equals(etMemberEmail.text.toString())) {
                                    alreadyExistEmail = true
                                    break
                                }
                            }
                        }

                        if(!alreadyExistEmail) {
                        viewModel.checkEmailAPIMethod(etMemberEmail.text.toString()).observe(requireActivity(),
                            Observer {
                                var data=it as CheckEmailResponse
                                if(data!=null)
                                {
                                    if(data.status==200) {
                                        dataBinding.txtErrorEmailSignup.setText("")
                                        addChipToGroup(requireContext(), etMemberEmail.text.toString())
                                        layoutAddPeopleAddPeopleEmail!!.visibility = View.GONE
                                        etMemberEmail!!.text?.clear()

                                        var data= SignInErrorData("", 0)
                                        emailErrorMethod(data)
                                    }else
                                    {
                                        var data= SignInErrorData(requireActivity().resources.getString(R.string.email_doesnot_exits), 1)
                                        emailErrorMethod(data)
                                    }
                                    }
                            })
                    }else
                        {
                            var data = SignInErrorData(requireActivity().resources.getString(R.string.email_already_enter), 1)
                            emailErrorMethod(data)
                        }


                       // viewModelAddPeopleEmail.errorData.value = SignInErrorData("", 0)
                    } else {
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun emailErrorMethod(data:SignInErrorData)
    {
        if(data!=null && data.message.isNotEmpty())
        {
            dataBinding.txtErrorEmailSignup!!.setText(data.message)
            changeBackgroundForEditError(dataBinding.etMemberEmail!!,requireActivity().resources.getColor(
                R.color.error_box_color),
                requireActivity().resources.getColor(R.color.error_border_color))
        }else
        {
            dataBinding.txtErrorEmailSignup!!.setText("")
            changeBackgroundForEditError(dataBinding.etMemberEmail!!,requireActivity().resources.getColor(
                R.color.white),
                requireActivity().resources.getColor(R.color.liteGrey))
        }
    }

    private fun showErrorUIObserver()
    {
        viewModel.errorData.observe(requireActivity(), Observer {
            var data=it as SignInErrorData
            emailErrorMethod(data)
        })
    }




    fun addChipToGroup(context: Context, person: String) {
        val chip = Chip(context)
        chip.text = person
        chip.setTextColor(ContextCompat.getColor(context, R.color.blueColor))
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.liteBlueForDrawable))
        chip.chipCornerRadius = context.resources.getDimension(com.intuit.sdp.R.dimen._5sdp)
        chip.isCloseIconVisible = true
        chip.closeIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_cross_icon)
        chip.closeIconSize=context.resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
        var paddingValue= context.resources.getDimension(com.intuit.sdp.R.dimen._5sdp).toInt()
        chip.isCheckable = false
        chip.closeIconTint =
            ColorStateList.valueOf(ContextCompat.getColor(context, com.yapi.R.color.darkLiteGrey))
        //.setTextSize(13f)
        chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected);
        chip.setPadding(paddingValue,paddingValue,paddingValue,paddingValue)

        dataBinding.chipGroupAddPeopleEmail!!.addView(chip as View)
        chip.setOnCloseIconClickListener {
            dataBinding.chipGroupAddPeopleEmail!!.removeView(chip as View)
        }
    }

   /* fun setTopLayoutMethod()
    {
        var rightMarginTopLayout=0
        if(checkDeviceType())
        {
            rightMarginTopLayout=requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._18sdp).toInt()
            dataBinding.ivOutsideCloseAddPeopleEmail.visibility=View.VISIBLE
            dataBinding.imgCancelAddPeopleEmail.visibility=View.GONE
            dataBinding.layoutAddPeopleEmail.setBackgroundResource(R.drawable.et_drawable)
        }else
        {
            dataBinding.layoutAddPeopleEmail.setBackgroundResource(0)
            rightMarginTopLayout=0
            dataBinding.ivOutsideCloseAddPeopleEmail.visibility=View.GONE
            dataBinding.imgCancelAddPeopleEmail.visibility=View.VISIBLE
        }
        val layoutParams = dataBinding.layoutAddPeopleEmail.layoutParams as LinearLayout.LayoutParams
        //  val newLayoutParams = toolbar.getLayoutParams()
        layoutParams.topMargin = 0
        layoutParams.leftMargin = 0
        layoutParams.rightMargin = rightMarginTopLayout
        dataBinding.layoutAddPeopleEmail.setLayoutParams(layoutParams)
    }*/
}


