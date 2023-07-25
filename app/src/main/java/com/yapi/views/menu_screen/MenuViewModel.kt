package com.yapi.views.menu_screen

import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.gson.JsonObject
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.*
import com.yapi.pref.PreferenceFile
import com.yapi.views.profile.ProfileData
import com.yapi.views.profile.ProfileFragment
import com.yapi.views.profile.ProfileResponse
import com.yapi.views.search.SearchFragment
import com.yapi.views.signup_code.LoginUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MenuViewModel @Inject constructor(val preferenceFile: PreferenceFile,
val repository: Repository,@Named("token") val userToken:String) : ViewModel() {

    var groupData=MutableLiveData<AllData>()
    var screenWidth: Int? = 0
    var openProfileScreenData = MutableLiveData<Boolean>()
    var openSearchScreenData = MutableLiveData<Boolean>()

    var noImageOnlyNameVisible=ObservableBoolean(false)

  //  var noImageOnlyNameVisible = MutableLiveData<Boolean>()
    var showTopNameTag=ObservableField("")

    var userPhotoUrl=ObservableField("")

    var loginUserData: LoginUserData?=null
    fun onClick(view: View) {
        when (view.id) {
            R.id.layoutSearch,R.id.etSearchMenu->{
                if(checkDeviceType()){
                    openSearchScreenData.value=true
                    //SearchFragment.newInstanceSearch("").showNow(view.re, " SimpleDialog.TAG")
                }else
                {
                if (view.findNavController().currentDestination?.id == R.id.menuFragment) {
                    view.findNavController()
                        .navigate(R.id.action_menuFragment_to_searchFragment)
                }
            }
            }

            com.yapi.R.id.imgProfilePicCustomerList,R.id.relNameValue -> {
                val mView: View = LayoutInflater.from(MainActivity.activity!!.get())
                    .inflate(com.yapi.R.layout.menu_popup_options, null, false)
                var newWidth=0.0

                if(checkDeviceType()){
                    newWidth =  LinearLayout.LayoutParams.WRAP_CONTENT.toDouble()
                }else
                {
                     newWidth = screenWidth!! / 1.5
                }

                //   val popUp = PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false)
                val popUp = PopupWindow(mView,
                    newWidth.toInt(),
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    false)
                // popUp.showAtLocation(mView, Gravity.RIGHT,0,0);
                popUp.isTouchable = true
                popUp.isFocusable = true
                popUp.isOutsideTouchable = true

                if(view.id==R.id.imgProfilePicCustomerList){
                    popUp.showAsDropDown(view.findViewById(com.yapi.R.id.imgProfilePicCustomerList))
                }else
                {
                    popUp.showAsDropDown(view.findViewById(com.yapi.R.id.relNameValue))
                }

                var constraintsProfile =
                    mView.findViewById<ConstraintLayout>(R.id.constraintsProfile)
                constraintsProfile.setOnClickListener {
                    popUp.dismiss()
                    if (checkDeviceType()) {
                        openProfileScreenData.value = true
                    } else {
                        if (view.findNavController().currentDestination?.id == R.id.menuFragment) {
                            view.findNavController()
                                .navigate(R.id.action_menuFragment_to_profileFragment)
                        }
                    }
                }
                var constraintsSettings =
                    mView.findViewById<ConstraintLayout>(R.id.constraintsSettings)
                constraintsSettings.setOnClickListener {
                    popUp.dismiss()
                    //  showLeaveGroupDialog()
                    // showDeleteGroupDialog()
                    /*   if (view.findNavController().currentDestination?.id == R.id.menuFragment) {
                           view.findNavController()
                               .navigate(R.id.action_menuFragment_to_profileFragment)
                       }*/
                }
                var constraintsLogout = mView.findViewById<ConstraintLayout>(R.id.constraintsLogout)
                constraintsLogout.setOnClickListener {
                    popUp.dismiss()
                   // preferenceFile.saveStringValue(Constants.USER_ID, "")
                    preferenceFile.clearAllPref()
                    if(checkDeviceType()){
                        var intent=Intent(MainActivity.activity!!.get(),MainActivity::class.java)
                        MainActivity.activity!!.get()!!.startActivity(intent)
                    }else
                    {
                        view.findNavController()
                            .navigate(R.id.action_menuFragment_to_signInFragment)
                    }

                    /*  if (view.findNavController().currentDestination?.id == R.id.menuFragment) {
                          view.findNavController()
                              .navigate(R.id.action_menuFragment_to_chatMessageFragment)
                      }*/
                }


//                       if (view.findNavController().currentDestination?.id == R.id.menuFragment) {
//                           view.findNavController().navigate(R.id.leaveGroupFragment)
//                       }
//                   val dialog=Dialog(MainActivity.activity!!.get()!!)

//                   dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
//                   dialog.setContentView(R.layout.delete_profile_popup)

//                   dialog.show()
//                   var cardviewDeleteProfile=dialog.findViewById<CardView>(R.id.cardviewDeleteProfile)
//                   cardviewDeleteProfile.layoutParams.width=(screenWidth!!.toDouble()/1.1).toInt()

            }
            /*R.id.layoutSearch,R.id.etSearchMenu->{
                if(checkDeviceType()){

                }else
                {
                if (view.findNavController().currentDestination?.id == R.id.menuFragment) {
                    view.findNavController()
                        .navigate(R.id.action_menuFragment_to_searchFragment)
                }
            }}*/

            /*  R.id.layoutAddNewGroupsMenu-> {
                 *//* if (view.findNavController().currentDestination?.id == R.id.menuFragment) {

                 view.findNavController()
                     .navigate(R.id.action_menuFragment_to_createGroupFragment)
             }*//*
         }*/
        }
    }

    fun showLeaveGroupDialog() {
        var dialog = Dialog(MainActivity.activity!!.get()!!)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.leave_module_popup)

        dialog.show()
        var cardviewDeleteProfile = dialog.findViewById<CardView>(R.id.cardviewDeleteProfile)
        cardviewDeleteProfile.layoutParams.width = (screenWidth!!.toDouble() / 1.1).toInt()
    }

    fun showDeleteGroupDialog() {
        var dialog = Dialog(MainActivity.activity!!.get()!!)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.delete_group_popup)

        dialog.show()
        var cardviewDeleteProfile = dialog.findViewById<CardView>(R.id.cardviewDeleteProfile)
        cardviewDeleteProfile.layoutParams.width = (screenWidth!!.toDouble() / 1.1).toInt()
    }


    fun fetchGroupDataMethod():LiveData<AllData> {
        Log.e("Token111====", preferenceFile.fetchStringValue(Constants.USER_TOKEN))
        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<GroupMenuResponse>> {
                override fun onSuccess(success: Response<GroupMenuResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())

                    groupData.value=success.body()!!.data

                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<GroupMenuResponse> {
                    return retrofitApi.fetchAllGroupData(userToken)
                }
            })
        return groupData
    }
}