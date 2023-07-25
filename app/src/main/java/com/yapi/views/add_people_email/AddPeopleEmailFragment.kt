package com.yapi.views.add_people_email

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.yapi.R
import com.yapi.common.*
import com.yapi.databinding.FragmentAddPeopleEmailBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.add_people_email_confirmation.AddPeopleEmailConfirmationFragment
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPeopleEmailFragment : DialogFragment() {
    private lateinit var binding: FragmentAddPeopleEmailBinding
    private val viewModelAddPeopleEmail: ViewModelAddPeopleEmail by viewModels()

    @Inject
    lateinit var preferenceFile: PreferenceFile

    companion object {
        fun newInstanceAddPeopleEmail(teamId: String): AddPeopleEmailFragment {
            val args = Bundle()
            args.putString("team_id", teamId)
            val fragment = AddPeopleEmailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new dialog
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        // Get the window of the dialog
        val window: Window = dialog.getWindow()!!

        // Set the dialog to be shown at the bottom of the screen
        window.setGravity(Gravity.RIGHT)

        var second_frame_height= preferenceFile.fetchStringValue("second_frame_height")
        var second_frame_width=  preferenceFile.fetchStringValue("second_frame_width")
        Log.e("nefjkwnddfkewfwefe===",second_frame_height+"==="+second_frame_width)
        window.setLayout(second_frame_width.toInt(),second_frame_height.toInt())
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(checkDeviceType()) {
            System.out.println("phone========tablet");
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddPeopleEmailBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.model = viewModelAddPeopleEmail
        viewModelAddPeopleEmail.chipGroupAddPeopleEmail = binding.chipGroupAddPeopleEmail
        addObserverForOpenAddPeopleEmail()
        dialogDismissMethod()
        showErrorDataObserver()
        hideKeyboardDataMethod()

        setTouchListenereForNested()

        if(Constants.API_CALL_DEMO)
        {
            var teamId=requireArguments().getString("team_id")
            viewModelAddPeopleEmail.teamId=teamId
        }

        return binding.root
    }

    //For hide keyboard
    fun setTouchListenereForNested()
    {
        binding.nestedScrollViewAddPeopleEmail.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                requireActivity().hideKeyboard()
                return false
            }
        })
    }

    private fun hideKeyboardDataMethod() {
        viewModelAddPeopleEmail.hideKeyboardData.observe(requireActivity(),Observer{
            var data=it as Boolean
            if(data)
            {
                requireActivity().hideKeyboard()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setTopLayoutMethod()
        binding.apply {
            etChipAddPeopleEmail.doOnTextChanged { text, start, before, count ->
                if (text?.isNotEmpty() == true) {
                    layoutAddPeopleAddPeopleEmail.visibility = View.VISIBLE
                    txtAddPeopleAddPeopleEmail.text = text
                    txtUserNameAddPeopleEmail.text = text[0].toString()
                } else {
                    layoutAddPeopleAddPeopleEmail.visibility = View.GONE
                }
            }
            layoutAddPeopleAddPeopleEmail.setOnClickListener {
                if (etChipAddPeopleEmail.text?.isNotEmpty() == true) {
                    val msg = requireActivity().isEmailValid(etChipAddPeopleEmail.text.toString())
                    if (msg.isEmpty()) {
                        var alreadyExistEmail=false
                        if(chipGroupAddPeopleEmail?.childCount!!>0) {
                            for (i in 0 until chipGroupAddPeopleEmail?.childCount!!) {
                                val chipView = chipGroupAddPeopleEmail?.getChildAt(i) as Chip
                                val title = chipView.text.toString()
                                if (title.equals(etChipAddPeopleEmail.text.toString())) {
                                    alreadyExistEmail = true
                                    break
                                }
                            }
                        }
                        if(!alreadyExistEmail) {
                            viewModelAddPeopleEmail.checkEmailAPIMethod(etChipAddPeopleEmail.text.toString())
                                .observe(requireActivity(),
                                    Observer {
                                        var data = it as CheckEmailResponse
                                        if (data != null) {
                                            if (data.status == 200) {
                                                Log.e("dataaaaaa===", data.toString())
                                                binding.txtErrorEmailAddPeople.setText("")
                                                addChipToGroup(requireContext(),
                                                    etChipAddPeopleEmail.text.toString())
                                                layoutAddPeopleAddPeopleEmail.visibility = View.GONE
                                                etChipAddPeopleEmail.text?.clear()
                                                viewModelAddPeopleEmail.errorData.value =
                                                    SignInErrorData("", 0)
                                            } else {
                                                var data= SignInErrorData(requireActivity().resources.getString(R.string.email_doesnot_exits), 1)
                                                emailErrorMethod(data)
                                            }
                                        }
                                    })
                        }else{
                            var data = SignInErrorData(requireActivity().resources.getString(R.string.email_already_enter), 1)
                            emailErrorMethod(data)
                        }
                    } else {
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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

        binding.chipGroupAddPeopleEmail.addView(chip as View)
        chip.setOnCloseIconClickListener {
            binding.chipGroupAddPeopleEmail.removeView(chip as View)
        }
    }

    private fun addObserverForOpenAddPeopleEmail() {
        viewModelAddPeopleEmail.addPeopleEmailConfirmationOpenData.observe(requireActivity(), Observer {
            var data =it as EmailData
           // if(data!=null){
                AddPeopleEmailConfirmationFragment.newInstanceEmailConfirmation(data).showNow(requireActivity().supportFragmentManager," SimpleDialog.TAG")
           // }
        })
    }

    fun dialogDismissMethod()
    {
        viewModelAddPeopleEmail.dismissDialogData.observe(requireActivity(), Observer {
            var data=it as Boolean
            if(data)
            {
                dismiss()
            }
        })
    }


    fun setTopLayoutMethod()
    {
        var rightMarginTopLayout=0
        if(checkDeviceType())
        {
            rightMarginTopLayout=requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._18sdp).toInt()
            binding.ivOutsideCloseAddPeopleEmail.visibility=View.VISIBLE
            binding.imgCancelAddPeopleEmail.visibility=View.GONE
            binding.layoutAddPeopleEmail.setBackgroundResource(R.drawable.et_drawable)
        }else
        {
            binding.layoutAddPeopleEmail.setBackgroundResource(0)
            rightMarginTopLayout=0
            binding.ivOutsideCloseAddPeopleEmail.visibility=View.GONE
            binding.imgCancelAddPeopleEmail.visibility=View.VISIBLE
        }
        val layoutParams = binding.layoutAddPeopleEmail.layoutParams as LinearLayout.LayoutParams
        //  val newLayoutParams = toolbar.getLayoutParams()
        layoutParams.topMargin = 0
        layoutParams.leftMargin = 0
        layoutParams.rightMargin = rightMarginTopLayout
        binding.layoutAddPeopleEmail.setLayoutParams(layoutParams)
    }

    fun showErrorDataObserver()
    {
        viewModelAddPeopleEmail.errorData.observe(requireActivity(), Observer {
            var data=it as SignInErrorData
            emailErrorMethod(data)
        })
    }

    fun emailErrorMethod(data: SignInErrorData)
    {
        if(data!=null && data.message!="")
        {
            binding.txtErrorEmailAddPeople.setText(data.message)
            changeBackgroundTintForError(binding.chipLayoutAddPeopleEmail!!,requireActivity().resources.getColor(
                R.color.error_box_color),
                requireActivity().resources.getColor(R.color.error_border_color),-1)
        }else
        {
            binding.txtErrorEmailAddPeople.setText("")
            //   binding.chipLayoutAddPeopleEmail!!.setbackg
            changeBackgroundTintForError(binding.chipLayoutAddPeopleEmail!!, requireActivity().resources.getColor(
                R.color.white),
                requireActivity().resources.getColor(R.color.liteGrey),requireActivity().resources.getColor(R.color.information_profile_back_box))
        }
    }
}