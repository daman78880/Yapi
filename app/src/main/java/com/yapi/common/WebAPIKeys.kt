package com.yapi.common

object WebAPIKeys {
    //Local Server
  //const val BASEURL:String="http://192.168.1.113:3000/"

    //Live Server
    const val BASEURL:String="http://3.15.150.5:3000/"
    const val LOGIN_URL="user/sign-up"
    const val VERIFY_OTP_URL="user/verify-otp"
    const val USER_EDIT_PROFILE="user/edit-user"
    const val USER_FETCH_PROFILE="user/view-user-detail"
    const val GROUP_CREATE_TEAM="group/create-team"
    const val ADD_TEAM_MEMBERS="group/add-team-members"
    const val VIEW_TEAM_INVITATION="group/view-team-invitation"
    const val ACCEPT_TEAM_INVITATION="group/update-team-invite"

    const val FETCH_ALL_MENU_DATA="group/view-all-user-data"
    const val USER_CHECK_EMAIL="user/check-email"
    const val DELETE_GROUP="group/delete-group"
    const val DELETE_ACCOUNT="user/delete-account"
    const val DEACTIVATE_ACCOUNT="user/deactivate-account"
    const val TEAM_DETAIL_API="group/view-team-detail"
    const val LEAVE_GROUP_API="group/leave-group"
}