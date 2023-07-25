package com.yapi.views.create_team.second_step_create_team

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.*
import com.yapi.pref.PreferenceFile
import com.yapi.views.edit_profile.EditProfileResponse
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SecondStepViewModel @Inject constructor(val repository: Repository,
val preferenceFile: PreferenceFile,@Named("token") val userToken:String) : ViewModel() {

    private var fileImage: File?=null
    private var isPrivate: Boolean?=false
    var countFieldValue = ObservableField("0/50")
    var workNameValue = ObservableField("")

    var firstTeamName:String?=""
    var quickJoinTeam:Boolean?=false
    var errorData=MutableLiveData<SignInErrorData>()
    fun onClick(view: View) {
        when (view.id) {
            R.id.btnSecondCreateTeam -> {
                Log.e("Hello_Text==", "Helloo")
                // showMessage("Hello")
                if (view.findNavController().currentDestination?.id == R.id.secondStepCreateTeam) {
                    if (workNameValue.get().toString().trim().isNotEmpty()) {
                        isPrivate=false
                        fileImage= File("")

                        if(Constants.API_CALL_DEMO) {
                            callCreateGroupAPIMethod(view)
                        }
                        else
                        {
                            errorData.value= SignInErrorData("",0)
                            view.findNavController()
                                .navigate(R.id.action_secondStepCreateTeam_to_thirdStepCreateTeam)
                        }
                    }else
                    {
                        errorData.value=SignInErrorData("Please enter name",0)
                       // showToastMessage("Please enter name")
                    }
                }
            }
            R.id.linearTopSecondStep, R.id.constrantsTopSecondStep -> {
                MainActivity.activity!!.get()!!.hideKeyboard()
            }
        }
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.w("tag", "onTextChanged $s")
        if(s.length>0)
        {
            errorData.value=SignInErrorData("",0)
        }
        countFieldValue.set(s.length.toString() + "/50")
    }

    fun callCreateGroupAPIMethod(view:View)
    {
        val nameRequest: RequestBody =
            RequestBody.Companion.create("text/plain".toMediaTypeOrNull(),
                firstTeamName.toString())
        val workingRequest: RequestBody = RequestBody.Companion.create("text/plain".toMediaTypeOrNull(), workNameValue.get().toString())
        val descriptionRequest: RequestBody = RequestBody.Companion.create("text/plain".toMediaTypeOrNull(), "")

        val isPrivateRequest: RequestBody = RequestBody.Companion.create("text/plain".toMediaTypeOrNull(),isPrivate.toString())
        val quickJoinRequest: RequestBody = RequestBody.Companion.create("text/plain".toMediaTypeOrNull(), quickJoinTeam.toString())

      //  val number = phoneNumberValue.get().toString().replace(" ","").toLong()

       var imagePath = if (fileImage!!.absolutePath.isEmpty()) null else MultipartBody.Part.createFormData(
            "ImagePath",
            fileImage!!.name!!,
            RequestBody.create("image/*".toMediaTypeOrNull(),fileImage!!.absolutePath))


     /*   val buffer = Buffer()
        buffer.writeLong(number)
        val phoneNumberRequest: RequestBody = RequestBody.create(
            "application/octet-stream".toMediaTypeOrNull(),
            buffer.readByteString())*/

        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<CreateTeamResponse>> {
                override fun onSuccess(success: Response<CreateTeamResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    preferenceFile.saveBooleanValue(Constants.USER_PROFILE_CREATED,true)
                    errorData.value= SignInErrorData("",0)

                    val bundle= Bundle()
                    bundle.putString("team_id", success.body()!!.data._id)
                    view.findNavController()
                        .navigate(R.id.action_secondStepCreateTeam_to_thirdStepCreateTeam,bundle)

                    /*   var bundle= Bundle()
                       bundle.putString("email",emailFieldValue.get())
                       view.findNavController().navigate(R.id.action_signInFragment_to_signUpCodeFragment,bundle)*/
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<CreateTeamResponse> {
                    return retrofitApi.createTeamAPI(userToken,nameRequest, workingRequest,descriptionRequest,isPrivateRequest,imagePath,quickJoinRequest)
                }
            })
    }
}