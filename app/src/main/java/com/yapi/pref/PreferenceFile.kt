package com.yapi.pref

import android.content.SharedPreferences
import com.google.gson.Gson
import com.yapi.common.Constants
import com.yapi.views.signup_code.LoginUserData
import javax.inject.Inject
import javax.inject.Singleton

class PreferenceFile @Inject constructor(
     val editor: SharedPreferences.Editor,
     val pref: SharedPreferences,
) {
    fun saveStringValue(key: String, value: String) {
        editor.putString(key, value).commit()
    }

    fun clearAllPref()
    {
        editor.clear().apply()
    }

    fun fetchStringValue(key:String):String
    {
        return pref.getString(key,"").toString()
    }

    fun saveBooleanValue(key: String, value: Boolean) {
        editor.putBoolean(key, value).commit()
    }

    fun fetchBooleanValue(key:String):Boolean
    {
        return pref.getBoolean(key,false)
    }

    fun saveIntValue(key: String, value: Int) {
        editor.putInt(key, value).commit()
    }

    fun fetchIntValue(key:String):Int
    {
        return pref.getInt(key,0).toInt()
    }

    fun saveLongValue(key: String, value: Long) {
        editor.putLong(key, value).commit()
    }

    fun fetchLongValue(key:String):Long
    {
        return pref.getLong(key,0)
    }

    fun saveFloatValue(key: String, value: Float) {
        editor.putFloat(key, value).commit()
    }

    fun fetchFloatValue(key:String):Float
    {
        return pref.getFloat(key,0f)
    }

    fun fetchUserData(): LoginUserData {
        var user_all_data = pref.getString(Constants.USER_ALL_DATA, "")
        if(user_all_data.equals("")){
            return LoginUserData()
        }else
        {
            return Gson().fromJson(user_all_data, LoginUserData::class.java)
        }
    }
}

@Singleton
class A @Inject constructor(   ){
    var a=122;
    var d=212;
}