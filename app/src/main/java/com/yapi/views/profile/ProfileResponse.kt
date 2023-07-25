package com.yapi.views.profile

data class ProfileResponse(
    val `data`: ProfileData,
    val message: String,
    val status: Int
)

data class ProfileData(
    var __v: Int?=0,
    var _id: String?="",
    var about: String?="",
    var blocked: Boolean?=false,
    var country_code: String?="",
    var createdAt: String?="",
    var deactivated: Boolean?=false,
    var deleted: Boolean?=false,
    var email: String?="",
    var email_otp: String?="",
    var email_otp_verified: Boolean?=false,
    var mobile_number: Long?=0,
    var mobile_otp: String?="",
    var mobile_otp_verified: Boolean?=false,
    var name: String?="",
    var profile_pic: String?="",
    var profile_pic_url: String?="",
    var updatedAt: String?="",
    var user_name: String?=""
):java.io.Serializable