package com.yapi

import android.util.Log
import androidx.lifecycle.ViewModel
import com.yapi.common.*
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DataViewModel @Inject constructor(
    @Named("abed") private var nameValue: String,
    private var checkValue:Int,
    private var repository: Repository,
) : ViewModel() {
    init {
        Log.e("wdwjdhjhdwhjdw===",checkValue.toString())

    }

    fun getCategoryMethod() {
        var token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNjNjZTIzMTFhNjdjNzA4NTc0MzlmNTQzIiwiaWF0IjoxNjc1MDYxMjUxfQ.-XFkdhmagEV-4V31HWuQ39nK2rsfmVaNUceoA5Zlyrw"

        repository.makeCall(false,
            requestProcessor = object : ApiProcessor<Response<GetCategorryResponse>> {
                override fun onSuccess(success: Response<GetCategorryResponse>) {
                    Log.e("Resposne_Dataaaa===", success.body().toString())
                }

                override fun onError(message: String) {
                    MainActivity.activity!!.get()!!.showMessage(message)
                }

                override suspend fun sendRequest(retrofitApi: RetrofitAPI): Response<GetCategorryResponse> {
                     return retrofitApi.getCategory(token,"30.8987", "76.7179", "1", "20", "")
                }
            })
    }
}