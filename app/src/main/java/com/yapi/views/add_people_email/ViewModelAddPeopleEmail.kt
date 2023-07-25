package com.yapi.views.add_people_email

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ViewModelAddPeopleEmail @Inject constructor(val repository: Repository,@Named("token") val userToken:String) : ViewModel() {
    var chipGroupAddPeopleEmail: ChipGroup? = null
    var addPeopleEmailConfirmationOpenData = MutableLiveData<EmailData>()
    var dismissDialogData = MutableLiveData<Boolean>()

    var errorData = MutableLiveData<SignInErrorData>()

    var hideKeyboardData = MutableLiveData<Boolean>()

    var teamId: String? = ""

    fun onClick(view: View) {
        when (view.id) {
            R.id.layoutNestedScrollViewAddPeopleEmail, R.id.layoutAddPeopleEmail, R.id.nestedScrollViewAddPeopleEmail -> {
                // MainActivity.activity?.get()?.hideKeyboard()
                hideKeyboardData.value = true
            }
            R.id.imgCancelAddPeopleEmail, R.id.ivOutsideCloseAddPeopleEmail -> {
                if (checkDeviceType()) {
                    dismissDialogData.value = true
                } else {
                    if (view.findNavController().currentDestination?.id == R.id.addPeopleEmailFragment) {
                        view.findNavController().popBackStack()
                    }
                }
            }
            R.id.btnNextGroupAddPeopleEmail -> {
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
                            addPeopleEmailConfirmationOpenData.value = EmailData()
                        }
                    } else {
                        if (view.findNavController().currentDestination?.id == R.id.addPeopleEmailFragment) {
                            if (Constants.API_CALL_DEMO) {
                                callAddMemberAPIMethod(view, list)
                            } else {
                                var bundle=Bundle()
                                bundle.putSerializable("invitation_list",EmailData())
                                    view.findNavController()
                                        .navigate(R.id.action_addPeopleEmailFragment_to_addPeopleEmailConfirmationFragment,bundle)
                            }

                        }
                    }
                }
            }
            R.id.btnBackAddPeopleEmail -> {
                if (checkDeviceType()) {
                    dismissDialogData.value = true
                } else {
                    if (view.findNavController().currentDestination?.id == R.id.addPeopleEmailFragment) {
                        view.findNavController().popBackStack()
                    }
                }

            }

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

                    if (checkDeviceType()) {
                        addPeopleEmailConfirmationOpenData.value = success.body()!!.data!!

                    } else {
                        var bundle=Bundle()
                        bundle.putSerializable("invitation_list",success.body()!!.data)
                        view.findNavController()
                            .navigate(R.id.action_addPeopleEmailFragment_to_addPeopleEmailConfirmationFragment,bundle)
                    }

                    /*   var bundle= Bundle()
                       bundle.putString("email",emailFieldValue.get())
                       view.findNavController().navigate(R.id.action_signInFragment_to_signUpCodeFragment,bundle)*/
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<AddEmailResponse> {
                    return retrofitApi.addTeamMembersAPI(userToken,finalJsonObject)
                }
            })
    }


     fun checkEmailAPIMethod(email:String):LiveData<CheckEmailResponse> {

        var finalJsonObject = JsonObject()
        var responseData=MutableLiveData<CheckEmailResponse>()
        finalJsonObject.addProperty("email", email)
        Log.e("Add_members_data_Input===", finalJsonObject.toString())
        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<CheckEmailResponse>> {
                override fun onSuccess(success: Response<CheckEmailResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    responseData.value=success.body()

                    /*   var bundle= Bundle()
                       bundle.putString("email",emailFieldValue.get())
                       view.findNavController().navigate(R.id.action_signInFragment_to_signUpCodeFragment,bundle)*/
                }

                override fun onError(message: String) {
                    var data=CheckEmailResponse(message,400)
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