package com.yapi.common

interface ApiProcessor<T> {
    fun onSuccess(success:T)
    fun onError(message:String)
    suspend fun sendRequest(retrofitApi: RetrofitAPI): T
}