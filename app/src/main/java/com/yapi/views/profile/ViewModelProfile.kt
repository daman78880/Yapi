package com.yapi.views.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.*
import com.yapi.pref.PreferenceFile
import com.yapi.views.chat.GroupDeleteResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ViewModelProfile @Inject constructor(
    val repository: Repository,
    val preferenceFile: PreferenceFile, @Named("token") val userToken: String,
) : ViewModel() {
    private var profileData: ProfileData? = null
    var openEditProfileData = MutableLiveData<ProfileData?>()
    var dismissDialogData = MutableLiveData<Boolean>()

    var nameValue = ObservableField("")
    var userNameValue = ObservableField("")

    var aboutValue = ObservableField("")
    var aboutTitle = ObservableField("")
    var aboutVisiblityValue = ObservableBoolean(false)

    var roleVisiblityValue = ObservableBoolean(false)
    var roleTitle = ObservableField("")

    var regionVisiblityValue = ObservableBoolean(false)
    var regionTitle = ObservableField("")

    var emailValue = ObservableField("")
    var emailVisiblityValue = ObservableBoolean(false)

    var phoneValue = ObservableField("")
    var phoneTitle = ObservableField("")
    var phoneVisiblityValue = ObservableBoolean(false)

    var phoneCountryValue = ObservableField("")
    var showTopNameTag = ObservableField("")
    var photoUrl = ObservableField("")

    var topProfileVisibility = ObservableBoolean(false)
    var noImageOnlyNameVisible = ObservableBoolean(false)

    var screenWidth: Int? = 0

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnEditProfile -> {
                gotoEditProfileScreen(view)
            }
            R.id.layoutDeleteAccountProfile -> {
                deleteAccountDialog(view)
            }
            R.id.layoutDeActiveProfile -> {
                deActiveAccountDialog(view)
            }
            R.id.imgCancelProfile, R.id.ivOutsideCloseProfile -> {
                if (checkDeviceType()) {
                    dismissDialogData.value = true
                } else {
                    if (view.findNavController().currentDestination?.id == R.id.profileFragment) {
                        view.findNavController().popBackStack()
                    }
                }
            }
            R.id.layoutProfile, R.id.layoutScrollViewProfile -> {
                //for hide keyboard
                MainActivity.activity!!.get()!!.hideKeyboard()
            }
            R.id.txtTempAboutLiteGreyProfile->{
                if(!aboutVisiblityValue.get()) {
                    gotoEditProfileScreen(view)
                }
            }
            R.id.txtTempRoleLiteGreyProfile->{
                if(!roleVisiblityValue.get()) {
                    gotoEditProfileScreen(view)
                }
            }
            R.id.txtTempPhoneLiteGreyProfile->{
                if(!phoneVisiblityValue.get()) {
                    gotoEditProfileScreen(view)
                }
            }
            R.id.txtTempRegionLiteGreyProfile->{
                if(!regionVisiblityValue.get()) {
                    gotoEditProfileScreen(view)
                }
            }
        }
    }

    private fun gotoEditProfileScreen(view:View)
    {
        var bundle = Bundle()
        if (profileData != null) {
            bundle.putSerializable("profile_data", profileData)
        }

        if (checkDeviceType()) {
            openEditProfileData.value = profileData ?: ProfileData()
        } else {
            if (view.findNavController().currentDestination?.id == R.id.profileFragment) {
                view.findNavController()
                    .navigate(R.id.action_profileFragment_to_editProfileFragment, bundle)
            }
        }
    }

    private fun deleteAccountDialog(screenView: View) {
        val dialog = Dialog(MainActivity.activity!!.get()!!)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.delete_profile_popup)
//        dialog.window?.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        val cancelBtn = dialog.findViewById<AppCompatButton>(R.id.btnProfileCancel)
        val deleteBtn = dialog.findViewById<AppCompatButton>(R.id.btnProfileDelete)
        val ivCross = dialog.findViewById<AppCompatImageView>(R.id.ivCross)

        ivCross.setOnClickListener {
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        deleteBtn.setOnClickListener {
            dialog.dismiss()
           deleteAccountMethod(screenView)
        }
        dialog.setCancelable(false)
        dialog.show()

        var cardviewDeleteProfile = dialog.findViewById<CardView>(R.id.cardviewDeleteProfile)

        var second_frame_height = preferenceFile.fetchStringValue("second_frame_height")
        var second_frame_width = preferenceFile.fetchStringValue("second_frame_width")

        if (checkDeviceType()) {
            cardviewDeleteProfile.layoutParams.width =
                (second_frame_width.toDouble() / 1.1).toInt()
            cardviewDeleteProfile.layoutParams.height =
                (second_frame_height.toDouble() / 1.1).toInt()
        } else {
            cardviewDeleteProfile.layoutParams.width = (screenWidth!!.toDouble() / 1.1).toInt()
        }
    }

    private fun deActiveAccountDialog(view: View) {
        val dialog = Dialog(MainActivity.activity!!.get()!!)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.dialog_de_activie_profile)
//        dialog.window?.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        val cancelBtn = dialog.findViewById<AppCompatButton>(R.id.btnCancelDeActiveDialog)
        val deActivateBtn = dialog.findViewById<AppCompatButton>(R.id.btnDeActiveDialog)

        val ivCrossDeactivate = dialog.findViewById<AppCompatImageView>(R.id.ivCrossDeactivate)

        ivCrossDeactivate.setOnClickListener {
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        deActivateBtn.setOnClickListener {
            dialog.dismiss()
            deactivateAccountMethod(view)
        }

        dialog.setCancelable(false)
        dialog.show()

        var cardViewDeActiveProfile = dialog.findViewById<CardView>(R.id.cardViewDeActiveProfile)
        var second_frame_height = preferenceFile.fetchStringValue("second_frame_height")
        var second_frame_width = preferenceFile.fetchStringValue("second_frame_width")

        if (checkDeviceType()) {
            cardViewDeActiveProfile.layoutParams.width =
                (second_frame_width.toDouble() / 1.1).toInt()
            cardViewDeActiveProfile.layoutParams.height =
                (second_frame_height.toDouble() / 1.1).toInt()
        } else {
            cardViewDeActiveProfile.layoutParams.width =
                (screenWidth!!.toDouble() / 1.1).toInt()
        }
    }

    fun fetchProfileData() {
        Log.e("Token111====", preferenceFile.fetchStringValue(Constants.USER_TOKEN))
        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<ProfileResponse>> {
                override fun onSuccess(success: Response<ProfileResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())
                    profileData = success.body()!!.data
                    preferenceFile.saveStringValue(Constants.USER_ALL_DATA, Gson().toJson(profileData))

                    topProfileVisibility.set(true)
                    nameValue.set(success.body()!!.data.name)
                    userNameValue.set("@" + success.body()!!.data.user_name)
                    emailValue.set(success.body()!!.data.email)

if(emailValue.get().toString().isEmpty())
{
    emailVisiblityValue.set(false)
}else
{
    emailVisiblityValue.set(true)
}

                    //showTopNameTag.set(success.body()!!.data.name!!.substring(0, 1).uppercase(Locale.getDefault()))
                    showTopNameTag.set(convertFromFullNameToTwoString(success.body()!!.data.name!!))

                    if (success.body()!!.data.profile_pic_url != "") {
                        noImageOnlyNameVisible.set(false)
                        photoUrl.set(success.body()!!.data.profile_pic_url)
                    } else {
                        noImageOnlyNameVisible.set(true)
                    }

                    aboutValue.set(success.body()!!.data.about.toString().trim())
                    if (success.body()!!.data.about!!.trim().isEmpty()) {
                        aboutVisiblityValue.set(false)
                        aboutTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.add_about_title_text))
                    } else {
                        aboutVisiblityValue.set(true)
                        aboutTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.about))
                    }

                    if (!(success.body()!!.data.mobile_number.toString().equals(""))
                        && success.body()!!.data.mobile_number.toString() != null
                        && !(success.body()!!.data.mobile_number.toString().equals("null"))
                    ) {
                        var phoneNumber =
                            addSpaceBetweenPhoneMethod(success.body()!!.data.mobile_number.toString())
                        phoneValue.set(success.body()!!.data.country_code.toString() + " " + phoneNumber)
                        phoneVisiblityValue.set(true)
                        phoneTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.phone_textt))
                    } else {
                        phoneTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.add_phone_title_text))
                        phoneVisiblityValue.set(false)
                    }
                    roleVisiblityValue.set(false)
                    roleTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.add_role_title_text))
                 //   roleTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.role))


                    regionVisiblityValue.set(false)
                    regionTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.add_region_title_text))
                    //regionTitle.set(MainActivity.activity!!.get()!!.resources.getString(R.string.region))


                    //phoneCountryValue.set(success.body()!!.data.country_code.toString())
                    /*   var bundle= Bundle()
                       bundle.putString("email",emailFieldValue.get())
                       view.findNavController().navigate(R.id.action_signInFragment_to_signUpCodeFragment,bundle)*/
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<ProfileResponse> {
                    Log.e("mflfldddff==", preferenceFile.fetchStringValue(Constants.LOGIN_USER_ID))
                    return retrofitApi.fetchProfileAPI(userToken,
                        preferenceFile.fetchStringValue(Constants.LOGIN_USER_ID))
                }
            })
    }


    fun deleteAccountMethod(screenView:View) {

        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<GroupDeleteResponse>> {
                override fun onSuccess(success: Response<GroupDeleteResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())
                    clearAllDataMethod(screenView)
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<GroupDeleteResponse> {
                    return retrofitApi.deleteAccountAPI(userToken)
                }
            })
    }

    fun clearAllDataMethod(screenView:View)
    {
        preferenceFile.clearAllPref()
        if(checkDeviceType()){
            var intent= Intent(MainActivity.activity!!.get(),MainActivity::class.java)
            MainActivity.activity!!.get()!!.startActivity(intent)
        }else
        {
            if (screenView.findNavController().currentDestination?.id == R.id.profileFragment) {
                screenView.findNavController()
                    .navigate(R.id.action_profileFragment_to_signInFragment)
            }
        }
    }

    fun deactivateAccountMethod(screenView:View) {

        repository.makeCall(true,
            requestProcessor = object : ApiProcessor<Response<GroupDeleteResponse>> {
                override fun onSuccess(success: Response<GroupDeleteResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())
                    clearAllDataMethod(screenView)
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<GroupDeleteResponse> {
                    return retrofitApi.deactivateAccountAPI(userToken)
                }
            })
    }
}