package com.yapi.views.add_people

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.yapi.R
import com.yapi.common.Constants
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard
import com.yapi.databinding.FragmentAddPeopleBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.add_people_email.AddPeopleEmailFragment
import com.yapi.views.create_group.CreateGroupFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPeopleFragment : DialogFragment() {
    private lateinit var binding: FragmentAddPeopleBinding
    private val viewModel: ViewModelAddPeople by viewModels()

    @Inject
    lateinit var preferenceFile: PreferenceFile

    companion object {
        fun newInstanceAddPeople(teamId: String): AddPeopleFragment {
            val args = Bundle()
            args.putString("team_id", teamId)
            val fragment = AddPeopleFragment()
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
        if(requireActivity().getResources().getBoolean(R.bool.isTab)) {
            System.out.println("phone========tablet");
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddPeopleBinding.inflate(LayoutInflater.from(requireActivity()))
            binding.vModel = viewModel
        addObserverForOpenAddPeople()
        dialogDismissMethod()
        setTouchListenereForNested()

        if(Constants.API_CALL_DEMO)
        {
            var teamId=requireArguments().getString("team_id")
            viewModel.teamId=teamId
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    //For hide keyboard
    fun setTouchListenereForNested()
    {
        binding.nestedScrollViewAddPeople.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                requireActivity().hideKeyboard()
                return false
            }
        })
    }

    private fun init() {
        binding.apply {

        }
        setTopLayoutMethod()
    }

    fun setTopLayoutMethod()
    {
        var rightMarginTopLayout=0
        if(checkDeviceType())
        {
            rightMarginTopLayout=requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._18sdp).toInt()
            binding.ivOutsideCloseAddPeople.visibility=View.VISIBLE
            binding.imgCancelAddPeople.visibility=View.GONE
            binding.constraintsAddPeople.setBackgroundResource(R.drawable.et_drawable)
        }else
        {
            binding.constraintsAddPeople.setBackgroundResource(0)
            rightMarginTopLayout=0
            binding.ivOutsideCloseAddPeople.visibility=View.GONE
            binding.imgCancelAddPeople.visibility=View.VISIBLE
        }

        val layoutParams = binding.constraintsAddPeople.layoutParams as LinearLayout.LayoutParams
        layoutParams.topMargin = 0
        layoutParams.leftMargin = 0
        layoutParams.rightMargin = rightMarginTopLayout
        binding.constraintsAddPeople.setLayoutParams(layoutParams)
    }

    private fun addObserverForOpenAddPeople() {
        viewModel.addPeopleEmailScreenOpenData.observe(requireActivity(), Observer {
            var data =it as String
            if(data!=null){
                var bundle = Bundle()
                bundle.putString("team_id", data)
                AddPeopleEmailFragment.newInstanceAddPeopleEmail(viewModel.teamId!!).showNow(requireActivity().supportFragmentManager," SimpleDialog.TAG")
            }
        })
    }

    fun dialogDismissMethod()
    {
        viewModel.dismissDialogData.observe(requireActivity(), Observer {
            var data=it as Boolean
            if(data)
            {
                dismiss()
            }
        })
    }
}