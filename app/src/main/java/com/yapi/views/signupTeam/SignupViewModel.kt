package com.yapi.views.signupTeam

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.gson.JsonObject
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.*
import com.yapi.views.profile.ProfileResponse
import dagger.hilt.EntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignupViewModel  @Inject constructor(val repository: Repository,@Named("token") val userToken:String):ViewModel() {

    var viewTemlListResponse=MutableLiveData<ArrayList<ViewInvitationData>>()
    var acceptInvitationResponse=MutableLiveData<Boolean>()

    var loginEmailMessage=ObservableField("")

    fun onClick(view: View){
        when(view.id){
            R.id.craeteTeamBtn ->{
                if(view.findNavController().currentDestination?.id==R.id.signupTeam) {
                    view.findNavController().navigate(R.id.action_signupTeam_to_firstStepCreateTeam)
                }
            }
        }
    }

    fun fetchViewInvitationMethod()
    {
        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<ViewInvitationResponse>> {
                override fun onSuccess(success: Response<ViewInvitationResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    viewTemlListResponse.value=success.body()!!.data
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<ViewInvitationResponse> {
                //    Log.e("mflfldddff==",preferenceFile.fetchStringValue(Constants.LOGIN_USER_ID))
                    return retrofitApi.viewTeamInvitationAPI(userToken)
                }
            })
    }


    fun acceptInvitationMethod(teamId: String):LiveData<Boolean>
    {
        var jsonObject=JsonObject()
        jsonObject.addProperty("team_id",teamId)
        jsonObject.addProperty("status","accepted")
        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<JsonObject>> {
                override fun onSuccess(success: Response<JsonObject>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    acceptInvitationResponse.value=true
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<JsonObject> {
                    //    Log.e("mflfldddff==",preferenceFile.fetchStringValue(Constants.LOGIN_USER_ID))
                    return retrofitApi.acceptTeamInvitationAPI(userToken,jsonObject)
                }
            })
        return acceptInvitationResponse
    }
}