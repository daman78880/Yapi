package com.yapi.views.signup_code

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.*
import com.yapi.pref.PreferenceFile
import com.yapi.views.sign_in.SignInErrorData
import com.yapi.views.sign_in.SignInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import org.greenrobot.eventbus.EventBus
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignupCodeViewModel @Inject constructor(private var preferenceFile: PreferenceFile,private val repository: Repository) : ViewModel() {
var email:String?=""
    var otpValue=ObservableField<String>()
var errorData=MutableLiveData<SignInErrorData>()
    fun onClick(view: View) {
        when (view.id) {
            R.id.linearTopSignupCode, R.id.constraintsTopSignupCode -> {
                MainActivity.activity!!.get()!!.hideKeyboard()
            }
            R.id.btnSignUpCode->{
                if(view.findNavController().currentDestination?.id==R.id.signUpCodeFragment) {
                    if(otpValue.get().toString().isEmpty() || otpValue.get().toString() == "null" || otpValue.get().toString() == null){
                        errorData.value=SignInErrorData(MainActivity.activity!!.get()!!.getString(R.string.enter_otp),0)
                    }else
                    if(otpValue.get().toString().length==6){
                        if(Constants.API_CALL_DEMO) {
                            verifyOTPAPIMethod(view)
                        }else
                        {
                            errorData.value= SignInErrorData("",0)
                            preferenceFile.saveStringValue("login_email",email.toString())
                            view.findNavController().navigate(R.id.action_signUpCodeFragment_to_signupTeam)
                        }
                    }else
                    {
                        errorData.value=SignInErrorData(MainActivity.activity!!.get()!!.getString(R.string.enter_correct_otp),0)
                     //   showToastMessage(MainActivity.activity!!.get()!!.getString(R.string.enter_correct_otp))
                    }
                }
            }
        }
    }

    fun verifyOTPAPIMethod(view:View)
    {
        var jsonObject=JsonObject()
        jsonObject.addProperty("email",email.toString())
        jsonObject.addProperty("otp",otpValue.get().toString())

        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<VerifyOTPResponse>> {
                override fun onSuccess(success: Response<VerifyOTPResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    preferenceFile.saveStringValue(Constants.USER_TOKEN,"Bearer "+success.body()!!.token.toString())
                    Log.e("mflfldddff16666==",success.body()!!.token.toString())
                    preferenceFile.saveStringValue(Constants.LOGIN_USER_ID,success.body()!!.data._id.toString())

                    Log.e("mflfldddff33==",preferenceFile.fetchStringValue(Constants.LOGIN_USER_ID))
                    errorData.value= SignInErrorData("",0)
                    preferenceFile.saveStringValue("login_email",email.toString())
                    preferenceFile.saveBooleanValue(Constants.USER_PROFILE_CREATED,success.body()!!.data.profile_created!!)

                    preferenceFile.saveStringValue(Constants.USER_ALL_DATA,Gson().toJson(success.body()!!.data))

                    if(success.body()!!.data.profile_created!!){
                        if(checkDeviceType())
                        {
                            EventBus.getDefault()
                                .post(MyMessageEvent(1, Constants.MENU_KEY)) //post event
                        }else
                        {
                            view.findNavController().navigate(R.id.action_signUpCodeFragment_to_menuFragment)
                        }
                    }else
                    {
                        view.findNavController().navigate(R.id.action_signUpCodeFragment_to_signupTeam)
                    }
                }

                override fun onError(message: String) {
                    //MainActivity.activity!!.get()!!.showMessage(message)
                    errorData.value= SignInErrorData(message,0)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<VerifyOTPResponse> {
                    return retrofitApi.verifyOTPAPI(jsonObject)
                }
            })
    }
}