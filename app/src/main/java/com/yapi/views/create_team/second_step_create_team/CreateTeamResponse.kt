package com.yapi.views.create_team.second_step_create_team

data class CreateTeamResponse(
    val `data`: Data,
    val message: String,
    val status: Int
)

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val image: String,
    val image_url: String,
    val invitaions: List<Any>,
    val is_private: Boolean,
    val name: String,
    val quick_join: Boolean,
    val role: String,
    val channel_name: String,
    val updatedAt: String,
    val user_id: String,
    val working: String
)
