package com.yapi.views.signupTeam

data class ViewInvitationResponse(
    val `data`: ArrayList<ViewInvitationData>,
    val message: String,
    val status: Int
)

data class ViewInvitationData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val image: String,
    val image_url: String,
    val invitaions: ArrayList<Invitaion>,
    val is_private: Boolean,
    val memberCount: Int,
    val name: String,
    val quick_join: Boolean,
    val role: String,
    val updatedAt: String,
    val user_id: String,
    val working: String
)

data class Invitaion(
    val _id: String,
    val role: String,
    val status: String,
    val user_email: String
)