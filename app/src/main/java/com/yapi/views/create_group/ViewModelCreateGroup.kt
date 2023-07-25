package com.yapi.views.create_group

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.*
import com.yapi.views.create_team.second_step_create_team.CreateTeamResponse
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ViewModelCreateGroup @Inject constructor(val repository: Repository,@Named("token") val userToken:String) : ViewModel() {
    private var isQuickJoin: Boolean? = false
    private var fileImage: File? = null
    var groupNameValue = ObservableField("")
    var groupNameCount = ObservableField("0/128")

    var groupDescriptionValue = ObservableField("")
    var groupDescriptionCount = ObservableField("0/256")

    var addPeopleScreenOpenData = MutableLiveData<String>()
    var dismissDialogData = MutableLiveData<Boolean>()

    var privateGroupToggle = ObservableBoolean(false)

    var errorData = MutableLiveData<SignInErrorData>()
    var hideKeyboardData = MutableLiveData<Boolean>()
    var photoFile: File?=null
    fun onClick(view: View) {
        when (view.id) {
            R.id.btnCreateGroup -> {
                if (checkValidation()) {
                    errorData.value = SignInErrorData("", 0)

                    isQuickJoin = false

                    fileImage = File("")
                    if (Constants.API_CALL_DEMO) {
                        callCreateGroupAPIMethod(view)
                    } else {
                        if (checkDeviceType()) {
                            addPeopleScreenOpenData.value = ""
                        } else {
                            var bundle = Bundle()
                            //bundle.putString("team_id", success.body()!!.data._id)
                            view.findNavController()
                                .navigate(R.id.action_createGroupFragment_to_addPeopleFragment,
                                    bundle)
                        }
                    }
                }
            }

            R.id.imgCancelCreateGroup, R.id.ivOutsideCloseGroup -> {
                if (checkDeviceType()) {
                    dismissDialogData.value = true
                } else {
                    if (view.findNavController().currentDestination?.id == R.id.createGroupFragment) {
                        view.findNavController().popBackStack()
                    }
                }
            }
            R.id.topCreateGroupLayout -> {
                //view.context.hideKeyboard()
                // MainActivity.activity!!.get()!!.hideKeyboard()
                hideKeyboardData.value = true
            }
        }
    }

    fun AfterTextChanged(s: CharSequence) {
        if (groupNameValue.get().toString().trim().length > 0) {
            errorData.value = SignInErrorData("", 1)
        }
        groupNameCount.set(groupNameValue.get().toString().trim().length.toString() + "/128")
    }

    fun AfterTextChangedDes(s: CharSequence) {
        if (groupDescriptionCount.get().toString().trim().length > 0) {
            errorData.value = SignInErrorData("", 2)
        }
        groupDescriptionCount.set(groupDescriptionValue.get().toString()
            .trim().length.toString() + "/256")
    }


    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.w("tag", "onTextChanged $s")


        /*   if (s.length == 0) {
               emailCorrectValue.set(false)
           } else if (emailFieldValue.get().toString().trim().length>0 && isValidEmail(emailFieldValue.get().toString())) {
               emailCorrectValue.set(true)
           } else {
               emailCorrectValue.set(false)
           }*/
    }

    fun checkValidation(): Boolean {
        if (groupNameValue.get().toString().trim().length == 0) {
            // showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.enter_group_name))
            errorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                .getString(R.string.enter_group_name), 1)
            return false
        } /*else
            if (groupDescriptionValue.get().toString().trim().length == 0) {
                //  showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.enter_group_description))
                errorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                    .getString(R.string.enter_group_description), 2)
                return false
            }*/
        return true
    }


    private fun callCreateGroupAPIMethod(view: View) {



        val nameRequest: RequestBody =
            groupNameValue.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val workingRequest: RequestBody =
            "".toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionRequest: RequestBody =
            groupDescriptionValue.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val isPrivateRequest: RequestBody =
            privateGroupToggle.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val quickJoinRequest: RequestBody =
            isQuickJoin.toString().toRequestBody("text/plain".toMediaTypeOrNull())





        var file:File?=null
        var photoBody:MultipartBody.Part?=null
        if(photoFile!=null) {
            file = File(photoFile!!.getPath())
            var reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file);
            photoBody = MultipartBody.Part.createFormData("profile_pic",
                file.getName(), reqFile);
        }else
        {
            file = File("")
        }


        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<CreateTeamResponse>> {
                override fun onSuccess(success: Response<CreateTeamResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    if (checkDeviceType()) {
                        addPeopleScreenOpenData.value = success.body()!!.data._id
                    } else {

                        var bundle = Bundle()
                        bundle.putString("team_id", success.body()!!.data._id)
                        view.findNavController()
                            .navigate(R.id.action_createGroupFragment_to_addPeopleFragment, bundle)
                    }
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<CreateTeamResponse> {
                    if(photoFile!=null) {
                        return retrofitApi.createTeamAPI(userToken, nameRequest,
                            workingRequest,
                            descriptionRequest,
                            isPrivateRequest,
                            photoBody,
                            quickJoinRequest)
                    }else
                    {
                        return retrofitApi.createTeamWithoutImageAPI(userToken, nameRequest,
                            workingRequest,
                            descriptionRequest,
                            isPrivateRequest,
                            quickJoinRequest)
                    }
                }
            })
    }
}