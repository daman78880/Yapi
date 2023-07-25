package com.yapi.views.create_team.third_step_create_team

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.*
import com.yapi.databinding.CrmDialogLayoutBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.add_people_email.AddEmailResponse
import com.yapi.views.add_people_email.CheckEmailResponse
import com.yapi.views.add_people_email.EmailData
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.greenrobot.eventbus.EventBus
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ThirdStepViewModel @Inject constructor(val preferenceFile: PreferenceFile,val repository: Repository,
    @Named("token") val userToken:String) : ViewModel() {
    var screenWidth: Int? = 0
    var emailFieldValue = ObservableField("")
    var errorData = MutableLiveData<SignInErrorData>()
    var chipGroupAddPeopleEmail: ChipGroup? = null

    var teamId: String? = ""
    fun onClick(view: View) {
        when (view.id) {
            R.id.btnThirdCreateTeam -> {
                val list = ArrayList<String>()
                for (i in 0 until chipGroupAddPeopleEmail?.childCount!!) {
                    val chipView = chipGroupAddPeopleEmail?.getChildAt(i) as Chip
                    val title = chipView.text.toString()
                    list.add(title)
                    Log.d("ChipGroupTitle", title)
                }
                val bundle = Bundle()
                bundle.putStringArrayList("personList", list)

                if (list.size == 0) {
                    errorData.value = SignInErrorData("Please enter email", 1)
                } else {
                    if (checkDeviceType()) {
                        if (Constants.API_CALL_DEMO) {
                            callAddMemberAPIMethod(view, list)
                        } else {
                            EventBus.getDefault().post(MyMessageEvent(1, Constants.MENU_KEY)) //post
                        }
                    } else {
                        if (view.findNavController().currentDestination?.id == R.id.thirdStepCreateTeam) {
                            if (Constants.API_CALL_DEMO) {
                                callAddMemberAPIMethod(view, list)
                            } else {
                                view.findNavController()
                                    .navigate(R.id.action_thirdStepCreateTeam_to_menuFragment)
                            }

                        }
                    }
                }


               /* if (view.findNavController().currentDestination?.id == R.id.thirdStepCreateTeam) {
                    //if (checkValidation()) {
                        errorData.value = SignInErrorData("", 0)
                       // preferenceFile.saveStringValue(Constants.USER_ID, "1")
                        if (MainActivity.activity!!.get()!!.resources
                                .getBoolean(R.bool.isTab)
                        ) {
                            System.out.println("phone========tablet")
                            Log.e("gsegegsgsgs111===", System.currentTimeMillis().toString())

                            EventBus.getDefault().post(MyMessageEvent(1, Constants.MENU_KEY)) //post
                            // event
                        } else {
                            view.findNavController()
                                .navigate(R.id.action_thirdStepCreateTeam_to_menuFragment)
                        }
                  //  }
                }*/
            }
            R.id.tvSkipStep -> {
                if (view.findNavController().currentDestination?.id == R.id.thirdStepCreateTeam) {
                   // preferenceFile.saveStringValue(Constants.USER_ID, "1")
                    if (MainActivity.activity!!.get()!!.resources.getBoolean(R.bool.isTab)) {
                        System.out.println("phone========tablet")
                        Log.e("gsegegsgsgs111===", System.currentTimeMillis().toString())

                        EventBus.getDefault()
                            .post(MyMessageEvent(1, Constants.MENU_KEY)) //post event
                    } else {
                        view.findNavController()
                            .navigate(R.id.action_thirdStepCreateTeam_to_menuFragment)
                    }
                }
            }
            R.id.linearTopThirdStep, R.id.constraintsTopThirdStep -> {
                MainActivity.activity!!.get()!!.hideKeyboard()
            }
            R.id.btnInviteCRM -> {
                //show CRM Invite Dialog
                showCRMDialog()
            }
            R.id.constraintsAddMember -> {
                //for Add Member
                val bundle = Bundle()
                if (Constants.API_CALL_DEMO) {
                    bundle.putString("team_id", teamId)
                }
                view.findNavController()
                    .navigate(R.id.action_thirdStepCreateTeam_to_addPeopleFragment, bundle)
            }
        }
    }

    //show CRM Dialog
    private fun showCRMDialog() {
        var dialog = Dialog(MainActivity.activity!!.get()!!)
        var databinding =
            CrmDialogLayoutBinding.inflate(LayoutInflater.from(MainActivity.activity!!.get()!!))
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(databinding.root)
        dialog.show()
        var cardviewCRMInvite = dialog.findViewById<CardView>(R.id.cardviewCRMInvite)
        cardviewCRMInvite.layoutParams.width = (screenWidth!!.toDouble() / 1.1).toInt()

        databinding.btnCancelInvite.setOnClickListener {
            dialog.dismiss()
        }
        databinding.btnInviteCRM.setOnClickListener { }
    }

    private fun checkValidation(): Boolean {
        if (emailFieldValue.get().toString().isEmpty()) {
            //  showToastMessage(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_email))
            errorData.value =
                SignInErrorData(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_email),
                    0)
            return false
        } else if (!(isValidEmail(emailFieldValue.get().toString()))) {
            // showToastMessage(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_valid_email))
            errorData.value =
                SignInErrorData(MainActivity.activity!!.get()!!.resources.getString(R.string.please_enter_valid_email),
                    0)

            return false
        } else {
            return true
        }
    }

    fun AfterTextChanged(s: Editable?) {
        if (emailFieldValue.get().toString().trim().length > 0 && isValidEmail(emailFieldValue.get()
                .toString())
        ) {
            errorData.value = SignInErrorData("", 0)
        } else {
        }
    }


    private fun callAddMemberAPIMethod(view: View, list: ArrayList<String>) {

        var finalJsonObject = JsonObject()
        var emailArray = JsonArray()
        for (idx in 0 until list.size) {
            var jsonObject = JsonObject()
            jsonObject.addProperty("user_email", list.get(idx).toString())
            jsonObject.addProperty("role", "employee")
            emailArray.add(jsonObject)
        }

        finalJsonObject.addProperty("team_id", teamId.toString())
        finalJsonObject.add("invitaions", emailArray)
        Log.e("Add_members_data_Input===", finalJsonObject.toString())
        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<AddEmailResponse>> {
                override fun onSuccess(success: Response<AddEmailResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    if (checkDeviceType())
                     {
                        EventBus.getDefault().post(MyMessageEvent(1, Constants.MENU_KEY)) //post
                    } else {
                        view.findNavController()
                            .navigate(R.id.action_thirdStepCreateTeam_to_menuFragment)
                    }
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<AddEmailResponse> {
                    return retrofitApi.addTeamMembersAPI(userToken,finalJsonObject)
                }
            })
    }

    fun checkEmailAPIMethod(email:String): LiveData<CheckEmailResponse> {

        var finalJsonObject = JsonObject()
        var responseData=MutableLiveData<CheckEmailResponse>()
        finalJsonObject.addProperty("email", email)
        Log.e("Add_members_data_Input===", finalJsonObject.toString())
        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<CheckEmailResponse>> {
                override fun onSuccess(success: Response<CheckEmailResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())
                    responseData.value=success.body()
                }

                override fun onError(message: String) {
                    var data= CheckEmailResponse(message,400)
                    responseData.value=data
                    //  MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<CheckEmailResponse> {
                    //   return retrofitApi.checkUserEmailAPI(userToken,finalJsonObject)
                    return retrofitApi.checkUserEmailAPI(finalJsonObject)
                }
            })
        return responseData
    }
}