package com.yapi.common

import com.google.gson.JsonObject
import com.yapi.views.add_people_email.AddEmailResponse
import com.yapi.views.add_people_email.CheckEmailResponse
import com.yapi.views.chat.GroupDeleteResponse
import com.yapi.views.chat.chatGroupInfo.ChatGroupInfoResponse
import com.yapi.views.create_team.second_step_create_team.CreateTeamResponse
import com.yapi.views.edit_profile.EditProfileResponse
import com.yapi.views.menu_screen.GroupMenuResponse
import com.yapi.views.profile.ProfileResponse
import com.yapi.views.sign_in.SignInResponse
import com.yapi.views.signupTeam.ViewInvitationResponse
import com.yapi.views.signup_code.VerifyOTPResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitAPI {
    @GET("getCategory")
    suspend fun getCategory(
        @Header("Authorization") token: String,
        @Query("lat") lat: String,
        @Query("long") lng: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String,
        @Query("search") search: String,
    ): Response<GetCategorryResponse>

    @POST(WebAPIKeys.LOGIN_URL)
    suspend fun loginAPI(@Body emailData:JsonObject): Response<SignInResponse>

    @POST(WebAPIKeys.VERIFY_OTP_URL)
    suspend fun verifyOTPAPI(@Body emailData:JsonObject)
    : Response<VerifyOTPResponse>

    @Multipart
    @POST(WebAPIKeys.USER_EDIT_PROFILE)
    suspend fun editProfileAPI(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("about") about: RequestBody
    ): Response<EditProfileResponse>

    @Multipart
    @POST(WebAPIKeys.USER_EDIT_PROFILE)
    suspend fun editProfileForPhotoAPI(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("about") about: RequestBody,
        @Part photoBody: MultipartBody.Part,
    ): Response<EditProfileResponse>


    @Multipart
    @POST(WebAPIKeys.USER_EDIT_PROFILE)
    suspend fun editProfileForAPI(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("about") about: RequestBody
    ): Response<EditProfileResponse>


    @GET(WebAPIKeys.USER_FETCH_PROFILE+"/{user_id}")
    suspend fun fetchProfileAPI(@Header ("Authorization") token:String,@Path("user_id") user_id:String)
            : Response<ProfileResponse>

    @Multipart
    @POST(WebAPIKeys.GROUP_CREATE_TEAM)
    suspend fun createTeamAPI(@Header ("Authorization") token:String,
        @Part("name") name:RequestBody,
        @Part("working") working:RequestBody,
        @Part("description") description:RequestBody,
        @Part("is_private") is_private:RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("quick_join") quick_join:RequestBody,
    ): Response<CreateTeamResponse>

    @Multipart
    @POST(WebAPIKeys.GROUP_CREATE_TEAM)
    suspend fun createTeamWithoutImageAPI(@Header ("Authorization") token:String,
                              @Part("name") name:RequestBody,
                              @Part("working") working:RequestBody,
                              @Part("description") description:RequestBody,
                              @Part("is_private") is_private:RequestBody,
                              @Part("quick_join") quick_join:RequestBody,
    ): Response<CreateTeamResponse>


    @POST(WebAPIKeys.ADD_TEAM_MEMBERS)
    suspend fun addTeamMembersAPI(@Header ("Authorization") token:String,@Body emailData:JsonObject): Response<AddEmailResponse>

    @GET(WebAPIKeys.VIEW_TEAM_INVITATION)
    suspend fun viewTeamInvitationAPI(@Header ("Authorization") token:String): Response<ViewInvitationResponse>

    @POST(WebAPIKeys.ACCEPT_TEAM_INVITATION)
    suspend fun acceptTeamInvitationAPI(
        @Header ("Authorization") token:String,
        @Body jsonObject: JsonObject): Response<JsonObject>

    @GET(WebAPIKeys.FETCH_ALL_MENU_DATA)
    suspend fun fetchAllGroupData(@Header("Authorization") token:String): Response<GroupMenuResponse>

    @POST(WebAPIKeys.USER_CHECK_EMAIL)
    suspend fun checkUserEmailAPI(/*@Header ("Authorization") token:String,*/@Body emailData:JsonObject): Response<CheckEmailResponse>


    @DELETE(WebAPIKeys.DELETE_GROUP+"/{team_id}")
    suspend fun deleteGroupAPI(@Header("Authorization") token:String,@Path("team_id") team_id:String): Response<GroupDeleteResponse>

    @DELETE(WebAPIKeys.DELETE_ACCOUNT)
    suspend fun deleteAccountAPI(@Header("Authorization") token:String): Response<GroupDeleteResponse>


    @POST(WebAPIKeys.DEACTIVATE_ACCOUNT)
    suspend fun deactivateAccountAPI(@Header("Authorization") token:String): Response<GroupDeleteResponse>


    @GET(WebAPIKeys.TEAM_DETAIL_API+"/{team_id}")
    suspend fun fetchTeamOrGroupDetailAPI(@Header ("Authorization") token:String,@Path("team_id") team_id:String)
            : Response<ChatGroupInfoResponse>

    @POST(WebAPIKeys.LEAVE_GROUP_API)
    suspend fun leaveGroupAPI(@Header("Authorization") token:String,@Body jsonObject: JsonObject): Response<JsonObject>
}