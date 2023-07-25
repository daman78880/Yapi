package com.yapi.views.add_people_email

data class AddEmailResponse(
    val `data`: EmailData?=null,
    val message: String,
    val status: Int
)

data class EmailData(
    var __v: Int?=0,
    var _id: String?="",
    var createdAt: String?="",
    var description: String?="",
    var image: String?="",
    var image_url: String?="",
    var invitaions: ArrayList<Invitaion>?=null,
    var is_private: Boolean?=false,
    var name: String?="",
    var quick_join: Boolean?=false,
    var role: String?="",
    var updatedAt: String?="",
    var user_id: String?="",
    var working: String?=""
):java.io.Serializable

data class Invitaion(
    var _id: String?="",
    var role: String?="",
    var status: String?="",
    var user_email: String?=""
)