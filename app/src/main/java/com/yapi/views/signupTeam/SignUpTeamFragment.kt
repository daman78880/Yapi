package com.yapi.views.signupTeam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yapi.R
import com.yapi.common.Constants
import com.yapi.databinding.SignupTeamLayoutBinding
import com.yapi.pref.PreferenceFile
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint

class SignUpTeamFragment : Fragment() {

    private lateinit var rvUsersAdapter: RVUsersAdapter
    private lateinit var dataBinding: SignupTeamLayoutBinding
    private val viewModelSignUpViewModel: SignupViewModel by viewModels()


    @Inject
    lateinit var preferenceFile: PreferenceFile

    //private var vm:SignupViewModel by ViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dataBinding = SignupTeamLayoutBinding.inflate(LayoutInflater.from(requireActivity()))
        dataBinding.model = viewModelSignUpViewModel
        initUI()
        return dataBinding.root
    }

    private fun initUI() {
        var loginEmail = preferenceFile.fetchStringValue("login_email")
        viewModelSignUpViewModel.loginEmailMessage.set(requireActivity().resources.getString(R.string.these_invitation_for) + "\n" + loginEmail)

        if (Constants.API_CALL_DEMO) {
            addViewTeamInvitationObserver()
            viewModelSignUpViewModel.fetchViewInvitationMethod()
        }
    }

    private fun addViewTeamInvitationObserver() {

        viewModelSignUpViewModel.viewTemlListResponse.observe(requireActivity(), Observer {
            var dataResponse = it as ArrayList<ViewInvitationData>
            if (dataResponse != null && dataResponse.size > 0) {
                rvUsersAdapter =
                    RVUsersAdapter(requireActivity(), dataResponse, object : TeamClickListener {
                        override fun onClickJoin(position: Int, teamId: String) {
                            acceptInvitationObserver(position, teamId)
                        }
                    })
                dataBinding.rvUsers.layoutManager = LinearLayoutManager(requireActivity())
                dataBinding.rvUsers.adapter = rvUsersAdapter
            }
        })
    }

    fun acceptInvitationObserver(position: Int, teamId: String) {
        viewModelSignUpViewModel.acceptInvitationMethod(teamId)
            .observe(requireActivity(), Observer {
                var data = it as Boolean
                if (data) {
                    Log.e("Success===", "Succeessss")
                    rvUsersAdapter.getInvitationList().removeAt(position)
                    rvUsersAdapter.notifyDataSetChanged()
                } else {
                    Log.e("Success===", "No Data")
                }
            })
    }
}