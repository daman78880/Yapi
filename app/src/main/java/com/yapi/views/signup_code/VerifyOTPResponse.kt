package com.yapi.views.signup_code

data class VerifyOTPResponse(
    val `data`: LoginUserData,
    val message: String,
    val status: Int,
    val token: String
)

data class LoginUserData(
    val __v: Int?=0,
    val _id: String?="",
    val about: String?="",
    val blocked: Boolean?=false,
    val country_code: String?="",
    val createdAt: String?="",
    val deactivated: Boolean?=false,
    val deleted: Boolean?=false,
    val email: String?="",
    val email_otp: String?="",
    val email_otp_verified: Boolean?=false,
    val mobile_number: Any?=null,
    val mobile_otp: String?="",
    val mobile_otp_verified: Boolean?=false,
    val name: String?="",
    val profile_pic: String?="",
    val profile_pic_url: String?="",
    val updatedAt: String?="",
    val user_name: String?="",
    var profile_created: Boolean?=false,
)