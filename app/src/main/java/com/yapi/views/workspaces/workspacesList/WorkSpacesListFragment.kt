package com.yapi.views.workspaces.workspacesList

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yapi.R
import com.yapi.common.changeBackgroundForEditError
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard
import com.yapi.databinding.AddWorkspaceLayoutBinding
import com.yapi.databinding.WorkspaceListFragmentBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkSpacesListFragment : Fragment() {

    private var editWorkspaceBinding: AddWorkspaceLayoutBinding? = null
    private var screenWidth: Int? = 0
    private var rvAdapter: RVWorkspaceListAdapter? = null
    private var dataBinding: WorkspaceListFragmentBinding? = null

    val viewModel: WorkspaceViewModel by viewModels()

    @Inject
     lateinit var preferenceFile: PreferenceFile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dataBinding = WorkspaceListFragmentBinding.inflate(inflater)
        dataBinding!!.vModel = viewModel

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        viewModel.screenWidth = screenWidth

        initUI()
        return dataBinding!!.root
    }

    private fun initUI() {
        if(checkDeviceType())
        {
            viewModel.showBackButton.set(false)

         //   var width=dataBinding!!.linearAddWorkspaces.layoutParams.width
            val vto: ViewTreeObserver = dataBinding!!.linearAddWorkspaces.getViewTreeObserver()
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
               override fun onGlobalLayout() {
                    //Do it here
                   /* val layoutGet: LinearLayout =
                        findViewById(android.R.id.GameField1) as LinearLayout*/
                    val layParamsGet = dataBinding!!.linearAddWorkspaces.getLayoutParams()
                    val width: Int = layParamsGet.width
                   Log.e("dfnkwsssadadad===",width.toString())
                  //  removeOnGlobalLayoutListener(layoutGet, this) // Assuming layoutGet is the View which you got the ViewTreeObserver from
                }
            })

        }else
        {
            viewModel.showBackButton.set(true)
        }

        checkValidationObserver()
        showAddWorkspaceObserver()

        setAdapter()
    }

    private fun showAddWorkspaceObserver() {

        viewModel.showAddWorkspaceDialog.observe(requireActivity(), Observer {
            var data =it as Boolean
            if(data)
            {
                showEditWorkspaceMethod("add")
            }
        })
    }

    private fun checkValidationObserver() {

        viewModel.checkValidationData.observe(requireActivity(), Observer {
            var data = it as SignInErrorData

            resetallFields(editWorkspaceBinding!!.txtErrorName, editWorkspaceBinding!!.etNameValue)
            resetallFields(editWorkspaceBinding!!.txtErrorDatabaseName, editWorkspaceBinding!!.etDatabaseValue)
            resetallFields(editWorkspaceBinding!!.txtErrorToken, editWorkspaceBinding!!.etTkenValue)
            resetallFields(editWorkspaceBinding!!.txtErrorSecret, editWorkspaceBinding!!.etSecretValue)

            if (data != null) {
                var editText: AppCompatEditText? = null
                var errorText: AppCompatTextView? = null
                if (data.fieldId == 1) {
                    editText = editWorkspaceBinding!!.etNameValue
                    errorText = editWorkspaceBinding!!.txtErrorName
                } else
                    if (data.fieldId == 2) {
                        editText = editWorkspaceBinding!!.etDatabaseValue
                        errorText = editWorkspaceBinding!!.txtErrorDatabaseName
                    } else
                        if (data.fieldId == 3) {
                            editText = editWorkspaceBinding!!.etTkenValue
                            errorText = editWorkspaceBinding!!.txtErrorToken
                        } else

                            if (data.fieldId == 4) {
                                editText = editWorkspaceBinding!!.etSecretValue
                                errorText = editWorkspaceBinding!!.txtErrorSecret
                            } else {
                                resetallFields(editWorkspaceBinding!!.txtErrorName,
                                    editWorkspaceBinding!!.etNameValue)
                                resetallFields(editWorkspaceBinding!!.txtErrorDatabaseName,
                                    editWorkspaceBinding!!.etDatabaseValue)
                                resetallFields(editWorkspaceBinding!!.txtErrorToken,
                                    editWorkspaceBinding!!.etTkenValue)
                                resetallFields(editWorkspaceBinding!!.txtErrorSecret,
                                    editWorkspaceBinding!!.etSecretValue)
                            }

                if (data != null && data.message.isNotEmpty()) {
                    if (data.fieldId != -1) {
                        errorText!!.text = data.message

                        changeBackgroundForEditError(editText!!,
                            requireActivity().resources.getColor(
                                R.color.error_box_color),
                            requireActivity().resources.getColor(R.color.error_border_color))
                    }
                }
            } else {
            }
        })
    }

    fun resetallFields(errorText: AppCompatTextView, editText: AppCompatEditText) {
        errorText.text = ""
        changeBackgroundForEditError(editText, requireActivity().resources.getColor(
            R.color.white),
            requireActivity().resources.getColor(R.color.liteGrey))
    }

    fun setAdapter() {
        var workspaceList = ArrayList<WorkSpaceData>()
        workspaceList.clear()
        workspaceList.add(WorkSpaceData("Airpos USA", false))
        workspaceList.add(WorkSpaceData("Dougs", false))
        workspaceList.add(WorkSpaceData("Hassen Air", false))
        workspaceList.add(WorkSpaceData("Above And Beyond S.", false))
        workspaceList.add(WorkSpaceData("Air Pros Ocala Division", false))

        workspaceList.add(WorkSpaceData("Airpos USA", false))
        workspaceList.add(WorkSpaceData("Dougs", false))
        workspaceList.add(WorkSpaceData("Hassen Air", false))
        workspaceList.add(WorkSpaceData("Above And Beyond S.", false))
        workspaceList.add(WorkSpaceData("Air Pros Ocala Division", false))
        workspaceList.add(WorkSpaceData("Airpos USA", false))
        workspaceList.add(WorkSpaceData("Dougs", false))
        workspaceList.add(WorkSpaceData("Hassen Air", false))
        workspaceList.add(WorkSpaceData("Above And Beyond S.", false))
        workspaceList.add(WorkSpaceData("Air Pros Ocala Division", false))
        workspaceList.add(WorkSpaceData("Airpos USA", false))
        workspaceList.add(WorkSpaceData("Dougs", false))
        workspaceList.add(WorkSpaceData("Hassen Air", false))
        workspaceList.add(WorkSpaceData("Above And Beyond S.", false))
        workspaceList.add(WorkSpaceData("Air Pros Ocala Division", false))
        rvAdapter = RVWorkspaceListAdapter(requireActivity(),
            workspaceList,
            object : WorkspaceClickListener {
                override fun onEditClickMethod(position: Int) {
                    //For Edit workspace
                    showEditWorkspaceMethod("edit")
                }

                override fun onOpenClickMethod(position: Int) {
                    //For Open workspace
                    findNavController().navigate(R.id.action_workspaceList_to_openWorkspace)
                }
            })
        dataBinding!!.rvWorkspaceList.layoutManager = LinearLayoutManager(requireActivity())
        dataBinding!!.rvWorkspaceList.adapter = rvAdapter
    }

    fun showEditWorkspaceMethod(popupType:String) {

        var dividedValue = 0.0
        if (checkDeviceType()) {
            dividedValue = 1.3
        } else {
            dividedValue = 1.05
        }
        var dialog = Dialog(requireContext())
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        editWorkspaceBinding = AddWorkspaceLayoutBinding.inflate(LayoutInflater.from(requireActivity()))

        editWorkspaceBinding!!.mViewModel=viewModel
        dialog.setContentView(editWorkspaceBinding!!.root)
        dialog.show()

        // var linearEditMember = dialog.findViewById<LinearLayout>(R.id.linearEditMember)
        // linearEditMember.layoutParams.width = (screenWidth!!.toDouble() / dividedValue).toInt()

        if (checkDeviceType()) {
            val marginDp =
                requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._10sdp)
            val rightmarginDp =
                requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._30sdp)
            editWorkspaceBinding!!.linearEditMember.setPadding(0, 0, rightmarginDp.toInt(), 0)

            var second_frame_height =
                preferenceFile.fetchStringValue("second_frame_height").toInt() - marginDp
            var second_frame_width =
                preferenceFile.fetchStringValue("second_frame_width").toInt() - marginDp
            //  Log.e("nefjkwnddfkewfwefe===",second_frame_height+"==="+second_frame_width)
            dialog.window!!.setLayout(second_frame_width!!.toInt(), second_frame_height.toInt())
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            dialog.window!!.setGravity(Gravity.RIGHT)

        } else {
            editWorkspaceBinding!!.linearEditMember.layoutParams.width =
                (screenWidth!!.toDouble() / dividedValue).toInt()
        }

        if (checkDeviceType()) {
            editWorkspaceBinding!!.ivOutsideCloseGroup.visibility = View.VISIBLE
            editWorkspaceBinding!!.ivInnerBack.visibility = View.GONE
            // editWorkspaceBinding!!.ivaddMemberLogo.visibility=View.VISIBLE

             editWorkspaceBinding!!.ivaddMemberLogo!!.setBackgroundResource(R.drawable.edit_workspace_banner)

            editWorkspaceBinding!!.btnSaveWorkspace.text =  requireActivity().resources.getString(
                R.string.save_information_text)
            editWorkspaceBinding!!.tvAddMemberTitle.visibility = View.VISIBLE
            editWorkspaceBinding!!.tvAddMemberDescription.visibility = View.VISIBLE

            editWorkspaceBinding!!.ivOutsideCloseGroup.visibility=View.VISIBLE
            editWorkspaceBinding!!.ivInnerBack.visibility=View.GONE

            if(popupType.equals("edit")) {
                editWorkspaceBinding!!.tvAddMemberTitle.text =
                    requireActivity().resources.getString(
                        R.string.edit_workspace_text_title)
                editWorkspaceBinding!!.btnSaveWorkspace.text = requireActivity().resources.getString(
                    R.string.save_changes_text)
            }else
            {
                editWorkspaceBinding!!.tvAddMemberTitle.text =
                    requireActivity().resources.getString(
                        R.string.add_workspace_text_title)

                editWorkspaceBinding!!.btnSaveWorkspace.text = requireActivity().resources.getString(
                    R.string.save_text)
            }

        } else {
            editWorkspaceBinding!!.ivaddMemberLogo!!.visibility=View.GONE
            editWorkspaceBinding!!.ivOutsideCloseGroup.visibility = View.GONE
            editWorkspaceBinding!!.ivInnerBack.visibility = View.VISIBLE
            editWorkspaceBinding!!.tvAddMemberTitle.visibility = View.VISIBLE
            editWorkspaceBinding!!.tvAddMemberDescription.visibility = View.VISIBLE

            editWorkspaceBinding!!.ivOutsideCloseGroup.visibility=View.GONE
            editWorkspaceBinding!!.ivInnerBack.visibility=View.VISIBLE

            if(popupType.equals("edit")) {
                editWorkspaceBinding!!.tvAddMemberTitle.text =
                    requireActivity().resources.getString(
                        R.string.edit_workspace_text_title)
            }else
            {
                editWorkspaceBinding!!.tvAddMemberTitle.text =
                    requireActivity().resources.getString(
                        R.string.add_workspace_text_title)
            }
            editWorkspaceBinding!!.btnSaveWorkspace.text = requireActivity().resources.getString(
                R.string.save_text)
        }

        editWorkspaceBinding!!.ivInnerBack.setOnClickListener {
            dialog.dismiss()
        }
        editWorkspaceBinding!!.ivOutsideCloseGroup.setOnClickListener {
            dialog.dismiss()
        }

        editWorkspaceBinding!!.btnCancelTemplate.setOnClickListener {
            dialog.dismiss()
        }
        editWorkspaceBinding!!.ivInnerBack.setOnClickListener {
            dialog.dismiss()
        }

        editWorkspaceBinding!!.constraintsTopTemplate.setOnClickListener {
           requireActivity().hideKeyboard()
        }
    }
}