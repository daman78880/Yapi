package com.yapi.views.chat.chatGroupInfo

data class ChatGroupInfoResponse(
    val `data`: GroupInfoData,
    val message: String,
    val status: Int
)

data class GroupInfoData(
    val __v: Int,
    val _id: String,
    val channel_name: String,
    val createdAt: String,
    val deleted: Boolean,
    val description: String,
    val image: String,
    val image_url: String,
    val invitaions: ArrayList<GroupInfoInvitaion>,
    val is_private: Boolean,
    val memberCount: Int,
    val name: String,
    val owner_leave: Boolean,
    val quick_join: Boolean,
    val role: String,
    val updatedAt: String,
    val userData: ArrayList<GroupUserData>,
    val user_id: String,
    val working: String
)

data class GroupInfoInvitaion(
    val _id: String,
    val role: String,
    val status: String,
    val user_email: String
)

data class GroupUserData(
    val __v: Int,
    val _id: String,
    val about: String,
    val blocked: Boolean,
    val country_code: String,
    val createdAt: String,
    val deactivated: Boolean,
    val deleted: Boolean,
    val email: String,
    val email_otp: String,
    val email_otp_verified: Boolean,
    val mobile_number: Long,
    val mobile_otp: String,
    val mobile_otp_verified: Boolean,
    val name: String,
    val profile_created: Boolean,
    val profile_pic: String,
    val profile_pic_url: String,
    val updatedAt: String,
    val user_name: String
)