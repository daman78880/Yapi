package com.yapi.views.edit_profile

import android.text.Editable
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
import com.yapi.pref.PreferenceFile
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
class ViewModelEditProfile @Inject constructor(
    val repository: Repository, val preferenceFile: PreferenceFile,
    @Named("token") val userToken: String,
) : ViewModel() {
    var countryCodeValue = ObservableField("")
    var phoneNumberValue = ObservableField("")
    var dismissDialogData = MutableLiveData<Boolean>()

    var checkTabValid: Boolean? = false
    var constraintsMarginEnd: Int? = 0

    var nameValue = ObservableField<String>("")
    var setNameValue = ObservableField<String>("")
    var userNameValue = ObservableField<String>("")
    var emailAddressValue = ObservableField<String>("")
    var aboutValue = ObservableField<String>("")
    var showTopNameTag = ObservableField<String>("")
    var photoUrl = ObservableField<String>("")

    var noImageOnlyNameVisible=ObservableBoolean(false)

    var errorData = MutableLiveData<SignInErrorData>()
    var phoneErrorData = MutableLiveData<SignInErrorData>()

    var photoFile: File?=null

    fun onClick(view: View) {
        when (view.id) {
            R.id.layoutEditProfile, R.id.layoutScrollViewEditProfile -> {
                //for hide keyboard
                MainActivity.activity!!.get()!!.hideKeyboard()
            }
            R.id.imgCancelEditProfile, R.id.ivOutsideCloseAddPeople -> {
                if (checkDeviceType()) {
                    dismissDialogData.value = true
                } else {
                    if (view.findNavController().currentDestination?.id == R.id.editProfileFragment) {
                        view.findNavController().popBackStack()
                    }
                }
            }
            R.id.btnDoneEditProfile -> {
                if (checkDeviceType()) {
                    if (checkValidation()) {
                        if (Constants.API_CALL_DEMO) {
                            Log.e("First_Token===", userToken)
                            Log.e("First_Token111===",
                                preferenceFile.fetchStringValue(Constants.USER_TOKEN))
                            callEditAPIMethod(view)
                        } else {
                            dismissDialogData.value = true
                        }
                    }
                } else {
                    if (view.findNavController().currentDestination?.id == R.id.editProfileFragment) {
                        if (checkValidation()) {
                            if (Constants.API_CALL_DEMO) {
                                Log.e("First_Token===", userToken)
                                Log.e("First_Token111===",
                                    preferenceFile.fetchStringValue(Constants.USER_TOKEN))
                                callEditAPIMethod(view)
                            } else {
                                view.findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }


    fun callEditAPIMethod(view: View) {
        val nameRequest: RequestBody =
            nameValue.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val userNameRequest: RequestBody =
            userNameValue.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val emailAddressRequest: RequestBody =
            emailAddressValue.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        var sendAboutValue=""
        if (aboutValue.get().toString().isEmpty()) {
            sendAboutValue=" "
        }else
        {
            sendAboutValue= aboutValue.get().toString()
        }
        val aboutRequest: RequestBody = sendAboutValue.toRequestBody("text/plain".toMediaTypeOrNull())

        val number = phoneNumberValue.get().toString().replace(" ", "")
        var phoneNumberRequest: RequestBody? = null
        var countryCodeRequest: RequestBody? = null
        if (number.toString().trim().isNotEmpty()) {
            countryCodeRequest =
                countryCodeValue.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        } else
        {
            var countryLocalValue=""
            countryCodeRequest =
                countryLocalValue.toRequestBody("text/plain".toMediaTypeOrNull())
        }
        phoneNumberRequest = number.toString().toRequestBody("text/plain".toMediaTypeOrNull())
      /*  val buffer = Buffer()
        buffer.writeInt(number)
        val phoneNumberRequest: RequestBody =
            buffer.readByteString().toRequestBody("application/octet-stream".toMediaTypeOrNull())*/

     //   photoFile
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
            requestProcessor = object : ApiProcessor<Response<EditProfileResponse>> {
                override fun onSuccess(success: Response<EditProfileResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    if(checkDeviceType()){
                        dismissDialogData.value = true
                    }else
                    {
                        if (view.findNavController().currentDestination?.id == R.id.editProfileFragment) {
                            view.findNavController().popBackStack()
                        }
                    }


                    /*   var bundle= Bundle()
                       bundle.putString("email",emailFieldValue.get())
                       view.findNavController().navigate(R.id.action_signInFragment_to_signUpCodeFragment,bundle)*/
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<EditProfileResponse> {
                    if(photoFile!=null) {
                        return retrofitApi.editProfileForPhotoAPI(userToken,
                            nameRequest,
                            userNameRequest,
                            emailAddressRequest,
                            phoneNumberRequest!!,
                            countryCodeRequest!!,
                            aboutRequest,photoBody!!)
                    }else
                    {
                        return retrofitApi.editProfileForAPI(userToken,
                            nameRequest,
                            userNameRequest,
                            emailAddressRequest,
                            phoneNumberRequest!!,
                            countryCodeRequest!!,
                            aboutRequest)
                    }
                }
            })
    }

    fun AfterTextChanged(s: Editable) {

        /*   if(phoneNumberValue.get().toString().length==4 || phoneNumberValue.get().toString().length==10)
           {
               phoneNumberValue.set(phoneNumberValue.get().toString()+" ")
           }*/
        /*if (emailFieldValue.get().toString().trim().length>0 && isValidEmail(emailFieldValue.get().toString())) {
            emailCorrectValue.set(true)
        } else {
            emailCorrectValue.set(false)
        }*/
    }

    fun checkValidation(): Boolean {
        if (nameValue.get().toString().trim().isEmpty()) {
            errorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                .getString(R.string.please_enter_name_text), 1)
            // showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.please_enter_name_text))
            return false
        } else
            if (userNameValue.get().toString().trim().isEmpty()) {
                errorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                    .getString(R.string.please_enter_user_name_text), 2)

                //  showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.please_enter_user_name_text))
                return false
            } else
                if (emailAddressValue.get().toString().trim().isEmpty()) {
                    errorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                        .getString(R.string.please_enter_email_text), 3)

                    //     showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.please_enter_email_text))
                    return false
                } else
                    if (!(isValidEmail(emailAddressValue.get().toString().trim()))) {
                        errorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                            .getString(R.string.please_enter_valid_email_text), 3)

                        // showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.please_enter_valid_email_text))
                        return false
                    } else
                        if (phoneNumberValue.get().toString().trim().length in 1 ..9) {
                            phoneErrorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                                .getString(R.string.please_enter_correct_phone_text), 4)

                            //   showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.please_enter_phone_text))
                            return false
                        } /*else
                            if (aboutValue.get().toString().trim().isEmpty()) {
                                errorData.value = SignInErrorData(MainActivity.activity!!.get()!!
                                    .getString(R.string.please_enter_yourself_text), 5)

                                //  showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.please_enter_yourself_text))
                                return false
                            }*/
        return true
    }

    fun nameTextChange(nameText: CharSequence?, start: Int, before: Int, count: Int) {
        if (nameText.toString().trim().length == 1) {
            errorData.value = SignInErrorData("", 1)
        }
    }

    fun userNameTextChange(userNameText: CharSequence?, start: Int, before: Int, count: Int) {
        if (userNameText.toString().trim().length == 1) {
            errorData.value = SignInErrorData("", 2)
        }
    }

    fun emailTextChange(nameEmail: CharSequence?, start: Int, before: Int, count: Int) {
        if (isValidEmail(nameEmail.toString().trim())) {
            errorData.value = SignInErrorData("", 3)
        }
    }

    fun aboutTextChange(aboutText: CharSequence?, start: Int, before: Int, count: Int) {
        if (aboutText!!.trim().length == 1) {
            errorData.value = SignInErrorData("", 5)
        }
    }
}