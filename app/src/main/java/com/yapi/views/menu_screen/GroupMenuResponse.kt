package com.yapi.views.menu_screen

data class GroupMenuResponse(
    val `data`: AllData,
    val message: String,
    val status: Int
)

data class AllData(
    val groups: ArrayList<GroupData>
)

data class GroupData(
    val __v: Int,
    val _id: String,
    var channel_name: String?="",
    val createdAt: String,
    val description: String,
    var image: String?="",
    var image_url: String?="",
    val invitaions: ArrayList<GroupInvitaion>,
    val is_private: Boolean,
    val name: String,
    val quick_join: Boolean,
    val role: String,
    val updatedAt: String,
    val user_id: String,
    val working: String,
    var selected: Boolean?=false,
):java.io.Serializable

data class GroupInvitaion(
    val _id: String,
    val role: String,
    val status: String,
    val user_email: String
)