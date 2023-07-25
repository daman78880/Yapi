package com.yapi.views.userList

import android.app.Dialog
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.pref.PreferenceFile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(val preferenceFile: PreferenceFile) : ViewModel() {

    var screenWidth:Int?=0
    fun onClick(view: View) {
        when (view.id) {
            R.id.txtuserBack -> {
                if (checkDeviceType()) {

                } else {
                    view.findNavController().popBackStack()
                }
            }
            R.id.linearAddMember->{
                //For Add Member
                showAddMemberMethod(1)
            }
        }
    }

    fun showAddMemberMethod(type:Int) {

        var dividedValue=0.0
        if(checkDeviceType())
        {
            dividedValue=1.3
        }else
        {
            dividedValue=1.05
        }
        var dialog = Dialog(MainActivity.activity!!.get()!!)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.edit_memeber_info_popup)


        var linearEditMember = dialog.findViewById<LinearLayout>(R.id.linearEditMember)

        if(checkDeviceType()){
            val marginDp = MainActivity.activity!!.get()!!.resources.getDimension(com.intuit.sdp.R.dimen._10sdp)
            val rightmarginDp = MainActivity.activity!!.get()!!.resources.getDimension(com.intuit.sdp.R.dimen._30sdp)
            linearEditMember.setPadding(0,0,rightmarginDp.toInt(),0)

            var second_frame_height= preferenceFile.fetchStringValue("second_frame_height").toInt()-marginDp
            var second_frame_width=  preferenceFile.fetchStringValue("second_frame_width").toInt()-marginDp
          //  Log.e("nefjkwnddfkewfwefe===",second_frame_height+"==="+second_frame_width)
            dialog.window!!.setLayout(second_frame_width.toInt(),second_frame_height.toInt())
            //dialog.window!!.attributes.horizontalMargin=10

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            dialog.window!!.setGravity(Gravity.RIGHT)

        }else
        {
            linearEditMember.layoutParams.width = (screenWidth!!.toDouble() / dividedValue).toInt()
        }

        dialog.show()
        var ivInnerBack = dialog.findViewById<ImageView>(R.id.ivInnerBack)
        var btnCancelTemplate = dialog.findViewById<AppCompatButton>(R.id.btnCancelTemplate)
        var ivOutsideCloseGroup = dialog.findViewById<ImageView>(R.id.ivOutsideCloseGroup)
        var linearTemplateHeader = dialog.findViewById<LinearLayout>(R.id.linearTemplateHeader)
        var ivaddMemberLogo = dialog.findViewById<AppCompatImageView>(R.id.ivaddMemberLogo)

        var tvAddMemberTitle = dialog.findViewById<AppCompatTextView>(R.id.tvAddMemberTitle)
        var tvAddMemberDescription = dialog.findViewById<AppCompatTextView>(R.id.tvAddMemberDescription)
        var btnTemplateSave = dialog.findViewById<AppCompatButton>(R.id.btnTemplateSave)

        if(checkDeviceType())
        {
            ivOutsideCloseGroup.visibility=View.VISIBLE
            ivInnerBack.visibility=View.GONE
            ivaddMemberLogo.visibility=View.VISIBLE
            ivaddMemberLogo.setImageResource(R.drawable.add_member_logo)
            btnTemplateSave.setText(MainActivity.activity!!.get()!!.resources.getString(R.string.add_member_button))

            tvAddMemberTitle.visibility=View.GONE
            tvAddMemberDescription.visibility=View.GONE
        }else
        {
            ivaddMemberLogo.visibility=View.GONE

            ivOutsideCloseGroup.visibility=View.GONE
            ivInnerBack.visibility=View.VISIBLE

            tvAddMemberTitle.visibility=View.VISIBLE
            tvAddMemberDescription.visibility=View.VISIBLE

            tvAddMemberTitle.setText(MainActivity.activity!!.get()!!.resources.getString(R.string.add_member_texttt))
            btnTemplateSave.setText(MainActivity.activity!!.get()!!.resources.getString(R.string.save_text))
        //    tvAddMemberDescription.setText(MainActivity.activity!!.get()!!.resources.getString(R.string.add_member_text))
        }
        linearTemplateHeader.visibility=View.VISIBLE

        ivInnerBack.setOnClickListener {
            dialog.dismiss()
        }
        ivOutsideCloseGroup.setOnClickListener {
            dialog.dismiss()
        }

        btnCancelTemplate.setOnClickListener {
            dialog.dismiss()
        }
    }
}