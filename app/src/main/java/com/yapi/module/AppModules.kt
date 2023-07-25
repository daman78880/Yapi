package com.yapi.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModules {

    @Provides
    @Singleton
    fun getPrefData(@ApplicationContext context: Context):SharedPreferences{
     return context.getSharedPreferences("pref",Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun getEditor(pref:SharedPreferences):SharedPreferences.Editor
    {
     return  pref.edit()
    }

    @Provides
    @Singleton
    @Named("abed")
    fun getDataValue():String
    {
        return "Hello Sir"
    }

    @Provides
    @Singleton
    fun checkUserStatus():Int
    {
        return 20
    }
}