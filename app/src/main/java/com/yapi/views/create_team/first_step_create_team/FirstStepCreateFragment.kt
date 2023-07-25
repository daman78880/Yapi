package com.yapi.views.create_team.first_step_create_team

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.Constants
import com.yapi.common.changeBackgroundForEditError
import com.yapi.common.changeBackgroundForError
import com.yapi.databinding.FirstStepCreateFragmentBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirstStepCreateFragment : Fragment() {
    private lateinit var dataBinding: FirstStepCreateFragmentBinding

    @Inject
    lateinit var preferenceFile:PreferenceFile
    val viewModel:FirstStepViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dataBinding = FirstStepCreateFragmentBinding.inflate(LayoutInflater.from(requireActivity()))
        dataBinding.vModel=viewModel

/*       var email=preferenceFile?.fetchStringValue(Constants.USER_TOKEN)
Log.e("fmlfafaafaf===",email.toString())*/
        initUI()
        return dataBinding.root
    }

    private fun initUI() {
        showErrorUIObserver()
        var email=  preferenceFile?.fetchStringValue("login_email")
        var firstText= MainActivity.activity?.get()?.resources?.getString(R.string.create_team_first_text)
        var lastText= MainActivity.activity?.get()?.resources?.getString(R.string.create_team_last_text)
        var companyName="@Qbistro.com"
        //var checkBoxText=
    //    dataBinding.tvCheckBoxName!!.setText(Html.fromHtml(requireActivity().resources.getString(R.string.create_team_first_text)+" <font color='#3D3D3D'><b>"+email+"</b></font> "+ requireActivity().resources.getString( R.string.create_team_last_text)))
        dataBinding.tvCheckBoxName!!.setText(Html.fromHtml(requireActivity().resources.getString(R.string.create_team_first_text)+" <font color='#3D3D3D'><b>"+companyName+"</b></font> "+ requireActivity().resources.getString(
            R.string.create_team_last_text)))
    }

    fun showErrorUIObserver()
    {
        viewModel.errorData.observe(requireActivity(), Observer {
            var data=it as SignInErrorData
            if(data!=null && data.message.isNotEmpty())
            {
                dataBinding.txtErrorFirstStep!!.setText(data.message)
                changeBackgroundForEditError(dataBinding.etTeamName,requireActivity().resources.getColor(R.color.error_box_color),
                    requireActivity().resources.getColor(R.color.error_border_color))
            }else
            {
                dataBinding.txtErrorFirstStep!!.setText("")
                changeBackgroundForEditError(dataBinding.etTeamName,requireActivity().resources.getColor(R.color.white),
                    requireActivity().resources.getColor(R.color.liteGrey))
            }
        })
    }
}